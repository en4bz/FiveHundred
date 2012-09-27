package comp303.fivehundred.ai;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;



/* 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * NO ACCESS TO THE SUIT OF THE CONTRACT!
 * CANNOT FULFILL THE FIRST REQUIREMENT COMPLETELY!
 * MUST FIX!
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */

/**
 * @author Rayyan Khoury
 *  * If leading, picks a card at random except a joker if the contract is in no trump.
 * If not leading and the hand contains cards that can follow suit, pick a suit-following 
 * card at random. If not leading and the hand does not contain cards that can follow suit,
 * pick a card at random (including trumps, if available).
 */
public class RandomPlayingStrategy implements IPlayingStrategy
{
	@Override
	public Card play(Trick pTrick, Hand pHand)
	{
		
		// The Card to return
		Card chosen = null;
		
		// Leading card
		Card leadingCard = pTrick.cardLed();
		Suit leadingSuit = leadingCard.getSuit();
		
		// Suit of the current play
		// Suit current = Bid.getSuit();
		
		// Checks if this player is leading or not and selects a card at random
		if (pTrick.size() == 0) 
		{
			// While loop chosen in case method returns null
			while (chosen != null)
			{
				chosen = pHand.random();
			}
			
			return chosen;
			
		}
		
		// Cards that are of the current suit being led
		CardList playableCards = pHand.getTrumpCards(leadingSuit);
		
		// While loop chosen in case method returns null
		while (chosen != null) 
		{
			
			if (playableCards.size() != 0) 
			{
				
				chosen = playableCards.random();
				
			}
			
			else 
			{
				
				chosen = pHand.random();
				
			}
			
		}
		
		return chosen;
	}
	
}
