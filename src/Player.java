import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
     * Solve the board.
     *
     * @return True if the board is solved, false otherwise.
     */
    public boolean solve() {
        if (board.isFinished())
            return true;

        List<int[]> moves = new ArrayList<>();
        boolean improved = true;
        while (improved) {
            improved = false;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (board.getNumber(i, j) == 0) {
                        Set<Integer> set = board.getMissingNumbersRow(i);
                        set.retainAll(board.getMissingNumbersCol(j));
                        set.retainAll(board.getMissingNumbersSquare((i / 3) * 3, (j / 3) * 3));
                        if (set.size() == 1) {
                            for (Integer num : set) {
                                board.insertNumber(i, j, num);
                                moves.add(new int[]{i, j});
                                improved = true;
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board.getNumber(i, j) == 0) {
                    Set<Integer> set = board.getMissingNumbersRow(i);
                    set.retainAll(board.getMissingNumbersCol(j));
                    set.retainAll(board.getMissingNumbersSquare((i / 3) * 3, (j / 3) * 3));
                    if (set.size() == 0) {
                        for (int[] move : moves)
                            board.insertNumber(move[0], move[1], 0);
                        return false;
                    }
                    for (Integer num : set) {
                        board.insertNumber(i, j, num);
                        if (solve())
                            return true;
                        board.insertNumber(i, j, 0);
                    }
                }
            }
        }

        for (int[] move : moves)
            board.insertNumber(move[0], move[1], 0);

        return false;
    }
}
