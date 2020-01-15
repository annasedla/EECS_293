package parser;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.Optional;

/**
 * Java class InternalNodes
 * This class is for the internal nodes in the tree representation of
 * a numerical expression.
 * 
 * @author Fangze Liu, Jacob Goldberg, Anna Sedlackova
 * @version 1.00, 31 Jan 2019
 */
public class InternalNode implements Node {
	//the children of this node
	private final List<Node> children;	
	
	//a cached representation of the tokens that are are children of this internal node (null if not generated)
	private List<Token> cachedTokenList = null;
	
	//a cached string representation of the children of this node (null if not generated)
	private String cachedStringRepresentation = null;
	
	//the characters used in representing the tree as a string
	private static final String representationOpenBracket = "[";
	private static final String representationCloseBracket = "]";
	private static final String representationSeparator = ",";

	/**
	 * Constructor for creating an internal node
	 * @param children the child nodes of this internal node
	 */
	private InternalNode(List<Node> children) {
		//store a copy of the list of children provided
		this.children = ListHandler.shallowCopy(children);
	}
	
	/**
	 * Method that returns the concatenation of the children's lists.
	 * @return a list of tokens containing all children's lists
	 */
	@Override
	public List<Token> toList() {
		return Collections.unmodifiableList(cachedTokenList = 
				ObjectHandler.createObjectIfNull(cachedTokenList, 
						(Void throwaway) -> {
							return children.stream()
								.map(node -> node.toList())
								.collect(LinkedList::new, List::addAll, List::addAll);
							}));
	}
	
	/**
	 * Getter for this internal node's children
	 * @return list of children nodes
	 */
	public List<Node> getChildren() {
		//return a copy of the list of this node's children
		return ListHandler.shallowCopy(children);
	}
	
	/**
	 * Method that creates an internal node given a list of children nodes
	 * @param children list of child nodes
	 * @return an internal node with the specified child nodes
	 */
	public static InternalNode build(List<Node> children) {
		Objects.requireNonNull(children, "children passed on InternalNode::build() must not be null");
		
		return new InternalNode(children);
	}
	
	/**
	 * Method that returns the string representation of this internal node,
	 * invokes toString() on each child
	 * @return a string representation of the node
	 */
	public String toString() {
		return cachedStringRepresentation = ObjectHandler.createObjectIfNull(cachedStringRepresentation, 
				(Void throwaway) -> {
					return children.stream()
							.map(child -> child.toString())
							.collect(Collectors.joining(representationSeparator, representationOpenBracket, representationCloseBracket));
				});
	}
	
	/**
	 * Copies this node into a new node
	 * @return An InternalNode object that is exactly the same as this object
	 */
	public Node clone() {
		return InternalNode.build(children);
	}
	
	/**
	 * Check if this node has children
	 */
	public boolean isFruitful() {
		return ListHandler.nonEmptyList(getChildren());
	}
	
	/**
	 * Return whether or not this node is an operator
	 * @return Always returns false since InternalNodes are never operators
	 */
	public boolean isOperator() {
		return false;
	}
	
	/**
	 * Return whether or not this node is started by an operator
	 * @return Returns true if the first child of this node starts with an operator, false otherwise
	 */
	public boolean isStartedByOperator() {
		return firstChild().map(node -> node.isOperator()).orElse(false);
	}
	
	/**
	 * Returns the first child of this node
	 * @return Returns the first child of this node or empty is the node has no child
	 */
	public Optional<Node> firstChild() {
		return isFruitful() ? Optional.of(ListHandler.listHead(getChildren())) : Optional.empty();
	}
	
	/**
	 * Return whether or not this node has only one child and the child is a leaf node
	 * @return Returns true if this node has only one child and the child is a leaf node, false otherwise
	 */
	public boolean isSingleLeafParent() {
		return ListHandler.containsSingleItem(getChildren()) && firstChild().map(node -> node instanceof LeafNode).orElse(false);
	}
	
	public int getWidth() {
		return getChildren().stream().map(node -> node.getWidth()).collect(Collectors.summingInt(Integer::intValue)) + 2;
	}
	
	public static class Builder {
		//the children of this builder
		private List<Node> children = ListHandler.createEmptyList();
		
		/**
		 * Add a child node to this builder
		 * @param node The child node to add
		 * @return Always returns true
		 */
		public boolean addChild(Node node) {
			return children.add(node);
		}
		
		/**
		 * Get the children of this builder
		 * @return Returns a copy of the children of this builder
		 */
		private List<Node> getChildren() {
			return ListHandler.shallowCopy(children);
		}
		
		/**
		 * If the only Node in this list is an InternalNode, replace that InternalNode with its children
		 * @param list The list of Nodes being checked
		 * @return If the only item in the provided list is an InternalNode, that InternalNode's children, the input list otherwise
		 */
		private static List<Node> unnestSingleChildren(List<Node> list) {
			return ListHandler.containsSingleItem(list) && ListHandler.listHead(list) instanceof InternalNode ? ListHandler.listHead(list).getChildren() : list;
		}
		
		/**
		 * Add all items in a specified list to the current position at a list iterator
		 * @param list The list of items to be added
		 * @param iterator The iterator to which items are added
		 */
		private static <T> void addAllToListIterator(List<T> list, ListIterator<T> iterator) {
			list.stream().forEachOrdered(item -> iterator.add(item));
		}
		
		/**
		 * Return whether a node starts with a binary operator, given the last child
		 * (i.e. whether the a node starts with an operator and the previous node does NOT start with an operator)
		 * @param curChild The node being checked
		 * @param lastChild The node that appears before it
		 * @return Returns true if curChild starts with a binary operator (given lastChild), false otherwise
		 */
		private static boolean startsWithBinaryOperator(Node curChild, Node lastChild) {
			return !lastChild.isOperator() && curChild.isStartedByOperator();
		}
		
		/**
		 * Remove unnecessarily nested operators from a list of nodes
		 * @param list The list to remove nested operators from
		 * @return Returns a list of nodes that is equivalent to the input list except that binary operators have been un-nested
		 */
		private static List<Node> unnestOperators(List<Node> list) {
			List<Node> workingList = ListHandler.shallowCopy(list);
			
			if (workingList.size() > 1) {
				ListIterator<Node> iterator = workingList.listIterator(1);
				Node lastChild = ListHandler.listHead(workingList);
				
				while (iterator.hasNext()) {
					Node curChild = iterator.next();
					if (startsWithBinaryOperator(curChild, lastChild)) {
						iterator.remove();
						addAllToListIterator(curChild.getChildren(), iterator);
					}
				}
			}
			
			return workingList;
		}
		
		/**
		 * Replace each node that is only the parent of a single leaf with its child
		 * @param list The list of nodes being considered
		 * @return The same list of nodes, but with each InternalNode with only one child being replaced with its child
		 */
		private static List<Node> replaceSingleLeafParent(List<Node> list){
			return ListHandler.replaceItemsMatching(list, (Node node) -> { return node.isSingleLeafParent(); }, (Node node) -> { return ListHandler.listHead(node.getChildren()); });
		}
		
		/**
		 * Simplify this builder
		 * @return Returns a simplified version of this builder
		 */
		public Builder simplify() {
			//throw out non-fruitful nodes
			List<Node> newChildren = getChildren().stream().filter(child -> child.isFruitful()).collect(Collectors.toList());
			
			newChildren = unnestSingleChildren(newChildren);
			newChildren = unnestOperators(newChildren);
			newChildren = replaceSingleLeafParent(newChildren);
			
			InternalNode.Builder newBuilder = new InternalNode.Builder();
			newBuilder.children = newChildren;
			
			return newBuilder;
		}
		
		/**
		 * Create a new InternalNode based on the contents of this builder
		 * @return a new InternalNode with the simplified children of this builder
		 */
		public InternalNode build() {
			return new InternalNode(this.simplify().getChildren());
		}	
		
	}
	
}
