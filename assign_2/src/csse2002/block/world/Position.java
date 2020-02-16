package csse2002.block.world;
import java.lang.Comparable;

/**
 * Represents the position of a Tile in the SparseTileArray
 */
public class Position implements Comparable<Position> {
    private int x;
    private int y;
    /**
     * Construct a position for (x, y)
     * @param x
     * @param y
     */
    public Position(int x, int y){
        this.x = x;
        this.y=y;
    }

    /**
     * Get the x coordinate
     *
     * @return the x coordinate
     */
    public int getX(){
        return x;
    }

    /**
     * Get the y coordinate
     *
     * @return the y coordinate
     */
    public int getY(){
        return y;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * Two Positions are equal if getX() == other.getX() && getY()
     * == other.getY()
     *
     * @param obj the object to compare to
     * @return true if obj is an instance of Position and
     * if obj.x == x and obj.y == y.
     */
    @Override
    public boolean equals(Object obj) {
        Position p = (Position)obj;
        return p.getX()==this.x && p.getY()==this.y;
    }

    /**
     * Compute a hashCode that meets the contract of Object.hashCode
     * @return a suitable hashcode for the Position
     */
    @Override
    public int hashCode(){
        return x*100+y*100;
    }

    /**
     * Compare this position to another position.
     * return
     * <ul>
     *     <li>-1 if getX() < other.getX() </li>
     *     <li>-1 if getX() == other.getX() and getY() < other.getY() </li>
     *     <li>0 if getX() == other.getX() and getY() == other.getY() </li>
     *     <li>1 if getX() > other.getX() </li>
     *     <li>1 if getX() == other.getX() and getY() > other.getY() </li>
     * </ul>
     *
     * @param other the other Position to compare to
     * @return -1, 0, or 1 depending on conditions above
     */
    public int compareTo(Position other){
        if(this.x<other.x ||(this.x==other.x && this.y< other.y)){
            return -1;
        }else if(this.x==other.x && this.y==other.y){
            return 0;
        }else {
            return 1;
        }
    }

    /**
     * Convert this position to a string.
     * String should be "(<x>, <y>)" where <x> is the value returned by
     * getX() and <y> is the value returned by getY().
     *
     * @return a string representation of the position "(<x>, <y>)"
     */
    @Override
    public String toString(){
        //return "(<"+x+">"+","+"<"+y+">)";
        StringBuilder sb = new StringBuilder();
        sb.append("(<"+x+">"+","+"<"+y+">)");
        return sb.toString();
    }
}

