package parser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

/**
 * java class TerminalSymbolTests
 * The set of unit tests for the TerminalSymbol class
 * @author Fangze Liu, Jacob Goldberg, Anna Sedlackova
 *
 */
class TerminalSymbolTests {
	//test that TerminalSymbol::parse rejects null values
	@Test
	void testRejectsNull() {
		assertEquals(TerminalSymbol.TIMES.parse(null), ParseState.FAILURE);
	}

	//test that TerminalSymbol::parse rejects empty lists
	@Test
	void testRejectsEmpty() {
		assertEquals(TerminalSymbol.TIMES.parse(ListHandler.createEmptyList()), ParseState.FAILURE);
	}
	
	//test that TerminalSymbol::parse rejects a mismatch between the first element in the list and the symbol
	@Test
	void testRejectsDifferentSymbol() {
		assertEquals(TerminalSymbol.TIMES.parse(Arrays.asList(Connector.build(TerminalSymbol.PLUS))), ParseState.FAILURE);
		assertEquals(TerminalSymbol.TIMES.parse(Arrays.asList(Variable.build("var"))), ParseState.FAILURE);
	}
	
	//test that TerminalSymbol::parse given a one-item list that matches the symbol returns a ParseState with no remainder and the first symbol being the same as the original terminal symbol
	@Test
	void testOnOneItemList() {
		ParseState testState = TerminalSymbol.TIMES.parse(Arrays.asList(Connector.build(TerminalSymbol.TIMES)));
		
		assertTrue(testState.hasNoRemainder());
		assertTrue(testState.getRemainder().isEmpty());
		assertTrue(testState.getNode().toList().get(0).matches(TerminalSymbol.TIMES));
	}
	
	//test that TerminalSymbol::parse works on a multi-item list
	@Test
	void testOnMultiItemList() {
		ParseState testState = TerminalSymbol.TIMES.parse(Arrays.asList(Connector.build(TerminalSymbol.TIMES), Variable.build("var")));
		
		assertFalse(testState.hasNoRemainder());
		assertEquals(testState.getRemainder().size(), 1);
		assertTrue(testState.getNode().toList().get(0).matches(TerminalSymbol.TIMES));
		assertTrue(testState.getRemainder().get(0).matches(TerminalSymbol.VARIABLE));
	}
}
