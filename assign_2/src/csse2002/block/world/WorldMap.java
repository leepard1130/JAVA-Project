package csse2002.block.world;

import javafx.beans.property.SimpleListProperty;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;

/**
 * A class to store a world map
 */
public class WorldMap {
    private csse2002.block.world.Tile tile;
    private Position position;
    private csse2002.block.world.Builder builder;
    private SparseTileArray sparseTileArray;
    private Tile startingTile;

    /**
     *Constructs a new block world map from a startingTile, position and
     *  builder, such that getBuilder() == builder, getStartPosition() ==
     *  startPosition, and getTiles() returns a list of tiles that are<br />
     *  linked  to startingTile
     *
     * @param startingTile the tile which the builder starts on
     * @param startPosition the position of the starting tile
     * @param builder the builder who will traverse the block world
     * @throws WorldMapInconsistentException f there are inconsistencies in
     * the positions of tiles (such as two tiles at a single position)
     * @require startingTile != null, startPosition != null, builder != null,
     * builder.getCurrentTile() == startingTile
     */
    public WorldMap (csse2002.block.world.Tile startingTile,
                    Position startPosition,
                    csse2002.block.world.Builder builder)
            throws WorldMapInconsistentException {
        this.startingTile = startingTile;
        this.builder = builder;
        this.position = startPosition;
        sparseTileArray.addLinkedTiles(startingTile,position.getX(),position.getY());

    }
    /**
     * Construct a block world map from the given filename. The block world
     * map format is as follows: <br />
     * <br />
     * <code><startingX></code><br />
     * <code><startingY></code><br />
     * <code><builder's name></code><br />
     * <code> <inventory1>,<inventory2>, ... ,<inventoryN></code><br />
     * <br />
     * <code> total:<number of tiles></code><br />
     * <code> <tile0 id> <block1>,<block2>, ... ,<blockN></code><br />
     * <code> <tile1 id> <block1>,<block2>, ... ,<blockN></code><br />
     * <code>...</code><br />
     * <code> <tileN-1 id> <block1>,<block2>, ... ,<blockN></code><br />
     * <br />
     * <code>exits</code><br />
     * <code> <tile0 id> <name1>:<id1>,<name2>:<id2>, ... ,<nameN>:<idN></code><br />
     * <code> <tile1 id> <name1>:<id1>,<name2>:<id2>, ... ,<nameN>:<idN></code><br />
     * <code>...</code><br />
     * <code> <tileN-1 id> <name1>:<id1>,<name2>:<id2>, ... ,<nameN>:<idN></code><br />
     * <br />
     * <br />
     * For example:<br />
     * <br />
     * 1<br />
     * 2<br />
     * Bob<br />
     * wood,wood,wood,soil<br />
     * <br />
     * total : 4<br />
     * 0 soil,soil,grass,wood<br />
     * 1 grass,grass,soil<br />
     * 2 soil,soil,soil,wood<br />
     * 3 grass,grass,grass,stone<br />
     * <br />
     * exits<br />
     * 0 east:2,north:1,west:3<br />
     * 1 south:0<br />
     * 2 west:0<br />
     * 3 east:0<br />
     * <br />
     * <br />
     * Tile IDs are the ordering of tiles returned by getTiles() i.e. tile 0 is
     * getTiles().get(0). <br />
     * The ordering does not need to be checked when loading a map (but the
     * saveMap function below does when saving).<br />
     * Note: A blank line is required for an empty inventory, and lines with
     * just an ID followed by a space are required for: <br />
     * <ul>
     *     <li>A tile entry below "total:N", if the tile has no blocks </li>
     *     <li>A tile entry below "exits", if the tile has no exits </li>
     * </ul>
     * The function should do the following:
     * <ol>
     *     <li>Open the filename and read a map in the format given above. </li>
     *     <li>Construct a new Builder with the name and inventory from the
     *     file (to be returned by getBuilder()), and a starting tile set to
     *     the tile with ID 0 </li>
     *     <li>Construct a new Position for the starting position from the file
     *     to be returned as getStartPosition() </li>
     *     <li>Construct a Tile for each tile entry in the file (to be returned
     *     by getTiles() and getTile()) </li>
     *     <li>Link each tile by the exits that are given. </li>
     *     <li>Throw a WorldMapFormatException if the format of the file is
     *     incorrect. This includes: </li>
     *     <ul>
     *         <li>Any lines are missing, including the blank lines before "total:N", and before exits.</li>
     *         <li>startingX or startingY (lines 2 and 3) are not valid integers </li>
     *         <li>There are not N entries under the line that says "total:N" </li>
     *         <li>There are not N entries under the "exits" line </li>
     *         <li>N is not a valid integer, or N is negative </li>
     *         <li>The names of blocks in inventory and on tiles are not one of
     *         "grass", "soil", "wood", "stone" </li>
     *         <li>The names of exits in the "exits" sections are not one of
     *         "north", "east", "south", "west" </li>
     *         <li>The ids of tiles are not valid integers, are less than 0 or greater than N - 1 </li>
     *         <li>The ids that the exits refer to do not exist in the list of tiles </li>
     *         <li>loaded tiles contain too many blocks, or GroundBlocks that
     *         have an index that is too high (i.e., if the Tile or constructors
     *         would throw exceptions). </li>
     *         <li>A file operation throws an IOException that is not a FileNotFoundException </li>
     *         <li></li>
     *     </ul>
     *     <li>Throw a WorldMapInconsistentException if the format is correct,
     *     but tiles would end up in geometrically impossible locations
     *     (see SparseTileArray.addLinkedTiles()). </li>
     *     <li>Throw a FileNotFoundException if the file does not exist. </li>
     * </ol>
     * @param filename- the name to load the file from
     * @throws WorldMapFormatException - if the file is incorrectly formatted
     * @throws WorldMapInconsistentException - if the file is correctly
     * formatted, but has inconsistencies (such as overlapping tiles)
     * @throws java.io.FileNotFoundException - if the file does not exist
     * @require filename != null
     * @Ensure the loaded map is geometrically consistent
     */
    public WorldMap(String filename) throws
            WorldMapFormatException,
            WorldMapInconsistentException,
            FileNotFoundException {

        if (filename == null) {
            throw new FileNotFoundException();
        }

        BufferedReader bf = new BufferedReader(new FileReader(filename));
        int lineNum = 0;
        // regular expression for an integer number
        String regex = "[+-]?[0-9][0-9]";
        // compiling regex
        Pattern p = Pattern.compile(regex);

        try{
            int posX = 0;
            int posY = 0;
            List<Tile> tileList = new LinkedList<>();
            List<Block> builderInventory = new LinkedList<>();
            String line = bf.readLine();
            try{
                posX = Integer.parseInt(line);
            }catch (NumberFormatException nfe){
                throw new WorldMapFormatException();
            }
            lineNum++;

            line = bf.readLine();
            lineNum++;
            try{
                posY = Integer.parseInt(line);
            } catch (NumberFormatException nfe) {
                throw new WorldMapFormatException();
            }
            position = new Position(posX,posY);

            line = bf.readLine();
            String builderName = line;
            lineNum++;

            line = bf.readLine();
            lineNum++;
            String[] inventory = line.split(",");
            for (int a =0; a < line.length(); a++){
                if (!inventory[a].equals("soil") || !inventory[a].equals("wood")
                        || !inventory[a].equals("stone")|| !inventory[a].equals("grass")) {
                    throw new WorldMapFormatException();
                }

                if (inventory[a].equals("soild")) {
                    builderInventory.add(new SoilBlock());
                } else if (inventory[a].equals("wood")) {
                    builderInventory.add(new WoodBlock());
                } else if (inventory[a].equals("grass")) {
                    builderInventory.add(new GrassBlock());
                } else if (inventory[a].equals("stone")) {
                    builderInventory.add(new StoneBlock());
                }
            }

            //read a blank line
            line = bf.readLine();
            if(!line.equals("")){
                throw new WorldMapFormatException();
            }
            lineNum++;

            //read total:N
            line=bf.readLine();
            lineNum++;
            String[] pair = line.split(":");
            int tileNo;
            try{
                tileNo = Integer.parseInt(line.substring(6));
            } catch (NumberFormatException nfe) {
                throw new WorldMapFormatException();
            }


            for (int i = 0; i < Integer.parseInt(pair[1]); i++) {
                try {
                    line = bf.readLine();
                    lineNum++;
                    List<Block> listBlocks = new LinkedList<>();
                    String[] blocksOnTile = line.substring(2).split(",");
                    String[] lineone = line.split("\\s ,");
                    //check tile ID is greater than 0
                    if (Integer.parseInt(lineone[0]) < 0 ||
                            Integer.parseInt(lineone[0]) >= Integer.parseInt(pair[1])) {
                        throw new WorldMapFormatException();
                    }

                    if (blocksOnTile.length > 8) {
                        throw new WorldMapFormatException();
                    }

                    for (int a=3;a<9;a++){
                        if (blocksOnTile[a].equals("soil")
                                || blocksOnTile.equals("grass")){
                            throw new WorldMapFormatException();
                        }
                    }
                    //convert the string into object block
                    for (int b = 0;b<blocksOnTile.length;b++) {
                        if (blocksOnTile[b]=="soil") {
                            listBlocks.add(new SoilBlock());
                        } else if (blocksOnTile[b]=="wood") {
                            listBlocks.add(new WoodBlock());
                        } else if (blocksOnTile[b]=="grass") {
                            listBlocks.add(new GrassBlock());
                        } else {
                            listBlocks.add(new StoneBlock());
                        }
                    }
                    //create a new list to store the tile given by the filename one by one
                    Tile[] tile = new Tile[i];
                    tile[i] = new Tile(listBlocks);
                    tileList.add(tile[i]);

                }catch (IOException ioe) {
                }catch (TooHighException the){}
            }


            //read a blank line
            line = bf.readLine();
            if (line==null) {
                throw new WorldMapFormatException();
            }
            lineNum++;

            //read the line only has "exit"
            line = bf.readLine();
            lineNum++;

            for (int i=0;i<Integer.parseInt(pair[1]);i++) {
                try{
                    line = bf.readLine();
                    lineNum++;
                    //to generate a String[] to check if the exitID is a valid integer
                    String[] exitInformation = line.split("[\\s , ;]");
                    Matcher checkID = p.matcher(exitInformation[0]);
                    //to check the target tile ID is the valid integer
                    if(!(checkID.find() && checkID.group().equals(exitInformation[0]))){
                        throw new WorldMapFormatException();
                    }
                    //to check the exit name is north or south or east or west
                    for (int b = 0; b<exitInformation.length; b++){
                        if (b % 2!=0){
                            if (exitInformation[b] != "north"|| exitInformation[b]
                                    != "south"||exitInformation[b] !=
                                    "east"||exitInformation[b] != "west") {
                                throw new WorldMapFormatException();
                            }
                        }
                        //crate a empty exit list to store exit names
                        List<String> exitname = new ArrayList();
                        //create an empty target-tile list to store tiles
                        List<Tile> target = new ArrayList();
                        if (b>0 && b%2!=0) {
//                            String exitname = exitInformation[b];
                            //if the index is odd then it should represent an exit name
                            exitname.add(exitInformation[b]);
                        }else {
//                            Tile tileID = tileList.get(Integer.parseInt(exitInformation[b]));
                            //if the index is even then it should represent a tile
                            target.add(tileList.get(Integer.parseInt(exitInformation[b])));
                        }
                        //call the tile in the tilelist one by one and add the referd exits
                        tileList.get(b).addExit(exitname.get(b),target.get(b));
                    }
                } catch (IOException ioe){
                } catch (NoExitException nee) {

                }
            }

        }catch (IOException ioe) {
        }

    }


    /**
     * Gets the builder associated with this block world.
     * @return the builder object
     */
    public csse2002.block.world.Builder getBuilder(){
        return builder;
    }

    /**
     * Gets the starting position.
     * @return the starting position.
     */
    public Position getStartPosition(){
        return position;
    }

    /**
     * Get a tile by position.
     * @param position- get the Tile at this position
     * @return the tile at that position
     * @require position != null
     */
    public csse2002.block.world.Tile getTile(Position position){
        return sparseTileArray.getTile(position);
    }

    /**
     * Get a list of tiles in a breadth-first-search order (see SparseTileArray.getTiles() for details).
     * @return a list of ordered tiles
     */
    public List<csse2002.block.world.Tile> getTiles(){
        return sparseTileArray.getTiles();
    }

    /**
     * Saves the given WorldMap to a file specified by the filename. <br />
     * See the WorldMap(filename) constructor for the format of the map. <br />
     * The Tile IDs need to relate to the ordering of tiles returned by
     * getTiles() i.e. tile 0 is getTiles().get(0) <br />
     * The function should do the following:
     * <ol>
     *     <li>Open the filename and write a map in the format given in the
     *     WorldMap constructor. </li>
     *     <li>Write the starting position (given by getStartPosition()) </li>
     *     <li>Write the current builder's (given by getBuilder()) name and
     *     inventory.</li>
     *     <li>Write the number of tiles </li>
     *     <li>Write the index, and then each tile as given by getTiles()
     *     (in the same order). </li>
     *     <li>Write each tiles exits, as given by getTiles().get(id).
     *     getExits() </li>
     *     <li>Throw an IOException if the file cannot be opened for writing,
     *     or if writing fails. </li>
     * </ol>
     * @param filename - the filename to be written to
     * @throws IOException- if the file cannot be opened or written to.
     * @require filename != null
     */
    public void saveMap(String filename) throws IOException{
        if(filename==null){
            throw new IOException();
        }

        StringBuilder sb = new StringBuilder();
        sb.append(getStartPosition().getX());
        sb.append("\n");
        sb.append(getStartPosition().getY());
        sb.append("\n");
        sb.append(getBuilder().getName());
        sb.append("\n");
        for (int i = 0; i < getBuilder().getInventory().size()-1; i++) {
            sb.append(getBuilder().getInventory().get(i).getBlockType());
            sb.append(",");
        }
        sb.append(getBuilder().getInventory().get(getBuilder().getInventory().size()-1).getBlockType());
        sb.append("\n");

        //blank line
        sb.append("\n");

        sb.append("total : " + getTiles().size());
        sb.append("\n");
        for (int i = 0; i < getTiles().size(); i++){
            sb.append(i+" ");
            for (int a = 0;a < getTiles().get(i).getBlocks().size()-1; a++) {
                sb.append(getTiles().get(i).getBlocks().get(a).getBlockType());
                sb.append(",");
            }
            sb.append(getTiles().get(i).getBlocks().get(getTiles().get(i).getBlocks().size()-1));
            sb.append("\n");
        }

        //blank line
        sb.append("\n");

        Map<Tile,Integer> m = new HashMap<>();
        for(int i=0;i<getTiles().size();i++){
            Tile t = getTiles().get(i);
            m.put(t,i);
        }

        sb.append("exits" + "\n");
        List<Tile> tiles = getTiles();
        for(Integer a:m.values()) {
            sb.append(a+" ");
            for (int i = 0; i < tiles.size(); i++) {
                Map<String, Tile> exits = tiles.get(i).getExits();
                for (Map.Entry<String, Tile> entry : exits.entrySet()) {
                    sb.append(entry.getKey());
                    sb.append(":");
                    sb.append(m.get(entry.getValue()) + ",");
                }
            }
        }

        try{
            FileWriter fw = new FileWriter(filename);
            fw.write(sb.toString());
            fw.close();
        } catch (IOException ioe) {}

    }
}
