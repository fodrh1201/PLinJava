package GameDefinition;

import Etc.Pos;
import GameDefinition.GameDef.Piece;

public class Board {
	Piece[][] board;
	private int row = 8;
	private int col = 8;

	public Board() {
		board = new Piece[row][col];
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public void reset() {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				board[i][j] = null;
			}
		}
	}

	public boolean terrain(Pos pos) {
		if (pos.x() >= row || pos.x() < 0)
			return false;
		if (pos.y() >= col || pos.y() < 0)
			return false;
		return true;
	}

	public Piece getPiece(Pos pos) {
		int x = pos.x();
		int y = pos.y();
		if (this.terrain(pos))
			return board[x][y];
		else
			return null;
	}

	public void setPiece(Piece piece) {
		Pos position = piece.getPos();
		int x = position.x();
		int y = position.y();
		
		if (this.terrain(position))
			board[x][y] = piece;
	}

	public void print() {
		System.out.print("  ");
		for (int i = 0; i < col; i++)
			System.out.print(" " + (char) (i + 'a') + " ");
		System.out.println();
		for (int i = 0; i < row; i++) {
			System.out.print(i + 1 + " ");
			for (int j = 0; j < col; j++) {
				if (board[i][j] == null)
					System.out.print("** ");
				else
					System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
	}
}
