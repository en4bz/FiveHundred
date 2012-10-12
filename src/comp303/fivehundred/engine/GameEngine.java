package comp303.fivehundred.engine;

import comp303.fivehundred.ai.IRobotPlayer;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Deck;

public class GameEngine
{
	private IRobotPlayer[] aPlayers;
	private Hand[] aPlayerHands;
	private Trick aCurrentTrick;
		
	public GameEngine(IRobotPlayer pPlayer1, IRobotPlayer pPlayer2, IRobotPlayer pPlayer3, IRobotPlayer pPlayer4)
	{
		aPlayerHands = new Hand[4];
		aPlayers = new IRobotPlayer[4];
		aPlayers[0] = pPlayer1;
		aPlayers[1] = pPlayer2;
		aPlayers[2] = pPlayer3;
		aPlayers[3] = pPlayer4;
	}
	
	public void dealCards()
	{
		Deck temp = new Deck();
		temp.shuffle();
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 10; j++)
			{
				aPlayerHands[i].add(temp.draw());
			}
		}
		
	}
	
	public void playTrick()
	{
		//
	}
	
	public void computeScore()
	{
		return;
	}
}
