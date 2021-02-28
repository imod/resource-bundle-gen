package ch.fortysix.messagebundle.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import ch.fortysix.messagebundle.test.example.ApplicationMessages;

class ApplicationMessagesTest {

	ApplicationMessages messages = new ApplicationMessages();

	@Test
	void testNoArgument() {
		assertEquals("secondvalue", messages.anotherkey(Locale.ENGLISH));
	}

	@Test
	void testWithOneArgument() {
		assertEquals("Hello World", messages.keyWithDots(Locale.ENGLISH, "World"));
	}

	@Test
	void testWithThreeArgument() {
		assertEquals("one sometext two three", messages.keyWithUnderlines(Locale.ENGLISH, "one", "two", "three"));
	}

}
