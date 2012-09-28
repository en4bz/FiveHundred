package comp303.fivehundred.model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;


import comp303.fivehundred.util.Card.Suit;

public class TestTrick
{
	@Test
	public void testGetTrumpSuit()
	{
		Trick testTrick = new Trick(new Bid(8,Suit.HEARTS));
		assertEquals(Suit.HEARTS, testTrick.getTrumpSuit());
	}
}
