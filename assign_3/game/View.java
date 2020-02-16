package game;

import csse2002.block.world.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class View {
    // The root node of the scene graph, to add all the GUI elements to
    private HBox rootBox;
    // Canvas context, this context allows us to draw stuff on the canvas
    private GraphicsContext context;
    //Buttons on the stage
    private Button[] buttons;
    //to indicate to drop which blocks
    private TextField dropIndex;
    //file menu
    private MenuButton fileButton;
    //move builder & move block button
    private MenuButton m;
    //dig button
    private Button dig;
    //drop button
    private Button drop;
    //menuItem in the menuButton
    private MenuItem moveBlock;
    //menuItem Move Builder
    private MenuItem moveBuilder;
    //menuItem load game world
    private MenuItem loadGame;
    //menuItem save world game
    private MenuItem saveGame;
    private ChoiceBox choiceBox;
    protected Label inventoryContent;

    protected HashMap<Tile, Position> tileCoordination;
    private HashMap<Position,Tile> tileMap;
    private Builder builder;

    private Stage stage;
    protected WorldMap worldMap;
    private  String fileName ;
    private List<Block> builderInventory;
    private MapMaker mapMaker;

    private int primaryAction;
    private String secAction;

    public View(Stage mainStage) {
        mapMaker = new MapMaker(450,450);
        tileCoordination= new HashMap<>();
        tileMap = new HashMap<>();
        this.stage =mainStage;
        rootBox = new HBox();
        addComponents();
        addMenuHandler(new MenuHandler());
        addButtonHandler(new ButtonHandler());
        addChoiceBoxHandler(new ChoiceHandler());
        addTextHandler(new TextHandler());
    }


    private void addComponents() {
        VBox leftBox = new VBox();
        leftBox.setPadding(new Insets(10, 10, 10, 10));
        leftBox.setStyle("-fx-background-color: white");
        addLeftSideComponents(leftBox);

        VBox rightBox = new VBox();
        rightBox.setSpacing(10);
        rightBox.setPadding(new Insets(20, 20, 20, 20));
        rightBox.setStyle("-fx-background-color: white");
        addRightSideComponents(rightBox);

        rootBox.getChildren().addAll(leftBox,rightBox);
    }

    /**
     * get the scene of the GUI
     *
     * @return current scene
     */
    public Scene getScene() {
        return new Scene(rootBox);
    }

    private void addRightSideComponents(VBox rightBox) {
        HBox controll = new HBox();
        buttons = new Button[4];
        String[] buttonName = {"north","east","south","west"};
         for (int i = 0; i< buttons.length; i++ ) {
            buttons[i] = new Button(buttonName[i]);
            buttons[i].setPrefSize(100,50);
            buttons[i].setTextFill(Color.WHITE);
            buttons[i].setStyle("-fx-background-color:#0400ff;");
            buttons[i].setDisable(true);
        }
        GridPane gp = new GridPane();
        gp.add(buttons[0],1,0);
        gp.add(buttons[1],2,1);
        gp.add(buttons[2],1,2);
        gp.add(buttons[3],0,1);
        controll.getChildren().addAll(gp);
        controll.setAlignment(Pos.CENTER);


        HBox menu = new HBox();
        choiceBox = new ChoiceBox();
        choiceBox.getItems().addAll("Move Builder", "Move Block");
        choiceBox.setValue("Move Builder");
        choiceBox.setDisable(true);

        menu.getChildren().addAll(choiceBox);
        menu.setAlignment(Pos.CENTER);

        HBox digButton = new HBox();
        dig = new Button("Dig");
        dig.setTextFill(Color.WHITE);
        dig.setStyle("-fx-background-color:#0400ff;");
        dig.setDisable(true);
        digButton.getChildren().addAll(dig);
        digButton.setAlignment(Pos.TOP_LEFT);

        HBox digAndText = new HBox();
        drop = new Button("Drop");
        drop.setTextFill(Color.WHITE);
        drop.setStyle("-fx-background-color:#0400ff;");
        drop.setDisable(true);

        dropIndex = new TextField();
        dropIndex.setPromptText("Drop Index");
        dropIndex.setPrefSize(240,20);
        dropIndex.setEditable(true);
        digAndText.getChildren().addAll(drop,dropIndex);

        rightBox.getChildren().addAll(controll,menu,digButton,digAndText);
    }

    private void addLeftSideComponents(VBox leftBox) {
        fileButton = new MenuButton("File");
        loadGame = new MenuItem("Load Game World");
        saveGame = new MenuItem("Save World Game");
        fileButton.getItems().addAll(loadGame,saveGame);

        HBox canvasContainer = new HBox();
        mapMaker = new MapMaker(450,450);
        canvasContainer.getChildren().add(mapMaker);
        canvasContainer.setStyle("-fx-border-color: black");

        HBox inventory = new HBox();
        inventoryContent = new Label("Inventory \n []");
        inventory.getChildren().addAll(inventoryContent);
        inventory.setAlignment(Pos.TOP_LEFT);

        leftBox.getChildren().addAll(fileButton,canvasContainer,inventory);
    }

    private class TextHandler implements EventHandler<ActionEvent> {

        public void handle(ActionEvent event) {
            TextField text = (TextField) event.getSource();
            secAction = text.getText();
        }
    }

    public void addTextHandler (EventHandler<ActionEvent> handler) {

    }

    private class ChoiceHandler implements EventHandler<ActionEvent> {

        public void handle(ActionEvent event) {
            ChoiceBox choiceBox = (ChoiceBox) event.getSource();
            String options = (String) choiceBox.getSelectionModel().
                    getSelectedItem();

            if (options.equals("Move Builder")) {
                primaryAction = 0;
            } else {
                primaryAction = 1;
            }
        }
    }

    public void addChoiceBoxHandler (EventHandler<ActionEvent> handler) {
        choiceBox.setOnAction(handler);
    }

    private class ButtonHandler implements EventHandler<ActionEvent> {

        public void handle(ActionEvent event) {
            Button pressedButton = (Button) event.getSource();
            String btnName = pressedButton.getText();
            builderAction(btnName);
        }
    }

    public void addButtonHandler(EventHandler<ActionEvent> handler) {
        for (Button b : buttons) {
            b.setOnAction(handler);
        }
        dig.setOnAction(handler);
        drop.setOnAction(handler);
    }

    private void goNorth(){
        Action action = null;
        ByteArrayOutputStream exception = new ByteArrayOutputStream();
        System.setOut(new PrintStream(exception));
        Alert errorMsg = new Alert(Alert.AlertType.ERROR);
        action = new Action(primaryAction,"north");

        Action.processAction(action, worldMap);
        mapMaker.update();
        Tile currentTile = worldMap.getBuilder().getCurrentTile();
        Tile moveToTile = currentTile.getExits().get("north");

        String error = exception.toString().trim();
        if(error.equals("No exit this way")) {
            if (moveToTile.getBlocks().size() - currentTile.getBlocks().size()
                    >= 2) {
                errorMsg.setTitle("Cannot move builder");
                errorMsg.setHeaderText("Target tile is too high");
                errorMsg.setContentText(null);
                errorMsg.showAndWait();
                return;
            }
            errorMsg.setTitle(error);
            errorMsg.setHeaderText("No exit in the intended direction");
            errorMsg.setContentText(null);
            errorMsg.showAndWait();
        } else if (error.equals("Too high")) {
            errorMsg.setTitle(error);
            errorMsg.setHeaderText("Target tile is too high");
            errorMsg.setContentText(null);
            errorMsg.showAndWait();
        } else if (error.equals("Cannot use that block")) {
            errorMsg.setTitle(error);
            errorMsg.setHeaderText("Top block is not movable");
            errorMsg.setContentText(null);
            errorMsg.showAndWait();
        }

    }

    private void goEast(){
        Action action = null;
        ByteArrayOutputStream exception = new ByteArrayOutputStream();
        System.setOut(new PrintStream(exception));
        Alert errorMsg = new Alert(Alert.AlertType.ERROR);
        action = new Action(primaryAction,"east");

        Action.processAction(action, worldMap);
        mapMaker.update();

        Tile currentTile = worldMap.getBuilder().getCurrentTile();
        Tile moveToTile = currentTile.getExits().get("east");
        String error = exception.toString().trim();
        if(error.equals("No exit this way")) {
            if (moveToTile.getBlocks().size() - currentTile.getBlocks().size()
                    >= 2) {
                errorMsg.setTitle("Cannot move builder");
                errorMsg.setHeaderText("Target tile is too high");
                errorMsg.setContentText(null);
                errorMsg.showAndWait();
                return;
            }
            errorMsg.setTitle(error);
            errorMsg.setHeaderText("No exit in the intended direction");
            errorMsg.setContentText(null);
            errorMsg.showAndWait();
        } else if (error.equals("Too high")) {
            errorMsg.setTitle(error);
            errorMsg.setHeaderText("Target tile is too high");
            errorMsg.setContentText(null);
            errorMsg.showAndWait();
        } else if (error.equals("Cannot use that block")) {
            errorMsg.setTitle(error);
            errorMsg.setHeaderText("Top block is not movable");
            errorMsg.setContentText(null);
            errorMsg.showAndWait();
        }

    }

    private void goSouth(){
        Action action = null;
        ByteArrayOutputStream exception = new ByteArrayOutputStream();
        System.setOut(new PrintStream(exception));
        Alert errorMsg = new Alert(Alert.AlertType.ERROR);
        action = new Action(primaryAction,"south");

        Action.processAction(action, worldMap);
        mapMaker.update();
        Tile currentTile = worldMap.getBuilder().getCurrentTile();
        Tile moveToTile = currentTile.getExits().get("south");

        String error = exception.toString().trim();
        if(error.equals("No exit this way")) {
            if (moveToTile.getBlocks().size() - currentTile.getBlocks().size()
                    >= 2) {
                errorMsg.setTitle("Cannot move builder");
                errorMsg.setHeaderText("Target tile is too high");
                errorMsg.setContentText(null);
                errorMsg.showAndWait();
                return;
            }
            errorMsg.setTitle(error);
            errorMsg.setHeaderText("No exit in the intended direction");
            errorMsg.setContentText(null);
            errorMsg.showAndWait();
        } else if (error.equals("Too high")) {
            errorMsg.setTitle(error);
            errorMsg.setHeaderText("Target tile is too high");
            errorMsg.setContentText(null);
            errorMsg.showAndWait();
        } else if (error.equals("Cannot use that block")) {
            errorMsg.setTitle(error);
            errorMsg.setHeaderText("Top block is not movable");
            errorMsg.setContentText(null);
            errorMsg.showAndWait();
        }

    }

    private void goWest(){
        Action action = null;
        ByteArrayOutputStream exception = new ByteArrayOutputStream();
        System.setOut(new PrintStream(exception));
        Alert errorMsg = new Alert(Alert.AlertType.ERROR);
        action = new Action(primaryAction,"west");

        Action.processAction(action, worldMap);
        mapMaker.update();
        Tile currentTile = worldMap.getBuilder().getCurrentTile();
        Tile moveToTile = currentTile.getExits().get("west");

        String error = exception.toString().trim();
        if(error.equals("No exit this way")) {
            if (moveToTile.getBlocks().size() - currentTile.getBlocks().size()
                    >= 2) {
                errorMsg.setTitle("Cannot move builder");
                errorMsg.setHeaderText("Target tile is too high");
                errorMsg.setContentText(null);
                errorMsg.showAndWait();
                return;
            }
            errorMsg.setTitle(error);
            errorMsg.setHeaderText("No exit in the intended direction");
            errorMsg.setContentText(null);
            errorMsg.showAndWait();
        } else if (error.equals("Too high")) {
            errorMsg.setTitle(error);
            errorMsg.setHeaderText("Target tile is too high");
            errorMsg.setContentText(null);
            errorMsg.showAndWait();
        } else if (error.equals("Cannot use that block")) {
            errorMsg.setTitle(error);
            errorMsg.setHeaderText("Top block is not movable");
            errorMsg.setContentText(null);
            errorMsg.showAndWait();
        }


    }

    private void doDig(){
        Action action = null;
        ByteArrayOutputStream exception = new ByteArrayOutputStream();
        System.setOut(new PrintStream(exception));
        Alert errorMsg = new Alert(Alert.AlertType.ERROR);
        action = new Action(2,"");

        Action.processAction(action, worldMap);
        mapMaker.update();
        inventoryContent.setText(mapMaker.writeInventory(worldMap.getBuilder()));
        String error = exception.toString().trim();

        if(error.equals("No exit this way")) {
            errorMsg.setTitle(error);
            errorMsg.setHeaderText("No exit in the intended direction");
            errorMsg.setContentText(null);
            errorMsg.showAndWait();
        } else if (error.equals("Too high")) {
            errorMsg.setTitle(error);
            errorMsg.setHeaderText("Target tile is too high");
            errorMsg.setContentText(null);
            errorMsg.showAndWait();
        } else if (error.equals("Cannot use that block")) {
            errorMsg.setTitle(error);
            errorMsg.setHeaderText("Top block is not diggable");
            errorMsg.setContentText(null);
            errorMsg.showAndWait();
        }

    }

    private void doDrop(){
        Action action = null;
        ByteArrayOutputStream exception = new ByteArrayOutputStream();
        System.setOut(new PrintStream(exception));
        Alert errorMsg = new Alert(Alert.AlertType.ERROR);
        action = new Action(3,secAction);

        Action.processAction(action, worldMap);
        mapMaker.update();
        inventoryContent.setText(mapMaker.writeInventory(worldMap.getBuilder()));
    }

    public void builderAction(String buttonName) {
        switch (buttonName){
            case "north":
                goNorth();
                break;
            case "east":
                goEast();
                break;
            case "south":
                goSouth();
                break;
            case "west":
                goWest();
                break;
            case "Dig":
                doDig();
                break;
            case "Drop":
                doDrop();
                break;
        }

        }

    public void addMenuHandler (EventHandler<ActionEvent> handler) {
        loadGame.setOnAction(handler);
        saveGame.setOnAction(handler);
    }

    private class MenuHandler implements EventHandler<ActionEvent> {
        File file = null;
        FileChooser fileChooser = new FileChooser();

        public void handle(ActionEvent event) {
            MenuItem menuItem = (MenuItem)event.getSource();
            String buttonName = menuItem.getText();

            if (buttonName.equals("Load Game World")) {
                fileChooser.setTitle("Load");
                file = fileChooser.showOpenDialog(stage);
                fileName = file.getAbsolutePath();

                try{
                    loadMap(fileName);
                    for (Button b : buttons) {
                        b.setDisable(false);
                    }
                    dig.setDisable(false);
                    drop.setDisable(false);
                    dropIndex.setDisable(false);
                    choiceBox.setDisable(false);

                } finally { }
            } else {
                fileChooser.setTitle("Save");
                file = fileChooser.showOpenDialog(stage);
                fileName = file.getAbsolutePath();
                try {
                    worldMap.saveMap(fileName);
                } catch (IOException ioe) {
                    Alert error = new Alert(Alert.AlertType.INFORMATION);
                    error.setTitle("Error!");
                    error.setHeaderText(null);
                    error.setContentText("Map cannot be saved!");
                }

            }
        }
    }

    /**
     * Reference:
     * supplied code SparseTileArray class
     *
     * @param tilesToProcess
     * @param positionToTile
     * @param tileToPosition
     * @param position
     * @param tile
     */
    private void addTileForProcessing(Queue<Tile> tilesToProcess,
                                      Map<Position, Tile> positionToTile,
                                      Map<Tile, Position> tileToPosition,
                                      Position position, Tile tile) {
        positionToTile.put(position, tile);
        tileToPosition.put(tile, position);
        tilesToProcess.add(tile);
    }

    /**
     * Reference :
     * supplied code SparseTileArray class
     */
    public boolean checkTileValid(Map<Position, Tile> positionToTile,
                                  Map<Tile, Position> tileToPosition,
                                  Position position, Tile tile)
            throws WorldMapInconsistentException{
        if (tile == null) {
            return false;
        }

        Tile tileToTest = positionToTile.get(position);
        Position positionToTest = tileToPosition.get(tile);

        if (positionToTest != null && !(position.equals(positionToTest))) {
            throw new WorldMapInconsistentException("Tile that should be at "
                    + position + " is already assigned a different position at "
                    + positionToTest);
        }

        if (tileToTest != null && tile != tileToTest) {
            throw new WorldMapInconsistentException("Position " + position +
                    " is already occupied by a different tile.");
        }

        if (tileToTest == null) {
            assert positionToTest == null;
            return true;
        } else {
            return false;
        }

    }

    /**
     * Reference:
     *supplied coed SparseTileArray class
     */
    public void assignPositionToTiles(WorldMap w){
        tileMap = new HashMap<>();
        Builder b = w.getBuilder();
        Tile startingTile = b.getCurrentTile();
        Position startingPosition = w.getStartPosition();
        List<Tile> orderedTiles = new ArrayList<>();
        Queue<Tile> tilesToProcess = new ArrayDeque<>();
        tileCoordination = new HashMap<>();

        addTileForProcessing(tilesToProcess,tileMap,tileCoordination,
                startingPosition,startingTile);

        final String[] EXITS = {"north", "east", "south", "west"};
        final int[] DIRECTIONS_X = {0, 1, 0, -1};
        final int[] DIRECTIONS_Y = {-1, 0, 1, 0};

        while(tilesToProcess.size() > 0) {
            Tile removedTile = tilesToProcess.element();
            orderedTiles.add(removedTile);
            Position removedTilePosition = tileCoordination.get(removedTile);
            tilesToProcess.remove();

            for (int i = 0; i < EXITS.length; i++) {
                Tile tileInDirection = removedTile.getExits().get(EXITS[i]);
                Position positionInDirection =
                        new Position(removedTilePosition.getX() +
                                DIRECTIONS_X[i],
                                removedTilePosition.getY() +
                                        DIRECTIONS_Y[i]);
                try {
                    if (checkTileValid(tileMap, tileCoordination,
                            positionInDirection, tileInDirection)) {

                        addTileForProcessing(tilesToProcess, tileMap,
                                tileCoordination, positionInDirection,
                                tileInDirection);
                    }
                } catch (WorldMapInconsistentException wmie) {

                }
            }
        }
    }

    private void loadMap (String mapFile){
        try {
            worldMap = new WorldMap(mapFile);
            builderInventory = worldMap.getBuilder().getInventory();
            builder = worldMap.getBuilder();
            assignPositionToTiles(worldMap);
            mapMaker.drawTileMap(worldMap);
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Success");
            success.setHeaderText("Map is successfully loaded");
            success.setContentText(null);
            success.showAndWait();
        } catch (WorldMapInconsistentException | FileNotFoundException
                | WorldMapFormatException es) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error!");
            error.setHeaderText("Map cannot be loaded!");
            error.setContentText(null);
            error.showAndWait();
        }

    }


class MapMaker extends Canvas{
    //the context of the graphic
    private GraphicsContext gc;
    //the width of the graphic
    private double width;
    //the height of the graphic
    private double height;
    //the length of the tile
    private double length;

    private double xReference;
    private double yReference;
    /**
     * the size of the whole map
     * @param w is the horizontal parameter
     * @param h is the vertical parameter
     */
    public MapMaker (double w, double h) {
        super(w,h);
        this.height = h;
        this.width = w;
        this.length = 50;
        gc = getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.strokeRect(0,0,450,450);
    }

    /**
     * draw  each tile and the top block decide the color tof this tile
     *
     * @param x is the horizontal parameter
     * @param y is the vertical parameter
     */
    private void drawTiles (double x, double y, Paint blockColor ) {
        gc.setFill(blockColor);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(xReference+x*length,yReference+y*length,
                length,length);
        gc.fillRect(xReference+x*length,yReference+y*length,
                length,length);
    }

    private void drawNumber(int n,double x,double y) {
        gc.setFill(Color.WHITE);
        String s = Integer.toString(n);
        gc.fillText(s,xReference+x*length+25,yReference+y*length+25 );
    }

    /**
     * initial place of the player
     *
     */
    public void drawBuilder () {
        gc.setFill(Color.YELLOW);
        gc.fillOval(width/2,height/2, 10,10);
    }

    /**
     * draw the exits
     * @param direction exit direction
     * @param x  is the horizontal parameter
     * @param y is the vertical parameter
     */
    private void drawExits (String direction, double x, double y) {
        if (direction.equals("north")) {
            gc.setFill(Color.WHITE);
            gc.fillPolygon(new double[]{xReference + x*length+25,
                            xReference + x*length+30, xReference + x*length+20},
                    new double[]{yReference + y*length,
                            yReference + y*length+5,yReference + y*length+5},
                    3);
            gc.setStroke(Color.WHITE);
            gc.strokePolygon(new double[]{xReference + x*length+25,
                            xReference + x*length+30, xReference + x*length+20},
                    new double[]{yReference + y*length,
                            yReference + y*length+5,yReference + y*length+5},
                    3);
        } else if (direction.equals("east")) {
            gc.setFill(Color.WHITE);
            gc.fillPolygon(new double[]{xReference+x*length+50,
                            xReference+x*length+45, xReference+x*length+45},
                    new double[]{yReference+y*length+25,
                            yReference+y*length+30,
                            yReference+y*length+20},3);
            gc.setStroke(Color.WHITE);
            gc.strokePolygon(new double[]{xReference+x*length+25,
                            xReference+x*length+30,
                            xReference+x*length+20},
                    new double[]{yReference+y*length,
                            yReference+y*length+5,
                            yReference+y*length+5},3);
        } else if (direction.equals("south")) {
            gc.setFill(Color.WHITE);
            gc.fillPolygon(new double[]{xReference+x*length+25,
                            xReference+x*length+20, xReference+x*length+30},
                    new double[]{yReference+y*length+50,
                            yReference+y*length+45,
                            yReference+y*length+45},3);
            gc.setStroke(Color.WHITE);
            gc.strokePolygon(new double[]{xReference+x*length+25,
                            xReference+x*length+20, xReference+x*length+30},
                    new double[]{yReference+y*length+50,yReference+y*length+45,
                            yReference+y*length+45},3);
        } else if (direction.equals("west")) {
            gc.setFill(Color.WHITE);
            gc.fillPolygon(new double[]{xReference+x*length,
                            xReference+x*length+5,xReference+x*length+5},
                    new double[]{yReference+y*length+25,yReference+y*length+20,
                            yReference+y*length+30},3);
            gc.setStroke(Color.WHITE);
            gc.strokePolygon(new double[]{xReference+x*length,
                            xReference+x*length+5,xReference+x*length+5},
                    new double[]{yReference+y*length+25,yReference+y*length+20,
                            yReference+y*length+30},3);
        }

    }

    public String writeInventory(Builder b) {
        String result = "Builder Inventory: \n [";
        for (int i = 0; i < b.getInventory().size(); i++) {
            result += b.getInventory().get(i).getBlockType();
            result +=", ";
        }
        String removeFinalBlank = result.trim();
        removeFinalBlank += "]";
        String fixString = removeFinalBlank.replaceAll(",]",
                "]");
        return fixString;
    }

    /**
     *
     * @param w WorldMap
     */
    private void drawTileMap (WorldMap w) {
        List<Tile> tiles = w.getTiles();
        Builder b = w.getBuilder();
        Tile startingTile = b.getCurrentTile();
        Position startingPosition = tileCoordination.get(startingTile);

        xReference = width/2 -(startingPosition.getX()+0.3)*length;
        yReference = height/2 - (startingPosition.getY()+0.3)*length;

        gc.setStroke(Color.BLACK);
        gc.strokeRect(0,0,450,450);

        for (Tile tile : tiles) {
            Position position = tileCoordination.get(tile);
            Block topBlock =null;
            Paint p = null;

            try{
                topBlock = tile.getTopBlock();
            } catch (TooLowException tle) { }

            if (topBlock.getColour().equals("green")) {
                p = Color.GREEN;
            } else if (topBlock.getColour().equals("brown")) {
                p = Color.BROWN;
            } else if (topBlock.getColour().equals("black")) {
                p = Color.BLACK;
            } else if (topBlock.getColour().equals("gray")) {
                p = Color.GRAY;
            }
            drawTiles(position.getX(),position.getY(),p);

            int blockNumber = tile.getBlocks().size();
            drawNumber(blockNumber,position.getX(),position.getY());

            Map<String, Tile> exitOnTile = tile.getExits();
            for (Map.Entry<String, Tile> m : exitOnTile.entrySet()) {
                String direction = m.getKey();
                drawExits(direction,position.getX(),position.getY());
            }
        }
        drawBuilder();
        inventoryContent.setText(writeInventory(b));
    }

    public void update(){
        gc.clearRect(0,0,450,450);
        drawTileMap(worldMap);
    }

    }
}
