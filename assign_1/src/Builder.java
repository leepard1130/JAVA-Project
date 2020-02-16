import java.util.ArrayList;
import java.util.List;
import java.lang.Math;
/**
 * A Player who modifies the map
 * Manages an inventory of Blocks
 * Maintains a position in the map (by maintaining the current tile that the<br>
 * Builder is on)
 */
public class Builder {
    //name of the builder(returned by getName())-cannot be null
    private String name;
    //the tile the builder starts in - cannot be null
    private Tile startingTile;
    //the starting inventory (blocks) - cannot be null
    private List<Block> startingInventory;

    /**
     * Create a builder.
     * Set the name of the Builder (such that getName() == name) and the <br>
     * current tile to startingTile (such that getCurrentTile() == startingTile).
     *
     * @param name - name of the builder (returned by getName())- cannot be null
     * @param  startingTile - the tile the builder starts in - cannot be null
     */
    public Builder(String name, Tile startingTile){
        this.name = name;
        this.startingTile = startingTile;
    }

    /**
     * Create a builder
     * Set the name of the Builder (such that getName() == name) and the <br>
     * current tile to startingTile (such that getCurrentTile() == startingTile).
     * Copy the starting inventory into the builder's inventory, such that the<br>
     * contents of getInventory() are identical to startingInventory.
     * i.e. getInventory() must contain the contents of startingInventory, <br>
     * but modifying startingInventory after the Builder is constructed <br>
     * should not change the result of getInventory().
     *
     * @param name - name of the builder (returned by getName()) - cannot be null
     * @param startingTile - the tile the builder starts in - cannot be null
     * @param startingInventory - the starting inventory (blocks) - cannot be null
     *
     * @throws InvalidBlockException - if for any Block (block) in <br>
     * startingInventory, block.isCarryable() == false
     */
    public Builder(String name, Tile startingTile, List<Block>
            startingInventory) throws InvalidBlockException {
        this.name= name;
        this.startingTile = startingTile;
        this.startingInventory=new ArrayList<>(startingInventory);

        for (int i =0; i<startingInventory.size();i++){
            if(!(startingInventory.get(i).isCarryable())){
                throw new InvalidBlockException();
            }
        }
    }

    /**
     * Get the Builder's name
     *
     * @return the Builder's name
     */
    public String getName(){
        return name;
    }

    /**
     * Get the current tile that the builder is on
     *
     * @return the current tile
     */
    public Tile getCurrentTile(){
        return startingTile;
    }

    /**
     * What is in the Builder's inventory
     *
     * @return blocks in the inventory
     */
    public List<Block> getInventory(){
        return startingInventory;
    }

    /**
     * Drop a block from inventory on the top of the current tile
     * Blocks can only be dropped on tiles with less than 8 blocks, or tiles<br>
     * with less than 3 blocks if a GroundBlock.
     * Note: the current tile is that given by getCurrentTile() and the index<br>
     * should refer to an item in the list returned by getInventory()
     * Handle the following cases:
     *
     * <ul>
     *      <li>If the inventoryIndex is < 0 or ≥ the inventory size, throw an <br>
     *      InvalidBlockException.</li>
     *       <li>If there are 8 blocks on the current tile, throw a <br>
     *      TooHighException.</li>
     *      <li>If there are 3 or more blocks on the current tile, and the<br>
     *      inventory block is a GroundBlock, throw a TooHighException</li>
     * </ul>
     *
     * Hint: call Tile.placeBlock, after checking the inventory
     *
     * @param inventoryIndex - the index in the inventory to place
     * @throws InvalidBlockException - if the inventoryIndex is out of <br>
     * the inventory range
     * @throws TooHighException - if there are 8 blocks on the current<br>
     * tile already, or if the block is an instance of GroundBlock and there<br>
     * are already 3 or more blocks on the current tile.
     */
    public void dropFromInventory(int inventoryIndex)throws
            InvalidBlockException,TooHighException{
        if(inventoryIndex<0 || inventoryIndex>startingInventory.size()){
            throw new InvalidBlockException();
        }else if(startingTile.getBlocks().size() ==8 ||
                (startingTile.getBlocks().size()>=3
                && startingTile.getBlocks() instanceof GroundBlock)){
            throw new TooHighException();
        }else{
            startingInventory.remove(inventoryIndex);
            startingTile.placeBlock(startingInventory.get(inventoryIndex));
        }
    }

    /**
     *Attempt to dig in the current tile and add tile to the inventory
     * If the top block (given by getCurrentTile().getTopBlock()) is diggable,<br>
     * remove the top block of the tile and destroy it, or add it to the end of<br>
     * the inventory (given by getInventory()).
     * Handle the following cases:
     *<ul>
     *     <li>If there are no blocks on the current tile, throw a <br>
     *     TooLowException</li>
     *     <li>If the top block is not diggable, throw a <br>
     *     InvalidBlockException</li>
     *     <li>If the top block is not carryable, remove the block, <br>
     *     but do not add it to the inventory.</li>
     *</ul>
     *
     * @throws TooLowException - if there are no blocks on the current tile.
     * @throws InvalidBlockException - if the top block is not diggable
     */
    public void digOnCurrentTile() throws TooLowException,
            InvalidBlockException{
        if(startingTile.getBlocks().isEmpty()){
            throw new TooLowException();
        }else if(!(startingTile.getTopBlock().isDiggable())){
            throw new InvalidBlockException();
        }else if(!(startingTile.getTopBlock().isCarryable())){
            startingTile.removeTopBlock();
        }else{
            startingInventory.add(startingTile.getTopBlock());
            startingTile.removeTopBlock();
        }
    }

    /**
     *Check if the Builder can enter a tile from the current tile.
     * Returns true if:
     * <ul>
     *     <li>the tiles are connected via an exit (i.e. there is an exit<br>
     *     from the current tile to the new tile), and</li>
     *     <li>the height of the new tile (number of blocks) is the same or<br>
     *     different by 1 from the current tile (i.e. abs(current tile height<br>
     *     - new tile) <= 1)</li>
     * </ul>
     *If newTile is null return false.
     *
     * @param newTile - the tile to test if we can enter
     * @return true if the tile can be entered
     */
    public boolean canEnter(Tile newTile){
        boolean result=true;
        if(newTile==null){
            result=false;
        }else if(!(startingTile.getExits().containsValue(newTile)) &&
                (Math.abs(startingTile.getBlocks().size()-
        newTile.getBlocks().size())<=1)){
            result=true;
        }
        return result;
    }

    /**
     *move the builder to a new tile.
     * If canEnter(newTile) == true then change the builders current tile to<br>
     * be newTile. (i.e. getCurrentTile() == newTile)
     * If canEnter(newTile) == false then throw a NoExitException.
     *
     * @param newTile - the tile to move to
     * @throws NoExitException - if canEnter(newTile) == false
     */
    public void moveTo(Tile newTile) throws NoExitException{
        if(!(canEnter(newTile))){
            throw new NoExitException();
        }else{
            startingTile=newTile;
        }
    }

}

