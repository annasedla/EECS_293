import java.util.*;
import java.io.*;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Java test Game Class
 * This class contains tests for the Game of gone
 *
 * @author Anna Sedlackova, Vincent Portelli
 * @version 2.00 21 March 2019
 */
public class GameTest {

    // definitions for fields
    Game game;
    Game zeroBoard;
    Game oneBoard;
    Game tenBoard;
    Game.GameTester gameTester;
    List<Location> locations = new ArrayList<>();

    @Before
    public void setUp(){
        game = new Game();
        gameTester = new Game.GameTester();
        zeroBoard  = gameTester.newBoardOfSize(0,0);
        oneBoard = gameTester.newBoardOfSize(1,1);
        tenBoard = gameTester.newBoardOfSize(10,10);
    }

    /**
     * Structured basis: nominal case, location is on the board
     * Good data: nominal case, location is on the board
     */
    @Test
    public void testStoneColorAtNominal(){
        gameTester.placeStone(tenBoard, Game.Color.WHITE, new Location(0,0));
        assertEquals(tenBoard.stoneColorAt(new Location(0,0)), Game.Color.WHITE);
    }

    /**
     * Bad data: location is not on board
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testStoneColorAtBadData(){
        gameTester.placeStone(tenBoard, Game.Color.WHITE, new Location(0,0));
        tenBoard.stoneColorAt(new Location(11,11));
    }

    /**
     * Structured basis: nominal case
     * Good data: nonempty list of valid locations
     */
    @Test
    public void testPlaceAllStonesNominal(){
        locations.add(new Location(0,0));
        tenBoard.placeAllStones(Game.Color.WHITE, locations);
        assertEquals(tenBoard.stoneColorAt(new Location(0,0)), Game.Color.WHITE);
    }

    /**
     * Bad data: nonempty list of invalid locations
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testPlaceAllStonesOutOfBounds(){
        locations.add(new Location(11,11));
        tenBoard.placeAllStones(Game.Color.WHITE, locations);
    }

    /**
     * Bad data: empty list
     */
    @Test
    public void testPlaceAllStonesEmptyList(){
        tenBoard.placeAllStones(Game.Color.WHITE, locations);
        assertEquals(tenBoard.stoneColorAt(new Location(0,0)), null);
    }

    /**
     * Bad data: location is occupied
     */
    @Test(expected = RuntimeException.class)
    public void testPlaceAllStonesLocationOccupied(){

        //place a pebble on board
        gameTester.placeStone(tenBoard, Game.Color.WHITE, new Location (1,1));
        locations.add(new Location(1,1));

        // attempt to place a pebble again
        tenBoard.placeAllStones(Game.Color.WHITE, locations);
    }

    /**
     * Structured basis: nominal case
     * Good data: valid location to place new stone
     */
    @Test
    public void testPlaceStoneNominal(){
        gameTester.placeStone(tenBoard,Game.Color.WHITE, new Location(0,0));
        assertEquals(tenBoard.stoneColorAt(new Location(0,0)), Game.Color.WHITE);
    }

    /**
     * Bad data: invalid location inputted
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testPlaceStoneOutOfBounds(){
        gameTester.placeStone(oneBoard,Game.Color.WHITE, new Location(1,1));
    }

    /**
     * Bad data: location is occupied
     */
    @Test(expected = RuntimeException.class)
    public void testPlaceStoneLocationOccupied(){

        locations.add(new Location(1,1));
        tenBoard.placeAllStones(Game.Color.WHITE, locations);

        //attempt to place again
        gameTester.placeStone(oneBoard,Game.Color.WHITE, new Location(1,1));
    }

    /**
     * Structured basis: nominal case
     * Data flow: color is white to be black
     */
    @Test
    public void testSwitchColorTestWhite(){
        assertEquals(tenBoard.switchColor(Game.Color.WHITE), Game.Color.BLACK);
    }

    /**
     * Structured basis: nominal case
     * Data flow: color is black to be white
     */
    @Test
    public void testSwitchColorTestBlack(){
        assertEquals(tenBoard.switchColor(Game.Color.BLACK), Game.Color.WHITE);
    }

    /**
     * Structured basis: if statement evaluates to false
     * Good data: stone placed at target location
     */
    @Test
    public void testChangeStoneColorAtNominal(){
        gameTester.placeStone(oneBoard, Game.Color.WHITE, new Location (0,0));
        oneBoard.changeStoneColorAt(new Location(0,0));
        assertEquals(Game.Color.BLACK, oneBoard.stoneColorAt(new Location(0,0)));
    }

    /**
     * Structured basis: if statement evaluates to true
     * Bad data: No stone at target location
     */
    @Test(expected = RuntimeException.class)
    public void testChangeStoneColorAtNoStone(){
        oneBoard.changeStoneColorAt(new Location(0,0));
    }

    /**
     * Bad data: Input location out of board bounds
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testChangeStoneColorAtLocationOutOfBounds(){
        oneBoard.changeStoneColorAt(new Location(1,1));
    }

    /**
     * Structured basis: Nominal case
     * Good data: nonempty list valid locations
     */
    @Test
    public void testChangeColorAtAllLocationsInNominal(){

        //add coordinates to the list of locations
        locations.add(new Location(0,0));
        locations.add(new Location(1,1));

        tenBoard.placeAllStones(Game.Color.WHITE, locations);
        gameTester.changeColorAtAllLocationsIn(tenBoard,locations);
        assertEquals(tenBoard.stoneColorAt(new Location (0,0)), Game.Color.BLACK);
        assertEquals(tenBoard.stoneColorAt(new Location (1,1)), Game.Color.BLACK);
    }

    /**
     * Bad data: empty list
     */
    @Test
    public void testChangeColorAtAllLocationsInEmpty(){

        gameTester.changeColorAtAllLocationsIn(tenBoard,locations);

        //add coordinates to the list of locations
        locations.add(new Location(0,0));
        locations.add(new Location(1,1));

        tenBoard.placeAllStones(Game.Color.WHITE, locations);
        assertEquals(tenBoard.stoneColorAt(new Location (0,0)), Game.Color.WHITE);
        assertEquals(tenBoard.stoneColorAt(new Location (1,1)), Game.Color.WHITE);
    }

    /**
     * Bad data: coordinates out of bounds
     */
    @Test(expected = RuntimeException.class)
    public void testChangeColorAtAllLocationsOutOfBounds(){
        //add coordinates to the list of locations
        locations.add(new Location(11,11));
        locations.add(new Location(12,12));

        gameTester.changeColorAtAllLocationsIn(tenBoard,locations);
    }

    /**
     * Structured basis: nominal case, location is on the board
     * Good data: valid board
     * Branch Coverage: nominal case
     * Boundary Coverage: 0 < x, x < board.length, 0 < y, y < board[0].length
     * Compound Boundary: 0 < x < board.length, 0 < y < board[0].length
     */
    @Test
    public void testIsOnBoardNominal(){
        assertTrue(tenBoard.isOnBoard(new Location(5,5)));
    }

    /**
     * Structured basis: nominal case, location is off the board
     * Good data: valid board
     * Branch Coverage: 0 <= x, x >= board.length
     * Boundary Coverage: 0 < x, board.length < x, 0 < y, board[0].length < y
     * Compound Boundary: 0 < board.length < x, 0 < board[0].length < y
     */
    @Test
    public void testIsOnBoardBigCoordinates(){
        assertFalse(tenBoard.isOnBoard(new Location(20,20)));
    }

    /**
     * Bad data: invalid board (size zero)
     * Branch Coverage: 0 >= x
     * Boundary Coverage: x < 0, x < board.length, y < 0, y < board[0].length
     * Compound Boundary: x < 0 < board.length, y < 0 < board[0].length, 0 = board.length, 0 = board[0].length
     */
    @Test
    public void testIsOnBoardNominalNegativeCoordinates(){
        assertFalse(zeroBoard.isOnBoard(new Location(-5,-5)));
    }

    /**
     * Bad Data: invalid board (size zero)
     * Boundary Coverage: 0 = x, x = board.length, 0 = y, y = board[0].length
     */
    @Test
    public void testIsOnBoardZeros(){
        assertFalse(zeroBoard.isOnBoard(new Location(0,0)));
    }

    /**
     * Structured basis: nominal case, location is off the board
     * Good data: valid board
     * Branch Coverage: 0 <= x, x < board.length, 0 <= y, y >= board[0].length
     */
    @Test
    public void testIsOnBoardYBig(){
        assertFalse(tenBoard.isOnBoard(new Location(5,15)));
    }

    /**
     * Structured basis: nominal case, location is off the board
     * Good data: valid board
     * Branch Coverage: 0 <= x, x < board.length, 0 > y
     */
    @Test
    public void testIsOnBoardYNegative(){
        assertFalse(tenBoard.isOnBoard(new Location(5,-5)));
    }

    /**
     * Structured basis: nominal case
     * Good data: pebble location is on the board
     */
    @Test
    public void testAdjacentLocationsToNominal(){
        locations.add(new Location (1,1));
        locations.add(new Location (0,2));
        locations.add(new Location(0,0));

        tenBoard.placeAllStones(Game.Color.WHITE, locations);
        assertEquals(tenBoard.adjacentLocationsTo(new Location (0,1)), locations);
    }

    /**
     * Bad data: pebble location is off the board
     */
    @Test
    public void testAdjacentLocationsToOutOfBounds(){

        //locations ois empty because target is off the board
        assertEquals(tenBoard.adjacentLocationsTo(new Location (11,11)), locations);
    }

    /**
     * Structured basis: nominal case, all conditions are true
     * Good data:  board is nonzero lenght, stones left on board
     */
    @Test
    public void testDoAnyStonesRemainNominal(){
        locations.add(new Location (1,1));
        locations.add(new Location (0,2));
        locations.add(new Location(0,0));
        tenBoard.placeAllStones(Game.Color.WHITE, locations);
        assertTrue(tenBoard.doAnyStonesRemain(Game.Color.WHITE));
    }

    /**
     * Data flow: Good data:  board size is zero
     */
    @Test
    public void testDoAnyStonesRemainZeroBoard(){
        assertFalse(zeroBoard.doAnyStonesRemain(Game.Color.WHITE));
    }

    /**
     * Data flow: Good data:  board size is one by one, one stone on board
     */
    @Test
    public void testDoAnyStonesRemainOneBoard(){
        locations.add(new Location(0,0));
        oneBoard.placeAllStones(Game.Color.WHITE, locations);
        assertTrue(oneBoard.doAnyStonesRemain(Game.Color.WHITE));
    }

    /**
     * Data flow: Good data:  board size is one by one, no stone on board
     */
    @Test
    public void testDoAnyStonesRemainOneBoardNoStones(){
        assertFalse(oneBoard.doAnyStonesRemain(Game.Color.WHITE));
    }

    /**
     * structured basis: nominal case, list is nonempty, locations are occupied with different colors
     * Good data: nonempty list
     * Data flow: nominal case, list is nonempty
     */
    @Test
    public void testAdjacentOppositeColorPebbleLocationsNominal(){
        tenBoard.placeAllStones(Game.Color.WHITE, Arrays.asList(new Location(0,0)));
        tenBoard.placeAllStones(Game.Color.BLACK, Arrays.asList(new Location(1,0)));
        Set<Location> locs = gameTester.adjacentOppositeColorPebbleLocations(tenBoard, new LinkedList<>(Arrays.asList(new Location(0,0))), Game.Color.WHITE);

        assertEquals(new HashSet<>(Arrays.asList(new Location(1,0))),locs);
    }

    /**
     * structured basis
     * Bad data: list with location coordinates out bounds
     * Data flow: location in list is two spaces off the board: adjacent locations is empty, for loop is initially false
     */
    @Test
    public void testAdjacentOppositeColorPebbleLocationsBadDataOutOfBounds(){
        Set<Location> locs = gameTester.adjacentOppositeColorPebbleLocations(tenBoard, new LinkedList<>(Arrays.asList(new Location(-2,-2))), Game.Color.WHITE);

        assertEquals(new HashSet<>(),locs);
    }

    /**
     * structured basis
     * Bad data: list with location coordinates empty
     */
    @Test
    public void testAdjacentOppositeColorPebbleLocationsBadDataEmptyList(){
        Set<Location> locs = gameTester.adjacentOppositeColorPebbleLocations(tenBoard, new LinkedList<>(), Game.Color.WHITE);

        assertEquals(new HashSet<>(),locs);
    }

    /**
     * Structured Basis: nominal case, only stones of desired color on board
     */
    @Test
    public void testLocationsOfStonesNominalOneColor(){
        tenBoard.placeAllStones(Game.Color.WHITE, Arrays.asList(new Location(0,0), new Location(0,1), new Location(0,2)));
        List<Location> locs = tenBoard.locationsOfStones(Game.Color.WHITE);

        assertEquals(locs, Arrays.asList(new Location(0,0), new Location(0,1), new Location(0,2)));
    }

    /**
     * Structured Basis: nominal case, stones of both colors on board
     */
    @Test
    public void testLocationsOfStonesNominalBothColors(){
        tenBoard.placeAllStones(Game.Color.WHITE, Arrays.asList(new Location(0,0), new Location(0,1), new Location(0,2)));
        tenBoard.placeAllStones(Game.Color.BLACK, Arrays.asList(new Location(1,0), new Location(1,1), new Location(1,2)));
        List<Location> locs = tenBoard.locationsOfStones(Game.Color.WHITE);

        assertEquals(locs, Arrays.asList(new Location(0,0), new Location(0,1), new Location(0,2)));
    }

    /**
     * Structured Basis: bad data, board size zero
     */
    @Test
    public void testLocationsOfStonesZeroBoard(){
        List<Location> locs = zeroBoard.locationsOfStones(Game.Color.WHITE);

        assertEquals(locs, Collections.emptyList());
    }

    /**
     * Structured Basis: good data, board size one, no stones on board
     */
    @Test
    public void testLocationsOfStonesOneBoard(){
        List<Location> locs = oneBoard.locationsOfStones(Game.Color.WHITE);

        assertEquals(locs, Collections.emptyList());
    }

    /**
     * Structured Basis: nominal case
     * Good data: stones of desired color are on board - triggers the while loop
     */
    @Test
    public void testTurnResultForNominal(){
        tenBoard.placeAllStones(Game.Color.WHITE, Arrays.asList(new Location(0,0)));
        tenBoard.placeAllStones(Game.Color.BLACK, Arrays.asList(new Location(0,1)));
        Game.TurnResult result = tenBoard.turnResultFor(Game.Color.WHITE);

        assertTrue(result.getRounds() == 1 && !result.doesOtherPebbleRemain());
    }

    /**
     * Structured Basis: nominal case
     * Good data: stones of desired color are not on board
     */
    @Test
    public void testTurnResultForEmptyBoard(){
        tenBoard.placeAllStones(Game.Color.BLACK, Arrays.asList(new Location(0,1)));
        Game.TurnResult result = tenBoard.turnResultFor(Game.Color.WHITE);

        assertTrue(result.getRounds() == 0 && result.doesOtherPebbleRemain());
    }

    /**
     * Structured Basis: nominal case, good data
     */
    @Test
    public void testLocationsFromInputNominal(){
        String input = "0,0 1,1 2,2\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        List<Location> locs = gameTester.locationsFromInput(game, new Scanner(System.in));

        assertEquals(locs, Arrays.asList(new Location(0,0), new Location(1,1), new Location(2,2)));

    }

    /**
     * Structured Basis: input empty
     */
    @Test
    public void testLocationsFromInputEmpty(){

        String input = "\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        List<Location> locs = gameTester.locationsFromInput(game, new Scanner(System.in));

        assertTrue(locs.isEmpty());

    }

    /**
     * Structured Basis: bad data
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testLocationsFromInputBadData(){

        String input = "7 ,,55,3\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        List<Location> locs = gameTester.locationsFromInput(game, new Scanner(System.in));

        assertTrue(locs.isEmpty());

    }

    /**
     * Structured Basis: nominal case, good data
     */
    @Test
    public void testMakeBoardFromInputNominal(){

        String input = "5\n5\n\n0,0\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        game.makeBoardFromInput();

        assertEquals(Game.Color.BLACK, game.stoneColorAt(new Location(0,0)));
    }

    /**
     * Structured Basis: bad data (wrong format)
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testMakeBoardFromInputBadInput(){

        String input = "5\n5\n0, 0; 1, 1\n1, 0; 0, 1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        game.makeBoardFromInput();
    }

    /**
     * Structured Basis: nominal case
     * just make sure it doesn't cause any errors
     */
    @Test
    public void testMain(){

        String input = "5\n5\n0,0 1,1\n1,0 0,1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Game.main(new String[0]);

    }

    /**
     * Stress Test:
     * make a bunch of boards for which:
     * there is one white pebble and many black pebbles that are in a contiguous formation
     * this can be automatically generated by using the adjacentLocations function to place
     * black pebbles
     *
     * every board should end the turn with no black pebbles remaining
     */
    @Test
    public void testStressManyBoards(){
        for(int i = 0; i < 100; i++){
            Game game = gameTester.newBoardOfSize(20,20);
            gameTester.placeStone(game, Game.Color.WHITE, new Location(10,10));
            Queue<Location> q = new LinkedList<>();
            q.add(new Location(10,10));

            for(int placed = 0; placed < 20; placed++){
                // Errors can happen here, but if you ignore them and keep going then it's not an
                // issue and the test is still valid (it will only ever place pebbles contiguously)
                try{
                    List<Location> locs = game.adjacentLocationsTo(q.remove());
                    Location nextPlace = locs.get((int)(Math.random()*locs.size()));
                    gameTester.placeStone(game, Game.Color.BLACK, nextPlace);
                    q.add(nextPlace);
                }catch(Exception e){}
            }

            Game.TurnResult result = game.turnResultFor(Game.Color.WHITE);

            assertFalse(result.doesOtherPebbleRemain());
        }
    }

    /**
     * Stress Test:
     * Make a board sized 1000 by 1000
     * Run the game on it and ensure the result is correct
     */
    @Test
    public void testStressLargeBoard(){

        List<Location> whiteLocations = new ArrayList<>();
        List<Location> blackLocations = new ArrayList<>();

        // fill location lists
        whiteLocations.add(new Location(999,999));

        for (int i = 0; i < 1000; i++){
            for (int j = 0; j < 1000; j++){
                if (i != 999 || j != 999){
                    blackLocations.add(new Location(i,j));
                }

            }
        }

        Game game = gameTester.newBoardOfSize(1000, 1000);

        // place all stones
        game.placeAllStones(Game.Color.WHITE, whiteLocations);
        game.placeAllStones(Game.Color.BLACK, blackLocations);

        // test that number of rounds is 1998
        assertEquals(game.turnResultFor(Game.Color.WHITE).getRounds(), 1998);

        // test that no pebbles are left on board
        assertFalse(game.turnResultFor(Game.Color.WHITE).doesOtherPebbleRemain());
    }
}
