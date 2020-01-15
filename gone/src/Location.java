/**
 * Java class Location
 * This is class for storing locations of stones
 *
 * @author Anna Sedlackova, Vincent Portelli
 * @version, 1.00 5 March 2019
 */
public class Location {

    //private fields storing location coordinates
    private int x;
    private int y;

    /**
     * Location constructor
     * @param x x coordinate
     * @param y y coordinate
     */
    public Location (int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for x
     * @return x location
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for y
     * @return y location
     */
    public int getY() {
        return y;
    }

    /**
     * Overriden .equals method for when the locations are placed in the Hashset
     * @param o to be converted to a location
     * @return whether the objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        Location location = (Location) o;
        return x == location.x && y == location.y;
    }

    /**
     * Hash value so that a HashSet can accurately identify unique locations
     * @return a hash value that will be identical for identical locations
     */
    @Override
    public int hashCode(){
        return String.format("%s,%s",x,y).hashCode();
    }
}
