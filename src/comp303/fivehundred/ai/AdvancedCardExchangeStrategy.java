package comp303.fivehundred.ai;

import java.util.ArrayList;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.ByRankComparator;
import comp303.fivehundred.util.Card.BySuitNoTrumpComparator;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;
import comp303.fivehundred.util.Card.BySuitComparator;

public class AdvancedCardExchangeStrategy extends BasicCardExchangeStrategy implements ICardExchangeStrategy 
{
	
	// Number of cards to discard
	private static final int NUMBER_CARDS_DISCARD = 6;
	
	// Number of cards to discard
	protected static Hand aDiscards;
	
	// Number of cards to retain
	private static CardList aRetain;
	
	// CardList temporary container
	private static CardList aTemp;
	
	@Override
	public CardList selectCardsToDiscard(Bid[] pBids, int pIndex, Hand pHand)
	{
		
		//aDiscards = new CardList();
		aRetain = new CardList();
		aTemp = new CardList();
		aDiscards = pHand.clone();
		
		// Trump game
		if (Bid.max(pBids).getSuit().equals(null))
		{
			
			trumpExchange(pBids, pIndex);
			
		}
		
		// No trump game just discards the lowest cards
		else
		{
			
			super.selectCardsToDiscard(pBids, pIndex, pHand);
			
		}
		
		return aDiscards;
		
	}
	
	// removes cards from discards
	// adds them to retain
	// checks if the size of discards has reached six and if so returns this
	private static boolean switchCards(CardList pTemp)
	{
		
		if (pTemp.size() == 0) return false;
		
		for (Card card : pTemp)
		{
			
			aDiscards.remove(card);
			aRetain.add(card);
			
			if (aDiscards.size() == NUMBER_CARDS_DISCARD)
			{
				
				return true;
				
			}
			
		}
		
		return false;
		
	}
	
	// Keep jokers in hand
	public static boolean keepJokers()
	{
		
		aTemp.clear();
		aTemp = aDiscards.getJokers();
		return switchCards(aTemp);
		
	}
	
	// Keeps all aces in the hand
	public static boolean keepAces()
	{
		
		aTemp.clear();
		for (Card card : aDiscards)
		{
			
			if (card.getRank() == Card.Rank.ACE)
			{
				
				aTemp.add(card);
				
			}
			
		}
		
		return switchCards(aTemp);
		
	}
	
	// Sorts the cards according to the trump
	public static void trumpExchange(Bid[] pBids, int pIndex)
	{
		
		
		Suit mySuit = pBids[pIndex].getSuit();
		//Suit partnerSuit = pBids[pIndex].getSuit();
		//Suit opponent1Suit = pBids[pIndex].getSuit();
		//Suit opponent2Suit = pBids[pIndex].getSuit();
		BySuitComparator compareTrump = new BySuitComparator(mySuit);
		ByRankComparator compareCards = new ByRankComparator();
		BySuitNoTrumpComparator compareNoTrump = new BySuitNoTrumpComparator();
		aDiscards.sort(compareTrump);
		
		while (true)
		{
			
			// keep all jokers
			if (keepJokers()) break;
			// keep all trumps
			if (keepTrumps(mySuit, compareTrump)) break;
			// keep aces
			if (keepAces()) break;
			// keep one card of partner bid
			if (keepPartner(pBids, pIndex, compareCards)) break;
			// keep kings + 1
			if (keepKingsPlusOne(compareNoTrump)) break;
			// keep non singletons and non doubletons
			if (keepNonSingletonsAndNonDoubletons(compareCards)) break;
			
		}
		
		// Removes random high cards at this point
		aDiscards.sort(compareCards);
		aDiscards.reverse();
		Card highestCard;
		while (aDiscards.size() > NUMBER_CARDS_DISCARD)
		{

			highestCard = aDiscards.getFirst();
			aDiscards.remove(highestCard);
			aRetain.add(highestCard);
			
		}
		
		return;
		
	}
	
	// Keeps all trumps
	public static boolean keepTrumps(Suit pSuit, BySuitComparator pCompare)
	{
		
		aTemp.clear();
		aTemp = aDiscards.getTrumpCards(pSuit);
		aTemp.sort(pCompare);
		aTemp.reverse();
		return switchCards(aTemp);
		
	}
	
	// Keeps one card of partner bid
	// CORRECT
	public static boolean keepPartner (Bid[] pBids, int pIndex, ByRankComparator pCompare)
	{
		
		Bid partnerBid = pBids[(pIndex+2)%4];
		if (partnerBid.isPass())
		{
			return false;
		}
		
		aDiscards.sort(pCompare);
		aDiscards.reverse();
		Suit partnerSuit = partnerBid.getSuit();
		aTemp.clear();
		
		for (Card card : aDiscards)
		{
			
			if (card.getSuit().equals(partnerSuit))
			{
				
				aTemp.add(card);
				break;
				
			}
			
		}
		
		return switchCards(aTemp);
		
	}
	
	// Keeps king plus one card
	// Takes in to account if the ace of this suit is already in the hand
	public static boolean keepKingsPlusOne(BySuitNoTrumpComparator pCompare)
	{
		
		aTemp.clear();
		aDiscards.sort(pCompare);
		aDiscards.reverse();
		Card tempKing = null;
		Card tempAce = null;
		
		for (Card card : aDiscards)
		{
			
			if (tempKing != null)
			{
				
				if (card.getSuit().equals(tempKing.getSuit()))
				{
					aTemp.add(tempKing);
					aTemp.add(card);
					tempKing = null;
					continue;
					
				}
				
			}
			
			if (card.getRank().equals(Card.Rank.KING))
			{
				
				tempAce = new Card(Card.Rank.ACE, card.getSuit());
				
				if (aRetain.contains(tempAce))
				{
					
					tempAce = null;
					aTemp.add(card);
					continue;
					
				}
				
				tempAce = null;
				tempKing = card;
				
			}
			
		}
		
		return switchCards(aTemp);
		
	}
	
	// Discards suits that are singleton or doubleton
	public static boolean keepNonSingletonsAndNonDoubletons(ByRankComparator pCompare)
	{

		aTemp.clear();

		ArrayList<CardList> allCards = new ArrayList<CardList>();

		allCards.add(aDiscards.getCardsOfNonTrumpSuit(Suit.DIAMONDS));
		allCards.add(aDiscards.getCardsOfNonTrumpSuit(Suit.SPADES));
		allCards.add(aDiscards.getCardsOfNonTrumpSuit(Suit.CLUBS));
		allCards.add(aDiscards.getCardsOfNonTrumpSuit(Suit.HEARTS));

		for (int i = 0; i < allCards.size(); i++)
		{

			if (allCards.get(i).size() > 2)
			{

				for (Card card : allCards.get(i))
				{

					aTemp.add(card);

				}

			}
			
		}
		
		aTemp.sort(pCompare);
		aTemp.reverse();
		return switchCards(aTemp);

	}
	
}
