package comp303.fivehundred.ai;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.Card.Suit;

public class AdvancedBiddingStrategy extends BasicBiddingStrategy implements IBiddingStrategy
{
	
	public AdvancedBiddingStrategy()
	{
		
		super();
	
	}
	
	@Override
	public Bid selectBid(Bid[] pPreviousBids, Hand pHand)
	{
		
		return super.selectBid(pPreviousBids, pHand);
		
	}

}
