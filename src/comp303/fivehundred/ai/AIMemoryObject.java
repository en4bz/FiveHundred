package comp303.fivehundred.ai;

import comp303.fivehundred.util.CardList;

public class AIMemoryObject
{
	
	private static CardList aCardsPlayed;
	
	public AIMemoryObject ()
	{
		
		aCardsPlayed = new CardList();
		
	}
	
	public void newGame ()
	{
		
		aCardsPlayed = new CardList();
		
	}

}
