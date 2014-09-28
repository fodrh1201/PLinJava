package GameDefinition;

import Etc.PieceType;
import Etc.Pos;
import Etc.Side;
import GameDefinition.GameDef.Piece;

public class Knight extends Piece {

	final private PieceType type;
	
	public Knight(Side side, Pos pos) {
		super(side, pos);
		type = PieceType.KNIGHT;
	}

	@Override
	protected void makeNeighbors() {
		neighbors.clear();
		neighbors.add(currentPos.ds(2, 1));
		neighbors.add(currentPos.ds(2, -1));
		neighbors.add(currentPos.ds(-2, 1));
		neighbors.add(currentPos.ds(-2, -1));
		neighbors.add(currentPos.ds(1, 2));
		neighbors.add(currentPos.ds(1, -2));
		neighbors.add(currentPos.ds(-1, 2));
		neighbors.add(currentPos.ds(-1, -2));		
	}

	@Override
	public String toString() {
		if (side == Side.BLACK)
			return "BN";
		return "WN";
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
		return new Knight(side, currentPos);
	}
}
