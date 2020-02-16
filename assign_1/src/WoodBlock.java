/**
 * A wooden block or crate
 */
public class WoodBlock implements Block {

    /**
     * Get the colour of a WoodBlock
     * Always returns "brown"
     */
    public String getColour(){
        return "brown";
    }

    /**
     * Get the type of a WoodBlock
     * Always returns "wood"
     */
    public String getBlockType(){
        return "woords";
    }

    /**
     * A woodblock is diggable
     * @return Always returns true
     */
    public boolean isDiggable(){
        return true;
    }

    /**
     * A woodblock is carryable
     * @return Always returns true
     */
    public boolean isCarryable(){
        return true;
    }

    /**
     * A woodblock is moveable
     * @return Always returns true
     */
    public boolean isMoveable(){
        return true;
    }

}
