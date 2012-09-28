package comp303.fivehundred.util;

import static org.junit.Assert.*;

//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.Iterator;

import org.junit.Test;

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
		
		
		Iterator<Card> it = lList.iterator();
		int sizeOfList = 0;

	    while (it.hasNext()){
	    	
	    	Card newCard = it.next();
	    	sizeOfList++;
	    }
	    
	    assertEquals(lList.size(), 3); 
	}
	

	@Test
	public void testToString()
	{
		
		CardList lList = new CardList();
		
		lList.add(AllCards.aKH);
		assertEquals(lList.getFirst().toString(), "KING of HEARTS");
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
		lList.add(AllCards.a6C);
		lList.add(AllCards.a7S);
		
		lList.sort(null); // what is the pComparator?
		
		assertEquals(lList.getFirst(), AllCards.a5C);
		assertEquals(lList.getLast(), AllCards.a7S); 
		
	}

}
