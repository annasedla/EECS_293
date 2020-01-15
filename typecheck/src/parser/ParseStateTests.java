package parser;

import org.junit.Test;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import java.util.Arrays;
import java.util.List;


/**
 * java class ParseStateTests
 * The set of unit tests for the ParseState class
 * @author Fangze Liu, Jacob Goldberg, Anna Sedlackova
 *
 */
class ParseStateTests {
	//test that the fail state has all required attributes
	@Test
	void testFailState() {
		assertFalse(ParseState.FAILURE.getSuccess());
		assertNull(ParseState.FAILURE.getRemainder());
		assertNull(ParseState.FAILURE.getNode());
	}

	//test that we can't modify the remainder or the node and have it alter the ParseState
	@Test
	void testReturnsCopies() {
		Node firstNode = LeafNode.build(Connector.build(TerminalSymbol.TIMES));
		List<Token> remainder = Arrays.asList(Connector.build(TerminalSymbol.MINUS));
		
		ParseState testState = ParseState.build(firstNode,  remainder);
		
		assertFalse(testState.getRemainder() == remainder);
		
		remainder.set(0, Connector.build(TerminalSymbol.PLUS));
		assertEquals(testState.getRemainder().get(0), Connector.build(TerminalSymbol.MINUS));
	}
	
	//test that ParseState::build() rejects null arguments
	@Test(expected = NullPointerException.class)
	void testRejectsNullArguments() {
	    ParseState.build(null, ListHandler.createEmptyList());
	    ParseState.build(LeafNode.build(Connector.build(TerminalSymbol.TIMES)), null);
	}
	
	//test that ParseState::hasNoRemainder() works
	@Test
	void testHasNoRemainder() {
		assertTrue(ParseState.build(LeafNode.build(Connector.build(TerminalSymbol.TIMES)), ListHandler.createEmptyList()).hasNoRemainder());
		assertFalse(ParseState.build(LeafNode.build(Connector.build(TerminalSymbol.TIMES)), Arrays.asList(Connector.build(TerminalSymbol.TIMES))).hasNoRemainder());
	}
}
