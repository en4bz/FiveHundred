package comp303.fivehundred.ai;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Rank;
import comp303.fivehundred.util.Card.Suit;

public class BasicsBiddingStrategyTest
{
	private BasicBiddingStrategy aStrategy = new BasicBiddingStrategy();
	
	@Test
	public void testObviousBid()
	{
		Bid[] lBids = {new Bid(),new Bid(),new Bid(),new Bid()};
		for(Suit lSuit : Suit.values()){
			Hand lHand = new Hand();
			for(Rank lRank : Rank.values())  
			{
				lHand.add( new Card( lRank, lSuit));
			}
			Bid lTest = aStrategy.selectBid(lBids, lHand);
			assertTrue(lTest.getSuit().equals(lSuit));
		}
	}
	
	@Test
	public void testPassingBid()
	{
		Bid[] lBids = {new Bid(10,null),new Bid(10,null),new Bid(10,null),new Bid(10,null),};
		for(Suit lSuit : Suit.values()){
			Hand lHand = new Hand();
			for(Rank lRank : Rank.values())  
			{
				lHand.add( new Card( lRank, lSuit));
			}
			Bid lTest = aStrategy.selectBid(lBids, lHand);
			assertTrue(lTest.equals(new Bid()));//Should be a pass
		}	
	}
	
	@Test
	public void testBid()
	{
		
	}
}
