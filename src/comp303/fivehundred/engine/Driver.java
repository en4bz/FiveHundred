package comp303.fivehundred.engine;

import comp303.fivehundred.ai.BasicRobot;
import comp303.fivehundred.ai.RandomRobot;
import comp303.fivehundred.player.Team;

/**
 * Uses the GameEngine to play a customizable number of games in automatic mode.
 * @author Ian Forbes and Eleyine Zarour 
 *
 */
public class Driver
{	
	private static final int MAX_GAMES = 10000;
	
	/**
	 * Play a customizable number of games in automatic mode.
	 * @param pArguments : Arguments to main
	 */
	public static void main(String[] pArguments)
	{
		Team lTeam1 = new Team(new RandomRobot("RandomA"), new RandomRobot("RandomB"));
		Team lTeam2 = new Team(new BasicRobot("BasicA"), new BasicRobot("BasicB"));
		GameEngine lEngine = new GameEngine(lTeam1, lTeam2);
		GameStatistics lGameStats = new GameStatistics();
		lEngine.addObserver(lGameStats);
		lEngine.addObserver(new GameLogger());
		
		for(int i = 0; i < MAX_GAMES; i++)
		{
			lEngine.newGame();
			while(!lEngine.isGameOver())
			{
				do
				{
					lEngine.deal();
					lEngine.bid();
				} while (lEngine.allPasses());
			//	System.out.println("EXCHANGE");
				lEngine.exchange();
			//	System.out.print("PLAY");
				lEngine.playRound();
				lEngine.computeScore();
			}
		}
		lGameStats.printStatistics();
	}
}
