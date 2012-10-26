package comp303.fivehundred.util;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author Eleyine Zarour
 * An immutable description of a playing card.
 */
public final class Card implements Comparable<Card>
{
	/**
	 * Represents the rank of the card.
	 */
	public enum Rank 
	{ FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE }
	
	/**
	 * Represents the suit of the card.
	 */
	public enum Suit 
	{ SPADES, CLUBS, DIAMONDS, HEARTS; 
		
		/**
		 * @return the other suit of the same color. 
		 */
		public Suit getConverse()
		{
			Suit lReturn = this;
			switch(this) 
			{
				case SPADES: lReturn = CLUBS; break;
				case CLUBS:  lReturn = SPADES; break;
				case DIAMONDS: lReturn = HEARTS; break;
				case HEARTS: lReturn = DIAMONDS; break;
				default: lReturn = this;
			}
			return lReturn;
		}
	}
	
	/**
	 * Represents the value of the card, if the card is a joker.
	 */
	public enum Joker
	{ LOW, HIGH }
	
	// If this field is null, it means the card is a joker, and vice-versa.
	private final Rank aRank;

	// If this field is null, it means the card is a joker, and vice-versa.
	private final Suit aSuit;
	
	// If this field is null, it means the card is not a joker, and vice-versa.
	private final Joker aJoker;
	
	/**
	 * Create a new card object that is not a joker. 
	 * @param pRank The rank of the card.
	 * @param pSuit The suit of the card.
	 * @pre pRank != null
	 * @pre pSuit != null
	 */
	public Card( Rank pRank, Suit pSuit )
	{
		assert pRank != null;
		assert pSuit != null;
		aRank = pRank;
		aSuit = pSuit;
		aJoker = null;
	}
	
	/**
	 * Creates a new joker card.
	 * @param pValue Whether this is the low or high joker.
	 * @pre pValue != null
	 */
	public Card( Joker pValue )
	{
		assert pValue != null;
		aRank = null;
		aSuit = null;
		aJoker = pValue;
	}
	
	/**
	 * @return True if this Card is a joker, false otherwise.
	 */
	public boolean isJoker()
	{
		return aJoker != null;
	}
	
	/**
	 * @return Whether this is the High or Low joker.
	 */
	public Joker getJokerValue()
	{
		assert isJoker();
		return aJoker;
	}
	
	/**
	 * Obtain the rank of the card.
	 * @return An object representing the rank of the card. Can be null if the card is a joker.
	 * @pre !isJoker();
	 */
	public Rank getRank()
	{
		assert !isJoker();
		return aRank;
	}
	
	/**
	 * Obtain the suit of the card.
	 * @return An object representing the suit of the card 
	 * @pre !isJoker();
	 */
	public Suit getSuit()
	{
		assert !isJoker();
		return aSuit;
	}
	
	/**
	 * Returns the actual suit of the card if pTrump is the
	 * trump suit. Takes care of the suit swapping of jacks.
	 * @param pTrump The current trump. Null if no trump.
	 * @return The suit of the card, except if the card is a Jack
	 * and its converse suit is trump. Then, returns the trump.
	 */
	public Suit getEffectiveSuit( Suit pTrump )
	{
		if( pTrump == null )
		{
			return aSuit;
		}
		else if( aRank == Rank.JACK && aSuit == pTrump.getConverse())
		{
			return pTrump;
		}
		else
		{
			return aSuit;
		}
	}
	
	/**
	 * @see java.lang.Object#toString()
	 * @return See above.
	 */
	@Override
	public String toString()
	{
		if( !isJoker() )
		{
			return aRank + " of " + aSuit;
		}
		else
		{
			return aJoker + " " + "Joker";
		}
	}
	
	/**
	 * @return A short textual representation of the card
	 */
	public String toShortString()
	{
		String lReturn = "";
		if( isJoker() )
		{
			lReturn = aJoker.toString().charAt(0) + "J";
		}
		else
		{
			if( aRank.ordinal() <= Rank.NINE.ordinal() )
			{
				lReturn += new Integer(aRank.ordinal() + 4).toString();
			}
			else
			{
				lReturn += aRank.toString().charAt(0);
			}
			lReturn += aSuit.toString().charAt(0);
		}
		return lReturn;
	}

	/**
	 * Compares two cards according to their rank.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * @param pCard The card to compare to
	 * @return Returns a negative integer, zero, or a positive integer 
	 * as this object is less than, equal to, or greater than pCard
	 * @pre pCard != null
	 */
	@Override
	public int compareTo(Card pCard)
	{
		assert pCard != null;
		int lReturn = 0;
		if (pCard.isJoker() && this.isJoker()) 
		{
			lReturn = this.getJokerValue().compareTo(pCard.getJokerValue());
		} 
		else if (!pCard.isJoker() && !this.isJoker()) 
		{
			lReturn = this.getRank().compareTo(pCard.getRank());
		} 
		else if(!pCard.isJoker() && this.isJoker()) 
		{
			lReturn = 1;
		} 
		else //pCard.isJoker() && !this.isJoker()
		{
			lReturn = -1;
		}
		return lReturn;
	}

	/**
	 * Two cards are equal if they have the same suit and rank or if they 
	 * are two jokers of the same value.
	 * @param pCard The card to test.
	 * @return true if the two cards are equal in suit and rank
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( Object pCard ) 
	{
		assert pCard != null;
		boolean lReturn = false;
		
		if ( !(pCard instanceof Card) )
		{
			return false;
		}
		
	    //cast to native object is now safe
		Card lCard = (Card)pCard;
		
		if(isJoker() && lCard.isJoker()) 
		{
			lReturn = this.getJokerValue().equals(lCard.getJokerValue());
		}
		if(!isJoker() && !lCard.isJoker() && getRank().equals(lCard.getRank()))
		{
			lReturn = this.getSuit().equals(lCard.getSuit());
		}
		return lReturn;
	}

	/** 
	 * The hashcode for a card is the suit*number of ranks + that of the rank (perfect hash). 
	 * If the card is a joker, apply the formula with the maximal Suit and Rank ordinal value 
	 * + the Joker ordinal value.
	 * @return the hashcode
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() 
	{
		int lReturn;
		int numRank = Rank.values().length;
		int numSuit = Suit.values().length;
		if(this.isJoker()) 
		{
			lReturn = numSuit * numRank + this.getJokerValue().ordinal();
		}
		else
		{
			lReturn = this.getSuit().ordinal() * numRank + this.getRank().ordinal();
		}
		return lReturn;
	}
	
	/**
	 * Compares cards using their rank as primary key, then suit. Jacks
	 * rank between 10 and queens of their suit.
	 */
	public static class ByRankComparator implements Comparator<Card>
	{	
		/**
		 * Compares two cards by rank then suit. 
		 * @param pCard1 the first card to compare
		 * @param pCard2 the second card to compare
		 * TODO: check if you can add the following preconditions
		 * @pre pCard1 != null
		 * @pre pCard2 != null
		 * @return Returns a negative integer, zero, or a positive integer 
		 * as pCard1 is less than, equal to, or greater than pCard2 by rank
		 * then by suit. Jacks rank between 10 and queens of their suit.
		 * */
		@Override
		public int compare(Card pCard1, Card pCard2)
		{
			assert pCard1 != null;
			assert pCard2 != null;
			int lReturn = pCard1.compareTo(pCard2);
			if (lReturn == 0 && !pCard1.equals(pCard2)) 
			// Note: If at least one of the cards is a joker, the only case where 
			// lReturn == 0 is when the cards are equal. Hence in this condition
			// block, all cards are not jokers and getSuit() can be called.
			{
				lReturn = pCard1.getSuit().compareTo(pCard2.getSuit());
			}
			return lReturn;
		}
		
	}
	
	/**
	 * Compares cards using their suit as primary key, then rank such that suit 
	 * order is defined by the user. Jacks rank between 10 and queens of their suit if there is no trump.
	 * Otherwise, they rank above aces.
	 *
	 */
	public static class GenericBySuitComparator implements Comparator<Card> 
	{
		private Suit aTrump = null;
		private Suit[] aSuitOrder;
		
		/**
		 * Creates a Comparator object which compares Cards by Suit according to the 
		 * order described in pSuitOrder. The first Suit may or may not be a Trump.
		 * @param pSuitOrder an array of Suits in ascending ranking order
		 * @param pIsFirstSuitTrump whether or not the first Suit in pSuitOrder is a trump
		 * @pre pSuitOrder.length == 4
		 */
		public GenericBySuitComparator(Suit[] pSuitOrder, boolean pIsFirstSuitTrump)
		{
			assert pSuitOrder.length == 4;
			aSuitOrder = pSuitOrder.clone();
			if(pIsFirstSuitTrump)
			{
				aTrump = aSuitOrder[aSuitOrder.length-1];
			}			
		}
		
		@Override
		public int compare(Card pCard1, Card pCard2)
		{
			int lReturn = 0;
			if(pCard1.equals(pCard2))
			{
				return lReturn;
			}
			
			// First, compare by suit.
			if(!pCard1.isJoker() && !pCard2.isJoker())
			{ // both cards are not jokers

				ArrayList<Suit> lSuitOrder = new ArrayList<Suit>();
				for(Suit s: aSuitOrder)
				{
					lSuitOrder.add(0, s);
				}
				Suit lCurSuit;
				
				while(!lSuitOrder.isEmpty())
				{
					// the current highest suit
					lCurSuit = lSuitOrder.remove(0);
					if(!pCard1.getEffectiveSuit(aTrump).equals(lCurSuit) && !pCard2.getEffectiveSuit(aTrump).equals(lCurSuit))
					{ // both cards do not have the current highest suit, try again with the next highest suit
						continue;
					} 
					else if(!pCard1.getEffectiveSuit(aTrump).equals(lCurSuit) && pCard2.getEffectiveSuit(aTrump).equals(lCurSuit))
					{ // card 2 has the highest suit
						lReturn = -1;
						break;
					} 
					else if(pCard1.getEffectiveSuit(aTrump).equals(lCurSuit) && !pCard2.getEffectiveSuit(aTrump).equals(lCurSuit))
					{ // card 1 has the highest suit
						lReturn = 1;
						break;
					} 
					else
					{ // both cards have the current highest suit
						lReturn = 0;
						break;
					}
				}
			}
			else
			{ // at least one card is a Joker
				lReturn = pCard1.compareTo(pCard2);
			}

			// Then, compare by rank.
			if (lReturn == 0) 
			// Note: If at least one of the cards is a joker, the only case where 
			// lReturn == 0 is when the cards are equal. Hence, in this condition
			// block, all cards are not jokers and both getRank() and getSuit() can be called.
			{
				if(pCard1.getEffectiveSuit(aTrump).equals(aTrump))
				{ // trump card ranking
					if(!pCard1.getRank().equals(Rank.JACK) && !pCard2.getRank().equals(Rank.JACK) )
					{
						lReturn = pCard1.getRank().compareTo(pCard2.getRank());
					}
					else
					{ // at least one card is a JACK
						if(pCard1.getRank().equals(Rank.JACK) && pCard2.getRank().equals(Rank.JACK))
						{ // if both cards are JACK and they are not equal, then they must be the right and left bowers
							lReturn = pCard1.getSuit().equals(aTrump)? 1 : -1;
						}
						else
						{
							lReturn = pCard1.getRank().equals(Rank.JACK)? 1 : -1;
						}
					}
				}
				else
				{ // the usual ranking is used for non-trump cards
					lReturn = pCard1.getRank().compareTo(pCard2.getRank());
				}
			}
			return lReturn;
		}
	}
		
	/**
	 * Compares cards using their suit as primary key, then rank. Jacks
	 * rank between 10 and queens of their suit.
	 */
	public static class BySuitNoTrumpComparator implements Comparator<Card>
	{
		private Suit[] aSuitOrder;
		
		/**
		 * Create a Comparator object which compares cards by suit, according to the natural
		 * ordering of the game, then by rank. 
		 */
		public BySuitNoTrumpComparator()
		{
			aSuitOrder = Card.Suit.values().clone();
		}
		
		@Override
		/**
		 * Compare cards according to their suit then rank in a no trump round.
		 * Suit order is the natural order of the game. Jacks rank between 10 and queens of their suit.
		 * @param pCard1 the first card to compare
		 * @param pCard2 the second card to compare
		 * @return Returns a negative integer, zero, or a positive integer 
		 * as pCard1 is less than, equal to, or greater than pCard2 by suit 
		 * then by rank. 
		 * @pre pCard1 != null
		 * @pre pCard2 != null
		 */
		public int compare(Card pCard1, Card pCard2)
		{
			Comparator<Card> lComparator = new Card.GenericBySuitComparator(aSuitOrder, false);
			return lComparator.compare(pCard1, pCard2);
		}
	}
	
	/**
	 * Compares cards using their suit as primary key, then rank. Jacks
	 * rank above aces if they are in the trump suit. The trump suit becomes the
	 * highest suit.
	 */
	public static class BySuitComparator implements Comparator<Card>
	{
		private Suit aTrump;
		private Suit[] aSuitOrder;

		/**
		 * Create a Comparator object which compares cards according to 
		 * their suit then rank in a trump round.
		 * @param pTrump the trump suit of the round 
		 * @pre pTrump != null;
		 */
		public BySuitComparator(Suit pTrump)
		{			
			assert pTrump != null;
			aTrump = pTrump;
			aSuitOrder = Suit.values().clone();
			// move TrumpSuit to the first position
			for(int i = 0; i < aSuitOrder.length; i++)
			{
				int top = aSuitOrder.length - 1;
				if(aSuitOrder[i].equals(aTrump))
				{
					Suit tmp = aSuitOrder[top];
					aSuitOrder[top] = aTrump;
					aSuitOrder[i] = tmp;
				}
			}
		}
		
		@Override
		/**
		 * Compare cards according to their suit then rank. The trump suit becomes the
		 * highest suit. Jokers are higher than all cards.
		 * @param pCard1 the first card to compare
		 * @param pCard2 the second card to compare
		 * @return Returns a negative integer, zero, or a positive integer 
		 * as pCard1 is less than, equal to, or greater than pCard2 by suit 
		 * then by rank. Jacks rank above aces if they are in the trump suit.
		 * @pre pCard1 != null;
		 * @pre pCard2 != null;
		 */
		public int compare(Card pCard1, Card pCard2)
		{
			Comparator<Card> lComparator = new Card.GenericBySuitComparator(aSuitOrder, true);
			return lComparator.compare(pCard1, pCard2);
		}
	}
}
