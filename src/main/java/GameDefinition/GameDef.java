package GameDefinition;

import java.util.ArrayList;
import java.util.LinkedList;

import Etc.PieceType;
import Etc.Pos;
import Etc.Side;


public abstract class GameDef {
	protected static LinkedList<Piece> availableBlacks = new LinkedList<Piece>();
	protected static LinkedList<Piece> availableWhites = new LinkedList<Piece>();
	protected static LinkedList<Piece> deletedPieces = new LinkedList<Piece>();
	protected static Board board = new Board();
	protected static Side turn;
	
	protected boolean terrain(Pos pos) {
		return board.terrain(pos);
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
