import java.util.LinkedList;

public class State {

	static final int X = 1; // User
	static final int O = -1; // Computer
	int EMPTY = 0;
	GamePlay lastMove;
	int lastLetterPlayed;
	int winner;
	int[][] gameBoard;
	String winningMethod;

	public State() {
		lastMove = new GamePlay();
		lastLetterPlayed = O; // The user starts first
		winner = 0;
		gameBoard = new int[6][7];
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				gameBoard[i][j] = EMPTY;
			}
		}
	}

	public void setWinner(int winner) {
		this.winner = winner;
	}

	public void setWinnerMethod(String winningMethod) {
		this.winningMethod = winningMethod;
	}

	// Thực hiện một nước đi bằng cách cập nhật trạng thái của bảng
	// theo cột và ký tự người chơi được chỉ định.
	public void makeMove(int col, int letter) {
		lastMove = lastMove.moveDone(getRowPosition(col), col);
		gameBoard[getRowPosition(col)][col] = letter;
		lastLetterPlayed = letter;
	}

	// Kiểm tra xem một nước đi có hợp lệ hay không dựa trên cột được chỉ định.
	public boolean isValidMove(int col) {
		int row = getRowPosition(col);
		if ((row == -1) || (col == -1) || (row > 5) || (col > 6)) {
			return false;
		}
		if (gameBoard[row][col] != EMPTY) {
			return false;
		}
		return true;
	}

	// Kiểm tra xem có thể thực hiện một nước đi tại hàng và cột được chỉ định hay
	// không.
	public boolean canMove(int row, int col) {
		if ((row <= -1) || (col <= -1) || (row > 5) || (col > 6)) {
			return false;
		}
		return true;
	}

	// Kiểm tra xem một cột đã đầy ô trống chưa.
	public boolean checkFullColumn(int col) {
		if (gameBoard[0][col] == EMPTY)
			return false;
		else {
			System.out.println("The column " + (col + 1) + " is full. Select another column.");
			return true;
		}
	}

	// Trả về hàng thấp nhất mà một cột còn ô trống.
	public int getRowPosition(int col) {
	    for (int row = 5; row >= 0; row--) {
	        if (gameBoard[row][col] == EMPTY) {
	            return row;
	        }
	    }
	    return -1;
	}

	

	// Tạo một bản sao mở rộng của trạng thái hiện tại của bảng.
	public State boardWithExpansion(State board) {
		State expansion = new State();
		expansion.lastMove = board.lastMove;
		expansion.lastLetterPlayed = board.lastLetterPlayed;
		expansion.winner = board.winner;
		expansion.gameBoard = new int[6][7];
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				expansion.gameBoard[i][j] = board.gameBoard[i][j];
			}
		}
		return expansion;
	}// kết thúc boardWithExpansion

	// Trả về danh sách các trạng thái con của bảng dựa trên ký tự người chơi được
	// chỉ định.
	public LinkedList<State> getChildren(int letter) {
		LinkedList<State> children = new LinkedList<State>();
		for (int col = 0; col < 7; col++) {
			if (isValidMove(col)) {
				State child = boardWithExpansion(this);
				child.makeMove(col, letter);
				children.add(child);
			}
		}
		return children;
	}// kết thúc getChildren
	public int heuristic() {
		int Xlines = 0;
		int Olines = 0;
		if (checkWinState()) {
			if (winner == X) {
				Xlines = Xlines + 90;
			} else {
				Olines = Olines + 90;
			}
		}
		Xlines = Xlines + check3In(X) * 10 + check2In(X) * 4;
		Olines = Olines + check3In(O) * 10 + check2In(O) * 4;
		return Olines - Xlines;
	}
	// Kiểm tra xem có trạng thái chiến thắng nào hay không.
	public boolean checkWinState() {
		// Trường hợp 4 theo hàng
		for (int i = 5; i >= 0; i--) {
			for (int j = 0; j < 4; j++) {
				if (gameBoard[i][j] == gameBoard[i][j + 1] && gameBoard[i][j] == gameBoard[i][j + 2]
						&& gameBoard[i][j] == gameBoard[i][j + 3] && gameBoard[i][j] != EMPTY) {
					setWinner(gameBoard[i][j]);
					setWinnerMethod("Winner by row.");
					return true;
				}
			}
		}

		// Trường hợp 4 theo cột
		for (int i = 5; i >= 3; i--) {
			for (int j = 0; j < 7; j++) {
				if (gameBoard[i][j] == gameBoard[i - 1][j] && gameBoard[i][j] == gameBoard[i - 2][j]
						&& gameBoard[i][j] == gameBoard[i - 3][j] && gameBoard[i][j] != EMPTY) {
					setWinner(gameBoard[i][j]);
					setWinnerMethod("Winner by column.");
					return true;
				}
			}
		}

		// Trường hợp 4 theo đường chéo trái qua phải, từ trên xuống
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				if (gameBoard[i][j] == gameBoard[i + 1][j + 1] && gameBoard[i][j] == gameBoard[i + 2][j + 2]
						&& gameBoard[i][j] == gameBoard[i + 3][j + 3] && gameBoard[i][j] != EMPTY) {
					setWinner(gameBoard[i][j]);
					setWinnerMethod("Winner by diagonal.");
					return true;
				}
			}
		}

		// Trường hợp 4 theo đường chéo trái qua phải, từ dưới lên
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				if (canMove(i - 3, j + 3)) {
					if (gameBoard[i][j] == gameBoard[i - 1][j + 1] && gameBoard[i][j] == gameBoard[i - 2][j + 2]
							&& gameBoard[i][j] == gameBoard[i - 3][j + 3] && gameBoard[i][j] != EMPTY) {
						setWinner(gameBoard[i][j]);
						setWinnerMethod("Winner by diagonal.");
						return true;
					}
				}
			}
		}
		setWinner(0);
		return false;
	}
	// Checks if there are 3 pieces of a same player
		public int check3In(int player) {
			int times = 0;
			// Theo dòng
			for (int i = 5; i >= 0; i--) {
				for (int j = 0; j < 7; j++) {
					if (canMove(i, j + 2)) {
						if (gameBoard[i][j] == gameBoard[i][j + 1] && gameBoard[i][j] == gameBoard[i][j + 2]
								&& gameBoard[i][j] == player) {
							times++;
						}
					}
				}
			}

			// Theo cột
			for (int i = 5; i >= 0; i--) {
				for (int j = 0; j < 7; j++) {
					if (canMove(i - 2, j)) {
						if (gameBoard[i][j] == gameBoard[i - 1][j] && gameBoard[i][j] == gameBoard[i - 2][j]
								&& gameBoard[i][j] == player) {
							times++;
						}
					}
				}
			}

			// Theo đường chéo, từ trên xuống
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 7; j++) {
					if (canMove(i + 2, j + 2)) {
						if (gameBoard[i][j] == gameBoard[i + 1][j + 1] && gameBoard[i][j] == gameBoard[i + 2][j + 2]
								&& gameBoard[i][j] == player) {
							times++;
						}
					}
				}
			}

			// Theo đường chéo, từ dưới lên
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 7; j++) {
					if (canMove(i - 2, j + 2)) {
						if (gameBoard[i][j] == gameBoard[i - 1][j + 1] && gameBoard[i][j] == gameBoard[i - 2][j + 2]
								&& gameBoard[i][j] == player) {
							times++;
						}
					}
				}
			}
			return times;
		}

		// Checks if there are 2 pieces of a same player
		public int check2In(int player) {
			int times = 0;
			// Theo dòng
			for (int i = 5; i >= 0; i--) {
				for (int j = 0; j < 7; j++) {
					if (canMove(i, j + 1)) {
						if (gameBoard[i][j] == gameBoard[i][j + 1] && gameBoard[i][j] == player) {
							times++;
						}
					}
				}
			}

			// Theo cột
			for (int i = 5; i >= 0; i--) {
				for (int j = 0; j < 7; j++) {
					if (canMove(i - 1, j)) {
						if (gameBoard[i][j] == gameBoard[i - 1][j] && gameBoard[i][j] == player) {
							times++;
						}
					}
				}
			}

			// Theo đường chéo, từ trên xuống
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 7; j++) {
					if (canMove(i + 1, j + 1)) {
						if (gameBoard[i][j] == gameBoard[i + 1][j + 1] && gameBoard[i][j] == player) {
							times++;
						}
					}
				}
			}

			// Theo đường chéo, từ dưới lên
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 7; j++) {
					if (canMove(i - 1, j + 1)) {
						if (gameBoard[i][j] == gameBoard[i - 1][j + 1] && gameBoard[i][j] == player) {
							times++;
						}
					}
				}
			}
			return times;
		}


	public boolean checkGameOver() {
		if (checkWinState()) {
			return true;
		}
		// Kiểm tra còn ô trống trong bảng không
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 7; col++) {
				if (gameBoard[row][col] == EMPTY) {
					return false;
				}
			}
		}
		return true;
	}

	public void printBoard() {
		System.out.println("  1   2   3   4   5   6   7 ");
		System.out.println("+---+---+---+---+---+---+---+");

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				System.out.print("| ");

				if (gameBoard[i][j] == 1) {
					System.out.print("X");
				} else if (gameBoard[i][j] == -1) {
					System.out.print("O");
				} else {
					System.out.print(" ");
				}

				System.out.print(" ");
			}

			System.out.println("|");
			System.out.println("+---+---+---+---+---+---+---+");
		}
	}
}
