import java.util.LinkedList;
import java.util.Random;

public class AI {
	private int maxDepth;
	private int computerLetter;

	public AI(int thePlayerLetter) {
		maxDepth = 5;
		computerLetter = thePlayerLetter;
	}

	public GamePlay getNextMoveMiniMax(State board) {
		return miniMax(board, 0, true);	
	}

	public GamePlay getNextMoveAlphaBeta(State board) {
		return alphaBeta(board, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
	}

	private GamePlay miniMax(State board, int depth, boolean isMaximizingPlayer) {
		if (board.checkGameOver() || depth == maxDepth) {
			return new GamePlay().possibleMove(board.lastMove.row, board.lastMove.col, board.heuristic());
		}

		LinkedList<State> children = new LinkedList<>(board.getChildren(isMaximizingPlayer ? computerLetter : State.X));
		GamePlay bestMove = new GamePlay().moveToCompare(isMaximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE);

		for (State child : children) {
			GamePlay move = miniMax(child, depth + 1, !isMaximizingPlayer);

			if (isMaximizingPlayer) {
				if (move.getValue() > bestMove.getValue()) {
					bestMove.setRow(child.lastMove.row);
					bestMove.setCol(child.lastMove.col);
					bestMove.setValue(move.getValue());
				}
			} else {
				if (move.getValue() < bestMove.getValue()) {
					bestMove.setRow(child.lastMove.row);
					bestMove.setCol(child.lastMove.col);
					bestMove.setValue(move.getValue());
				}
			}
		}

		return bestMove;
	}

	private GamePlay alphaBeta(State board, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
		if (board.checkGameOver() || depth == maxDepth) {
			return new GamePlay().possibleMove(board.lastMove.row, board.lastMove.col, board.heuristic());
		}

		LinkedList<State> children = new LinkedList<>(board.getChildren(isMaximizingPlayer ? computerLetter : State.X));
		GamePlay bestMove = new GamePlay().moveToCompare(isMaximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE);

		for (State child : children) {
			GamePlay move = alphaBeta(child, depth + 1, alpha, beta, !isMaximizingPlayer);

			if (isMaximizingPlayer) {
				if (move.getValue() > bestMove.getValue()) {
					bestMove.setRow(child.lastMove.row);
					bestMove.setCol(child.lastMove.col);
					bestMove.setValue(move.getValue());
				}
				alpha = Math.max(alpha, bestMove.getValue());
				if (beta <= alpha) {
					break;
				}
			} else {
				if (move.getValue() < bestMove.getValue()) {
					bestMove.setRow(child.lastMove.row);
					bestMove.setCol(child.lastMove.col);
					bestMove.setValue(move.getValue());
				}
				beta = Math.min(beta, bestMove.getValue());
				if (beta <= alpha) {
					break;
				}
			}
		}

		return bestMove;
	}
}