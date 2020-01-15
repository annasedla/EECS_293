package typecheck;

/**
 * Java class UndeterminableTypeException
 * This class extends RuntimeException to throw a custom error message.
 *
 * @author Anna Sedlackova, Jason Shin
 * contact: axs1202@case.edu, jjs270@case.edu
 * @version 1.1, 25 Apr 2019
 */
public class UndeterminableTypeException extends RuntimeException{

    /**
     * Custom exception for when the Type of an Expression or a Variable cannot be determined
     * @param msg custom error message
     */
    public UndeterminableTypeException(String msg) {
        super(msg);
    }
}
