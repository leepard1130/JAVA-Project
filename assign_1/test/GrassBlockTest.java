import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for GrassBlock
 */
public class GrassBlockTest {

    private GrassBlock grassBlockA;
    private GrassBlock grassBlockB;



    @Before
    public void setUp() throws Exception {

        //correct assumption
        grassBlockA = new GrassBlock();
        //incorrect assumption
        grassBlockB = new GrassBlock();
    }

    @Test
    public void getColour() {
        assertTrue(grassBlockA.getColour()=="green");
        assertFalse(grassBlockB.getColour()=="black");
    }

    @Test
    public void getBlockType() {
        assertTrue(grassBlockA.getBlockType()=="grass");
        assertFalse(grassBlockB.getBlockType()=="soil");
    }

    @Test
    public void isCarryable() {
        assertTrue(grassBlockA.isCarryable()==false);
        assertFalse(grassBlockB.isCarryable()==true);
    }

    @Test
    public void isMoveable() {
        assertTrue(grassBlockA.isMoveable()==false);
        assertFalse(grassBlockB.isMoveable()==true);
    }

    @Test
    public void isDiggable() {
        assertTrue(grassBlockA.isDiggable()==true);
        assertFalse(grassBlockB.isDiggable()==false);
    }
}