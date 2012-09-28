package comp303.fivehundred.util;

import static comp303.fivehundred.util.AllCards.a4C;
import static comp303.fivehundred.util.AllCards.a7D;
import static comp303.fivehundred.util.AllCards.a8C;
import static comp303.fivehundred.util.AllCards.a9C;
import static comp303.fivehundred.util.AllCards.a9D;
import static comp303.fivehundred.util.AllCards.aAC;
import static comp303.fivehundred.util.AllCards.aHJo;
import static comp303.fivehundred.util.AllCards.aJC;
import static comp303.fivehundred.util.AllCards.aJD;
import static comp303.fivehundred.util.AllCards.aJS;
import static comp303.fivehundred.util.AllCards.aKC;
import static comp303.fivehundred.util.AllCards.aKS;
import static comp303.fivehundred.util.AllCards.aLJo;
import static comp303.fivehundred.util.AllCards.aQD;
import static comp303.fivehundred.util.AllCards.aQH;
import static comp303.fivehundred.util.AllCards.aTC;
import static comp303.fivehundred.util.AllCards.aTH;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import comp303.fivehundred.util.Card.Joker;
import comp303.fivehundred.util.Card.Rank;
import comp303.fivehundred.util.Card.Suit;

/*
 * @author Ian Forbes
 */

public class TestCard
{
	
	@Test
	public void testIsJoker(){
		assertEquals(true, aHJo.isJoker());
		assertEquals(true, aLJo.isJoker());
		assertEquals(false, a4C.isJoker());
		assertEquals(false, aKC.isJoker());
	}
	
	@Test
	public void testGetJokerValue(){
		assertEquals(Joker.HIGH, aHJo.getJokerValue());
		assertEquals(Joker.LOW, aLJo.getJokerValue());
	}
	
	@Test
	public void testGetRank(){
		assertEquals(Rank.ACE, aAC.getRank());
		assertEquals(Rank.FOUR, a4C.getRank());
	}
	
	@Test
	public void testGetConverse()
	{
		assertEquals(Suit.SPADES, Suit.CLUBS.getConverse());
		assertEquals(Suit.HEARTS, Suit.DIAMONDS.getConverse());
		assertEquals(Suit.DIAMONDS, Suit.HEARTS.getConverse());
		assertEquals(Suit.CLUBS, Suit.SPADES.getConverse());
	}
	
	@Test
	public void testGetSuit(){
		Card lToTest = new Card(Rank.ACE, Suit.HEARTS);
		assertEquals(Suit.HEARTS, lToTest.getSuit());
	}
	
	@Test
	public void testToString()
	{
		assertEquals( "ACE of CLUBS", aAC.toString());
		assertEquals( "TEN of CLUBS", aTC.toString());
		assertEquals( "JACK of CLUBS", aJC.toString());
		assertEquals( "QUEEN of HEARTS", aQH.toString());
		assertEquals( "KING of SPADES", aKS.toString());
		assertEquals( "QUEEN of DIAMONDS", aQD.toString());
		assertEquals( "HIGH Joker", aHJo.toString());
	}
	
	@Test
	public void testToShortString()
	{
		assertEquals("4C", a4C.toShortString());
		assertEquals("TH", aTH.toShortString());
		assertEquals("HJ", aHJo.toShortString());
		assertEquals("LJ", aLJo.toShortString());
	}
	
	@Test
	public void testCompareTo()
	{
		assertEquals(true,a7D.compareTo(a8C) < 0);
		assertEquals(true ,a9C.compareTo(a9C) == 0);
		assertEquals(true ,a9C.compareTo(a4C) > 0);
		assertEquals(true ,aHJo.compareTo(a4C) > 0);
		assertEquals(true,a8C.compareTo(aLJo) < 0);
		assertEquals(true, aHJo.compareTo(aLJo) > 0);
	}
	
	@Test
	public void testEquals()
	{
		assertEquals(true, a4C.equals(a4C));
		assertEquals(false, a9D.equals(a4C));
		assertEquals(false, aTH.equals(new Object()));
	}
	
	@Test
	public void testHashCode()
	{
		assertEquals(a4C.hashCode(),a4C.hashCode());
		assertEquals(aHJo.hashCode(), aHJo.hashCode());
	}
	
	@Test
	public void testGetEffectiveSuit()
	{
		assertEquals(Suit.CLUBS, a4C.getEffectiveSuit(Suit.HEARTS));
		assertEquals(Suit.HEARTS, aJD.getEffectiveSuit(Suit.HEARTS));
		assertEquals(Suit.SPADES, aJS.getEffectiveSuit(Suit.DIAMONDS));
		assertEquals(Suit.CLUBS, aJS.getEffectiveSuit(Suit.CLUBS));
		assertEquals(Suit.CLUBS, aKC.getEffectiveSuit(null));
	}
}
