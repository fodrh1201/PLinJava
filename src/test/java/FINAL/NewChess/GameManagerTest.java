package FINAL.NewChess;

import org.junit.Before;
import org.junit.Test;

import Etc.Side;
import FINAL.MAIN.GameManager;
import FINAL.MAIN.User;

public class GameManagerTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		GameManager gm = new GameManager(new User(Side.BLACK), new User(Side.WHITE));
		gm.printBoard();
	}

}
