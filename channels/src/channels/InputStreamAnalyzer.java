package channels;

/**
 * @author Anna Sedlackova, Jason Shin
 */
enum Strings {
    TO("TO"),
    REP("REP"),
    THISIS("THISIS");

    private final String text;

    /**
     * @param text setter
     */
    Strings(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}

/**
 * @author Anna Sedlackova, Jason Shin
 */
class InputStreamAnalyzer {

    private String[] goalData;
    private String[] parserData;
    private int TOCount = 0;
    private int REPCount = 0;

    /**
     * Constructor for the analyzer
     * @param receiver receiver address
     * @param local local address
     */
    InputStreamAnalyzer(String receiver, String local){

        //initiate goal data array
        goalData = new String[receiver.length() * 2 + local.length()];

        //here lets also initiate the parser data
        parserData = new String[receiver.length() * 2 + local.length()];

        //fill up goal data array
        fillGoalData(receiver, local);
    }

    /**
     * last method, still part of the barricade, cleans data if "" and ensures address is valid
     * @param input current input line
     * @return true if valid and full addresses confirmed, false otherwise
     */
    boolean checkValidLineAndParse(String input){
        checkREPCount(input);
        if (!input.equals("")){
            return parseInputLine(input);
        } else {
            return false;
        }
    }

    /**
     * Ensures that both addresses correspond to actual ones
     * @param input current input line
     */
    private void checkREPCount(String input){
        if (input.contains("REP")){
            REPCount++;
            if (REPCount > ((goalData.length-3)/2)){
                parserData[1] = "REPBAD";
            }
        } else {
            REPCount = 0;
        }
    }

    /**
     * Parser purified string
     * @param input parsed input line
     * @return returns true if parsing finished and data is correct
     */
    private boolean parseInputLine (String input){

        //assert that inmput is the required string
        assert (input.matches("("+Strings.TO.toString()+"[0-9]"+"|"+Strings.REP.toString()+"[0-9]"+"|"+Strings.THISIS.toString()+"[0-9])"));

        //check that to count is correct
        checkTOCount(input);

        // keep filling the array in
        for (int i = 0; i < goalData.length; i++){
            if (input.equals(goalData[i]) && parserData[i] == null && followsOrder(i)){
                parserData[i] = input;
                break;
            }
        }

        //only returns true if it caught a valid address
        return isArrayFullDoArraysEqual();
    }

    /**
     * Fill the goal data array
     * @param receiver receiver address
     * @param local local address
     */
    private void fillGoalData(String receiver, String local) {
        fillReceiver(receiver, 0);
        fillReceiver(receiver, receiver.length());
        fillLocal(local, goalData.length - local.length());
    }

    /**
     * Fills the goalData with receiver information
     * @param receiver receiver address
     * @param startValue value to begin filling the array at
     */
    private void fillReceiver(String receiver, int startValue){
        int j = startValue;
        //fill the data array
        for (int i = 0; i < receiver.length(); i++){
            if (i == 0){
                goalData[j] = Strings.TO.toString() + receiver.charAt(i);
            } else {
                goalData[j] = Strings.REP.toString() + receiver.charAt(i);
            }
            j++;
        }
    }

    /**
     * Fills the goalData with local information
     * @param local local address
     * @param startValue value to begin filling the array at
     */
    private void fillLocal(String local, int startValue){
        int j = startValue;
        for (int i = 0; i < local.length(); i++){
            if (i == 0){
                goalData[j] = Strings.THISIS.toString() + local.charAt(i);
            } else {
                goalData[j] = Strings.REP.toString() + local.charAt(i);
            }
            j++;
        }
    }

    /**
     * Compares parser and actual data arrays and makes sure original array is full
     * @return true if they match false otherwise
     **/
     
    private boolean isArrayFullDoArraysEqual(){
        for (int i = 0; i < parserData.length; i++){
            if (parserData[i] == null){
                return false;
            }
            if (!parserData[i].equals(goalData[i])){
                return false;
            }
        }
        return true;
    }

    /**
     * Make sure that order is followed while filling in array
     * @param i index for loop
     * @return true if previous entry has been filled
     */
    private boolean followsOrder(int i){
        return (i == 0 || parserData[i-1] != null);
    }

    /**
     * Checks that there are no more then ten TOs in a row
     * @param input current parsed input
     */
    private void checkTOCount(String input){
        if (TOCount == 10){
            System.out.println("note: the calling sequence would be valid if it had less lines reading " + goalData[0]);
        }

        if (input.equals(goalData[0])){
            TOCount++;
        }else{
            TOCount = 0;
        }
    }
    
    class TestHook {
    	void checkREPCount(String input){
    		InputStreamAnalyzer.this.checkREPCount(input);
    	}
    	boolean parseInputLine(String input) {
    		return InputStreamAnalyzer.this.parseInputLine(input);
    	}
    	boolean isArrayFullDoArraysEqual() {
    		return InputStreamAnalyzer.this.isArrayFullDoArraysEqual();
    	}
    	boolean followsOrder(int i) {
    		return InputStreamAnalyzer.this.followsOrder(i);
    	}
    	void checkTOCount(String input) {
    		InputStreamAnalyzer.this.checkTOCount(input);
    	}
        String[] getGoalData() {
    		return InputStreamAnalyzer.this.goalData;
    	}
        String[] getParseData() {
    		return InputStreamAnalyzer.this.parserData;
    	}
    	int getTOCount() {
    		return InputStreamAnalyzer.this.TOCount;
    	}
    	int getREPCount() {
    		return InputStreamAnalyzer.this.REPCount;
    	}
    }
}
