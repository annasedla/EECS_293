package typecheck;

import parser.Connector;
import java.util.Objects;

/**
 * Java class Expression
 * Type is an entity that stores an expression representation.
 * An expression consists of a variable Type, a connector (+,-,/,*), and a variable Type (Integer + Double).
 * This class contains a constructor, getters and setters and the overriden equals method.
 *
 * @author Anna Sedlackova, Jason Shin
 * contact: axs1202@case.edu, jjs270@case.edu
 * @version 1.1, 25 Apr 2019
 */
final class Expression {

    //Fields

    /**
     * Stores the Type of the variable on the left side of an expression
     */
    private Type leftExpressionType;

    /**
     * Stores the Type of the variable on the right side of an expression
     */
    private Type rightExpressionType;

    /**
     * Stores an operator (+, -, *, /)
     */
    private Connector expressionSymbol;

    /**
     * Constructs an Expression using two or one variable Types and a Connector
     * @param leftExpressionType Type of variable on the left, can be null if unary expression
     * @param rightExpressionType Type of variable on the right, should never null
     * @param expressionSymbol, operator
     */
    Expression (Type leftExpressionType, Type rightExpressionType, Connector expressionSymbol){
        this.leftExpressionType = leftExpressionType;
        this.rightExpressionType = rightExpressionType;
        this.expressionSymbol = expressionSymbol;
    }

    //Methods

    /**
     * Overrides Object's equals method to compare equality of individual expression parts rather than object instance
     * @param o Object to be compared to this
     * @return true if objects contain the same Types and a Connector
     */
    public boolean equals(Object o){
        if (o == this){
            return true;
        }

        if (!(o instanceof Expression)){
            return false;
        }
        Expression ex = (Expression) o;

        return (this.leftExpressionType.equals(ex.leftExpressionType) &&
                this.expressionSymbol.equals(ex.expressionSymbol) &&
                this.rightExpressionType.equals(ex.rightExpressionType));
    }

    /**
     * Overrides Object's hashcode method
     * @return An integer that will be identical for the same Expressions and different otherwise
     */
    @Override
    public int hashCode() {
        int result = 17; //arbitrary prime number to differentiate between Expressions
        result = 31 * result + leftExpressionType.hashCode();
        result = 19 * result + expressionSymbol.hashCode();
        result = 23 * result + rightExpressionType.hashCode();
        return result;
    }

    /**
     * Getter for left side of an expression
     * @return The left variable Type of an expression
     */
    Type getLeftExpressionType(){
        return leftExpressionType;
    }

    /**
     * Getter for right side of an expression
     * @return The right variable Type of an expression
     */
    Type getRightExpressionType(){
        return rightExpressionType;
    }

    /**
     * Getter for connector
     * @return The connector type (+,-,/,*) of an expression
     */
    Connector getExpressionSymbol(){
        return expressionSymbol;
    }

    /**
     * Setter for the left side of an expression
     * @param leftExpressionType new Type of the left side of an expression
     * @throws NullPointerException if the Type is null
     */
    void setLeftExpressionType(Type leftExpressionType){
        Objects.requireNonNull(leftExpressionType, "Type cannot be null.");
        this.leftExpressionType = leftExpressionType;
    }

    /**
     * Setter for the right side of an expression
     * @param rightExpressionType new Type of the right side of an expression
     * @throws NullPointerException if the Type is null
     */
    void setRightExpressionType(Type rightExpressionType){
        Objects.requireNonNull(rightExpressionType, "Type cannot be null.");
        this.rightExpressionType = rightExpressionType;
    }

    /**
     * Setter for the Connector
     * @param expressionSymbol new Connector of an Expression
     * @throws NullPointerException if the Connector is null
     */
    void setExpressionSymbol (Connector expressionSymbol){
        Objects.requireNonNull(expressionSymbol, "Connector type cannot be null.");
        this.expressionSymbol = expressionSymbol;
    }
}

