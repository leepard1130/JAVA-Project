/**
 * An abstract GroundBlock that enforces not moveable and diggable
 */
public abstract class GroundBlock implements Block{

    /**
     * Is the GroundBlock moveable? GroundBlocks enforce not moving
     *
     * @return false
     */
    public final boolean isMoveable(){
        return false;
    }

    /**
     * Is the GroundBlock diggable? GroundBlocks enforce allowing digging
     *
     * @return true
     */
    public final boolean isDiggable(){
        return true;
    }
}
