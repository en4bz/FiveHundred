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
		newHand.add(AllCards.a8S);
		newHand.add(AllCards.aQD);
		newHand.add(AllCards.aAH);
		newHand.add(AllCards.a5D);
		newHand.add(AllCards.a7C);
		newHand.add(AllCards.a9C);
		newHand.add(AllCards.a4S);

		
		RandomCardExchangeStrategy testStrategy = new RandomCardExchangeStrategy();
		
		CardList listofCardsTest = testStrategy.selectCardsToDiscard(testBid, pIndex, newHand);
		
		assertEquals(listofCardsTest.size(), 6);
		
		
		
	}

}
