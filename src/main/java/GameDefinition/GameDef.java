package GameDefinition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import Etc.PieceType;
import Etc.Pos;
import Etc.Side;


public abstract class GameDef {
	final protected static Board board = new Board();
	final protected static LinkedList<Piece> availableBlacks = new LinkedList<Piece>();
	final protected static LinkedList<Piece> availableWhites = new LinkedList<Piece>();
	final protected static LinkedList<Piece> deletedPieces = new LinkedList<Piece>();
	protected static Side turn = Side.BLACK;
	protected static Piece blackKing;
	protected static Piece whiteKing;
	
	protected void move(Piece chosen, Pos pastPos, Pos goal) {
		deletePiece(goal);
		chosen.move(goal);
		enPassant(chosen, pastPos);
		castling(chosen, goal);
		renewalBoard();
		calculPiecePos();
	}

	protected void calculPiecePos() {
		Iterator<Piece> iterBlack = availableBlacks.iterator();
		Iterator<Piece> iterWhite = availableWhites.iterator();
		while (iterBlack.hasNext()) {
			iterBlack.next().makeLegalNeighbors();
		}
		while (iterWhite.hasNext()) {
			iterWhite.next().makeLegalNeighbors();
		}
	}

	private void deletePiece(Pos pos) {
		Piece enemyPiece = board.getPiece(pos);
		if (enemyPiece == null) {
			deletedPieces.add(null);
			return;
		}
		deletedPieces.add(enemyPiece);
		if (turn == Side.BLACK) {
			availableWhites.remove(enemyPiece);
		} else {
			availableBlacks.remove(enemyPiece);
		}
	}

	protected void turnChange() {
		if (turn == Side.BLACK) turn = Side.WHITE;
		else turn = Side.BLACK;
	}

	protected boolean terrain(Pos pos) {
		return board.terrain(pos);
	}
	
	protected Side whoIsWinner() {
		if (turn == Side.BLACK)
			return Side.WHITE;
		else
			return Side.BLACK;
	}

	private void turnBack() {
		Piece turnBack = deletedPieces.pollLast();
		if (turnBack == null)
			return;
		Side side = turnBack.getSide();
		if (side == Side.BLACK)
			availableBlacks.add(turnBack);
		else
			availableWhites.add(turnBack);
	}

	private void renewalBoard() {
		board.reset();
		Iterator<Piece> iterBlack = availableBlacks.iterator();
		Iterator<Piece> iterWhite = availableWhites.iterator();
		while (iterBlack.hasNext())
			board.setPiece(iterBlack.next());
		while (iterWhite.hasNext())
			board.setPiece(iterWhite.next());
	}

	private void castling(Piece piece, Pos pos) {
		Side side = piece.getSide();
		PieceType type = piece.getType();
		int count = piece.getMoveCount();
		
		if (type == PieceType.KING && side == Side.BLACK && count == 1 &&
				(pos.equals(new Pos(8,'g')) || pos.equals(new Pos(8,'c')))) {
			if (pos.equals(new Pos(8,'g'))) {
				Piece rook = board.getPiece(new Pos(8, 'h'));
				rook.move(new Pos(8, 'f'));
			} else {
				Piece rook = board.getPiece(new Pos(8, 'a'));
				rook.move(new Pos(8, 'd'));
			}
		} else if (type == PieceType.KING && side == Side.WHITE && count == 1 &&
				( pos.equals(new Pos(1,'g')) || pos.equals(new Pos(1,'c')) )) {
			if (pos.equals(new Pos(1,'g'))) {
				Piece rook = board.getPiece(new Pos(1, 'h'));
				rook.move(new Pos(1, 'f'));
			} else {
				Piece rook = board.getPiece(new Pos(1, 'a'));
				rook.move(new Pos(1, 'd'));
			}
		}
		return;
	}
	
	private void enPassant(Piece piece, Pos pastPos) {
		Pos currentPos = piece.getPos();
		int curRow = currentPos.x() + 1;
		int curCol = currentPos.y();
		int pastRow = pastPos.x() + 1;
		int pastCol = pastPos.y();
		Side side = piece.getSide();
		PieceType type = piece.getType();
		
		if (type == PieceType.PAWN && side == Side.BLACK && pastRow == 4 && curRow == 3 && Math.abs((int)(curCol-pastCol)) == 1) {
			Pos enemyPos = currentPos.ds(1, 0);
			Piece enemyPiece = board.getPiece(enemyPos);
			if (enemyPiece == null)
				return;
			Side enemySide = enemyPiece.getSide();
			PieceType enemyType = enemyPiece.getType();
			if (enemySide == Side.WHITE && enemyType == PieceType.PAWN && enemyPiece.getMoveCount() == 1)
				deletePiece(enemyPos);
		} else if (type == PieceType.PAWN && side == Side.WHITE && pastRow == 5 && curRow == 6 && Math.abs((int)(curCol-pastCol)) == 1) {
			Pos enemyPos = currentPos.ds(-1, 0);
			Piece enemyPiece = board.getPiece(enemyPos);
			if (enemyPiece == null)
				return;
			Side enemySide = enemyPiece.getSide();
			PieceType enemyType = enemyPiece.getType();
			if (enemySide == Side.BLACK && enemyType == PieceType.PAWN && enemyPiece.getMoveCount() == 1)
				deletePiece(enemyPos);
		}
		return;
	}
	
	protected boolean checkMate() {
		Iterator<Piece> iterBlack = availableBlacks.iterator();
		Iterator<Piece> iterWhite = availableWhites.iterator();
		
		if (turn == Side.BLACK) {
			while (iterBlack.hasNext()) {
				Piece shieldPiece = iterBlack.next();
				Pos pastPos = shieldPiece.getPos();
				ArrayList<Pos> legalPositions = shieldPiece.getLegalNeighbors();
				int len = legalPositions.size();
				for (int i = 0; i < len; i++) {
					boolean isAllWhiteUnAttachable = true;
					Pos to = legalPositions.get(i);
					move(shieldPiece, pastPos, to);
					Pos kingPos = blackKing.getPos();
					iterWhite = availableWhites.iterator();
					while (iterWhite.hasNext()) {
						if (iterWhite.next().isAttachable(kingPos)) {
							isAllWhiteUnAttachable = false;
							break;
						}
					}
					shieldPiece.turnBack();
					turnBack();
					renewalBoard();
					calculPiecePos();
					if (isAllWhiteUnAttachable)
						return false;
				}
			}
			if (!availableBlacks.contains(blackKing))
				return true;
			return true;
		} else {
			while (iterWhite.hasNext()) {
				Piece shieldPiece = iterWhite.next();
				Pos pastPos = shieldPiece.getPos();
				ArrayList<Pos> legalPositions = shieldPiece.getLegalNeighbors();
				int len = legalPositions.size();
				for (int i = 0; i < len; i++) {
					boolean isAllBlackUnAttachable = true;
					Pos to = legalPositions.get(i);
					move(shieldPiece, pastPos, to);
					Pos kingPos = whiteKing.getPos();
					iterBlack = availableBlacks.iterator();
					while (iterBlack.hasNext()) {
						if (iterBlack.next().isAttachable(kingPos)) {
							isAllBlackUnAttachable = false;
							break;
						}
					}
					shieldPiece.turnBack();
					turnBack();
					renewalBoard();
					calculPiecePos();
					if (isAllBlackUnAttachable)
						return false;
				}
			}
			if (!availableWhites.contains(whiteKing))
				return true;
			return true;
		}
	}
	
	private void inputPiece(Piece piece) {
		if (piece.getSide() == Side.BLACK)
			availableBlacks.add(piece);
		else
			availableWhites.add(piece);
		
		board.setPiece(piece);
	}

	protected void inputPieces() {
		blackKing = new King(Side.BLACK, new Pos(8, 'e'));
		inputPiece(blackKing);
		inputPiece(new Queen(Side.BLACK, new Pos(8, 'd')));
		inputPiece(new Bishop(Side.BLACK, new Pos(8, 'c')));
		inputPiece(new Bishop(Side.BLACK, new Pos(8, 'f')));
		inputPiece(new Knight(Side.BLACK, new Pos(8, 'b')));
		inputPiece(new Knight(Side.BLACK, new Pos(8, 'g')));
		inputPiece(new Rook(Side.BLACK, new Pos(8, 'a')));
		inputPiece(new Rook(Side.BLACK, new Pos(8, 'h')));
		inputPiece(new Pawn(Side.BLACK, new Pos(7, 'a')));
		inputPiece(new Pawn(Side.BLACK, new Pos(7, 'b')));
		inputPiece(new Pawn(Side.BLACK, new Pos(7, 'c')));
		inputPiece(new Pawn(Side.BLACK, new Pos(7, 'd')));
		inputPiece(new Pawn(Side.BLACK, new Pos(7, 'e')));
		inputPiece(new Pawn(Side.BLACK, new Pos(7, 'f')));
		inputPiece(new Pawn(Side.BLACK, new Pos(7, 'g')));
		inputPiece(new Pawn(Side.BLACK, new Pos(7, 'h')));
		
		whiteKing = new King(Side.WHITE, new Pos(1, 'e'));
		inputPiece(whiteKing);
		inputPiece(new Queen(Side.WHITE, new Pos(1, 'd')));
		inputPiece(new Bishop(Side.WHITE, new Pos(1, 'c')));
		inputPiece(new Bishop(Side.WHITE, new Pos(1, 'f')));
		inputPiece(new Knight(Side.WHITE, new Pos(1, 'b')));
		inputPiece(new Knight(Side.WHITE, new Pos(1, 'g')));
		inputPiece(new Rook(Side.WHITE, new Pos(1, 'a')));
		inputPiece(new Rook(Side.WHITE, new Pos(1, 'h')));
		inputPiece(new Pawn(Side.WHITE, new Pos(2, 'a')));
		inputPiece(new Pawn(Side.WHITE, new Pos(2, 'b')));
		inputPiece(new Pawn(Side.WHITE, new Pos(2, 'c')));
		inputPiece(new Pawn(Side.WHITE, new Pos(2, 'd')));
		inputPiece(new Pawn(Side.WHITE, new Pos(2, 'e')));
		inputPiece(new Pawn(Side.WHITE, new Pos(2, 'f')));
		inputPiece(new Pawn(Side.WHITE, new Pos(2, 'g')));
		inputPiece(new Pawn(Side.WHITE, new Pos(2, 'h')));
		
//		blackKing = new King(Side.BLACK, new Pos(3,'h'));
//		inputPiece(blackKing);
//		whiteKing = new King(Side.WHITE, new Pos(1,'h'));
//		inputPiece(whiteKing);
//		inputPiece(new Bishop(Side.BLACK, new Pos(3, 'e')));
//		inputPiece(new Bishop(Side.BLACK, new Pos(3, 'f')));
		
//		blackKing = new King(Side.BLACK, new Pos(8, 'e'));
//		inputPiece(blackKing);
//		whiteKing = new King(Side.WHITE, new Pos(1, 'g'));
//		inputPiece(whiteKing);
//		inputPiece(new Rook(Side.BLACK, new Pos(5, 'h')));
//		inputPiece(new Queen(Side.BLACK, new Pos(1, 'h')));
//		inputPiece(new Pawn(Side.WHITE, new Pos(2, 'g')));
//		inputPiece(new Rook(Side.WHITE, new Pos(1, 'f')));
//		inputPiece(new Rook(Side.WHITE, new Pos(2, 'f')));
	}
	
	public void printBoard() {
		board.print();
	}
	
	public static abstract class Piece {
		final protected Side side;
		final protected Board refBoard = board;
		protected ArrayList<Pos> neighbors = new ArrayList<Pos>();
		protected ArrayList<Pos> legalNeighbors = new ArrayList<Pos>();
		protected LinkedList<Pos> history = new LinkedList<Pos>();
		protected int moveCount;
		protected Pos currentPos;
		
		public abstract Piece clone();
		protected abstract boolean isLegal(Pos pos);
		protected abstract void makeNeighbors();
		public abstract PieceType getType();
		
		public Piece(Side side, Pos pos) {
			currentPos = pos;
			this.side = side;
			makeNeighbors();
			makeLegalNeighbors();
			moveCount = 0;
		}
		
		protected boolean terrain(Pos pos) {
			return refBoard.terrain(pos);
		}
		
		public void makeLegalNeighbors() {
			legalNeighbors.clear();
			makeNeighbors();
			int len = neighbors.size();
			for (int i = 0; i < len; i++) {
				if (isLegal(neighbors.get(i))) 
					legalNeighbors.add(neighbors.get(i));
			}
		}
		public boolean isAttachable(Pos pos) {
			int len = legalNeighbors.size();
			for (int i = 0; i < len; i++) {
				if (legalNeighbors.get(i).equals(pos))
					return true;
			}
			return false;
		}
		
		public void move(Pos pos) {
			moveCount++;
			history.add(currentPos.ds(0, 0));
			currentPos = pos;
		}
		
		public void turnBack() {
			currentPos = history.pollLast();
		}
		
		public Side getSide() {
			return side;
		}
		public Pos getPos() {
			return currentPos;
		}
		
		public int getMoveCount() {
			return moveCount;
		}
		public ArrayList<Pos> getLegalNeighbors() {
			return legalNeighbors;
		}
	}
}
