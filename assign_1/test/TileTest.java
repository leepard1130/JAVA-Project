import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

public class TileTest {
    private Tile tile1;
    private Tile tile2;
    private Tile tile3;
    private Tile tile4;
    private List<Block> startingBlocks;
    private Map<String,Tile> exits;

    //To initialize the needed Objects
    @Before
    public void setUp() throws Exception {
//        startingBlocks = new ArrayList<>();//初始化startingBlocks
//        startingBlocks.add(new WoodBlock());//在startingBlocks加入Block

        tile1 = new Tile(startingBlocks);//
//        startingBlocks.add(new GrassBlock());
//        System.out.println(tile1.getBlocks());
//        System.out.println(startingBlocks);


//        tile2 = new Tile();
//        tile3 = new Tile();
//        exits = new HashMap<String, Tile>();
//
//        startingBlocks= new ArrayList<>();
//        startingBlocks.add(new SoilBlock());
//        tile4 = new Tile(startingBlocks);
    }

    //To test the constructor public Tile()
    @Test
    public void testFirstConstructor(){
        assertTrue(tile1.getBlocks().size()==3);
        assertFalse((tile1.getExits().size()==1));
    }

    //To test the constructor public Tile(List<Block> startingBlocks)
    @Test
    public void testSecondConstructor(){
        assertFalse(tile4.getBlocks()==null);
        assertTrue(tile4.getExits().size()==0);
    }

    //If number of blocks are greater than 8 TooHighException would be thrown
    @Test(expected = TooHighException.class)
    public void testNineBlocks() throws TooHighException{
        for(int i=0; i<8; i++){
            startingBlocks.add(new GrassBlock());
        }
        tile4 =  new Tile(startingBlocks);
    }

    /**
     * if Blocks elements ≥ 3 are instances of GroundBlock TooHighException<br>
     * would be thrown
     */
    @Test(expected = TooHighException.class)
    public void testGoundBlocksinIndex4() throws TooHighException{
        for(int i=0; i<3; i ++){
            startingBlocks.add(new GrassBlock());
        }
        tile4 = new Tile(startingBlocks);
    }

    /**
     * To test getExit() method. Firstly add exits to the empty tile then <br>
     *     then check if the name and the target tile are what I expect.
     * @throws NoExitException
     */
    @Test
    public void getExits() throws NoExitException{
        exits.put("exit2",tile2);
        exits.put("exit3",tile3);
        tile1.addExit("exit2",tile2);
        tile1.addExit("exit3", tile3);
        assertEquals(tile1.getExits(),exits);
        tile1.removeExit("exit2");
        assertFalse(tile1.getExits()==exits);
    }

    //To test the NoExitException in addExit method
//    @Test(expected = NoExitException.class)
//    public void testNoExitExceptionInaddExit() throws NoExitException{
//        tile1.addExit("exit2", null);
//    }
//
//    //To test getBlock method
//    @Test
//    public void getBlocks() {
//        assertEquals(3,tile1.getBlocks().size());
//        assertNotEquals("soil",tile1.getBlocks().get(2).getBlockType());
//    }
//
//    //To test getTopBlock method
//    @Test
//    public void getTopBlock() throws TooLowException{
//        assertEquals("grass",tile1.getTopBlock().getBlockType());
//        assertNotEquals("black",tile1.getTopBlock().getColour());
//
//        //catch TooLowException
//        try{
//            for(int i =0; i<4;i++) {
//                tile1.removeTopBlock();
//            }
//        }catch(TooLowException e){
//                System.out.println("No Blocks here");
//            }
//    }
//
//    //To test if TooLowException thrown when there is no blocks on the tile
//    @Test(expected = TooLowException.class)
//    public void testTooLowException() throws TooLowException{
//        for (int i = 2; i >=0; i--) {
//            tile1.getBlocks().remove(i);
//        }
//        tile1.removeTopBlock();
//    }
//
//    //To test removeTopBlock method
//    @Test
//    public void removeTopBlock() throws TooLowException {
//        tile1.removeTopBlock();
//        assertTrue(tile1.getTopBlock().equals(tile1.getBlocks().get(1)));
//        assertFalse(tile1.getBlocks().size()==1);
//    }
//
//    //To test addExit method
//    @Test
//    public void addExit() throws NoExitException{
//        tile1.addExit("exit2",tile2);
//        exits.put("exit2", tile2);
//        assertEquals(exits,tile1.getExits());
//
//        //overwrites the name
//        exits.put("exit2", tile2);
//        tile1.addExit("exit2",tile2);
//        assertEquals(exits,tile1.getExits());
//
//        //catch NoExitException
//        try{
//            tile1.addExit(null,tile2);
//        }catch (NoExitException n){
//            System.out.println("Name cannot be null.");
//        }
//    }
//
//    //To test NoExitException in removeExit method
//    @Test(expected = NoExitException.class)
//    public void testNameIsNotInExits() throws NoExitException{
//        tile1.removeExit("exit1");
//    }
//
//    //To test NoExitException in moveBlock method
//    @Test(expected = NoExitException.class)
//    public void testNameDoesNotExist() throws NoExitException{
//        try {
//            tile1.moveBlock("exit1");
//        }catch (TooHighException the) {
//            System.out.println("Shouldn't throw TooHighException");
//        }catch (InvalidBlockException ibe) {
//            System.out.println("Shouldn't throw InvalidBlockException");
//        }
//    }
//
//    //To test removeExit method
//    @Test
//    public void removeExit() throws NoExitException{
//        tile1.addExit("exit2", tile2);
//        exits.put("exit2", tile2);
//        assertEquals(exits, tile1.getExits());
//        tile1.removeExit("exit2");
//        assertNotEquals(exits,tile1.getExits());
//
//        //catch NoExitException
//        try{
//            tile1.removeExit(null);
//        }catch (NoExitException n){
//            System.out.println("Name cannot be null.");
//        }
//    }
//
//    //To test dig method
//    @Test
//    public void dig() throws TooLowException, InvalidBlockException{
//        assertTrue(tile1.dig().getColour()=="green");
//        assertFalse(tile1.dig().getBlockType()=="grass");
//    }
//
//    //if the block is not moveable and not diggable or the block is null
//    @Test(expected = InvalidBlockException.class)
//    public void testInvalidBlockException() throws InvalidBlockException{
//        try {
//            tile1.getBlocks().add(3, new StoneBlock());
//            tile1.dig();
//        }catch(TooLowException tle){
//            System.out.println("Shouldn't throw TooLowException.");
//        }
//    }
//
//    //To test moveBlock method
//    @Test
//    public void moveBlock() throws TooHighException,
//            InvalidBlockException,NoExitException, TooLowException{
//        WoodBlock w1 = new WoodBlock();
//        tile1.getBlocks().add(w1);
//        tile1.addExit("exit2",tile2);
//
//        tile1.moveBlock("exit2");
//        try {
//            assertEquals(tile2.getTopBlock(), w1);
//        }catch (TooLowException tle){
//        }
//        assertTrue(tile1.getBlocks().size()==3);
//        assertFalse(tile2.getBlocks().size()==3);
//
//        //to test the TooHighException in moveBlock method
//        try{
//            tile2.placeBlock(new WoodBlock());
//            tile1.placeBlock(new WoodBlock());
//            tile1.moveBlock("exit2");
//        }catch(TooHighException the){
//        }
//    }
//
//    //To test the InvalidBlockException when the block is not moveable
//    @Test(expected = InvalidBlockException.class)
//    public void testBlockIsNotMoveable() throws InvalidBlockException{
//        try {
//            tile1.addExit("exit1", tile2);
//            tile1.placeBlock(new StoneBlock());
//            tile1.moveBlock("exit1");
//        }catch(TooHighException the){
//            System.out.println("shouldn't throw TooHighException");
//        }catch (NoExitException nee){
//            System.out.println("shouldn't throw NoExitException");
//        }
//    }
//
//    //To test the InvalidBlockException when the block is null
//    @Test(expected = InvalidBlockException.class)
//    public void testBlockIsNull() throws InvalidBlockException{
//        try {
//            tile1.placeBlock(null);
//        }catch(TooHighException the){
//            System.out.println("shouldn't throw TooHighException");
//        }
//    }
//
//    //To test the placeBlock method
//    @Test
//    public void placeBlock() throws TooHighException,
//            InvalidBlockException,TooLowException{
//        WoodBlock w1 = new WoodBlock();
//        tile1.placeBlock(w1);
//        assertEquals(w1,tile1.getBlocks().get(tile1.getBlocks().size()-1));
//        assertFalse(tile1.getTopBlock().getBlockType()=="soil");
//    }
//
//    //To test the TooHighException in placeBlock method
//    @Test(expected = TooHighException.class)
//    public void testTooHighException() throws TooHighException{
//        WoodBlock w1 = new WoodBlock();
//        try {
//            for (int i = 3; i < 8; i++) {
//                tile1.placeBlock(w1);
//            }
//            tile1.placeBlock(w1);
//        }catch(InvalidBlockException ibe){
//            System.out.println("Shouldn't throw InvalidBlockException.");
//        }
//    }

}