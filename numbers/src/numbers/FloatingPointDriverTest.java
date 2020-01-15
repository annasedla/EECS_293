package numbers;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.Optional;

import org.junit.Test;

import numbers.FloatingPointDriver.TestHook;
import numbers.FloatingPointDriver.TestHook;

public class FloatingPointDriverTest {
	// For using hook methods that are not object-specific
    private static final FloatingPointDriver fpDriver = new FloatingPointDriver();
    private static final TestHook hook = fpDriver.new TestHook();

    // Tests for readForValidInput
    
    /*
     * Structured Basis, input is given that does not lead to an exception when
     * reading from the BufferedReader
     */
    @Test
    public void testReadForValidInputNominal() throws IOException {
        BufferedReader input = new BufferedReader(new StringReader("1234.56"));
        assertEquals("1234.56", hook.readForValidInput(input));
    }

    /*
     * Structured Basis, given null input the method should catch and print the exception
     */
    @Test
    public void testReadForValidInputNull() throws IOException {
        assertEquals(null, hook.readForValidInput(null));
    }
    
    // Tests for createStringBuilder
    
    /*
     * Structured Basis, when given a non-null input it returns an empty builder
     */
    @Test
    public void testCreateStringBuilderNominal() {
    	String line = "1234.56";
        assertEquals("", hook.createStringBuilder(line).toString());
    }
    
    /*
     * Structured Basis, when given a null input it throws a NullPointerException
     */
    @Test(expected = NullPointerException.class)
    public void testCreateStringBuilderNull() {
        hook.createStringBuilder(null);
    }
    
    // Tests for removeWhiteSpace
    
    /*
     * Structured Basis, covers the entire for loop and internal if 
     * as 'line' contains both whitespace and not whitespace
     * Nominal
     */
    @Test
    public void testRemoveWhiteSpaceNominal() {
    	StringBuilder builder = new StringBuilder();
    	String line = " 1 2 3 4 . 5 6 ";
    	assertEquals("1 2 3 4 . 5 6", hook.removeWhiteSpace(builder, line));
    }

    /*
     * Boundary, testing for when the given 'line' is empty
     */
    @Test
    public void testRemoveWhiteSpaceEmptyString() {
    	StringBuilder builder = new StringBuilder();
    	String line = "";
    	assertEquals("", hook.removeWhiteSpace(builder, line));
    }

    /*
     * Good data, testing for when the given 'line' contains only whitespace
     */
    @Test
    public void testRemoveWhiteSpaceOnlyWhitespace() {
    	StringBuilder builder = new StringBuilder();
    	String line = "         ";
    	assertEquals("", hook.removeWhiteSpace(builder, line));
    }

    /*
     * Good data, testing for when the given 'line' contains no whitespace
     */
    @Test
    public void testRemoveWhiteSpaceNoWhitespace() {
    	StringBuilder builder = new StringBuilder();
    	String line = "1234.56";
    	assertEquals("1234.56", hook.removeWhiteSpace(builder, line));
    }

    /*
     * Bad data, the builder should always be empty when passed in here
     */
    @Test
    public void testRemoveWhiteSpaceBuilderNotEmpty() {
    	StringBuilder builder = new StringBuilder("");
    	String line = "Data";
    	assertEquals("Data", hook.removeWhiteSpace(builder, line));
    }
    
    // Following tests are for the buildParser method
    
    /*
     * Structured Basis, Nominal
     */
    @Test
    public void testBuildParserNominal() {
    	FloatingPointParser parser = new FloatingPointParser("1234.56");
    	assertSame(parser, hook.buildParser(parser));
    }
    
    /*
     * Bad data, can't pull the base value once the parser is made
     */
    @Test
    public void testBuildParserNull() {
    	FloatingPointParser parser = new FloatingPointParser("1234.56");
    	FloatingPointParser nullParser = hook.buildParser(null);
    	FloatingPointParser.TestHook th = nullParser.new TestHook();
    	assertEquals("+input that is r", th.getBase().toString());
    }
    
    // Tests for getFloatingPointParser
    
    /*
     * 
     */
    @Test
    public void testGetFloatingPointParserNullInput() {
        BufferedReader input = new BufferedReader(new StringReader("1234.56"));
    	FloatingPointParser parser = hook.getFloatingPointParser(null);
    	FloatingPointParser.TestHook th = parser.new TestHook();
    	assertEquals("+input that is r", th.getBase().toString());
    }
    
    /*
     * 
     */
    @Test
    public void testGetFloatingPointParserNominalEmptyReader() throws IOException {
        BufferedReader input = new BufferedReader(new StringReader(""));
        //System.out.println(input.readLine().toString());
    	FloatingPointParser parser = hook.getFloatingPointParser(input);
    	FloatingPointParser.TestHook th = parser.new TestHook();
    	assertEquals("+bad input", th.getBase().toString());
    }
    
    /*
     * 
     */
    @Test
    public void testGetFloatingPointParserNominal() {
        BufferedReader input = new BufferedReader(new StringReader("1234.56"));
    	FloatingPointParser parser = hook.getFloatingPointParser(input);
    	FloatingPointParser.TestHook th = parser.new TestHook();
    	assertEquals("+1234.56+0", th.getBase().toString() + th.getExpo().toString());
    }
    
    // Test for printOutput
    
    /*
     * 
     */
    @Test
    public void testPrintOutputNominal() {
        BufferedReader input = new BufferedReader(new StringReader("1234.56"));
        FloatingPointDriver driver = new FloatingPointDriver();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Optional<Double> result = driver.runFloatingPointParser(input);
        FloatingPointDriver.printOutput(result);
        assertEquals(Optional.of("1234.56").toString(), Optional.of(outContent).toString().replace("\n", "").replace("\r", ""));
    }

    /*
     * 
     */
    @Test
    public void testPrintOutputNull() {
        BufferedReader input = new BufferedReader(new StringReader("1234.56"));
        FloatingPointDriver driver = new FloatingPointDriver();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Optional<Double> result;
        FloatingPointDriver.printOutput(Optional.empty());
        assertEquals("Invalid Input", outContent.toString().replace("\n", "").replace("\r", ""));
    }
    
    // Running main for code coverage MAIN METHOD WILL NOT BE CALLED
    @Test
    public void testMain() {
    	String[] args = new String[] {"1234.56"};
    	FloatingPointDriver.main(args);
    }
}
