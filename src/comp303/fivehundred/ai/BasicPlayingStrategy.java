package comp303.fivehundred.ai;

import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.ByRankComparator;
import comp303.fivehundred.util.Card.BySuitComparator;
import comp303.fivehundred.util.Card.Joker;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;

/**
 * @author Rayyan Khoury
 * If leading, picks a card at random except jokers if playing in no trump.
 * If following, choose the lowest card that can follow suit and win. 
 * If no card can follow suit and win, picks the lowest card that can follow suit.
 * If no card can follow suit, picks the lowest trump card that can win.
 * If there are no trump card or the trump cards can�t win 
 * (because the trick was already trumped), then picks the lowest card.
 * If a joker was led, dump the lowest card unless it can be beaten with the high joker 
 * according to the rules of the game.
 */
public class BasicPlayingStrategy implements IPlayingStrategy
{
	private static final ByRankComparator COMPARE_BY_RANK = new ByRankComparator();
	
	// Trump suit of this game
	private Suit aTrumpSuit;
	
	@Override
	public Card play(Trick pTrick, Hand pHand)
	{
		// Suit of the current hand
		aTrumpSuit = pTrick.getTrumpSuit();
		// CASE 1)
		// ROBOT IS LEADING
		if (pTrick.size() == 0) 
		{
			return isLeading(pHand);
		}
		// Leading card
		Card leadingCard = pTrick.cardLed();
		
		// The card to beat
		Card winningCard = pTrick.highest();
		
		// CASE 2)
		// ROBOT IS FOLLOWING A WINNING JOKER
		if (winningCard.isJoker())
		{
			return followingWinningJoker(winningCard, pHand, leadingCard);
		}
		
		// CASE 3)
		// ROBOT IS FOLLOWING IN A NO TRUMP GAME
		if (aTrumpSuit == null)
		{
			return followingNoTrumpGame(pHand, winningCard, leadingCard);
		}
		
		// CASE 4)
		// ROBOT IS FOLLOWING IN A TRUMP GAME
		return followingTrumpGame(pHand, winningCard, leadingCard);
	}
	
	/**
	 * Chooses a card to lead; if the game is a trump game it leads a random card.
	 * Otherwise, in a no trump game, it leads a non joker unless it MUST lead a joker.
	 * @param pTrump Trump of the game
	 * @param pHand Hand of the robot
	 * @return Card to lead
	 */
	private Card isLeading(Hand pHand) 
	{
		// If the game is in no trump
		if (aTrumpSuit == null)
		{
			return pHand.canLead(true).random();
		}
		// Otherwise the game is a trump game
		else
		{
			return pHand.canLead(false).random();
		}
	}
	
	/**
	 * Returns a high joker if the hand contains it and the leading card 
	 * was a low joker. Otherwise returns the lowest card.
	 * If the leading card was not a joker but it was played, calls
	 * leadingCardNotJokerFollowingJoker.
	 * @param pWinningCard Joker (high or low)
	 * @param pHand Hand of the robot
	 * @param pLeadingCard The leading card (not necessarily a joker)
	 * @return The card to play following a winning joker
	 */
	private Card followingWinningJoker(Card pWinningCard, Hand pHand, Card pLeadingCard)
	{
		// If the leading card is not a joker
		if (!pLeadingCard.isJoker())
		{
			return leadingCardNotJokerFollowingJoker(pWinningCard, pHand, pLeadingCard);
		}
		// If the leading card is a LOW joker and the hand contains the HIGH joker
		if (pWinningCard.getJokerValue().equals(Joker.LOW) &&
				pHand.getJokers().size() == 1)
		{
			return pHand.getJokers().getFirst();
		}
		// Otherwise, returns the most worthless card in response to either joker
		else
		{
			return pHand.selectLowest(aTrumpSuit);
		}
	}
	
	/**
	 * Returns the card that can legally follow a winning joker which was not led.
	 * @param pWinningCard Current winning card
	 * @param pHand Hand of the robot
	 * @param pLeadingCard Leading card
	 * @return The card that can legally follow a winning joker which was not led
	 */
	private Card leadingCardNotJokerFollowingJoker(Card pWinningCard, Hand pHand, Card pLeadingCard)
	{
		//Suit leadingCardSuit = pLeadingCard.getEffectiveSuit(aTrumpSuit);
		if (pWinningCard.getJokerValue().equals(Joker.HIGH))
		{
			return winningCardHighJoker(pWinningCard, pHand, pLeadingCard);
		}
		else if (pHand.getJokers().size() > 0)
		{
			CardList joker = pHand.getJokers();
			return joker.getFirst();
		}
		else
		{
			CardList playableCards = pHand.playableCards(pLeadingCard.getEffectiveSuit(aTrumpSuit), aTrumpSuit);
			// There are no valid playable cards
			if (playableCards.size() == 0)
			{
				CardList nonJokers = pHand.getNonJokers();
				if(aTrumpSuit != null)
				{
					return nonJokers.sort(new BySuitComparator(aTrumpSuit)).getFirst();
				}
				else
				{
					return nonJokers.sort(new Card.BySuitNoTrumpComparator()).getFirst();
				}
				
			}
			else
			{
				playableCards.sort(new BySuitComparator(aTrumpSuit));
				return playableCards.getFirst();
			}
		}
	}
	
	/**
	 * Returns the card that can legally follow a winning joker which was not led.
	 * @param pWinningCard Current winning card
	 * @param pHand Hand of the robot
	 * @param pLeadingCard Leading card
	 * @return The card that can legally follow a winning joker which was not led
	 */
	private Card winningCardHighJoker(Card pWinningCard, Hand pHand, Card pLeadingCard)
	{
		CardList nonJokers = pHand.getNonJokers();
		
		// There is only the low joker
		if (nonJokers.size() == 0)
		{
			return pHand.getJokers().getFirst();
		}
		if (aTrumpSuit != null)
		{
			return nonJokers.sort(new BySuitComparator(aTrumpSuit)).getFirst();
		}
		else
		{
			return nonJokers.sort(new Card.BySuitNoTrumpComparator()).getFirst();
		}
		
	}
	/**
	 * If the hand can follow suit, returns the card that can beat the highest 
	 * card played, or if the hand cannot beat it, returns the lowest card in the suit.
	 * If the hand cannot follow suit, then returns the lowest card.
	 * @assert pHighestCard != joker
	 * @param pHand
	 * @param pWinningCard
	 * @param pLeadingCard
	 * @return
	 */
	private Card followingNoTrumpGame(Hand pHand, Card pWinningCard, Card pLeadingCard)
	{
		assert !pWinningCard.isJoker();
		// Looks at the cards that are playable given the suit
		CardList noTrumpPlayableCards = pHand.playableCards(pLeadingCard.getSuit(), aTrumpSuit);
		
		// If there are no cards that can follow suit returns the lowest card from the hand
		if (noTrumpPlayableCards.size() == 0)
		{
			return pHand.selectLowest(aTrumpSuit);
		}
		
		// Otherwise the robot chooses a higher card to play
		else
		{
			// Otherwise just chooses a card of the suit that can beat the card played
			// Returns the card that can beat the current winning trick or the lowest card
			return chooseBeatingCard(noTrumpPlayableCards.sort(COMPARE_BY_RANK), pWinningCard);
		}
	}
	
	/**
	 * Returns the card to play when following a trump game according to:
	 * 1) If suit led is trump
	 * 2) If suit led is not trump and:
	 * 		a) The card can/must follow suit.
	 * 		b) Card cannot follow suit and can beat winning card with trump.
	 * 		c) Card cannot follow suit and cannot beat winning card.
	 * @param pHand Hand of robot
	 * @param pWinningCard Winning Card
	 * @param pLeadingCard Leading card
	 * @return Card to play
	 */
	private Card followingTrumpGame(Hand pHand, Card pWinningCard, Card pLeadingCard)
	{
		assert !pWinningCard.isJoker();
		// CASE 1: SUIT BEING PLAYED IS TRUMP
		if (pLeadingCard.getEffectiveSuit(aTrumpSuit).equals(aTrumpSuit))
		{
			return followingTrumpGameTrumpLead(pHand, pWinningCard);
		}
		// CASES: SUIT LED IS NOT THE TRUMP
	
		// CASE 2: A CARD CAN/MUST FOLLOW SUIT
		if (pHand.getCardsOfNonTrumpSuit(pLeadingCard.getEffectiveSuit(aTrumpSuit)) != null)
		{
			if (pHand.getCardsOfNonTrumpSuit(pLeadingCard.getEffectiveSuit(aTrumpSuit)).size() > 0)
			{
				return followingTrumpGameNonTrumpLeadFollowSuit(pHand.getCardsOfNonTrumpSuit(pLeadingCard.getEffectiveSuit(aTrumpSuit)), pWinningCard);
			}
		}
		// CASE 3: CARDS CANNOT FOLLOW SUIT AND CAN BEAT HIGHEST CARD WITH TRUMP
		CardList tryToTrump = pHand.getTrumpCards(aTrumpSuit);
		tryToTrump = tryToTrump.sort(new BySuitComparator(aTrumpSuit));
		
		// The hand contains trumps
		if (tryToTrump.size() > 0)
		{
			// If the trick has not been trumped, play lowest trump
			if (!pWinningCard.getEffectiveSuit(aTrumpSuit).equals(aTrumpSuit))
			{
				return tryToTrump.getFirst();
			}
			// Otherwise the trick has been trumped and tries to find a higher trump or plays the lowest card
			else
			{
				CardList tryToTrumpAgain = pHand.reverse();
				tryToTrumpAgain = tryToTrumpAgain.sort(new BySuitComparator(aTrumpSuit));
				// If the method chooses the lowest trump 
				return chooseBeatingCard(tryToTrumpAgain, pWinningCard);
			}
		}
		else
		{
			// CASE 4: CARDS CANNOT FOLLOW SUIT AND CANNOT BEAT HIGHEST CARD WITH TRUMP
			return pHand.selectLowest(aTrumpSuit);
		}
	}
	
	/**
	 * Returns the card played by the robot in a trump game when the card that was led
	 * was a non trump card and the robot can follow suit.
	 * @param pFollowSuit Card List of cards that can follow suit
	 * @param pWinningCard Current winning card
	 * @return Either lowest card that can beat winning card or lowest card
	 */
	private Card followingTrumpGameNonTrumpLeadFollowSuit(CardList pFollowSuit, Card pWinningCard)
	{
		pFollowSuit.sort(COMPARE_BY_RANK);
		// If the trick has been trumped, return the lowest card in this cardlist
		if (pWinningCard.getEffectiveSuit(aTrumpSuit).equals(aTrumpSuit))
		{
			return pFollowSuit.getFirst();
		}
		
		// If the trick has not been trumped, return the lowest possible winning card
		// Or the lowest card
		return chooseBeatingCard(pFollowSuit, pWinningCard);
	}
	
	
	/**
	 * Returns the card played by the robot in a trump game when the card that was led
	 * was a trump card.
	 * @param pHand Hand of robot
	 * @param pWinningCard Current winning card
	 * @return Card played
	 */
	private Card followingTrumpGameTrumpLead(Hand pHand, Card pWinningCard)
	{
		// The hand contains trumps or jokers
		if (pHand.getTrumpCards(aTrumpSuit).size() > 0)
		{
			return chooseBeatingCard(pHand.getTrumpCards(aTrumpSuit).sort(new BySuitComparator(aTrumpSuit)), pWinningCard);
		}
		// Otherwise discard least valuable card
		else
		{
			return pHand.selectLowest(aTrumpSuit);
		}
	}
	
	/**
	 * Finds and returns the card that can beat the already winning card, 
	 * or returns the lowest card if there are no cards that can beat the winning card.
	 * @param pCardList Sorted CardList in ascending order
	 * @param pWinningCard The card that is currently winning the trick
	 * @return A card just higher than the winning card OR lowest card in list if not found
	 */
	private Card chooseBeatingCard(CardList pCardList, Card pWinningCard)
	{
		// If there is a card just higher than the card currently winning, then returns
		// This card, otherwise it returns the lowest card
		// NOTE: ASSUME CARDS ARE SORTED IN ASCENDING ORDER
		for(Card c : pCardList)
		{
			// If the winning card is a trump
			if (pWinningCard.getEffectiveSuit(aTrumpSuit).equals(aTrumpSuit))
			{
				// If the card being checked is a joker
				if (c.isJoker())
				{
					return c;
				}
				// If the card being checked is not a trump
				if (!c.getEffectiveSuit(aTrumpSuit).equals(aTrumpSuit))
				{
					continue;
				}
			}
			if (c.compareTo(pWinningCard) < 0)
			{
				continue;
			}
			return c;
		}
		// If there was no card found that could beat the winning card, then plays
		// The lowest card which should be in position one
		return pCardList.getFirst();
	}
}