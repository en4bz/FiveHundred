package comp303.fivehundred.player;

import comp303.fivehundred.ai.IRobotPlayer;
import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;
/**
 * Collects all the behavior necessary to serve as a complete
 * robot player. Harmonizes IRobotPlayer with the APlayer class
 * so that the client can use common methods with HumanPlayer. 
 * @author Eleyine Zarour
 *
 */
public abstract class ARobotPlayer extends APlayer implements IRobotPlayer
{
	/**
	 * Constructs new robot player object.
	 * @param pName the robot's name
	 */
	public ARobotPlayer(String pName)
	{
		super(pName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CardList exchange(Bid[] pBids, int pIndex, Hand pWidow)
    {
		// the rest of the assertion checks are passed on to selectCardsToDiscard
		assert pWidow.size() == 6; 

        for(Card c: pWidow)
        {
            this.addCardToHand(c);
        }
        CardList lDiscarded = selectCardsToDiscard(pBids, pIndex, this.getHand());
        for(Card c: lDiscarded)
        {
            this.removeCardFromHand(c);   
        }
        return lDiscarded;
    }
    

	/**
	 * {@inheritDoc}
	 */ 
    public Bid selectBid(Bid[] pPreviousBids)
    {
    	assert pPreviousBids.length <=4;
        return selectBid(pPreviousBids, getHand());  
    }

    /**
     * {@inheritDoc} 
     */
    public Card play(Trick pTrick)
    {
    	assert pTrick != null;
    	
		Card lPlayed = play(pTrick, getHand());
		this.removeCardFromHand(lPlayed);
		return lPlayed;
    }    
}
