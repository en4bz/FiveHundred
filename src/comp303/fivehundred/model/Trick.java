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
@SuppressWarnings("serial")
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
	public int winnerIndex()
	{
		Card lHighest = highest();
		return indexOf(lHighest);
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
			lSuitOrder = moveUp(lSuitOrder, getTrumpSuit(), lTrumpSuitIndex);
		} 
		
		// move suit led
		if(!jokerLed() && (getTrumpSuit() == null || (getTrumpSuit() != null && !getTrumpSuit().equals(getSuitLed()))))
		{
			lSuitOrder = moveUp(lSuitOrder, getSuitLed(), lSuitLedIndex);
		}
		
		return new Card.GenericBySuitComparator(lSuitOrder, getTrumpSuit() != null);
	}
	
	/**
	 * Helper method to move a given suit up the array to new position.
	 */
	private Suit[] moveUp(Suit[] pSuitArray, Suit pSuit, int pNewPosition)
	{
		// get index of pSuit
		int lOldPosition;
		for(lOldPosition = 0; lOldPosition < pSuitArray.length; lOldPosition++)
		{
			if(pSuitArray[lOldPosition].equals(pSuit))
			{
				break;
			}
		}
		
		if (lOldPosition < pNewPosition)
		{
			for(int i = lOldPosition; i < pNewPosition; i++)
			{
				pSuitArray[i] = pSuitArray[i+1];
			}
			pSuitArray[pNewPosition] = pSuit;
		}
		return pSuitArray;
	}
}
