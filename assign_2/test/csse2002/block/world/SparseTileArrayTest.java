package csse2002.block.world;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SparseTileArrayTest {
    private  SparseTileArray s ;
    private Tile t1;
    private Tile t2;
    private Tile t3;
    private Tile t4;
    private Tile t5;
    private Tile t6;
    private Position p1;
    private Position p2;
    private Position p3;

    @Before
    public void setUp() throws Exception {
        s = new SparseTileArray();
        t1 = new Tile();
        t2 = new Tile();
        t3 = new Tile();
        t4 = new Tile();
        t5 = new Tile();
        t6 = new Tile();
        t1.addExit("north",t2);
        t1.addExit("east",t3);
        t1.addExit("south",t4);
        t1.addExit("west",t5);
        t2.addExit("west",t6);
        p1 = new Position(0,0);
        p2 = new Position(0,-1);
        p3 = new Position(-1,-1);


    }

    @Test
    public void getTile() {
        try {
            s.addLinkedTiles(t1, 0, 0);
            assertEquals(s.getTile(p1),t1);
            assertNotEquals(s.getTile(p2),t1);
        }catch (WorldMapInconsistentException wmie){}
    }

    @Test
    public void getTiles() {
        try {
            s.addLinkedTiles(t1, 0, 0);
            assertEquals(s.getTiles().get(0),t1);
            assertFalse(s.getTiles().size() != 6);
        } catch (WorldMapInconsistentException wmie) {}
    }

    @Test
    public void addLinkedTiles() {
        try {
            s.addLinkedTiles(t1, 0, 0);
            assertEquals(s.getTile(p2),t2);
            assertNotEquals(s.getTiles().get(2),t1);
        }catch(WorldMapInconsistentException wmie) {}
    }


}