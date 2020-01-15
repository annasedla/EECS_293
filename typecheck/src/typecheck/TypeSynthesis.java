package typecheck;

import parser.*;
import java.util.*;

/**
 * Java class TypeSynthesis
 * TypeSynthesis is an entity that contains the main algorithm for analyzing the resulting Type of a parse tree.
 * ExpressionTypes and Variables Types are necessary for analysis.
 *
 * @author Anna Sedlackova, Jason Shin
 * contact: axs1202@case.edu, jjs270@case.edu
 * @version 1.1, 25 Apr 2019
 */
public final class TypeSynthesis {

    //Fields

    /**
     * The mappings between a Variable and its Type
     */
    private VariableTypes variableTypes;

    /**
     * The rules that determine resulting Type of an Expression
     */
    private ExpressionTypes typeConversionRules;

    /**
     * Root of the parsed tree
     */
    private Optional<Node> parsedTree;

    /**
     * Constructs a TypeSynthesis object containing tree root, Expression-to-Type mappings, Variable-toType mappings
     * @param parsedTree root of the tree to be parsed
     * @param variableTypes map of Variables to Types
     * @param typeConversionRules map of Expressions to Types
	 * @throws NullPointerException if any of the parameters are null
     */
    TypeSynthesis(Optional<Node> parsedTree, VariableTypes variableTypes, ExpressionTypes typeConversionRules){
            this.variableTypes = Objects.requireNonNull(variableTypes);
            this.parsedTree = Objects.requireNonNull(parsedTree);
            this.typeConversionRules = Objects.requireNonNull(typeConversionRules);
    }

    //Methods

	/**
     * Evaluates Type of parsedTree's root, throws an exception if the tree is not build properly
     * @return Resulting Type of the root of the tree
     * @throws UndeterminableTypeException if the tree is invalid
     */
    public Type evaluateRootType() {
			Node parsedTreeNode = this.parsedTree.get();     // Gets the Node from the Optional
            //ensures that tree is valid before analyzing it
            if (TypeUtilities.isTreeValid(parsedTreeNode)) {
            	return traverseTree(parsedTreeNode);
            }
            throw new UndeterminableTypeException("Input Tree was Invalid.");
    }

    /**
     * Traverses the tree and returns the final type at each level until the top
     * @param parsedTree the root of the tree to be parsed
     * @return Type of the root node if available
     */
    private Type traverseTree(Node parsedTree) {
        Expression exp = new Expression(Type.EMPTY, Type.EMPTY, Connector.empty); // Creation of a new expression with empty fields

		// Determines the type from left to right of the given expression
        for (Node child : parsedTree.getChildren()) {
        	exp = evalExpressionAndSetLeft(addChildToExpression(child, exp));
        }
        return exp.getLeftExpressionType();
    }

    /**
     * Attempts to add a child node to the expression
     * @param child child node whose contents are added to an expression
     * @param exp expression to be altered
     * @return Altered expression is returned
     * @throws UndeterminableTypeException if the Type of a variable could not be found in the mapping
     */
    private Expression addChildToExpression(Node child, Expression exp) {
    	Type childType; // childType declared to be set depending on what the child node is

    	// Adds the child to the appropriate part of the expression (left, right, operator)

		// If the child is an InternalNode, call traverseTree to determine its type.
		if (!child.isLeaf()) {
    		childType = traverseTree(child);
    	}

		// If the child is an operator, add its corresponding connector to the expression
    	else if (child.isOperator()) {
    	    exp.setExpressionSymbol((Connector)((LeafNode)child).getToken());
    		return exp;
    	}

    	// Ignore parenthesis
    	else if (!TypeUtilities.isParenthesis(child)) {
    	    // Look up the variable in variableTypes to obtain its type
    		childType = this.variableTypes.variableType((Variable) ((LeafNode) child).getToken());
    	    if (childType == null) {
    	    	throw new UndeterminableTypeException("Variable could not be found in Variable Types");
			}
    	} else {
    	    return exp;
    	}
    	return TypeUtilities.addTypeToExpression(childType, exp);
    }

    /**
     * Evaluates the type of the expression if right part of expression filled, otherwise return the original expression
     * @param exp input expression to be tentatively modified
     * @return unchanged expression, if right part unmodified, otherwise Connector and right Expression are empty
     * @throws UndeterminableTypeException If the expression does not exist in the Type Conversion Rules
     */
    private Expression evalExpressionAndSetLeft(Expression exp) {
        Type rightExpression = exp.getRightExpressionType();             // Get the right type from the given expression

		// Expression not filled yet, so return unmodified expression
		if (rightExpression.equals(Type.EMPTY)) {
    		return exp;
    	}
    	Type typeOfExpression = typeConversionRules.expressionType(exp); // Look up the given expression's type in the Type Conversion Rules
    	if (typeOfExpression == null) {
    		throw new UndeterminableTypeException("Expression not found in Type Conversion Rules");
    	}

    	// Set evaluated expression to the expression's left type and reset other fields
    	exp.setLeftExpressionType(typeOfExpression);
    	exp.setExpressionSymbol(Connector.empty);
    	exp.setRightExpressionType(Type.EMPTY);
    	return exp;
    }

    /**
     * TestHook class necessary for testing
     */
    class TestHook {

    	Type traverseTree(Node parsedTree) {
    		return TypeSynthesis.this.traverseTree(parsedTree);
    	}
    	Expression addChildToExpression(Node child, Expression exp) {
    		return TypeSynthesis.this.addChildToExpression(child, exp);
    	}
    	Expression evalExpressionAndSetLeft(Expression exp) {
    		return TypeSynthesis.this.evalExpressionAndSetLeft(exp);
    	}
    }
}
