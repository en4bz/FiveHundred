package comp303.fivehundred.model;

import java.util.Comparator;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;


/**
 * @author Eleyine Zarour
 * A card list specialized for handling cards discarded
 * as part of the play of a trick.
 */
public class Trick extends CardList
{	
	private final Suit aTrump;
	
	/**
	 * Constructs a new empty trick for the specified contract.
	 * @param pContract The contract that this trick is played for.
	 */
	public Trick(Bid pContract)
	{
		aTrump = pContract.getSuit();
	}
	
	/**
	 * @return The Suit of the trump. Can be null for no-trump.
	 */
	public Suit getTrumpSuit()
	{
		return aTrump;
	}
	
	
	/**
	 * @return The effective suit led.
	 * @throws ModelException if the card led is a joker
	 */
	public Suit getSuitLed()
	{
		if(jokerLed())
		{
			throw new ModelException("Cannot get suit of leading joker.");
		}
		return cardLed().getEffectiveSuit(aTrump);
	}
	
	/**
	 * @return True if a joker led this trick
	 */
	public boolean jokerLed()
	{
		return cardLed().isJoker();
	}
	
	/**
	 * @return The card that led this trick
	 * @pre size() > 0
	 */
	public Card cardLed()
	{
		assert size() > 0;
		return getFirst();
	}

	/**
	 * @return Highest card that actually follows suit (or trumps it).
	 * I.e., the card currently winning the trick.
	 * @pre size() > 0
	 */
	public Card highest()
	{
		assert size() > 0;
		return sort(getBySuitComparator()).getLast();
	}
	
	/**
	 * @return The index of the card that wins the trick.
	 */
	// TODO: might want to add a getIndexOf(Card) method to CardList, it could be useful 
	// if we have more methods like the one below
	public int winnerIndex()
	{
		Card lHighest = highest();
		int lIndex = 0;
		for(Card c: this)
		{
			if(c.equals(lHighest))
			{
				break;
			}
			lIndex++;
		}
		return lIndex;
	}
	
	//Helper Methods (non-specified by milestone 1)
	
	/**
	 * Creates a comparator for this trick which uses suit as primary key, then rank. 
	 * Adjusts for a trump (if there is one) and the suit led. Jacks rank above aces 
	 * if they are in the trump suit.
	 * @return a comparator which returns the highest card based on the trick TrumpSuit 
	 * (which may be no-trump) and SuitLed.
	 */
	public Comparator<Card> getBySuitComparator()
	{

		Suit[] lSuitOrder = Card.Suit.values().clone();
		int lTrumpSuitIndex = lSuitOrder.length - 1;
		int lSuitLedIndex = lSuitOrder.length - 1; 
		
		// move trump
		if(getTrumpSuit() != null)
		{
			lSuitLedIndex--;
			for(int i = 0; i < lSuitOrder.length; i++)
			{
				if(lSuitOrder[i].equals(getTrumpSuit()))
				{
					Suit tmp = lSuitOrder[lTrumpSuitIndex];
					lSuitOrder[lTrumpSuitIndex] = getTrumpSuit();
					lSuitOrder[i] = tmp;
				}
			}
		} 
		
		// move suit led 
		if(!getTrumpSuit().equals(getSuitLed()) || getTrumpSuit() == null)
		{
			for(int i = 0; i < lSuitOrder.length; i++)
			{
				if(lSuitOrder[i].equals(getSuitLed()))
				{
					Suit tmp = lSuitOrder[lSuitLedIndex];
					lSuitOrder[lSuitLedIndex] = getSuitLed();
					lSuitOrder[i] = tmp;
				}
			}
		}

		return new Card.GenericBySuitComparator(lSuitOrder, getTrumpSuit() != null);
	}
}
