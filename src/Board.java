import java.util.*;

/**
 * The class responsible for keeping
 * information about the board.
 *
 * @author mikolajdeja
 * @version 2021.05.01
 */
public class Board {
    private final int[][] board;
    private static final int size = 9;

    /**
     * A constructor for the board of a given size.
     */
    public Board() {
        board = new int[size][size];
    }

    /**
     * A constructor for the board with a given position.
     *
     * @param pos The starting position.
     */
    public Board(int[][] pos) {
        board = pos;
    }

    /**
     * Insert a given number in a given place.
     *
     * @param row The given row.
     * @param col The given column.
     * @param num The given number.
     */
    public void insertNumber(int row, int col, int num) {
        board[row][col] = num;
    }

    /**
     * Get the number in a given place.
     *
     * @param row The given row.
     * @param col The given column.
     * @return The number in the given place.
     */
    public int getNumber(int row, int col) {
        return board[row][col];
    }

    /**
     * Check whether the game is finished (the board is full)
     *
     * @return True if the game is finished (the board is full)
     */
    public boolean isFinished() {
        for (int[] row : board) {
            for (int i : row) {
                if (i == 0)
                    return false;
            }
        }
        return true;
    }

    public boolean isErroneous() {
        Set<Integer> set;

        // check rows
        for (int[] row : board) {
            set = new HashSet<>();
            for (int i : row) {
                if (i != 0 && set.contains(i)) {
                    return true;
                }
                set.add(i);
            }
        }
        // check columns
        for (int i = 0; i < board[0].length; i++) {
            set = new HashSet<>();
            for (int[] row : board) {
                if (i != 0 && set.contains(row[i])) {
                    return true;
                }
                set.add(row[i]);
            }
        }
        // check all small squares
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (isSmallSquareErroneous(i, j))
                    return true;
            }
        }

        return false;
    }

    /**
     * Check if the small square starting at a given row and column is erroneous.
     *
     * @param i The starting row.
     * @param j THe starting column.
     * @return True if the small square is erroneous, false otherwise.
     */
    private boolean isSmallSquareErroneous(int i, int j) {
        Set<Integer> set = new HashSet<>();
        for (int k = 0; k < 3; k++) {
            for (int l = 0; l < 3; l++) {
                if (i != 0 && set.contains(board[i + k][j + l])) {
                    return true;
                }
                set.add(board[i + k][j + l]);
            }
        }

        return false;
    }

    /**
     * Get missing numbers from a given row.
     *
     * @param i The row number.
     * @return A set of missing numbers.
     */
    public Set<Integer> getMissingNumbersRow(int i) {
        Set<Integer> set = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        for (int j = 0; j < size; j++) {
            set.remove(board[i][j]);
        }
        return set;
    }

    /**
     * Get missing numbers from a given column.
     *
     * @param j The column number.
     * @return A set of missing numbers.
     */
    public Set<Integer> getMissingNumbersCol(int j) {
        Set<Integer> set = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        for (int i = 0; i < size; i++) {
            set.remove(board[i][j]);
        }
        return set;
    }

    /**
     * Get missing numbers from a small square starting at a given row and column.
     *
     * @param i The row number.
     * @param j The column number.
     * @return A set of missing numbers.
     */
    public Set<Integer> getMissingNumbersSquare(int i, int j) {
        Set<Integer> set = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        for (int k = 0; k < 3; k++) {
            for (int l = 0; l < 3; l++) {
                set.remove(board[i + k][j + l]);
            }
        }
        return set;
    }

    /**
     * Get the string representation of the board.
     *
     * @return The string representation of the board.
     */
    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                ans.append(board[i][j]).append(" ");
            }
            ans.append("\n");
        }
        return ans.toString();
    }
}
