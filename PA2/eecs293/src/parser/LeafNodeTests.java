package parser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

/**
 * java class LeafNodeTests
 * The set of unit tests for the LeafNode class
 * @author Fangze Liu, Jacob Goldberg, Anna Sedlackova
 *
 */
class LeafNodeTests {
	final Token timesToken = Connector.build(TerminalSymbol.TIMES);
	
	//ensure that LeafNode::build() rejects null arguments
	@Test
	void testLeafNodeConstructorFailsOnNull() {
		assertThrows(NullPointerException.class, () -> {
			LeafNode.build(null);
		});
	}

	//ensure that leaf nodes are not cached (i.e. building one with the same token will result in different instances)
	@Test
	void testLeafNodesAreNotCached() {		
		assertFalse(LeafNode.build(timesToken) == LeafNode.build(timesToken));
	}
	
	//ensure that LeafNode::toString() returns the string representation of its stored symbol
	@Test
	void testLeafNodeToString() {
		assertEquals(LeafNode.build(timesToken).toString(), timesToken.toString());
	}
	
	//ensure that LeafNode::toList() returns a list containing only the token used to build the list
	@Test
	void testLeafNodeToList() {
		assertEquals(LeafNode.build(timesToken).toList(), Arrays.asList(timesToken));
	}
	
	@Test
	void testGetChildren() {
		assertEquals(LeafNode.build(timesToken).getChildren(), null);
	}
	
	@Test
	void testIsFruitful() {
		assertTrue(LeafNode.build(timesToken).isFruitful());
	}
	
	@Test
	void testIsOperator() {
		assertTrue(LeafNode.build(timesToken).isOperator());
		assertFalse(LeafNode.build(Variable.build("a")).isOperator());
	}
	
	@Test
	void testIsStartedByOperator() {
		assertFalse(LeafNode.build(timesToken).isStartedByOperator());
		assertFalse(LeafNode.build(Variable.build("a")).isStartedByOperator());
	}
	
	@Test
	void testFirstChild() {
		assertFalse(LeafNode.build(timesToken).firstChild().isPresent());
	}
	
	@Test
	void testIsSingleLeafParent() {
		assertFalse(LeafNode.build(timesToken).isSingleLeafParent());
	}	
	
}
