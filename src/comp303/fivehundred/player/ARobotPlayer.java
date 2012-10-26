package comp303.fivehundred.player;

import comp303.fivehundred.ai.IRobotPlayer;
import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

public abstract class ARobotPlayer extends APlayer implements IRobotPlayer
{
	protected ARobotPlayer(String pName)
	{
		super(pName);
	}
	
	public CardList exchange(Bid[] pBids, int pIndex, Hand pWidow)
    {
		// merge widow and hand together
        Hand lHand = getHand().clone();
        for(Card c: pWidow)
        {
            lHand.add(c);
        }
        CardList lDiscarded = selectCardsToDiscard(pBids, pIndex, lHand);
        for(Card c: lDiscarded)
        {
            lHand.remove(c);   
        }
        this.setHand(lHand);
        return lDiscarded;
    }
    
    public Bid selectBid(Bid[] pPreviousBids)
    {
        return selectBid(pPreviousBids, getHand());  
    }
    
    public Card play(Trick pTrick)
    {
        return play(pTrick, getHand()); 
    }    
}
