package numbers;

import java.util.Optional;

/* Identifies and parses legal floating point constants into Double
 * This class describes a floating chunk as either the fractional portion following
 * a decimal or the exponent portion
 */
class FloatingPointParser {
    private static final FloatingPointParser INVALID_PARSER = new FloatingPointParser("");

    //TODO fixed: default exponent should not be one but zero
    private static final DecimalInput DEFAULT_EXPONENT = new DecimalInput("0");
    private static final char EXPONENTIAL = 'e';
    private final DecimalInput base;
    private final Optional<DecimalInput> expo;

    /**
     * Makes a new FloatingPointParse instance
     * @param number number to be parsed
     */
    FloatingPointParser(String number) {
        //TODO fixed: now works with both uppercase and lowercase
        number = number.toLowerCase();
        if (number.contains(""+EXPONENTIAL)) {
            String[] numbers = number.split(""+EXPONENTIAL, 2);
            base = new DecimalInput(numbers[0]);
            expo = Optional.of(new DecimalInput(numbers[1]));
        } else {
            base = new DecimalInput(number);
            expo = Optional.empty();
        }
    }

    /**
     * Getter for exponent
     * @return the exponent
     */
    private DecimalInput getExpo() { return expo.orElseGet(() -> DEFAULT_EXPONENT); }

    /**
     * Double parser
     * @return the parser double value
     */
    public Double parseDouble() {
        assert this.isValidInput() : "Input not valid before parsing.";
        return Double.parseDouble(base+""+EXPONENTIAL+getExpo());
    }

    /**
     * A floating point constant is valid if it contains one or two floating chunks and both
     * the base and the integer exponent are valid.
     * @return true if valid
     * TODO fixed: removed hasValidBase() from here because it threw 20e-2 as an error (when base is not decimal)
     */
    public boolean isValidInput() {
        return containsAtLeastOneFloatingChunk() &&  hasValidIntegerExponent();
    }

    /**
     * Checks if either is an exponent with a decimal base OR
     * Exponent with an integer base
     * @return true if contains at least one floating chunk
     * TODO  fixed: moved has valid base here so it no longer throws an error with non decimal base
     * TODO fixed: ensures that the base is an integer for cases such as 2-2e2
     */
    private boolean containsAtLeastOneFloatingChunk() {
        return (!base.isInteger() && hasValidBase()) || (base.isInteger() && expo.isPresent());
    }

    /**
     * True if base is valid
     * @return
     */
    private boolean hasValidBase() { return base.isValid(); }

    /**
     * TODO  fixed: Changed this because exponent was checking for valid decimal which it shouldnt
     * The exponent must be a valid INTEGER
     * @return true if exponent valid
     */
    private boolean hasValidIntegerExponent() {
        return getExpo().isValidExponent();
    }

    /**
     * Builds the floating point parser
     * @param number
     * @return new FloatingPointParser instance
     */
    public static final FloatingPointParser build(String number) {
        return number != null ? new FloatingPointParser(number) : INVALID_PARSER;
    }

    /**
     * Class for testing
     */
    class TestHook {
    	FloatingPointParser fpParser = new FloatingPointParser("");

    	DecimalInput getExpo() {
    		return FloatingPointParser.this.getExpo();
    	}
    	boolean isValidInput() {
    		return FloatingPointParser.this.isValidInput();
    	}
    	boolean containsAtLeastOneFloatingChunk() {
    		return FloatingPointParser.this.containsAtLeastOneFloatingChunk();
    	}
    	boolean hasValidBase() {
    		return FloatingPointParser.this.hasValidBase();
    	}
    	boolean hasValidIntegerExponent() {
    		return FloatingPointParser.this.hasValidIntegerExponent();
    	}
    	FloatingPointParser build(String number) {
    		return fpParser.build(number);
    	}
        DecimalInput getBase() {
        	return base;
        }
    }
}
