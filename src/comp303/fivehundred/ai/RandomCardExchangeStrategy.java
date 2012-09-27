package comp303.fivehundred.ai;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.CardList;

/**
 * @author Rayyan Khoury
 * Picks six cards at random. 
 */
public class RandomCardExchangeStrategy implements ICardExchangeStrategy
{
	
	// Number of cards to select
	private static final int NUMBER_CARDS = 6;
	
	@Override
	public CardList selectCardsToDiscard(Bid[] pBids, int pIndex, Hand pHand)
	{
		
		// Creates a new card list of the cards to be removed
		CardList removeCards = new CardList();
		
		/*
		 *  Runs a while loop checking the size of the newly created card list until 
		 *  there are six cards, and keeps adding cards at random using the add method 
		 *  from CardList which does not add a card if the card already exists
		 */
		while (removeCards.size() < (NUMBER_CARDS + 1))
		{
			
			removeCards.add(pHand.random());
		
		}
		
		// Returns the newly created CardList
		return removeCards;
		
	}

}
