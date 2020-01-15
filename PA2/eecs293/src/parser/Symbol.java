package parser;

import java.util.List;

/**
 * An interface representing any kind of mathematical symbol
 * All classes that implement this interface must be immutable.
 *
 */
interface Symbol {
	public ParseState parse(List<Token> input);
}
