import org.junit.Test;

import java.io.Serializable;
import java.util.*;

/**
 * Tiles for a map.
 * Contains Blocks
 * Maintains a mapping between exit names and other tiles.
 */
public class Tile implements Serializable {
    //blocks on the current Tile
    private List<Block> blockList;
    //exits on the Tile
    private Map<String, Tile> exits;

    /**
     * Construct a new tile
     * Each tile should be constructed with no exits (getExits().size() == 0).
     * Each tile must be constructed to start with two soil blocks and then <br>
     * a grass block on top.
     * i.e. getBlocks() must contain {SoilBlock, SoilBlock, GrassBlock} <br>
     * for a new Tile.
     */
    public Tile(){
        exits = new HashMap();
        blockList = new ArrayList<>();
        blockList.add(new SoilBlock());
        blockList.add(new SoilBlock());
        blockList.add(new GrassBlock());
    }

    /**
     * Construct a new tile.
     * Each tile should be constructed with no exits (getExits().size() == 0).
     * Set the blocks on the tile to be the contents of startingBlocks.
     * iIndex 0 in startingBlocks is the lowest block on the tile, while<br>
     * index N -1 is the top block on the tile for N blocks.
     * startingBlocks cannot be null.
     * i.e. getBlocks() must contain the contents of startingBlocks, but <br>
     * modifying startingBlocks after constructing the Tile should not<br>
     * change the results of getBlocks().
     * Handle the following cases:
     * <ol>
     *     <li>If startingBlocks contains 8 or more elements, <br>
     *     throw a TooHighException. </li>
     *     <li>If startingBlocks contains an instance of GroundBlock <br>
     *     that is at an index of 3 or higher, throw a TooHighException. </li>
     * </ol>
     *
     * @param startingBlocks - a list of blocks on the tile, cannot be null
     * @throws TooHighException - if startingBlocks.size() > 8, <br>
     * or if startingBlocks elements ≥ 3 are instances of GroundBlock
     */
    public Tile(List<Block> startingBlocks)
            throws TooHighException{
        exits = new HashMap();
        blockList=new ArrayList<>(startingBlocks);

        if (startingBlocks.size() > 8){
            throw new TooHighException();
        }

        for(int i = 3; i<startingBlocks.size();i++){
            if (blockList.get(i) instanceof GroundBlock){
                throw new TooHighException();
            }
        }
    }

    /**
     * What exits are there from this Tile?
     * No ordering is required.
     * @return map of names to Tiles
     */
    public Map<String,Tile> getExits(){
        return exits;
    }

    /**
     * What Blocks are on this Tile?
     * Order of blocks returned must be in order of height.
     * Index 0 is bottom, and index N - 1 is the top, for N blocks.
     *
     * @return Blocks on the Tile
     */
    public List<Block> getBlocks(){
        return this.blockList;
    }

    /**
     * Return the block that is the top block on the tile.
     * If there are no blocks, throw a TooLowException
     *
     * @return top Block or null if no blocks
     * @throws TooLowException - if there are no blocks on the tile
     */
    public Block getTopBlock() throws TooLowException{
        if(blockList.size()==0){
            throw new TooLowException();
        }else{
            return blockList.get(blockList.size()-1);
        }
    }

    /**
     * Remove the block on top of the tile
     * Throw a TooLowException if there are no blocks on the tile
     *
     * @throws TooLowException
     */
    public void removeTopBlock() throws TooLowException{
        if(blockList.size()==0){
            throw new TooLowException();
        }else {
            blockList.remove(blockList.size()-1);
        }
    }

    /**
     * Add a new exit to this tile
     * The Map returned by getExits() must now include an entry (name, target).<br>
     * Overwrites any existing exit with the same name
     * If name or target is null, throw a NoExitException
     *
     * @param  name - Name of the exit
     * @param  target - Tile the exit goes to
     * @throws NoExitException - if name or target is null
     */
    public void addExit(String name, Tile target)
            throws NoExitException{
        if(target==null|| name ==null) {
            throw new NoExitException();
        }else if(exits.containsKey(name)){
            exits.put(name,target);
        }else{
            exits.put(name,target);
        }
    }

    /**
     * Remove an exit from this tile
     * The Map returned by getExits() must no longer have the key name.
     * If name does not exist in getExits(), or name is null, throw a NoExitException.
     *
     * @param name - Name of exit to remove
     * @throws NoExitException - if name is not in exits, or name is null
     */
    public void removeExit(String name) throws NoExitException{
        if(name==null|| !exits.containsKey(name)){
            throw new NoExitException();
        }else{
            exits.remove(name);
        }
    }

    /**
     * Attempt to dig in the current tile.
     * If the top block (given by getTopBlock()) is diggable (block.isDiggable()),<br>
     * remove the top block of the tile and return it.
     * Handle the following cases:
     * <ol>
     *      <li>Throw a TooLowException if there are no blocks on the tile</li>
     *      <li>Throw an InvalidBlockException if the block is not diggable</li>
     *</ol>
     * @return the removed block
     * @throws TooLowException - if there are no blocks on the tile
     * @throws InvalidBlockException - if the block is not diggable
     */
    public Block dig() throws TooLowException,
            InvalidBlockException{
        //assign topBlock to store the top block
        List<Block> topBlock = new ArrayList<>();

        if(blockList.size()==0){
            throw new TooLowException();
        }

        if(!blockList.get(blockList.size() - 1).isDiggable()) {
            throw new InvalidBlockException();
        }else{
            topBlock.add(blockList.get(blockList.size()-1));
            blockList.remove(blockList.size()-1);
            return topBlock.get(0);
        }
    }

    /**
     * Attempt to move the current top block to another tile. Remove the top <br>
     * block (given by getTopBlock()) from this tile and add it to the tile at the <br>
     *  named exit (exitName in getExits()), if the block is<br>
     * moveable (block.isMoveable()) and the height of that tile<br>
     * (the number of blocks given by getBlocks().size()) is less than the current <br>
     * tile *before* the move.
     * Handle the following cases:
     * <ul>
     *      <li>If the exit is null, or does not exist, throw a NoExitException</li>
     *      <li>If the number of blocks on the target tile is ≥ to this one, throw a <br>
     *      TooHighException</i>
     *      <li>If the block is not moveable, throw a InvalidBlockException</li>
     *</ul>
     *
     * @param exitName - the name of the exit to move the block to
     * @throws TooHighException - if the target tile is ≥ to this one.
     * @throws InvalidBlockException - if the block is not moveable
     * @throws NoExitException - if the exit is null or does not exist
     */
    public void moveBlock(String exitName)throws TooHighException,
            InvalidBlockException,NoExitException{
        if(!(exits.containsKey(exitName)) || exits ==null){
            throw new NoExitException();
        }else if(!(blockList.get(blockList.size() - 1).isMoveable())){
            throw new InvalidBlockException();
        }else if(blockList.size()<=exits.get(exitName).getBlocks().size()){
            throw new TooHighException();
        }else{
            exits.get(exitName).blockList.add(blockList.get(blockList.size()-1));
            blockList.remove(blockList.size()-1);
        }
    }

    /**
     * Place a block on a tile. Add the block to the top of the blocks on this<br>
     * tile. If the block is an instance of GroundBlock, it can only be<br>
     * placed underground. Handle the following cases:
     * <ul>
     *     <li>If the block is null, throw an InvalidBlockException </li>
     *     <li>If the target tile has 8 blocks already, or if the block is a <br>
     *     GroundBlock and the target block has 3 or more blocks already,.<br>
     *      throw a TooHighException </li>
     * </ul>
     *
     * @param block - the block to place.
     * @throws TooHighException - if there are already 8 blocks on the<br>
     *  tile, or if this is a ground block and there are already 3 or more <br>
     *  blocks on the tile.
     * @throws InvalidBlockException - if the block is null
     */
    public void placeBlock(Block block) throws TooHighException,
            InvalidBlockException{
        if(block==null){
            throw new InvalidBlockException();
        }else if((blockList.size() == 8) || (block instanceof
                GroundBlock && blockList.size()>=3)){
            throw new TooHighException();
        }else{
            blockList.add(block);
        }
    }

}
