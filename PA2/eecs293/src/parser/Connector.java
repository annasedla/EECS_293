package parser;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Java class Connector
 * This class is for the connectors in a numerical expression.
 * 
 * @author Fangze Liu, Jacob Goldberg, Anna Sedlackova
 * @version 1.00, 31 Jan 2019
 */
public final class Connector extends AbstractToken {
	//the only allowed types of terminal symbols that can be used in a connector
    private static final List<TerminalSymbol> allowedSymbols = Arrays.asList(TerminalSymbol.PLUS, TerminalSymbol.MINUS, TerminalSymbol.TIMES, TerminalSymbol.DIVIDE, TerminalSymbol.OPEN, TerminalSymbol.CLOSE);
    
    private static final List<TerminalSymbol> operatorSymbols = Arrays.asList(TerminalSymbol.PLUS, TerminalSymbol.MINUS, TerminalSymbol.TIMES, TerminalSymbol.DIVIDE);
    
    //cache to keep track of connectors already created
    private static Cache<TerminalSymbol, Connector> cache = new Cache<>();
    
    //a generic function that can be used to call the constructor
    static Function<TerminalSymbol, Connector> connectorConstructor = connectorType -> new Connector(connectorType);
    
    /** 
     * Constructor to set type for the connector
     * @param type the type for the connector to be created
     */
    private Connector(TerminalSymbol type) {    	
        this.type = type;
    }

    /** 
     * Method that returns a connector given terminal symbol type
     * @param type the type of the connector to be created
     * @return the created connector
     */
    public static final Connector build (TerminalSymbol type) {
        Objects.requireNonNull(type, "Connector type for Connector::build() cannot be null");
        
        if (!allowedSymbols.contains(type)) {
            throw new IllegalArgumentException("Invalid connector type for Connector::build()");
        }

        return cache.get(type, connectorConstructor);
    }

    /** 
     * Method that returns the character representation of this connector
     * @return the string representation of this connector
     */
    public String toString() {
    	return this.getType().toString();
    }
    
    public boolean isOperator() {
    	return operatorSymbols.contains(getType());
    }
}