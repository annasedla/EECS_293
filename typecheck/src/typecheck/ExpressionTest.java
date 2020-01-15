package typecheck;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import parser.Connector;
import parser.TerminalSymbol;

public class ExpressionTest {
	
	private Type cat;
	private Type dog;
	private Connector plus;
	private Connector minus;
	private Expression exp;
	
	@Before
	public void setUp() {
		cat = new Type("cat");
		dog = new Type("dog");
		plus = Connector.build(TerminalSymbol.PLUS);
		minus = Connector.build(TerminalSymbol.MINUS);
		exp = new Expression(cat, dog, plus);
	}

	/**
	 * Structured basis: nominal case
	 */
	@Test
	public void testConstructor() {
		Expression exp = new Expression(new Type("cat"), new Type("dog"), Connector.build(TerminalSymbol.PLUS));
		assertEquals(cat, exp.getLeftExpressionType());
		assertEquals(dog, exp.getRightExpressionType());
		assertEquals(plus, exp.getExpressionSymbol());
	}

	/**
	 * Structured basis: nominal case
	 * Good data: input is itself
	 */
	@Test
	public void testEqualsItself() {
		assertTrue(exp.equals(exp));
	}

	//Bad data: inputs not type expression
	@Test
	public void testEqualsOtherType() {
		assertFalse(exp.equals(cat));
	}

	//Good data: new expression equals
	@Test
	public void testDoFieldsEqualNominal() {
		Expression exp2 = new Expression(new Type("cat"), new Type("dog"), Connector.build(TerminalSymbol.PLUS));
		assertTrue(exp.equals(exp2));
	}

	//Bad data: left fields do not equal
	@Test
	public void testEqualsLeftUnequal() {
		Expression exp2 = new Expression(new Type("dog"), new Type("dog"), Connector.build(TerminalSymbol.PLUS));
		assertFalse(exp.equals(exp2));
	}

	//Bad data: connectors do not equal
	@Test
	public void testEqualsSymbolUnequal() {
		Expression exp2 = new Expression(new Type("cat"), new Type("dog"), Connector.build(TerminalSymbol.MINUS));
		assertFalse(exp.equals(exp2));
	}

	//Bad data: right fields do not equal
	@Test
	public void testEqualsRightUnequal() {
		Expression exp2 = new Expression(new Type("cat"), new Type("cat"), Connector.build(TerminalSymbol.PLUS));
		assertFalse(exp.equals(exp2));
	}

    /**
     * Structured basis: nominal case
     */
	@Test
	public void testHashCode() {
        Expression exp2 = new Expression(new Type("cat"), new Type("dog"), Connector.build(TerminalSymbol.PLUS));

        assertTrue(exp.hashCode() == exp2.hashCode());
	}

    /**
     * Structured basis: nominal case
     */
    @Test
    public void testGetLeftExpression(){
        assertEquals(exp.getLeftExpressionType(), cat);
    }

    /**
     * Structured basis: nominal case
     */
    @Test
    public void testGetRightExpression(){
        assertEquals(exp.getRightExpressionType(), dog);
    }

    /**
     * Structured basis: nominal case
     */
    @Test
    public void testGetExpressionSymbol(){
        assertEquals(exp.getExpressionSymbol(), plus);
    }

    /**
     * Structured basis: nominal case
     */
    @Test
    public void testSetLeftExpression(){
        exp.setLeftExpressionType(dog);
        assertEquals(exp.getLeftExpressionType(),dog);
    }

    /**
     * Structured basis: nominal case
     */
    @Test
    public void testSetRightExpression(){
        exp.setRightExpressionType(cat);
        assertEquals(exp.getLeftExpressionType(),cat);
    }


    /**
     * Structured basis: nominal case
     */
    @Test
    public void testSetExpressionSymbol(){
        exp.setExpressionSymbol(minus);
        assertEquals(exp.getExpressionSymbol(), minus);
    }
}
