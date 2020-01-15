import java.util.*;
import java.util.stream.Collectors;

/**
 * Java class Game
 * This class is for keeping track of the result of the game of gone
 * Holds a representation of game board
 *
 * @author Anna Sedlackova, Vincent Portelli
 * @version 2.00 21 March 2019
 */
public final class Game {

    // Holds the board representation
    private Color[][] board;

    /**
     * Enum for stone color
     */
    enum Color {
        WHITE, BLACK
    }

    /**
     * Java Class TurnResult
     * This private class is for holding the result of a turn,
     * which includes the number of rounds and whether a pebble
     * of the other color remains on the board.
     */
    public class TurnResult{

        // how many rounds happened before the algorithm terminated
        private int rounds;

        // how many stones remain
        private boolean otherPebbleRemains;

        /**
         * constructor
         * @param rounds number of rounds before termination
         * @param otherPebbleRemains number of enemy stones remaining
         */
        public TurnResult(int rounds, boolean otherPebbleRemains){
            this.rounds = rounds;
            this.otherPebbleRemains = otherPebbleRemains;
        }

        /**
         * Getter for the number of rounds
         * @return number of rounds
         */
        public int getRounds(){
            return rounds;
        }

        /**
         * Getter for the number of remaining pebbles
         * @return true if enemy pebbles are on the board
         */
        public boolean doesOtherPebbleRemain(){
            return otherPebbleRemains;
        }
    }

    /**
     * Retrieves location of a stone
     * @param location location to retrieve stone color at
     * @return the color of the stone at the given location
     */
    public Color stoneColorAt(Location location){
        return board[location.getX()][location.getY()];
    }

    /**
     * Calls place stone method for each color and each location in the list of locations
     * @param color black or white stone color
     * @param locations list of locations to place the stone colors
     */
    public void placeAllStones(Color color, List<Location> locations) {
        for(Location location : locations) {
            placeStone(color, location);
        }
    }

    /**
     * Place individual stones on the board
     * @param color color of the stone to be placed on board
     * @param location location of the stone to be placed on board
     */
    private void placeStone(Color color, Location location){
        if(isOnBoard(location)){
            if(stoneColorAt(location) == null){
                board[location.getX()][location.getY()] = color;
            }else{
                //there is stone already at a location
                throw new RuntimeException("Cannot place stone at an occupied location.");
            }
        }else{
            //the location is out of bounds
            throw new ArrayIndexOutOfBoundsException("Cannot place stone at location outside of board.");
        }
    }

    /**
     * Switches the stone color from black to white or vice versa
     * @param color color to be swithced
     * @return the opposite of the input color
     */
    public Color switchColor(Color color){
        if(color == Color.WHITE){
            return Color.BLACK;
        }else{
            return Color.WHITE;
        }
    }

    /**
     * Changes the stone color on the board at a particular location
     * @param location where stone color should be changed (must contain a stone)
     */
    void changeStoneColorAt(Location location) {
        Color colorAtLocation = stoneColorAt(location);
        if (colorAtLocation == null) {
            throw new NullPointerException("Cannot change stone at empty location");
        }
        board[location.getX()][location.getY()] = switchColor(colorAtLocation);
    }

    /**
     * For an input list of locations it calls the changeStoneColorAt() method above
     * @param locations the list of locations to be called onto
     */
    private void changeColorAtAllLocationsIn(Collection<Location> locations){
        for(Location location: locations){
            changeStoneColorAt(location);
        }
    }

    /**
     * Returns true if location fits on the board
     * @param l location of the stone
     * @return true if fits on the board, false otherwise
     */
    boolean isOnBoard(Location l){
        return 0 <= l.getX() && l.getX() < board.length && 0 <= l.getY() && l.getY() < board[0].length;
    }

    /**
     * Retrieve all the adjacent locations
     * @param location input coordinates
     * @return list of valid adjacent locations
     */
    List<Location> adjacentLocationsTo(Location location){
        int x = location.getX();
        int y = location.getY();

        List<Location> adjacents = new ArrayList<>();

        // looking right, up and down
        adjacents.add(new Location(x+1, y));
        adjacents.add(new Location(x-1, y));
        adjacents.add(new Location(x, y+1));
        adjacents.add(new Location(x, y-1));

        // filter out coordinates that are outside of the board
        return adjacents.stream().filter(l -> isOnBoard(l)).collect(Collectors.toList());
    }

    /**
     * Determines if there are stones left on the board of that particular color
     * @param color stone color to be parsed for
     * @return true if there are stones remaining false otherwise
     */
    boolean doAnyStonesRemain(Color color){
        for(int x = 0; x < board.length; x++){
            for(int y = 0; y < board[0].length; y++){
                if(stoneColorAt(new Location(x,y)) == color){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Adds locations to be processed to marked
     * @param stones list of stones to be processed each loop
     * @param color targeted color
     * @return Hashset of marked locations to be placed on the list q
     */
    private Set<Location> adjacentOppositeColorPebbleLocations(LinkedList<Location> stones, Color color){

        //set of locations that are to be switched in color and processed in the list stones
        Set<Location> marked = new HashSet<>();

        // run until the list stones is empty
        while(!stones.isEmpty()){
            Location loc = stones.remove(0);

            for(Location location : adjacentLocationsTo(loc)) {
                if(stoneColorAt(location) == switchColor(color)) {
                    marked.add(location);
                }
            }

        }
        return marked;
    }

    /**
     * Returns list of the locations of that particular color on the board
     * @param color black or white color
     * @return list of locations of color
     */
    public LinkedList<Location> locationsOfStones(Color color){
        LinkedList<Location> l = new LinkedList<>();
        for(int x = 0; x < board.length; x++){
            for(int y = 0; y < board[0].length; y++){
                Location loc = new Location(x,y);
                if(stoneColorAt(loc) == color){
                    l.add(loc);
                }
            }
        }
        return l;
    }

    /**
     * Main algorithm returns TurnResult containing number of rounds and pebbles remaining
     * @param color color that is flipping
     * @return TurnResult instance
     */
    public TurnResult turnResultFor(Color color){

        // list of stones to be pulled at each loop iteration
        LinkedList<Location> stones = locationsOfStones(color);

        // set of opposite team stones to be flipped in color and placed at the end of stones
        Set<Location> marked = new HashSet<>();

        //number of rounds in the game
        int rounds = 0;

        do {
            rounds += 1;
            marked.addAll(adjacentOppositeColorPebbleLocations(stones,color));
            changeColorAtAllLocationsIn(marked);
            stones.addAll(marked);
            marked.clear();
        } while(!stones.isEmpty());

        boolean otherPebbleRemains = doAnyStonesRemain(switchColor(color));

        // need to subtract from rounds because of the algorithm requirements
        rounds -= 1;

        return new TurnResult(rounds, otherPebbleRemains);
    }

    /**
     * Parses the input from the user with a scanner
     * @return list of locations of a particular stone color
     */
    private List<Location> locationsFromInput(Scanner in){
        return Arrays.stream(in.nextLine().split("\\s+")).filter(string -> !string.isEmpty()).map(coordinate -> {
            String[] separatedCoordinates = coordinate.split(",");
            return new Location(Integer.parseInt(separatedCoordinates[0]),Integer.parseInt(separatedCoordinates[1]));
        }).collect(Collectors.toList());
    }

    /**
     * Creates the game by getting board info, and stone placement from the user
     */
    public void makeBoardFromInput() {
        Scanner in = new Scanner(System.in);

        // get board dimensions from user
        System.out.println("Board Width:");
        int width = Integer.parseInt(in.nextLine());

        System.out.println("Board Height:");
        int height = Integer.parseInt(in.nextLine());

        // get stone locations from user
        System.out.println("Enter white stone locations in format 'x,y x,y x,y':");
        List<Location> whiteStoneLocations = locationsFromInput(in);

        System.out.println("Enter black stone locations in format 'x,y x,y x,y':");
        List<Location> blackStoneLocations = locationsFromInput(in);

        // creates board
        board = new Color[height][width];
        placeAllStones(Color.BLACK,blackStoneLocations);
        placeAllStones(Color.WHITE,whiteStoneLocations);

        in.close();
    }

    /**
     * Main method, calls makeBoardFromInput()
     * @param args terminal input
     */
    public static void main(String[] args){
        Game game = new Game();
        game.makeBoardFromInput();
        TurnResult result = game.turnResultFor(Color.WHITE);
        System.out.println("Rounds to end of white's turn: "+result.getRounds());
        System.out.println("Black pebbles remain: "+result.doesOtherPebbleRemain());
    }

    /**
     * Testing class for JUNIT to access private methods
     */
    public static class GameTester{

        /**
         * Gets locations from input
         * @param game game instance method
         * @param in scanner input
         * @return return list of locations
         */

        public List<Location> locationsFromInput(Game game, Scanner in){
            return game.locationsFromInput(in);
        }

        public Set<Location> adjacentOppositeColorPebbleLocations(Game game, LinkedList<Location> stones, Color color){
            return game.adjacentOppositeColorPebbleLocations(stones, color);
        }

        /**
         * Returns a board and game instance
         * @param width of board
         * @param height of board
         * @return new game instance with a specified board
         */
        public Game newBoardOfSize(int width, int height){
            Game g = new Game();
            g.board = new Color[width][height];
            return g;
        }

        /**
         * Access place stone method for testing
         * @param game game instance
         * @param color of target pebbles
         * @param location location to place pebble
         */
        public void placeStone(Game game, Color color, Location location){
            game.placeStone(color, location);
        }

        /**
         * Access changeColorAtAllLocationsIn
         * @param locations the list of locations to be called onto
         */
        public void changeColorAtAllLocationsIn(Game game, Collection<Location> locations){
            game.changeColorAtAllLocationsIn(locations);
        }
    }
}