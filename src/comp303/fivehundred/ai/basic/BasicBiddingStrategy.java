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
import comp303.fivehundred.util.Card.BySuitNoTrumpComparator;

/**
 * @author Rayyan Khoury
 * Discards the six lowest non-trump cards. 
 */

public class BasicBiddingStrategy implements IBiddingStrategy
{

	private static final Suit[] SUITS = Suit.values();
	
	// Counts the points in a particular suit given that the robot will bid a trump bid
	private static EnumMap<Suit, Integer> aSuitPointsTrump = new EnumMap<Suit, Integer>(Suit.class);
	
	// Counts the points in a particular suit given that the robot will bid a no trump bid
	private static EnumMap<Suit, Integer> aSuitPointsNoTrump = new EnumMap<Suit, Integer>(Suit.class);

	// Adjusted points taking in to account previous bids
	private static EnumMap<Suit, Integer> aSuitPointsTrumpAdjusted = new EnumMap<Suit, Integer>(Suit.class);
	
	// Adjusted points taking in to account previous bids
	private static EnumMap<Suit, Integer> aSuitPointsNoTrumpAdjusted = new EnumMap<Suit, Integer>(Suit.class);
	
	
	// Checks whether this hand has already been counted
	private static Hand aCloneHand = new Hand();

	// Comparators to compare by suit given the robot will bid a trump bid
	private static EnumMap<Suit, BySuitComparator> aCompareSuitTrump = new EnumMap<Suit, BySuitComparator>(Suit.class);
	
	// Comparators to compare by suit given the robot will bid a no trump bid
	private static EnumMap<Suit, BySuitNoTrumpComparator> aCompareSuitNoTrump = new EnumMap<Suit, BySuitNoTrumpComparator>(Suit.class);
	
	// Points of the cards
	private final static int HIGH_JOKER = 6;
	private final static int LOW_JOKER = 5;
	private final static int FIRST = 4;
	private final static int SECOND = 3;
	private final static int THIRD = 2;
	private final static int FOURTH = 1;
	
	// Threshold of the different kinds of bids
	private final static int TEN = 14;
	private final static int NINE = 12;
	private final static int EIGHT = 10;
	private final static int SEVEN = 8;
	private final static int SIX = 6;
	
	// Points added if the partner has bid a particular trump
	private final static int PARTNER_TEN_TRUMP = 14;
	private final static int PARTNER_NINE_TRUMP = 12;
	private final static int PARTNER_EIGHT_TRUMP = 10;
	private final static int PARTNER_SEVEN_TRUMP = 8;
	private final static int PARTNER_SIX_TRUMP = 6;
	
	// Points added if the partner has bid a no trump bid
	private final static int PARTNER_TEN_NO_TRUMP = 14;
	private final static int PARTNER_NINE_NO_TRUMP = 12;
	private final static int PARTNER_EIGHT_NO_TRUMP = 10;
	private final static int PARTNER_SEVEN_NO_TRUMP = 8;
	private final static int PARTNER_SIX_NO_TRUMP = 6;
	
	// Points taken away if opponent has bid a particular trump
	private final static int OPPONENT_TEN_TRUMP = 14;
	private final static int OPPONENT_NINE_TRUMP = 12;
	private final static int OPPONENT_EIGHT_TRUMP = 10;
	private final static int OPPONENT_SEVEN_TRUMP = 8;
	private final static int OPPONENT_SIX_TRUMP = 6;
	
	// Points taken away if opponent has bid a no trump bid
	private final static int OPPONENT_TEN_NO_TRUMP = 14;
	private final static int OPPONENT_NINE_NO_TRUMP = 12;
	private final static int OPPONENT_EIGHT_NO_TRUMP = 10;
	private final static int OPPONENT_SEVEN_NO_TRUMP = 8;
	private final static int OPPONENT_SIX_NO_TRUMP = 6;
	
	// Minimum possible points per suit for a no trump bid
	private final static int MIN_NO_TRUMP_POINTS = 3;
	
	// Whether we are looking at opponent or partner
	private final static boolean OPPONENT = false;
	private final static boolean PARTNER = true;
	
	// Whether or not the cards in this hand can have a no trump bid
	private boolean noTrumpPossible = false;
	
	
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

	    // The index of the highest bid
	    int highestBidIndex = Bid.max(pPreviousBids).toIndex();
		
		// If this hand has not already been checked by this robot
		if (!handChecked(pHand))
		{
			
			updatePointsOfHand (pHand);
			
		}
		
		// Readjusts points based on previous bids
		adjustPoints(pPreviousBids);


		

		return null;
		
		
		
		
		
	
	}
	
	private static void updatePointsOfHand (Hand pHand)
	{
		
		// Updates the points for a trump and a no trump bid
		for (int i = 0; i < SUITS.length; i++)
		{
			
			// Base points
			aSuitPointsTrump.put(SUITS[i], countPointsInSuit(pHand.getTrumpCards(SUITS[i]), SUITS[i], true));
			aSuitPointsNoTrump.put(SUITS[i], countPointsInSuit(pHand.playableCards(SUITS[i], SUITS[i]), SUITS[i], false));
			
			// Adjusted points (keeps track of all adjustments)
			aSuitPointsTrumpAdjusted.put(SUITS[i], countPointsInSuit(pHand.getTrumpCards(SUITS[i]), SUITS[i], true));
			aSuitPointsNoTrumpAdjusted.put(SUITS[i], countPointsInSuit(pHand.playableCards(SUITS[i], SUITS[i]), SUITS[i], false));
			
		}

	}
	
	private static int adjustPoints(Bid[] pPreviousBids)
	{
		
		Bid partnerBid = null;
		Bid rightBid = null;
		Bid leftBid = null;
		
		// Finds the bid indices of the previous bids to use in the calculation of this bid
		switch(pPreviousBids.length)
		{
		
		case 0 : 
			break;
		case 1 : 
			rightBid = pPreviousBids[0]; 
			break;
		case 2 : 
			rightBid = pPreviousBids[1];
			partnerBid = pPreviousBids[0];
			break;
		case 3 : 
			rightBid = pPreviousBids[2];
			partnerBid = pPreviousBids[1];
			leftBid = pPreviousBids[0];
			break;
		default :
			break;
		
		}
		
		if (leftBid != null)
		{
			
			if (!leftBid.isPass())
			{
				
				int bidStrength = rightBid.getTricksBid();
				Suit bidSuit = rightBid.getSuit();
				
				// Adjustment based on previous points
				aSuitPointsTrumpAdjusted.put(bidSuit , (aSuitPointsTrump.get(bidSuit) - ));
				
			}
			
		}
		
		for (int i = 0; i < SUITS.length; i++)
		{
			
			if (rightBid.equals(SUITS[i]))
			{
				
				int bidStrength = rightBid.getTricksBid();
				aSuitPointsTrump.put(rightSuit, (aSuitPointsTrump.get(rightSuit)));
				
			}
			
			if (leftBid.equals(SUITS[i]))
			{
				
				aSuitPointsTrump.put(rightSuit, (aSuitPointsTrump.get(rightSuit)));
				
			}
			
			if (partnerBid.equals(SUITS[i]))
			{
				
				aSuitPointsTrump.put(rightSuit, (aSuitPointsTrump.get(rightSuit)));
				
				
			}
			
			
		}
		
		if (!rightBid.isPass())
		{
			
			Suit rightSuit = rightBid.getSuit();
			
			if (rightSuit != null)
			{
				
				
				aSuitPointsTrump.put(rightSuit, (aSuitPointsTrump.get(rightSuit)));
				
			}
			
			
		}
		Suit leftSuit = leftBid.getSuit();
		Suit partnerSuit = partnerBid.getSuit();
				
		
		
	}
	
	private static int opponentAdjustmentTrump (int pTricksBid)
	{
		
		switch (pTricksBid)
		{
		
		case 6 : return OPPONENT_SIX_TRUMP;
		case 7 : return OPPONENT_SEVEN_TRUMP;
		case 8 : return OPPONENT_EIGHT_TRUMP;
		case 9 : return OPPONENT_NINE_TRUMP;
		case 10 : return OPPONENT_TEN_TRUMP;
		default : return 0;
		
		}

	}
	
	private static int partnerAdjustmentTrump (int pTricksBid)
	{
		
		switch (pTricksBid)
		{
		
		case 6 : return PARTNER_SIX_TRUMP;
		case 7 : return PARTNER_SEVEN_TRUMP;
		case 8 : return PARTNER_EIGHT_TRUMP;
		case 9 : return PARTNER_NINE_TRUMP;
		case 10 : return PARTNER_TEN_TRUMP;
		default : return 0;
		
		}

	}
	
	private static int bidToIndex (Bid pBid)
	{
		
		if (pBid.isPass())
		{
			
			return 0;
			
		}
		
		return pBid.toIndex()
		
	}

	
	/**
	 * Checks to see whether this hand can play a no trump bid
	 * @return true when hand can play a no trump bid, else false
	 */
	private static boolean noTrumpCheck()
	{
		
		int check = 0;
		
		for (int i = 0; i < SUITS.length; i++)
		{
			
			check = (int) aSuitPointsNoTrump.get(SUITS[i]);
			
			if (check < MIN_NO_TRUMP_POINTS)
			{
				
				return false;
				
			}
			
		}
		
		return true;
		
	}
	
	/** 
	 * Counts the points in a particular suit
	 * Takes in to account the converse jokers when pTrump is true (TRUMP BID)
	 * Does not take in to account converse jokers when pTrump is false (NO TRUMP BID)
	 * @assert pCardsSuit is not null
	 * @param pCardsSuit Cards of a particular suit
	 * @param pSuit The suit to check
	 * @param pTrump Whether we are checking for a trump game or not
	 * @return Points in hand based on whether trump bid or not
	 */
	private static int countPointsInSuit (CardList pCardsSuit, Suit pSuit, boolean pTrump)
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
			currentCardPoint = valueOfCardSuit(currentCard, pSuit, pTrump);
			
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
	 * @assert pCard is not a joker
	 * @param pCard The card to check
	 * @param pSuit The suit currently being checked
	 * @param pTrump Whether we are checking for a trump game or not
	 * @return Value of the card
	 */
	private static int valueOfCardSuit (Card pCard, Suit pSuit, boolean pTrump)
	{
		
		// Makes sure the card is not a joker
		assert (!pCard.isJoker());
		
		// If we are checking for a trump bid
		if (pTrump)
		{
			
			return valueOfCardTrump(pCard,pSuit);
			
		}
		
		// Otherwise we are checking for a no trump bid
		else
		{
			
			return valueOfCardNoTrump(pCard);
			
		}
		
	}
	
	/**
	 * Returns the value of the card when checking for a no trump bid
	 * @param pCard The card to check
	 * @return Value of the card based on: 4:Ace, 3:King, 2:Queen, 1:Jack
	 */
	private static int valueOfCardNoTrump (Card pCard)
	{
		
		switch (pCard.getRank())
		{
			case ACE : return FIRST;
			case KING : return SECOND;
			case QUEEN : return THIRD;
			case JACK : return FOURTH;
			default : return 0;
		}

	}
	
	/**
	 * Returns the value of the card when checking for a trump bid
	 * @param pCard The card to check
	 * @param pSuit Current trump whose bid is being checked
	 * @return 4:Jack(suit), 3:Jack(converse suit), 2:Ace(suit), 1:King(suit)
	 */
	private static int valueOfCardTrump (Card pCard, Suit pSuit)
	{
		
		switch (pCard.getRank())
		{
		
			case JACK : 
				
				// If the jack is of this suit
				if (pCard.getSuit().equals(pSuit))
				{

					return FIRST;
					
				}
				
				// The jack is of the converse suit
				else
				{
					
					return SECOND;
					
				}
				
			case ACE : return THIRD;
			case KING : return FOURTH;
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
			
			return HIGH_JOKER;
			
		}
		
		else
		{
			
			return LOW_JOKER;
			
		}
		
	}
	
	

	/** 
	 * Counts the points in a particular suit
	 * Takes in to account the converse jacks
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
