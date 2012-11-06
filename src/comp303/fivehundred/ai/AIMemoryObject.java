package comp303.fivehundred.ai;

import java.util.Iterator;

import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

public class AIMemoryObject
{
	
	private static CardList aCardsPlayed;
	private static Iterator<Card> aIt;
	
	public AIMemoryObject ()
	{
		
		aCardsPlayed = new CardList();
		
	}
	
	public void newGame()
	{
		
		aCardsPlayed.clear();
		
	}
	
	public void addCards(Trick pTrick)
	{
	
		aIt = pTrick.iterator();
		
		while (aIt.hasNext())
		{
			
			aCardsPlayed.add(aIt.next());
			
		}
		
	}
	
	public boolean alreadyPlayed(Card pCard)
	{
		
		return aCardsPlayed.contains(pCard);
		
	}
	
	public CardList allCardsPlayed()
	{
		
		return aCardsPlayed.clone();
		
	}
	
	

}
