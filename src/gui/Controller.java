package gui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Board;
import model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * The controller class for the application.
 *
 * @author mikolajdeja
 * @version 2021.05.04
 */
public class Controller {
    @FXML
    private BorderPane borderPane;
    private GridPane gridPane;
    private Label[][] labels;

    private boolean[][] given;
    private Board board;
    private Player player;

    private static final int SIZE = 9;

    /**
     * Initialise the gui elements.
     */
    @FXML
    private void initialize() {
        int[][] testHard = new int[][]{
                {0, 0, 0, 5, 0, 0, 4, 2, 0},
                {6, 0, 0, 0, 4, 0, 0, 0, 0},
                {0, 5, 0, 0, 0, 0, 0, 0, 3},
                {0, 0, 0, 6, 8, 0, 1, 0, 0},
                {0, 0, 2, 0, 0, 0, 0, 0, 0},
                {5, 3, 0, 0, 1, 0, 7, 6, 0},
                {0, 0, 0, 0, 0, 0, 8, 7, 0},
                {7, 2, 0, 0, 0, 1, 0, 0, 9},
                {0, 9, 0, 0, 0, 3, 0, 5, 0}
        };
        board = new Board(testHard);
        player = new Player(board);
        initialiseGrid();
        Platform.runLater(() -> {
            Stage stage = ((Stage) borderPane.getScene().getWindow());
            ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> drawBoard();
            stage.widthProperty().addListener(stageSizeListener);
            stage.heightProperty().addListener(stageSizeListener);
            drawBoard();
        });
        setGiven();
    }

    /**
     * Initialise the grid.
     */
    private void initialiseGrid() {
        gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);
        gridPane.getStyleClass().add("grid");
        labels = new Label[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Label label = new Label();
                labels[i][j] = label;
                label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                gridPane.add(label, i, j);
                // add user inserting numbers here
            }
        }
        gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        borderPane.setCenter(gridPane);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if ((i + j) % 2 == 0)
                    colourSquare(i, j, "even");
                else
                    colourSquare(i, j, "odd");
            }
        }
    }

    /**
     * Mark the given numbers.
     */
    private void setGiven() {
        given = new boolean[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                given[i][j] = board.getNumber(i, j) != 0;
                if (given[i][j])
                    labels[i][j].getStyleClass().add("givenNum");
            }
        }
    }

    /**
     * Solve the sudoku.
     */
    @FXML
    private void solveSudoku() {
        player.solve();
        drawBoardSeq();
    }

    /**
     * Draw the board.
     */
    private void drawBoard() {
        double size = Math.min(gridPane.getWidth() / 9.5, gridPane.getHeight() / 9.5);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int num = board.getNumber(i, j);
                labels[i][j].setText(num != 0 ? String.valueOf(num) : "");
                labels[i][j].setPrefSize(size, size);
                labels[i][j].setAlignment(Pos.CENTER);
            }
        }
    }

    /**
     * Draw the board with random order and short random pauses.
     *
     * TODO fix flashing of the grid
     */
    private void drawBoardSeq() {
        new Thread(() -> {
            List<Integer> indices = new ArrayList<>();
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (!given[i][j])
                        indices.add(9 * i + j);
                }
            }
            Collections.shuffle(indices);
            Random random = new Random();
            double size = Math.min(gridPane.getWidth() / 9.5, gridPane.getHeight() / 9.5);
            for (Integer index : indices) {
                try {
                    Thread.sleep(random.nextInt(200));
                } catch (Exception e) {
                    System.out.println("The sleep was interrupted");
                }
                int i = index / SIZE, j = index % SIZE;
                Platform.runLater(() -> {
                    labels[i][j].setText(String.valueOf(board.getNumber(i, j)));
                    labels[i][j].setPrefSize(size, size);
                    labels[i][j].setAlignment(Pos.CENTER);
                });
            }
        }).start();
    }

    /**
     * Colour the small square appropriately.
     *
     * @param i The small square row index.
     * @param j The small square column index.
     * @param style The name of the style used.
     */
    private void colourSquare(int i, int j, String style) {
        for (int k = 0; k < 3; k++) {
            for (int l = 0; l < 3; l++) {
                labels[i * 3 + k][j * 3 + l].getStyleClass().add(style);
            }
        }
    }
}
