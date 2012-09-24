package comp303.fivehundred.util;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Test;

/**
 * @author Gabrielle Germain
 * Test Class for CardList
 */

public class TestCardList
{
	private CardList lInitiallyEmpty = new CardList();

	@Test
	public void testCardList()
	{
		fail("Not yet implemented");
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
		
	}

	@Test
	public void testSize()
	{
		fail("Not yet implemented");
	}

	@Test(expected=GameUtilException.class)
	public void testGetFirst()
	{
		//exception expected since the list is empty
		lInitiallyEmpty.getFirst();
		
		//add one card in lList and test if it returns the same card as "first card"
		CardList lList = new CardList();
		lList.add(AllCards.a4C);
		assertEquals(AllCards.a4C, lList.getFirst());
		
		lInitiallyEmpty.add(AllCards.a4C);
		
		//Add more than one card in each list and see if it returns the same "first card" from both lists
		lList.add(AllCards.aAH);
		lInitiallyEmpty.add(AllCards.aAH);
		lList.add(AllCards.a5S);
		lInitiallyEmpty.add(AllCards.a5S);
		assertEquals(lList.getFirst(), AllCards.a5S);
		assertEquals(lList.getFirst(), lInitiallyEmpty.getFirst());
		//both sets of card must return the same "first card" (the last added to the set)
		
	}

	@Test(expected=GameUtilException.class)
	public void testGetLast()
	{
		CardList lList = new CardList();
		lList.getLast();//exception expected since the list is empty
		
		//add one card in lList and test if it returns the same card as "last card"
		lList.add(AllCards.a4C);
		assertEquals(AllCards.a4C, lList.getLast());
		
		//return the first card added to the set
		lList.add(AllCards.a4D);
		lList.add(AllCards.a4S);
		assertEquals(AllCards.a4C, lInitiallyEmpty.getLast());
		
		//We need to test lInitialltEmptyList also
	}

	@Test(expected=GameUtilException.class)
	public void testRemove()
	{
		
		CardList lList = new CardList();
		
		//remove the cards in the same order as they were inserted
		lList.add(AllCards.a7H);
		lList.add(AllCards.a8S);
		lList.add(AllCards.a5H);
		assertEquals(lList.size(), 3);
		lList.remove(AllCards.a7H);
		lList.remove(AllCards.a8S);
		lList.remove(AllCards.a5H);
		assertEquals(lList.size(), 0);//lList is supposed to be empty
		
		lList.remove(AllCards.a4D); //exception expected since the deck is empty
		
		//remove the cards in the inverse order in which they were inserted
		lList.add(AllCards.a7H);
		lList.add(AllCards.a8S);
		lList.add(AllCards.aAS);
		lList.remove(AllCards.aAS);		
		assertEquals(lList.size(),2);//lList contains two elements
		lList.remove(AllCards.a8S);
		lList.remove(AllCards.a7H);
		assertEquals(lList.size(), 0);//lList is supposed to be empty
		
		
	}

	@Test
	public void testClone()
	{
		
		CardList lList = new CardList();
		
		//we create a new list and add cards to it
		lList.add(AllCards.a7H);
		lList.add(AllCards.a8S);
		lList.add(AllCards.aAS);
		
		//we create a clone of lList
		CardList clonedList = lList.clone();
		
		assertEquals(lList.size(), clonedList.size());
		assertEquals(lList.getFirst(), clonedList.getFirst());
		assertEquals(lList.getLast(), clonedList.getLast());
		
		//We modify the lList. We remove the first card
		lList.remove(AllCards.a7H);
		
		assertNotSame(lList.getFirst(), clonedList.getFirst());
		assertNotSame(lList.getLast(), clonedList.getLast());
		assertNotSame(lList.size(), clonedList.size());
		
		
		
	}

	@Test
	public void testIterator()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testToString()
	{
		
		CardList lList = new CardList();
		
		lList.add(AllCards.aKH);
		lList.add(AllCards.a8S);
		lList.add(AllCards.a5H);
		
		assertEquals(lList.getFirst().toString(), "five of hearts");
		
		
	}

	@Test
	public void testRandom()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSort()
	{
		
		CardList lList = new CardList();
		
		lList.add(AllCards.a5C);
		lList.add(AllCards.a6C);
		lList.add(AllCards.a7S);
		
		//lList.sort(pComparator); // what is pComparator?
		
		assertEquals(lList.getFirst(), AllCards.a7S);//assuming that the lowest card is the first card in an ordered set
		assertEquals(lList.getLast(), AllCards.a6C); //assuming that the highest card is the last card in an ordered set
		
	}

}
