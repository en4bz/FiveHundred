package comp303.fivehundred.engine;

import comp303.fivehundred.ai.BasicRobot;
import comp303.fivehundred.ai.RandomRobot;
import comp303.fivehundred.player.Team;

public class Driver
{	
	
	public static void main(String[] args)
	{
		int MAX_GAMES = 2;
		Team lTeam1 = new Team(new RandomRobot("RandomA"), new RandomRobot("RandomB"));
		Team lTeam2 = new Team(new RandomRobot("BasicA"), new RandomRobot("BasicB"));
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
				
				lEngine.exchange();
				lEngine.playRound();
				lEngine.computeScore();
			}
		}
		lGameStats.printStatistics();
		
	}
}
