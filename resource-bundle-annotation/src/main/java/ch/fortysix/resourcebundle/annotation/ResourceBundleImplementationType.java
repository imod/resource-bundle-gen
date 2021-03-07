package ch.fortysix.resourcebundle.annotation;

public enum ResourceBundleImplementationType {

	/**
	 * Generates the implementation based on the Java JDK only, no other
	 * dependencies are needed at runtime. Note: this implementation can not ensure
	 * the order of the arguments passed to <code>MessageFormat</code> is correct.
	 */
	JDK,
	/**
	 * Generates the implementation based on apache commons-text, supporting named
	 * arguments within the message. If you choose this type, you also need to add
	 * <code>org.apache.commons:commons-text</code> as a dependency to your project.
	 */
	COMMONS_TEXT
}
