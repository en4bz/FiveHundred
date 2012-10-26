package comp303.fivehundred.ai;

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
		Bid winningBid = Bid.max(pBids);
		Suit currentTrump = winningBid.getSuit();

		// Select lowest takes into account a no trump game
		while (discards.size() < NUMBER_CARDS)
		{
			discards.add(pHand.selectLowest(currentTrump));	
		}
		
		// Returns the discards
		return discards;
	}
}
