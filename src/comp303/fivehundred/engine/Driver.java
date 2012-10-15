package comp303.fivehundred.engine;

public class Driver
{	
	public static void main(String[] args)
	{
		GameEngine lEngine = new GameEngine(null, null, null, null);
		
		lEngine.newGame();
		while(!lEngine.gameOver())
		{
			lEngine.dealCards();
			lEngine.bid();
			lEngine.exchange();
			for(int i = 0; i < 10; i++){
				lEngine.playTrick();
			}
			lEngine.computeScore();
		}
		lEngine.declareWinner();
	}
}
