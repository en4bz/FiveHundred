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
	public void testBid()
	{
		Bid[] lBids = {new Bid(),new Bid(),new Bid(),new Bid()};
		Hand lHand = new Hand();
        for(Rank lRank : Rank.values())  
        {
        	lHand.add( new Card( lRank, Suit.HEARTS));
        }
		Bid lTest = aStrategy.selectBid(lBids, lHand);
		assertTrue(lTest.getSuit().equals(Suit.HEARTS));
	}
}
