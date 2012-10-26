package comp303.fivehundred.ai.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.EnumMap;
import java.util.Iterator;

import comp303.fivehundred.ai.IBiddingStrategy;
import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.Card.Joker;
import comp303.fivehundred.util.Card.Rank;
import comp303.fivehundred.util.Card.BySuitComparator;

/**
 * @author Rayyan Khoury
 * Discards the six lowest non-trump cards. 
 */

public class BasicBiddingStrategy implements IBiddingStrategy
{

	private static final Suit[] SUITS = Suit.values();
	
	private static EnumMap<Suit, Integer> aSuitPoints = new EnumMap<Suit, Integer>(Suit.class);
	
	// Checks whether this hand has already been counted
	private static Hand aCloneHand = new Hand();

	// Comparators to compare by suit
	private static EnumMap<Suit, BySuitComparator> aCompareSuit = new EnumMap<Suit, BySuitComparator>(Suit.class);
	
	// Creates a new basic bidding strategy object
	public BasicBiddingStrategy ()
	{
		
		for (int i = 0; i < SUITS.length; i++)
		{
			
			aSuitPoints.put(SUITS[i], 0);
			aCompareSuit.put(SUITS[i], new BySuitComparator(SUITS[i]));
			
		}
		
	}
	
	@Override
	public Bid selectBid(Bid[] pPreviousBids, Hand pHand)
	{
		
		Suit toBid = null;
		
		if (!handChecked(pHand))
		{
			
			toBid = suitToBid(pHand);
			
		}
		
		
	
	}
	
	private static boolean contractCanBeNoTrump (CardList pCardsSuit)
	{
		
		
		
		return false;
		
	}
	
	/** 
	 * Counts the points in a particular suit
	 * Takes in to account the converse jokers
	 * @assert pCardsSuit is not null
	 * @param pCardsSuit Cards of a particular suit
	 * @param pSuit The suit to check
	 */
	private static int countPointsInSuit (CardList pCardsSuit, Suit pSuit)
	{
		
		// Makes sure the card list is not null
		assert (pCardsSuit != null);
		
		// The current card whose value is to be checked
		Card currentCard = null;
		
		// Sorts the cards by the comparator for this suit
		pCardsSuit.sort(aCompareSuit.get(pSuit));
		
		// Creates the iterator for this card list
		Iterator<Card> it = pCardsSuit.iterator();
		
		// Counts all the points in this suit
		int totalPoints = 0;
		
		// Checks what the point of this card is
		int currentCardPoint = 0;
		
		// While loop to check the value of this card
		while (it.hasNext())
		{
			currentCard = it.next();
			
			// If the card is a joker we do not count the points as we assume a wild card instead of part of the suit
			if (currentCard.isJoker())
			{
				
				continue;
				
			}
			
			// Otherwise card is not a joker and we will count the points
			currentCardPoint = valueOfCardSuit(currentCard, pSuit);
			
			// If the value of the card is 0, then we have counted all the cards worth counting
			if (currentCardPoint == 0)
			{
				
				break;
				
			}

			totalPoints += currentCardPoint;
			
			// Resets the current card point
			currentCardPoint = 0;
			
		}
		
		return totalPoints;
		
	}
	
	/**
	 * Returns the value of the card in the case of a suit game
	 * @param pCard The card to check
	 * @param pSuit The suit currently being checked
	 * @return Value of the card: 4 if Jack of suit, 3 if Jack of converse suit, 2 if Ace of suit, 1 if King of suit
	 */
	private static int valueOfCardSuit (Card pCard, Suit pSuit)
	{
		
		// Makes sure the card is not a joker
		assert (!pCard.isJoker());

		switch (pCard.getRank())
		{
		
			case JACK : 
				
				// If the jack is of this suit
				if (pCard.getSuit().equals(pSuit))
				{

					return 4;
					
				}
				
				// The jack is of the converse suit
				else
				{
					
					return 3;
					
				}
				
			case ACE : return 2;
			case KING : return 1;
			default : return 0;
		
		}	
		
	}
	
	/**
	 * 
	 * Finds the value of the joker
	 * @param pCard
	 * @assert pCard is a joker
	 * @return 4 if HIGH and 3 if LOW
	 */
	private static int valueOfJoker (Card pCard)
	{
		
		assert (pCard.isJoker());
		
		if (pCard.getJokerValue().equals(Joker.HIGH))
		{
			
			return 4;
			
		}
		
		else
		{
			
			return 3;
			
		}
		
	}
	
	/** 
	 * Counts the points in a particular suit
	 * Takes in to account the converse jokers
	 * @param pCardsSuit Cards of a particular suit
	 */
	
	private static Suit suitToBid (Hand pHand)
	{
		
		Suit mostPoints = null;
		
		CardList maxSuitCards = new CardList();
		Suit maxSuit = null;
		int maxPoints = 0;
		
		CardList currentSuitCards = new CardList();
		Suit currentSuit = null;
		int currentPoints = 0;
		
		for (int i = 0; i < SUITS.length; i++) {
			
			currentSuitCards = pHand.getTrumpCards(SUITS[i]);
			currentPoints = countPointsInSuit(currentSuitCards, SUITS[i]);
			aSuitPoints.put(SUITS[i], currentPoints);
			
		}
		
		return mostPoints;
		
	}
	
	/** 
	 * Checks whether this hand has already had
	 * its points counted
	 * @param pHand Hand of the robot
	 * @assert aCloneHand != null
	 * @return True if the hand has already been counted, false otherwise
	 */
	private static boolean handChecked (Hand pHand) 
	{
		
		assert (aCloneHand != null);
		
		if (aCloneHand.equals(pHand))
		{
			
			return true;
			
		}
		
		else
		{
			
			aCloneHand = pHand.clone();
			return false;
			
		}
		
	}

}
