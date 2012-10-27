package comp303.fivehundred.ai.basic;

import java.util.Iterator;

import comp303.fivehundred.ai.IPlayingStrategy;
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

	// Trump suit of this game
	private static Suit trumpSuit;
	
	private static final ByRankComparator COMPARE_BY_RANK = new ByRankComparator();
	
	@Override
	public Card play(Trick pTrick, Hand pHand)
	{

		// Suit of the current hand
		trumpSuit = pTrick.getTrumpSuit();
		
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

			return followingLedJoker(winningCard, pHand);

		}
		
		// CASE 3)
		// ROBOT IS FOLLOWING IN A NO TRUMP GAME
		if (trumpSuit == null)
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
	private static Card isLeading(Hand pHand) 
	{
		
		
		// If the game is in no trump
		if (trumpSuit == null)
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
	 * @param pLeadingCard Joker (high or low)
	 * @param pHand Hand of the robot
	 * @return The card to play following a lead of a joker
	 */
	private static Card followingLedJoker(Card pJoker, Hand pHand)
	{
		
		// If the leading card is a LOW joker and the hand contains the HIGH joker
		if (pJoker.getJokerValue().equals(Joker.LOW) &&
				pHand.getJokers().size() == 1)
		{

			return pHand.getJokers().getFirst();

		}
		
		// Otherwise, returns the most worthless card in response to either joker
		else
		{

			return pHand.selectLowest(trumpSuit);

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
	private static Card followingNoTrumpGame(Hand pHand, Card pWinningCard, Card pLeadingCard)
	{
		
		assert !pWinningCard.isJoker();
		
		// Looks at the cards that are playable given the suit
		CardList noTrumpPlayableCards = pHand.playableCards(pLeadingCard.getSuit(), trumpSuit);
		
		// If there are no cards that can follow suit returns the lowest card from the hand
		if (noTrumpPlayableCards.size() == 0)
		{
			
			return pHand.selectLowest(trumpSuit);
			
		}
		
		// Otherwise the robot chooses a higher card to play
		else
		{
			
			// Otherwise just chooses a card of the suit that can beat the card played
			noTrumpPlayableCards.sort(COMPARE_BY_RANK);
			
			// Returns the card that can beat the current winning trick or the lowest card
			return chooseBeatingCard(noTrumpPlayableCards, pWinningCard);
			
		}
		
	}
	
	private static Card followingTrumpGame(Hand pHand, Card pWinningCard, Card pLeadingCard)
	{
		
		assert !pWinningCard.isJoker();
		
		// CASE 1: SUIT BEING PLAYED IS TRUMP
		if (pLeadingCard.getEffectiveSuit(trumpSuit).equals(trumpSuit))
		{
			
			return followingTrumpGameTrumpLead(pHand, pWinningCard);
			
		}
		
		// CASES: SUIT LED IS NOT THE TRUMP

		CardList followSuit = pHand.getCardsOfNonTrumpSuit(pLeadingCard.getEffectiveSuit(trumpSuit));
		
		// CASE 2: A CARD CAN/MUST FOLLOW SUIT
		if (followSuit.size() > 0)
		{
			
			return followingTrumpGameNonTrumpLeadFollowSuit(followSuit, pWinningCard);
			
		}
		
		// CASE 3: CARDS CANNOT FOLLOW SUIT AND CAN BEAT HIGHEST CARD WITH TRUMP
		
		CardList tryToTrump = pHand.getTrumpCards(trumpSuit);
		tryToTrump.sort(new BySuitComparator(trumpSuit));
		Card highestCard = tryToTrump.getLast();
		
		// The hand contains cards that can beat the highest card
		if (highestCard.compareTo(pWinningCard) > 0)
		{
			
			// If the trick has not been trumped, play lowest trump
			if (!pWinningCard.getEffectiveSuit(trumpSuit).equals(trumpSuit))
			{
				
				return tryToTrump.getFirst();
				
			}
			
			// Otherwise the trick has been trumped
			return chooseBeatingCard(tryToTrump, pWinningCard);
			
		}
		
		// CASE 4: CARDS CANNOT FOLLOW SUIT AND CANNOT BEAT HIGHEST CARD WITH TRUMP
		return pHand.selectLowest(trumpSuit);
		
	}
	
	/**
	 * Returns the card played by the robot in a trump game when the card that was led
	 * was a non trump card and the robot can follow suit.
	 * @param pFollowSuit Card List of cards that can follow suit
	 * @param pWinningCard Current winning card
	 * @return Either lowest card that can beat winning card or lowest card
	 */
	private static Card followingTrumpGameNonTrumpLeadFollowSuit(CardList pFollowSuit, Card pWinningCard)
	{
		
		pFollowSuit.sort(COMPARE_BY_RANK);
		
		// If the trick has been trumped, return the lowest card in this cardlist
		if (pWinningCard.getEffectiveSuit(trumpSuit).equals(trumpSuit))
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
	private static Card followingTrumpGameTrumpLead(Hand pHand, Card pWinningCard)
	{
		
		// The hand contains trumps or jokers
		if (pHand.getTrumpCards(trumpSuit).size() > 0)
		{

			return chooseBeatingCard(pHand.getTrumpCards(trumpSuit).sort(new BySuitComparator(trumpSuit)), pWinningCard);

		}

		// Otherwise discard least valuable card
		else
		{

			return pHand.selectLowest(trumpSuit);

		}
		
	}
	
	/**
	 * Finds and returns the card that can beat the already winning card, 
	 * or returns the lowest card if there are no cards that can beat the winning card.
	 * @param pCardList Sorted CardList in ascending order
	 * @param pWinningCard The card that is currently winning the trick
	 * @return A card just higher than the winning card OR lowest card in list if not found
	 */
	private static Card chooseBeatingCard(CardList pCardList, Card pWinningCard)
	{

		// Creates the iterator for this card list
		Iterator<Card> it = pCardList.iterator();

		Card cardInHand = null;

		// If there is a card just higher than the card currently winning, then returns
		// This card, otherwise it returns the lowest card
		// NOTE: ASSUME CARDS ARE SORTED IN ASCENDING ORDER
		while (it.hasNext())
		{

			cardInHand = it.next();

			if (cardInHand.compareTo(pWinningCard) < 0)
			{
				continue;
			}

			return cardInHand;

		}

		// If there was no card found that could beat the winning card, then plays
		// The lowest card which should be in position one
		return pCardList.getFirst();

	}
	
}