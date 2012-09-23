package comp303.fivehundred.util;

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
	 * @return true if the two cards are equal
	 * @pre pCard != null
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( Object pCard ) 
	{
		// TODO: ask if this is okay
		assert pCard != null;
		return pCard instanceof Card && this.hashCode() == pCard.hashCode();
		
		/*boolean lReturn = false;
		
		if ( !(pCard instanceof Card) )
		{
			return false;
		}
		
	    //cast to native object is now safe
		Card lCard = (Card)pCard;
		
		if(this.isJoker() && lCard.isJoker() ) 
		{
			lReturn = this.getJokerValue().equals(lCard.getJokerValue());
		}
		if(!this.isJoker() && !lCard.isJoker()) 
		{
			lReturn = this.getRank().equals(lCard.getRank());
		}
		return lReturn;
		*/
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
		// I don't know what purpose this serves. TODO: Look into this.
		//@Override 		
		/**
		 * Compares two cards by rank then suit. Suit order corresponds to enum ordinality.
		 * @param pCard1 the first card to compare
		 * @param pCard2 the second card to compare
		 * @pre pCard1 != null
		 * @pre pCard2 != null
		 * @return Returns a negative integer, zero, or a positive integer 
		 * as pCard1 is less than, equal to, or greater than pCard2 by rank
		 * then by suit. Jacks rank between 10 and queens of their suit.
		 * */
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
	 * Compares cards using their suit as primary key, then rank. Jacks
	 * rank between 10 and queens of their suit.
	 */
	public static class BySuitNoTrumpComparator implements Comparator<Card>
	{
		private Suit aLead;
		/**
		 * Create a Comparator object which compares cards according to 
		 * their suit then rank. Jacks rank between 10 and queens of their suit.
		 * @param pLead the suit of the leading card in the trick
		 * @pre pLead != null
		 */
		public BySuitNoTrumpComparator(Suit pLead)
		{
			assert pLead != null;
			aLead = pLead;
		}
		
		//@Override
		/**
		 * Compare cards according to their suit then rank in a no trump round.
		 * The highest card is the highest card of suit led; if there is no lead
		 * suit, then the highest card of the highest ordinal suit as defined in 
		 * the enum declaration. Jokers are higher than all cards. 
		 * @param pCard1 the first card to compare
		 * @param pCard2 the second card to compare
		 * @return Returns a negative integer, zero, or a positive integer 
		 * as pCard1 is less than, equal to, or greater than pCard2 by rank
		 * then by suit. Jacks rank between 10 and queens of their suit.
		 * @pre pCard1 != null
		 * @pre pCard2 != null
		 */
		public int compare(Card pCard1, Card pCard2)
		{
			assert pCard1 != null;
			assert pCard2 != null;
			int lReturn = 0;
				
			// First compare by Suit.			
			if(!pCard1.isJoker() && !pCard2.isJoker())
			{
				if( ( !pCard1.getSuit().equals(aLead) && !pCard2.getSuit().equals(aLead))
					|| (pCard1.getSuit().equals(aLead) && pCard2.getSuit().equals(aLead))) 
				{
					lReturn = pCard1.getSuit().compareTo(pCard2.getSuit());
				}
				else
				// exactly one card has a the leading suit
				{
					lReturn = (pCard1.getSuit().equals(aLead))? 1 : -1;
				}
			}
			else
			// if at least one card is a joker, then compareTo returns the correct result
			{
				lReturn = pCard1.compareTo(pCard2);
			}
			
			// Then compare by Rank.
			if (lReturn == 0 && !pCard1.equals(pCard2)) 
			// Note: If at least one of the cards is a joker, the only case where 
			// lReturn == 0 is when the cards are equal. Hence, in this condition
			// block all cards are not jokers and getRank() can be called.
			{
				lReturn = pCard1.getRank().compareTo(pCard2.getRank());
			}
			return lReturn;
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
		private Suit aLead;

		/**
		 * Create a Comparator object which compares cards according to 
		 * their suit then rank in a trump round. 
		 * @param pTrump the trump suit of the round 
		 * @param pLead the suit of the leading card in the trick
		 * @pre pTrump != null
		 * @pre pLead != null
		 */
		public BySuitComparator(Suit pTrump, Suit pLead)
		{
			assert pTrump != null;
			assert pLead != null;
			aTrump = pTrump;
			aLead = pLead;
		}
		
		//@Override
		/**
		 * Compare cards according to their suit then rank. The highest card is the highest
		 * trump; if there is no trump, then the highest card of the suit led; if there is no
		 * lead suit, then the highest card of the highest ordinal suit as defined in the enum 
		 * declaration. Jokers are higher than all cards.
		 * @param pCard1 the first card to compare
		 * @param pCard2 the second card to compare
		 * @return Returns a negative integer, zero, or a positive integer 
		 * as pCard1 is less than, equal to, or greater than pCard2.
		 * @pre pCard1 != null
		 * @pre pCard2 != null
		 */
		public int compare(Card pCard1, Card pCard2)
		{
			assert pCard1 != null;
			assert pCard2 != null;
			int lReturn = 0;
			
			if(pCard1.equals(pCard2))
			{
				return 0;
			}
			
			// First compare by Suit.			
			if(!pCard1.isJoker() && !pCard2.isJoker())
			{
				if( ( !pCard1.getEffectiveSuit(aTrump).equals(aTrump) && !pCard2.getEffectiveSuit(aTrump).equals(aTrump)) // both not trump
					|| (pCard1.getEffectiveSuit(aTrump).equals(aTrump) && pCard2.getEffectiveSuit(aTrump).equals(aTrump))) // both trump
				{
					if( ( !pCard1.getEffectiveSuit(aTrump).equals(aLead) && !pCard2.getEffectiveSuit(aTrump).equals(aLead)) // both not lead
							|| (pCard1.getSuit().equals(aLead) && pCard2.getSuit().equals(aLead))) // both lead
					{
						// both trump || both lead || both neither trump nor lead
						lReturn = pCard1.getSuit().compareTo(pCard2.getSuit());
					}
					else
					// exactly one card has the leading suit and no card is a trump
					{
						lReturn = (pCard1.getSuit().equals(aLead))? 1 : -1;
					}
				}
				else
				// exactly one card has the trump suit
				{
					lReturn = (pCard1.getSuit().equals(aTrump))? 1 : -1;
				}
			}
			else
			// if at least one card is a joker, then compareTo returns the correct result
			{
				lReturn = pCard1.compareTo(pCard2);
			}
			
			// Then compare by Rank.
			if (lReturn == 0) 
			// Note: If at least one of the cards is a joker, the only case where 
			// lReturn == 0 is when the cards are equal. Hence, in this condition
			// block, all cards are not jokers and both getRank() and getSuit() can be called.
			{
				if(pCard1.getEffectiveSuit(aTrump).equals(aTrump))
				{	
					if(!pCard1.getRank().equals(Rank.JACK) && !pCard2.getRank().equals(Rank.JACK) )
					{
						lReturn = pCard1.getRank().compareTo(pCard2.getRank());
					}
					else
					// at least one card is a JACK
					{
						if(pCard1.getRank().equals(Rank.JACK) && pCard2.getRank().equals(Rank.JACK))
						// if both cards are JACK and they are not equal, then they must be the right and left bowers
						{
							lReturn = pCard1.getSuit().equals(aTrump)? 1 : -1;
						}
						else
						{
							lReturn = pCard1.getRank().equals(Rank.JACK)? 1 : -1;
						}
					}
				}
				else
				// the usual ranking is used for non-trump cards
				{
					lReturn = pCard1.getRank().compareTo(pCard2.getRank());
				}
			}
			return lReturn;
		}
	}
}
