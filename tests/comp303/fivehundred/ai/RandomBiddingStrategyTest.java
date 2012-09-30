package comp303.fivehundred.ai;

import static org.junit.Assert.*;

import org.junit.Test;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.ModelException;
import comp303.fivehundred.util.AllCards;

public class RandomBiddingStrategyTest
{

	@Test
	public void testRandomBiddingStrategy()
	{
		RandomBiddingStrategy strategyTest = new RandomBiddingStrategy();
		
	}

	@Test
	public void testRandomBiddingStrategyInt()
	{
		int passFrequency = 20;
		
		RandomBiddingStrategy strategyTest = new RandomBiddingStrategy(passFrequency);
	}

	@Test
	public void testSelectBid()
	{
		//new hand
		Hand newHand = new Hand();
		newHand.add(AllCards.a4D);
		newHand.add(AllCards.aJH);
		newHand.add(AllCards.a7D);
		newHand.add(AllCards.a8S);
		
		//Test 1:Highest bid has already been bid. Must return a new bid
		
		Bid[] testBid1 = {new Bid(24), new Bid(15), new Bid(12)}; //new array of bids
		
		RandomBiddingStrategy strategyTest1 = new RandomBiddingStrategy();
		
		Bid bidSelected1 = strategyTest1.selectBid(testBid1, newHand);
		assertEquals(bidSelected1, new Bid()); // must return new bid() 
		
		//Test 2: The highest bid has not been bid. 
		
		Bid[] testBid2 = {new Bid(20), new Bid(15), new Bid(12)}; //new array of bids
		RandomBiddingStrategy strategyTest2 = new RandomBiddingStrategy();
		
		Bid bidSelected2 = strategyTest2.selectBid(testBid2, newHand);
		try
		{
			 // must return a bid, but that bid can be a pass which will raise an exception
			assertNotNull(bidSelected2.getTricksBid());
		}
		catch(ModelException e)
		{
			
		}
	}

}
