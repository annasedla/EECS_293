package parser;

import java.util.Objects;
import java.util.function.Function;

/**
 * Java class Variable
 * This class is for the variables in a numerical expression.
 * 
 * @author Fangze Liu, Jacob Goldberg, Anna Sedlackova
 * @version 1.00, 31 Jan 2019
 */
public final class Variable extends AbstractToken {
    //the string representation (i.e. variable name) of this variable
    private final String representation;
    
    //a generic function that can be used to call the constructor
    static Function<String, Variable> variableConstructor = stringRepresentation -> new Variable(stringRepresentation);

    //cache to keep track of variables already created
    private static Cache<String, Variable> cache = new Cache<>();
    
    /**
     * Constructor for a variable
     * @param representation the string representation of the variable
     */
    private Variable(String representation) {
        this.representation = representation;
        this.type = TerminalSymbol.VARIABLE;
    }
    
    /** 
     * Public getter method for a variable's representation
     * @return the string representation of this variable
     */
    public final String getRepresentation() {
        return this.representation;
    }

    /** 
     * Method that returns a variable given a representation
     * @param representation the string representation of the variable
     * @return a variable with the given string representation
     */
    public static final Variable build(String representation) {
    	Objects.requireNonNull(representation, "Variable representation cannot be null.");
        
        return cache.get(representation, variableConstructor);
    }

    /** 
     * Method that returns this variable's representation
     * @return the string representation of this variable
     */
    public String toString() {
        return this.getRepresentation();
    }

    public boolean isOperator() {
    	return false;
    }
}