package typecheck;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import parser.Variable;

public class VariableTypesTest {
	
	Type cat;
	Variable v;
	VariableTypes vt;
	
	@Before
	public void setUp() {
		cat = new Type("cat");
		v = Variable.build("v");
		vt = new VariableTypes();
	}

	/**
	 * Structured basis: nominal case
	 */
	@Test
	public void testAddVariableType() {
		vt.addVariableType(v, cat);
		VariableTypes.TestHook th = vt.new TestHook();
		assertEquals(cat, th.getMap().get(v));
	}

	@Test
	public void testAddVariableTypesNullVariable() {
		vt.addVariableType(null, cat);
		VariableTypes.TestHook th = vt.new TestHook();
		assertTrue(th.getMap().isEmpty());
	}

	@Test
	public void testAddVariableTypesNullType() {
		vt.addVariableType(v, null);
		VariableTypes.TestHook th = vt.new TestHook();
		assertTrue(th.getMap().isEmpty());
	}

	/**
	 * Structured basis: nominal case
	 */
	@Test
	public void testVariableType() {
		vt.addVariableType(v, cat);
		Type returnedFromVT = vt.variableType(v);
		assertEquals(cat, returnedFromVT);
	}
}
