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
	
	ArrayList<Trick> aTricks = new ArrayList<Trick>();
	ArrayList<Hand> aHand = new ArrayList<Hand>();
	ArrayList<CardList> aCorrectCardList = new ArrayList<CardList>();
	
	BasicPlayingStrategy newStrategy = new BasicPlayingStrategy();
	
	@Test
	public void testBasicPlayingStrategy()
	{

		// Test 0: Leading, trump Spades, has joker
		// Can return any card
		createTest(
				(new Trick(new Bid(7,Suit.SPADES))),
				new Card[] {a4H, a8H, aLJo, a4S},
				new Card[] {a4H, a8H, a4S, aLJo}
				);
		
		// Test 1: Leading, no trump, no joker
		// Can return any card
		createTest(
				(new Trick(new Bid(8,null))),
				new Card[] {a4H, a8H, a7S, a4S},
				new Card[] {a4H, a8H, a7S, a4S}
				);
		
		// Test 2: Leading, no trump, has both jokers
		// Must NOT return any jokers
		createTest(
				(new Trick(new Bid(6,null))),
				new Card[] {aLJo, a7S, a4S, aHJo},
				new Card[] {a7S, a4S}
				);
		
		// Test 3: Leading, no trump, has high joker
		// Must NOT return the joker
		createTest(
				(new Trick(new Bid(6,null))),
				new Card[] {a7S, a4S, aHJo},
				new Card[] {a7S, a4S}
				);
		
		// Test 4: Leading, no trump, has low joker
		// Must NOT return the joker
		createTest(
				(new Trick(new Bid(6,null))),
				new Card[] {a7S, a4S, aLJo},
				new Card[] {a7S, a4S}
				);

		// Test 5: Leading, no trump only both jokers
		// Can return either joker
		createTest(
				(new Trick(new Bid(6,null))),
				new Card[] {aHJo, aLJo},
				new Card[] {aHJo, aLJo}
				);
		
		// Test 6: Leading, no trump, high joker
		// Must return high joker
		createTest(
				(new Trick(new Bid(6,null))),
				new Card[] {aHJo},
				new Card[] {aHJo}
				);
		
		// Test 7: Not leading, following a led high joker, has low joker
		// Six no trump
		// Must return lowest card
		Trick theTrick = new Trick(new Bid(6,null));
		theTrick.add(aHJo);
		createTest(
				(theTrick),
				new Card[] {a7S, a4S, aLJo},
				new Card[] {a4S}
				);
		
		// Test 8: Not leading, following a led low joker, has high joker
		// Seven spades
		// Must return high joker
		theTrick = new Trick(new Bid(7,Suit.SPADES));
		theTrick.add(aLJo);
		createTest(
				(theTrick),
				new Card[] {a7S, a4S, aHJo},
				new Card[] {aHJo}
				);
		
		// Test 9: Not leading, following a led low joker, does not have high joker
		// Ten no trump
		// Must return lowest card
		theTrick = new Trick(new Bid(10,null));
		theTrick.add(aLJo);
		createTest(
				(theTrick),
				new Card[] {a7S, a4S, a7C, a8D},
				new Card[] {a4S}
				);
		
		// Test 10: Not leading, following a led seven spades, then high joker, has low joker
		// Six no trump
		// Must return lowest card
		theTrick = new Trick(new Bid(6,null));
		theTrick.add(a7S);
		theTrick.add(aHJo);
		createTest(
				(theTrick),
				new Card[] {a8S, a4S, aLJo, a4H},
				new Card[] {a4S}
				);
		
		// Test 11: Not leading, following a led seven spades, then low joker, has high joker
		// Seven spades
		// Must return high joker
		theTrick = new Trick(new Bid(7,Suit.SPADES));
		theTrick.add(a7S);
		theTrick.add(aLJo);
		createTest(
				(theTrick),
				new Card[] {a8S, a4S, aHJo},
				new Card[] {aHJo}
				);
		
		
		// Test 12: Not leading, following a led seven of hearts, then low joker, does not have high joker
		// Ten Hearts
		// Must return lowest card that follows suit
		theTrick = new Trick(new Bid(10,Suit.HEARTS));
		theTrick.add(a7H);
		theTrick.add(aLJo);
		createTest(
				(theTrick),
				new Card[] {a7S, a4S, a7C, a8D, a6H, a9H},
				new Card[] {a6H}
				);
		
		// Test 13: Not leading, following a led seven of diamonds, then low joker, does not have high joker, cannot follow suit
		// Ten No Trump
		// Must return lowest card
		theTrick = new Trick(new Bid(10,null));
		theTrick.add(a7D);
		theTrick.add(aLJo);
		createTest(
				(theTrick),
				new Card[] {a7S, a4S, a7C, a6H, a9H},
				new Card[] {a4S}
				);
		
		// Test 14: Following no trump, has suit but cannot beat highest card
		// Ten No Trump
		// Must return lowest card of suit
		theTrick = new Trick(new Bid(10,null));
		theTrick.add(a7D);
		theTrick.add(a8D);
		createTest(
				(theTrick),
				new Card[] {a4D, a6H, a7H},
				new Card[] {a4D}
				);
		
		// Test 15: Following no trump, has suit and can beat highest card
		// Ten No Trump
		// Must return first highest card of suit
		theTrick = new Trick(new Bid(10,null));
		theTrick.add(a7D);
		theTrick.add(a8D);
		createTest(
				(theTrick),
				new Card[] {aAD, aQD, aJD, a6H, a7H},
				new Card[] {aJD}
				);
		
		// Test 16: Following no trump, does not have suit
		// Ten No Trump
		// Must return lowest card in hand
		theTrick = new Trick(new Bid(10,null));
		theTrick.add(a7D);
		theTrick.add(a8D);
		createTest(
				(theTrick),
				new Card[] {a7S, a4S, a7C, a6H, a9H},
				new Card[] {a4S}
				);

		// Test 17: Playing trump, following trump, can beat trump with trump
		// Ten Diamonds
		// Must return lowest card that can beat the trumps
		theTrick = new Trick(new Bid(10,Suit.DIAMONDS));
		theTrick.add(a7D);
		theTrick.add(a8D);
		createTest(
				(theTrick),
				new Card[] {a4D, a6H, a7H, aTD, aQD, aLJo},
				new Card[] {aTD}
				);

		// Test 18: Playing trump, following trump, can beat trump with joker, has both jokers and lower trumps
		// Ten Spades
		// Must return lowest joker
		theTrick = new Trick(new Bid(10,Suit.SPADES));
		theTrick.add(a7S);
		theTrick.add(a8S);
		createTest(
				(theTrick),
				new Card[] {aAD, aQD, aJD, a6H, a7H, a4S, aLJo, aHJo},
				new Card[] {aLJo}
				);

		// Test 19: Playing trump, following non trump, has non trump suit, can beat in non trump suit
		// Ten Hearts
		// Must return lowest card in hand that beats non trump suit
		theTrick = new Trick(new Bid(10,Suit.HEARTS));
		theTrick.add(a7D);
		theTrick.add(a8D);
		createTest(
				(theTrick),
				new Card[] {a7S, a4S, a7C, a6H, a9H, a6D, aTD, aJD},
				new Card[] {aTD}
				);
		
		// Test 20: Playing trump, following non trump, has non trump suit, cannot beat in non trump suit
		// Ten Diamonds
		// Must return lowest card in hand in non trump suit
		theTrick = new Trick(new Bid(10,Suit.DIAMONDS));
		theTrick.add(a7H);
		theTrick.add(a8H);
		createTest(
				(theTrick),
				new Card[] {a7S, a4S, a7C, a6H},
				new Card[] {a6H}
				);
		
		// Test 21: Playing trump, following non trump, doesn't have non trump suit, can beat with low trump, has jokers
		// Ten Clubs
		// Must return lowest trump and not the joker
		theTrick = new Trick(new Bid(10,Suit.CLUBS));
		theTrick.add(a7D);
		theTrick.add(a8D);
		createTest(
				(theTrick),
				new Card[] {a7S, a4S, a7C, a6H, a9H, aLJo},
				new Card[] {a7C}
				);
		
		// Test 22: Playing trump, following non trump, doesn't have non trump suit, can beat with low trump, does not have jokers
		// Ten Spades
		// Must return lowest trump and not the joker
		theTrick = new Trick(new Bid(10,Suit.SPADES));
		theTrick.add(a7D);
		theTrick.add(a8D);
		createTest(
				(theTrick),
				new Card[] {a7S, a4S, a7C, a6H, a9H, a8C},
				new Card[] {a7C}
				);
		
		// Test 23: Playing trump, following non trump, doesn't have non trump suit, does not have trump, can beat with joker
		// Ten Hearts
		// Must return lowest card in hand
		theTrick = new Trick(new Bid(10,Suit.HEARTS));
		theTrick.add(a7D);
		theTrick.add(a8D);
		createTest(
				(theTrick),
				new Card[] {a7S, a4S, a7C, aLJo, aHJo},
				new Card[] {aLJo}
				);
		
		// Test 24: Playing trump, following non trump, already trumped, no non trump cards, can beat with higher trump, has jokers
		// Ten Clubs
		// Must return lowest card in hand
		theTrick = new Trick(new Bid(10,Suit.CLUBS));
		theTrick.add(a7D);
		theTrick.add(a4C);
		createTest(
				(theTrick),
				new Card[] {a7S, a4S, a7C, a6H, a9H, a5C, aHJo},
				new Card[] {a5C}
				);
		
		// Test 25: Playing trump, following non trump, already trumped, no non trump cards, has jokers
		// Ten Hearts
		// Must return lowest card in hand
		theTrick = new Trick(new Bid(10,Suit.DIAMONDS));
		theTrick.add(a7C);
		theTrick.add(a8D);
		createTest(
				(theTrick),
				new Card[] {a7S, a4S, a6H, a9H, aHJo},
				new Card[] {aHJo}
				);
		
		// Test 26: Playing trump, following non trump, already trumped, no non trump cards, cannot beat with trumps nor jokers
		// Ten Hearts
		// Must return lowest card in hand
		theTrick = new Trick(new Bid(10,Suit.HEARTS));
		theTrick.add(a7D);
		theTrick.add(a8H);
		createTest(
				(theTrick),
				new Card[] {a7S, a4S, a7C, a6H},
				new Card[] {a4S}
				);



		// Runs the tests
		/*
		runTest(0);
		runTest(1);
		runTest(2);
		runTest(3);
		runTest(4);
		runTest(5);
		runTest(6);
		runTest(7);
		runTest(8);
		runTest(9);
		runTest(10);
		runTest(11);
		runTest(12);
		runTest(13);
		runTest(14);
		runTest(15);
		runTest(16);
		runTest(17);
		runTest(18);
		runTest(19);
		runTest(20);
		*/
		
		runTest(21);
		//runTest(22);
		//runTest(23);
		//runTest(24);
		//runTest(25);
		
		/*
		runTest(26);
		*/
		

	}
	
	public void runTest(int pInt)
	{
		
		System.out.println(aCorrectCardList.get(pInt));
		System.out.println(aTricks.get(pInt));
		System.out.println(aHand.get(pInt));
		
		// runs the test thirty two times
		for (int j = 0; j < 32; j++)
		{
			assertTrue(aCorrectCardList.get(pInt).contains(newStrategy.play(aTricks.get(pInt), aHand.get(pInt))));
			
		}
		
	}
	
	public void createTest (Trick pTrick, Card[] pOriginal, Card[] pCorrect)
	{
		
		CardList correctCards = new CardList();
		correctCards.addCardArray(pCorrect);
		
		// Creates the original hand
		Hand theHand = new Hand();
		theHand.addCardArray(pOriginal);
		
		// Adds everything
		aTricks.add(pTrick);
		aHand.add(theHand);
		aCorrectCardList.add(correctCards);
		
	}

}
