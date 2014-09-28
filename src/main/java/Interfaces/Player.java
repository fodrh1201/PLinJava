package Interfaces;

import Etc.Pos;
import GameDefinition.GameDef.Piece;

public interface Player {
	Piece choose();
	Pos moveTo();
	void endInput();
}
