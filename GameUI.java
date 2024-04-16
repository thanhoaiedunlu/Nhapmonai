import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class GameUI extends JFrame {
	private State gameBoard;
	private JButton[][] buttons;
	static AI computerPlayer;

	public GameUI() {
		gameBoard = new State();
		buttons = new JButton[6][7];
		computerPlayer = new AI(State.O);

		setTitle("Connect 4");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(6, 7));

		initializeButtons();
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void initializeButtons() {
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 7; col++) {
				JButton button = new JButton();
				button.setPreferredSize(new Dimension(80, 80));
				button.setFont(new Font("Arial", Font.BOLD, 40));
				button.addActionListener(new ButtonListener(row, col));
				buttons[row][col] = button;
				add(button);
			}
		}
	}

	private void updateButton(int row, int col, int letter) {
		JButton button = buttons[row][col];
		if (letter == State.X) {
			button.setText("X");
			button.setEnabled(false);
		} else if (letter == State.O) {
			button.setText("O");
			button.setEnabled(false);
		}
	}

	private class ButtonListener implements ActionListener {
		private int row;
		private int col;

		public ButtonListener(int row, int col) {
			this.row = row;
			this.col = col;
		}

		public void actionPerformed(ActionEvent e) {
			if (gameBoard.isValidMove(col)) {
				gameBoard.makeMove(col, State.X);
				updateButton(gameBoard.getRowPosition(col) + 1, col, State.X);
				if (!gameBoard.checkGameOver()) {
					GamePlay computerMove = computerPlayer.getNextMoveAlphaBeta(gameBoard);
					gameBoard.makeMove(computerMove.getCol(), State.O);
					updateButton(gameBoard.getRowPosition(computerMove.getCol()) + 1, computerMove.getCol(), State.O);
				}
				if (gameBoard.checkGameOver()) {
				    String message;
				    if (gameBoard.winner == State.X) {
				        message = "Người chơi đã thắng!";
				    } else if (gameBoard.winner == State.O) {
				        message = "Máy tính đã thắng!";
				    } else {
				        message = "Hòa!";
				    }
				    JOptionPane.showMessageDialog(null, message, "Kết quả", JOptionPane.INFORMATION_MESSAGE);
				    System.exit(0);
				}
			}
		}
	}

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new GameUI();
			}
		});
	}
}
