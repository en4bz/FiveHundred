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
	
	// No trump biddable suit
	private static BiddableSuit noTrumpBiddable;
	
	// Updates the points based on previous bids
	private static Suit rightBidSuit = null;
	private static Suit leftBidSuit = null;
	private static Suit partnerBidSuit = null;
	
	private static boolean rightBid = false;
	private static boolean leftBid = false;
	private static boolean partnerBid = false;
	
	// Points for the card order
	private static final int HIGH_JOKER = 6;
	private static final int LOW_JOKER = 5;
	private static final int FIRST_POINTS = 4;
	private static final int SECOND_POINTS = 3;
	private static final int THIRD_POINTS = 2;
	private static final int FOURTH_POINTS = 1;
	private static final int NO_POINTS = 0;
	
	// Minimum thresholds
	private static final int MIN_POINTS_THRESHOLD_TRUMP = 7;
	private static final int MIN_CARDS_THRESHOLD_TRUMP = 4;
	private static final int MIN_POINTS_THRESHOLD_NOTRUMP = 9;
	private static final int LONG_SUIT = 5;
	
	// Partner bid the same suit
	private static final int PARTNER_BID = 5;
	private static final int OPPONENT_BID = -4;
	
	// Thresholds
	private static final int MIN_SIX = 8;
	private static final int MIN_SEVEN = 10;
	private static final int MIN_EIGHT = 13;
	private static final int MIN_NINE = 17;
	private static final int MIN_TEN = 20;
	
	// BIDS
	private static final int SIX = 6;
	private static final int SEVEN = 7;
	private static final int EIGHT = 8;
	private static final int NINE = 9;
	private static final int TEN = 10;
	
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
		
		BiddableSuit bestBid = bestBiddable();
		
		// If there is no biddable suit, return a pass
		if (bestBid == null)
		{
			
			return new Bid();
			
		}
		
		// Takes in to account the jokers in calculating the points
		accountForJokers(bestBid, pHand);
		
		// If the bid cannot meet the threshold
		if (bestBid.getPoints() < MIN_SIX)
		{
			
			return new Bid();
			
		}
		
		// Gets the best bid
		Bid toBid = getBid(bestBid);
		
		// If the best bid is higher than the maximum bid, returns this bid
		if (toBid.compareTo(Bid.max(pPreviousBids)) > 0)
		{
			
			return toBid;
			
		}
		
		// Otherwise passes
		return new Bid();
		
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
	
	
	/**
	 * Updates all biddable objects to the current hand's point and card count values.
	 * Updates whether this suit is biddable or not.
	 * @param pHand
	 */
	private static void updateSuitsBiddable(Hand pHand)
	{
		
		for (int i = 0; i < allSuits.length; i++)
		{
			
			allBiddableSuits.get(allSuits[i]).updateTrump(pHand);
			
		}
		
		noTrumpBiddable.updateNoTrump(pHand);
		
	}
	
	
	/**
	 * Updates the previous bids based on the passed in Bid array
	 * Sets the global BIDS to true if that person has bid a non pass,
	 * and sets the global SUITS to the suit of that bid.
	 * @param pPreviousBids
	 */
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
	
	
	/**
	 * Updates the points in the suits/ no trump biddable objects based on whether the opponents
	 * or the partner has bid (positive if the partner has bid, and negative if the opponents have bid).
	 * @param pSuit The suit of the biddable object
	 * @param pBidVariance The variance +- of points to add to the current points in the object
	 */
	private static void updateSuitsBiddablePreviousBids(Suit pSuit, int pBidVariance)
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

	/**
	 * Returns the best biddable object, or null if there are no possible bids.
	 * @return temp Best biddable suit CAN RETURN NULL
	 */
	private static BiddableSuit bestBiddable()
	{
		
		BiddableSuit temp = null;
		BiddableSuit highest = null;
		int highestPoints = 0;
		
		for (int i = 0; i < allSuits.length; i++)
		{
			
			temp = allBiddableSuits.get(allSuits[i]);
			
			if (temp.getBiddable() && (temp.getPoints() > highestPoints))
			{
				
				highest = temp;
				highestPoints = highest.getPoints();
				
			}
			
		}
		
		if (noTrumpBiddable.getBiddable() && (noTrumpBiddable.getPoints() > highestPoints))
		{
			
			highest = noTrumpBiddable;
			highestPoints = highest.getPoints();
			
		}
		
		// No biddable to return
		return highest;
		
	}
	
	private static void accountForJokers(BiddableSuit pBestBid, Hand pHand)
	{
		
		CardList jokersInHand = pHand.getJokers();
		
		if (jokersInHand.size() == 2)
		{
			
			pBestBid.addPoints(HIGH_JOKER + LOW_JOKER);
			
		}
		
		if (jokersInHand.size() == 1)
		{
			
			if (jokersInHand.getFirst().getJokerValue().equals(Joker.HIGH))
			{
				
				pBestBid.addPoints(HIGH_JOKER);
				
			}
			
			else 
			{
				
				pBestBid.addPoints(LOW_JOKER);
				
			}
			
		}
		
	}
	
	
	/**
	 * Point association for cards in the no trump bid.
	 * @param pCard Card to check (suit does not matter)
	 * @return Integer representing the amount of points this card is worth
	 */
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
	
	/**
	 * Point association for cards in the trump bid.
	 * @param pCard Card to check (suit matters)
	 * @return Integer representing the amount of points this card is worth
	 */
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
	 * Gets the best bid using the biddable object and working with external thresholds.
	 * @param pBiddable The biddable suit object of the highest points
	 * @return The bid which most represents this robot's cards
	 */
	private static Bid getBid(BiddableSuit pBiddable)
	{
		
		int points = Math.abs(pBiddable.getPoints());
		Suit suit = pBiddable.getSuit();
		
		if (points >= MIN_SIX && points < MIN_SEVEN)
		{
			
			return new Bid(SIX, suit);
			
		}
		
		else if (points >= MIN_SEVEN && points < MIN_EIGHT)
		{
			
			return new Bid(SEVEN, suit);
			
		}
		
		else if (points >= MIN_EIGHT && points < MIN_NINE)
		{
			
			return new Bid(EIGHT, suit);
			
		}
			
		else if (points >= MIN_NINE && points < MIN_TEN)
		{
			
			return new Bid(NINE, suit);

		}

		else 
		{

			return new Bid(TEN, suit);
			
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
		
		// Suit of this biddable object ; can be null for a no trump
		private final Suit aSuit;
		
		// A way to arrange cards in increasing order
		private final BySuitComparator aSuitComparator;
		
		// Whether or not this suit is biddable
		private boolean aBiddable;
		
		// Number of points of suit / HAND if no trump
		// Does not include jokers
		private int aPoints;
		
		// Number of cards in suit
		private int aCards;
		
		/**
		 * Creates the new biddable object.
		 * @param pSuit The suit of the biddable object
		 */
		private BiddableSuit(Suit pSuit)
		{
			
			aSuit = pSuit;
			aBiddable = false;
			aPoints = 0;
			aCards = 0;
			aSuitComparator = new BySuitComparator(pSuit);
			
		}
		
		/**
		 * Resets the biddable object by reseting non-final values to
		 * default values.
		 */
		private void reset()
		{
			
			aBiddable = false;
			aPoints = 0;
			aCards = 0;
			
		}

		/**
		 * Updates the amount of points the hand has in terms of no trump.
		 * Updates the parameter biddable to represent whether this is biddable
		 * @param pHand Hand to be checked
		 */
		private void updateNoTrump(Hand pHand)
		{
			
			// All the suit cards in this hand
			CardList noJokers = pHand.getNonJokers();
			Card currentCard = null;
			
			// Creates the iterator for this card list
			Iterator<Card> it = noJokers.iterator();
			
			// Counters
			int totalPoints = 0;
			int currentCardPoint = 0;
			
			// While loop to check the value of this card
			while(it.hasNext())
			{
				currentCard = it.next();
				
				// Counts the raw point value
				currentCardPoint = pointsNoTrump(currentCard);

				totalPoints = totalPoints + currentCardPoint;
				
				// Resets the current card point
				currentCardPoint = 0;
				
			}
			
			aPoints = totalPoints;
			
			if(aPoints >= MIN_POINTS_THRESHOLD_NOTRUMP)
			{
				
				aBiddable = true;
				
				
			}
			
		}
		
		/**
		 * Updates the amount of points the hand has in terms of a trump bid.
		 * @param pHand Hand to be checked
		 */
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
			
			aPoints = totalPoints;
			aCards = cardCount;
			
			// Adds points for a long suit
			aPoints = aPoints + aCards - LONG_SUIT + 1;
			
			if(aPoints >= MIN_POINTS_THRESHOLD_TRUMP && aCards >= MIN_CARDS_THRESHOLD_TRUMP)
			{
				
				aBiddable = true;
				
				
			}
			
		}
		
		/**
		 * Returns whether this suit is biddable or not.
		 * @return True if biddable, false if not
		 */
		private boolean getBiddable()
		{
			
			return aBiddable;
			
		}

		/**
		 * Gets the points in this hand in terms of no trump/ trump bid.
		 * @return Amount of points for this suit/no trump
		 */
		private int getPoints()
		{
			
			return aPoints;
			
		}
		
		private void setPoints(int pPoints)
		{
			
			aPoints = pPoints;
			
		}
		
		private void addPoints(int pPoints)
		{
			
			aPoints = aPoints + pPoints;
			
		}
		
		private Suit getSuit()
		{
			
			return aSuit;
			
		}
		
	}
	
	
}