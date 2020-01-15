package parser;

/**
 * Java interface Token
 * 
 * This represents an immutable token. This is to say, it should ONLY be extended by immutable classes per the discussion board.
 * 
 * @author Fangze Liu, Jacob Goldberg, Anna Sedlackova
 * @version 1.00, 31 Jan 2019
 */
public interface Token {
	/**
	 * Get the type of a token
	 * @return the type of token represented
	 */
    TerminalSymbol getType();
    
    /**
     * See if this Token is of a given symbol type
     * @param type the type of symbol against which to check this token
     * @return true if the token is of the provided type, false otherwise
     */
    boolean matches(TerminalSymbol type);
    
    boolean isOperator();
}