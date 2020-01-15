package numbers;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/* Validation class for simple decimal numbers (an integer with optional fractional chunk) */
/* Global Precondition: number is not null nor does it have leading or trailing spaces
 */
public class DecimalInput {

    private static final char DECIMAL = '.';
    private static final char PADDING = '_';
    private static final Set<Character> SIGN_SET;
    private static final Set<Integer> VALID_CHAR_SET; // int set to match chars IntStreams
    static { // Setup set of valid signs
        Set<Character> signs = new HashSet<>();
        signs.add('-');
        //TODO added plus to the sign set
        signs.add('+');
        SIGN_SET = Collections.unmodifiableSet(signs);
    }
    static { // Setup set of valid characters in a decimal
        Set<Character> validChars = new HashSet<>();
        validChars.add('0');
        validChars.add('1');
        validChars.add('2');
        validChars.add('3');
        validChars.add('4');
        validChars.add('5');
        validChars.add('6');
        validChars.add('7');
        validChars.add('8');
        validChars.add('9');
        validChars.add(DECIMAL);
        validChars.add(PADDING);
        VALID_CHAR_SET = Collections.unmodifiableSet(validChars
                .stream()
                .mapToInt(Character::charValue)
                .mapToObj(Integer::valueOf)
                .collect(Collectors.toSet()));
    }

    private final boolean isPositive;
    private final String number;

    /**
     * Constructor for decimal input
     * @param number input number to be validated
     */
    DecimalInput(String number) {
        assert number != null : "Number given should not be null.";
        assert number.trim().equals(number)
                : "Number given should not have leading or trailing whitespace: \""+number+"\"";
        this.isPositive = isNumberPositive(number);
        this.number = removeSign(number);
    }

    /**
     * Prints the number to the string format
     * @return number as string
     */
    public String toString() {
        return (isPositive ? "+" : "-") + removePadding(number);
    }

    /**
     * TODO changed so that cases like these 2-2e2 wont get executed
     * TODO added check so that number is not empty
     * Checks that number is a valid integer
     * @return true if number is a valid integer
     */
    boolean isInteger() {
        return !number.isEmpty() && isNotWithinString(DECIMAL,number) && hasValidChars() && hasValidLeadingPadding(number);
    }

    /**
     * TODO added check for number not empty
     * Checks that number is valid and nonempty
     * @return true if number is valid
     */
    boolean isValid() {
        return !number.isEmpty() && hasValidChars() && hasValidDecimalPoint() && hasValidPadding();
    }

    /**
     * TODO added multiple checks because exponents cannot have decimal or padding
     * @return
     */
    boolean isValidExponent(){
        return !number.isEmpty() && hasValidChars() && isNotWithinString('_', number) && isInteger();
    }

    /**
     * True if it contains a valid char
     * @return
     */
    private boolean hasValidChars() {
        return number.chars().allMatch(VALID_CHAR_SET::contains);
    }

    /**
     * A number is considered to have a valid decimal point if none exist, or only one
     *  exists that splits the string into two further numbers.
     * @return true if decimal point is valid
     */
    private boolean hasValidDecimalPoint() {
        String[] numbers = getAllChunks();

        //TODO fixed this because numbers will never have length other than 2
        return numbers.length == 2
                && !numbers[0].isEmpty()
                && !numbers[1].isEmpty();
    }

    /* A number is considered to have valid padding if they only appear
     * in the place of a comma in the leading number.
     */

    /**
     * TODO rewritten for clarity
     * Returns true if the number has a valid padding
     * @return true if padding valid, false otherwise
     */
    private boolean hasValidPadding() {
    	String[] numbers = getAllChunks();

        if (numbers.length == 2 && hasValidLeadingPadding(numbers[0])){
            return isNotWithinString(PADDING, numbers[1]);
        } else {
            return false;
        }
    }

    /**
     * TODO rewritten so its easier to process
     * TODO fixed so that it works for dot at the end of an expression such as 2.2.
     * Split the number by decimal
     * @return string array with just numbers
     */
    private String[] getAllChunks() {
        String[] resultingString = number.split(getRegexOf(DECIMAL), -1);
    	return resultingString;
    }

    /**
     * Checks if paddings are correct by calling two other methods
     * @param leading
     * @return
     */
    private static boolean hasValidLeadingPadding(String leading) {
        return hasNoEdgePadding(leading) && hasValidMiddlePadding(leading);
    }

    /**
     * Tests if there is no padding on either edge of the number
     * @param leading
     * @return if no edge paddings occur
     */
    private static boolean hasNoEdgePadding(String leading) {
        return leading.charAt(0) != PADDING && leading.charAt(leading.length()-1) != PADDING;
    }

    /**
     * TODO changed this method entirely to work properly
     * Tests if middle padding is valid
     * @param leading
     * @return true if valid
     */
    private static boolean hasValidMiddlePadding(String leading) {
        /* The padding (underscores) in the middle of a decimal are valid when
         * followed by a multiple of three digits
         *
         * ex: 1_234, 1__234 => true
         *     12_34, _1_234 => false
         */

        return Arrays.stream(leading.split("_", -1)).skip(1).allMatch(x->x.length()%3 == 0);
    }

    /**
     * Removes paddings
     * @param number
     * @return number with no paddings
     */
    private static String removePadding(String number) {
        return number.replaceAll(getRegexOf(PADDING), "");
    }

    /**
     * Removes sign from a number
     * @param number
     * @return number without a sign
     */
    private static String removeSign(String number) {
        return number.isEmpty() || !SIGN_SET.contains(number.charAt(0)) ? number : number.substring(1, number.length());
    }

    /**
     * TODO changed so it checks that the first character isnt negative
     * Determines if a number is positive
     * @param number
     * @return true if it is, false otherwise
     */
    private static boolean isNumberPositive(String number) {
        return !number.isEmpty() && number.charAt(0) != '-';
    }

    /**
     * Determines if a digit is or is not within a string
     * @param c
     * @param str
     * @return true if it s not, false otherwise
     */
    private static boolean isNotWithinString (char c, String str) {
        return str.indexOf(c) < 0;
    }

    /**
     * TODO added an if statement so regex works
     * Returns regex of a character
     * @param ch
     * @return string
     */
    private static String getRegexOf(char ch) {
        if (ch == '.'){
            return "\\.";
        }
        String result = "" + ch;
        return result;
    }

    class TestHook {

        //test case nominal
        DecimalInput decInput = new DecimalInput("2_000.0");

        String getRegexOf(char ch) {
        	return DecimalInput.getRegexOf(ch);
        }
        boolean isNotWithinString (char c, String str) {
        	return DecimalInput.isNotWithinString (c, str);
        }
        boolean isNumberPositive(String number) {
        	return DecimalInput.isNumberPositive(number);
        }
        String removeNegativeSign(String number) {
        	return DecimalInput.removeSign(number);
        }
        String removePadding(String number) {
        	 return DecimalInput.removePadding(number);
        }
        boolean hasValidMiddlePadding(String leading) {
            return DecimalInput.hasValidMiddlePadding(leading);
        }
        boolean hasNoEdgePadding(String leading) {
        	return DecimalInput.hasNoEdgePadding(leading);
        }
        boolean hasValidLeadingPadding(String leading) {
        	return DecimalInput.hasValidLeadingPadding(leading);
        }
        String[] getAllChunks() {
        	return DecimalInput.this.getAllChunks();
        }
        boolean hasValidPadding() {
        	return decInput.hasValidPadding();
        }
        boolean hasValidDecimalPoint() {
        	return DecimalInput.this.hasValidDecimalPoint();
        }
        boolean hasValidChars() {
        	return DecimalInput.this.hasValidChars();
        }
    }
}
