package comp303.fivehundred.ai;

import static org.junit.Assert.*;

import org.junit.Test;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.AllCards;
import comp303.fivehundred.util.CardList;

/**
 * @author Gabrielle Germain
 * Test class for RandomCardExchangeStrategy
 */

public class RandomCardExchangeStrategyTest
{
	
	@Test
	public void testSelectCardsToDiscard()
	{	
		Bid[] testBid = {new Bid(20), new Bid(15), new Bid(12)}; //new array of bids
		int pIndex = 0;//what value should have pIndex?
		
		//We create a new hand (10 cards)
		Hand newHand = new Hand();
		newHand.add(AllCards.a4D);
		newHand.add(AllCards.aJH);
		newHand.add(AllCards.a7D);
		newHand.add(AllCards.a8D);
		newHand.add(AllCards.a5H);
		newHand.add(AllCards.a9C);
		newHand.add(AllCards.aAC);
		newHand.add(AllCards.aAS);
		newHand.add(AllCards.a5S);
		newHand.add(AllCards.a4H);
		
		//selectCardsToDiscard(testBid, pIndex, newHand);
		
		
	}

}
