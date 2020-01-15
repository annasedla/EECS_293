package parser;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Java class ConnectorTests
 * The set of unit tests for the Connector class
 * @author Fangze Liu, Jacob Goldberg, Anna Sedlackova
 *
 */
public class ConnectorTests {
	//ensure that Connector::build() rejects a null argument
	@Test
	public void testConnectorFailsOnNull() {
		assertThrows(NullPointerException.class, () -> {
			Connector.build(null);
		});
	}
	
	//ensure that non-recognized terminal symbol types (such as variable) are rejected by Connector::build()
	@Test
	public void testConnectorFailOnInvalidArgument() {
		assertThrows(IllegalArgumentException.class, () -> {
			Connector.build(TerminalSymbol.VARIABLE);
		});
	}
	
	//ensure Connectors are cached
	@Test
	public void testConnectorIsCached() {
		final TerminalSymbol testType = TerminalSymbol.TIMES;
		
		assertTrue(Connector.build(testType) == Connector.build(testType));
	}

	//ensure that Connector::getType() properly returns the type used in building it
	@Test
	public void testConnectorTypeStoredCorrectly() {
		final TerminalSymbol testType = TerminalSymbol.MINUS;
		
		assertEquals(Connector.build(testType).getType(), testType);
		assertTrue(Connector.build(testType).matches(testType));
	}
	
	//ensure that Connector::toString() works for all terminal symbols
	@Test
	public void testConnectorToString() {
		assertEquals(Connector.build(TerminalSymbol.PLUS).toString(), "+");
		assertEquals(Connector.build(TerminalSymbol.MINUS).toString(), "-");
		assertEquals(Connector.build(TerminalSymbol.TIMES).toString(), "*");
		assertEquals(Connector.build(TerminalSymbol.DIVIDE).toString(), "/");
		assertEquals(Connector.build(TerminalSymbol.OPEN).toString(), "(");
		assertEquals(Connector.build(TerminalSymbol.CLOSE).toString(), ")");
	}
	
	@Test
	public void testIsOperator() {
		assertTrue(Connector.build(TerminalSymbol.PLUS).isOperator());
		assertTrue(Connector.build(TerminalSymbol.MINUS).isOperator());
		assertTrue(Connector.build(TerminalSymbol.TIMES).isOperator());
		assertTrue(Connector.build(TerminalSymbol.DIVIDE).isOperator());
		assertFalse(Connector.build(TerminalSymbol.OPEN).isOperator());
		assertFalse(Connector.build(TerminalSymbol.CLOSE).isOperator());
	}
}
