package parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class SymbolSequenceTest {
	private final List<Symbol> timesPlusList = Arrays.asList(TerminalSymbol.TIMES, TerminalSymbol.PLUS);
	
	@Test
	// ensure SymbolSequence::build rejects null input
	public void testRejectNull() {
		assertThrows(NullPointerException.class, () -> {
			SymbolSequence.build((List<Symbol>)null);
		});
	}

	@Test
	// ensure SymbolSequence::toString returns the correct string representation
	public void testToString() {		
		assertEquals(SymbolSequence.build(timesPlusList).toString(), "[*, +]");
	}
	
	@Test
	// ensure SymbolSequence::toString returns the correct string representation
	public void testNotCached() {
		SymbolSequence testSymSequence1 = SymbolSequence.build(timesPlusList);
		SymbolSequence testSymSequence2 = SymbolSequence.build(timesPlusList);
		
		assertFalse(testSymSequence1 == testSymSequence2);
	}

	@Test
	// ensure SymbolSequence::match rejects null input
	public void testMatchRejectNull() {		
		assertThrows(NullPointerException.class, () -> {
			SymbolSequence.build(timesPlusList).match(null);
		});
	}
	
	@Test
	// ensure SymbolSequence::match returns ParseState.FAILURE when parsing is unsuccessful
	public void testMatchFailure() {
		assertEquals(SymbolSequence.build(timesPlusList).match(Arrays.asList(Connector.build(TerminalSymbol.MINUS))), ParseState.FAILURE);
	}
	
	@Test
	// ensure SymbolSequence::match returns the correct ParseState when called on SymbolSequence with one item
	public void testMatchOnOneItemList() {
		ParseState testState = SymbolSequence.build(Arrays.asList(TerminalSymbol.TIMES)).match(Arrays.asList(Connector.build(TerminalSymbol.TIMES), Connector.build(TerminalSymbol.PLUS)));
		
		assertTrue(testState.getSuccess());
		assertTrue(testState.getRemainder().get(0).matches(TerminalSymbol.PLUS));
		assertTrue(testState.getNode().toList().get(0).matches(TerminalSymbol.TIMES));
	}
	
	@Test
	// ensure SymbolSequence::match returns the correct ParseState when called on SymbolSequence with one item
	public void testMatchEpsilonEmptyList() {
		assertTrue(SymbolSequence.EPSILON.match(ListHandler.createEmptyList()).getSuccess());
	}
	
	@Test
	// ensure SymbolSequence::match returns the correct ParseState when called on SymbolSequence with multiple items
	public void testMatchOnMultiItemList() {
		ParseState testState = SymbolSequence.build(timesPlusList).match(Arrays.asList(Connector.build(TerminalSymbol.TIMES), Connector.build(TerminalSymbol.PLUS), Connector.build(TerminalSymbol.MINUS)));
		
		assertTrue(testState.getSuccess());
		assertEquals(testState.getRemainder().size(), 1);
		assertTrue(testState.getNode().toList().get(0).matches(TerminalSymbol.TIMES));
		assertTrue(testState.getNode().toList().get(1).matches(TerminalSymbol.PLUS));
	}

}
