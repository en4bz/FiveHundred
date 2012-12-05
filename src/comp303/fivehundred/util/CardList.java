package comp303.fivehundred.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * A mutable list of cards. Does not support duplicates.
 * The cards are maintained in the order added.
 * @author Ian Forbes
 * 
 */
@SuppressWarnings("serial")
public class CardList extends ArrayList<Card> implements Cloneable, Iterable<Card>
{
	/**
	 * Creates a new, empty card list.
	 */
	public CardList()
	{
		super();
	}
	
	public void addCardArray(Card[] pCardArray)
	{
		assert pCardArray != null;
		this.addAll(Arrays.asList(pCardArray));
	}
		
	/**
	 * @return The first card in the list, according to whatever
	 * order is currently being used. 
	 * @throws GameUtilException if the list is empty.
	 */
	public Card getFirst()
	{
		// Throws an exception if the card list is empty
		if (this.isEmpty()) 
		{
			throw new GameUtilException("CARD LIST EMPTY - cannot retrieve first card");
		}
		return this.get(0);
	}
	
	@Override
	public boolean add(Card pCard){
		if(this.contains(pCard)){
			return false;
		}
		else{
			return super.add(pCard);
		}
	}
	
	/**
	 * @return The last card in the list, according to whatever
	 * order is currently being used. 
	 * @throws GameUtilException If the list is empty.
	 */
	public Card getLast()
	{
		// Throws an exception if the card list is empty
		if (this.isEmpty()) 
		{
			throw new GameUtilException("CARD LIST EMPTY - cannot retrieve last card");
		}
		// Returns the last card using (n-1) indexing
		return this.get(this.size() - 1);
	}
	
	/**
	 * @see java.lang.Object#clone()
	 * {@inheritDoc}
	 */

	@Override
	public CardList clone()
	{
		return (CardList) super.clone();
	}
	
	@Override
	public String toString()
	{
		String toReturn = "";
	    for(Card c : this){
	    	toReturn += c.toShortString();
	    	toReturn += ",";
	    }
	    
	    return toReturn.subSequence(0, toReturn.length()-1).toString();
	}
	
	/**
	 * @pre this.size() > 0
	 * @return A randomly-chosen card from the set. 
	 */
	public Card random()
	{
		// Assertions
		assert this != null;
		assert this.size() > 0;
		
		// Random object
		Random aNum = new Random();

		// Finding a random number between 0 and size
		int size = this.size();
	    int index = aNum.nextInt(size);
	    
	    // Returns the random card
		return this.get(index);
		
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
		assert pComparator != null;
		CardList copy = this.clone();
		Collections.sort(copy, pComparator);
		return copy;
	}
	
	/**
	 * Returns another CardList in reverse order. This
	 * method has no side effects.
	 * @return the reversed CardList
	 */

	public CardList reverse()
	{
		CardList copy = this.clone();
		Collections.reverse(copy);
		return copy;
	}

	@Override
	public int hashCode()
	{
		return this.toString().hashCode();
	}
}

