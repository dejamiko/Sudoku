package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * The main class of the application.
 *
 * @author mikolajdeja
 * @version 2021.05.04
 */
public class Main extends Application {

    /**
     * Start the gui.
     *
     * @param primaryStage The primary stage in use.
     * @throws Exception Exceptions thrown by the FXMLLoader.
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("sudoku.fxml")));
        primaryStage.setTitle("Sudoku solver");
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();
    }

    /**
     * The main method.
     *
     * @param args The arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
