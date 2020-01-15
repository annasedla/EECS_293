package channels;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class InputStreamAnalyzerTest {
	
	ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	
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
	public void checkValidLineAndParseEmptyString() {
		InputStreamAnalyzer ISA = new InputStreamAnalyzer("1", "2");
		assertFalse(ISA.checkValidLineAndParse(""));
	}
	
	@Test
	public void checkValidLineAndParseNominal() {
		InputStreamAnalyzer ISA = new InputStreamAnalyzer("1", "2");
		ISA.checkValidLineAndParse("TO1");
		ISA.checkValidLineAndParse("TO1");
		assertTrue(ISA.checkValidLineAndParse("THISIS2"));
	}
	
	@Test
	public void checkRepCountNoRep() {
		InputStreamAnalyzer ISA = new InputStreamAnalyzer("123", "45");
		InputStreamAnalyzer.TestHook th = ISA.new TestHook();
		ISA.checkValidLineAndParse("TO1");
		ISA.checkValidLineAndParse("REP2");
		th.checkREPCount("TO1");
		assertEquals(0, th.getREPCount());
	}
	
	@Test
	public void checkRepCountContainsRep() {
		InputStreamAnalyzer ISA = new InputStreamAnalyzer("123", "45");
		InputStreamAnalyzer.TestHook th = ISA.new TestHook();
		ISA.checkValidLineAndParse("TO1");
		th.checkREPCount("REP2");
		assertEquals(1, th.getREPCount());
	}
	
	@Test
	public void checkRepCountOverLimit() {
		InputStreamAnalyzer ISA = new InputStreamAnalyzer("123", "45");
		InputStreamAnalyzer.TestHook th = ISA.new TestHook();
		ISA.checkValidLineAndParse("TO1");
		ISA.checkValidLineAndParse("REP2");
		ISA.checkValidLineAndParse("REP3");
		th.checkREPCount("REP3");
		assertEquals("REPBAD", th.getParseData()[1]);
	}
	
	@Test
	public void testParseInputLineNominal() {
		InputStreamAnalyzer ISA = new InputStreamAnalyzer("1", "2");
		InputStreamAnalyzer.TestHook th = ISA.new TestHook();
		ISA.checkValidLineAndParse("TO1");
		ISA.checkValidLineAndParse("TO1");
		assertTrue(th.parseInputLine("THISIS2"));
	}
	
	@Test
	public void testParseInputLineTooManyTOs() {
		InputStreamAnalyzer ISA = new InputStreamAnalyzer("1", "2");
		ISA.checkValidLineAndParse("TO1");
		ISA.checkValidLineAndParse("TO1");
		ISA.checkValidLineAndParse("TO1");
		ISA.checkValidLineAndParse("TO1");
		ISA.checkValidLineAndParse("TO1");
		ISA.checkValidLineAndParse("TO1");
		ISA.checkValidLineAndParse("TO1");
		ISA.checkValidLineAndParse("TO1");
		ISA.checkValidLineAndParse("TO1");
		ISA.checkValidLineAndParse("TO1");
		ISA.checkValidLineAndParse("TO1");
		assertEquals("note: the calling sequence would be valid if it had less lines reading TO1\n", outContent.toString());
	}
	
	@Test
	public void testParseInputLineDoesntEqualGoalData() {
		InputStreamAnalyzer ISA = new InputStreamAnalyzer("1", "2");
		InputStreamAnalyzer.TestHook th = ISA.new TestHook();
		assertFalse(th.parseInputLine("TO3"));
	}
	
	@Test
	public void testParseInputLineDataNotNull() {
		InputStreamAnalyzer ISA = new InputStreamAnalyzer("1", "2");
		InputStreamAnalyzer.TestHook th = ISA.new TestHook();
		ISA.checkValidLineAndParse("TO1");
		assertFalse(th.parseInputLine("TO1"));
	}
	
	@Test
	public void testParseInputLineOrderNotFollowed() {
		InputStreamAnalyzer ISA = new InputStreamAnalyzer("1", "2");
		InputStreamAnalyzer.TestHook th = ISA.new TestHook();
		assertFalse(th.parseInputLine("THISIS2"));
	}
	
	@Test
	public void testFillGoalDataNominal() {
		// Constructor calls fillGoalData
		InputStreamAnalyzer ISA = new InputStreamAnalyzer("1", "2");
		InputStreamAnalyzer.TestHook th = ISA.new TestHook();
		assertArrayEquals(new String[] {"TO1", "TO1", "THISIS2"}, th.getGoalData());
	}

	@Test
	public void testFillReceiverAndLocalEmpty() {
		InputStreamAnalyzer ISA = new InputStreamAnalyzer("", "");
		InputStreamAnalyzer.TestHook th = ISA.new TestHook();
		assertArrayEquals(new String[0], th.getGoalData());
	}
	
	@Test
	public void testFillReceiverAndLocalNominal() {
		// Receiver and Local length >2 ensures else case is satisfied
		InputStreamAnalyzer ISA = new InputStreamAnalyzer("12", "23");
		InputStreamAnalyzer.TestHook th = ISA.new TestHook();
		assertArrayEquals(new String[] {"TO1", "REP2", "TO1", "REP2", "THISIS2", "REP3"}, th.getGoalData());
	}

	@Test
	public void testIsArrayFullDoArraysEqualEmptyParser() {
		InputStreamAnalyzer ISA = new InputStreamAnalyzer("", "");
		InputStreamAnalyzer.TestHook th = ISA.new TestHook();
		assertTrue(th.isArrayFullDoArraysEqual());
	}

	@Test
	public void testIsArrayFullDoArrayEqualNotFull() {
		InputStreamAnalyzer ISA = new InputStreamAnalyzer("1", "2");
		InputStreamAnalyzer.TestHook th = ISA.new TestHook();
		assertFalse(th.isArrayFullDoArraysEqual());
	}

	@Test
	public void testIsArrayFullDoArraysEqualFull() {
		InputStreamAnalyzer ISA = new InputStreamAnalyzer("1", "2");
		InputStreamAnalyzer.TestHook th = ISA.new TestHook();
		ISA.checkValidLineAndParse("TO1");
		ISA.checkValidLineAndParse("TO1");
		ISA.checkValidLineAndParse("THISIS2");
		assertTrue(th.isArrayFullDoArraysEqual());
	}
	
	@Test
	public void testIsArrayFullDoArraysNotEqual() {
		InputStreamAnalyzer ISA = new InputStreamAnalyzer("123", "2");
		InputStreamAnalyzer.TestHook th = ISA.new TestHook();
		ISA.checkValidLineAndParse("TO1");
		ISA.checkValidLineAndParse("REP2");
		ISA.checkValidLineAndParse("REP3");
		ISA.checkValidLineAndParse("TO1");
		ISA.checkValidLineAndParse("REP2");
		ISA.checkValidLineAndParse("REP3");
		ISA.checkValidLineAndParse("REP3");
		ISA.checkValidLineAndParse("THISIS2");
		assertFalse(th.isArrayFullDoArraysEqual());		
	}
	
	@Test
	public void testFollowsOrderEquals0() {
		InputStreamAnalyzer ISA = new InputStreamAnalyzer("1", "2");
		InputStreamAnalyzer.TestHook th = ISA.new TestHook();	
		assertTrue(th.followsOrder(0));
	}
	
	public void testFollowsOrderPrevNotNull() {
		InputStreamAnalyzer ISA = new InputStreamAnalyzer("1", "2");
		InputStreamAnalyzer.TestHook th = ISA.new TestHook();
		ISA.checkValidLineAndParse("TO1");
		assertTrue(th.followsOrder(1));
	}
	
	@Test
	public void testFollowsOrderPrevNull() {
		InputStreamAnalyzer ISA = new InputStreamAnalyzer("1", "2");
		InputStreamAnalyzer.TestHook th = ISA.new TestHook();
		assertFalse(th.followsOrder(1));
		
	}
	
	@Test
	public void testCheckTOCountEquals10() {
		InputStreamAnalyzer ISA = new InputStreamAnalyzer("1", "2");
		InputStreamAnalyzer.TestHook th = ISA.new TestHook();
		ISA.checkValidLineAndParse("TO1");
		ISA.checkValidLineAndParse("TO1");
		ISA.checkValidLineAndParse("TO1");
		ISA.checkValidLineAndParse("TO1");
		ISA.checkValidLineAndParse("TO1");
		ISA.checkValidLineAndParse("TO1");
		ISA.checkValidLineAndParse("TO1");
		ISA.checkValidLineAndParse("TO1");
		ISA.checkValidLineAndParse("TO1");
		ISA.checkValidLineAndParse("TO1");
		ISA.checkValidLineAndParse("TO1");
		assertEquals("note: the calling sequence would be valid if it had less lines reading TO1\n", outContent.toString());
	}
	
	@Test
	public void testCheckTOCountIncrement() {
		InputStreamAnalyzer ISA = new InputStreamAnalyzer("1", "2");
		InputStreamAnalyzer.TestHook th = ISA.new TestHook();
		int prevCount = th.getTOCount();
		th.checkTOCount("TO1");
		assertEquals(prevCount+1, th.getTOCount());
	}
	
	@Test
	public void testCheckTOReset() {
		InputStreamAnalyzer ISA = new InputStreamAnalyzer("12", "2");
		InputStreamAnalyzer.TestHook th = ISA.new TestHook();
		th.checkTOCount("TO1");
		th.checkTOCount("TO1");
		th.checkTOCount("TO1");
		th.checkTOCount("REP2");
		assertEquals(0, th.getTOCount());
	}
}
