package csse2002.block.world;

import java.util.*;

/**
 * A sparse representation of tiles in an Array.
 *  * Contains Tiless stored with an associated Position (x, y) in a map.
 */
public class SparseTileArray {
    private Deque<Tile> toVisit;
    private Map<Position,Tile> tileMap;
    private Set<Tile> visited;
    private List<Tile> tiles;


    /**
     * Constructor for a SparseTileArray. Initializes an empty SparseTileArray,
     * such that getTile(new Position(x, y)) returns null for any x and y and
     * getTiles() returns an empty list.
     */
    public SparseTileArray(){
        toVisit = new LinkedList<Tile>();
        visited = new HashSet<Tile>();
    }

    /**
     * Get the tile at position (x, y). Return null if there is no tile at (x, y).<br />
     * Hint: Construct a Map<Position, Tile> in addLinkedTiles to allow
     * looking up tiles by position.
     *
     * @param position the tile position
     * @return the tile at (x, y) or null if no such tile exists.
     * @require position != null
     */
    public csse2002.block.world.Tile getTile(Position position){
        if (position == null) {
            return null;
        } else {
            return tileMap.get(position);
        }
    }

    /**
     * Get a set of ordered tiles from SparseTileArray in breadth-first-search
     * order.<br />
     * The startingTile (passed to addLinkTiles) should be the first tile in
     * the list. The following tiles should be the tiles at the "north",
     *"east", "south" and "west" exits from the starting tile, if they
     * exist. <br />
     * Then for each of those tiles, the next tiles will be their "north",
     *"east", "south" and "west" exits, if they exist. The order should
     * continue in the same way through all the tiles that are linked
     * to startingTile.
     * The list returned by getTiles may be immutable, and if not, changing
     * the list (i.e., adding or removing elements) should not change that
     * returned by subsequent calls to getTiles().
     * @return a list of tiles in breadth-first-search order.
     */
    public java.util.List<csse2002.block.world.Tile> getTiles() {
        return tiles;
    }

    /**
     * Add a set of tiles to the sparse tilemap. <br />
     * This function does the following:
     * <ol>
     *     <li>Remove any tiles that are already existing in the sparse map. </li>
     *     <li>Add startingTile at position (startingX, startingY), such that
     *         getTile(startingX, startingY) == startingTile. </li>
     *     <li>For each pair of linked tiles (tile1 at (x1, y1) and tile2 at
     *         (x2, y2) that are accessible from startingTile (i.e. there is a
     *         path through a series of exits startingTile.getExits().get
     *         ("north").getExits().get("east") ... between the two tiles),
     *         tile2 will get a new position based on tile1's position, and
     *         tile1's exit name.
     *         <ul>
     *             <li>tile2 at "north" exit should get a new position of
     *                 (x1, y1 - 1), i.e. getTile(x1, y1 - 1) == tile1.
     *                 getExits().get("north")</li>
     *             <li>tile2 at "East" exit should get a position of
     *                 (x1 + 1, y1), i.e. getTile(x1 + 1, y1) == tile1.
     *                 getExits().get("east")</li>-
     *             <li>tile2 at "South" exit should get a position of
     *                 (x1, y1 + 1), i.e. getTile(x1, y1 + 1) == tile1.
     *                 getExits().get("south")</li>
     *             <li>tile2 at "West" exit should get a position of
     *                 (x1 - 1, y1), i.e. getTile(x1 - 1, y1) == tile1.
     *                 getExits().get("west")</li>
     *         </ul></li>
     *      <li>If there are tiles that are not geometrically consistent, i.e.<br />
     *      Tiles that would occupy the same position or require two different <br />
     *      coordinates for getTile() method to work, throw a
     *      WorldMapInconsistentException.<br />
     *       Two examples of inconsistent tiles are: </li>
     *       <ul>
     *           <li>tile1.getExits().get("north").getExits().get("south) is
     *           non null and not == to tile1, throw a
     *           WorldMapInconsistentException. Note: one way exits are
     *           allowed, so tile1.getExits().get("north").getExits().
     *           get("south) == null would be acceptable, but tile1.getExits().
     *           get("north").getExits().get("south) == tile2 for some other<br />
     *           non-null tile2 is not.</li>
     *           <li>tile1.getExits().get("north").getExits().get("north") ==
     *           tile1. tile1 exits in two different places in this case. </li>
     *       </ul>
     *       <li>getTiles() should return a list of each accessible tile in a
     *           breadth-first search order (see getTiles()) </li>
     *       <li>If an exception is thrown, reset the state of the
     *           SparseTileArray such that getTile(x, y) returns null for any
     *           x and y. </li>
     * </ol>
     * @param startingTile- the starting point in adding the linked tiles. All
     *                    added tiles must have a path (via multiple exits) to
     *                    this tile.
     * @param startingX- the x coordinate of startingTile in the array
     * @param startingY - the y coordinate of startingTile in the array
     * @throws WorldMapInconsistentException if the tiles in the set are not
     * Geometrically consistent
     * @require startingTile != null
     * @Ensure tiles accessed through getTile() are geometrically consistent
     */
    public void addLinkedTiles(csse2002.block.world.Tile startingTile,
                               int startingX,
                               int startingY)
            throws WorldMapInconsistentException {
        reset();
        Position startPosition = new Position(startingX,startingY);
        tileMap = new HashMap<Position,Tile>();
        tiles = new ArrayList<>();
        List<Position> position = null;
        Position p = null;
        Position newPosition = null;

        tileMap.put(startPosition,startingTile);
        toVisit.add(startingTile);

        while (!toVisit.isEmpty()) {
            Tile removedTile = toVisit.removeFirst();
            //"tiles" is to store the Tile removed from toVisit and return
            // it in getTiles
            tiles.add(removedTile);

            if (!(visited.contains(removedTile))) {
                visited.add(removedTile);

                if (removedTile.getExits().get("north") != null) {
                    toVisit.add(removedTile.getExits().get("north"));
                }
                if (removedTile.getExits().get("east") != null) {
                    toVisit.add(removedTile.getExits().get("east"));
                }
                if (removedTile.getExits().get("south") != null) {
                    toVisit.add(removedTile.getExits().get("south"));
                }
                if (removedTile.getExits().get("west") != null) {
                    toVisit.add(removedTile.getExits().get("west"));
                }

                Map<String, Tile> exits = removedTile.getExits();
                Tile neighbour = null;
                if (!visited.contains(exits.get("north")) &&
                        exits.get("north") != null) {
                    position = getPosition(removedTile,tileMap);
                    neighbour = removedTile.getExits().get("north");
                    if (position.size() > 1) {
                        throw new WorldMapInconsistentException();
                    } else {
                        p = position.get(0);
                    }
                    newPosition = new Position(p.getX(),p.getY()-1);
                    tileMap.put(newPosition,neighbour);
                }
                if (!visited.contains(exits.get("east")) &&
                        exits.get("east") != null) {
                    position = getPosition(removedTile,tileMap);
                    neighbour = removedTile.getExits().get("east");
                    if (position.size() > 1) {
                        throw new WorldMapInconsistentException();
                    } else {
                        p = position.get(0);
                    }
                    newPosition = new Position(p.getX()+1,p.getY());
                    tileMap.put(newPosition,neighbour);
                }
                if (!visited.contains(exits.get("south")) &&
                        exits.get("south") != null) {
                    position = getPosition(removedTile,tileMap);
                    neighbour = removedTile.getExits().get("south");
                    if (position.size() > 1) {
                        throw new WorldMapInconsistentException();
                    } else {
                        p = position.get(0);
                    }
                    newPosition = new Position(p.getX(),p.getY()+1);
                    tileMap.put(newPosition,neighbour);
                }
                if (!visited.contains(exits.get("west")) &&
                        exits.get("west") != null) {
                    position = getPosition(removedTile,tileMap);
                    neighbour = removedTile.getExits().get("west");
                    if(position.size() > 1) {
                        throw new WorldMapInconsistentException();
                    } else {
                        p = position.get(0);
                    }
                    newPosition = new Position(p.getX()-1,p.getY());
                    tileMap.put(newPosition,neighbour);
                }

            }

            }

        }

    /**
     * This method is to ensure get the correct position of the corresponding
     * tile and we can assign the position to the neighbors of the tile<br />
     *<br />
     * Reference: Dr. Joel Fenwick. 2018 Semester One . CSSE7023  Programming
     * in the Large, Assignment 3 supplied code, BoundsMapper.java.
     *
     * @param tile the tile removed from the "toVisit"
     * @param map the map from the "tileMap"
     * @return  an unique position
     */
    private List<Position> getPosition(Tile tile, Map<Position,Tile> map) {
        List<Position> p = new LinkedList<>();
        for (Map.Entry<Position, Tile> e : map.entrySet()) {
            if (e.getValue().equals(tile)) {
                p.add(e.getKey());
            }
        }
        return p;
    }


    protected void reset() {
        toVisit = new LinkedList<Tile>();
        visited = new HashSet<Tile>();
    }

    }

