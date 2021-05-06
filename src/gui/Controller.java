package gui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
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
    private Button easyButton;
    @FXML
    private Button mediumButton;
    @FXML
    private Button hardButton;
    @FXML
    private Button oneDigitButton;
    @FXML
    private Button buttonOne;
    @FXML
    private Button buttonTwo;
    @FXML
    private Button buttonThree;
    @FXML
    private Button buttonFour;
    @FXML
    private Button buttonFive;
    @FXML
    private Button buttonSix;
    @FXML
    private Button buttonSeven;
    @FXML
    private Button buttonEight;
    @FXML
    private Button buttonNine;

    private GridPane gridPane;
    private Label[][] labels;

    private boolean[][] given;
    private Board board;
    private int selectedRow;
    private int selectedCol;

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
        // a convention that -1 means 'none selected'
        selectedRow = -1;
        selectedCol = -1;
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
                label.setOnMouseClicked(this::clickGrid);
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
     * Get the coordinates of the label.
     *
     * @param label The label.
     * @return The coordinates of the source.
     */
    private int[] getSource(Label label) {
        int row = -1, col = -1;
        for (int i = 0; i < labels.length && row < 0; i++)
            for (int j = 0; j < labels[i].length; j++)
                if (labels[i][j].equals(label)) {
                    row = i;
                    col = j;
                    break;
                }
        return new int[]{row, col};
    }

    /**
     * Let the user input a number to a clicked place in the grid.
     *
     * @param event The event of clicking the place in the grid.
     */
    private void clickGrid(MouseEvent event) {
        Label label = (Label) event.getSource();
        int[] coords = getSource(label);
        if (!given[coords[0]][coords[1]]) {
            // deselect the previous selected cell
            if (selectedRow != -1) {
                labels[selectedRow][selectedCol].getStyleClass().removeIf(e -> e.equals("selected"));
            }
            // if the same cell is clicked, leave it deselected
            if (!(selectedRow == coords[0] && selectedCol == coords[1])) {
                label.getStyleClass().add("selected");
                selectedRow = coords[0];
                selectedCol = coords[1];
            } else {
                // mark as 'none selected'
                selectedRow = -1;
                selectedCol = -1;
            }
        }
    }

    /**
     * Manage clicks on the number buttons.
     *
     * @param event The event of clicking on the number button.
     */
    @FXML
    private void clickNum(MouseEvent event) {
        // if a cell is selected
        if (selectedCol != -1) {
            Button button = (Button) event.getSource();
            int num = Integer.parseInt(button.getText());
            if (board.getNumber(selectedRow, selectedCol) != num) {
                board.insertNumber(selectedRow, selectedCol, 0);
                if (board.canInsert(selectedRow, selectedCol, num)) {
                    labels[selectedRow][selectedCol].getStyleClass().removeIf(e -> e.equals("incorrect"));
                } else {
                    labels[selectedRow][selectedCol].getStyleClass().add("incorrect");
                }
                board.insertNumber(selectedRow, selectedCol, num);
            } else {
                board.insertNumber(selectedRow, selectedCol, 0);
                labels[selectedRow][selectedCol].getStyleClass().removeIf(e -> e.equals("incorrect"));
            }
            drawBoard();
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
        if (player.solve()) {
            drawBoardSeq();
        }
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
        easyButton.setDisable(disability);
        mediumButton.setDisable(disability);
        hardButton.setDisable(disability);
        oneDigitButton.setDisable(disability);
        buttonOne.setDisable(disability);
        buttonTwo.setDisable(disability);
        buttonThree.setDisable(disability);
        buttonFour.setDisable(disability);
        buttonFive.setDisable(disability);
        buttonSix.setDisable(disability);
        buttonSeven.setDisable(disability);
        buttonEight.setDisable(disability);
        buttonNine.setDisable(disability);
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
                labels[i][j].getStyleClass().removeIf(e -> e.equals("incorrect"));
            }
        }
    }

    /**
     * Generate a new board.
     */
    @FXML
    private void newBoard(MouseEvent event) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Easy", 40);
        map.put("Medium", 30);
        map.put("Hard", 20);
        map.put("This has to be a joke", 1);

        if (selectedRow != -1)
            labels[selectedRow][selectedCol].getStyleClass().removeIf(e -> e.equals("selected"));
        selectedRow = -1;
        selectedCol = -1;

        new Thread(() -> {
            solveButton.setDisable(true);
            setDisabilityOfButtons(true);
            board = Generator.generateBoard(map.get(((Button) event.getSource()).getText()));
            Platform.runLater(() -> {
                setDisabilityOfButtons(false);
                setGiven();
                clearBoard();
                drawBoard();
                solveButton.setDisable(false);
            });
        }).start();
    }
}
