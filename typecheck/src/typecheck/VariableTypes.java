package typecheck;

import parser.Variable;

import java.util.HashMap;
import java.util.Map;

/**
 * Java class VariableTypes
 * VariableTypes is an entity that stores a set of type rules by mapping an Variable → Type.
 * It contains a method that allows insertion of more variables and their respective types,
 * as well as a method that retrieves a Type of a given Variable.
 *
 * @author Anna Sedlackova, Jason Shin
 * contact: axs1202@case.edu, jjs270@case.edu
 * @version 1.1, 25 Apr 2019
 */
final class VariableTypes {

    //Fields

    /**
     * Map that stores the variable types with Variable as a key and resulting Type as a value
     */
    private Map<Variable, Type> types = new HashMap<>();

    //Methods

    /**
     * Inserts an additional mapping if neither the key nor the value are null
     * @param variable the key
     * @param type the resulting Type mapped to a variable
     */
    void addVariableType(Variable variable, Type type){
        if (variable != null && type != null){
            types.put(variable, type);
        }
    }

    /**
     * Retrieves the mapped Type of an input variable
     * @param variable key, variable with type to be determined
     * @return Resulting Type as stored in the map
     */
    Type variableType(Variable variable){
        return types.get(variable);
    }

    /**
     * TestHook class necessary for testing
     */
    class TestHook {
    	Map<Variable, Type> getMap() {
    		return VariableTypes.this.types;
    	}
    }
}
