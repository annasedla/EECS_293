package typecheck;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import parser.Connector;
import parser.TerminalSymbol;

public class TypeTest {

	private Type t;
	private Type t1;
	private Type t2;


	@Before
	public void initialize() {
		t = new Type("cat");
		t1 = new Type("cat");
		t2 = new Type ("dog");
	}

	/**
	 * Structured basis: nominal case
	 * */
	@Test
	public void testConstructorAndGetType() {
		assertEquals("cat", t.getType());
	}

	//Bad data: type is null
    @Test(expected=NullPointerException.class)
    public void testConstructorAndGetTypeNull() {
	    Type nullType = new Type (null);
        nullType.getType();
    }

    //Bad data: type is null
    @Test(expected = NullPointerException.class)
    public void testConstructorNull(){
        Type nullType = null;
        nullType.getType();
    }

    //Bad data: type is null
    @Test
    public void testConstructorAndGetTypeEmpty() {
	    Type emptyType = new Type("");
        assertEquals("", emptyType.getType());
    }

	/**
	 * Structured basis: nominal case
	 * */
	@Test
	public void testToString() {
		assertEquals("cat", t.toString());
	}

	/**
	 * Structured basis: nominal case
     * Good data: input is itself
	 * input == this
	 */
	@Test
	public void testEqualsItself() {
		assertTrue(t.equals(t));
	}

	// Good data: inputs contain the same string
	@Test
	public void testEqualsNominalTrue() {
		assertTrue(t.equals(t1));
	}

	// Bad data: input not type Type
	@Test
	public void testEqualsOtherType() {
		Connector c = Connector.build(TerminalSymbol.PLUS);
		assertFalse(t.equals(c));
	}

	// Bad data: inputs not equal
	@Test
	public void testEqualsNominalFalse() {
		assertFalse(t1.equals(t2));
	}

	/**
	 * Structured basis: nominal case
	 */
	@Test
	public void testHashCode() {
		assertEquals(t.hashCode(), 98293);
	}

    //Branch coverage: null type
    @Test
    public void testHashCodeDouble() {
        assertTrue(t.hashCode() == t1.hashCode());
    }
}
