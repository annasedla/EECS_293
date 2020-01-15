package channels;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;

public class ChannelsTest {
	
	ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	Channels.TestHook th = new Channels().new TestHook();

	// Redirecting output to outContent
	@Before
	public void streamsForPrintTests() {
		System.setOut(new PrintStream(outContent));
	}
	
	// Resetting System.in and System.out
	@After
	public void resetSystemOut() {
		System.setOut(System.out);
		System.setIn(System.in);
	}
	
	@Test
	public void testMainValidFilename() {
		Channels.main(new String[] {});
		assertEquals("Beginning reading from input...\ntrue\n", outContent.toString());
	}
	
	@Test
	public void testMakeFileReaderNominal() {
		// Should fail, no way to check correctness of fileReader
		assertTrue(th.makeFileReader(System.getProperty("user.dir") + "/src/channels/data.txt") instanceof FileReader);
		
	}
	
	@Test
	public void testMakeFileReaderFails() {
		assertNull(th.makeFileReader(""));
	}
	
	@Test
	public void testIsValidReceiverAndLocalNominal() {
		assertTrue(th.isValidReceiverAndLocal("123", "45"));
	}
	
	@Test
	public void testIsValidReceiverAndLocalInvalidReceiver() {
		assertFalse(th.isValidReceiverAndLocal("123A", "45"));
	}
	
	@Test
	public void testIsValidReceiverAndLocalNullReceiver() {
		assertFalse(th.isValidReceiverAndLocal(null, "45"));
	}
	
	@Test
	public void testIsValidReceiverAndLocalInvalidLocal() {
		assertFalse(th.isValidReceiverAndLocal("123", "45A"));
	}
	
	@Test
	public void testIsValidReceiverAndLocalNullLocal() {
		assertFalse(th.isValidReceiverAndLocal("123", null));
	}
	
	@Test
	public void testisExpectedLineEmptyLine() {
		assertTrue(th.isExpectedLine("", "1", "2"));
	}
	
	@Test
	public void testisExpectedLineReceiverLength1() {
		assertTrue(th.isExpectedLine("TO1", "1", "12"));
	}
	
	@Test
	public void testisExpectedLineLocalLength1() {
		assertTrue(th.isExpectedLine("THISIS2", "12", "2"));
	}
	
	@Test
	public void testisExpectedLineNominal() {
		assertTrue(th.isExpectedLine("REP2", "12", "23"));
	}
	
	@Test
	public void testTryToConnectNominal() {
		String input = "1\n1\nTO 1\nTO 1\nTHISIS 1\n";
		Reader inputReader = new StringReader(input);
		BufferedReader reader = new BufferedReader(inputReader);
		th.tryToConnect("1", "1", reader);
		assertEquals("true\n", outContent.toString());
	}
	
	@Test
	public void testTryToConnectReadlineFails() {		
		String input = "1\n1\nTO 1\nTO 1\nTHISIS 1\n";
		Reader inputReader = new StringReader(input);
		BufferedReader reader = new BufferedReader(inputReader);
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		th.tryToConnect("1", "1", reader);
		assertEquals("false\nError reading from the channel file - no input.\n", outContent.toString());
	}
	
	@Test
	public void testTryToConnectReadLineNull() {
		String input = "";
		Reader inputReader = new StringReader(input);
		BufferedReader reader = new BufferedReader(inputReader);
		th.tryToConnect("1", "1", reader);
		assertEquals("false\nReached the end of the input stream and could not establish connection.\n", outContent.toString());
	}
	
	@Test
	public void testTryToConnectUnexpectedLine() {
		String input = "1\n2\nTO2\n";
		Reader inputReader = new StringReader(input);
		BufferedReader reader = new BufferedReader(inputReader);
		th.tryToConnect("1", "2", reader);
		assertEquals("false\nReceiver or local does not match provided receiver or local\n", outContent.toString());
	}
	
	@Test
	public void testTryToConnectNotEnoughToConnect() {
		String input = "1\n\2nTO1\nTHISIS2\n";
		Reader inputReader = new StringReader(input);
		BufferedReader reader = new BufferedReader(inputReader);
		th.tryToConnect("1", "2", reader);
		assertEquals("false\nReached the end of the input stream and could not establish connection.\n", outContent.toString());
	}
	
	@Test
	public void testReadInputFromFileNominal() {
		String input = "1\n2\nTO 1\nTO 1\nTHISIS 2\n";
		Reader inputReader = new StringReader(input);
		BufferedReader reader = new BufferedReader(inputReader);
		th.readInputFromFile(reader);
		assertEquals("true\n", outContent.toString());
	}
	
	@Test
	public void testReadInputFromFileReadlineFails() {
		String input = "1\n1\nTO 1\nTO 1\nTHISIS 1\n";
		Reader inputReader = new StringReader(input);
		BufferedReader reader = new BufferedReader(inputReader);
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		th.readInputFromFile(reader);
		assertEquals("false\nError reading from the channel file - no input.\n", outContent.toString());
	}
	
	@Test
	public void testReadInputFromFileInvalidReceiverOrLocal() {
		String input = "1a\n1b\nTO 1\nTO 1\nTHISIS 1\n";
		Reader inputReader = new StringReader(input);
		BufferedReader reader = new BufferedReader(inputReader);
		th.readInputFromFile(reader);
		assertEquals("false\nNot a valid receiver address\n", outContent.toString());
	}
	
	@Test
	public void testFilterWrongCharacterValidTo() {
		assertEquals("TO1", th.filterWrongCharacters("to 1"));
	}
	
	@Test
	public void testFilterWrongCharacterValidRep() {
		assertEquals("REP2", th.filterWrongCharacters("REP 2"));
	}
	
	@Test
	public void testFilterWrongCharacterValidThisis() {
		assertEquals("THISIS3", th.filterWrongCharacters("ThIsIs 3"));
	}
	
	@Test
	public void testFilterWrongCharacterInvalid() {
		assertEquals("", th.filterWrongCharacters("GARBLED"));
	}
	
	@Test
	public void testContainsValidToValidTo() {
		assertTrue(th.containsValidTO("TO3"));
	}
	
	@Test
	public void testContainsValidToInvalidTo() {
		assertFalse(th.containsValidTO("TO23"));
	}
	
	@Test
	public void testContainsValidRepValidRep() {
		assertTrue(th.containsValidREP("REP3"));
	}
	
	@Test
	public void testContainsValidRepInvalidRep() {
		assertFalse(th.containsValidREP("REP23"));
	}
	
	@Test
	public void testContainsValidThisisValidThisis() {
		assertTrue(th.containsValidTHISIS("THISIS3"));
	}

	@Test
	public void testContainsValidThisisInvalidThisis() {
		assertFalse(th.containsValidTHISIS("THISIS23"));
	}	
}
