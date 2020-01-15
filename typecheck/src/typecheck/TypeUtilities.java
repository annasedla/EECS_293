package typecheck;

import parser.*;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Java class TypeUtilities
 * TypeUtilities is an entity that contains helper functions used by TypeSynthesis to perform valid input checks.
 * It also perform operations that do not depend on the specific TypeSynthesis instance.
 *
 * @author Anna Sedlackova, Jason Shin
 * contact: axs1202@case.edu, jjs270@case.edu
 * @version 1.1, 25 Apr 2019
 */
abstract class TypeUtilities {

    //Methods

    /**
     * Checks if each open parenthesis has a matching closed parenthesis using a stack
     * @param input root Node of a parse tree to be checked
     * @return True if parentheses are balanced, false otherwise
     */
    static boolean areParenthesisValid(Node input) {
        Stack<Character> stack = new Stack<>();              // Initialization of Stack
        // Matches each open parenthesis in the tree with a closed parenthesis
        for (Node n : input.getChildren()) {
            if (n.isLeaf()) {
                Token nodeToken = ((LeafNode) n).getToken(); // Token of the LeafNode
                if (nodeToken.matches(TerminalSymbol.OPEN)) {
                    stack.push('(');
                } else if (nodeToken.matches(TerminalSymbol.CLOSE)) {
                    try {
                        stack.pop();
                    }
                    // Occurs if too many closed parenthesis
                    catch (EmptyStackException e) {
                        return false;
                    }
                }
            }
        }
        return stack.isEmpty();
    }

    /**
     * Checks if the specific node contains a parenthesis
     * @param n node to be examined
     * @return True if parenthesis is present, false otherwise
     */
    static boolean isParenthesis(Node n) {
        String nodeString = n.toString(); // String representation of n
        return (nodeString.equals("(") || nodeString.equals(")"));
    }

    /**
     * If the operator is empty in the expression, set the type to expression's left type, else set the type to expression's right type
     * @param t type to add to the expression
     * @param exp expression to add the type to
     * @return Expression with inserted type
     */
    static Expression addTypeToExpression(Type t, Expression exp) {
        if (!exp.getExpressionSymbol().equals(Connector.empty)) {
            exp.setRightExpressionType(t);
        } else {
            exp.setLeftExpressionType(t);
        }
        return exp;
    }

    /**
     * Checks if the children list alternates between a node and an operator
     * @param prevIsOperator true if previous child was an operator, false otherwise
     * @param child child to be checked
     * @return True if the alternating pattern is preserved, false otherwise
     */
    static boolean isAlternating(boolean prevIsOperator, Node child) {
        if (child.isOperator() == prevIsOperator) {
            return false;
        }
        return child.isLeaf() || isTreeValid(child);
    }

    /**
     * Checks if the tree is valid by parsing its root
     * @param root root of the tree
     * @return return true if tree is valid, false otherwise
     */
    static boolean isTreeValid(Node root) {
        if (areParenthesisValid(root)) {
            // Determines format of root and parses it accordingly
            if (root.isStartedByOperator()) {
                return isUnaryOpTreeValid(root);
            } else {
                return isBinaryOpTreeValid(root);
            }
        }
        return false;
    }

    /**
     * Check if the node contains a valid unary operator pattern (missing left Expession)
     * @param n node to be evaluated
     * @return True if node's children follow [- Node] pattern, false otherwise
     */
    static boolean isUnaryOpTreeValid(Node n) {
        if (n.getChildren().size() != 2) {
            return false;
        } else {
            Node secondChild = ListHandler.listSecond(n.getChildren()); // Second child of node's children list
            if (secondChild.isOperator()) {
                return false;
            } else if (!secondChild.isLeaf()) {
                return isTreeValid(ListHandler.listSecond(n.getChildren()));
            }
        }
        return true;
    }

    /**
     * Checks if the node contains a valid binary operator pattern (left Expression, Connector, right Expression)
     * @param n node to be evaluated
     * @return true if node's children follows [Node Operator Node ...] pattern, false otherwise
     */
    static boolean isBinaryOpTreeValid(Node n) {
        boolean prevIsOperator = true;  // Pretend previous node is an operator because expression must start with a non-operator

        // Check the alternating behavior of [Node Operator Node ...] pattern
        for (Node child : n.getChildren()) {
            if (!isParenthesis(child)) {
                if (!isAlternating(prevIsOperator, child)) {
                    return false;
                }
                prevIsOperator = !prevIsOperator;
            }
        }

        // Checks that operator does not end an expression
        if (ListHandler.listLast(n.getChildren()).isOperator()) {
            return false;
        }
        return true;
    }
}
