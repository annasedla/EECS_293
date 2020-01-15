package parser;

import org.junit.Test;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * java class VariableTests
 * The set of unit tests for the Variable class
 * @author Fangze Liu, Jacob Goldberg, Anna Sedlackova
 *
 */
class VariableTests {
	//ensure that Variable::build() rejects a null argument
	@Test(expected = NullPointerException.class)
	void testVariableFailsOnNull() {
		Variable.build(null);
	}
	
	//ensure that Variable::getType() returns variable and that it matches variable type
	@Test
	void testVariableType() {
		assertEquals(Variable.build("a").getType(), TerminalSymbol.VARIABLE);
		assertTrue(Variable.build("a").matches(TerminalSymbol.VARIABLE));
	}
	
	//ensure that Variable::getRepresentation() returns a string passed into Variable::build()
	@Test
	void testVariableStringRepresentation() {
		final String testVariableName = "bbbbbb";
		assertEquals(Variable.build(testVariableName).getRepresentation(), testVariableName);
	}
	
	//ensure that Variables are cached
	@Test
	void testVariableCaching() {
		final String testVariableName = "abcd";
		assertTrue(Variable.build(testVariableName) == Variable.build(testVariableName));
	}
	
	//ensure that Variable::toString() returns the string representation passed into Variable::build()
	@Test
	void testVariableToString() {
		final String testVariableName = "defg";
		assertEquals(Variable.build(testVariableName).toString(), testVariableName);
	}
	
	@Test
	void testIsOperator() {
		assertFalse(Variable.build("x").isOperator());
	}
}
