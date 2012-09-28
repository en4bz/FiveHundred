package comp303.fivehundred.util;

import java.util.ArrayList;
// TODO: uncomment here
//import java.util.Collections;
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
	
	// Random object
	private Random aNum = new Random();
	
	/**
	 * Creates a new, empty card list.
	 */
	public CardList()
	{
		
		// Creates a new array list of type card
		aCards = new ArrayList<Card>();
		
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
		
		// If the card is already contained, returns without adding card
		if (aCards.contains(pCard))
		{
			return;
		}
		
		// Adds the unique card
		aCards.add(pCard);
		
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
	public Card getFirst() throws GameUtilException
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
	public Card getLast() throws GameUtilException
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
	
	/**
	 * @see java.lang.Object#clone()
	 * {@inheritDoc}
	 */
	
	//TODO: implement clone
	public CardList clone()
	{
		/*
		super.clone();
		CardList copy = (CardList) super.clone();
		copy.aCards = (ArrayList<Card>) aCards.clone();
		return new CardList();
		*/
		return null;
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
		String NEWLINE = System.getProperty("line.separator");
		
		sb.append("The Cards in this List are as follows in order:" + NEWLINE);
		
		// Creates the iterator for this object
		Iterator<Card> it = this.iterator();
		int cardNumber = 1;
		
		// Goes through the card list and prints all the cards to the console in order
	    while (it.hasNext())
	    {
	    	sb.append(cardNumber+ ": " + it.next().toString() + NEWLINE);
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
	
	//TODO : implement sort
	public CardList sort(Comparator<Card> pComparator)
	{
		/*
		// Assertions
		assert pComparator != null;
		
		// Clones the current CardList
		CardList copy = this.clone();
		
		// Sorts the clone's cards
		Collections.sort(copy.aCards, pComparator);
		
		// Returns the copied sorted card list
		return copy;
		*/
		return null;
		
	}

}
