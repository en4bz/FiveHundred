package comp303.fivehundred.model;

import static org.junit.Assert.assertEquals;
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
	}
	
	@Test
	public void testGetSuitLed()
	{
		Trick testTrick = new Trick(new Bid(6, Suit.SPADES));
		testTrick.add(AllCards.a5S);
		assertEquals(Suit.SPADES, testTrick.getSuitLed());
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
	}
	
	@Test
	public void testWinnerIndex()
	{
		Trick testTrick = new Trick(new Bid(6, Suit.SPADES));
		testTrick.add(AllCards.a6H);
		testTrick.add(AllCards.aTH);
		testTrick.add(AllCards.aKD);
		testTrick.add(AllCards.a8S);
		assertEquals(4, testTrick.winnerIndex());
	}
}
