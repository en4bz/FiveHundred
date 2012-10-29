package comp303.fivehundred.ai;

import static comp303.fivehundred.util.AllCards.a4C;
import static comp303.fivehundred.util.AllCards.a4H;
import static comp303.fivehundred.util.AllCards.a4S;
import static comp303.fivehundred.util.AllCards.a5C;
import static comp303.fivehundred.util.AllCards.a5D;
import static comp303.fivehundred.util.AllCards.a5H;
import static comp303.fivehundred.util.AllCards.a5S;
import static comp303.fivehundred.util.AllCards.a6C;
import static comp303.fivehundred.util.AllCards.a6H;
import static comp303.fivehundred.util.AllCards.a6S;
import static comp303.fivehundred.util.AllCards.a7S;
import static comp303.fivehundred.util.AllCards.a8C;
import static comp303.fivehundred.util.AllCards.a8D;
import static comp303.fivehundred.util.AllCards.a8S;
import static comp303.fivehundred.util.AllCards.a9D;
import static comp303.fivehundred.util.AllCards.a9S;
import static comp303.fivehundred.util.AllCards.aAC;
import static comp303.fivehundred.util.AllCards.aAH;
import static comp303.fivehundred.util.AllCards.aAS;
import static comp303.fivehundred.util.AllCards.aJC;
import static comp303.fivehundred.util.AllCards.aJS;
import static comp303.fivehundred.util.AllCards.aKD;
import static comp303.fivehundred.util.AllCards.aKS;
import static comp303.fivehundred.util.AllCards.aQC;
import static comp303.fivehundred.util.AllCards.aQH;
import static comp303.fivehundred.util.AllCards.aQS;
import static comp303.fivehundred.util.AllCards.aTC;
import static comp303.fivehundred.util.AllCards.aTH;
import static comp303.fivehundred.util.AllCards.aTS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Rank;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.Deck;

public class BasicBiddingStrategyTest
{

	private BasicBiddingStrategy aStrategy = new BasicBiddingStrategy();

	@Test
	/**
	 * Test 0: All cards are lower than jack, no possiblity to bid
	 * Must return a pass
	 */
	public void test0AllCardsLowerThanJack()
	{

		Bid[] bidArray = new Bid[1];
		bidArray[0] = new Bid();

		createTest(
				(bidArray),
				new Card[] {a8C, aTC, a8D, a9D, a9S, a8S, a5S, a6H, aTH, a4H},
				new Bid()
				);
	}

	@Test
	/**
	 * Test 1: only has cards in spades
	 * Must return a spades bid
	 */
	public void test1OnlyCardsInSpades()
	{

		Bid[] bidArray = new Bid[2];
		bidArray[0] = new Bid();
		bidArray[1] = new Bid();

		createTest(
				(bidArray),
				new Card[] {aKS, aQS, aJS, aTS, a9S, a8S, a7S, a6S, a5S, a4S},
				new Bid(10, Suit.SPADES)
				);
	}

	@Test
	/**
	 * Test 2: only has cards in spades, opponents both bid spades
	 * Must return a significantly lower bid of spades
	 */
	public void test2OnlyCardsInSpadesOpponentsBothBidSpades()
	{

		Bid[] bidArray = new Bid[3];
		bidArray[0] = new Bid(6, Suit.SPADES);
		bidArray[1] = new Bid();
		bidArray[2] = new Bid(7, Suit.SPADES);

		createTest(
				(bidArray),
				new Card[] {aKS, aQS, aJS, aTS, a9S, a8S, a7S, a6S, a5S, a4S},
				new Bid()
				);
	}

	@Test
	/**
	 * Test 3: only has cards in spades, one opponent bid spades
	 * Must return a lower than normal bid of spades
	 */
	public void test3OnlyCardsInSpadesOneOpponentBidSpades()
	{

		Bid[] bidArray = new Bid[1];
		bidArray[0] = new Bid(6, Suit.SPADES);

		createTest(
				(bidArray),
				new Card[] {aKS, aQS, aJS, aTS, a9S, a8S, a7S, a6S, a5S, a4S},
				new Bid(8, Suit.SPADES)
				);
	}

	@Test
	/**
	 * Test 4: only has cards in spades, partner bids spades
	 * Must return a higher than normal bid of spades
	 */
	public void test4OnlyCardsInSpadesPartnerBidSpades()
	{

		Bid[] bidArray = new Bid[3];
		bidArray[0] = new Bid(6, Suit.HEARTS);
		bidArray[1] = new Bid(7, Suit.SPADES);
		bidArray[2] = new Bid(8, Suit.HEARTS);

		createTest(
				(bidArray),
				new Card[] {aKS, aQS, aJS, aTS, a9S, a8S, a7S, a6S, a5S, a4S},
				new Bid(10, Suit.SPADES)
				);
	}

	@Test
	/**
	 * Test 5: half cards in spades, half in clubs, high in spades, low in clubs
	 * Must return a bid in spades
	 */
	public void test5HalfSpadesHalfClubsHighSpades()
	{

		Bid[] bidArray = new Bid[2];
		bidArray[0] = new Bid(6, Suit.HEARTS);
		bidArray[1] = new Bid(7, Suit.CLUBS);

		createTest(
				(bidArray),
				new Card[] {aKS, aQS, aJS, aTS, aAS, a8C, aJC, a6C, a5C, a4C},
				new Bid(9, Suit.SPADES)
				);
	}

	@Test
	/**
	 * Test 6: High cards in every suit
	 * Must return a no trump bid
	 */
	public void test6HighCardsEverySuit()
	{

		Bid[] bidArray = new Bid[3];
		bidArray[0] = new Bid();
		bidArray[1] = new Bid();
		bidArray[2] = new Bid();

		createTest(
				(bidArray),
				new Card[] {aAS, aKS, a4S, aAC, aQC, a4C, aKD, a5D, aAH, aQH},
				new Bid(10, null)
				);
	}

	@Test
	/**
	 * Test 7: High cards in every suit except one
	 * Must not return a no trump bid
	 */
	public void test7HighCardsEverySuitExceptOne()
	{

		Bid[] bidArray = new Bid[3];
		bidArray[0] = new Bid();
		bidArray[1] = new Bid();
		bidArray[2] = new Bid();

		createTest(
				(bidArray),
				new Card[] {aAS, aKS, a4S, aAC, aQC, a4C, aKD, a5D, a4H, a5H},
				new Bid()
				);
	}


	public void createTest (Bid[] pPreviousBids, Card[] pOriginal, Bid pCorrect)
	{

		// Creates the original hand
		Hand theHand = new Hand();
		theHand.addCardArray(pOriginal);

		assertEquals(pCorrect, (aStrategy.selectBid(pPreviousBids, theHand)));

	}

	@Test
	public void testObviousBid()
	{
		Bid[] lBids = {new Bid(),new Bid(),new Bid(),new Bid()};
		for(Suit lSuit : Suit.values()){
			Hand lHand = new Hand();
			for(Rank lRank : Rank.values())  
			{
				lHand.add( new Card( lRank, lSuit));
			}
			Bid lTest = aStrategy.selectBid(lBids, lHand);
			assertTrue(lTest.getSuit().equals(lSuit));//Player has all the cards of lSuit
		}
	}

	@Test
	public void testPassingBid()
	{
		Bid[] lBids = {new Bid(10,null),new Bid(10,null),new Bid(10,null),new Bid(10,null)};
		for(Suit lSuit : Suit.values()){
			Hand lHand = new Hand();
			for(Rank lRank : Rank.values())  
			{
				lHand.add( new Card( lRank, lSuit));
			}
			Bid lTest = aStrategy.selectBid(lBids, lHand);
			assertTrue(lTest.equals(new Bid()));//Should be a pass
		}	
	}

	@Test
	public void testBid1()
	{
		for(int i = 0; i < 1000; i++){
			Bid[] passes = new Bid[]{new Bid(),new Bid(),new Bid(),new Bid()};
			Hand lHand = generateHand();
			Bid theBid = aStrategy.selectBid(passes, lHand);
			for(Suit s : Suit.values())
			{
				//If there are no cards of s do not bid s
				if(!theBid.isPass() && !theBid.isNoTrump() && lHand.getTrumpCards(s).size() == 0){
					assertTrue(!theBid.equals(s));
				}
			}
		}
	}

	public static Hand generateHand()
	{
		Deck lDeck = new Deck();
		lDeck.shuffle();
		Hand lHand = new Hand();
		for(int i = 0; i < 10; i++){
			lHand.add(lDeck.draw());
		}
		return lHand;
	}

	@Test 
	public void testGreaterOrPass()
	{
		for(int i = 6; i <= 10; i++)
		{
			for(Suit s : Suit.values())
			{
				Bid selected = aStrategy.selectBid(new Bid[]{new Bid(i,s)}, generateHand());
				//Make sure bid is larger or pass
				assertTrue(selected.compareTo(new Bid(i,s)) > 0 || selected.isPass());
			}
		}
	}

	@Test 
	public void testFirstBidder()
	{
		for(int i = 0; i < 100; i++)
		{
			Bid selected = aStrategy.selectBid(new Bid[0], generateHand());
			//assertTrue(!selected.equals(new Bid()));
		}
	}
}
