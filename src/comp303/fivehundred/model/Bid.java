package comp303.fivehundred.model;

import java.util.Arrays;
import java.util.Collections;

import comp303.fivehundred.util.Card.Suit;

/**
 * Represents a bid or a contract for a player. Immutable.
 */
public class Bid implements Comparable<Bid>
{
	
	private int aTricks;
	private Suit aSuit;
	private boolean aPass;
	private final Suit[] aSUITS = { Suit.SPADES, Suit.CLUBS, Suit.DIAMONDS, Suit.HEARTS, null};
	private final int aMINTRICKS = 6;
	private final int aMAXTRICKS = 10;
	
	
	/**
	 * Constructs a new standard bid (not a pass) in a trump.
	 * @param pTricks Number of tricks bid. Must be between 6 and 10 inclusive.
	 * @param pSuit Suit bid. 
	 * @pre pTricks >= 6 && pTricks <= 10
	 */
	public Bid(int pTricks, Suit pSuit)
	{
		assert pTricks >= aMINTRICKS;
		assert pTricks <= aMAXTRICKS;
		aTricks = pTricks;
		aSuit = pSuit;
		aPass = false;
	}
	
	/**
	 * Constructs a new passing bid.
	 */
	public Bid()
	{
		aTricks = 0;
		aSuit = null;
		aPass = true;
	}
	
	/**
	 * Creates a bid from an index value between 0 and 24 representing all possible
	 * bids in order of strength.
	 * @param pIndex 0 is the weakest bid (6 spades), 24 is the highest (10 no trump),
	 * and everything in between.
	 * @pre pIndex >= 0 && pIndex <= 24
	 */
	public Bid(int pIndex)
	{
		assert pIndex >= 0;
		assert pIndex <= ((aMAXTRICKS - aMINTRICKS + 1) * aSUITS.length - 1);
		aSuit =	aSUITS[pIndex % aSUITS.length];
		aTricks = pIndex / aSUITS.length + aMINTRICKS;
		aPass = false;
	}
	
	/**
	 * @return The suit the bid is in, or null if it is in no-trump.
	 * @throws ModelException if the bid is a pass.
	 */
	public Suit getSuit()
	{
		if(aPass)
		{
			throw new ModelException("Cannot get the suit of a passing bid.");
		}
		return aSuit;
	}
	
	/**
	 * @return The number of tricks bid.
	 * @throws ModelException if the bid is a pass.
	 */
	public int getTricksBid()
	{
		if(aPass)
		{
			throw new ModelException("Cannot get the number of tricks of a passing bid.");
		}
		return aTricks;
	}
	
	/**
	 * @return True if this is a passing bid.
	 */
	public boolean isPass()
	{
		return aPass;
	}
	
	/**
	 * @return True if the bid is in no trump.
	 */
	public boolean isNoTrump()
	{
		boolean lReturn = false;
		if(!aPass && aSuit == null)
		{
			lReturn = true;
		}
		return lReturn;
	}

	@Override
	// TODO: how does a passing bid compare to a non-passing bid?
	public int compareTo(Bid pBid)
	{
		return this.toIndex() - pBid.toIndex();
	}
	
	/**
	 * @see java.lang.Object#toString()
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		if(aSuit != null)
		{
			return "" + aTricks + " of " + aSuit.toString();
		}
		else
		{
			return "" + aTricks + " of NO TRUMP";
		}
	}

	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object pBid)
	{
		return pBid instanceof Bid && this.hashCode() == pBid.hashCode();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		if(aPass)
		{
			return (aMAXTRICKS - aMINTRICKS + 1) * aSUITS.length;
		}
		else
		{
			return this.toIndex();
		}
	}

	/**
	 * Converts this bid to an index in the 0-24 range.
	 * @return 0 for a bid of 6 spades, 24 for a bid of 10 no-trump,
	 * and everything in between.
	 * @throws ModelException if this is a passing bid.
	 */
	public int toIndex()
	{
		if(aPass)
		{
			throw new ModelException("Cannot get index of a passing bid.");
		}		
		return (aTricks - aMINTRICKS) * aSUITS.length + getSuitIndex();
	}
	
	/**
	 * Returns the highest bid in pBids. If they are all passing
	 * bids, returns pass.
	 * @param pBids The bids to compare.
	 * @return the highest bid.
	 */
	public static Bid max(Bid[] pBids)
	{
		Bid lReturn;
		boolean lAllPass = true;
		for(Bid b: pBids)
		{
			if(b.isPass())
			{
				lAllPass = false;
				break;
			}
		}
		if(lAllPass)
		{
			lReturn = new Bid();
		}
		else
		{
			lReturn = Collections.max(Arrays.asList(pBids));
		}
		return lReturn;
	}
	
	/**
	 * @return The score associated with this bid.
	 * @throws ModelException if the bid is a pass.
	 */
	public int getScore()
	{
		if(aPass)
		{
			throw new ModelException("Cannot get score of passing bid.");
		}
		return (aTricks - aMINTRICKS) * 100 + getSuitIndex() * 20 + 40;
	}
	
	/**
	 * Helper function which returns the index of the suit according to the fivehundred rules.
	 * @return the index of pSuit according to the fivehundred rules.
	 */
	private int getSuitIndex()
	{
		int lIndex;
		for(lIndex = 0; lIndex < aSUITS.length; lIndex++)
		{
			if(aSUITS[lIndex] == aSuit)
			{
				break;
			}
		}
		return lIndex;
	}
}
