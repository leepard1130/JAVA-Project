package csse2002.block.world;

import java.io.*;

/**
 * Handles top-level interaction with performing actions on a WorldMap
 */
public class Main {
    /**
     * The entry point of the application.<br />
     * Takes 3 parameters an input map file (args[0]), actions (args[1]), and
     * an output map file (args[2]). <br />
     * The actions parameter can be either a filename, or the string
     * "System.in". ,<br />
     * This function does the following: <br />
     * <ul>
     *     <li>If there are not 3 parameters, (i.e. args.length != 3), print
     *     "Usage: program inputMap actions outputMap" using System.err.
     *     println() and then exit with status 1 (Hint: use System.exit()) </li>
     *     <li>Create a new WorldMap using the input map file.
     *     If an exception is thrown, print the exception to the console
     *     using System.err.println(), and then exit with status 2. </li>
     *     <li>Create a BufferedReader to read actions. If parameter 2 is a
     *     filename, the BufferedReader should be initialised using a new
     *     FileReader. If parameter 2 is the string "System.in", the buffered
     *     reader should be initialised using System.in and a new
     *     InputStreamReader. If an exception is thrown, print the exception
     *     to the console using System.err.println, and then exit with status 3. </li>
     *     <li>Call Action.processActions() using the created BufferedReader
     *     and WorldMap. If an exception is thrown, print the exception to the
     *     console using System.err.println, and then exit with status 4. </li>
     *     <li>Call WorldMap.saveMap() using the 3rd parameter to save
     *     the map to an output file. If an exception is thrown, print the
     *     exception to the console using System.err.println() and then
     *     exit with status 5. </li>
     * </ul><br />
     * To print an exception to System.err, use System.err.println(e), where<br />
     * e is the caught exception.
     * @param args - the input arguments to the program
     */
    public static void main(String[] args){
        WorldMap w = null;
        BufferedReader bf = null;

        if (args.length != 3) {
            System.err.println("Usage: program inputMap actions outputMap");
            System.exit(1);
        }

        try{
            w = new WorldMap(args[0]);
        } catch (WorldMapFormatException wmfe) {
            System.err.println("wmfe");
            System.exit(2);
        } catch (WorldMapInconsistentException wmie) {
            System.err.println("wmie");
            System.exit(2);
        } catch (FileNotFoundException fnfe) {
            System.err.println("fnfe");
            System.exit(2);
        }

        try{
            if (args[1].equals("System.in")) {
                bf = new BufferedReader(new InputStreamReader(System.in));
            } else {
                bf = new BufferedReader(new FileReader(args[1]));
            }
        } catch (FileNotFoundException fnfe) {
            System.err.println("fnfe");
            System.exit(3);
        }

        try {
            Action.processActions(bf, w);
        }catch (ActionFormatException afe) {
            System.err.println("afe");
            System.exit(4);
        }

        try {
            w.saveMap(args[2]);
        }catch (IOException ioe) {
            System.err.println("ioe");
            System.exit(5);
        }
    }

}
