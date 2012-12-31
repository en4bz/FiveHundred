package comp303.fivehundred.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;

import comp303.fivehundred.util.Card.Suit;


/**
 * @author Eleyine Zarour & Ian Forbes
 * Represents a bid or a contract for a player. Immutable.
 */
public class Bid implements Comparable<Bid>
{
	private static final int NUM_SUITS = Suit.values().length + 1;
	private static final int MINTRICKS = 6;
	private static final int MAXTRICKS = 10;
	
	private final int aTricks;
	private final Suit aSuit;
	
	/**
	 * Constructs a new standard bid (not a pass) in a trump.
	 * @param pTricks Number of tricks bid. Must be between 6 and 10 inclusive.
	 * @param pSuit The suit the bid is in, or null if it is in no-trump.
	 * @pre pTricks >= 6 && pTricks <= 10
	 */
	public Bid(int pTricks, Suit pSuit){
		assert pTricks >= MINTRICKS && pTricks <= MAXTRICKS;
		aTricks = pTricks;
		aSuit = pSuit;
	}
	
	/**
	 * Constructs a new passing bid.
	 */
	public Bid(){
		aTricks = 0;
		aSuit = null;
	}
	
	/**
	 * Creates a bid from an index value between 0 and 24 representing all possible
	 * bids in order of strength.
	 * @param pIndex 0 is the weakest bid (6 spades), 24 is the highest (10 no trump).
	 * @pre pIndex >= 0 && pIndex <= 24
	 */
	public Bid(int pIndex){
		Suit[] lSuits = {Suit.SPADES, Suit.CLUBS, Suit.DIAMONDS, Suit.HEARTS, null};
		assert pIndex >= 0;
		assert pIndex <= ((MAXTRICKS - MINTRICKS + 1) * Suit.values().length); // 24
		aSuit =	lSuits[pIndex % NUM_SUITS];
		aTricks = pIndex / lSuits.length + MINTRICKS;
	}
	
	/**
	 * Get the suit the bid is in.
	 * @return The suit the bid is in, or null if it is in no-trump.
	 * @throws ModelException if the bid is a pass.
	 */
	public Suit getSuit(){
		if(isPass()){
			throw new ModelException("Cannot get the suit of a passing bid.");
		}
		return aSuit;
	}
	
	/**
	 * Get the number of tricks in the bid.
	 * @return The number of tricks in the bid.
	 * @throws ModelException if the bid is a pass.
	 */
	public int getTricksBid(){
		if(isPass()){
			throw new ModelException("Cannot get the number of tricks in a passing bid.");
		}
		return aTricks;
	}
	
	/**
	 * @return True if this is a passing bid.
	 */
	public boolean isPass(){
		return (this.aTricks == 0);
	}
	
	/**
	 * @return True if the bid is in no trump.
	 * @throws ModelException if the bid is a pass.
	 */
	public boolean isNoTrump(){
		if(isPass()){
			throw new ModelException("Cannot check if a passing bid is no-trump.");			
		}
		if(aSuit == null){
			return true;
		}
		return false;
	}

	@Override
	/**
	 * Compare bids according to the score table (see game rules). A passing bid is lower than all bids 
	 * (is considered to have an unofficial score of 0).
	 * @pre pBid != null
	 * @return Returns a negative integer, zero, or a positive integer 
	 * if this bid's score is less than, equal to, or greater than pBid's score.
	 */
	public int compareTo(Bid pBid)	{
		assert pBid != null;
		int lBidScore = 0;
		int lThisScore = 0;
		if(!pBid.isPass())	{
			lBidScore = pBid.getScore();
		}
		if(!this.isPass()){
			lThisScore = this.getScore();
		}
		return lThisScore - lBidScore;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 * {@inheritDoc}
	 */
	@Override
	public String toString(){
		if(isPass()){
			return "PASS";
		} 
		else{
			if(aSuit != null){
				return "" + aTricks + " of " + aSuit.toString();
			}
			else{
				return "" + aTricks + " of NO TRUMP";
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object pBid){
		if(!(pBid instanceof Bid) ){
			return false;
		}
		Bid lBid = (Bid) pBid;
		if(this.aTricks == 0 && lBid.aTricks == 0){ //Both Passes
			return true;
		}
		else if(this.aTricks == lBid.aTricks && this.aSuit == lBid.aSuit){
			return true;
		}
		else{
			return false;
		}
	}

	/**
	 * @see java.lang.Object#hashCode()
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode(){
		if(isPass()){
			return (MAXTRICKS - MINTRICKS + 1) * NUM_SUITS; // 25
		}
		else{
			return this.toIndex();
		}
	}

	/**
	 * Converts this bid to an index in the 0-24 range.
	 * @return 0 for a bid of 6 spades, 24 for a bid of 10 no-trump,
	 * and everything in between.
	 * @throws ModelException if this is a passing bid.
	 */
	public int toIndex(){
		if(isPass()){
			throw new ModelException("Cannot get index of a passing bid.");
		}		
		return (aTricks - MINTRICKS) * NUM_SUITS + getSuitIndex();
	}
	
	/**
	 * Returns the highest bid in pBids. If they are all passing bids, returns pass. 
	 * @param pBids The bids to compare.
	 * @return the highest bid.
	 */
	public static Bid max(Bid[] pBids){
		try{
			return Collections.max(Arrays.asList(pBids));
		}
		catch(NoSuchElementException e){
			return new Bid();
		}
	}
	
	/**
	 * @return The score associated with this bid.
	 * @throws ModelException if the bid is a pass.
	 */
	public int getScore(){
		if(isPass()){
			throw new ModelException("Cannot get score of passing bid.");
		}
		return (aTricks - MINTRICKS) * 100 + getSuitIndex() * 20 + 40;
	}
	
	/**
	 * Helper function which returns the index of the suit according to the fivehundred rules in
	 * the following order { SPADES, CLUBS, DIAMONDS, HEARTS, NO-TRUMP}.
	 * @return the index of pSuit according to the fivehundred rules.
	 */
	private int getSuitIndex(){
		int lIndex;
		Suit[] lSuits = {Suit.SPADES, Suit.CLUBS, Suit.DIAMONDS, Suit.HEARTS, null};
		for(lIndex = 0; lIndex < lSuits.length; lIndex++){
			if(lSuits[lIndex] == aSuit){
				return lIndex;
			}
		}
		return lIndex;
	}
}