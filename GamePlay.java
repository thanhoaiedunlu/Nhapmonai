
public class GamePlay {
	int row;
	int col;
	private int value;

	public GamePlay() {
		row = -1;
		col = -1;
		value = 0;
	}// end Constructor

	// Trả về một đối tượng GamePlay mới sau khi
	// thực hiện một nước đi tại hàng và cột được chỉ định.
	
	
	public GamePlay moveDone(int row, int col) {
		GamePlay moveDone = new GamePlay();
		moveDone.row = row;
		moveDone.col = col;
		moveDone.value = -1;
		return moveDone;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	// Nước đi có thể di chuyển
	public GamePlay possibleMove(int row, int col, int value) {
		GamePlay posisibleMove = new GamePlay();
		posisibleMove.row = row;
		posisibleMove.col = col;
		posisibleMove.value = value;
		return posisibleMove;
	}

	// Di chuyển được sử dụng để so sánh trong thuật toán MinMax
	public GamePlay moveToCompare(int value) {
		GamePlay moveToCompare = new GamePlay();
		moveToCompare.row = -1;
		moveToCompare.col = -1;
		moveToCompare.value = value;
		return moveToCompare;
	}

	public int getValue() {
		return value;
	}

	public void setRow(int aRow) {
		row = aRow;
	}

	public void setCol(int aCol) {
		col = aCol;
	}

	public void setValue(int aValue) {
		value = aValue;
	}
}
