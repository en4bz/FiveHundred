package comp303.fivehundred.ai;

import static comp303.fivehundred.util.AllCards.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;
import comp303.fivehundred.util.Card.Suit;

/**
 * @author Rayyan Khoury
 * Test class for AdvancedCardExchangeStrategy
 */
public class AdvancedCardExchangeStrategyTest
{

	AdvancedCardExchangeStrategy aStrategy = new AdvancedCardExchangeStrategy();

	@Test
	/**
	 * Trump Spades no Jokers
	 */
	public void test0TrumpSpadesNoJokers()
	{
		createTest(
				new Bid(), 
				new Bid(6, Suit.CLUBS), 
				new Bid(), 
				new Bid(7, Suit.DIAMONDS),
				new Card[] {aHJo, aJD, aJH, aAD, aKD, aTD, a8D, aAS, aAH, aJS, aTS, a7C, a6H, a5C, a9C, a8C},
				new Card[] {aJS, aTS, a7C, a6H, a5C, a8C},
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
