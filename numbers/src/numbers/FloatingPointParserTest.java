package numbers;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;

import numbers.FloatingPointParser.TestHook;

public class FloatingPointParserTest {

	// For using hook methods that are not object-specific
    private static TestHook hook;

    /** Test for invalid parser **/
    @Test
    public void testBuild() {
        // no failures
        hook = FloatingPointParser.build(null).new TestHook();
    }

    /**Tests for hasValidIntegerExponent()**/

    // nominal case
    @Test
    public void testHasValidIntegerExponentNominal() {
        hook = FloatingPointParser.build("100e10").new TestHook();
    	assertTrue(hook.hasValidIntegerExponent());
    }
    
    /*
     * Bad data: Exponent is ""
     */
    @Test
    public void testHasValidIntegerExponentEmptyExpo() {
        hook = FloatingPointParser.build("100e").new TestHook();
    	assertFalse(hook.hasValidIntegerExponent());
    }
    
    /* 
     * Bad data: Exponent is not valid. Has '_'
     */
    @Test
    public void testHasValidIntegerExponentUnderscore() {
        hook = FloatingPointParser.build("100e1_1").new TestHook();
    	assertFalse(hook.hasValidIntegerExponent());
    }
    
    /*
     * Bad data: Exponent is not valid. Has unnecessary chars
     */
    @Test
    public void testHasValidIntegerExponentExtraChars() {
        hook = FloatingPointParser.build("100e1**100").new TestHook();
    	assertFalse(hook.hasValidIntegerExponent());
    }
    
    /*
     * Bad data: Exponent is not an integer. Contains '.'
     */
    @Test
    public void testHasValidIntegerExponentNonInteger() {
        hook = FloatingPointParser.build("100e10.1").new TestHook();
    	assertFalse(hook.hasValidIntegerExponent());
    }
    
    /**Tests for hasValidBase()**/

    /*
     * Nominal case
     */
    @Test
    public void testHasValidBaseNominal() {
        hook = FloatingPointParser.build("123.45").new TestHook();
        assertTrue(hook.hasValidBase());
    }

    /*
     * Bad data: no decimal
     */
    @Test
    public void testHasValidBaseNoDecimal() {
        hook = FloatingPointParser.build("12345").new TestHook();
    	assertFalse(hook.hasValidBase());
    }
    
    /*
     * Bad data: Does not have valid padding
     */
    @Test
    public void testHasValidBaseBadPadding() {
        hook = FloatingPointParser.build("123_45").new TestHook();
    	assertFalse(hook.hasValidBase());
    }
    
    /*
     * Bad data: Does not have valid decimal placing
     */
    @Test
    public void testHasValidBaseDecimalPlace() {
        hook = FloatingPointParser.build("12.3.45").new TestHook();
    	assertFalse(hook.hasValidBase());
    }
    
    /*
     * Bad data: Does not have valid chars
     */
    @Test
    public void testHasValidBaseInvalidChars() {
        hook = FloatingPointParser.build("123,45").new TestHook();
    	assertFalse(hook.hasValidBase());
    }
    
    /**Test containsAtLeastOneFloatingChunk**/
    
    /*
     * Nominal case, two chunks present
     */
    @Test
    public void testContainsAtLeastOneFloatingChunkNominal() {
        hook = FloatingPointParser.build("123e456").new TestHook();
    	assertTrue(hook.containsAtLeastOneFloatingChunk());
    }
    
    /*
     * Bad data: valid base but exponent is not present
     */
    @Test
    public void testContainsAtLeastOneFloatingChunkNoExpo() {
        hook = FloatingPointParser.build("123456").new TestHook();
    	assertFalse(hook.containsAtLeastOneFloatingChunk());
    }
    
    /*
     * Good data: Base is not valid, exponent is present
     */
    @Test
    public void testContainsAtLeastOneFloatingChunkNonValidBase() {
        hook = FloatingPointParser.build("12.34e56").new TestHook();
    	assertTrue(hook.containsAtLeastOneFloatingChunk());
    }
    
    /*
     * Good data: Base is valid, exponent is not present
     */
    @Test
    public void testContainsAtLeastOneFloatingChunkInvalidBaseNoExpo() {
        hook = FloatingPointParser.build("12.34").new TestHook();
    	assertTrue(hook.containsAtLeastOneFloatingChunk());
    }

    /** Tests for isValidInput() **/

    /*
     * Nominal case, two chunks present
     */

    @Test
    public void testIsValidInputNominal() {
        hook = FloatingPointParser.build("123e4").new TestHook();
        assertTrue(hook.isValidInput());
    }

    /*
     * Bad data: exponent invalid
     */

    @Test
    public void testIsValidInputBadExponent() {
        hook = FloatingPointParser.build("123e1.1").new TestHook();
        assertFalse(hook.isValidInput());
    }

    // contains at least one floating chunk invalid
    @Test
    public void testIsValidInputBadBase() {
        hook = FloatingPointParser.build("123").new TestHook();
        assertFalse(hook.isValidInput());
    }

    /** Tests for parseDouble **/

    // Nominal case
    @Test
    public void testParseDoubleNominal() {
        FloatingPointParser parser = new FloatingPointParser("110e2");
        assertEquals(parser.parseDouble().toString(), "11000.0");
    }

    // bad input
    @Test
    public void testParseDoubleBadInput() throws AssertionError {
        FloatingPointParser parser = new FloatingPointParser("112");
        try{
            parser.parseDouble();
        }
        catch (AssertionError e){
            System.out.println(e);
        }
        catch (NullPointerException e){
            System.out.println(e);
        }

    }

    /** Tests for getExpo()**/

    //Nominal case
    @Test
    public void testGetExpo(){
        hook = FloatingPointParser.build("100e10").new TestHook();
        assertEquals(hook.getExpo().toString(), "+10");
    }
}
