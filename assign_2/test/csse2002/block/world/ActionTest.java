package csse2002.block.world;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader ;

import static org.junit.Assert.*;

public class ActionTest {
        private Action a1;
        private BufferedReader bf;

    @Before
    public void setUp() throws Exception {
        a1 = new Action(Action.MOVE_BLOCK,"north");
    }

    @Test
    public void getPrimaryAction() {
        assertTrue(a1.getPrimaryAction() == 1);
        assertFalse(a1.getPrimaryAction() == 0);
    }

    @Test
    public void getSecondaryAction() {
        assertTrue(a1.getSecondaryAction().equals("north"));
        assertFalse(a1.getSecondaryAction().equals(" "));
    }

    @Test
    public void loadAction() {
        String one = "MOVE_BUILDER north";
        BufferedReader input = new BufferedReader(new StringReader(one));

        try {
            assertEquals(a1.loadAction(input).getPrimaryAction(),0);
            assertNotEquals(a1.loadAction(input).getSecondaryAction(),"south");
        } catch (ActionFormatException afe) {}
        catch (NullPointerException npp){}

    }

    @Test
    public void processActions() {


    }

    @Test
    public void processAction() {

    }
}