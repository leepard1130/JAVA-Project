/**
 * A grass block
 */
public class GrassBlock extends GroundBlock{

    /**
     * Get the colour of a GrassBlock
     *
     * @return Always returns "green"
     */
    public String getColour(){
        return "green";
    }


    /**
     * Get the type of a GrassBlock
     *
     * @return grass
     */
    public String getBlockType(){
        return "grass";
    }

    /**
     * GrassBlocks are not carryable
     *
     * @return Always returns false
     */
    public boolean isCarryable(){
        return false;
    }
}
