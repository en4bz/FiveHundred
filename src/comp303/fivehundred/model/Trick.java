package comp303.fivehundred.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;


/**
 * A card list specialized for handling cards discarded
 * as part of the play of a trick.
 */
public class Trick extends CardList
{	
	private Suit aTrump;
	
	/**
	 * Constructs a new empty trick for the specified contract.
	 * @pre !pContract.isPass()
	 * @pre pContract != null
	 * @param pContract The contract that this trick is played for.
	 */
	public Trick(Bid pContract)
	{
		assert pContract != null;
		assert !pContract.isPass();
		aTrump = pContract.getSuit();
	}
	
	/**
	 * @return Can be null for no-trump.
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
		//TODO: does it make sense to use EffectiveSuit here
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
		Card lReturn;
		ArrayList<Card> lCardList = new ArrayList<Card>();
		for(Card c: this)
		{
			lCardList.add(c);
		}
		// TODO: find a way to make a Collection out of "this".
		if(aTrump == null)
		{
			lReturn = Collections.max(lCardList, new Card.BySuitNoTrumpComparator(getSuitLed()));
		}
		else 
		{
			lReturn = Collections.max(lCardList, new Card.BySuitComparator(getTrumpSuit(), getSuitLed()));
		}
		return lReturn;
	}
	
	/**
	 * @return The index of the card that wins the trick.
	 */
	public int winnerIndex()
	{
		Card lHighest = highest();
		int lIndex = 0;
		Iterator<Card> lIter = iterator();
		while(lIter.hasNext())
		{
			if(lIter.next().equals(lHighest))
			{
				break;
			}
			lIndex++;
		}
		return lIndex;
	}
}
