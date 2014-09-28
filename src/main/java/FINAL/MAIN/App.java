package FINAL.MAIN;

import Etc.Side;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	GameManager gm = new GameManager(new User(Side.BLACK), new User(Side.WHITE));
		gm.printBoard();
		gm.gameStart();
    }
}
