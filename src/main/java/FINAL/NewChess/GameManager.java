package FINAL.NewChess;


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

	public void reset() {
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
}
