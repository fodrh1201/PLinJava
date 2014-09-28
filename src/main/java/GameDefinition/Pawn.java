package GameDefinition;

import Etc.PieceType;
import Etc.Pos;
import Etc.Side;
import GameDefinition.GameDef.Piece;

public class Pawn extends Piece {

	final private PieceType type;
	
	public Pawn(Side side, Pos pos) {
		super(side, pos);
		type = PieceType.PAWN;
	}

	@Override
	protected boolean isLegal(Pos pos) {
		Pos left = currentPos.ds(0, -1);
		Pos right = currentPos.ds(0, 1);
		if (!terrain(pos))
			return false;
		if (side == Side.BLACK) {
			Pos oneStep = currentPos.ds(-1, 0);
			Pos twoStep = currentPos.ds(-2, 0);
			Pos downLeft = currentPos.ds(-1, -1);
			Pos downRight = currentPos.ds(-1, 1);
			if (pos.equals(oneStep) && refBoard.getPiece(oneStep) == null)
				return true;
			if (pos.equals(twoStep) && refBoard.getPiece(twoStep) == null)
				return true;
			if (pos.equals(downLeft) && refBoard.getPiece(downLeft) == null) {
				Piece leftPiece = refBoard.getPiece(left);
				if (leftPiece == null)
					return false;
				if (leftPiece.getMoveCount() != 1)
					return false;
				if (currentPos.x() != 3)
					return false;
				return true;
			}
			if (pos.equals(downLeft) && refBoard.getPiece(downLeft) != null) {
				if (refBoard.getPiece(downLeft).getSide() != side)
					return true;
				return false;
			}
			if (pos.equals(downRight) && refBoard.getPiece(downRight) == null) {
				Piece rightPiece = refBoard.getPiece(right);
				if (rightPiece == null)
					return false;
				if (rightPiece.getMoveCount() != 1)
					return false;
				if (currentPos.x() != 3)
					return false;
				return true;
			}
			if (pos.equals(downRight) && refBoard.getPiece(downRight) != null) {
				if (refBoard.getPiece(downRight).getSide() != side)
					return true;
				return false;
			}
		}
		if (side == Side.WHITE) {
			Pos oneStep = currentPos.ds(1, 0);
			Pos twoStep = currentPos.ds(2, 0);
			Pos upLeft = currentPos.ds(1, -1);
			Pos upRight = currentPos.ds(1, 1);
			if (pos.equals(oneStep) && refBoard.getPiece(oneStep) == null)
				return true;
			if (pos.equals(twoStep) && refBoard.getPiece(twoStep) == null)
				return true;
			if (pos.equals(upLeft) && refBoard.getPiece(upLeft) == null) {
				Piece leftPiece = refBoard.getPiece(left);
				if (leftPiece == null)
					return false;
				if (leftPiece.getMoveCount() != 1)
					return false;
				if (currentPos.x() != 4)
					return false;
				return true;
			}
			if (pos.equals(upLeft) && refBoard.getPiece(upLeft) != null) {
				if (refBoard.getPiece(upLeft).getSide() != side)
					return true;
				return false;
			}
			if (pos.equals(upRight) && refBoard.getPiece(upRight) == null) {
				Piece rightPiece = refBoard.getPiece(right);
				if (rightPiece == null)
					return false;
				if (rightPiece.getMoveCount() != 1)
					return false;
				if (currentPos.x() != 4)
					return false;
				return true;
			}
			if (pos.equals(upRight) && refBoard.getPiece(upRight) != null) {
				if (refBoard.getPiece(upRight).getSide() != side)
					return true;
				return false;
			}
		}
		return false;
	}

	@Override
	protected void makeNeighbors() {
		neighbors.clear();
		neighbors.add(currentPos.ds(1, 0));
		neighbors.add(currentPos.ds(1, 1));
		neighbors.add(currentPos.ds(-1, 1));
		neighbors.add(currentPos.ds(-1, -1));
		neighbors.add(currentPos.ds(1, -1));
		neighbors.add(currentPos.ds(-1, 0));
		neighbors.add(currentPos.ds(2, 0));
		neighbors.add(currentPos.ds(-2, 0));
	}

	@Override
	public String toString() {
		if (side == Side.BLACK)
			return "BP";
		return "WP";
	}

	@Override
	public PieceType getType() {
		return type;
	}

	@Override
	public Piece clone() {
		return new Pawn(side, currentPos);
	}
	
}
