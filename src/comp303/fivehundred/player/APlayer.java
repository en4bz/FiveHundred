package comp303.fivehundred.player;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

/**
 * Collects the common behavior necessary for all players (both human and robot).
 * @author Eleyine
 *
 */
public abstract class APlayer
{ 
    private Hand aHand;
    private String aName;
    
    /**
     * Constructs a new player.
     * @param pName the player's name
     */
    protected APlayer(String pName)
    {
        aName = pName;
        aHand = new Hand();
    }
    
    /**
     * Gets the player's hand such that the method has no side-effects.
     * @return a clone of the player's hand
     */
    public Hand getHand()
    {
        return aHand.clone();
    }

    /**
     * Sets the player's hand to a copy of pHand. 
     * @param pHand the player's new hand
     */
    public void setHand(Hand pHand)
    {
        aHand = pHand.clone();
    }
    
    /**
     * Gets the player's name.
     * @return the player's name
     */
    public String getName()
    {
        return aName;
    }
    
    @Override
    /**
     * Returns true if pPlayer have equal names and the same reference.
     * @return true if both players share the same name and reference.
     */
    public boolean equals(Object pPlayer)
    {
    	boolean lReturn = false;
    	if (pPlayer != null && pPlayer instanceof APlayer)
    	{
    		APlayer lPlayer = (APlayer) pPlayer;
    		lReturn = aName.equals(lPlayer.getName()) && this == pPlayer;
    	}
    	return lReturn;
    }
    
    @Override
    public int hashCode()
    {
    	return getName().hashCode();
    }
    
    @Override
    /**
     * Returns string representation of the player: his name and his hand.
     * @return a string representation of the player
     */
    public String toString()
    {
    	return getName() + " [" + getHand() + "]";
    }
    
	/**
	 * Select exactly 6 cards from the 16 cards in pWidow and the player's hand
	 * to discard.
	 * @param pBids The bids for this round. An array of 4 elements.
	 * @param pIndex The index of the player in this round. Between 0 and 3 incl.
	 * @param pWidow The widow in this deal.
	 * @return Six cards to remove from the hand + widow.
	 * @pre A least one bid in pBids is non-passing.
	 * @pre pBids.length == 4
	 * @pre pIndex >= 0 && pIndex <= 3
	 * @pre pWidow.size() == 6
	 */
    public abstract CardList exchange(Bid[] pBids, int pIndex, Hand pWidow);
    
	/**
	 * Produces a valid bid, i.e., a between 6 and 10 for any suit or
	 * no trump, that is higher that the last bid or a pass.
	 * @param pPreviousBids All the previous bids for this hand, in order. The
	 * size of the array is the number of bids already entered (between 0 and 3).
	 * @return A valid bid (higher than the last bid, or pass).
	 * @pre pPreviousBids.length <= 4
	 */ 
    public abstract Bid selectBid(Bid[] pPreviousBids);
    
	/**
	 * Selects a card to be played by the player and removes it from the player's hand.
	 * @param pTrick Cards played so far in the trick. Note that the 
	 * number of cards in pTrick determines the playing order of the player. For 
	 * example, if pTrick.size() == 0, the player leads. If pTrick.size() == 1, he plays
	 * second, etc.
	 * @return One of the cards in pHand. The card must be a legal play, that is, follow suit
	 * if possible.
	 * @pre pTrick != null
	 */
    public abstract Card play(Trick pTrick);
}