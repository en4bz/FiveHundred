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

	@Test //test before?
	public void testAdd()
	{
		fail("Not yet implemented");
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

	@Test
	public void testRemove()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testClone()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testIterator()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testToString()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testRandom()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSort()
	{
		fail("Not yet implemented");
	}

}
