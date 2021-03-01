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
		assertEquals("zweiter wert", messages.anotherkey(Locale.GERMAN));
	}

	@Test
	void testWithOneArgument() {
		assertEquals("Hello World", messages.keyWithDots(Locale.ENGLISH, "World"));
		assertEquals("Hallo Welt", messages.keyWithDots(Locale.GERMAN, "Welt"));
	}

	@Test
	void testWithThreeArgument() {
		assertEquals("one sometext two three", messages.keyWithUnderlines(Locale.ENGLISH, "one", "two", "three"));
		assertEquals("eins ein text zwei drei", messages.keyWithUnderlines(Locale.GERMAN, "eins", "zwei", "drei"));
	}

}
