package FINAL.MAIN;


import Etc.Pos;
import Etc.Side;
import GameDefinition.*;
import Interfaces.OffLine;
import Interfaces.Player;

public class GameManager extends GameDef implements OffLine {
	private Player black;
	private Player white;
	
	public GameManager(Player black, Player white) {
		this.black = black;
		this.white = white;
		reset();
	}

	/*
	 * inputPieceDef(); -> 평범한 게임 배치.
	 * inputPieceCheck(); -> 체크메이트 되는 상황.
	 * inputPieceCheck(); -> 체크 상황이지만 체크메이트는 아닌 상황.
	 * 
	 * 킹을 먹거나, 모든 경우를 따져봐도 체크를 벗어날 수 없으면 게임이 종료됩니다.
	 * (체크메이트일 경우 게임 종료.)
	 * 
	 */
	
	public void reset() {
		availableBlacks.clear();
		availableWhites.clear();
		board.reset();
		inputPiecesDef();
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
}
