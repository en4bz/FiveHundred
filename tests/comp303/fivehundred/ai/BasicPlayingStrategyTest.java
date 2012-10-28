package comp303.fivehundred.ai;

import static comp303.fivehundred.util.AllCards.*;

import java.util.ArrayList;

import org.junit.Test;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;
import comp303.fivehundred.util.Card.Suit;

import static org.junit.Assert.*;

/**
 * 
 * @author Rayyan Khoury
 *
 */
public class BasicPlayingStrategyTest
{

	BasicPlayingStrategy newStrategy = new BasicPlayingStrategy();

	@Test
	/**
	 * Test 0: Leading, trump Spades, has joker
	 * Can return any card
	 */
	public void testTrumpLeadingHasJoker()
	{
		createTest(
				(new Trick(new Bid(7,Suit.SPADES))),
				new Card[] {a4H, a8H, aLJo, a4S},
				new Card[] {a4H, a8H, a4S, aLJo}
				);
	}

	@Test
	/**
	 * Test 1: Leading, no trump, no joker
	 * Can return any card
	 */
	public void testNoTrumpLeadingHasNoJoker()
	{
		createTest(
				(new Trick(new Bid(8,null))),
				new Card[] {a4H, a8H, a7S, a4S},
				new Card[] {a4H, a8H, a7S, a4S}
				);
	}

	@Test
	/**
	 * Test 2: Leading, no trump, has both jokers
	 * Must NOT return any jokers
	 */
	public void testNoTrumpLeadingHasBothJokers()
	{
		
		createTest(
				(new Trick(new Bid(6,null))),
				new Card[] {aLJo, a7S, a4S, aHJo},
				new Card[] {a7S, a4S}
				);
	}

	@Test
	/**
	 * Test 3: Leading, no trump, has high joker
	 * Must NOT return the joker
	 */
	public void testNoTrumpLeadingHasHighJoker()
	{
		createTest(
				(new Trick(new Bid(6,null))),
				new Card[] {a7S, a4S, aHJo},
				new Card[] {a7S, a4S}
				);
	}

	@Test
	/**
	 * Test 4: Leading, no trump, has low joker
	 * Must NOT return the joker
	 */
	public void testNoTrumpLeadingHasLowJoker()
	{
		createTest(
				(new Trick(new Bid(6,null))),
				new Card[] {a7S, a4S, aLJo},
				new Card[] {a7S, a4S}
				);
	}

	@Test
	/**
	 * Test 5: Leading, no trump only both jokers
	 * Can return either joker
	 */
	public void testNoTrumpLeadingHasOnlyJokers()
	{
		createTest(
				(new Trick(new Bid(6,null))),
				new Card[] {aHJo, aLJo},
				new Card[] {aHJo, aLJo}
				);
	}

	@Test
	/**
	 * Test 6: Leading, no trump, high joker
	 * Must return high joker
	 */
	public void testNoTrumpLeadingHasOnlyHighJoker()
	{
		createTest(
				(new Trick(new Bid(6,null))),
				new Card[] {aHJo},
				new Card[] {aHJo}
				);
	}

	@Test
	/**
	 * Test 7: Not leading, following a led high joker, has low joker
	 * Six no trump
	 * Must return lowest card
	 */
	public void testTrumpFollowingLedHighJokerHasLowJoker()
	{
		Trick theTrick = new Trick(new Bid(6,null));
		theTrick.add(aHJo);
		createTest(
				(theTrick),
				new Card[] {a7S, a4S, aLJo},
				new Card[] {a4S}
				);
	}

	@Test
	/**
	 * Test 8: Not leading, following a led low joker, has high joker
	 * Seven spades
	 * Must return high joker
	 */
	public void testTrumpFollowingLedLowJokerHasHighJoker()
	{
		Trick theTrick = new Trick(new Bid(7,Suit.SPADES));
		theTrick.add(aLJo);
		createTest(
				(theTrick),
				new Card[] {a7S, a4S, aHJo},
				new Card[] {aHJo}
				);
	}

	@Test
	/**
	 * Test 9: Not leading, following a led low joker, does not have high joker
	 * Ten no trump
	 * Must return lowest card
	 */
	public void testNoTrumpFollowingLedLowJokerHasNoJokers()
	{
		Trick theTrick = new Trick(new Bid(10,null));
		theTrick.add(aLJo);
		createTest(
				(theTrick),
				new Card[] {a7S, a4S, a7C, a8D},
				new Card[] {a4S}
				);
	}

	@Test
	/**
	 * Test 10: Not leading, following a led seven spades, then high joker, has low joker
	 * Six no trump
	 * Must return lowest card
	 */
	public void testNoTrumpFollowingLedSuitHighJokerPlayedHasLowJokerAndSuit()
	{
		Trick theTrick = new Trick(new Bid(6,null));
		theTrick.add(a7S);
		theTrick.add(aHJo);
		createTest(
				(theTrick),
				new Card[] {a8S, a4S, aLJo, a4H},
				new Card[] {a4S}
				);
	}

	@Test
	/**
	 * Test 11: Not leading, following a led seven spades, then low joker, has high joker
	 * Seven spades
	 * Must return high joker
	 */
	public void testTrumpFollowingTrumpSuitLowJokerPlayedHasHighJokerAndTrump()
	{
		Trick theTrick = new Trick(new Bid(7,Suit.SPADES));
		theTrick.add(a7S);
		theTrick.add(aLJo);
		createTest(
				(theTrick),
				new Card[] {a8S, a4S, aHJo},
				new Card[] {aHJo}
				);
	}

	@Test
	/**
	 * Test 12: Not leading, following a led seven of hearts, then low joker, does not have high joker
	 * Ten Hearts
	 * Must return lowest card that follows suit
	 */
	public void testTrumpFollowingTrumpSuitLowJokerPlayedHasTrumpButNoJoker()
	{
		Trick theTrick = new Trick(new Bid(10,Suit.HEARTS));
		theTrick.add(a7H);
		theTrick.add(aLJo);
		createTest(
				(theTrick),
				new Card[] {a7S, a4S, a7C, a8D, a6H, a9H},
				new Card[] {a6H}
				);
	}

	@Test
	/**
	 * Test 13: Not leading, following a led seven of diamonds, then low joker, does not have high joker, cannot follow suit
	 * Ten No Trump
	 * Must return lowest card
	 */
	public void testNoTrumpFollowingSuitLowJokerPlayedHasNoHighJokerCannotFollowSuit()
	{
		Trick theTrick = new Trick(new Bid(10,null));
		theTrick.add(a7D);
		theTrick.add(aLJo);
		createTest(
				(theTrick),
				new Card[] {a7S, a4S, a7C, a6H, a9H},
				new Card[] {a4S}
				);
	}

	@Test
	/**
	 * Test 14: Following no trump, has suit but cannot beat highest card
	 * Ten No Trump
	 * Must return lowest card of suit
	 */
	public void testNoTrumpFollowingSuitHasSuitButCannotWin()
	{
		Trick theTrick = new Trick(new Bid(10,null));
		theTrick.add(a7D);
		theTrick.add(a8D);
		createTest(
				(theTrick),
				new Card[] {a4D, a6H, a7H},
				new Card[] {a4D}
				);
	}

	@Test
	/**
	 * Test 15: Following no trump, has suit and can beat highest card
	 * Ten No Trump
	 * Must return first highest card of suit
	 */
	public void testNoTrumpFollowingSuitCanBeat()
	{
		Trick theTrick = new Trick(new Bid(10,null));
		theTrick.add(a7D);
		theTrick.add(a8D);
		createTest(
				(theTrick),
				new Card[] {aAD, aQD, aJD, a6H, a7H},
				new Card[] {aJD}
				);
	}

	@Test
	/**
	 * Test 16: Following no trump, does not have suit
	 * Ten No Trump
	 * Must return lowest card in hand
	 */
	public void testNoTrumpCannotFollowSuitCannotWin()
	{
		Trick theTrick = new Trick(new Bid(10,null));
		theTrick.add(a7D);
		theTrick.add(a8D);
		createTest(
				(theTrick),
				new Card[] {a7S, a4S, a7C, a6H, a9H},
				new Card[] {a4S}
				);
	}

	@Test
	/**
	 * Test 17: Playing trump, following trump, can beat trump with trump
	 * Ten Diamonds
	 * Must return lowest card that can beat the trumps
	 */
	public void testTrumpFollowingTrumpCanBeatWithTrump()
	{
		Trick theTrick = new Trick(new Bid(10,Suit.DIAMONDS));
		theTrick.add(a7D);
		theTrick.add(a8D);
		createTest(
				(theTrick),
				new Card[] {a4D, a6H, a7H, aTD, aQD, aLJo},
				new Card[] {aTD}
				);
	}

	@Test
	/**
	 * Test 18: Playing trump, following trump, can beat trump with joker, has both jokers and lower trumps
	 * Ten Spades
	 * Must return lowest joker
	 */
	public void testTrumpFollowingTrumpHasTrumpCanOnlyBeatWithJokers()
	{
		Trick theTrick = new Trick(new Bid(10,Suit.SPADES));
		theTrick.add(a7S);
		theTrick.add(a8S);
		createTest(
				(theTrick),
				new Card[] {aAD, aQD, aJD, a6H, a7H, a4S, aLJo, aHJo},
				new Card[] {aLJo}
				);
	}

	@Test
	/**
	 * Test 19: Playing trump, following non trump, has non trump suit, can beat in non trump suit
	 * Ten Hearts
	 * Must return lowest card in hand that beats non trump suit
	 */
	public void testTrumpFollowingNonTrumpHasNonTrumpCanWinWithoutTrumping()
	{
		Trick theTrick = new Trick(new Bid(10,Suit.HEARTS));
		theTrick.add(a7D);
		theTrick.add(a8D);
		createTest(
				(theTrick),
				new Card[] {a7S, a4S, a7C, a6H, a9H, a6D, aTD, aJD},
				new Card[] {aTD}
				);
	}

	@Test
	/**
	 * Test 20: Playing trump, following non trump, has non trump suit, cannot beat in non trump suit
	 * Ten Diamonds
	 * Must return lowest card in hand in non trump suit
	 */
	public void testTrumpFollowingNonTrumpHasNonTrumpCannotWin()
	{
		Trick theTrick = new Trick(new Bid(10,Suit.DIAMONDS));
		theTrick.add(a7H);
		theTrick.add(a8H);
		createTest(
				(theTrick),
				new Card[] {a7S, a4S, a7C, a6H},
				new Card[] {a6H}
				);
	}

	@Test
	/**
	 * Test 21: Playing trump, following non trump, doesn't have non trump suit, can beat with low trump, has jokers
	 * Ten Clubs
	 * Must return lowest trump and not the joker
	 */
	public void testTrumpFollowingNonTrumpDoesNotHaveNonTrumpSuitCanBeatWithTrumpHasJokers()
	{
		Trick theTrick = new Trick(new Bid(10,Suit.CLUBS));
		theTrick.add(a7D);
		theTrick.add(a8D);
		createTest(
				(theTrick),
				new Card[] {a7S, a4S, a7C, a6H, a9H, aLJo},
				new Card[] {a7C}
				);
	}

	@Test
	/**
	 * Test 22: Playing trump, following non trump, doesn't have non trump suit, can beat with low trump, does not have jokers
	 * Ten Spades
	 * Must return lowest trump and not the joker
	 */
	public void testTrumpFollowingNonTrumpDoesNotHaveNonTrumpSuitCanBeatWithTrumpHasNoJokers()
	{
		Trick theTrick = new Trick(new Bid(10,Suit.SPADES));
		theTrick.add(a7D);
		theTrick.add(a8D);
		createTest(
				(theTrick),
				new Card[] {a7S, a4S, a7C, a6H, a9H, a8C},
				new Card[] {a7C}
				);
	}

	@Test
	/**
	 * Test 23: Playing trump, following non trump, doesn't have non trump suit, does not have trump, can beat with joker
	 * Ten Hearts
	 * Must return lowest card in hand
	 */
	public void testTrumpFollowingNonTrumpDoesNotHaveNonTrumpSuitHasJokers()
	{
		Trick theTrick = new Trick(new Bid(10,Suit.HEARTS));
		theTrick.add(a7D);
		theTrick.add(a8D);
		createTest(
				(theTrick),
				new Card[] {a7S, a4S, a7C, aLJo, aHJo},
				new Card[] {aLJo}
				);
	}

	@Test
	/**
	 * Test 24: Playing trump, following non trump, already trumped, no non trump cards, can beat with higher trump, has jokers
	 * Ten Clubs
	 * Must return lowest card in hand
	 */
	public void testTrumpFollowingNonTrumpAlreadyTrumpedHasNoNonTrumpCanBeatWithTrumpHasJokers()
	{
		Trick theTrick = new Trick(new Bid(10,Suit.CLUBS));
		theTrick.add(a7D);
		theTrick.add(a4C);
		createTest(
				(theTrick),
				new Card[] {a7S, a4S, a7C, a6H, a9H, a5C, aHJo},
				new Card[] {a5C}
				);
	}

	@Test
	/**
	 * Test 25: Playing trump, following non trump, already trumped, no non trump cards, no trump, has jokers
	 * Ten Hearts
	 * Must return lowest card in hand
	 */
	public void testTrumpFollowingNonTrumpAlreadyTrumpedHasNoNonTrumpAndNoTrumpButHasJokers()
	{
		Trick theTrick = new Trick(new Bid(10,Suit.DIAMONDS));
		theTrick.add(a7C);
		theTrick.add(a8D);
		createTest(
				(theTrick),
				new Card[] {a7S, a4S, a6H, a9H, aHJo},
				new Card[] {aHJo}
				);
	}

	@Test
	/**
	 * Test 26: Playing trump, following non trump, already trumped, no non trump cards, cannot beat with trumps nor jokers
	 * Ten Hearts
	 * Must return lowest card in hand
	 */
	public void testTrumpFollowingNonTrumpAlreadyTrumpedHasNoTrumpsNonTrumpsOrJokers()
	{
		Trick theTrick = new Trick(new Bid(10,Suit.HEARTS));
		theTrick.add(a7D);
		theTrick.add(a8H);
		createTest(
				(theTrick),
				new Card[] {a7S, a4S, a7C, a6H},
				new Card[] {a4S}
				);
	}

	/**
	 * Creates the test and runs it 32 times for standard deviation purposes
	 * @param pTrick The trick that will be passed
	 * @param pOriginal A card array representing the hand
	 * @param pCorrect The correct possible outcome of cards
	 */
	public void createTest (Trick pTrick, Card[] pOriginal, Card[] pCorrect)
	{

		// Creates the original hand
		Hand theHand = new Hand();
		theHand.addCardArray(pOriginal);
		
		CardList correctCards = new CardList();
		correctCards.addCardArray(pCorrect);
		
		// runs the test thirty two times
		for (int j = 0; j < 32; j++)
		{
			assertTrue(correctCards.contains(newStrategy.play(pTrick, theHand)));

		}

	}

}
