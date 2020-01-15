package parser;

import java.util.List;

/**
 * Java enum class TerminalSymbol
 * This class specifies all the symbols allowed in a mathematical expression.
 * 
 * @author Fangze Liu, Jacob Goldberg, Anna Sedlackova
 * @version 1.00, 31 Jan 2019
 */
public enum TerminalSymbol implements Symbol {
    VARIABLE(null), PLUS("+"), MINUS("-"), TIMES("*"), DIVIDE("/"), OPEN("("), CLOSE(")");
	
	//stores the string representation of a symbol if all symbols of that type have the same representation, null otherwise
	private final String stringRepresentation;
	
	/**
	 * Constructor of a terminal symbol
	 * @param stringRepresentation the string representation of the symbol
	 */
	private TerminalSymbol(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}
	
	/**
	 * Method that converts the terminal symbol to a string
	 * @return string representation of this terminal symbol if available, and null if not all symbols of this symbol's type have the same string representation
	 */
	public String toString() {
		return stringRepresentation;
	}
	
	/**
	 * Method that attempts to parse a list of tokens based on this terminal symbol
	 * @param input A list of tokens to parse
	 * @return If the first token in input matches this symbol, then this returns a parse state with this token followed by the remainder of the list. Otherwise, returns a failure state.
	 */
	public ParseState parse(List<Token> input) {
		if (!ListHandler.nonEmptyList(input) || !ListHandler.listHead(input).matches(this)) {
			return ParseState.FAILURE;
		}
		
		List<Token> remainder = ListHandler.shallowCopy(input);
		
		return ParseState.build(LeafNode.build(remainder.remove(0)), remainder);
	}
	
	/**
	 * 
	 * @param string
	 * @return
	 */
	static Token stringToToken(String string) {
		//TODO: make this cleaner
		for (TerminalSymbol symbol : values()) {
			if (string.equals(symbol.toString())) {
				return Connector.build(symbol);
			}
		}
		
		return Variable.build(string);
	}
}