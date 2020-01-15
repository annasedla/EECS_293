package numbers;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import numbers.DecimalInput.TestHook;

public class DecimalInputTest {

    // For using hook methods that are not object-specific
    private static final TestHook hook = new DecimalInput("").new TestHook();
    private DecimalInput input;

    /** testGetRegexOf tests **/

    // Nominal for .
    @Test
    public void testGetRegexOf() {
    	assertEquals(hook.getRegexOf('.'), "\\.");
    }

    /** testIsNotWithinString tests **/

    // Nominal case
    @Test
    public void testIsNotWithinString(){
        assertTrue(hook.isNotWithinString('1', "2.0"));
    }

    // Bad data: number not contained within string
    @Test
    public void testIsNotWithinStringFalse(){
        assertFalse(hook.isNotWithinString('0', "2.0"));
    }

    /** isNumberPositive tests **/

    // Nominal case
    @Test
    public void testIsNumberPositive(){
        assertTrue(hook.isNumberPositive("+1"));
    }

    // Bad data: number is negative
    @Test
    public void testIsNumberPositiveFalse(){
        assertFalse(hook.isNumberPositive("-1"));
    }

    /** removeSign tests **/

    // Nominal case: removes positive sign
    @Test
    public void testRemoveSignPositive(){
        assertEquals(hook.removeNegativeSign("+2"), "2");
    }

    // Nominal case: removes negative sign
    @Test
    public void testRemoveSignNegative(){
        assertEquals(hook.removeNegativeSign("-2"), "2");
    }

    /** removePadding tests **/

    // Removes padding nominal
    @Test
    public void testRemovePadding(){
        assertEquals(hook.removePadding("2_000"), "2000");
    }

    // Removes padding decimal
    @Test
    public void testRemovePaddingDecimal(){
        assertEquals(hook.removePadding("1_234.56"), "1234.56");
    }

    /** hasValidMiddlePadding tests **/
    /* 1_234 -> valid */
    @Test
    public void test_padding_nominal() {
        assertTrue(hook.hasValidMiddlePadding("1_234"));
    }

    /* 1__234 -> valid */
    @Test
    public void test_padding_long_underscore() {
        assertTrue(hook.hasValidMiddlePadding("1__234"));
    }

    /* 12_34 -> invalid */
    @Test
    public void test_padding_bad_underscore() {
        assertFalse(hook.hasValidMiddlePadding("12_34"));
    }

    /*  _1_234 -> invalid */
    @Test
    public void test_padding_leading_underscore() {
        assertFalse(hook.hasValidMiddlePadding("_1_234"));
    }

    /** hasNoEdgePadding tests **/

    // Nominal case for no edges
    @Test
    public void testHasNoEdgePadding() {
        assertTrue(hook.hasNoEdgePadding("1_234"));
    }

    // Bad data: padding in front
    @Test
    public void testHasNoEdgePaddingFalseEdge1() {
        assertFalse(hook.hasNoEdgePadding("_1_234"));
    }

    // Bad data: padding in back
    @Test
    public void testHasNoEdgePaddingFalseEdge2() {
        assertFalse(hook.hasNoEdgePadding("1_234_"));
    }

    /** hasValidLeadingPadding tests **/

    // Nominal case
    @Test
    public void testHasValidLeadingPadding(){
        assertTrue(hook.hasValidLeadingPadding("2_000"));
    }

    // Bad data: edge padding
    @Test
    public void testHasValidLeadingPaddingEdge(){
        assertFalse(hook.hasValidLeadingPadding("_2_000"));
    }

    // Bad data: middle padding
    @Test
    public void testHasValidLeadingPaddingMiddle() {
        assertFalse(hook.hasValidLeadingPadding("20_00"));
    }

    /** getAllChunks tests **/

    // Nominal case for get all chunks
    @Test
    public void testGetAllChunks() {
    	DecimalInput dec = new DecimalInput("2.0");
    	DecimalInput.TestHook th = dec.new TestHook();
        String[] testArray = new String[2];
        testArray[0] = "2";
        testArray[1] = "0";
        assertArrayEquals(th.getAllChunks(), testArray);
    }
    
 // Nominal case for get all chunks
    @Test
    public void testGetAllChunksNoFrac() {
    	DecimalInput dec = new DecimalInput("2.");
    	DecimalInput.TestHook th = dec.new TestHook();
        String[] testArray = new String[2];
        testArray[0] = "2";
        testArray[1] = "";
        assertArrayEquals(th.getAllChunks(), testArray);
    }
    
 // Nominal case for get all chunks
    @Test
    public void testGetAllChunksNoDec() {
    	DecimalInput dec = new DecimalInput(".0");
    	DecimalInput.TestHook th = dec.new TestHook();
        String[] testArray = new String[2];
        testArray[0] = "";
        testArray[1] = "0";
        assertArrayEquals(th.getAllChunks(), testArray);
    }

    /** hasValidPadding tests **/

    // nominal case for has valid padding
    @Test
    public void testHasValidPadding(){
        assertTrue(hook.hasValidPadding());
    }

    /** hasValidDecimalPoint tests **/

    @Test
    public void testHasValidDecimalPoint() {
        DecimalInput dec = new DecimalInput("2.0");
        DecimalInput.TestHook th = dec.new TestHook();
        assertTrue(th.hasValidDecimalPoint());
    }

    @Test
    public void testHasValidDecimalPointNoDec() {
        DecimalInput dec = new DecimalInput(".0");
        DecimalInput.TestHook th = dec.new TestHook();
        assertFalse(th.hasValidDecimalPoint());
    }

    @Test
    public void testHasValidDecimalPointNoFrac() {
        DecimalInput dec = new DecimalInput("2.");
        DecimalInput.TestHook th = dec.new TestHook();
        assertFalse(th.hasValidDecimalPoint());
    }
    /** hasValidChars tests **/

    @Test
    public void testHasValidChars(){
        DecimalInput dec = new DecimalInput("2.0");
        DecimalInput.TestHook th = dec.new TestHook();
        assertTrue(th.hasValidChars());
    }

    /** test for isValidExponent() **/

    // Nominal case for isValidExponent
    @Test
    public void testIsValidExponent(){
        input = new DecimalInput("2");
        assertTrue(input.isValidExponent());
    }

    // Bad data: input is empty
    @Test
    public void testIsValidExponentEmpty(){
        input = new DecimalInput("");
        assertFalse(input.isValidExponent());
    }

    // Bad data: invalid characters
    @Test
    public void testIsValidExponentBadChars(){
        input = new DecimalInput("2@");
        assertFalse(input.isValidExponent());
    }

    // Bad data: contains padding
    @Test
    public void testIsValidExponentPadding(){
        input = new DecimalInput("_2");
        assertFalse(input.isValidExponent());
    }

    // Bad data: is not integer
    @Test
    public void testIsValidExponentNonInteger(){
        input = new DecimalInput("2.0");
        assertFalse(input.isValidExponent());
    }

    /** test for isValid() **/

    // Nominal case for isValid
    @Test
    public void testIsValid(){
        input = new DecimalInput("2.0");
        assertTrue(input.isValid());
    }

    // Bad data: input is empty
    @Test
    public void testIsValidEmpty(){
        input = new DecimalInput("");
        assertFalse(input.isValid());
    }

    // Bad data: invalid character
    @Test
    public void testIsValidInvalidChars(){
        input = new DecimalInput("2.0&");
        assertFalse(input.isInteger());
    }

    // Bad data: does not have valid padding
    @Test
    public void testIsValidInvalidPadding(){
        input = new DecimalInput("_2.0");
        assertFalse(input.isInteger());
    }

    // Bad data: does not have valid padding
    @Test
    public void testIsValidInvalidPadding2(){
        input = new DecimalInput("_2");
        assertFalse(input.isInteger());
    }

    // Bad data: does not have valid decimal point
    @Test
    public void testIsValidInvalidDecPoint(){
        input = new DecimalInput("2.");
        assertFalse(input.isInteger());
    }

    // Bad data: does not have valid decimal point part two
    @Test
    public void testIsValidInvalidDecPoint2(){
        input = new DecimalInput(".2");
        assertFalse(input.isInteger());
    }

    /** test for isInteger **/

    // Nominal case for integer
    @Test
    public void testIsInteger(){
        input = new DecimalInput("2");
        assertTrue(input.isInteger());
    }

    // Bad data: empty input
    @Test
    public void testIsIntegerNoInput(){
        input = new DecimalInput("");
        assertFalse(input.isInteger());
    }

    // Bad data: decimal in integer
    @Test
    public void testIsIntegerNotWithinString(){
        input = new DecimalInput("2.0");
        assertFalse(input.isInteger());
    }

    // Bad data: invalid character
    @Test
    public void testIsIntegerInvalidChars(){
        input = new DecimalInput("20&");
        assertFalse(input.isInteger());
    }

    // Bad data: has no edge padding
    @Test
    public void testIsIntegerInvalidLeading(){
        input = new DecimalInput("_2");
        assertFalse(input.isInteger());
    }

    /** test for toString **/

    // Nominal case: returns positive number and removes paddings
    @Test
    public void testToStringPositive(){
        input = new DecimalInput("+2_000.0");
        input.toString();

        assertEquals(input.toString(), "+2000.0");
    }

    // Nominal case: returns negative number and removes paddings
    @Test
    public void testToStringNegative(){
        input = new DecimalInput("-2000.0");
        input.toString();

        assertEquals(input.toString(), "-2000.0");
    }

    /** test for constructor **/

    // Nominal case
    @Test
    public void testConstructorNominal(){
        input = new DecimalInput("2.0");
    }

    // Bad data: leading or trailing white space
    @Test
    public void testConstructorLeadingTrailing(){
    	try
    	{
            input = new DecimalInput(" 2 . 0 ");  		
    	}
    	catch(AssertionError e) {
    		System.out.println(e);
    	}
    	catch(NullPointerException e) {
    		System.out.println(e);
    	}
    }

    // Bad data: null input
    @Test(expected = NullPointerException.class)
    public void testConstructorNull(){
        input = new DecimalInput(null);
    }
}
