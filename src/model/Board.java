package model;

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
     * Check if the player can insert a given number in a given place.
     *
     * @param row The given row.
     * @param col The given column.
     * @param num The given number.
     * @return True if the player can insert a number there, false otherwise.
     */
    public boolean canInsert(int row, int col, int num) {
        // check row
        for (int i = 0; i < board[row].length; i++) {
            if (board[i][col] == num) {
                return false;
            }
        }
        // check column
        for (int j = 0; j < board[row].length; j++) {
            if (board[row][j] == num)
                return false;
        }
        // check small square
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[(row / 3) * 3 + i][(col / 3) * 3 + j] == num)
                    return false;
            }
        }
        return true;
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
