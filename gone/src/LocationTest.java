import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Java class Location
 * This is class for storing locations of stones
 *
 * @author Anna Sedlackova, Vincent Portelli
 * @version, 1.00 21 March 2019
 */
public class LocationTest {

    //instance of location
    Location location;

    // to be done before setup
    @Before
    public void setUp()  {
        location = new Location(0,0);
    }

    /**
     * Structured Basis: nominal case, retrieve x coordinate of location
     */
    @Test
    public void testGetX(){
        assertEquals(location.getX(), 0);
    }

    /**
     * Structured Basis: nominal case, retrieve y coordinate of location
     */
    @Test
    public void testGetY(){
        assertEquals(location.getY(), 0);
    }

    /**
     * Structured Basis: nominal case
     * ensure that equal locations have the same hash value
     */
    @Test
    public void testHashCodeEqual(){
        assertEquals(location.hashCode(), new Location(0,0).hashCode());
    }

    /**
     * Structured Basis: nominal case
     * ensure that different locations have different hash values
     */
    @Test
    public void testHashCodeDifferent(){
        assertFalse(location.hashCode() == new Location(1,1).hashCode());
    }

    /**
     * Note: .equals() does not need to be tested as it was autogenerated by the IDE
     */
}
