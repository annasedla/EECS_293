package parser;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Java class SymbolSequence This class is for representing a sequence of
 * symbols
 * 
 * @author Fangze Liu, Jacob Goldberg, Anna Sedlackova
 * @version 1.00, 5 Feb 2019
 */

final class SymbolSequence {
	// the list of symbols being stored
	private final List<Symbol> production;

	// an empty symbol sequence
	final static SymbolSequence EPSILON = new SymbolSequence(ListHandler.createEmptyList());

	/**
	 * Constructor for a SymbolSequence
	 * 
	 * @param production a list of symbols
	 */
	private SymbolSequence(List<Symbol> production) {
		this.production = production;
	}

	/**
	 * Method to build a SymbolSequence given a production
	 * 
	 * @param production a list of symbols
	 * @return a SymbolSequence with the given list of symbols
	 */
	public static SymbolSequence build(List<Symbol> production) {
		Objects.requireNonNull(production, "Production provided to SymbolSequence::build must not be null");

		return new SymbolSequence(ListHandler.shallowCopy(production));
	}

	/**
	 * Method to build a SymbolSequence given multiple symbols
	 * 
	 * @param symbols the symbols to be added into a sequence
	 * @return a symbol sequence with the given symbols
	 */
	static final SymbolSequence build(Symbol... symbols) {
		return new SymbolSequence(Arrays.asList(symbols));
	}

	/**
	 * Method to return a SymbolSequence as a string
	 * 
	 * @return the string representation of this SymbolSequence
	 */
	public String toString() {
		return production.toString();
	}

	/**
	 * Method to match the symbols from this SymbolSequence to an input token list
	 * Invokes parse() of Symbol
	 * 
	 * @param input a list of tokens
	 * @return a ParseState corresponding to the parsing result
	 */
	public ParseState match(List<Token> input) {
		Objects.requireNonNull(input, "Input provided to SymbolSequence::match must not be null");

		List<Token> remainder = ListHandler.shallowCopy(input);
		List<Node> children = ListHandler.createEmptyList();
		
		for (Symbol symbol : production) {
			ParseState parsingResult = symbol.parse(remainder);
			if (parsingResult.getSuccess()) {
				children.add(parsingResult.getNode());
				remainder = parsingResult.getRemainder();
			} else {
				return ParseState.FAILURE;
			}
		}

		InternalNode.Builder builder = new InternalNode.Builder();
		children.stream().forEachOrdered(node -> builder.addChild(node));
		
		return ParseState.build(builder.simplify().build(), remainder);
	}
}
