package comp303.fivehundred.util;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import org.junit.Test;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;

import comp303.fivehundred.util.Card.ByRankComparator;

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
		
		lList.add(AllCards.aAH);
		assertEquals(lList.size(), 1);
		
		lList.add(AllCards.a7H);
		lList.add(AllCards.aAD);
		
		assertEquals(lList.size(), 3);
		
		//Since the card is already in the set, it will not be added to the hand
		lList.add(AllCards.a7H);
		assertEquals(lList.size(), 3); // size must remain the same
		
		assertEquals(lList.getFirst(), AllCards.aAH);
		assertEquals(lList.getLast(), AllCards.aAD);
		
	}

	@Test
	public void testSize()
	{
		CardList lList = new CardList();
		
		assertEquals(lList.size(), 0);
		lList.add(AllCards.aAH);
		assertEquals(lList.size(), 1);
	}

	@Test(expected=GameUtilException.class)
	public void testGetFirst()
	{
		CardList lList = new CardList();
		
		//exception expected since the list is empty
		lList.getFirst();
		
		//fill lList with cards and test if it returns the first card inserted
		lList.add(AllCards.a4C);
		lList.add(AllCards.a5D);
		lList.add(AllCards.aAS);
		assertEquals(AllCards.a4C, lList.getFirst());
			
	}

	@Test(expected=GameUtilException.class)
	public void testGetLast()
	{
		CardList lList = new CardList();
		
		//exception expected since the list is empty
		lList.getLast();
		
		//fill lList with cards and test if it returns the last card inserted
		lList.add(AllCards.a9C);
		lList.add(AllCards.aAS);
		lList.add(AllCards.a5D);
		assertEquals(AllCards.a5D, lList.getLast());
		
	}

	@Test
	public void testRemove()
	{
		
		CardList lList = new CardList();
		
		lList.add(AllCards.a7H);
		lList.add(AllCards.a8S);
		lList.add(AllCards.a5H);
		assertEquals(lList.size(), 3);
		lList.remove(AllCards.a8S);
		lList.remove(AllCards.a5H);
		assertEquals(lList.size(), 1);
		lList.remove(AllCards.a7H);
		assertEquals(lList.size(), 0);//lList is supposed to be empty
		
	}

	@Test
	public void testClone() throws CloneNotSupportedException
	{
		CardList lList = new CardList();
		
		//we create a new list and add cards to it
		lList.add(AllCards.a7H);
		lList.add(AllCards.a8S);
		lList.add(AllCards.aAS);
		
		CardList copy = lList.clone();
		
		assertEquals(lList.size(), copy.size());
		assertEquals(lList.getFirst(), copy.getFirst());
		assertEquals(lList.getLast(), copy.getLast());
		
		//We modify lList. We remove the first card
		lList.remove(AllCards.a7H);
		assertNotSame(lList.size(), copy.size());

	}

	@Test
	public void testIterator()
	{
		CardList lList = new CardList();
		lList.add(AllCards.a5H);
		lList.add(AllCards.a4S);
		lList.add(AllCards.aAS);
	    assertEquals(lList.size(), 3); 
	}
	

	@Test
	public void testToString()
	{
		
		CardList lList = new CardList();
		
		lList.add(AllCards.aKH);
		assertEquals("The Cards in this List are as follows in order:\n1: KING of HEARTS\n",lList.toString());
		//\n is the "line separator"	
	}

	@Test
	public void testRandom()
	{
		CardList lList = new CardList();
		
		lList.add(AllCards.a5C);
		lList.add(AllCards.a6C);
		lList.add(AllCards.a7S);
		
		Card randomCard = lList.random();
		
		//We make sure that the random card chosen is originally in lList
		if (randomCard != AllCards.a5C && randomCard != AllCards.a6C && randomCard != AllCards.a7S){
			fail();
		}		
	}

	@Test 
	public void testSort() throws CloneNotSupportedException
	{
		
		CardList lList = new CardList();
		
		lList.add(AllCards.a5C);
		lList.add(AllCards.a7S);
		lList.add(AllCards.a6C);
		

		lList.sort(null);
		
		// Test assertions
		try
		{
			lList.sort(null);
			fail();
		}
		catch(AssertionError e)
		{
			
		}
		
		CardList test = lList.sort(new Card.ByRankComparator());

		lList.sort(new Card.ByRankComparator()); // what is the pComparator?

		
		assertEquals(test.size(), lList.size());
        assertTrue(test != lList);
        assertFalse(test.getLast().equals(lList.getLast()));
   
        //By RankComparatorSorting
		assertEquals(test.getFirst(), AllCards.a5C);
		assertEquals(test.getLast(), AllCards.a7S); 
		
		// Test BySuitComparator sorting
        CardList lBySuit = lList.sort(new Card.BySuitComparator(Suit.SPADES));
        assertEquals(lBySuit.getFirst(), AllCards.a5C);
        assertEquals(lBySuit.getLast(), AllCards.a7S);  
        
       //Test BySuitNoTrumpComparator sorting
        CardList lBySuitNoTrump = lList.sort(new Card.BySuitNoTrumpComparator());
        assertEquals(lBySuitNoTrump.getFirst(), AllCards.a7S);
        assertEquals(lBySuitNoTrump.getLast(), AllCards.a6C); 
		
	}
}
