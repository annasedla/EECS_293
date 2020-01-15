import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Java test TurnResult class
 * This class contains tests for the result of Game
 *
 * @author Anna Sedlackova, Vincent Portelli
 * @version 2.00 21 March 2019
 */
public class TurnResultTest {

    // instances
    Game game;
    Game.TurnResult turnResult;

    // to be done before test
    @Before
    public void setUp()  {
        game = new Game();
        turnResult = game.new TurnResult(2,true);
    }

    /**
     * Structured Basis: nominal case, retrieve the number of rounds
     */
    @Test
    public void getRoundsTest(){
        assertEquals(turnResult.getRounds(), 2);
    }

    /**
     * Structured Basis: nominal case, retrieve the number of rounds
     */
    @Test
    public void doesOtherPebbleRemainTest(){
        assertEquals(turnResult.doesOtherPebbleRemain(), true);
    }
}
