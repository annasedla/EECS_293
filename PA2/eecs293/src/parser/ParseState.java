package parser;

import java.util.List;
import java.util.Objects;

/**
 * java class ParseState
 * Represents the results of parsing an expression
 * @author Fangze Liu, Jacob Goldberg, Anna Sedlackova
 *
 */
final class ParseState {
	//stores whether parsing was successful
	private final boolean success;
	
	//the first node of this parse state
	private final Node node;
	
	//the remainder of this parse state
	private final List<Token> remainder;
	
	//the parse state that will always be returned for a failure to parse
	final static ParseState FAILURE = new ParseState(false, null, null);
	
	/**
	 * Create a ParseState with a given success state, initial node, and remainder list
	 * @param success the success state
	 * @param node the first node
	 * @param remainder the list of tokens remaining to be parsed
	 */
	private ParseState(boolean success, Node node, List<Token> remainder) {		
		this.success = success;
		this.node = node;
		this.remainder = ListHandler.shallowCopy(remainder);
	}
	
	/**
	 * Return a new parse state with a given node and remainder
	 * @param node the node for the first element of the ParseState
	 * @param remainder the remaining tokens to be parsed
	 * @return a new ParseState object with the provided properties
	 */
	public static ParseState build(Node node, List<Token> remainder) {
		Objects.requireNonNull(node, "node passed to ParseState::build() must not be null");
		Objects.requireNonNull(remainder, "remainder passed to ParseState::build() must not be null");
		
		return new ParseState(true, node, remainder);
	}
	
	/**
	 * See whether or not this parse was successful
	 * @return true if the parse was successful, false otherwise
	 */
	public boolean getSuccess() {
		return success;
	}
	
	/**
	 * Get the first node for this parse state
	 * @return the first node in this parse state, or null if no such node exists 
	 */
	public Node getNode() {
		return node;
	}
	
	/**
	 * Get the remaining tokens to be parsed
	 * @return the remaining tokens to be parsed, and null if there is no remainder
	 */
	public List<Token> getRemainder() {
		return ListHandler.shallowCopy(remainder);
	}
	
	/**
	 * See if there is no remainder to this list
	 * @return true if there is no remainder to this list, false otherwise
	 */
	public final boolean hasNoRemainder() {
		return !ListHandler.nonEmptyList(remainder);
	}
}
