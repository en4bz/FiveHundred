package comp303.fivehundred.model;

import java.util.Comparator;
import java.util.Iterator;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;

/**
 * @author Ian Forbes
 * Additional services to manage a card list that corresponds to
 * the cards in a player's hand.
 */
public class Hand extends CardList
{
	/**
	 * @see java.lang.Object#clone()
	 * {@inheritDoc}
	 */
	@Override
	public Hand clone()
	{
		return (Hand) super.clone();
	}
	
	/**
	 * @param pNoTrump If the contract is in no-trump
	 * @return A list of cards that can be used to lead a trick.
	 */
	public CardList canLead(boolean pNoTrump)
	{
		CardList lReturn =  new CardList();
		
		// check if there are only jokers in the hand
		boolean lOnlyJokers = true;
		for(Card c: this)
		{
			if(!c.isJoker())
			{
				lOnlyJokers = false;
				break;
			}
		}
		
		// cannot lead with a joker in no-trump unless there are only jokers
		for(Card c: this)
		{
			if(!pNoTrump || (pNoTrump && !c.isJoker()) || lOnlyJokers)
			{
				lReturn.add(c);
			}
		}
		return lReturn;
	}
	
	/**
	 * @return The cards that are jokers.
	 */
	public CardList getJokers()
	{
		CardList lReturn = new CardList();
		for(Card c: this)
		{
			if(c.isJoker())
			{
				lReturn.add(c);
			}
		}
		return lReturn;
	}
	
	/**
	 * @return The cards that are not jokers.
	 */
	public CardList getNonJokers()
	{
		CardList lReturn = new CardList();
		for(Card c: this)
		{
			if(!c.isJoker())
			{
				lReturn.add(c);
			}
		}
		return lReturn;
	}
	
	/**
	 * Returns all the trump cards in the hand, including jokers.
	 * Takes jack swaps into account.
	 * @param pTrump The trump to check for. Cannot be null.
	 * @return All the trump cards and jokers.
	 * @pre pTrump != null
	 */
	public CardList getTrumpCards(Suit pTrump)
	{
		assert pTrump != null;
		CardList lReturn = new CardList();
		for(Card c: this)
		{
			if(c.isJoker() || c.getEffectiveSuit(pTrump).equals(pTrump))
			{
				lReturn.add(c);
			}
		}
		return lReturn;
	}
	
	/**
	 * Returns all the cards in the hand that are not trumps or jokers.
	 * Takes jack swaps into account.
	 * @param pTrump The trump to check for. Cannot be null.
	 * @return All the cards in the hand that are not trump cards.
	 * @pre pTrump != null
	 */
	public CardList getNonTrumpCards(Suit pTrump)
	{
		assert pTrump != null;
		CardList lReturn = new CardList();
		for(Card c: this)
		{
			if(!c.isJoker() && !c.getEffectiveSuit(pTrump).equals(pTrump))
			{
				lReturn.add(c);
			}
		}
		return lReturn;
	}
	
	
	/**
	 * Selects the least valuable card in the hand, if pTrump is the trump.
	 * @param pTrump The trump suit. Can be null to indicate no-trump.
	 * @return The least valuable card in the hand.
	 */
	public Card selectLowest(Suit pTrump)
	{

		CardList lList = sort(new Card.ByRankComparator());
		Card lReturn = lList.getFirst();
		if(pTrump != null)
		{
			for(Card c: lList)
			{
				if(!c.isJoker() && !c.getEffectiveSuit(pTrump).equals(pTrump))
				{
					lReturn = c;
					break;
				}	
			}
		}
		return lReturn;
	}
	
	/**
	 * @param pLed The suit led.
	 * @param pTrump Can be null for no-trump
	 * @return All cards that can legally be played given a lead and a trump.
	 */
	public CardList playableCards( Suit pLed, Suit pTrump )
	{
		CardList lReturn = new CardList();
		for(Card c: this)
		{
			if(c.isJoker() || c.getSuit().equals(pLed) || c.getEffectiveSuit(pTrump).equals(pTrump))
			{
				lReturn.add(c);
			}
		}
		return lReturn;
	}
	
	/**
	 * Returns the number of cards of a certain suit 
	 * in the hand, taking jack swaps into account.
	 * Excludes jokers.
	 * @param pSuit Cannot be null.
	 * @param pTrump Cannot be null.
	 * @return pSuit Can be null.
	 */
	public int numberOfCards(Suit pSuit, Suit pTrump)
	{
		assert pSuit != null;
		assert pTrump != null;
		int lCounter = 0;
		for(Card c: this)
		{
			if (!c.isJoker() && c.getEffectiveSuit(pTrump).equals(pSuit))
			{
				lCounter++;
			}
		}
		return lCounter;
	}
}
