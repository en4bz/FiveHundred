package comp303.fivehundred.ai;

import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;

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
		
		// Suit of the current play
		Suit trumpSuit = pTrick.getTrumpSuit();
		
		// Leading card
		Card leadingCard = pTrick.cardLed();
		
		// Leading card suit
		Suit leadingCardSuit = leadingCard.getSuit();
		
		// CardList all cards in the hand without the jokers
		CardList allCardsNoJokers = pHand.getNonJokers();
		
		// Cards that are of the current suit being led
		CardList followSuit = pHand.playableCards(leadingCardSuit, trumpSuit);

		// Checks if this player is leading or not and selects a card at random
		if (pTrick.size() == 0) 
		{
			// If the game is in no trump
			if (trumpSuit == null)
			{
				
				// If there are no cards except for Jokers
				if (allCardsNoJokers.size() == 0)
				{
					
					chosen = pHand.random();
					return chosen;
					
				}
				
				// Otherwise the hand contains some other cards
				else
				{
					
					chosen = allCardsNoJokers.random();
					return chosen;
					
				}
				
			}
			
			// If the game is a trump game
			else
			{
				
				chosen = pHand.random();
				return chosen;
				
			}
			
		}
		
		// Otherwise, this player is not leading and responding to the first card
		else 
		{
			
			// Chooses from legal cards
			if (followSuit.size() != 0) 
			{
				
				chosen = followSuit.random();
				return chosen;
				
			}
			
			// Just plays from the hand as cannot follow suit
			else 
			{
				
				chosen = pHand.random();
				return chosen;
				
			}
			
		}

	}
	
}
