package ch.fortysix.messagebundle.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import ch.fortysix.messagebundle.test.example.ApplicationMessages;

class ApplicationMessagesTest {

	ApplicationMessages messages = new ApplicationMessages();

	@Test
	void testNoArgument() {
		assertEquals("secondvalue", messages.getAnotherkey(Locale.ENGLISH));
		assertEquals("zweiter wert", messages.getAnotherkey(Locale.GERMAN));
	}

	@Test
	void testWithOneArgument() {
		assertEquals("Hello World", messages.getKeyWithDots(Locale.ENGLISH, "World"));
		assertEquals("Hallo Welt", messages.getKeyWithDots(Locale.GERMAN, "Welt"));
	}

	@Test
	void testWithThreeArgument() {
		assertEquals("one sometext two three", messages.getKeyWithUnderlines(Locale.ENGLISH, "one", "two", "three"));
		assertEquals("eins ein text zwei drei", messages.getKeyWithUnderlines(Locale.GERMAN, "eins", "zwei", "drei"));
	}

}
