package numbers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Optional;

/* Driver to build and run FloatingPointParser on input readers */
public class FloatingPointDriver {

    /**
     * Main method12
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Type something:");
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        FloatingPointDriver driver = new FloatingPointDriver();
        Optional<Double> result = driver.runFloatingPointParser(input);
        printOutput(result);
    }

    /**
     * Prints out the given Double (or "Invalid Input" if given empty result)
     * @param result
     */
    public final static void printOutput(Optional<Double> result) {
        System.out.println(result.isPresent() ? result.get() : "Invalid Input");
    }


    /**
     * Retrieves input from the given BufferedReader and parses it to a Double
     * @param input
     * @return
     */
    public final Optional<Double> runFloatingPointParser(BufferedReader input) {
        FloatingPointParser parser = getFloatingPointParser(input);
        return parser.isValidInput() ? Optional.of(parser.parseDouble()) : Optional.empty();
    }


    /**
     * Reads from the input reader and builds a parser
     * TODO this was redone so that complexity would be decreased
     * @param input
     * @return
     */
    private final FloatingPointParser getFloatingPointParser(BufferedReader input) {
        FloatingPointParser parser = null;
        if (input != null) {
            String line = readForValidInput(input);
            line = removeWhiteSpace(line);
            if (line.length() != 0) {
                parser = FloatingPointParser.build(line);
            } else {
                parser = FloatingPointParser.build("bad input");
            }
        }
        return buildParser(parser);
    }
    
    private FloatingPointParser buildParser(FloatingPointParser parser) {
    	if (parser == null) {
            return FloatingPointParser.build("input that is really bad");
        } else {
            return parser;
        }
    }

    /**
     * Removes white spaces from input string
     * //TODO edited so it only removes white space at the end or beginning
     * @param line
     * @return
     */
    private String removeWhiteSpace(String line) {

        int i = 0;
    	
    	if (line == null || line.isEmpty()){
            return "";
        }

        while (Character.isWhitespace(line.charAt(i))){
            if (i+1 == line.length()){
                return "";
            }
            i++;
        }

        String result = line.substring(i, line.length());
        int j = result.length();

        j = checkForEndSpaces(result, j);

        result = result.substring(0, j);
        return result;
    }

    /**
     * Checks if end spaces are valid, pulled out to reduce complexity
     * @param result
     * @param j
     * @return
     */
    private int checkForEndSpaces(String result, int j) {
    	while (Character.isWhitespace(result.charAt(j-1))){
            j--;
        }
    	
    	return j;
    }

    /**
     * Creates a new string builder
     * @param line
     * @return new builder
     */
    private StringBuilder createStringBuilder(String line) {
    	if (line == null) {
        	throw new NullPointerException("No input recieved.");
    	}
        else {
    		StringBuilder builder = new StringBuilder();
    		return builder;
    	}
    }

    /**
     * Reads input from buffered reader and catches exception if invalid
     * @param input
     * @return
     */
    private String readForValidInput(BufferedReader input) {
    	String line = null;
        try {
            line = input.readLine();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return line;
    }
    
    public class TestHook {
    	FloatingPointDriver fpDriver = new FloatingPointDriver();
    	FloatingPointParser buildParser(FloatingPointParser parser) {
    		return fpDriver.buildParser(parser);
    	}
    	FloatingPointParser getFloatingPointParser(BufferedReader input) {
    		return fpDriver.getFloatingPointParser(input);
    	}
    	String removeWhiteSpace(StringBuilder builder, String line) {
    		return fpDriver.removeWhiteSpace(line); 
    	}
    	StringBuilder createStringBuilder(String line) {
    		return fpDriver.createStringBuilder(line);
    	}
    	String readForValidInput(BufferedReader input) {
    		return fpDriver.readForValidInput(input);
    	}
    }
}
