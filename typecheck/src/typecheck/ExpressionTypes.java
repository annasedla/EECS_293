package typecheck;

import java.util.HashMap;
import java.util.Map;

/**
 * Java class ExpressionTypes
 * ExpressionTypes is an entity that stores a set of conversion rules by mapping an Expression → Type.
 * It contains a method that allows insertion of more rules, as well as a method that retrieves a Type of an Expression.
 *
 * @author Anna Sedlackova, Jason Shin
 * contact: axs1202@case.edu, jjs270@case.edu
 * @version 1.1, 25 Apr 2019
 */
final class ExpressionTypes {

    //Fields

    /**
     * Map that stores the rules with Expression as a key and resulting Type as a value
     */
    private Map<Expression, Type> rules = new HashMap<>();

    //Methods

    /**
     * Inserts an additional rule inside the map if neither the key nor the value are null
     * @param expression the key
     * @param type the resulting Type of an Expression operation
     */
    void addRule(Expression expression, Type type){

        if (expression != null && type != null) {
            rules.put(expression, type);
        }
    }

    /**
     * Retrieves the resulting Type of an Expression operation
     * @param expression key, an Expression whose type is to be determined
     * @return Resulting Type as stored in the map
     */
    Type expressionType(Expression expression){
        return rules.get(expression);
    }

    /**
     * TestHook class necessary for testing
     */
    class TestHook {
    	Map<Expression, Type> getMap() {
    		return ExpressionTypes.this.rules;
    	}
    }
}
