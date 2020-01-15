package parser;

import org.junit.Test;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.List;

/**
 * Java class InternalNodeTests
 * The set of unit tests for the InternalNode class
 * @author Fangze Liu, Jacob Goldberg, Anna Sedlackova
 *
 */
class InternalNodeTests {
	private final InternalNode emptyInternalNode = InternalNode.build(ListHandler.createEmptyList());
	
	private final Token timesToken = Connector.build(TerminalSymbol.TIMES);
	private final LeafNode timesNode = LeafNode.build(timesToken);
	
	private final Token aVarToken = Variable.build("a");
	private final Node aVarNode = LeafNode.build(aVarToken);
	private final List<Node> aTimesA = Arrays.asList(aVarNode, timesNode, aVarNode);
	
	//test that InternalNode::build() rejects a null list of children
	@Test(expected = NullPointerException.class)
	void testInternalNodeFailsOnNull() {
	    InternalNode.build(null);
	}

	//ensure that internal nodes are not cached
	@Test
	void testInternalNodesNotCached() {
		assertFalse(InternalNode.build(aTimesA) == InternalNode.build(aTimesA));
	}
	
	//ensure that InternalNode::toList() properly converts to a list of tokens on a non-nested instance
	@Test
	void testInternalNodeToListBasic() {				
		assertEquals(InternalNode.build(aTimesA).toList(), Arrays.asList(
					aVarToken,
					timesToken,
					aVarToken
				));
		}
	
	//ensure that InternalNode::toList() works correctly when one of its children is another InternalNode
	@Test
	void testInternalNodeToListNested() {
		assertEquals(InternalNode.build(Arrays.asList(
				aVarNode,
				timesNode,
				InternalNode.build(Arrays.asList(
						LeafNode.build(Variable.build("y")),
						LeafNode.build(Connector.build(TerminalSymbol.CLOSE))
					))
		)).toList(), Arrays.asList(
				aVarToken,
				timesToken,
				Variable.build("y"),
				Connector.build(TerminalSymbol.CLOSE)
		));
	}
	
	//ensure that InternalNode::toList() works correctly for a simple non-nested expression
	@Test
	void testInternalNodeToStringBasic() {
		assertEquals(InternalNode.build(aTimesA).toString(), "[a,*,a]");
	}
	
	//ensure that InternalNode::toString() works correctly on a nested expression
	@Test
	void testInternalNodeToStringNested() {
		assertEquals(InternalNode.build(Arrays.asList(
				aVarNode,
				timesNode,
				InternalNode.build(Arrays.asList(
						LeafNode.build(Connector.build(TerminalSymbol.OPEN)),
						LeafNode.build(Variable.build("x")),
						LeafNode.build(Connector.build(TerminalSymbol.PLUS)),
						LeafNode.build(Variable.build("y")),
						LeafNode.build(Connector.build(TerminalSymbol.CLOSE))
				)),
				LeafNode.build(Variable.build("b"))
		)).toString(), "[a,*,[(,x,+,y,)],b]");
	}
	
	//make sure that modifying the input list used in InternalNode::build() after using it to build a node does not modify the list stored in the node
	@Test
	void doesNotDirectlyStoreInput() {
		List<Node> children = ListHandler.shallowCopy(aTimesA);
		
		//create the node and modify one of the children in the list we originally passed in
		InternalNode node = InternalNode.build(children);
		children.set(2, LeafNode.build(Variable.build("b")));
		
		//ensure that the internally stored children were not modified in the way that the input list was
		assertEquals(node.toString(), "[a,*,a]");
		assertEquals(node.toList().get(2), aVarToken);
	}

	//make sure that changes in the list we return from InternalNode::toList() cannot affect the internally stored list
	@Test(expected = Exception.class)
	void testCannotChangeList() {
		//take InternalNode::toList() and replace one of the elements
		InternalNode node = InternalNode.build(aTimesA);
		node.toList().set(2, Variable.build("b"));
		
		//make sure that the internal representation of the node is not affected
		assertEquals(node.toString(), "[a,*,a]");
		assertEquals(node.toList().get(2), aVarToken);
	} 
	
	@Test
	void testIsFruitful() {
		assertFalse(emptyInternalNode.isFruitful());
		
		assertTrue(InternalNode.build(aTimesA).isFruitful());
	}
	
	private final InternalNode timesInternalNode = InternalNode.build(Arrays.asList(timesNode));
	@Test
	void testBuilderSimplifiesInternalNodeWithOnlyOneInternalNodeChild() {		
		InternalNode.Builder builder = new InternalNode.Builder();
		builder.addChild(timesInternalNode);
		assertEquals(builder.build().toString(), "[*]");
	}
	
	@Test
	void testBuilderRemovesEmptyChildren() {
		
		InternalNode.Builder builder = new InternalNode.Builder();
		builder.addChild(emptyInternalNode);
		builder.addChild(timesInternalNode);
		builder.addChild(emptyInternalNode);
		assertEquals(builder.build().toString(), "[*]");
	}
	
	@Test
	void testIsOperator() {
		assertFalse(InternalNode.build(aTimesA).isOperator());
	}
	
	@Test
	void testIsStartedByOperator() {
		assertFalse(InternalNode.build(aTimesA).isStartedByOperator());
		assertTrue(InternalNode.build(Arrays.asList(
				timesNode,
				aVarNode
				)).isStartedByOperator());
	}
	
	@org.junit.Test
	void testFirstChild() {
		assertTrue(InternalNode.build(aTimesA).firstChild().isPresent());
		assertFalse(InternalNode.build(ListHandler.createEmptyList()).firstChild().isPresent());	
	}
	
	@Test
	void testIsSingleLeafParent() {
		assertTrue(InternalNode.build(Arrays.asList(LeafNode.build(Variable.build("y")))).isSingleLeafParent());
		assertFalse(InternalNode.build(ListHandler.createEmptyList()).isSingleLeafParent());
		assertFalse(InternalNode.build(Arrays.asList(LeafNode.build(Variable.build("y")),LeafNode.build(Connector.build(TerminalSymbol.CLOSE)))).isSingleLeafParent());
	}
	
	@Test
	void testBuilderReplaceSingleLeafParent() {
		InternalNode leafParent = InternalNode.build(Arrays.asList(LeafNode.build(Variable.build("y"))));
		InternalNode leafGrandParent = InternalNode.build(Arrays.asList(leafParent));
		InternalNode.Builder builder = new InternalNode.Builder();
		builder.addChild(leafGrandParent);
		assertEquals(builder.build().toString(), "[y]");
	}
	
}
