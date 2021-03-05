package ch.fortysix.resourcebundle.processor;

import static ch.fortysix.resourcebundle.annotation.ResourceBundleImplementationType.COMMONS_TEXT;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.text.CaseUtils;

import com.google.auto.service.AutoService;

import ch.fortysix.resourcebundle.annotation.ResourceBundle;
import ch.fortysix.resourcebundle.annotation.ResourceBundleImplementationType;

@SupportedAnnotationTypes("ch.fortysix.resourcebundle.annotation.ResourceBundle")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class ResourceBundleProcessor extends AbstractProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

		for (TypeElement annotation : annotations) {

			Set<? extends Element> bundleClasses = roundEnv.getElementsAnnotatedWith(annotation);

			for (Element bundleClassElement : bundleClasses) {

				Messager messager = this.processingEnv.getMessager();

				TypeElement bundleClass = (TypeElement) bundleClassElement;

				ResourceBundle bundleAnnotation = bundleClass.getAnnotation(ResourceBundle.class);
				ResourceBundleImplementationType type = bundleAnnotation.type();

				try {
					messager.printMessage(Kind.NOTE, "generating Message class for " + bundleAnnotation.bundle(),
							bundleClass);
					// load the bundle/properties file
					Properties props = loadBundleMessages(bundleAnnotation);
					Set<Entry<Object, Object>> propertiesEntries = props.entrySet();

					Map<String, List<String>> methodDefinitions = new HashMap<>();

					for (Entry<Object, Object> entry : propertiesEntries) {
						List<String> arguments = getArguments((String) entry.getValue());
						methodDefinitions.put(String.valueOf(entry.getKey()), arguments);
					}

					writeMessagesJavaFile(type, bundleAnnotation.bundle(), bundleClass.getQualifiedName().toString(),
							bundleAnnotation.suffix(), methodDefinitions);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}

		}

		return true;
	}

	private Properties loadBundleMessages(ResourceBundle bundleAnnotation) throws IOException {
		Properties props = new Properties();

		String[] filesToAnalyse = bundleAnnotation.filesToAnalyse();
		if (ArrayUtils.isNotEmpty(filesToAnalyse)) {
			for (String bundleFileName : filesToAnalyse) {
				loadProperties(props, bundleFileName);
			}
		} else {
			String bundleName = bundleAnnotation.bundle();

			// build the file name of the bundle name, we currently only support properties
			// files
			String bundleFileName = bundleName.replace(".properties", "").replace('.', '/').concat(".properties");
			loadProperties(props, bundleFileName);
		}
		return props;
	}

	private void loadProperties(Properties props, String bundleFileName) throws IOException {
		FileObject fileObject = this.processingEnv.getFiler().getResource(StandardLocation.CLASS_OUTPUT, "",
				bundleFileName);
		try (InputStream inputStream = fileObject.openInputStream()) {
			props.load(inputStream);
		}
	}

	private void writeMessagesJavaFile(ResourceBundleImplementationType type, String bundleName, String className,
			String suffix, Map<String, List<String>> methodDefinitions) throws IOException {

		String packageName = null;
		int lastDot = className.lastIndexOf('.');
		if (lastDot > 0) {
			packageName = className.substring(0, lastDot);
		}

		String fullQualifiedClassName = className + suffix;
		String simpleClassName = fullQualifiedClassName.substring(lastDot + 1);

		JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(fullQualifiedClassName);

		try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {

			if (packageName != null) {
				out.print("package ");
				out.print(packageName);
				out.println(";");
				out.println();
			}

			out.println("import java.text.MessageFormat;");
			out.println("import java.util.HashMap;");
			out.println("import java.util.Locale;");
			out.println("import java.util.Map;");
			out.println("import java.util.MissingResourceException;");
			out.println("import java.util.ResourceBundle;");
			out.println("import java.util.concurrent.ConcurrentHashMap;");
			out.println();
			if (COMMONS_TEXT.equals(type)) {
				out.println("import org.apache.commons.text.StringSubstitutor;");
			}
			out.println();
			out.print("public class ");
			out.print(simpleClassName);
			out.println(" {");
			out.println();

			out.println("	private static final class Holder {");
			out.println("		private static final String BUNDLE_NAME = \"" + bundleName + "\";");
			out.println("		private Map<Locale, ResourceBundle> bundles = new ConcurrentHashMap<>();");
			out.println();
			out.println("		public ResourceBundle getBundle(Locale locale) {");
			out.println(
					"			return bundles.computeIfAbsent(locale, l -> ResourceBundle.getBundle(BUNDLE_NAME, l));");
			out.println("		}");
			out.println("	}");

			out.println();
			out.println("	private Holder holder = new Holder();");
			out.println();

			methodDefinitions.forEach((key, arguments) -> {

				if (COMMONS_TEXT.equals(type)) {
					commonsTextMethod(out, key, arguments);
				} else {
					javaLangMethod(out, key, arguments);
				}

			});

			out.println("}");
		}

	}

	private void commonsTextMethod(PrintWriter out, String key, List<String> arguments) {
		String methodName = RegExUtils.removeAll(CaseUtils.toCamelCase(key, false, '_', '.'), "([\\.|_])");

		out.print("    public String ");
		out.print(methodName);

		out.print("(");
		out.print("Locale locale");
		Iterator<String> argsIterator = arguments.iterator();
		while (argsIterator.hasNext()) {
			String arg = argsIterator.next();
			out.print(", Object ");
			out.print(arg);
		}
		out.println(") {");

		if (arguments.size() == 0) {
			out.println("		return holder.getBundle(locale).getString(\"" + key + "\");");
			out.println("    }");
			out.println();
		} else {
			out.println("		try {");
			out.println("			String messagePattern = holder.getBundle(locale).getString(\"" + key + "\");");
			out.println("			Map<String, Object> values = new HashMap<>();");
			for (String arg : arguments) {
				out.println("			values.put(\"" + arg + "\", " + arg + ");");
			}
			out.println("			StringSubstitutor sub = new StringSubstitutor(values, \"{\", \"}\");");
			out.println("			return sub.replace(messagePattern);");
			out.println("		} catch (MissingResourceException e) {");
			out.println("			return \"[" + key + "]\";");
			out.println("		}");
			out.println("    }");
			out.println();
		}
	}

	private void javaLangMethod(PrintWriter out, String key, List<String> arguments) {
		// build the method name out of the message key
		String methodName = RegExUtils.removeAll(CaseUtils.toCamelCase(key, false, '_', '.'), "([\\.|_])");

		out.print("    public String ");
		out.print(methodName);

		out.print("(");
		out.print("Locale locale");
		Iterator<String> argsIterator = arguments.iterator();
		while (argsIterator.hasNext()) {
			String arg = argsIterator.next();
			out.print(", Object ");
			out.print(arg);
		}
		out.println(") {");

		if (arguments.size() == 0) {
			out.println("		return holder.getBundle(locale).getString(\"" + key + "\");");
			out.println("    }");
			out.println();
		} else {
			out.println("		try {");
			out.println("			String messagePattern = holder.getBundle(locale).getString(\"" + key + "\");");
			out.println("			return MessageFormat.format(messagePattern, " + buildArgList(arguments) + ");");
			out.println("		} catch (MissingResourceException e) {");
			out.println("			return \"[" + key + "]\";");
			out.println("		}");
			out.println("    }");
			out.println();
		}
	}

	private List<String> getArguments(String input) {

		// 1. try to count how many arguments with the pattern {N} are in the value
		int nrOfArguments = (input.split("(\\{\\d\\})", -1).length - 1);
		if (nrOfArguments > 0) {
			return IntStream.rangeClosed(1, nrOfArguments).mapToObj(i -> "arg" + (i-1)).collect(Collectors.toList());
		}

		// 2. try to resolve argument names following the pattern {name} are in the
		// value
		List<String> arguments = new ArrayList<>();
		Pattern argPattern = Pattern.compile("\\{(\\w+)\\}");
		Matcher matcher = argPattern.matcher(input);
		while (matcher.find()) {
			arguments.add(matcher.group(1));
		}

		return arguments;
	}

	private String buildArgList(List<String> arguments) {
		return arguments.stream().collect(Collectors.joining(", "));
	}

}