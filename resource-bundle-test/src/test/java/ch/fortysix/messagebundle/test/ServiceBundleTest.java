package ch.fortysix.messagebundle.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import ch.fortysix.messagebundle.test.example.ServiceBundle;

class ServiceBundleTest {

	ServiceBundle bundle = new ServiceBundle();

	@Test
	void testWithTwoArgument() {
		assertEquals("By Hans Muster", bundle.sayBy(Locale.ENGLISH, "Hans", "Muster"));
		assertEquals("Auf wiedersehen Hans Muster", bundle.sayBy(Locale.GERMAN, "Hans", "Muster"));
	}

	@Test
	void testWithOneArgument() {
		assertEquals("Hello Hans", bundle.sayHi(Locale.ENGLISH, "Hans"));
		assertEquals("Hallo Erich", bundle.sayHi(Locale.GERMAN, "Erich"));
	}

	@Test
	void testWithNoArgument() {
		assertEquals("somevalue", bundle.somekey(Locale.ENGLISH));
		assertEquals("einwert", bundle.somekey(Locale.GERMAN));
	}

}
