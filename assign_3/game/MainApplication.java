package game;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


public class MainApplication extends javafx.application.Application{
    private Stage stage;


    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("Block World");
        View view = new View(stage);

        stage.setScene(view.getScene());
        stage.show();
    }

}
