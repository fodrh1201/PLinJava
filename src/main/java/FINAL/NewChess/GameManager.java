package FINAL.NewChess;

import java.util.ArrayList;
import java.util.Iterator;

import Etc.PieceType;
import Etc.Pos;
import Etc.Side;
import GameDefinition.*;
import Interfaces.OffLine;
import Interfaces.Player;

public class GameManager extends GameDef implements OffLine {
	private Player black;
	private Player white;
	private Piece blackKing;
	private Piece whiteKing;
	
	public GameManager(Player black, Player white) {
		this.black = black;
		this.white = white;
		reset();
	}

	public void reset() {
		turn = Side.BLACK;
		availableBlacks.clear();
		availableWhites.clear();
		board.reset();
		inputPieces();
		calculPiecePos();
	}
	public void endGame() {
		black.endInput();
		white.endInput();
	}
	
	public void gameStart() {
		reset();
		while (!checkMate()) {
			if (turn == Side.BLACK) {
				Piece chosen = black.choose();
				Pos pastPos = chosen.getPos();
				Pos goal = black.moveTo();
				move(chosen, pastPos, goal);
				printBoard();
				turnChange();
			} else {
				Piece chosen = white.choose();
				Pos pastPos = chosen.getPos();
				Pos goal = white.moveTo();
				move(chosen, pastPos, goal);
				printBoard();
				turnChange();
			}
		}
		Side winner = whoIsWinner();
		
		switch (winner) {
		case BLACK:
			System.out.println("Black Win!");
			break;
		default:
			System.out.println("White Win!");
			break;
		}
		endGame();
	}
	
	private void move(Piece chosen, Pos pastPos, Pos goal) {
		deletePiece(goal);
		chosen.move(goal);
		//enPassant(chosen, pastPos);
		castling(chosen, goal);
		renewalBoard();
		calculPiecePos();
	}
	
	private void calculPiecePos() {
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
		if (enemyPiece == null)
			return;
		deletedPieces.add(enemyPiece);
		if (turn == Side.BLACK) {
			availableWhites.remove(enemyPiece);
		} else {
			availableBlacks.remove(enemyPiece);
		}
	}
	
	private void turnChange() {
		if (turn == Side.BLACK) turn = Side.WHITE;
		else turn = Side.BLACK;
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
	
	private void renewalBoard() {
		board.reset();
		Iterator<Piece> iterBlack = availableBlacks.iterator();
		Iterator<Piece> iterWhite = availableWhites.iterator();
		while (iterBlack.hasNext())
			board.setPiece(iterBlack.next());
		while (iterWhite.hasNext())
			board.setPiece(iterWhite.next());
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
	
	private boolean checkMate() {
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
			return true;
		}
	}
	
	private Side whoIsWinner() {
		if (turn == Side.BLACK)
			return Side.WHITE;
		else
			return Side.BLACK;
	}
	
	private void inputPieces() {
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
//		inputPiece(new Bishop(Side.WHITE, new Pos(2, 'g')));
//		inputPiece(new Rook(Side.WHITE, new Pos(1, 'f')));
//		inputPiece(new Rook(Side.WHITE, new Pos(2, 'f')));
	}
	
	private void inputPiece(Piece piece) {
		if (piece.getSide() == Side.BLACK)
			availableBlacks.add(piece);
		else
			availableWhites.add(piece);
		
		board.setPiece(piece);
	}
	
	public void printBoard() {
		board.print();
	}
}
