package ch.fortysix.messagebundle.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import ch.fortysix.messagebundle.test.example.ServiceBundle;

class ServiceBundleTest {

	ServiceBundle bundle = new ServiceBundle();

	@Test
	void testWithTwoArgument() {
		assertEquals("Hello Hans Muster", bundle.sayBy(Locale.ENGLISH, "Hans", "Muster"));
	}

	@Test
	void testWithOneArgument() {
		assertEquals("Hello Hans", bundle.sayHi(Locale.ENGLISH, "Hans"));
	}

	@Test
	void testWithNoArgument() {
		assertEquals("somevalue", bundle.somekey(Locale.ENGLISH));
	}

}
