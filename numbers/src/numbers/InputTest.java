package numbers;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

import org.junit.Test;

import numbers.DecimalInput.TestHook;

/** Some example tests of parser **/
public class InputTest {
    // For using hook methods that are not object-specific
    private static final TestHook hook = new DecimalInput("").new TestHook();

    /* 
     * @throws IOException
    */
    @Test
    public void testValidDataText() throws IOException {
    	System.out.println("Working Directory = " +
                System.getProperty("user.dir") + "/src/numbers/validData.txt");
    	FileReader file = new FileReader(System.getProperty("user.dir") + "/src/numbers/validData.txt");
    	FileReader expectedFile = new FileReader(System.getProperty("user.dir") + "/src/numbers/expectedValid.txt");
    	BufferedReader reader = new BufferedReader(file);
    	BufferedReader expectedReader = new BufferedReader(expectedFile);
		FloatingPointDriver driver = new FloatingPointDriver();
    	for (int i = 0; i < 23; i++) {
    		BufferedReader line = new BufferedReader(new StringReader(reader.readLine()));
    		BufferedReader expectedLine = new BufferedReader(new StringReader(expectedReader.readLine()));
            Optional<Double> result = driver.runFloatingPointParser(line);
            System.out.println(result.isPresent() ? result.get() : "Invalid Input");
            assertEquals(Optional.of(expectedLine.readLine()).toString(), result.toString().replace("\n", "").replace("\r", ""));
    	}
    }

    /* 
     * @throws IOException
    */
    @Test
    public void testInvalidDataText() throws IOException {
    	System.out.println("Working Directory = " +
                System.getProperty("user.dir") + "/src/numbers/invalidData.txt");
    	FileReader file = new FileReader(System.getProperty("user.dir") + "/src/numbers/invalidData.txt");
    	BufferedReader reader = new BufferedReader(file);
		FloatingPointDriver driver = new FloatingPointDriver();
    	for (int i = 0; i < 23; i++) {
    		BufferedReader line = new BufferedReader(new StringReader(reader.readLine()));
            Optional<Double> result = driver.runFloatingPointParser(line);
            System.out.println(result.isPresent() ? result.get() : "Invalid Input");
            assertEquals(Optional.empty(), result);
    	}
    }
}
