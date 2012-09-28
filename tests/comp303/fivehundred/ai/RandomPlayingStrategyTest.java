package comp303.fivehundred.ai;

import static org.junit.Assert.*;

/**
 * @author Gabrielle Germain
 * Test class for RandomPlayingStrategy
 */

import org.junit.Test;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.AllCards;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Suit;

public class RandomPlayingStrategyTest
{

	@Test
	public void testPlay()
	{
		RandomPlayingStrategy testStrategy = new RandomPlayingStrategy();
		
		//We create a new hand (4 cards)
		Hand newHand = new Hand();
		newHand.add(AllCards.a9S);
		newHand.add(AllCards.aJH);
		newHand.add(AllCards.aLJo); //low joker
		newHand.add(AllCards.a8C);

		//Test 1: Leading(empty trick), no trump, hand contains a joker. The method must not return the joker
		Bid someBid1 = new Bid (6, null); //null = no trump
		Trick emptyTrick = new Trick (someBid1);
		
		Card testCard1 = testStrategy.play(emptyTrick, newHand);
		assertNotSame(testCard1, AllCards.aLJo); // the card returned must not be the joker
		
		
		//Test 2: Not leading, trump = Hearts, contains a card with the same suit as the tricks: returns a random card from the same suit (Hearts in this case)
		Bid someBid2 = new Bid (7, Suit.HEARTS);
		Trick initiallyEmptyTrick = new Trick (someBid2); // empty trick
		initiallyEmptyTrick.add(AllCards.a5H); // initiallyEmptyTrick now contains one trick that was played already
		
		Card testCard2 = testStrategy.play(initiallyEmptyTrick, newHand);
		assertEquals(testCard2, AllCards.aJH); //must return the only "heart" that the hand contains, since the suit played is heart
		
		
		//Test 3: Not Leading, trump = Spades, hand does not contain card of the same suit as the tricks played, but contains a spade. Must return trump (spades)
		Bid someBid3 = new Bid (6, Suit.SPADES);
		Trick notEmptyTrick = new Trick (someBid3); // empty trick
		notEmptyTrick.add(AllCards.a4D); 
		notEmptyTrick.add(AllCards.a6D); // initiallyEmptyTrick now contains two tricks that were played already
		
		Card testCard3 = testStrategy.play(notEmptyTrick, newHand);
		
		assertEquals(testCard3, AllCards.a9S);//Return "Spades"
		
		
		//Test 4: Leading, trump : Can return any card from the hand, joker included (Test if a card is returned)
		Bid someBid4 = new Bid (6, Suit.SPADES);
		Trick emptyTrick2 = new Trick (someBid4); // empty trick = leading
		
		Card testCard4 = testStrategy.play(emptyTrick2, newHand);
		assertNotSame(testCard4, null); //must return a card
		
	}

}
