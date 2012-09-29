package comp303.fivehundred.model;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import comp303.fivehundred.util.Card.Suit;

public class TestBid
{
	@Test
	public void testGetSuit()
	{
		Bid testBid = new Bid(8, Suit.HEARTS);
		assertEquals(Suit.HEARTS, testBid.getSuit());
		testBid = new Bid();
		try
		{
			testBid.getSuit();
			fail();
		}
		catch(ModelException e)
		{
			
		}
	}
	
	@Test
	public void testGetTrickBids()
	{
		Bid testBid = new Bid(8, Suit.HEARTS);
		assertEquals(8, testBid.getTricksBid());
		testBid = new Bid();
		try
		{
			testBid.getTricksBid();
			fail();
		}
		catch(ModelException e)
		{
			
		}
	}
	
	@Test
	public void testIsPass()
	{
		Bid testBid = new Bid();
		assertEquals(true, testBid.isPass());
		testBid = new Bid(8, Suit.HEARTS);
		assertEquals(false, testBid.isPass());

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
		testBid = new Bid();
		assertEquals("PASS", testBid.toString());
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
	
	@Test
	public void testToIndex()
	{
		Bid testBid = new Bid(12);
		assertEquals(12, testBid.toIndex());
		testBid = new Bid();
		try
		{
			testBid.toIndex();
			fail();
		}
		catch(ModelException e)
		{
			
		}
	}
	
	@Test
	public void testHashCode()
	{
		Bid A = new Bid();
		Bid B = new Bid();
		assertEquals(A.hashCode(), B.hashCode());
	}
	
	@Test
	public void testGetScore()
	{
		Bid testBid = new Bid(6,Suit.SPADES);
		assertEquals(40, testBid.getScore());
		testBid = new Bid();
		try
		{
			testBid.getScore();
			fail();
		}
		catch(ModelException e)
		{
			
		}
	}
	
	@Test
	public void testCompareTo()
	{
		assertTrue((new Bid(7, Suit.SPADES)).compareTo( new Bid(8, Suit.SPADES)) < 0);
		assertTrue((new Bid()).compareTo(new Bid(9, Suit.HEARTS)) < 0);
		assertTrue((new Bid()).compareTo(new Bid()) == 0);
	}
}
