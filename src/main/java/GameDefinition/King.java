package GameDefinition;

import Etc.PieceType;
import Etc.Pos;
import Etc.Side;
import GameDefinition.GameDef.Piece;

public class King extends Piece {

	final private PieceType type;
	
	public King(Side side, Pos pos) {
		super(side, pos);
		type = PieceType.KING;
	}

	@Override
	protected void makeNeighbors() {
		neighbors.clear();
		neighbors.add(currentPos.ds(1, 0));
		neighbors.add(currentPos.ds(1, 1));
		neighbors.add(currentPos.ds(0, 1));
		neighbors.add(currentPos.ds(-1, 1));
		neighbors.add(currentPos.ds(-1, -1));
		neighbors.add(currentPos.ds(0, -1));
		neighbors.add(currentPos.ds(1, -1));
		neighbors.add(currentPos.ds(-1, 0));
	}

	@Override
	public String toString() {
		if (side == Side.BLACK)
			return "BK";
		return "WK";
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
		return new King(side, currentPos);
	}
}
