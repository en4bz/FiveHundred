package comp303.fivehundred.ai;

import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.ByRankComparator;
import comp303.fivehundred.util.Card.BySuitComparator;
import comp303.fivehundred.util.Card.BySuitNoTrumpComparator;
import comp303.fivehundred.util.Card.Joker;
import comp303.fivehundred.util.Card.Rank;
import comp303.fivehundred.util.CardList;
import comp303.fivehundred.util.Card.Suit;


/**
 * @author Rayyan Khoury
 * Has a much more fine tuned strategy than BasicPlayingStrategy
 * Checks the discards and widow to make sure of discards, but it seems that this doesn't improve the robot's performance
 */

public class AdvancedPlayingStrategy implements IPlayingStrategy
{
	
	// Compare by rank comparator
	private static ByRankComparator compareByRank = new ByRankComparator();

	// Total number of trumps possible
	private int NUMBER_OF_TOTAL_TRUMPS = 14;

	// CardLists of the discarded widow and all other discards
	private static Hand aDiscards;
	private static Hand aWidow;

	// CardList representing all cards that can be seen
	private static Hand aAllCardsSeen;

	// Boolean representing whether this robot is the contractor
	private static boolean contractor;

	// Trump suit of this game
	private static Suit aTrumpSuit;

	// All suits
	private static Suit[] aSuits = new Suit[] {Suit.SPADES, Suit.CLUBS, Suit.DIAMONDS, Suit.HEARTS};

	// All ranks
	private static Rank[] aRanks = new Rank[] {Rank.ACE, Rank.KING, Rank.QUEEN, Rank.JACK, Rank.TEN, Rank.NINE, Rank.EIGHT, Rank.SEVEN, Rank.SIX, Rank.FIVE, Rank.FOUR};

	// All trump ranks
	private static Rank[] aTrumpRanks = new Rank[] {Rank.ACE, Rank.KING, Rank.QUEEN, Rank.TEN, Rank.NINE, Rank.EIGHT, Rank.SEVEN, Rank.SIX, Rank.FIVE, Rank.FOUR};


	// Rank of the cards in order
	//private static ArrayList<Rank>aRankCards = new ArrayList<Rank> () {Rank.FOUR};
	
	// Observer
    private AIObserver aObserver;
    
	public AdvancedPlayingStrategy(AIObserver pObserver)
	{
		aObserver = pObserver;
	}
	
	@Override
	public Card play(Trick pTrick, Hand pHand)
	{
		return play(pTrick, pHand, aObserver);
	}

	/**
	 * The new play call
	 * @param pTrick
	 * @param pHand
	 * @param pObserver
	 * @return
	 */
	public Card play(Trick pTrick, Hand pHand, AIObserver pObserver)
	{

		// Sets variables
		Hand handClone = pHand.clone();
		aDiscards = null;
		aWidow = null;
		aAllCardsSeen = new Hand();

		// TODO: Checks if this robot is the contractor
		contractor = pObserver.isContractor();

		// Updates what cards can be seen
		updateCardsSeen(pObserver);

		// Suit of the current hand
		aTrumpSuit = pTrick.getTrumpSuit();
		// CASE 1)
		// ROBOT IS LEADING
		if (pTrick.size() == 0) 
		{
			return isLeading(handClone, aTrumpSuit);
		}

		// Leading card
		Card leadingCard = pTrick.cardLed();

		// The card to beat
		Card winningCard = pTrick.highest();
		
		// CASE 2)
		// ROBOT IS FOLLOWING A LEADING JOKER (TRUMP)
		if (leadingCard.isJoker() && (aTrumpSuit != null))
		{
			
			return followingTrumpGameJoker(leadingCard, handClone);
			
		}
		
		// CASE 3)
		// ROBOT IS FOLLOWING A LEADING TRUMP
		if (aTrumpSuit != null)
		{
			
			return followingTrumpGame(leadingCard, winningCard, handClone, pTrick);
			
		}
		
		// CASE 4)
		// ROBOT IS FOLLOWING A LEADING NO TRUMP GAME
		if (aTrumpSuit == null)
		{
			
			return followingNoTrumpGame(leadingCard, winningCard, pTrick, handClone);
			
		}
		
		// Contingency return
		return pHand.selectLowest(aTrumpSuit);

	}

	/**
	 * Updates the cards that can be seen by this robot
	 * @param pObserver
	 */
	private static void updateCardsSeen(AIObserver pObserver)
	{

		// if i am the contractor
		if (contractor)
		{
			aDiscards = pObserver.getDiscardedCards();
			aWidow = pObserver.getDiscardedWidow();

			// Gets all cards from the discards
			for (Card card : aDiscards) 
			{
				aAllCardsSeen.add(card);
			}

			// Gets all cards from the widow
			for (Card card : aWidow)
			{
				aAllCardsSeen.add(card);
			}

		}

		else
		{
			aDiscards = pObserver.getDiscardedCards();

			for (Card card : aDiscards) 
			{
				aAllCardsSeen.add(card);
			}

		}

	}


	/**
	 * If the robot is leading
	 * @param pHand
	 * @param pTrumpSuit
	 * @return
	 */
	private Card isLeading(Hand pHand, Suit pTrumpSuit) 
	{

		if (pTrumpSuit != null)
		{

			if (contractor) 
				return trumpLeadContractor(pHand, pTrumpSuit);
			else 
				return trumpLeadNotContractor(pHand, pTrumpSuit);

		}

		else
			return noTrumpLead(pHand);

	}

	/**
	 * This robot is leading in a no trump game
	 * If the hand has nothing but jokers, play the joker
	 *  Otherwise look at the other cards and remove the jokers
	 * @param pHand
	 * @return
	 */
	private Card noTrumpLead(Hand pHand)
	{

		// gets non jokers
		Hand nonJokers = new Hand(pHand.getNonJokers());

		// returns a joker if there is nothing else to play
		if (nonJokers.size() == 0)
		{

			return pHand.random();

		}

		BySuitNoTrumpComparator compareNoTrump = new BySuitNoTrumpComparator();

		nonJokers = new Hand(nonJokers.sort(compareNoTrump));
		nonJokers = new Hand(nonJokers.reverse());

		ByRankComparator compareByRank = new ByRankComparator();

		// If there are cards other than the jokers, look at what cards have already been played and see if there is any potential to win


		// Highest card
		// Order is Spades, Clubs, Diamonds, Hearts
		Card[] myHighestCards = new Card[4];
		CardList[] myCards = new CardList[4];

		Rank[] highestDiscard = new Rank[4];
		CardList[] discards = new CardList[4];

		// If there are cards other than the jokers, look at what cards have already been played and see if there is any potential to win
		for (int i = 0; i < myCards.length; i++)
		{

			myCards[i] = nonJokers.getCardsOfNonTrumpSuit(aSuits[i]).sort(compareByRank).reverse();
			discards[i] = aAllCardsSeen.getCardsOfNonTrumpSuit(aSuits[i]).sort(compareByRank).reverse();
			
			if (myCards[i].size() > 0)
			{
				
				myHighestCards[i] =  myCards[i].getFirst();
				
			}

			else
			{
				
				myHighestCards[i] = null;
				
			}

		}

		for (int i = 0; i < discards.length; i++)
		{

			int j = 0;
			for (Card card : discards[i])
			{

				if (card.getRank().equals(aRanks[j]))
				{

					j++;
					continue;

				}

				else {

					highestDiscard[i] = aRanks[j];
					break;

				}
			}

		}

		CardList winnableCards = new CardList();

		// Find if there is a higher card to play
		for (int i = 0; i < myHighestCards.length; i++)
		{
			
			if (myHighestCards[i] == null)
			{
				continue;
			}

			if (highestDiscard[i] == null)
			{
				winnableCards.add(myHighestCards[i]);
				continue;
			}

			if (myHighestCards[i].getRank().compareTo(highestDiscard[i]) >= 0)
			{

				winnableCards.add(myHighestCards[i]);

			}

		}

		// If this hand has any cards that can win
		if (winnableCards.size() > 0)
		{

			return winnableCards.getFirst();

		}

		// Otherwise just plays a random low card
		return nonJokers.selectLowest(null);

	}

	/**
	 * This robot is leading in a trump game
	 * @param pHand
	 * @param pTrumpSuit
	 * @return
	 */
	private Card trumpLeadContractor(Hand pHand, Suit pTrumpSuit)
	{

		CardList trumpsInDiscards = aAllCardsSeen.getTrumpCards(pTrumpSuit);
		CardList trumpsInHand = pHand.getTrumpCards(pTrumpSuit);
		
		if (trumpsInHand.size() == 0)
		{
			
			return noTrumpLead(pHand);
			
		}

		int amountOfTrumpsOut = trumpsInDiscards.size() + trumpsInHand.size();

		// No need to force any trumps out
		if (amountOfTrumpsOut == NUMBER_OF_TOTAL_TRUMPS)
		{

			return noTrumpLead(pHand);

		}

		// If there is only one trump in the hand, hold on to it for future purposes
		if (trumpsInHand.size() == 1 || trumpsInHand.size() == 0)
		{

			// Otherwise just plays a random low card
			return pHand.selectLowest(pTrumpSuit);

		}

		BySuitComparator compareTrump = new BySuitComparator(pTrumpSuit);
		Card highestTrumpInHand = trumpsInHand.sort(compareTrump).reverse().getFirst();
		trumpsInDiscards = trumpsInDiscards.sort(compareTrump).reverse();
		
		// Plays the joker
		if (highestTrumpInHand.isJoker())
		{
			
			return highestTrumpInHand;
			
		}
		
		// If there are no trumps in discards
		if (trumpsInDiscards.size() == 0)
		{
			
			return highestTrumpInHand;
			
		}
		
		// Checking for jokers
		if (trumpsInDiscards.getFirst().isJoker())
		{
			
			// If the discards contains the high joker
			if (trumpsInDiscards.getFirst().getJokerValue().equals(Joker.HIGH))
			{
				
				// return the lowest trump
				return trumpsInHand.getLast();
				
			}
			
			// Otherwise it is the low joker
			else
			{
				
				// If we have the high joker
				if (highestTrumpInHand.isJoker())
				{
					
					return highestTrumpInHand;
				}
				
				else
				{
					
					// return the lowest trump
					return trumpsInHand.getLast();
					
				}
				
			}
			
		}
		
		// Checking for converse jacks
		if (trumpsInDiscards.getFirst().getRank().equals(Rank.JACK))
		{
			
			// If the discards contains the high jack
			if (trumpsInDiscards.getFirst().getSuit().equals(aTrumpSuit))
			{
				
				// return the lowest trump
				return trumpsInHand.getLast();
				
			}
			
			// Otherwise it is the converse jack
			else
			{
				
				// If we have the high jack
				if (highestTrumpInHand.getSuit().equals(aTrumpSuit))
				{
					
					return highestTrumpInHand;
				}
				
				else
				{
					
					// return the lowest trump
					return trumpsInHand.getLast();
					
				}
				
			}
			
		}

		
		Card toBeat = null;
		int j = 0;
		
		// Jokers and jacks are taken care of by this point
		for (Card card : trumpsInDiscards)
		{

			// Jacks problem
			if (card.getRank().equals(aTrumpRanks[j]))
			{
				j++;
				continue;

			}

			else
			{

				toBeat = card;
				break;

			}
		}
		// Plays highest trump card to eat up trumps
		if (highestTrumpInHand.compareTo(toBeat) >= 0)
		{

			return highestTrumpInHand;

		}

		// Plays lowest trump card to force out trumps
		return trumpsInHand.getLast();

	}

	/**
	 * Leading a trump game but not the contractor
	 * @param pHand
	 * @param pTrumpSuit
	 * @return
	 */
	private Card trumpLeadNotContractor(Hand pHand, Suit pTrumpSuit)
	{

		// If we can eat up trumps eat them
		CardList trumpsInHand = pHand.getTrumpCards(pTrumpSuit);
		CardList trumpsInDiscards = aAllCardsSeen.getTrumpCards(pTrumpSuit);
		
		if (trumpsInHand.size() == 0 || trumpsInHand.size() == 1)
		{
			
			return noTrumpLead(pHand);
			
		}
		
		BySuitComparator compareTrump = new BySuitComparator(pTrumpSuit);
		Card highestTrumpInHand = trumpsInHand.sort(compareTrump).reverse().getFirst();
		return highestTrumpInHand;
		
/*
		BySuitComparator compareTrump = new BySuitComparator(pTrumpSuit);
		Card highestTrumpInHand = trumpsInHand.sort(compareTrump).reverse().getFirst();
		trumpsInDiscards = trumpsInDiscards.sort(compareTrump).reverse();
		Card toBeat = null;

		boolean highJokerFound = false;
		boolean jackSuitFound = false;
		int i = 0;
		int j = 0;

		for (Card card : trumpsInDiscards)
		{

			// If the first/second cards are jokers
			if (card.isJoker() && (i==0 || i==1))
			{

				if (card.getJokerValue().equals(Joker.HIGH))
				{
					i++;
					highJokerFound = true;
					continue;

				}

				if (card.getJokerValue().equals(Joker.LOW) && highJokerFound)
				{
					i++;
					continue;

				}

				else
				{

					toBeat = card;
					break;
				}
			}

			// Jacks problem
			if (card.getRank().equals(aTrumpRanks[j]) && card.getSuit().equals(pTrumpSuit))
			{
				jackSuitFound = true;
				j++;
				continue;

			}

			else if (card.getRank().equals(aTrumpRanks[j]) && card.getSuit().equals(pTrumpSuit.getConverse()) && jackSuitFound)
			{

				j++;
				continue;

			}

			else
			{

				toBeat = card;
				break;

			}
		}
		
		System.out.println("trumpLeadNotContractor: " + toBeat);

		// Plays highest trump card to eat up trumps
		if (highestTrumpInHand.compareTo(toBeat) >= 0)
		{

			return highestTrumpInHand;

		}

		// Otherwise try and play a winning card
		// Otherwise play a signal card
		// Otherwise play a low card

		return noTrumpLead(pHand);
		*/

	}

	/**
	 * Following a trump game
	 * @param pLeadingCard
	 * @param pWinningCard
	 * @param pHand
	 * @param pTrick
	 * @return
	 */
	private static Card followingTrumpGame(Card pLeadingCard, Card pWinningCard, Hand pHand, Trick pTrick)
	{
		
		// If leading card is a trump
		if (pLeadingCard.getEffectiveSuit(aTrumpSuit).equals(aTrumpSuit))
		{
			
			return followingTrumpCard(pLeadingCard, pWinningCard, pHand, pTrick);
			
		}
		
		// If leading card is not a trump
		else
		{
			
			return followingNonTrumpCard(pLeadingCard, pWinningCard, pHand, pTrick);
			
		}
		
	}
	
	/**
	 * Following a trump game in which a trump card was led
	 * @param pLeadingCard
	 * @param pWinningCard
	 * @param pHand
	 * @param pTrick
	 * @return
	 */
	private static Card followingTrumpCard(Card pLeadingCard, Card pWinningCard, Hand pHand, Trick pTrick)
	{
		
		BySuitComparator compareTrump = new BySuitComparator(aTrumpSuit);
		Hand trumps = new Hand(pHand.getTrumpCards(aTrumpSuit).sort(compareTrump).reverse());
		
		// If I have trumps
		if (trumps.size() > 0)
		{
			
			Card highestTrump = trumps.getFirst();
			
			// If I cannot beat the winning card
			if (highestTrump.compareTo(pWinningCard) < 0)
			{
				
				return trumps.getLast();
				
			}
			
			// else I can beat the winning card
			else
			{
				
				if (pTrick.size() == 2)
				{
					
					return highestTrump;
					
				}
				
				else
				{
					
					trumps = new Hand(trumps.reverse());
					// If I am second or fourth player, I will play the next card above
					for (Card card : trumps)
					{
						
						if (card.compareTo(pWinningCard) > 0)
						{
							
							return card;
							
						}
						
					}
					
					// Contingency return
					return trumps.getFirst();
					
				}
				
			}
			
		}
		
		// Cannot follow trump
		else
		{
			
			return pHand.selectLowest(aTrumpSuit);
			
		}
		
		
	}
	
	/**
	 * Following a trump game in which a non trump card was led
	 * @param pLeadingCard
	 * @param pWinningCard
	 * @param pHand
	 * @param pTrick
	 * @return
	 */
	private static Card followingNonTrumpCard(Card pLeadingCard, Card pWinningCard, Hand pHand, Trick pTrick)
	{
		
		Hand playableCards = new Hand(pHand.getCardsOfNonTrumpSuit(pLeadingCard.getEffectiveSuit(aTrumpSuit)).sort(compareByRank).reverse());

		// Must follow suit
		if (playableCards.size() > 0)
		{
			Card highestCard = playableCards.getFirst();
			// If i can beat the winning card
			if (highestCard.compareTo(pWinningCard) > 0)
			{

				if (pTrick.size() == 2)
				{

					return highestCard;

				}

				else
				{

					playableCards = new Hand(playableCards.reverse());
					// If I am second or fourth player, I will play the next card above
					for (Card card : playableCards)
					{

						if (card.compareTo(pWinningCard) > 0)
						{

							return card;

						}

					}

					// Contingency return
					return playableCards.getFirst();

				}
			}
			
			// I cannot beat the winning card
			else
			{
				
				return playableCards.getLast();
				
				
			}
			
		}
		
		// Otherwise can check if I can trump because I cannot follow suit
		else
		{
			
			return couldTrumpFollowingNonTrumpCard(pLeadingCard, pWinningCard, pHand, pTrick);
			
		}
		
	}
	
	/**
	 * The robot is unable to follow suit
	 * @param pLeadingCard
	 * @param pWinningCard
	 * @param pHand
	 * @param pTrick
	 * @return
	 */
	private static Card couldTrumpFollowingNonTrumpCard(Card pLeadingCard, Card pWinningCard, Hand pHand, Trick pTrick)
	{
		
		BySuitComparator compareTrump = new BySuitComparator(aTrumpSuit);
		Hand trumpsInHand = new Hand(pHand.getTrumpCards(aTrumpSuit).sort(compareTrump).reverse());
		
		if (trumpsInHand.size() > 0)
		{
			
			Card highestTrump = trumpsInHand.getFirst();
			// I can beat by trumping
			if (highestTrump.compareTo(pWinningCard) > 0)
			{
				
				if (pTrick.size() == 2)
				{

					return highestTrump;

				}

				else
				{

					trumpsInHand = new Hand(trumpsInHand.reverse());
					// If I am second or fourth player, I will play the next card above
					for (Card card : trumpsInHand)
					{

						if (card.compareTo(pWinningCard) > 0)
						{

							return card;

						}

					}

					// Contingency return
					return trumpsInHand.getFirst();

				}
				
			}
			
			// I cannot beat by trumping
			else
			{
				
				return pHand.selectLowest(aTrumpSuit);
				
			}
			
		}
		
		// I cannot trump
		else
		{
			
			return pHand.selectLowest(aTrumpSuit);
			
		}
		
	}
	

	/**
	 * The robot is folowing a trump game that was started by a joker
	 * @param pLeadingCard
	 * @param pHand
	 * @return
	 */
	private static Card followingTrumpGameJoker(Card pLeadingCard, Hand pHand)
	{
		
		BySuitComparator compareTrump = new BySuitComparator(aTrumpSuit);
		
		// If the leading card is a low joker and the hand contains the high joker
		// Play the high joker to win
		if (pLeadingCard.getJokerValue().equals(Joker.LOW) && (pHand.getJokers().size() > 0))
		{
			
			return pHand.getJokers().getFirst();
			
		}
		
		// Hand cannot beat the joker
		else
		{
			
			// Check to see if we can follow suit
			Hand canBePlayed = new Hand(pHand.getCardsOfNonTrumpSuit(aTrumpSuit));
			
			// We can follow suit
			if (canBePlayed.size() > 0)
			{
				
				return canBePlayed.sort(compareTrump).getFirst();
				
				
			}
			
			// Make sure we dont have the low joker
			// If we do we must play it
			if (pHand.getJokers().size() > 0)
			{
				
				return pHand.getJokers().getFirst();
				
			}
			
			// We cannot follow suit and so play the lowest card in another suit
			return pHand.selectLowest(null);
			
		}
		
	}

	/**
	 * The robot is following a no trump game
	 * @param pLeadingCard
	 * @param pWinningCard
	 * @param pTrick
	 * @param pHand
	 * @return
	 */
	private static Card followingNoTrumpGame(Card pLeadingCard, Card pWinningCard, Trick pTrick, Hand pHand)
	{

		// If the leading card is a joker
		if (pLeadingCard.isJoker())
		{
			
			return followingNoTrumpGameLeadingJoker(pLeadingCard, pWinningCard, pTrick, pHand);
			
		}
		
		// If the winning card is a joker
		if (pWinningCard.isJoker())
		{
			
			return followingNoTrumpGameWinningJoker(pLeadingCard, pWinningCard, pTrick, pHand);
			
		}
		
		// Check what my highest card is
		Suit currentSuit = pLeadingCard.getSuit();
		Hand cardsOfSuit = new Hand(pHand.getCardsOfNonTrumpSuit(currentSuit));
		
		// If I can follow suit
		if (cardsOfSuit.size() > 0)
		{
			
			return followingNoTrumpGameCanFollowSuit(currentSuit, cardsOfSuit, pWinningCard, pTrick, pHand);
			
		}
		
		// If I cannot follow suit
		else
		{
			// If I have a joker
			if (pHand.getJokers().size() > 0)
			{
				
				return pHand.getJokers().getFirst();
				
			}
			
			// If I dont have a joker
			return pHand.selectLowest(null);
			
		}


	}
	
	/**
	 * The robot is following a no trump game with a leading joker
	 * @param pLeadingCard
	 * @param pWinningCard
	 * @param pTrick
	 * @param pHand
	 * @return
	 */
	private static Card followingNoTrumpGameLeadingJoker(Card pLeadingCard, Card pWinningCard, Trick pTrick, Hand pHand)
	{
		
		// If the leading card is a low joker and the hand contains the high joker
		if (pLeadingCard.getJokerValue().equals(Joker.LOW) && (pHand.getJokers().size() > 0))
		{
			
			return pHand.getJokers().getFirst();
			
		}
		
		else
		{
			
			return pHand.selectLowest(null);
			
		}
		
	}
	
	/**
	 * The robot is following a no trump game with a winning joker
	 * @param pLeadingCard
	 * @param pWinningCard
	 * @param pTrick
	 * @param pHand
	 * @return
	 */
	private static Card followingNoTrumpGameWinningJoker(Card pLeadingCard, Card pWinningCard, Trick pTrick, Hand pHand)
	{
		
		
		// If the winning card is a low joker and the hand contains the high joker
		if (pWinningCard.getJokerValue().equals(Joker.LOW) && (pHand.getJokers().size() > 0))
		{
			
			return pHand.getJokers().getFirst();
			
		}
		
		// Otherwise try and follow suit
		else
		{
			
			Hand followSuit = new Hand(pHand.getCardsOfNonTrumpSuit(pLeadingCard.getSuit()));
			
			// If we can follow suit
			if (followSuit.size() > 0)
			{
				
				return followSuit.sort(compareByRank).getFirst();
				
			}
			
			// Otherwise return a random low card
			return pHand.selectLowest(null);
			
		}
	}
	
	/**
	 * The robot is following a no trump game and can follow suit
	 * @param currentSuit
	 * @param cardsOfSuit
	 * @param pWinningCard
	 * @param pTrick
	 * @param pHand
	 * @return
	 */
	private static Card followingNoTrumpGameCanFollowSuit(Suit currentSuit, Hand cardsOfSuit, Card pWinningCard, Trick pTrick, Hand pHand)
	{
		
		cardsOfSuit = new Hand(cardsOfSuit.sort(compareByRank).reverse());
		Card highestCard = cardsOfSuit.getFirst();
		
		// If I cannot beat the current game
		if (highestCard.compareTo(pWinningCard) < 0)
		{
			
			// If I have a joker
			if (pHand.getJokers().size() > 0)
			{
				
				return pHand.getJokers().getFirst();
				
			}
			
			// If I dont have a joker
			return cardsOfSuit.getLast();
			
		}
		
		// If I can beat the current game
		// If I am third player I will play my highest card
		if (pTrick.size() == 2)
		{
			
			return highestCard;
			
		}
		
		else
		{
			
			cardsOfSuit = new Hand(cardsOfSuit.reverse());
			// If I am second or fourth player, I will play the next card above
			for (Card card : cardsOfSuit)
			{
				
				if (card.compareTo(pWinningCard) > 0)
				{
					
					return card;
					
				}
				
			}
			
			// Contingency return
			return cardsOfSuit.getFirst();
			
		}

		
	}
	
}
