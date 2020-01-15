package parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Java class ConnectorTests
 * The set of unit tests for the Connector class
 * @author Fangze Liu, Jacob Goldberg, Anna Sedlackova
 *
 */
public class ConnectorTest {

    Connector c;
    Connector c1;
    Connector c2;

    @Before
    public void setUp(){
        c = Connector.build(TerminalSymbol.OPEN);
        c1 = Connector.build(TerminalSymbol.OPEN);
        c2 = Connector.build(TerminalSymbol.CLOSE);
    }

	//ensure that Connector::build() rejects a null argument
    @Test(expected = NullPointerException.class)
	public void testConnectorFailsOnNull() {
        Connector.build(null);
	}
	
	//ensure that non-recognized terminal symbol types (such as variable) are rejected by Connector::build()
	@Test(expected = IllegalArgumentException.class)
	public void testConnectorFailOnInvalidArgument() {
        Connector.build(TerminalSymbol.VARIABLE);
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

	//ensure that Connector::getType() properly returns the type used in building it
	@Test
	public void testConnectorTypeStoredCorrectlyEmpty() {
		final TerminalSymbol testType = TerminalSymbol.EMPTY;

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
		assertEquals(Connector.build(TerminalSymbol.EMPTY).toString(), " ");
	}
	
	@Test
	public void testIsOperator() {
		assertTrue(Connector.build(TerminalSymbol.PLUS).isOperator());
		assertTrue(Connector.build(TerminalSymbol.MINUS).isOperator());
		assertTrue(Connector.build(TerminalSymbol.TIMES).isOperator());
		assertTrue(Connector.build(TerminalSymbol.DIVIDE).isOperator());
		assertFalse(Connector.build(TerminalSymbol.OPEN).isOperator());
		assertFalse(Connector.build(TerminalSymbol.CLOSE).isOperator());
		assertFalse(Connector.build(TerminalSymbol.EMPTY).isOperator());
	}

    /**
     * Structured basis: nominal case
     * Good data: input is itself
     * input == this
     */
    @Test
    public void testEqualsItself() {
        assertTrue(c.equals(c));
    }

    // Good data: inputs contain the same string
    @Test
    public void testEqualsNominalTrue() {
        Assert.assertTrue(c.equals(c1));
    }

    // Bad data: input not type Type
    @Test
    public void testEqualsOtherType() {
        Integer one = new Integer(1);
        assertFalse(one.equals(c));
    }

    // Bad data: inputs not equal
    @Test
    public void testEqualsNominalFalse() {
        assertFalse(c1.equals(c2));
    }

    /**
     * Structured basis: nominal case
     */
    @Test
    public void testHashCode() {
        assertTrue(c.hashCode()==c1.hashCode());
    }
}
