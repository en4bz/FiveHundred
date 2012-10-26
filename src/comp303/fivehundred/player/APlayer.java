package comp303.fivehundred.player;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

public abstract class APlayer
{ 
    private Hand aHand;
    private String aName;
    
    protected APlayer(String pName)
    {
        aName = pName;
        aHand = new Hand();
    }
    
    public Hand getHand()
    {
        return aHand.clone();
    }

    public void setHand(Hand pHand){
        aHand = pHand.clone();
    }
    
    public String getName()
    {
        return aName;
    }
    
    // to implement
    abstract public CardList exchange(Bid[] pBids, int pIndex, Hand aWidow);
    abstract public Bid selectBid(Bid[] pPreviousBids);
    abstract public Card play(Trick pTrick);
}