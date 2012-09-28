package comp303.fivehundred.test;

import static org.junit.Assert.*;

/**
 * @author Gabrielle Germain
 * Test class for RandomPlayingStrategy
 */

import org.junit.Test;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.AllCards;

public class RandomPlayingStrategyTest
{
	
	

	@Test
	public void testPlay()
	{
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
		
		Bid someBid = new Bid (6, null);//null = no trump
		Trick highestBid = new Trick (someBid);
		
		//play(highestBid, newHand);
	}

}
