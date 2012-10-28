package comp303.fivehundred.util;

import static comp303.fivehundred.util.AllCards.a4C;
import static comp303.fivehundred.util.AllCards.a4S;
import static comp303.fivehundred.util.AllCards.a5C;
import static comp303.fivehundred.util.AllCards.a5D;
import static comp303.fivehundred.util.AllCards.a5H;
import static comp303.fivehundred.util.AllCards.a6C;
import static comp303.fivehundred.util.AllCards.a7H;
import static comp303.fivehundred.util.AllCards.a7S;
import static comp303.fivehundred.util.AllCards.a8S;
import static comp303.fivehundred.util.AllCards.a9C;
import static comp303.fivehundred.util.AllCards.aAD;
import static comp303.fivehundred.util.AllCards.aAH;
import static comp303.fivehundred.util.AllCards.aAS;
import static comp303.fivehundred.util.AllCards.aJD;
import static comp303.fivehundred.util.AllCards.aKH;
import static comp303.fivehundred.util.AllCards.aTC;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import comp303.fivehundred.util.Card.Suit;

/**
 * @author Gabrielle Germain
 * Test Class for CardList
 */

public class TestCardList
{

	@Test
	public void testCardList()
	{
		CardList lInitiallyEmpty = new CardList();
		assertEquals(0, lInitiallyEmpty.size());
	}

	@Test 
	public void testAdd()
	{
		CardList lList = new CardList();
		
		lList.add(aAH);
		assertEquals(lList.size(), 1);
		
		lList.add(a7H);
		lList.add(aAD);
		
		assertEquals(lList.size(), 3);
		
		//Since the card is already in the set, it will not be added to the hand
		lList.add(a7H);
		assertEquals(lList.size(), 3); // size must remain the same
		
		assertEquals(lList.getFirst(), aAH);
		assertEquals(lList.getLast(), aAD);
		
	}

	@Test
	public void testSize()
	{
		CardList lList = new CardList();
		
		assertEquals(lList.size(), 0);
		lList.add(aAH);
		assertEquals(lList.size(), 1);
	}

	@Test
	public void testGetFirst()
	{
		CardList lList = new CardList();
		
		//exception expected since the list is empty
		try
		{
			lList.getFirst();
			fail();
		}
		catch(GameUtilException e)
		{
		
		}
		
		//fill lList with cards and test if it returns the first card inserted
		lList.add(a4C);
		lList.add(a5D);
		lList.add(aAS);
		assertEquals(a4C, lList.getFirst());
			
	}

	@Test
	public void testGetLast()
	{
		CardList lList = new CardList();
		
		//exception expected since the list is empty
		try
		{
			lList.getLast();
			fail();
		}
		catch(GameUtilException e)
		{
		
		}
		
		//fill lList with cards and test if it returns the last card inserted
		lList.add(a9C);
		lList.add(aAS);
		lList.add(a5D);
		assertEquals(a5D, lList.getLast());
		
	}

	@Test
	public void testRemove()
	{
		
		CardList lList = new CardList();
		
		lList.add(a7H);
		lList.add(a8S);
		lList.add(a5H);
		assertEquals(lList.size(), 3);
		lList.remove(a8S);
		lList.remove(a5H);
		assertEquals(lList.size(), 1);
		lList.remove(a7H);
		assertEquals(lList.size(), 0);//lList is supposed to be empty
		
	}

	@Test
	public void testClone()
	{
		CardList lList = new CardList();
		
		//we create a new list and add cards to it
		lList.add(a7H);
		lList.add(a8S);
		lList.add(aAS);
		
		CardList copy = lList.clone();
		
		assertEquals(lList.size(), copy.size());
		assertEquals(lList.getFirst(), copy.getFirst());
		assertEquals(lList.getLast(), copy.getLast());
		
		//We modify lList. We remove the first card
		lList.remove(a7H);
		assertNotSame(lList.size(), copy.size());

	}

	@Test
	public void testIterator()
	{
		CardList lList = new CardList();
		lList.add(a5H);
		lList.add(a4S);
		lList.add(aAS);
	    assertEquals(lList.size(), 3); 
	}
	

	@Test
	public void testToString()
	{
		
		CardList lList = new CardList();
		
		lList.add(aKH);
		lList.add(a5C);
		assertEquals("KING of HEARTS, FIVE of CLUBS",lList.toString());
		//\n is the "line separator"	
	}

	@Test
	public void testRandom()
	{
		CardList lList = new CardList();
		
		lList.add(a5C);
		lList.add(a6C);
		lList.add(a7S);
		
		Card randomCard = lList.random();
		
		//We make sure that the random card chosen is originally in lList
		if (randomCard != a5C && randomCard != a6C && randomCard != a7S){
			fail();
		}		
	}

	@Test
	public void testSort()
	{
		CardList lList = new CardList();
		lList.add(a5C);
		lList.add(aTC);
		lList.add(a7S);
		lList.add(aKH);
		lList.add(aJD);
		
		// Test assertions
		try
		{
			lList.sort(null);
			fail();
		}
		catch(AssertionError e)
		{
			
		}
		
		// Test method has no side effects
		CardList lByRank = lList.sort(new Card.ByRankComparator());
		assertEquals(lByRank.size(), lList.size());
		assertTrue(lByRank != lList);
		assertTrue(!lByRank.getLast().equals(lList.getLast()));

		// Test ByRankComparator sorting
		assertEquals(lByRank.getFirst(), a5C);
		assertEquals(lByRank.getLast(), aKH); 		

		// Test BySuitComparator sorting
		CardList lBySuit = lList.sort(new Card.BySuitComparator(Suit.HEARTS));
		assertEquals(lBySuit.getFirst(), a7S);
		assertEquals(lBySuit.getLast(), aJD); 	
		
		// Test BySuitNoTrumpComparator sorting
		CardList lBySuitNoTrump = lList.sort(new Card.BySuitNoTrumpComparator());
		assertEquals(lBySuitNoTrump.getFirst(), a7S);
		assertEquals(lBySuitNoTrump.getLast(), aKH); 	

		
	}
}
