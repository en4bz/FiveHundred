package comp303.fivehundred.ai;

import static org.junit.Assert.assertTrue;

import static comp303.fivehundred.model.AllCards.*;

import org.junit.Before;
import org.junit.Test;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Deck;
import comp303.fivehundred.util.Card.Rank;
import comp303.fivehundred.util.Card.Suit;

public class BasicsBiddingStrategyTest
{
	private BasicBiddingStrategy aStrategy;
	
	@Before
	public void setup()
	{
		 aStrategy = new BasicBiddingStrategy();
	}
	
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
			assertTrue(lTest.getSuit().equals(lSuit));//Player has all the cards of lSuit
		}
	}
	
	@Test
	public void testPassingBid()
	{
		Bid[] lBids = {new Bid(10,null),new Bid(10,null),new Bid(10,null),new Bid(10,null)};
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
	public void testBid1()
	{
		for(int i = 0; i < 100; i++){
			Bid[] passes = new Bid[]{new Bid(),new Bid(),new Bid(),new Bid()};
			Hand lHand = generateHand();
			Bid theBid = aStrategy.selectBid(passes, lHand);
			for(Suit s : Suit.values())
			{
				//If there are no cards of s do not bid s
				if(!theBid.isPass() && !theBid.isNoTrump() && lHand.getTrumpCards(s).size() == 0){
					assertTrue(!theBid.equals(s));
				}
			}
		}
	}
	
	public static Hand generateHand()
	{
		Deck lDeck = new Deck();
		lDeck.shuffle();
		Hand lHand = new Hand();
		for(int i = 0; i < 10; i++){
			lHand.add(lDeck.draw());
		}
		return lHand;
	}
	
	@Test 
	public void testGreaterOrPass()
	{
		for(int i = 6; i <= 10; i++)
		{
			for(Suit s : Suit.values())
			{
				Bid selected = aStrategy.selectBid(new Bid[]{new Bid(i,s)}, generateHand());
				//Make sure bid is larger or pass
				assertTrue(selected.compareTo(new Bid(i,s)) > 0 || selected.isPass());
			}
		}
	}
}
