package comp303.fivehundred.util;

import java.util.Comparator;

/**
 * @author Eleyine
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
	 */
	public int compareTo(Card pCard)
	{
		return 0; // TODO
	}

	/**
	 * Two cards are equal if they have the same suit and rank or if they 
	 * are two jokers of the same value.
	 * @param pCard The card to test.
	 * @return true if the two cards are equal
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( Object pCard ) 
	{
		return false; // TODO
	}

	/** 
	 * The hashcode for a card is the suit*number of ranks + that of the rank (perfect hash).
	 * @return the hashcode
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() 
	{
		return 0; // TODO
	}
	
	/**
	 * Compares cards using their rank as primary key, then suit. Jacks
	 * rank between 10 and queens of their suit.
	 */
	public static class ByRankComparator implements Comparator<Card>
	{
		@Override
		public int compare(Card pCard1, Card pCard2)
		{
			return 0; // TODO
		}
		
	}
	
	/**
	 * Compares cards using their suit as primary key, then rank. Jacks
	 * rank between 10 and queens of their suit.
	 */
	public static class BySuitNoTrumpComparator implements Comparator<Card>
	{
		@Override
		public int compare(Card pCard1, Card pCard2)
		{
			return 0; // TODO
		}
	}
	
	/**
	 * Compares cards using their suit as primary key, then rank. Jacks
	 * rank above aces if they are in the trump suit. The trump suit becomes the
	 * highest suit.
	 */
	public static class BySuitComparator implements Comparator<Card>
	{
		@Override
		public int compare(Card pCard1, Card pCard2)
		{
			return 0; // TODO
		}
	}
}
