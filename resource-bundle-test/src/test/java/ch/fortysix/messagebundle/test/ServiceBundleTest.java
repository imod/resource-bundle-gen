package ch.fortysix.messagebundle.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import ch.fortysix.messagebundle.test.example.ServiceBundle;

class ServiceBundleTest {

	ServiceBundle bundle = new ServiceBundle();

	@Test
	void testWithTwoArgument() {
		assertEquals("By Hans Muster", bundle.getSayBy(Locale.ENGLISH, "Hans", "Muster"));
		assertEquals("Auf wiedersehen Hans Muster", bundle.getSayBy(Locale.GERMAN, "Hans", "Muster"));
	}

	@Test
	void testWithOneArgument() {
		assertEquals("Hello Hans", bundle.getSayHi(Locale.ENGLISH, "Hans"));
		assertEquals("Hallo Erich", bundle.getSayHi(Locale.GERMAN, "Erich"));
	}

	@Test
	void testWithNoArgument() {
		assertEquals("somevalue", bundle.getSomekey(Locale.ENGLISH));
		assertEquals("einwert", bundle.getSomekey(Locale.GERMAN));
	}

	@Test
	void javaKeyWordsMustWorkAsKeyAndParam() {
		assertEquals("synchronized is a java key word", bundle.getSynchronized(Locale.ENGLISH));
		assertEquals("native is a java key word, class is one too", bundle.getNative(Locale.ENGLISH, "class"));
	}
}
