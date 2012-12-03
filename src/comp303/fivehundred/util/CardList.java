package comp303.fivehundred.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

/**
 * @author Rayyan Khoury
 * A mutable list of cards. Does not support duplicates.
 * The cards are maintained in the order added.
 */
public class CardList implements Iterable<Card>, Cloneable
{

	// The array list of all cards
	private ArrayList<Card> aCards;
	
	/**
	 * Creates a new, empty card list.
	 */
	
	public CardList()
	{
		// Creates a new array list of type card
		aCards = new ArrayList<Card>();
	}
	
	/**
	 * Clears the card list
	 */
	public void clear()
	{
		
		aCards.clear();
		
	}
	
	/**
	 * Adds a card if it is not
	 * already in the list. Has no effect if the card
	 * is already in the list.
	 * @param pCard The card to add.
	 * @pre pCard != null
	 */
	public void add(Card pCard)
	{
		assert pCard != null;	
		// If the card is already contained don't do anything
		if (!aCards.contains(pCard))
		{
			aCards.add(pCard);
		}
	}
	
	/**
	 * Adds cards if they are not
	 * already in the list. Has no effect if they
	 * are already in the list.
	 * @param pCardArray The card to add.
	 * @pre pCardArray != null
	 */
	public void addCardArray(Card[] pCardArray)
	{
		assert pCardArray != null;
		
		Card toAdd = null;
		
		for (int i = 0; i < pCardArray.length; i++)
		{
			
			toAdd = pCardArray[i];
			// If the card is already contained don't do anything
			if (!aCards.contains(toAdd))
			{
				aCards.add(toAdd);
			}
			
		}
	}
	
	/**
	 * @return The number of cards in the list.
	 */
	public int size()
	{
		
		return aCards.size();
		
	}
	
	/**
	 * @return The first card in the list, according to whatever
	 * order is currently being used. 
	 * @throws GameUtilException if the list is empty.
	 */
	public Card getFirst()
	{

		// Throws an exception if the card list is empty
		if (aCards.isEmpty()) 
		{
			throw new GameUtilException("CARD LIST EMPTY - cannot retrieve first card");
		}
		
		return aCards.get(0);
	}
	
	/**
	 * @return The last card in the list, according to whatever
	 * order is currently being used. 
	 * @throws GameUtilException If the list is empty.
	 */
	public Card getLast()
	{
		
		// Throws an exception if the card list is empty
		if (aCards.isEmpty()) 
		{
			throw new GameUtilException("CARD LIST EMPTY - cannot retrieve last card");
		}
		
		// Returns the last card using (n-1) indexing
		return aCards.get(aCards.size() - 1);
	}
	
	/**
	 * Removes a card from the list. Has no effect if the card is
	 * not in the list.
	 * @param pCard The card to remove. 
	 * @pre pCard != null;
	 */
	public void remove(Card pCard)
	{
		
		assert pCard != null;
		
		// Remove already takes care of whether the array list contains the card or not
		aCards.remove(pCard);
		
	}
	
	public void removeAll()
	{
		aCards.clear();
	}
	
	/**
	 * @see java.lang.Object#clone()
	 * {@inheritDoc}
	 */

	public CardList clone()
	{
		try 
		{
			CardList lCopy = (CardList)super.clone();
			lCopy.aCards = (ArrayList<Card>) this.aCards.clone();
			return lCopy;
		} 
		catch (CloneNotSupportedException e) 
		{
			// shouldn't happen
			throw new AssertionError(e);
		}
	}
	
	
	/**
	 * @see java.lang.Iterable#iterator()
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Card> iterator()
	{
		// Returns an iterator
		Iterator<Card> it = aCards.iterator();
		return it;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 * {@inheritDoc}
	 */
	public String toString()
	{
		// Starts building the string
		StringBuilder sb = new StringBuilder();
		
		// Creates the iterator for this object
		Iterator<Card> it = this.iterator();
		
		// Goes through the card list and prints all the cards to the console in order
	    while (it.hasNext())
	    {
	    	sb.append(it.next().toShortString());
	    	
	    	if (it.hasNext())
	    	{	
	    		sb.append(", ");	
	    	}
	    }
	    
	    // Returns the full string of cards
	    return sb.toString();
		
	}
	
	/**
	 * @pre aCards.size() > 0
	 * @return A randomly-chosen card from the set. 
	 */
	public Card random()
	{
		
		// Assertions
		assert aCards != null;
		assert aCards.size() > 0;
		
		// Random object
		Random aNum = new Random();

		// Finding a random number between 0 and size
		int size = aCards.size();
	    int index = aNum.nextInt(size);
	    
	    // Returns the random card
		return aCards.get(index);
		
	}
	
	/**
	 * Returns another CardList, sorted as desired. This
	 * method has no side effects.
	 * @param pComparator Defines the sorting order.
	 * @return the sorted CardList
	 * @pre pComparator != null
	 */

	public CardList sort(Comparator<Card> pComparator)
	{
		
		// Assertions
		assert pComparator != null;
		
		// Clones the current CardList
		CardList copy = this.clone();
		
		// Sorts the clone's cards
		Collections.sort(copy.aCards, pComparator);
		
		// Returns the copied sorted card list
		return copy;
		
	}
	
	/**
	 * Returns another CardList in reverse order. This
	 * method has no side effects.
	 * @return the reversed CardList
	 */

	public CardList reverse()
	{
		
		// Clones the current CardList
		CardList copy = this.clone();
		
		// Reverse the clone's cards
		Collections.reverse(copy.aCards);
		
		// Returns the copied sorted card list
		return copy;
		
	}
	/**
	 * Get the first index of the specified card in the cardlist.
	 * @param pCard the card to get the index of
	 * @return the first index of the card
	 */
	public int indexOf(Card pCard)
	{
		return aCards.indexOf(pCard);
	}
	
	/**
	 * Returns true if the card list contains the specified card.
	 * @param pCard the card to look for
	 * @return true if the card list contains pCard
	 */
	public boolean contains(Card pCard)
	{
		return aCards.contains(pCard);
	}
	
	@Override
	public boolean equals(Object pCardList)
	{
    	boolean lReturn = false;
    	if (pCardList != null && pCardList instanceof CardList)
    	{
    		CardList lList = (CardList) pCardList;
    		lReturn = aCards.equals(lList.aCards);
    	}
    	return lReturn;
	}
	
	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}
	
}

