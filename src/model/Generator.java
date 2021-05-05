package model;

import java.util.Random;

/**
 * A simple random generator for sudoku. It doesn't care
 * if the puzzle is solvable by humans.
 *
 * @author mikolajdeja
 * @version 2021.05.05
 */
public class Generator {
    /**
     * Generate a board with a given number of given numbers.
     * The board has to be solvable.
     *
     * @param n The number of given numbers.
     * @return The generated board.
     */
    public static Board generateBoard(int n) {
        while (true) {
            int numberOfGiven = n;
            Board board = new Board();
            Random random = new Random();
            int max = 81, size = 9;
            while (numberOfGiven > 0) {
                int r = random.nextInt(max);
                int num = random.nextInt(size);
                if (board.canInsert(r / 9, r % 9, num)) {
                    board.insertNumber(r / 9, r % 9, num);
                    numberOfGiven--;
                }
            }
            Board copy = new Board(board);
            Player player = new Player(copy);
            if (player.solve())
                return board;
        }
    }
}
