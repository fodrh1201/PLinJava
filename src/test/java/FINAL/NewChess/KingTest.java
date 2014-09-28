package FINAL.NewChess;

import org.junit.Test;

import Etc.Pos;
import Etc.Side;
import GameDefinition.King;
import junit.framework.TestCase;

public class KingTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	@Test
	public void testConstruct () {
		King king = new King(Side.BLACK, new Pos(3, 'a'));
		King king2 = new King(Side.WHITE, new Pos(4, 'b'));
		System.out.println(king.getPos() + " " + king2.getPos());
	}
}
