package comp303.fivehundred.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import comp303.fivehundred.util.AllCards;
import comp303.fivehundred.util.Card.Suit;

/*
 * @author Ian Forbes
 */
public class TestTrick
{
	@Test
	public void testGetTrumpSuit()
	{
		Trick testTrick = new Trick(new Bid(8,Suit.HEARTS));
		assertEquals(Suit.HEARTS, testTrick.getTrumpSuit());
		testTrick = new Trick(new Bid(10, null));
		assertNull(testTrick.getTrumpSuit());		
		try
		{
			testTrick = new Trick(new Bid());
			fail();
		}
		catch(ModelException e)
		{
			
		}
	}
	
	@Test
	public void testGetSuitLed()
	{
		Trick testTrick = new Trick(new Bid(6, Suit.SPADES));
		testTrick.add(AllCards.a5S);
		assertEquals(Suit.SPADES, testTrick.getSuitLed());
		testTrick.remove(AllCards.a5S);
		testTrick.add(AllCards.aHJo);
		try
		{
			testTrick.getSuitLed();
			fail();
		}
		catch(ModelException e)
		{
			
		}
	}
	
	@Test 
	public void testHighest()
	{
		Trick testTrick = new Trick(new Bid(6, Suit.SPADES));
		testTrick.add(AllCards.a6H);
		testTrick.add(AllCards.aTH);
		testTrick.add(AllCards.aKD);
		testTrick.add(AllCards.a8S);
		assertEquals(AllCards.a8S, testTrick.highest());
		testTrick = new Trick(new Bid(6, Suit.HEARTS));
		try
		{
			testTrick.highest();
			fail();
		}
		catch(AssertionError e)
		{
			
		}
	}
	
	@Test
	public void testWinnerIndex()
	{
		Trick testTrick = new Trick(new Bid(6, Suit.SPADES));
		testTrick.add(AllCards.a6H);
		testTrick.add(AllCards.aTH);
		testTrick.add(AllCards.aKD);
		testTrick.add(AllCards.a8S);
		assertEquals(3 , testTrick.winnerIndex());
		testTrick = new Trick(new Bid(6, Suit.HEARTS));
		try
		{
			testTrick.winnerIndex();
			fail();
		}
		catch(AssertionError e)
		{
			
		}
	}
}
