package parser;

import java.util.List;
import java.util.Optional;

/**
 * Java interface Node
 * This class is for the nodes in the tree representation of
 * a numerical expression.
 * 
 * This may ONLY be extended by immutable objects.
 * 
 * @author Fangze Liu, Jacob Goldberg, Anna Sedlackova
 * @version 1.00, 31 Jan 2019
 */
public interface Node {
	/**
	 * Get a list representation of all tokens stored in a node
	 * @return a list of all nodes stored by this node in order
	 */
	List<Token> toList();
	List<Node> getChildren();
	boolean isFruitful();
	boolean isOperator();
	boolean isStartedByOperator();
	Optional<Node> firstChild();
	boolean isSingleLeafParent();
	int getWidth();
}
