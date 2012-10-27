package comp303.fivehundred.ai.basic;


import java.util.EnumMap;
import java.util.Iterator;

import comp303.fivehundred.ai.IBiddingStrategy;
import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.Card.Joker;
import comp303.fivehundred.util.Card.BySuitComparator;
import comp303.fivehundred.util.Card.BySuitNoTrumpComparator;

/**
 * @author Rayyan Khoury
 * Supports a point-based bidding strategy
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
	private static BySuitNoTrumpComparator aCompareSuitNoTrump;
	
	// Points of the cards
	private static final int HIGH_JOKER = 6;
	private static final int LOW_JOKER = 5;
	private static final int FIRST = 4;
	private static final int SECOND = 3;
	private static final int THIRD = 2;
	private static final int FOURTH = 1;
	
	// Threshold of the different kinds of bids for a trump bid
	private static final int TEN_TRUMP = 14;
	private static final int NINE_TRUMP = 12;
	private static final int EIGHT_TRUMP = 10;
	private static final int SEVEN_TRUMP = 8;
	private static final int SIX_TRUMP = 6;
	
	// Threshold of the different kinds of bids for a no trump bid
	private static final int TEN_NO_TRUMP = 29;
	private static final int NINE_NO_TRUMP = 24;
	private static final int EIGHT_NO_TRUMP = 20;
	private static final int SEVEN_NO_TRUMP = 16;
	private static final int SIX_NO_TRUMP = 12;
	
	// Points added if the partner has bid a particular trump
	private static final int PARTNER_TEN_TRUMP = 14;
	private static final int PARTNER_NINE_TRUMP = 12;
	private static final int PARTNER_EIGHT_TRUMP = 10;
	private static final int PARTNER_SEVEN_TRUMP = 8;
	private static final int PARTNER_SIX_TRUMP = 6;

	// Points taken away if opponent has bid a particular trump
	private static final int OPPONENT_TEN_TRUMP = 14;
	private static final int OPPONENT_NINE_TRUMP = 12;
	private static final int OPPONENT_EIGHT_TRUMP = 10;
	private static final int OPPONENT_SEVEN_TRUMP = 8;
	private static final int OPPONENT_SIX_TRUMP = 6;

	
	// Minimum possible points per suit for a no trump bid
	private static final int MIN_NO_TRUMP_POINTS = 3;
	
	// Whether we are looking at opponent or partner
	private static final boolean OPPONENT = false;
	private static final boolean PARTNER = true;
	
	// Whether or not the cards in this hand can have a no trump bid
	private static boolean noTrumpPossible = false;
	
	// Number of points in the no trump hand
	private static int totalNoTrumpPoints = 0;
	private static int noTrumpValue = 0;
	
	/**
	 * 
	 */
	public BasicBiddingStrategy()
	{
		
		for (int i = 0; i < SUITS.length; i++)
		{
			
			aSuitPointsTrump.put(SUITS[i], 0);
			aSuitPointsNoTrump.put(SUITS[i], 0);
			aSuitPointsTrumpAdjusted.put(SUITS[i], 0);
			aSuitPointsNoTrumpAdjusted.put(SUITS[i], 0);
			aCompareSuitTrump.put(SUITS[i], new BySuitComparator(SUITS[i]));
			aCompareSuitNoTrump = new BySuitNoTrumpComparator();
			
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
			noTrumpPossible = noTrumpCheck();
			
		}
		
		// Readjusts points based on previous bids
		adjustPointsTrump(pPreviousBids);

		return adjustedBid(highestBidIndex);
		
	}
	
	private static Bid adjustedBid(int pHighestPreviousBid)
	{
		
		Suit biddableSuit = null;
		int pointsBiddableSuit = 0;
		
		Bid noTrumpBid = null;
		Bid trumpBid = null;
		
		for (int i = 0; i < SUITS.length; i++)
		{
			
			if (pointsBiddableSuit < aSuitPointsTrumpAdjusted.get(SUITS[i]))
			{
				
				biddableSuit = SUITS[i];
				pointsBiddableSuit = aSuitPointsTrumpAdjusted.get(SUITS[i]);
				
			}
			
			else if (pointsBiddableSuit == aSuitPointsTrumpAdjusted.get(SUITS[i]) && pointsBiddableSuit > 0)
			{
				
				if (biddableSuit.compareTo(SUITS[i]) < 0)
				{
					
					biddableSuit = SUITS[i];
					pointsBiddableSuit = aSuitPointsTrumpAdjusted.get(SUITS[i]);
					
				}
				
			}
			
		}
		
		if (noTrumpPossible)
		{
			
			noTrumpValue = totalNoTrumpPoints;
			noTrumpBid = getNoTrumpBid(noTrumpValue);
			
			
		}
		
		trumpBid = getTrumpBid(biddableSuit, pointsBiddableSuit);
		
		if (trumpBid.compareTo(noTrumpBid) < 0)
		{
			
			return noTrumpBid;
			
		}
		
		else
		{
			
			return trumpBid;
			
		}
		
	}
	
	private static Bid getNoTrumpBid (int noTrumpValue)
	{
		
		if (noTrumpValue < SIX_NO_TRUMP)
		{
			
			return new Bid();
			
		}
		
		else if (noTrumpValue < SEVEN_NO_TRUMP && noTrumpValue >= SIX_NO_TRUMP)
		{
			
			return new Bid(6, null);
			
		}
		
		else if (noTrumpValue < EIGHT_NO_TRUMP && noTrumpValue >= SEVEN_NO_TRUMP)
		{
			
			return new Bid(7, null);
			
		}
		
		else if (noTrumpValue < NINE_NO_TRUMP && noTrumpValue >= EIGHT_NO_TRUMP)
		{
			
			return new Bid(8, null);
			
		}
		
		else if (noTrumpValue < TEN_NO_TRUMP && noTrumpValue >= NINE_NO_TRUMP)
		{
			
			return new Bid(9, null);
			
		}
		
		else
		{
			
			return new Bid(10, null);
			
		}
		
		
	}
	
	private static Bid getTrumpBid(Suit pSuit, int pPoints)
	{
		
		if (pPoints < SIX_TRUMP)
		{
			
			return new Bid();
			
		}
		
		else if (pPoints < SEVEN_TRUMP && pPoints >= SIX_TRUMP)
		{
			
			return new Bid(6, pSuit);
			
		}
		
		else if (pPoints < EIGHT_TRUMP && pPoints >= SEVEN_TRUMP)
		{
			
			return new Bid(7, pSuit);
			
		}
		
		else if (pPoints < NINE_TRUMP && pPoints >= EIGHT_TRUMP)
		{
			
			return new Bid(8, pSuit);
			
		}
		
		else if (pPoints < TEN_TRUMP && pPoints >= NINE_TRUMP)
		{
			
			return new Bid(9, pSuit);
			
		}
		
		else
		{
			
			return new Bid(10, pSuit);
			
		}
		
	}
	
	/**
	 * Updates the points of a both a no trump and a trump game in both the adjusted and unadjusted point maps
	 * @param pHand The hand of the robot
	 */
	private static void updatePointsOfHand (Hand pHand)
	{
		
		// Updates the points for a trump and a no trump bid
		for (int i = 0; i < SUITS.length; i++)
		{
			
			int noTrumpPoints = countPointsInSuit(pHand.playableCards(SUITS[i], SUITS[i]), SUITS[i], false);
			
			// Base points
			aSuitPointsTrump.put(SUITS[i], countPointsInSuit(pHand.getTrumpCards(SUITS[i]), SUITS[i], true));
			aSuitPointsNoTrump.put(SUITS[i], noTrumpPoints);
			
			// Adjusted points (keeps track of all adjustments)
			aSuitPointsTrumpAdjusted.put(SUITS[i], countPointsInSuit(pHand.getTrumpCards(SUITS[i]), SUITS[i], true));
			
			// Updates total points of no trump
			totalNoTrumpPoints += noTrumpPoints;
			
		}

	}
	
	/**
	 * Adjust the points of a trump game in the adjusted point max
	 * @param pPreviousBids The previous bids of all players
	 */
	private static void adjustPointsTrump(Bid[] pPreviousBids)
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
			adjustPointsPreviousBids(rightBid, OPPONENT);
			break;
		case 2 : 
			rightBid = pPreviousBids[1];
			partnerBid = pPreviousBids[0];
			adjustPointsPreviousBids(partnerBid, PARTNER);
			adjustPointsPreviousBids(rightBid, OPPONENT);
			break;
		case 3 : 
			rightBid = pPreviousBids[2];
			partnerBid = pPreviousBids[1];
			leftBid = pPreviousBids[0];
			adjustPointsPreviousBids(partnerBid, PARTNER);
			adjustPointsPreviousBids(rightBid, OPPONENT);
			adjustPointsPreviousBids(leftBid, OPPONENT);
			break;
		default :
			break;
		
		}
		
	}
	
	/**
	 * Adjusts points in favor of this robot if the partner bid a suit, and against if the opponents bid a suit
	 * @assert pBid is not null
	 * @param pBid The Bid of either the partner or the player
	 * @param pPlayer A boolean stating whether the bid is of the partner or the opponent
	 */
	private static void adjustPointsPreviousBids (Bid pBid, boolean pPlayer)
	{

		assert (pBid != null);

		if (!pBid.isPass())
		{

			int bidStrength = pBid.getTricksBid();
			Suit bidSuit = pBid.getSuit();

			// Adjustment based on previous points

			// The adjustment is in favor of this robot
			if (pPlayer == PARTNER)
			{

				aSuitPointsTrumpAdjusted.put(bidSuit, (aSuitPointsTrump.get(bidSuit) + partnerAdjustmentTrump(bidStrength)));

			}

			// The adjustment is not in favor of this robot
			else
			{

				aSuitPointsTrumpAdjusted.put(bidSuit, Math.max(0,(aSuitPointsTrump.get(bidSuit) - opponentAdjustmentTrump(bidStrength))));

			}
			
		}

	}
	
	/**
	 * Gets the value of the readjustment when an opponent bids a suit
	 * @param pTricksBid The number of tricks bid by the opponent
	 * @return The value of the readjustment based on the opponent's bid
	 */
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
	
	/**
	 * Gets the value of the readjustment when the partner bids a suit
	 * @param pTricksBid The number of tricks bid by the partner
	 * @return The value of the readjustment based on the partner's bid
	 */
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
		pCardsSuit.sort(aCompareSuitTrump.get(pSuit));
		
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
