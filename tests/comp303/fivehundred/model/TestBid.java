package comp303.fivehundred.model;

import static org.junit.Assert.*;
import org.junit.Test;

import comp303.fivehundred.util.Card.Suit;

public class TestBid
{
	@Test
	public void testGetSuit()
	{
		Bid testBid = new Bid(8, Suit.HEARTS);
		assertEquals(Suit.HEARTS, testBid.getSuit());
	}
	
	@Test
	public void testGetTrickBids()
	{
		Bid testBid = new Bid(8, Suit.HEARTS);
		assertEquals(8, testBid.getTricksBid());
	}
	
	@Test
	public void testIsPass()
	{
		Bid testBid = new Bid();
		assertEquals(true, testBid.isPass());

	}
	
	@Test
	public void testIsNoTrump()
	{
		Bid testBid = new Bid(8, null);
		assertEquals(true, testBid.isNoTrump());
		testBid = new Bid(10, Suit.CLUBS);
		assertEquals(false, testBid.isNoTrump());
		testBid = new Bid();
		assertEquals(false, testBid.isNoTrump());

	}
	
	@Test
	public void testToString()
	{
		Bid testBid = new Bid(10, Suit.HEARTS);
		assertEquals("10 of HEARTS", testBid.toString());
		testBid = new Bid(10, null);
		assertEquals("10 of NO TRUMP", testBid.toString());
	}
	
	@Test
	public void testEquals()
	{
		Bid testBid1 = new Bid(8, Suit.HEARTS);
		Bid testBid2 = new Bid(8, Suit.HEARTS);
		assertEquals(true, testBid1.equals(testBid2));
		testBid1 = new Bid(7, Suit.HEARTS);
		assertEquals(false, testBid1.equals(testBid2));

	}
}
