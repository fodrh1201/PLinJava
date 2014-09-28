package FINAL.NewChess;

import java.util.ArrayList;
import java.util.Scanner;

import Etc.Pos;
import Etc.Side;
import GameDefinition.GameDef;
import Interfaces.Player;

public class User extends GameDef implements Player {
	final private Side side;
	private Piece chosen;
	private Scanner scanner = new Scanner(System.in);
	
	public User(Side side) {
		this.side = side;
	}
	
	private Pos input() {
		System.out.println("행과 열을 입력하세요.");
		int row = scanner.nextInt();
		String input = scanner.next();
		char col = input.charAt(0);
		return new Pos(row, col);
	}

	public Piece choose() {
		Pos choose = input();
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
		Pos goal = input();
		while (!isMovable(goal, target)) {
			System.out.println(goal);
			goal = input();
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
}
