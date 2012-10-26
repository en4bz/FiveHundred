package comp303.fivehundred.ai.basic;

import comp303.fivehundred.ai.ICardExchangeStrategy;
import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;

/**
 * @author Rayyan Khoury
 * Discards the six lowest non-trump cards. 
 */

public class BasicCardExchangeStrategy implements ICardExchangeStrategy
{
	
	// Number of cards to select
	private static final int NUMBER_CARDS = 6;
	
	@Override
	public CardList selectCardsToDiscard(Bid[] pBids, int pIndex, Hand pHand)
	{
		
		// CardList maintaining the discarded cards
		CardList discards = new CardList();
		
		// Determining which bid is the winning bid, and storing this suit
		Bid winningBid = pBids[0];
		Suit currentTrump = winningBid.getSuit();
		for (int i = 1; i < 4; i++)
		{
			
			if (winningBid.compareTo(pBids[i]) < 0)
			{
				
				winningBid = pBids[i];
				currentTrump = winningBid.getSuit();
				
			}
			
		}

		// Select lowest takes in to account a no trump game
		while (discards.size() < 6)
		{
			
			discards.add(pHand.selectLowest(currentTrump));
			
		}
		
		// Returns the discards
		return discards;
		
	}

}
