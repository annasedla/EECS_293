package typecheck;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import parser.Connector;
import parser.TerminalSymbol;

public class ExpressionTypesTest {
	
	private Type cat;
	private Type dog;
	private Connector plus;
	private ExpressionTypes expTypes;
	private Expression exp;
	
	@Before
	public void setUp() {
		cat = new Type("cat");
		dog = new Type("dog");
		plus = Connector.build(TerminalSymbol.PLUS);
		expTypes = new ExpressionTypes();
		exp = new Expression(cat, dog, plus);
	}

	/**
	 * Structured basis: nominal case
	 */
	@Test
	public void testAddRule() {
		expTypes.addRule(exp, cat);
		ExpressionTypes.TestHook th = expTypes.new TestHook();
		assertEquals(expTypes.expressionType(exp), th.getMap().get(exp));
	}

	@Test
	public void testAddRuleNullExpression() {
		expTypes.addRule(null, cat);
		ExpressionTypes.TestHook th = expTypes.new TestHook();
		assertTrue(th.getMap().isEmpty());
	}

	@Test
	public void testAddRuleNullType() {
		expTypes.addRule(exp, null);
		ExpressionTypes.TestHook th = expTypes.new TestHook();
		assertTrue(th.getMap().isEmpty());
	}

	/**
	 * Structured basis: nominal case
	 */
	@Test
	public void testGetRule() {
		expTypes.addRule(exp, cat);
		Type getType = expTypes.expressionType(exp);
		assertEquals(cat, getType);
	}

}
