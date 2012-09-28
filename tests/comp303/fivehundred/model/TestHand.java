package comp303.fivehundred.model;

import java.util.HashSet;
import java.util.Set;

import comp303.fivehundred.util.AllCards;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * JUnit test for the Hand class.
 * @author Eleyine Zarour
 *
 */
public class TestHand
{
	private Card[] aJoker = {AllCards.aLJo};
	private Card[] aJokers = {AllCards.aHJo, AllCards.aLJo};
	private Card[] aNonJokers = {AllCards.a5C, AllCards.aJD, AllCards.aAS};
	private Card[] aMisc = {AllCards.aHJo, AllCards.a5C, AllCards.aJD, AllCards.aAS};
	private Card[] aAllSuits = {AllCards.a5C, AllCards.aJD, AllCards.aAS, AllCards.aJH};
	private Card[] aTrumpTest = {AllCards.aJC, AllCards.aJH,AllCards.aJS,AllCards.aJD};
	
	@Test
	public void testCanLead()
	{
		// Expected values in a trump round.
		boolean lNoTrump = false;
		Card[] lJoker = {AllCards.aLJo};
		Card[] lJokers = {AllCards.aHJo, AllCards.aLJo};
		Card[] lNonJokers = {AllCards.a5C, AllCards.aJD, AllCards.aAS};
		Card[] lMisc = {AllCards.aHJo, AllCards.a5C, AllCards.aJD, AllCards.aAS};
		Card[] lAllSuits = {AllCards.a5C, AllCards.aJD, AllCards.aAS, AllCards.aJH};
		Card[] lTrumpTest = {AllCards.aJC, AllCards.aJH,AllCards.aJS,AllCards.aJD};
		
		// Tests for trump round.
		assertTrue(toSet(lJoker).equals(toSet(toHand(aJoker).canLead(lNoTrump))));
		assertTrue(toSet(lJokers).equals(toSet(toHand(aJokers).canLead(lNoTrump))));
		assertTrue(toSet(lNonJokers).equals(toSet(toHand(aNonJokers).canLead(lNoTrump))));
		assertTrue(toSet(lMisc).equals(toSet(toHand(aMisc).canLead(lNoTrump))));
		assertTrue(toSet(lAllSuits).equals(toSet(toHand(aAllSuits).canLead(lNoTrump))));
		assertTrue(toSet(lTrumpTest).equals(toSet(toHand(aTrumpTest).canLead(lNoTrump))));

		// Expected values in a no-trump round.
		lNoTrump = true;
		Card[] lJokerNT = {AllCards.aLJo};
		Card[] lJokersNT = {AllCards.aHJo, AllCards.aLJo};
		Card[] lNonJokersNT = {AllCards.a5C, AllCards.aJD, AllCards.aAS};
		Card[] lMiscNT = {AllCards.a5C, AllCards.aJD, AllCards.aAS};
		Card[] lAllSuitsNT = {AllCards.a5C, AllCards.aJD, AllCards.aAS, AllCards.aJH};
		Card[] lTrumpTestNT = {AllCards.aJC, AllCards.aJH,AllCards.aJS,AllCards.aJD};
		
		// Tests for no-trump round.
		assertTrue(toSet(lJokerNT).equals(toSet(toHand(aJoker).canLead(lNoTrump))));
		assertTrue(toSet(lJokersNT).equals(toSet(toHand(aJokers).canLead(lNoTrump))));
		assertTrue(toSet(lNonJokersNT).equals(toSet(toHand(aNonJokers).canLead(lNoTrump))));
		assertTrue(toSet(lMiscNT).equals(toSet(toHand(aMisc).canLead(lNoTrump))));
		assertTrue(toSet(lAllSuitsNT).equals(toSet(toHand(aAllSuits).canLead(lNoTrump))));
		assertTrue(toSet(lTrumpTestNT).equals(toSet(toHand(aTrumpTest).canLead(lNoTrump))));
		
	}
	
	@Test
	public void testGetJokers()
	{
		// Expected values
		Card[] lJoker = {AllCards.aLJo};
		Card[] lJokers = {AllCards.aHJo, AllCards.aLJo};
		Card[] lNonJokers = {};
		Card[] lMisc = {AllCards.aHJo};
		Card[] lAllSuits = {};
		Card[] lTrumpTest = {};
		
		// Tests
		assertTrue(toSet(lJoker).equals(toSet(toHand(aJoker).getJokers())));
		assertTrue(toSet(lJokers).equals(toSet(toHand(aJokers).getJokers())));
		assertTrue(toSet(lNonJokers).equals(toSet(toHand(aNonJokers).getJokers())));
		assertTrue(toSet(lMisc).equals(toSet(toHand(aMisc).getJokers())));
		assertTrue(toSet(lAllSuits).equals(toSet(toHand(aAllSuits).getJokers())));
		assertTrue(toSet(lTrumpTest).equals(toSet(toHand(aTrumpTest).getJokers())));
	}
	
	@Test
	public void testGetNonJokers()
	{
		// Expected values
		Card[] lJoker = {};
		Card[] lJokers = {};
		Card[] lNonJokers = {AllCards.a5C, AllCards.aJD, AllCards.aAS};
		Card[] lMisc = {AllCards.a5C, AllCards.aJD, AllCards.aAS};
		Card[] lAllSuits = {AllCards.a5C, AllCards.aJD, AllCards.aAS, AllCards.aJH};
		Card[] lTrumpTest = {AllCards.aJC, AllCards.aJH,AllCards.aJS,AllCards.aJD};
		
		// Tests 
		assertTrue(toSet(lJoker).equals(toSet(toHand(aJoker).getNonJokers())));
		assertTrue(toSet(lJokers).equals(toSet(toHand(aJokers).getNonJokers())));
		assertTrue(toSet(lNonJokers).equals(toSet(toHand(aNonJokers).getNonJokers())));
		assertTrue(toSet(lMisc).equals(toSet(toHand(aMisc).getNonJokers())));
		assertTrue(toSet(lAllSuits).equals(toSet(toHand(aAllSuits).getNonJokers())));
		assertTrue(toSet(lTrumpTest).equals(toSet(toHand(aTrumpTest).getNonJokers())));
	}
	
	@Test
	public void testGetTrumpCards()
	{
		// Expected values in a Hearts round
		Suit lTrump = Suit.HEARTS;
		Card[] lJokerH = {AllCards.aLJo};
		Card[] lJokersH = {AllCards.aHJo, AllCards.aLJo};
		Card[] lNonJokersH = {AllCards.aJD};
		Card[] lMiscH = {AllCards.aHJo,AllCards.aJD};
		Card[] lAllSuitsH = {AllCards.aJD, AllCards.aJH};
		Card[] lTrumpTestH = {AllCards.aJH ,AllCards.aJD};
		
		// Tests for Hearts round.
		assertTrue(toSet(lJokerH).equals(toSet(toHand(aJoker).getTrumpCards(lTrump))));
		assertTrue(toSet(lJokersH).equals(toSet(toHand(aJokers).getTrumpCards(lTrump))));
		assertTrue(toSet(lNonJokersH).equals(toSet(toHand(aNonJokers).getTrumpCards(lTrump))));
		assertTrue(toSet(lMiscH).equals(toSet(toHand(aMisc).getTrumpCards(lTrump))));
		assertTrue(toSet(lAllSuitsH).equals(toSet(toHand(aAllSuits).getTrumpCards(lTrump))));
		assertTrue(toSet(lTrumpTestH).equals(toSet(toHand(aTrumpTest).getTrumpCards(lTrump))));
		
		// Expected values in a Clubs round
		lTrump = Suit.CLUBS;
		Card[] lJokerC = {AllCards.aLJo};
		Card[] lJokersC = {AllCards.aHJo, AllCards.aLJo};
		Card[] lNonJokersC = {AllCards.a5C};
		Card[] lMiscC = {AllCards.aHJo, AllCards.a5C};
		Card[] lAllSuitsC = {AllCards.a5C};
		Card[] lTrumpTestC = {AllCards.aJC, AllCards.aJS};
		
		// Tests for a Clubs round
		assertTrue(toSet(lJokerC).equals(toSet(toHand(aJoker).getTrumpCards(lTrump))));
		assertTrue(toSet(lJokersC).equals(toSet(toHand(aJokers).getTrumpCards(lTrump))));
		assertTrue(toSet(lNonJokersC).equals(toSet(toHand(aNonJokers).getTrumpCards(lTrump))));
		assertTrue(toSet(lMiscC).equals(toSet(toHand(aMisc).getTrumpCards(lTrump))));
		assertTrue(toSet(lAllSuitsC).equals(toSet(toHand(aAllSuits).getTrumpCards(lTrump))));
		assertTrue(toSet(lTrumpTestC).equals(toSet(toHand(aTrumpTest).getTrumpCards(lTrump))));
		
	}
	
	@Test
	public void testGetNonTrumpCards()
	{
		// Expected values in a Hearts round.
		Suit lTrump = Suit.HEARTS;
		Card[] lJoker = {};
		Card[] lJokers = {};
		Card[] lNonJokers = {AllCards.a5C, AllCards.aAS};
		Card[] lMisc = {AllCards.a5C, AllCards.aAS};
		Card[] lAllSuits = {AllCards.a5C, AllCards.aAS};
		Card[] lTrumpTest = {AllCards.aJC,AllCards.aJS};
		
		// Tests for Hearts round.
		assertTrue(toSet(lJoker).equals(toSet(toHand(aJoker).getNonTrumpCards(lTrump))));
		assertTrue(toSet(lJokers).equals(toSet(toHand(aJokers).getNonTrumpCards(lTrump))));
		assertTrue(toSet(lNonJokers).equals(toSet(toHand(aNonJokers).getNonTrumpCards(lTrump))));
		assertTrue(toSet(lMisc).equals(toSet(toHand(aMisc).getNonTrumpCards(lTrump))));
		assertTrue(toSet(lAllSuits).equals(toSet(toHand(aAllSuits).getNonTrumpCards(lTrump))));
		assertTrue(toSet(lTrumpTest).equals(toSet(toHand(aTrumpTest).getNonTrumpCards(lTrump))));
	}
	
	@Test
	//TODO: should not throw CloneNotSupportedException
	public void testSelectLowest() throws CloneNotSupportedException
	{
		// Expected values in a Clubs round.
		Suit lTrump = Suit.CLUBS;
		Card lJoker = AllCards.aLJo;
		Card lJokers = AllCards.aLJo;
		Card lNonJokers = AllCards.aJD;
		Card lMisc = AllCards.aJD;
		Card lAllSuits = AllCards.aJD;
		Card lTrumpTest = AllCards.aJD;
		
		// Tests
		assertTrue(lJoker.equals(toHand(aJoker).selectLowest(lTrump)));
		assertTrue(lJokers.equals(toHand(aJokers).selectLowest(lTrump)));
		assertTrue(lNonJokers.equals(toHand(aNonJokers).selectLowest(lTrump)));
		assertTrue(lMisc.equals(toHand(aMisc).selectLowest(lTrump)));
		assertTrue(lAllSuits.equals(toHand(aAllSuits).selectLowest(lTrump)));
		assertTrue(lTrumpTest.equals(toHand(aTrumpTest).selectLowest(lTrump)));
	}
	
	@Test
	public void testPlayableCards()
	{
		// Expected values in a no-trump round with a led Diamonds.
		Suit lLed = Suit.DIAMONDS;
		Suit lTrump = null;
		Card[] lJokerD = {AllCards.aLJo};
		Card[] lJokersD = {AllCards.aHJo, AllCards.aLJo};
		Card[] lNonJokersD = {AllCards.aJD};
		Card[] lMiscD = {AllCards.aHJo, AllCards.aJD};
		Card[] lAllSuitsD = {AllCards.aJD};
		Card[] lTrumpTestD = {AllCards.aJD};
		
		// Tests for a no-trump round with led Diamonds.
		assertTrue(toSet(lJokerD).equals(toSet(toHand(aJoker).playableCards(lLed, lTrump))));
		assertTrue(toSet(lJokersD).equals(toSet(toHand(aJokers).playableCards(lLed, lTrump))));
		assertTrue(toSet(lNonJokersD).equals(toSet(toHand(aNonJokers).playableCards(lLed, lTrump))));
		assertTrue(toSet(lMiscD).equals(toSet(toHand(aMisc).playableCards(lLed, lTrump))));
		assertTrue(toSet(lAllSuitsD).equals(toSet(toHand(aAllSuits).playableCards(lLed, lTrump))));
		assertTrue(toSet(lTrumpTestD).equals(toSet(toHand(aTrumpTest).playableCards(lLed, lTrump))));
	
		// Expected values in a Clubs trump round with led Clubs.
		lTrump = Suit.CLUBS;
		lLed = Suit.CLUBS;
		Card[] lJokerCC = {AllCards.aLJo};
		Card[] lJokersCC = {AllCards.aHJo, AllCards.aLJo};
		Card[] lNonJokersCC = {AllCards.a5C};
		Card[] lMiscCC = {AllCards.aHJo, AllCards.a5C};
		Card[] lAllSuitsCC = {AllCards.a5C};
		Card[] lTrumpTestCC = {AllCards.aJC, AllCards.aJS};
		
		// Tests for a Clubs trump round with led Clubs.
		assertTrue(toSet(lJokerCC).equals(toSet(toHand(aJoker).playableCards(lLed, lTrump))));
		assertTrue(toSet(lJokersCC).equals(toSet(toHand(aJokers).playableCards(lLed, lTrump))));
		assertTrue(toSet(lNonJokersCC).equals(toSet(toHand(aNonJokers).playableCards(lLed, lTrump))));
		assertTrue(toSet(lMiscCC).equals(toSet(toHand(aMisc).playableCards(lLed, lTrump))));
		assertTrue(toSet(lAllSuitsCC).equals(toSet(toHand(aAllSuits).playableCards(lLed, lTrump))));
		assertTrue(toSet(lTrumpTestCC).equals(toSet(toHand(aTrumpTest).playableCards(lLed, lTrump))));
	
		// Expected values in a Clubs trump round with led Spades.
		lTrump = Suit.HEARTS;
		lLed = Suit.SPADES;
		Card[] lJokerHS = {AllCards.aLJo};
		Card[] lJokersHS = {AllCards.aHJo, AllCards.aLJo};
		Card[] lNonJokersHS = {AllCards.aJD, AllCards.aAS};
		Card[] lMiscHS = {AllCards.aHJo, AllCards.aJD, AllCards.aAS};
		Card[] lAllSuitsHS = {AllCards.aJD, AllCards.aAS, AllCards.aJH};
		Card[] lTrumpTestHS = {AllCards.aJH,AllCards.aJS,AllCards.aJD};
		
		// Tests for a Clubs trump round with led Spades.
		assertTrue(toSet(lJokerHS).equals(toSet(toHand(aJoker).playableCards(lLed, lTrump))));
		assertTrue(toSet(lJokersHS).equals(toSet(toHand(aJokers).playableCards(lLed, lTrump))));
		assertTrue(toSet(lNonJokersHS).equals(toSet(toHand(aNonJokers).playableCards(lLed, lTrump))));
		assertTrue(toSet(lMiscHS).equals(toSet(toHand(aMisc).playableCards(lLed, lTrump))));
		assertTrue(toSet(lAllSuitsHS).equals(toSet(toHand(aAllSuits).playableCards(lLed, lTrump))));
		assertTrue(toSet(lTrumpTestHS).equals(toSet(toHand(aTrumpTest).playableCards(lLed, lTrump))));
	}
	
	@Test
	public void testNumberOfCards()
	{
		// Consider the following:
		// Card[] aJoker = {AllCards.aLJo};
		// Card[] aJokers = {AllCards.aHJo, AllCards.aLJo};
		// Card[] aNonJokers = {AllCards.a5C, AllCards.aJD, AllCards.aAS};
		// Card[] aMisc = {AllCards.aHJo, AllCards.a5C, AllCards.aJD, AllCards.aAS};
		// Card[] aAllSuits = {AllCards.a5C, AllCards.aJD, AllCards.aAS, AllCards.aJH};
		// Card[] aTrumpTest = {AllCards.aJC, AllCards.aJH,AllCards.aJS,AllCards.aJD};
		
		// number of expected cards of suit DIAMONDS in a HEARTS round
		Suit lSuit = Suit.DIAMONDS;
		Suit lTrump = Suit.HEARTS;
		int lJokerDH = 0;
		int lJokersDH = 0;
		int lNonJokersDH = 0;
		int lMiscDH = 0;
		int lAllSuitsDH = 0;
		int lTrumpTestDH = 0;
		
		assertEquals(lJokerDH, toHand(aJoker).numberOfCards(lSuit, lTrump));
		assertEquals(lJokersDH, toHand(aJoker).numberOfCards(lSuit, lTrump));
		assertEquals(lNonJokersDH, toHand(aJoker).numberOfCards(lSuit, lTrump));
		assertEquals(lMiscDH, toHand(aJoker).numberOfCards(lSuit, lTrump));
		assertEquals(lAllSuitsDH, toHand(aJoker).numberOfCards(lSuit, lTrump));
		assertEquals(lTrumpTestDH, toHand(aJoker).numberOfCards(lSuit, lTrump));		
		
		// number of expected cards of suit CLUBS in a SPADES round
		lSuit = Suit.CLUBS;
		lTrump = Suit.SPADES;
		int lJokerCS = 0;
		int lJokersCS = 0;
		int lNonJokersCS = 1;
		int lMiscCS = 1;
		int lAllSuitsCS = 1;
		int lTrumpTestCS = 0;
		
		assertEquals(lJokerCS, toHand(aJoker).numberOfCards(lSuit, lTrump));
		assertEquals(lJokersCS, toHand(aJoker).numberOfCards(lSuit, lTrump));
		assertEquals(lNonJokersCS, toHand(aJoker).numberOfCards(lSuit, lTrump));
		assertEquals(lMiscCS, toHand(aJoker).numberOfCards(lSuit, lTrump));
		assertEquals(lAllSuitsCS, toHand(aJoker).numberOfCards(lSuit, lTrump));
		assertEquals(lTrumpTestCS, toHand(aJoker).numberOfCards(lSuit, lTrump));		
		
		// number of expected cards of suit CLUBS in a CLUBS round
		lSuit = Suit.CLUBS;
		lTrump = Suit.CLUBS;
		int lJokerCC = 0;
		int lJokersCC = 0;
		int lNonJokersCC = 1;
		int lMiscCC = 1;
		int lAllSuitsCC = 1;
		int lTrumpTestCC = 2;
		
		assertEquals(lJokerCC, toHand(aJoker).numberOfCards(lSuit, lTrump));
		assertEquals(lJokersCC, toHand(aJoker).numberOfCards(lSuit, lTrump));
		assertEquals(lNonJokersCC, toHand(aJoker).numberOfCards(lSuit, lTrump));
		assertEquals(lMiscCC, toHand(aJoker).numberOfCards(lSuit, lTrump));
		assertEquals(lAllSuitsCC, toHand(aJoker).numberOfCards(lSuit, lTrump));
		assertEquals(lTrumpTestCC, toHand(aJoker).numberOfCards(lSuit, lTrump));			
	}
	
	private Hand toHand(Card[] pCardArray)
	{
		Hand lReturn = new Hand();
		for(Card c: pCardArray)
		{
			lReturn.add(c);
		}
		return lReturn;
	}
	
	// Helper methods
	private Set<Card> toSet(CardList pHand)
	{
		Set<Card> lReturn = new HashSet<Card>();
		for(Card c: pHand)
		{
			lReturn.add(c);
		}
		return lReturn;
	}

	private Set<Card> toSet(Card[] pHand)
	{
		Set<Card> lReturn = new HashSet<Card>();
		for(Card c: pHand)
		{
			lReturn.add(c);
		}
		return lReturn;
	}
}
