package parser;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * Java class LeafNode
 * This class is for the leaf nodes in the tree representation of
 * a numerical expression.
 * 
 * @author Fangze Liu, Jacob Goldberg, Anna Sedlackova
 * @version 1.00, 31 Jan 2019
 */
public class LeafNode implements Node {
	//the token represented by this leaf node
	private final Token token;
	
	/**
	 * Constructor for leaf node
	 * @param token the token value for this leaf node
	 */
	private LeafNode(Token token) {
		this.token = token;
	}
	
	/**
	 * Method that returns this leaf node as a list of tokens
	 * @return list of token
	 */
	@Override
	public List<Token> toList() {
		return Arrays.asList(token);
	}
	
	/**
	 * Getter method for token
	 * @return the token of this leafnode
	 */
	public Token getToken() {
		return this.token;
	}

	/**
	 * Method that creates a new leaf node given a token value
	 * @param token the token value to be given to the leaf node
	 * @return the leaf node created
	 */
	public static LeafNode build(Token token) {
		Objects.requireNonNull(token, "token provided to LeafNode::build must not be null");
		
		return new LeafNode(token);
	}
	
	/**
	 * Method that returns a string representation of the token stored by this node
	 * @return the string representation of this leaf node
	 */
	public String toString() {
		return token.toString();
	}
	
	/**
	 * Get the children of this node (null since there are no children of a leaf node)
	 */
	public List<Node> getChildren() {
		return null;
	}
	
	/**
	 * Check if this node has data (always true since this is a leaf node)
	 */
	public boolean isFruitful() {
		return true;
	}
	
	public boolean isOperator() {
		return token.isOperator();
	}
	
	public boolean isStartedByOperator() {
		return false;
	}
	
	public Optional<Node> firstChild(){
		return Optional.empty();
	}
	
	public boolean isSingleLeafParent() {
		return false;
	}
	
	public int getWidth() {
		return toString().length();
	}

	public boolean isLeaf() { return true; }
}
