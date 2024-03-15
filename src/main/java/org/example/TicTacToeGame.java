package org.example;

/**
 * The class <b>TicTacToeGame</b> is the class that implements the Tic Tac Toe
 * Game. It contains the grid and tracks its progress. It automatically maintain
 * the current state of the game as players are making moves.
 *
 * @author Guy-Vincent Jourdan, University of Ottawa
 */
public class TicTacToeGame {

	/**
	 * The board of the game, stored as a single array.
	 */
	private CellValue[] board;

	/**
	 * level records the number of rounds that have been played so far. Starts at 0.
	 */
	private int level;

	/**
	 * gameState records the current state of the game.
	 */
	private GameState gameState;

	/**
	 * lines is the number of lines in the grid
	 */
	private final int lines;

	/**
	 * columns is the number of columns in the grid
	 */
	private final int columns;

	/**
	 * sizeWin is the number of cell of the same type that must be aligned to win
	 * the game. For simplicity, it will be always 3 in this assignment.
	 */
	private final int sizeWin;

	/**
	 * default constructor, for a game of 3x3, which must align 3 cells
	 */
	public TicTacToeGame() {
		this(3, 3, 3);
	}

	/**
	 * constructor allowing to specify the number of lines and the number of columns
	 * for the game. 3 cells must be aligned.
	 * 
	 * @param lines   the number of lines in the game
	 * @param columns the number of columns in the game
	 */
	public TicTacToeGame(int lines, int columns) {
		this(lines, columns, 3);
	}

	/**
	 * constructor allowing to specify the number of lines and the number of columns
	 * for the game, as well as the number of cells that must be aligned to win.
	 * 
	 * @param lines   the number of lines in the game
	 * @param columns the number of columns in the game
	 * @param sizeWin the number of cells that must be aligned to win.
	 */
	public TicTacToeGame(int lines, int columns, int sizeWin) {
		board = new CellValue[lines * columns];
		this.sizeWin = sizeWin;
		this.lines = lines;
		this.columns = columns;
		level = 0;
		gameState = GameState.PLAYING;

		for (int i = 0; i < board.length; i++)
			board[i] = CellValue.EMPTY;
	}

	/**
	 * getter for the variable lines
	 * 
	 * @return the value of lines
	 */
	public int getLines() {
		return lines;
	}

	/**
	 * getter for the variable columns
	 * 
	 * @return the value of columns
	 */
	public int getColumns() {
		return columns;
	}

	/**
	 * getter for the variable level
	 * 
	 * @return the value of level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * getter for the variable gameState
	 * 
	 * @return the value of gameState
	 */
	public GameState getGameState() {
		return gameState;
	}

	/**
	 * getter for the variable sizeWin
	 * 
	 * @return the value of sizeWin
	 */
	public int getSizeWin() {
		return sizeWin;
	}

	/**
	 * returns the cellValue that is expected next, in other word, which played (X
	 * or O) should play next. This method does not modify the state of the game.
	 * 
	 * @return the value of the enum CellValue corresponding to the next expected
	 *         value.
	 */
	public CellValue nextCellValue() {
		return level % 2 == 1 ? CellValue.O : CellValue.X;
	}

	/**
	 * returns the value of the cell at index i. If the index is invalid, an error
	 * message is printed out. The behaviour is then unspecified
	 * 
	 * @param i the index of the cell in the array board
	 * @return the value at index i in the variable board.
	 */
	public CellValue valueAt(int i) {
		if (i < 0 || i >= board.length) {
			System.out.println("Index out of range");
		}
		return board[i];
	}

	/**
	 * This method is called by the next player to play at the cell at index i. If
	 * the index is invalid, an error message is printed out. The behaviour is then
	 * unspecified If the chosen cell is not empty, an error message is printed out.
	 * The behaviour is then unspecified If the move is valide, the board is
	 * updated, as well as the state of the game. To faciliate testing, it is
	 * acceptable to keep playing after a game is already won. If that is the case,
	 * the a message should be printed out and the move recorded. the winner of the
	 * game is the player who won first
	 * 
	 * @param i the index of the cell in the array board that has been selected by
	 *          the next player
	 */
	public void play(int i) {

		// check the index range
		if (i < 0 || i >= this.board.length)
			System.out.println("The value should be between " + 1 + " and " + board.length);
		else {
			// check for illegal values
			if (this.board[i] != CellValue.EMPTY)
				System.out.println("This cell has already been played");
			else {
				this.board[i] = nextCellValue();
				level += 1;
				if (gameState == GameState.PLAYING)
					setGameState(i);
			}
		}

	}

	/**
	 * A helper method which updates the gameState variable correctly after the cell
	 * at index i was just set. The method assumes that prior to setting the cell at
	 * index i, the gameState variable was correctly set. it also assumes that it is
	 * only called if the game was not already finished when the cell at index i was
	 * played (the the game was playing). Therefore, it only needs to check if
	 * playing at index i has concluded the game So check if 3 cells are formed to
	 * win. // * @param i the index of the cell in the array board that has just
	 * been set
	 */

	private void setGameState(int index) {

		// get the row and cell related to this index
		int row = index / columns;
		int col = index % columns;

		// check row
		checkBoard(row, 0, columns - 1, 0, 1);

		// check line wins
		checkBoard(0, col, lines - 1, 1, 0);

		// check diagonal

		// ==== main diagonal
		// suppose the diagonal is line col = row + minV
		int minV = col - row;

		// the intersection with col = 0 is (-minV , 0)
		if (minV < 0)
			checkBoard(-minV, 0, Math.max(lines, columns), 1, 1);
		else
			checkBoard(0, minV, Math.max(lines, columns), 1, 1);

		// ==== reverse diagonal
		// suppose the reverse diagonal is line col = -row + minV
		minV = col + row;

		// the intersection with col = 0 is (minV , 0)
		if (minV >= lines)
			checkBoard(minV, 0, Math.max(lines, columns), -1, 1);
		else
			checkBoard(lines - 1, minV - lines + 1, Math.max(lines, columns), -1, 1);

		// check if game ended in draw
		if (gameState == GameState.PLAYING && level == lines * columns)
			gameState = GameState.DRAW;
	}

	/**
	 * checks the board for consecutive blocks
	 * 
	 * @param row       the start row address
	 * @param col       the start column address
	 * @param stepCount number of steps to jump
	 * @param dr        jump value and direction in row index
	 * @param dc        jump value and direction in column index
	 */
	private void checkBoard(int row, int col, int stepCount, int dr, int dc) {
		// count number of consecutive similar cells
		int consecutiveX = 0;
		int consecutiveO = 0;
		for (int i = 0; i <= stepCount; i++) {
			// check if the step is valid
			if ((row + i * dr) < 0 || (row + i * dr) >= lines || (col + i * dc) < 0 || (col + i * dc) >= columns)
				continue;
			int x = (row + i * dr) * columns + (col + i * dc);
			if (x < 0 || x >= board.length)
				continue;
			switch (board[x]) {
			case EMPTY:

				consecutiveO = 0;
				consecutiveX = 0;
				break;
			case X:
				consecutiveO = 0;
				consecutiveX += 1;
				break;

			case O:
				consecutiveX = 0;
				consecutiveO += 1;
				break;
			}

			if (consecutiveX >= sizeWin || consecutiveO >= sizeWin)
				break;
		}
		if (consecutiveO >= sizeWin) {
			gameState = GameState.OWIN;
			return;
		}
		if (consecutiveX >= sizeWin) {
			gameState = GameState.XWIN;
			return;
		}
	}

	final String NEW_LINE = System.getProperty("line.separator");
	// returns the OS dependent line separator

	/**
	 * Returns a String representation of the game matching the example provided in
	 * the assignment's description
	 *
	 * @return String representation of the game
	 */

	public String toString() {
		// your code here
		// use NEW_LINE defined above rather than \n
		String res = "";
		for (int i = 0; i < lines; i++) {
			for (int j = 0; j < columns; j++) {
				switch (board[i * columns + j]) {
				case EMPTY:
					res += "   ";
					break;
				case X:
					res += " X ";
					break;
				case O:
					res += " O ";
					break;

				}
				if (j < columns - 1)
					res += "|";
				else
					res += NEW_LINE;
			}
			for (int j = 0; j < columns * 3 + columns - 1; j++)
				res += "-";
			res += NEW_LINE;

		}

		if (nextCellValue() == CellValue.X)
			res += "X";
		else
			res += "O";
		res += " to play: ";

		return res;

	}

}
