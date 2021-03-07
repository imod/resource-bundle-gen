package ch.fortysix.resourcebundle.annotation;

import static ch.fortysix.resourcebundle.annotation.ResourceBundleImplementationType.JDK;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface ResourceBundle {
	/**
	 * The name of the resource bundle. If the (properties) files are located within
	 * a package, you need to define the whole path to the file, but without the
	 * extension. e.g. <code>ch.fortysix.test.mybundle</code>
	 */
	String bundle();

	/**
	 * The type of implementation that should be generated.
	 */
	ResourceBundleImplementationType type() default JDK;

	/**
	 * The suffix to be added to the generated class. Base name is the name of the
	 * class this annotation is set on.
	 */
	String suffix() default "Bundle";

	/**
	 * Per default, the default bundle file will be used to determine all the
	 * available keys and messages. If the default bundle does not hold all the
	 * messages which should be made available within the final class, you can add
	 * all the files which should be used to find keys and messages here.
	 */
	String[] filesToAnalyse() default {};
}