package gui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Board;
import model.Generator;
import model.Player;

import java.util.*;

/**
 * The controller class for the application.
 *
 * @author mikolajdeja
 * @version 2021.05.04
 */
public class Controller {
    @FXML
    private BorderPane borderPane;
    @FXML
    private Button solveButton;
    @FXML
    private Button resetButton;
    @FXML
    private Button newBoardButton;

    private GridPane gridPane;
    private Label[][] labels;

    private boolean[][] given;
    private Board board;

    private static final int SIZE = 9;

    /**
     * Initialise the gui elements.
     */
    @FXML
    private void initialize() {
        board = Generator.generateBoard(20);
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
            }
        }
    }

    /**
     * Solve the sudoku.
     */
    @FXML
    private void solveSudoku() {
        solveButton.setDisable(true);
        Player player = new Player(board);
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
                if (given[i][j])
                    labels[i][j].getStyleClass().add("givenNum");
                else
                    labels[i][j].getStyleClass().removeIf(e -> e.equals("givenNum"));
            }
        }
    }

    /**
     * Draw the board with random order and short random pauses.
     * <p>
     * TODO fix flashing of the grid
     */
    private void drawBoardSeq() {
        new Thread(() -> {
            setDisabilityOfButtons(true);
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
                    Thread.sleep(random.nextInt(100));
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
            Platform.runLater(() -> setDisabilityOfButtons(false));
        }).start();
    }

    /**
     * Set the disability setting of reset and newBoardButton.
     *
     * @param disability The new disability setting.
     */
    private void setDisabilityOfButtons(boolean disability) {
        resetButton.setDisable(disability);
        newBoardButton.setDisable(disability);
    }

    /**
     * Colour the small square appropriately.
     *
     * @param i     The small square row index.
     * @param j     The small square column index.
     * @param style The name of the style used.
     */
    private void colourSquare(int i, int j, String style) {
        for (int k = 0; k < 3; k++) {
            for (int l = 0; l < 3; l++) {
                labels[i * 3 + k][j * 3 + l].getStyleClass().add(style);
            }
        }
    }

    /**
     * Reset the board to a starting state.
     */
    @FXML
    private void reset() {
        clearBoard();
        drawBoard();
        solveButton.setDisable(false);
    }

    /**
     * Put 0s in every place that is not a given number.
     */
    private void clearBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (!given[i][j])
                    board.insertNumber(i, j, 0);
            }
        }
    }

    /**
     * Generate a new board.
     */
    @FXML
    private void newBoard() {
        List<String> choices = new ArrayList<>();

        HashMap<String, Integer> map = new HashMap<>();
        map.put("Easy", 40);
        map.put("Medium", 30);
        map.put("Hard", 20);
        map.put("Hmm, that doesn't seem possible", 1);

        choices.add("Easy");
        choices.add("Medium");
        choices.add("Hard");
        choices.add("Hmm, that doesn't seem possible");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("Medium", choices);
        dialog.setTitle("New board generator");
        dialog.setContentText("Choose the difficulty of the new puzzle:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(s -> new Thread(() -> {
            setDisabilityOfButtons(true);
            board = Generator.generateBoard(map.get(s));
            Platform.runLater(() -> {
                setDisabilityOfButtons(false);
                setGiven();
                clearBoard();
                drawBoard();
                solveButton.setDisable(false);
            });
        }).start());
    }
}
