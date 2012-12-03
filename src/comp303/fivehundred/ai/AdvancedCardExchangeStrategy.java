package comp303.fivehundred.ai;

import java.util.ArrayList;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.ByRankComparator;
import comp303.fivehundred.util.Card.BySuitNoTrumpComparator;
import comp303.fivehundred.util.Card.GenericBySuitComparator;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;
import comp303.fivehundred.util.Card.BySuitComparator;


/**
 * @author Rayyan Khoury
 * Discards the worst cards for play
 */

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
	
	// All suits in this game
	private static Suit[] aSuits = new Suit[] {Suit.SPADES, Suit.CLUBS, Suit.DIAMONDS, Suit.HEARTS};
	
	@Override
	public CardList selectCardsToDiscard(Bid[] pBids, int pIndex, Hand pHand)
	{
		
		Hand handClone = pHand.clone();
		
		//aDiscards = new CardList();
		aRetain = new CardList();
		aTemp = new CardList();
		aDiscards = pHand.clone();
		
		// Trump game
		if (Bid.max(pBids).getSuit() != null)
		{
			
			trumpExchange(pBids, pIndex);
			
		}
		
		// No trump game just discards the lowest cards
		else
		{
			
			aDiscards = new Hand(super.selectCardsToDiscard(pBids, pIndex, handClone));
			
		}
		
		return aDiscards;
		
	}
	
	/**
	 * removes cards from discards
	 * adds them to retain
	 * checks if the size of discards has reached six and if so returns this
	 * @param pTemp
	 * @return
	 */
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
	
	/**
	 * Keeps the jokers in hand
	 * @return
	 */
	public static boolean keepJokers()
	{
		
		aTemp.removeAll();
		aTemp = aDiscards.getJokers();
		return switchCards(aTemp);
		
	}

	/**
	 * Keeps all runs of ace to queen in the hand
	 * Always keeps the ace
	 * @param pCompare
	 * @return
	 */
	public static boolean keepRuns(BySuitNoTrumpComparator pCompare)
	{
		
		aTemp.removeAll();
		aDiscards = (Hand) aDiscards.sort(pCompare);
		aDiscards = (Hand) aDiscards.reverse();
		
		Card tempCard = null;
		Suit tempSuit = null;
		
		for (Card card : aDiscards)
		{
			
			// keeps the ace
			if (card.getRank() == Card.Rank.ACE && tempCard == null)
			{
				tempCard = card;
				tempSuit = card.getSuit();
				aTemp.add(card);
				continue;
				
			}
			
			// if the temp card has been set
			if (tempCard != null)
			{

				// entering with an ace
				if (tempCard.getRank().equals(Card.Rank.ACE) && card.getSuit().equals(tempSuit))
				{

					if (card.getRank() == Card.Rank.KING)
					{

						tempCard = card;
						aTemp.add(card);
						continue;

					}

					// not a king
					tempCard = null;
					continue;

				}

				// entering with a king
				if (tempCard.getRank().equals(Card.Rank.KING) && card.getSuit().equals(tempSuit))
				{

					if (card.getRank() == Card.Rank.QUEEN)
					{

						tempCard = null;
						aTemp.add(card);
						continue;

					}

				}
				
			}
			
		}
		return switchCards(aTemp);
		
	}
	
	/**
	 * Sorts the cards according to the trump
	 * @param pBids
	 * @param pIndex
	 */
	public static void trumpExchange(Bid[] pBids, int pIndex)
	{
		
		
		Suit mySuit = pBids[pIndex].getSuit();
		//Suit partnerSuit = pBids[pIndex].getSuit();
		//Suit opponent1Suit = pBids[pIndex].getSuit();
		//Suit opponent2Suit = pBids[pIndex].getSuit();
		BySuitComparator compareTrump = new BySuitComparator(mySuit);
		ByRankComparator compareCards = new ByRankComparator();
		BySuitNoTrumpComparator compareNoTrump = new BySuitNoTrumpComparator();
		aDiscards = (Hand)aDiscards.sort(compareTrump);
		aDiscards = (Hand)aDiscards.reverse();
		
		while (true)
		{
			
			// keep all jokers
			if (keepJokers()) break;
			// keep all trumps
			if (keepTrumps(mySuit, compareTrump)) break;
			// keep runs of high cards always including aces
			if (keepRuns(compareNoTrump)) break;
			// keep kings + 1
			if (keepKingsPlusOne(compareNoTrump)) break;
			// keep one card of partner bid
			if (keepPartner(pBids, pIndex, compareCards)) break;
			// keep non singletons and non doubletons
			if (keepNonSingletonsAndNonDoubletons(pBids, pIndex, mySuit)) break;
			
		}
		
		// Removes random high cards at this point
		aDiscards = (Hand)aDiscards.sort(compareCards);
		aDiscards = (Hand)aDiscards.reverse();
		Card highestCard;
		
		if (aDiscards.size() > NUMBER_CARDS_DISCARD)
		{
			
			while (aDiscards.size() != NUMBER_CARDS_DISCARD)
			{

				highestCard = aDiscards.getFirst();
				aDiscards.remove(highestCard);
				aRetain.add(highestCard);
				
			}
			
		}
		
		return;
		
	}
	
	/**
	 * Keeps all the trumps
	 * @param pSuit
	 * @param pCompare
	 * @return
	 */
	public static boolean keepTrumps(Suit pSuit, BySuitComparator pCompare)
	{
		
		aTemp.removeAll();
		aTemp = aDiscards.getTrumpCards(pSuit);
		aTemp = aTemp.sort(pCompare);
		aTemp = aTemp.reverse();
		return switchCards(aTemp);
		
	}
	
	/**
	 * Keeps one card of the partner's bid
	 * @param pBids
	 * @param pIndex
	 * @param pCompare
	 * @return
	 */
	public static boolean keepPartner (Bid[] pBids, int pIndex, ByRankComparator pCompare)
	{
		
		Bid partnerBid = pBids[(pIndex+2)%4];
		if (partnerBid.isPass())
		{
			return false;
		}
		
		aDiscards = (Hand) aDiscards.sort(pCompare);
		aDiscards = (Hand) aDiscards.reverse();
		Suit partnerSuit = partnerBid.getSuit();
		aTemp.removeAll();
		
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
	
	/**
	 * Keeps king plus one card
	 * Takes in to account if the ace of this suit is already in the hand
	 * @param pCompare
	 * @return
	 */
	public static boolean keepKingsPlusOne(BySuitNoTrumpComparator pCompare)
	{
		
		aTemp.removeAll();
		aDiscards = (Hand) aDiscards.sort(pCompare);
		aDiscards = (Hand) aDiscards.reverse();
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
	
	/**
	 * Discards suits that are singleton or doubleton
	 * Takes in to account partner suit and opponent suit in terms of suit precendence
	 * @param pBids
	 * @param pIndex
	 * @param pMySuit
	 * @return
	 */
	public static boolean keepNonSingletonsAndNonDoubletons(Bid[] pBids, int pIndex, Suit pMySuit)
	{

		aTemp.removeAll();
		Suit mySuit = pMySuit;
		Suit partSuit = null;
		Suit opp1Suit = null;
		Suit opp2Suit = null;
		
		if (!pBids[((pIndex+2)%4)].isPass())
		{
			
			partSuit = pBids[((pIndex+2)%4)].getSuit();
			
		}
		
		if (!pBids[((pIndex+1)%4)].isPass())
		{
			
			opp1Suit = pBids[((pIndex+1)%4)].getSuit();
			
		}
		
		if (!pBids[((pIndex+3)%4)].isPass())
		{
			
			opp2Suit = pBids[((pIndex+3)%4)].getSuit();
			
		}
		
		
		Suit[] gameSuits = new Suit[] {mySuit, partSuit, opp1Suit, opp2Suit};
		ArrayList<Suit> leftOverSuits = new ArrayList<Suit>();
		
		for (int i = 0; i < aSuits.length; i++)
		{
			
			leftOverSuits.add(aSuits[i]);
			
		}
		
		for (Suit suit : aSuits)
		{
			
			for (Suit ourSuit : gameSuits)
			{
				
				if (suit.equals(ourSuit))
				{
					
					leftOverSuits.remove(suit);
					break;
					
				}
				
			}
			
		}
		
		for (int i  = 0; i < gameSuits.length ; i++)
		{
			
			if (gameSuits[i] == null)
			{
				
				gameSuits[i] = leftOverSuits.get(0);
				//System.out.println("change suit:" +ourSuit);
				leftOverSuits.remove(gameSuits[i]);
				
			}
			
		}
		
		for (Suit ourSuit : gameSuits)
		{
			//System.out.println(ourSuit);
		}
		
		GenericBySuitComparator compare = new GenericBySuitComparator(gameSuits, true);

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
		aTemp = aTemp.sort(compare);
		aTemp = aTemp.reverse();
		return switchCards(aTemp);

	}
	
}
