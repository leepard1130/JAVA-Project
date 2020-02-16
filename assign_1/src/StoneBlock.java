/**
 *A stone block
 */
public class StoneBlock implements Block {

    public StoneBlock(){

    }

    /**
     * Get the colour of a StoneBlock
     *
     * @return Always returns "gray"
     */
    public String getColour(){
        return "gray";
    }

    /**
     * Get the type of a StoneBlock
     *
     * @return Always returns "stone"
     */
    public String getBlockType(){
        return "stone";
    }

    /**
     * StoneBlocks are not diggable
     *
     * @return Always returns false
     */
    public boolean isDiggable(){
        return false;
    }

    /**
     * StoneBlocks are not moveable
     *
     * @return Always returns false
     */
    public boolean isMoveable(){
        return false;
    }

    /**
     * StoneBlocks are not carryable
     *
     * @return Always returns false
     */
    public boolean isCarryable(){
        return false;
    }
}
