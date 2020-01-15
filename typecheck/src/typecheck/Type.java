package typecheck;

import java.util.Objects;

/**
 * Java class Type
 * Type is an entity that stores the custom type of the input. It contains a special private field EMPTY
 * and also contains a constructor, getters, and setters and the overriden equals method.
 *
 * @author Anna Sedlackova, Jason Shin
 * contact: axs1202@case.edu, jjs270@case.edu
 * @version 1.1, 25 Apr 2019
 */
final class Type {

    //Fields

    /**
     * Stores the constructor's input representation
     */
    private final String variableType;

    /**
     * Stores an empty Type
     */
    static final Type EMPTY = new Type("");

    /**
     * Constructs a Type using the input String
     * @throws NullPointerException if input is null
     * @param type String representation of variable Type
     */
    Type(String type) {
        Objects.requireNonNull(type, "Type of variable cannot be null.");
        variableType = type;
    }

    //Methods

    /**
     * Getter method for this variable
     * @return Retrieves Type of a variable with a String representation
     */
    String getType(){
        return variableType;
    }

    /**
     * Overrides Object's toString method
     * @return String representation of the variable Type
     */
    @Override
    public String toString(){
        return variableType;
    }

    /**
     * Overrides Object's equals method
     * @param o Object to be compared to this
     * @return True if objects contain the same String representation of a variable
     */
    @Override
    public boolean equals(Object o){

        if (o == this){
            return true;
        }

        if (!(o instanceof Type)){
            return false;
        }

        Type t = (Type) o;

        return (variableType.equals(t.getType()));
    }

    /**
     * Overrides Object's hashcode method
     * @return An integer that will be identical for the same variable Types and different otherwise
     */
    @Override
    public int hashCode(){
        int prime = 31; //arbitrary prime number to differentiate between Types
        return prime + variableType.hashCode();
    }
}
