package channels;
import java.io.*;

/**
 * Main class for the Channels programming assignment
 * @author Anna Sedlackova, Jason Shin
 */
public class Channels {

    /**
     * Main method
     * @param args command line input
     */
    public static void main(String[] args) {

        System.out.println("Beginning reading from input...");
        String filePath = System.getProperty("user.dir") + "/src/channels/data.txt";
        
        //make an instance of FileReader and BufferedReader
        FileReader file = fileReader(filePath);
        BufferedReader reader = new BufferedReader(file);

        // start reading from input file line by line
        readInputFromFile(reader);
    }

    /**
     * Creates an instance of file reader, terminates program if file not found
     * @return a new instance of file reader
     */
    private static FileReader fileReader (String filePath){

        FileReader file = null;

        // catch a file not found exception
        try {
            file = new FileReader(filePath);

        } catch (FileNotFoundException e){
            System.out.println("false");
            System.out.println("Error detecting channels - file not found.");

            //NOTE: must be commented when testing
            //System.exit(0);
        }
        return file;
    }

    /**
     * Checks for valid receiver and local
     * @param receiver Receiver address
     * @param local local address
     */
    private static boolean isValidReceiverAndLocal(String receiver, String local) {
    	if (receiver == null || !receiver.matches("[0-9]+")) {
	        System.out.println("false");
        	System.out.println("Not a valid receiver address");
        	return false;
        }
    	if (local == null || !local.matches("[0-9]+")) {
	        System.out.println("false");
        	System.out.println("Not a valid local address");
        	return false;
        }

    	return true;
    }

    /**
     * Makes sure that we are catching the correct receiver/caller addresses
     * @param filteredLine current input line
     * @param receiver receiver address
     * @param local local address
     * @return true if correct, false and temrminate otherwise
     */
    private static boolean isExpectedLine(String filteredLine, String receiver, String local) {
    	if (filteredLine.equals("")) {
    		return true;
    	}
    	String expectedTO = Strings.TO.toString()+receiver.charAt(0);
    	String expectedREP = Strings.REP.toString()+"["+receiver.substring(1)+local.substring(1)+"]";
    	String expectedTHISIS = Strings.THISIS.toString()+local.charAt(0);
    	if (receiver.length() == 1 && local.length() == 1) {
    		return filteredLine.matches("("+expectedTO+"|"+expectedTHISIS+")");
    	}
    	return filteredLine.matches("("+expectedTO+"|"+expectedREP+"|"+expectedTHISIS+")");
    }

    /**
     * Parses each line to the other side of the barricade
     * @param receiver receiver address
     * @param local local address
     * @param reader buffer reader
     */
    private static void tryToConnect(String receiver, String local, BufferedReader reader) {
    	//new instance of input stream analyzer
    	InputStreamAnalyzer inputStreamAnalyzer = new InputStreamAnalyzer(receiver, local);
    	String currentLine;
    	boolean isEnoughDataToConnect;
    	try {
	    	while ((currentLine = reader.readLine()) != null){
	    		String filteredLine = filterWrongCharacters(currentLine);
	    		if (!isExpectedLine(filteredLine, receiver, local)) {
	    	        System.out.println("false");
	    	        System.out.println("Receiver or local does not match provided receiver or local");
	    	        return;
	    		}
	    		isEnoughDataToConnect = inputStreamAnalyzer.checkValidLineAndParse(filteredLine);
	    		if (isEnoughDataToConnect){
	    		    System.out.println("true");
	    		    return;
	    		}
	    	}
	        System.out.println("false");
	        System.out.println("Reached the end of the input stream and could not establish connection.");
    	} catch (IOException e){
    		System.out.println("false");
    		System.out.println("Error reading from the channel file - no input.");
    	}
    }

    /**
     * Continuously reads input from a file
     * Determines if receiver and local addresses are valid
     * @param reader determines if input is valid by printing true or false to the console
     */
    private static void readInputFromFile(BufferedReader reader){
        try {
            //set receiver
            String receiver = reader.readLine();
            
            //set local
            String local = reader.readLine();
            if (isValidReceiverAndLocal(receiver, local)) {
            	tryToConnect(receiver, local, reader);
            }
            
        } catch (IOException e){
            System.out.println("false");
            System.out.println("Error reading from the channel file - no input.");
        }
    }

    /**
     * Filter out wrong characters
     */
    private static String filterWrongCharacters(String currentLine){
        String result = currentLine.replaceAll("\\s","").toUpperCase();

        if (containsValidTO(result)) {
            return result;
        }

        if (containsValidREP(result)){
            return result;
        }

        if (containsValidTHISIS(result)){
            return result;
        }
        return "";
    }

    /**
     * @param input STDIN parsed line
     * @return if TO symbol is valid
     */
    private static boolean containsValidTO (String input){
    	return input.matches(Strings.TO.toString()+"[0-9]");
    }

    /**
     * @param input STDIN parsed line
     * @return if REP symbol is valid
     */
    private static boolean containsValidREP (String input){
    	return input.matches(Strings.REP.toString()+"[0-9]");
    }

    /**
     * @param input STDIN parsed line
     * @return if THISIS symbol is valid
     */
    private static boolean containsValidTHISIS (String input){
    	return input.matches(Strings.THISIS.toString()+"[0-9]");
    }
    
    class TestHook {
        FileReader makeFileReader(String path) {
    		return Channels.fileReader(path);
    	}
    	
    	boolean isValidReceiverAndLocal(String receiver, String local) {
    		return Channels.isValidReceiverAndLocal(receiver, local);
    	}
    	
    	boolean isExpectedLine(String filteredLine, String receiver, String local) {
    		return Channels.isExpectedLine(filteredLine, receiver, local);
    	}
    	
    	void tryToConnect(String receiver, String local, BufferedReader reader) {
    		Channels.tryToConnect(receiver, local, reader);
    	}
    	
    	void readInputFromFile(BufferedReader reader) {
    		Channels.readInputFromFile(reader);
    	}
    	
    	String filterWrongCharacters(String currentLine) {
    		return Channels.filterWrongCharacters(currentLine);
    	}
    	
    	boolean containsValidTO(String input) {
    		return Channels.containsValidTO(input);
    	}
    	
    	boolean containsValidREP(String input) {
    		return Channels.containsValidREP(input);
    	}
    	
    	boolean containsValidTHISIS(String input) {
    		return Channels.containsValidTHISIS(input);
    	}
    }
}
