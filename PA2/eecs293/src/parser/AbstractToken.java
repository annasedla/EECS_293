package parser;

/**
 * Java class AbstractToken
 * 
 * @author Fangze Liu, Jacob Goldberg, Anna Sedlackova
 * @version 1.00, 31 Jan 2019
 */
public abstract class AbstractToken implements Token {
	//the type this token has (stored here to prevent code duplication with getType())
	protected TerminalSymbol type;
	
	/**
	 * The method returns whether the input argument is equal to getType()
	 * @param type the argument to be matched with getType()
	 * @return boolean, if argument is equal to getType()
	 */
    public final boolean matches(TerminalSymbol type) {
    	return type == getType();
    }
    
    public TerminalSymbol getType() {
    	return type;
    }
}