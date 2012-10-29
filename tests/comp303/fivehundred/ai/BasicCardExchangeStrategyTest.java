package comp303.fivehundred.ai;

import static comp303.fivehundred.util.AllCards.*;

import org.junit.Test;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;

import static org.junit.Assert.*;


/**
 * @author Rayyan Khoury
 * Test class for BasicCardExchangeStrategy
 */
public class BasicCardExchangeStrategyTest
{
	BasicCardExchangeStrategy aStrategy = new BasicCardExchangeStrategy();

	@Test
	/**
	 * Trump Spades no Jokers
	 */
	public void test0TrumpSpadesNoJokers()
	{
		createTest(
				new Bid(), 
				new Bid(6, Suit.CLUBS), 
				new Bid(7, Suit.SPADES), 
				new Bid(),
				new Card[] {a4H, a8H, aTH, a4C, a7C, aJC, aQC, a7D, aAD, a4S, a6S, a8S, aTS, aJS, aQS, aKS},
				new Card[] {a4C, a4H, a7C, a7D, a8H, aTH},
				2
				);
	}
	
	@Test
	/**
	 * Trump Clubs with High Joker
	 */
	public void test1TrumpClubsHighJoker()
	{
		createTest(
				new Bid(6, null), 
				new Bid(8, Suit.CLUBS), 
				new Bid(), 
				new Bid(9,Suit.CLUBS),
				new Card[] {aHJo, aJC, aJS, aAC, aQC, a8C, a7C, a4C, aTD, a7D, aAS, a7S, aAH, aKH, a7H, a4H},
				new Card[] {a4H, a7S, a7D, a7H, aTD, aKH},
				3
				);

	}
	@Test
	/**
	 * No trump with Both Jokers
	 */
	public void test2NoTrumpBothJokers()
	{
		createTest(
				new Bid(7, null), 
				new Bid(), 
				new Bid(), 
				new Bid(),
				new Card[] {aHJo, aLJo, aAD, aKD, aTD, a5D, aAS, aTS, a8S, a6S, aKH, a8H, aJC, a9C, a4C, a4S},
				new Card[] {a4S, a4C, a5D, a6S, a8S, a8H},
				0
				);

	}
	
	@Test
	/**
	 * Trump with converse jacks and jokers
	 */
	public void test3TrumpMustKeepConverseJack()
	{
		createTest(
				new Bid(7, null), 
				new Bid(), 
				new Bid(), 
				new Bid(9, Suit.SPADES),
				new Card[] {aHJo, aJS, aJC, aAS, aKS, aTS, a9S, a7S, a4S, aAH, aKH, aAC, a5C, aAD, aKD, aTD},
				new Card[] {a5C, aTD, aKD, aKH, aAC, aAD},
				3
				);
	}

	public void createTest(Bid pFirst, Bid pSecond, Bid pThird, Bid pFourth, Card[] pAllCards, Card[] pCorrectCards, int pIndex)
	{

		// New bids
		Bid[] bidArray = new Bid[4];
		bidArray[0] = pFirst;
		bidArray[1] = pSecond;
		bidArray[2] = pThird;
		bidArray[3] = pFourth;

		// 16 cards
		Hand cList = new Hand();
		cList.addCardArray(pAllCards);

		// 6 correct cards
		CardList correctCardList = new CardList();
		correctCardList.addCardArray(pCorrectCards);

		assertEquals(correctCardList, aStrategy.selectCardsToDiscard(bidArray, pIndex, cList));
	}
}
