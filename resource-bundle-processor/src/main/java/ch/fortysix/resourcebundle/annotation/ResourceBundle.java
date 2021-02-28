package ch.fortysix.resourcebundle.annotation;

import static ch.fortysix.resourcebundle.annotation.ResourceBundleImplementationType.JDK;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface ResourceBundle {
	String bundle();

	ResourceBundleImplementationType type() default JDK;

	String suffix() default "Bundle";
}