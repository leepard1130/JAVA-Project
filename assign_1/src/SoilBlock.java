/**
 * A soil block
 */
public class SoilBlock extends GroundBlock{

    /**
     * Get the colour of a SoilBlock
     *
     * @return Always returns "black"
     */
    public String getColour(){
        return "black";
    }

    /**
     * Get the type of a SoilBlock
     *
     * @retrun Always returns "soil"
     */
    public String getBlockType(){
        return "soil";
    }

    /**
     * SoilBlocks are carryable.
     *
     * @return Always returns true
     */
    public boolean isCarryable(){
        return true;
    }
}
