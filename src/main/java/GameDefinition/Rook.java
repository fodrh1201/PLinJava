package GameDefinition;

import Etc.PieceType;
import Etc.Pos;
import Etc.Side;
import GameDefinition.GameDef.Piece;

public class Rook extends Piece {

	final private PieceType type;
	
	public Rook(Side side, Pos pos) {
		super(side, pos);
		type = PieceType.ROOK;
	}

	@Override
	protected void makeNeighbors() {
		neighbors.clear();
		findPosition(1, 0);
		findPosition(-1, 0);
		findPosition(0, 1);
		findPosition(0, -1);
	}

	private void findPosition(int row, int col) {
		int size = 8;
		for (int i = 1; i < size; i++) {
			Pos newPos = currentPos.ds(i*row, i*col);
			neighbors.add(newPos);
			if (refBoard.getPiece(newPos) != null)
				break;
		}
	}
	
	@Override
	public String toString() {
		if (side == Side.BLACK)
			return "BR";
		return "WR";
	}

	@Override
	public PieceType getType() {
		return type;
	}
	protected boolean isLegal(Pos pos) {
		if (!terrain(pos))
			return false;
		if (refBoard.getPiece(pos) == null)
			return true;
		if (refBoard.getPiece(pos).getSide() != side)
			return true;
		return false;
	}

	@Override
	public Piece clone() {
		return new Rook(side, currentPos);
	}
}
