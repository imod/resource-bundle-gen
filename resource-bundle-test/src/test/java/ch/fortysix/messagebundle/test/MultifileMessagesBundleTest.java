package ch.fortysix.messagebundle.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import ch.fortysix.messagebundle.test.example.MultifileMessagesBundle;

class MultifileMessagesBundleTest {

	MultifileMessagesBundle bundle = new MultifileMessagesBundle();

	@Test
	void testNoArgument() {
		assertEquals("dummyvalue", bundle.getDummy(Locale.ENGLISH));
	}

	@Test
	void testWithOneArgument() {
		assertEquals("deutscher wert Welt", bundle.getMessageFromDeFile(Locale.GERMAN, "Welt"));
		assertEquals("english value World", bundle.getMessageFromEnFile(Locale.ENGLISH, "World"));
	}

	@Test
	void testMissingMessageResource() {
		assertEquals("[message.from.de.file]", bundle.getMessageFromDeFile(Locale.ENGLISH, "Welt"));
	}

}
