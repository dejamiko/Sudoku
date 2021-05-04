package model;

import model.Board;

/**
 * A class responsible for the player.
 *
 * @author mikolajdeja
 * @version 2021.05.01
 */
public class Player {
    private final Board board;

    /**
     * A constructor for the player.
     *
     * @param board The board the player is solving.
     */
    public Player(Board board) {
        this.board = board;
    }

    /**
     * Solve the board with backtracking.
     *
     * @return True if the board is solved, false otherwise.
     */
    public boolean solve() {
        int row = -1, col = -1;
        boolean isFinished = true;
        for (int i = 0; i < 9 && isFinished; i++) {
            for (int j = 0; j < 9; j++) {
                if (board.getNumber(i, j) == 0) {
                    isFinished = false;
                    row = i;
                    col = j;
                    break;
                }
            }
        }

        if (isFinished)
            return true;

        for (int num = 1; num <= 9; ++num) {
            if (board.canInsert(row, col, num)) {
                board.insertNumber(row, col, num);
                if (solve())
                    return true;
                board.insertNumber(row, col, 0);
            }
        }
        return false;
    }
}
