package FINAL.MAIN;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

import Etc.PieceType;
import Etc.Pos;
import Etc.Side;
import GameDefinition.GameDef;
import Interfaces.Player;

public class User extends GameDef implements Player {
	final private Side side;
	private Piece chosen;
	private Pos choose;
	private Scanner scanner = new Scanner(System.in);

	public User(Side side) {
		this.side = side;
	}

	private Pos input() {
		System.out.println("행과 열을 입력하세요.");
		int row = 0;

		try {
			System.out.print("행: ");
			row = scanner.nextInt();
		} catch (InputMismatchException e) {
			System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
		}
		System.out.print("열: ");
		String input = scanner.next();
		char col = input.charAt(0);
		return new Pos(row, col);
	}

	public Piece choose() {
		System.out.println();
		System.out.println(side + "턴, Piece를 선택하겠습니다.");
		choose = input();
		while (!isChoosable(choose)) {
			choose = input();
		}
		System.out.println(choose + "선택됐습니다.");
		chosen = board.getPiece(choose);
		return chosen;
	}

	public Pos moveTo() {
		Piece target = chosen;
		if (target == null)
			return null;
		System.out.println("움직을 곳을 선택하겠습니다.");
		Pos goal = input();

		if (isCastling(goal))
			return goal;

		while (!isMovable(goal, target)) {
			System.out.println(goal);
			goal = input();

			if (isCastling(goal))
				return goal;
		}
		System.out.println(goal + "로 이동합니다.");
		return goal;
	}

	public void endInput() {
		scanner.close();
	}

	private boolean isChoosable(Pos pos) {
		if (!terrain(pos))
			return false;
		if (board.getPiece(pos) == null)
			return false;
		if (board.getPiece(pos).getSide() != side)
			return false;
		return true;
	}

	private boolean isMovable(Pos pos, Piece piece) {
		ArrayList<Pos> movableList = piece.getLegalNeighbors();
		return movableList.contains(pos);
	}

	private boolean isCastling(Pos pos) {
		char col = (char) (pos.y() + 'a');
		int row = pos.x() + 1;
		if (chosen.getType() != PieceType.KING)
			return false;
		if (chosen.getMoveCount() != 0)
			return false;
		if (!(col == 'g' || col == 'c'))
			return false;
		if (check())
			return false;
		if (col == 'g') {
			if (board.getPiece(new Pos(row, 'f')) != null)
				return false;
			if (board.getPiece(new Pos(row, 'g')) != null)
				return false;
			if (isAttackable(chosen, new Pos(row, 'f'))) 
				return false;
			if (isAttackable(chosen, new Pos(row, 'g')))
				return false;
			if (board.getPiece(new Pos(row, 'h')) == null)
				return false;
			if (board.getPiece(new Pos(row, 'h')).getMoveCount() != 0)
				return false;
		}
		if (col == 'c') {
			if (board.getPiece(new Pos(row, 'd')) != null)
				return false;
			if (board.getPiece(new Pos(row, 'c')) != null)
				return false;
			if (isAttackable(chosen, new Pos(row, 'd')))
				return false;
			if (isAttackable(chosen, new Pos(row, 'c')))
				return false;
			if (board.getPiece(new Pos(row, 'a')) == null)
				return false;
			if (board.getPiece(new Pos(row, 'a')).getMoveCount() != 0)
				return false;
		}
		return true;
	}

	private boolean isAttackable(Piece piece, Pos pos) {
		Iterator<Piece> iterBlack = availableBlacks.iterator();
		Iterator<Piece> iterWhite = availableWhites.iterator();

		if (turn == Side.BLACK) {
			boolean isAllWhiteUnAttachable = true;
			move(piece, choose, pos);
			renewalBoard();
			calculPiecePos();
			while (iterWhite.hasNext()) {
				if (iterWhite.next().isAttachable(pos)) {
					isAllWhiteUnAttachable = false;
					break;
				}
			}
			piece.turnBack();
			turnBack();
			renewalBoard();
			calculPiecePos();
			if (isAllWhiteUnAttachable)
				return false;
			return true;
		} else {
			boolean isAllBlackUnAttachable = true;
			move(piece, choose, pos);
			renewalBoard();
			calculPiecePos();
			while (iterBlack.hasNext()) {
				if (iterBlack.next().isAttachable(pos)) {
					isAllBlackUnAttachable = false;
					break;
				}
			}
			piece.turnBack();
			turnBack();
			renewalBoard();
			calculPiecePos();
			if (isAllBlackUnAttachable)
				return false;
			return true;
		}
	}
}
