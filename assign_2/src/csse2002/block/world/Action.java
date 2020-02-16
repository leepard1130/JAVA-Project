package csse2002.block.world;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents an Action which can be performed on the block world
 *     (also called world map).
 * An action is something that a builder can do on a tile in the block world.
 *     The actions include, moving the builder in a direction, moving a block
 *      in a direction,digging on the current tile the builder is standing on and
 *      dropping an item from a builder's inventory.
 */
public class Action {
    //DIG action which is represented by integer 2
    public static final int DIG = 2;
    //DROP action which is represented by integer 3
    public static final int DROP = 3;
    //MOVE_BLOCK action which is represented by integer 1
    public static final int MOVE_BLOCK = 1;
    //MOVE_BUILDER action which is represented by integer 0
    public static final int MOVE_BUILDER = 0;

    private int primaryAction;
    private String secondaryAction;

    /**
     * Create an Action that represents a manipulation of the blockworld. An
     * action is represented by a primary action  (one of MOVE_BUILDER,
     * MOVE_BLOCK, DIG or DROP), and a secondary action.
     * Whether a secondary action is required depends on the primary
     * action:
     * <ol>
     *      <li>MOVE_BUILDER and MOVE_BLOCK require a direction
     *      as the secondaryAction (one of "north", "east", "south" or
     *      "west"). </li>
     *      <li>DROP requires the index of at which a Block from the inventory
     *      should be dropped (stored as a string in this class, e.g., "1"). </li>
     *      <li>DIG does not require a secondary action, so an empty string
     *      can be passed to secondaryAction. </li>
     * </ol>
     * This constructor does not need to check primaryAction or secondaryAction,
     * it just needs to construct an action such that getPrimaryAction() ==
     * primaryAction, and getSecondaryAction().equals(secondaryAction).
     *
     * @param primaryAction   the action to be created
     * @param secondaryAction the supplementary information associated
     *                        with the primary action
     */
    public Action(int primaryAction, String secondaryAction) {
        this.primaryAction = primaryAction;
        this.secondaryAction = secondaryAction;
    }

    /**
     * Get the integer representing the Action (e.g., return 0 if Action is
     * MOVE_BUILDER)
     *
     * @returnthe primary action
     */
    public int getPrimaryAction() {
        if (primaryAction == DIG) {
            return 2;
        } else if (primaryAction == MOVE_BUILDER) {
            return 0;
        } else if (primaryAction == MOVE_BLOCK) {
            return 1;
        } else {
            return 3;
        }
    }

    /**
     * Gets the supplementary information associated with the Action
     *
     * @return the secondary action, or "" (empty string) if no secondary
     * action exists
     */
    public String getSecondaryAction() {
            return secondaryAction;
    }

    /**
     * Create a single Action if possible from the given reader.<br />
     * Read a line from the given reader and load the Action on that line.
     * Only load one Action (hint: reader.readLine()) and return the created action.
     * Each line consists of a primary action, and optionally a secondary action.
     * This function should do the following: <br />
     * <ul>
     *     </li>
     *     <li>If any line consists of 2 spaces or more spaces (i.e. more than 2<br />
     *      tokens) throws an ActionFormatException.
     *      <li>If the primary action is not one of MOVE_BLOCK, MOVE_BUILDER, DROP
     *      or DIG, throw an ActionFormatException. </li>
     *      <li>If the primary action is MOVE_BLOCK, MOVE_BUILDER or DROP, and the
     *      primary action is not followed by a secondary action, throws an
     *      ActionFormatException.</li>
     *      <li>If the primary action is DIG, and DIG is not on a line by
     *      itself, with no trailing whitespace, throws an ActionFormatException. </li>
     *      <li>If the primary action is MOVE_BLOCK, MOVE_BUILDER or DROP, then
     *      creates and return a new Action with the primary action constant
     *      with the same name, and the secondary action. This method does
     *      not check the secondary action. </li>
     *      <li>If the primary action is DIG, returns a new Action with the
     *      primary action constant DIG, and an empty string ("") for the<br />
     *      secondary action. </li>
     *      <li>If reader is at the end of the file, returns null. </li>
     *      <li>If an IOException is thrown by the reader, then throw an<br />
     *      ActionFormatException. </li>
     * </ul>
     * For details of the action format see Action.loadActions().<br />
     * <br />
     * @param reader the reader to read the action contents form
     * @return the created action, or null if the reader is at the end of the file.
     * @throws ActionFormatException if the line has invalid contents and
     * the action cannot be created
     * @require reader != null
     */
    public static Action loadAction(BufferedReader reader) throws
            ActionFormatException {
        String line = null;
        int blank = 0;

        //If any line consists of 2 spaces or more spaces
        try {
            line = reader.readLine();
            char[] c = line.toCharArray();
            for (int i = 0 ; i < line.length() ; i++) {
                if (c[i] == ' ') {
                    blank++;
                    if (blank == 2) {
                        throw new ActionFormatException();
                    }
                }
            }
                // If the primary action is not one of MOVE_BLOCK,
                // MOVE_BUILDER, DROP or DIG, throw an ActionFormatException.

            String[] s = line.split(" ");
            try {
                int primaryAct = Integer.parseInt(s[0]);
                if ( primaryAct != MOVE_BLOCK || primaryAct != MOVE_BUILDER
                        || primaryAct != DIG || primaryAct != DROP) {
                    throw new ActionFormatException();
                }

                if (primaryAct == MOVE_BUILDER || primaryAct == MOVE_BLOCK
                        || primaryAct == DROP) {
                    if(s.length != 2 ){
                        throw new ActionFormatException();
                    }
                    return new Action(primaryAct,s[1]);
                }
                if (primaryAct == DIG) {
                    if (s.length != 1) {
                        throw new ActionFormatException();
                    }
                    return new Action(primaryAct,s[1]);
                }
            } catch (NumberFormatException nfe){}

        } catch (IOException ioe) {
        }
        //If reader is at the end of the file, returns null.
        return null;
    }

    /**
     * Read all the actions from the given reader and perform them on the
     * given block world.<br/>
     * All actions that can be performed should print an appropriate
     * message (as outlined in processAction()), any invalid actions that
     * cannot be created or performed on the world map, should also
     * print an error message (also described in processAction()).<br />
     * Each message should be printed on a new line (Use System.out.
     * println()).<br />
     * Each action is listed on a single line, and one file can contain
     * multiple actions.<br />
     * Each action must be processed after it is read (i.e. do not
     * read the whole file first, read and process each action one
     * at a time). The file format is as follows:<br/>
     *<br />
     *<code>primaryAction1 secondaryAction1</code><br />
     * <code>primaryAction2 secondaryAction2</code><br />
     * <code>...</code><br />
     * <code>primaryActionN secondaryActionN</code><br />
     * <br />
     * <br />
     * There is a single space " " between each primaryAction and
     * secondaryAction.
     * The primaryAction should be one of the following values:
     * <ul>
     * <li>MOVE_BUILDER </li>
     * <li>MOVE_BLOCK </li>
     * <li>DIG </li>
     * <li>DROP </li>
     * </ul>
     * If the secondaryAction is present, it should be one of the following
     * values:
     * <ul>
     *     <li>north </li>
     *     <li>east</li>
     *     <li>south</li>
     *     <li>west</li>
     *     <li>(a number) for DROP action</li>
     * </ul>
     * An example file may look like this: <br />
     * <br / >
     * <code> MOVE_BUILDER north</code><br />
     * <code> MOVE_BUILDER south</code><br />
     * <code> MOVE_BUILDER west</code><br />
     * <code> DROP 1</code><br />
     * <code> DROP 3</code><br />
     * <code> DROP text</code><br />
     * <code> DIG</code><br />
     * <code> MOVE_BUILDER south</code><br />
     * <code> MOVE_BLOCK north</code><br />
     * <code> RANDOM_ACTION</code><br />
     * <br />
     * <br />
     * If all actions can be performed on the map, the output from the above
     * file is: <br />
     * <br />
     * <code> Moved builder north</code><br />
     * <code> Moved builder south</code><br />
     * <code> Moved builder west</code><br />
     * <code> Dropped a block from inventory</code><br />
     * <code> Dropped a block from inventory</code><br />
     * <code> Error: Invalid Action</code><br />
     * <code> Top block on current tile removed</code><br />
     * <code> Moved builder south</code><br />
     * <code> Moved builder north</code><br />
     * <br />
     *(The line "RANDOM_ACTION" should then cause an ActionFormatException
     *     to be thrown)<br />
     * <br />
     * <br />
     * Hint: Repeatedly call Action.loadAction() to get the next Action, and then
     * Action.processAction() to process the action.
     * @param reader reader - the reader to read actions from
     * @param startingMap the starting map that actions will be applied to
     * Throws:
     * @throws ActionFormatException - if loadAction throws an
     * ActionFormatException
     * @require reader != null, startingMap != null
     */
    public static void processActions(BufferedReader reader,
                                     WorldMap startingMap)
            throws ActionFormatException{
        int primeAct=0;
        Action action;

        try {
            String line = reader.readLine();
            while (line != null) {
                String[] s = line.split(" ");
                if (s[0].equals("MOVE_BLOCK")) {
                    primeAct = MOVE_BLOCK;
                } else if (s[0].equals("MOVE_BUILDER")) {
                    primeAct = MOVE_BUILDER;
                } else if (s[0].equals("DROP")) {
                    primeAct = DROP;
                } else if (s[0].equals("DIG")) {
                    primeAct = DIG;
                }

                action = new Action(primeAct,s[1]);
                processAction(action,startingMap);

            }
        } catch (IOException ioe) {}


    }

    /**
     * Perform the given action on a WorldMap, and print output to System.out.
     * After this method finishes, map should be updated. (e.g., If the
     * action is DIG, the Tile on which the builder is currently on should
     * be updated to contain 1 less block (Builder.digOnCurrentTile()).
     * The builder to use for actions is that given by map.getBuilder().
     * Do the following for these actions:
     * <ul>
     * <li>For DIG action: call Builder.digOnCurrentTile(), then print to
     * console "Top block on current tile removed".</li>
     *     <li>For DROP action: call Builder.dropFromInventory(), then print
     *         to console "Dropped a block from inventory". The dropped item is
     *         given by action.getSecondaryAction(), that is first converted
     *         to an int. If the action.getSecondaryAction() cannot be
     *         converted to an int, print "Error: Invalid action" to the
     *         console. Valid integers (including negative integers and
     *         large positive integers) should be passed to
     *         Builder.dropFromInventory(). </li>
     *     <li>For the MOVE_BLOCK action: call Tile.moveBlock() on the
     *         builder's current tile (Builder.getCurrentTile()), then print
     *         to console "Moved block {direction}". The direction is given
     *         by action.getSecondaryAction()
     *     </li>
     *     <li>For MOVE_BUILDER action: call Builder.moveTo(), then print
     *         to console "Moved builder {direction}". The direction is given by
     *         action.getSecondaryAction()
     *     </li>
     *     <li>If action.getPrimaryAction() < 0 or action.getPrimaryAction()
     *         > 3, or action.getSecondary() is not a direction (for MOVE_BLOCK
     *         or MOVE_BUILDER), or a valid integer (for DROP) then print to
     *         console "Error: Invalid action" </li>
     * </ul>
     * "{direction}" is one of "north", "east", "south" or "west".
     * For handling exceptions do the following:
     * <ul>
     *     <li>If a NoExitException is thrown, print to the console "No exit
     *         this way" </li>
     *     <li>If a TooHighException is thrown, print to the console
     *         "Too high"
     *     </li>
     *     <li>If a TooLowException is thrown, print to the console "Too low" </li>
     *     <li>If an InvalidBlockException is thrown, print to the console
     *         "Cannot use that block" </li>
     * </ul>
     * Each line printed to the console should have a trailing newline
     *     (i.e., use System.out.println()).
     * @param action the action to be done on the map
     * @param map the map to perform the action on
     * @require action != null, map != null
     */
    public static void processAction(Action action,WorldMap map) {
        if (action.getPrimaryAction() < 0 || action.getPrimaryAction() > 3) {
            System.out.println("Error: Invalid action");
        }

        if (action.getPrimaryAction() == MOVE_BUILDER || action.getPrimaryAction() == MOVE_BLOCK) {
            String direction = action.getSecondaryAction();
            if (!(direction.equals("north")) || !(direction.equals("east")) || !(direction.equals("south"))
                    || !(direction.equals("west"))){
                System.out.println("Error: Invalid action" );
            }
        }
        try {
            if (action.getPrimaryAction() == DIG) {
                map.getBuilder().digOnCurrentTile();
                System.out.println("Top block on current tile removed");
            }
        } catch (InvalidBlockException ibe) {
            System.out.println("Cannot use that block");
        } catch (TooLowException tle) {
            System.out.println("Too low");
        }
        try{
            if(action.getPrimaryAction() == DROP) {
                map.getBuilder().dropFromInventory(Integer.parseInt(action.getSecondaryAction()));
            }
        }catch(InvalidBlockException ibe) {
            System.out.println("Cannot use that block");
        }catch (TooHighException the) {
            System.out.println("Too high");
        }catch (NumberFormatException nfe) {
            System.out.println("Error: Invalid action");
        }
        try{
            if (action.getPrimaryAction() == MOVE_BLOCK) {
                map.getBuilder().getCurrentTile().moveBlock(action.getSecondaryAction());
                System.out.println("Moved block"+action.getSecondaryAction());
            }
        }catch (NoExitException nee){
            System.out.println("No exit this way" );
        }catch (TooHighException the){
            System.out.println("Too high");
        }catch (InvalidBlockException ibe) {
            System.out.println("Cannot use that block");
        }
        try{
            if (action.getPrimaryAction() == MOVE_BUILDER) {
                map.getBuilder().moveTo(map.getBuilder().getCurrentTile().getExits().get(action.getSecondaryAction()));
                System.out.println("Moved builder" + action.getSecondaryAction());
            }
        } catch (NoExitException nee) {
            System.out.println("No exit this way");
        }

    }
}
