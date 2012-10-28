package comp303.fivehundred.ai.basic;


import java.util.ArrayList;
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
	
	// Enum Map of biddable suits
	private static EnumMap<Suit, BiddableSuit> allBiddableSuits = new EnumMap<Suit, BiddableSuit>(Suit.class);
	
	// Enum array
	private static Suit[] allSuits = {Suit.HEARTS, Suit.CLUBS, Suit.DIAMONDS, Suit.SPADES};
	
	// Declarations for points and number of cards in threshold checking
	private static BiddableSuit noTrumpBiddable;
	
	// Points for the card order
	private static final int HIGH_JOKER = 6;
	private static final int LOW_JOKER = 5;
	private static final int FIRST_POINTS = 4;
	private static final int SECOND_POINTS = 3;
	private static final int THIRD_POINTS = 2;
	private static final int FOURTH_POINTS = 1;
	private static final int NO_POINTS = 0;
	
	// Minimum thresholds
	private static final int MIN_POINTS_THRESHOLD_TRUMP = 8;
	private static final int MIN_CARDS_THRESHOLD_TRUMP = 4;
	private static final int MIN_POINTS_THRESHOLD_NOTRUMP = 15;
	private static final int LONG_SUIT = 5;
	
	// Partner bid the same suit
	private static final int PARTNER_BID = 5;
	private static final int OPPONENT_BID = -4;
	
	// Updates the points based on previous bids
	private static Suit rightBidSuit = null;
	private static Suit leftBidSuit = null;
	private static Suit partnerBidSuit = null;
	
	private static boolean rightBid = false;
	private static boolean leftBid = false;
	private static boolean partnerBid = false;
	
	
	
	/**
	 * Creates a point based bidding strategy.
	 */
	public BasicBiddingStrategy()
	{
		
		for (int i = 0; i < allSuits.length; i++)
		{
			
			allBiddableSuits.put(allSuits[i], new BiddableSuit(allSuits[i]));
			
		}

		noTrumpBiddable = new BiddableSuit(null);
		
	}
	
	@Override
	public Bid selectBid(Bid[] pPreviousBids, Hand pHand)
	{
		
		// Reset all the biddables to default values
		resetAllBiddables();
		
		// Arraylist to hold all possible bids
		ArrayList<Bid> possibleBids = new ArrayList<Bid>();
		
		// Checks whether suits are biddable not based on opponent/partner bids
		updateSuitsBiddable(pHand);
		
		// Updates the suits of the previous bids
		updatePreviousBids(pPreviousBids);
		
		// Updates the points based on previous bids
		if (rightBid)
		{
		
			updateSuitsBiddablePreviousBids(rightBidSuit, OPPONENT_BID);
			
		}
		
		if (leftBid)
		{
			
			updateSuitsBiddablePreviousBids(leftBidSuit, OPPONENT_BID);
			
		}
			
		if (partnerBid)
		{
			
			updateSuitsBiddablePreviousBids(partnerBidSuit, PARTNER_BID);
			
		}
		
		BiddableSuit bestBid = chooseBestBiddableSuit();
		
		if (bestBid == null)
		{
			
			return new Bid();
			
		}
		
		accountForJokers(bestBid, pHand);

		
		return null;
		
	}
	
	private static void accountForJokers(BiddableSuit bestBid, Hand pHand)
	{
		
		CardList jokersInHand = pHand.getJokers();
		
		if (jokersInHand.size() == 2)
		{
			
			bestBid.addPoints(HIGH_JOKER + LOW_JOKER);
			
		}
		
		if (jokersInHand.size() == 1)
		{
			
			if (jokersInHand.getFirst().getJokerValue().equals(Joker.HIGH))
			{
				
				bestBid.addPoints(HIGH_JOKER);
				
			}
			
			else 
			{
				
				bestBid.addPoints(LOW_JOKER);
				
			}
			
		}
		
	}
	
	private static BiddableSuit chooseBestBiddableSuit()
	{
		
		
		
		
	}
	

	
	private static void updatePreviousBids(Bid[] pPreviousBids)
	{
		
		rightBid = false;
		leftBid = false;
		partnerBid = false;
		
		rightBidSuit = null;
		leftBidSuit = null;
		partnerBidSuit = null;
		
		switch(pPreviousBids.length)
		{
		
		case 0 : 
			break;
			
		case 1 : 
			if (!pPreviousBids[0].isPass())
			{
				rightBidSuit = pPreviousBids[0].getSuit();
				rightBid = true;
			}
			
			break;
			
		case 2 : 
			
			if (!pPreviousBids[0].isPass())
			{
				partnerBidSuit = pPreviousBids[0].getSuit();
				partnerBid = true;
			}
			
			if (!pPreviousBids[1].isPass())
			{
				rightBidSuit = pPreviousBids[1].getSuit();
				rightBid = true;
			}
			
			break;
			
		case 3 : 
			if (!pPreviousBids[2].isPass())
			{
				rightBidSuit = pPreviousBids[2].getSuit();
				rightBid = true;
			}
			
			if (!pPreviousBids[1].isPass())
			{
				partnerBidSuit = pPreviousBids[1].getSuit();
				partnerBid = true;
			}
			
			if (!pPreviousBids[0].isPass())
			{
				leftBidSuit = pPreviousBids[0].getSuit();
				leftBid = true;
			}
			
			break;
			
		default :
			break;
		
		}
		
	}

	private static void updateSuitsBiddablePreviousBids(Suit pSuit , int pBidVariance)
	{

		BiddableSuit temp = null;

		if (pSuit == null)
		{

			noTrumpBiddable.setPoints(noTrumpBiddable.getPoints() + pBidVariance);

		}

		else
		{

			temp = allBiddableSuits.get(pSuit);
			temp.setPoints(temp.getPoints() + pBidVariance);

		}

	}


	private static void updateSuitsBiddable(Hand pHand)
	{
		
		for (int i = 0; i < allSuits.length; i++)
		{
			
			allBiddableSuits.get(allSuits[i]).update(pHand);
			
		}
		
		noTrumpBiddable.update(pHand);
		
	}
	
	/**
	 * Resets all the biddable objects.
	 */
	private static void resetAllBiddables()
	{
		
		for (int i = 0; i < allSuits.length; i++)
		{
			
			allBiddableSuits.get(allSuits[i]).reset();
			
		}
		
		noTrumpBiddable.reset();
		
	}
	
	private static int pointsNoTrump(Card pCard)
	{
		
		switch (pCard.getRank())
		{
			case ACE : return FIRST_POINTS;
			case KING : return SECOND_POINTS;
			case QUEEN : return THIRD_POINTS;
			case JACK : return FOURTH_POINTS;
			default : return 0;
		}
		
	}
	
	private static int pointsTrump(Card pCard, Suit pTrump)
	{
		
		switch (pCard.getRank())
		{
		
			case JACK : 
				
				// If the jack is of this suit
				if (pCard.getSuit().equals(pTrump))
				{

					return FIRST_POINTS;
					
				}
				
				// The jack is of the converse suit
				else
				{
					
					return SECOND_POINTS;
					
				}
				
			case ACE : return THIRD_POINTS;
			case KING : return FOURTH_POINTS;
			default : return NO_POINTS;
		
		}	
		
	}
	

	
	/**
	 * Creates a new BiddableSuit object, used to store the number of points per suit,
	 * the number of cards per suit, and a boolean whether the object is biddable.
	 * @author Rayyan Khoury
	 *
	 */
	private final class BiddableSuit 
	{
		
		private final Suit aSuit;
		private boolean aBiddable;
		private int aPoints;
		private int aCards;
		private final BySuitComparator aSuitComparator;
		
		private BiddableSuit(Suit pSuit)
		{
			
			aSuit = pSuit;
			aBiddable = false;
			aPoints = 0;
			aCards = 0;
			aSuitComparator = new BySuitComparator(pSuit);
			
		}
		
		private void reset()
		{
			
			aBiddable = false;
			aPoints = 0;
			aCards = 0;
			
		}
		
		private void update(Hand pHand)
		{
			
			// If updating for a no trump bid
			if (aSuit == null)
			{
				
				updateNoTrump(pHand);
				
			}
			
			// Otherwise updating for a trump bid
			updateTrump(pHand);
			
			
		}
		
		private void updateNoTrump(Hand pHand)
		{
			
			// All the trump cards of this suit in this hand
			CardList noJokers = pHand.getNonJokers();
			Card currentCard = null;
			
			// Creates the iterator for this card list
			Iterator<Card> it = noJokers.iterator();
			
			// Counters
			int totalPoints = 0;
			int currentCardPoint = 0;
			int cardCount = 0;
			
			// While loop to check the value of this card
			while(it.hasNext())
			{
				currentCard = it.next();
				
				// Counts the raw point value
				currentCardPoint = pointsNoTrump(currentCard);
				
				if (currentCardPoint > 0)
				{
					
					cardCount++;
					
				}

				totalPoints = totalPoints + currentCardPoint;
				
				// Resets the current card point
				currentCardPoint = 0;
				
			}
			
			setNoTrumpPoints(totalPoints);
			setNumberCards(cardCount);
			setBiddableNoTrump();
			
		}
		
		private void updateTrump(Hand pHand)
		{

			// All the trump cards of this suit in this hand
			CardList trumpCards = pHand.getTrumpCards(aSuit);
			trumpCards.sort(aSuitComparator);
			Card currentCard = null;
			
			// Creates the iterator for this card list
			Iterator<Card> it = trumpCards.iterator();
			
			// Counters
			int totalPoints = 0;
			int currentCardPoint = 0;
			int cardCount = 0;
			
			// While loop to check the value of this card
			while(it.hasNext())
			{
				currentCard = it.next();
				
				// If we have reached the jokers, we break as we do not count jokers
				if (currentCard.isJoker())
				{
					
					break;
					
				}
				
				cardCount++;
				
				// Otherwise card is not a joker and we will count the points
				currentCardPoint = pointsTrump(currentCard, aSuit);

				totalPoints = totalPoints + currentCardPoint;
				
				// Resets the current card point
				currentCardPoint = 0;
				
			}
			
			setNumberCards(cardCount);
			setTrumpPoints(totalPoints);
			setBiddableTrump();
			
		}
		
		
		private Suit getSuit()
		{
			
			return aSuit;
			
		}
		
		private boolean getBiddable()
		{
			
			return aBiddable;
			
		}
		
		private void setBiddableTrump()
		{
			
			if ((getPoints() > MIN_POINTS_THRESHOLD_TRUMP) && getNumberCards() > MIN_CARDS_THRESHOLD_TRUMP)
			{
				
				aBiddable = true;
				
			}
			
		}
		
		private void setBiddableNoTrump()
		{
			
			if (getPoints() > MIN_POINTS_THRESHOLD_NOTRUMP)
			{
				
				aBiddable = true;
				
			}
			
		}
		
		private int getPoints()
		{
			
			return aPoints;
			
		}
		
		private void setTrumpPoints(int pPoints)
		{
			
			int longSuit = 0;
			if (getNumberCards() >= LONG_SUIT)
			{
				
				longSuit = getNumberCards() - LONG_SUIT + 1;
				
			}
			aPoints = pPoints + longSuit;
			
		}
		
		private void setPoints(int pPoints)
		{
			
			aPoints = pPoints;
			
		}
		
		private void addPoints(int pPoints)
		{
			
			aPoints = aPoints + pPoints;
			
		}
		
		private void setNoTrumpPoints(int pPoints)
		{
			
			aPoints = pPoints;
			
		}
		
		private int getNumberCards()
		{
			
			return aCards;
			
		}
		
		private void setNumberCards(int pCards)
		{
			
			aCards = pCards;
			
		}
		
		
	}
	
	
}