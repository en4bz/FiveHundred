package comp303.fivehundred.model;

import java.util.HashSet;
import java.util.Set;

import static comp303.fivehundred.util.AllCards.*;
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
	private Card[] aJoker = {aLJo};
	private Card[] aJokers = {aHJo, aLJo};
	private Card[] aNonJokers = {a5C, aJD, aAS};
	private Card[] aMisc = {aHJo, a5C, aJD, aAS};
	private Card[] aAllSuits = {a5C, aJD, aAS, aJH};
	private Card[] aTrumpTest = {aJC, aJH,aJS,aJD};
	
	@Test
	public void testCanLead()
	{
		// Expected values in a trump round.
		boolean lNoTrump = false;
		Card[] lJoker = {aLJo};
		Card[] lJokers = {aHJo, aLJo};
		Card[] lNonJokers = {a5C, aJD, aAS};
		Card[] lMisc = {aHJo, a5C, aJD, aAS};
		Card[] lAllSuits = {a5C, aJD, aAS, aJH};
		Card[] lTrumpTest = {aJC, aJH,aJS,aJD};
		
		// Tests for trump round.
		assertTrue(toSet(lJoker).equals(toSet(toHand(aJoker).canLead(lNoTrump))));
		assertTrue(toSet(lJokers).equals(toSet(toHand(aJokers).canLead(lNoTrump))));
		assertTrue(toSet(lNonJokers).equals(toSet(toHand(aNonJokers).canLead(lNoTrump))));
		assertTrue(toSet(lMisc).equals(toSet(toHand(aMisc).canLead(lNoTrump))));
		assertTrue(toSet(lAllSuits).equals(toSet(toHand(aAllSuits).canLead(lNoTrump))));
		assertTrue(toSet(lTrumpTest).equals(toSet(toHand(aTrumpTest).canLead(lNoTrump))));

		// Expected values in a no-trump round.
		lNoTrump = true;
		Card[] lJokerNT = {aLJo};
		Card[] lJokersNT = {aHJo, aLJo};
		Card[] lNonJokersNT = {a5C, aJD, aAS};
		Card[] lMiscNT = {a5C, aJD, aAS};
		Card[] lAllSuitsNT = {a5C, aJD, aAS, aJH};
		Card[] lTrumpTestNT = {aJC, aJH,aJS,aJD};
		
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
		Card[] lJoker = {aLJo};
		Card[] lJokers = {aHJo, aLJo};
		Card[] lNonJokers = {};
		Card[] lMisc = {aHJo};
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
		Card[] lNonJokers = {a5C, aJD, aAS};
		Card[] lMisc = {a5C, aJD, aAS};
		Card[] lAllSuits = {a5C, aJD, aAS, aJH};
		Card[] lTrumpTest = {aJC, aJH,aJS,aJD};
		
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
		// AssertionError test
		try
		{
			Hand testHand = toHand(aMisc);
			testHand.getTrumpCards(null);
			fail();
		}
		catch(AssertionError e)
		{
			
		}
		
		
		// Expected values in a Hearts round
		Suit lTrump = Suit.HEARTS;
		Card[] lJokerH = {aLJo};
		Card[] lJokersH = {aHJo, aLJo};
		Card[] lNonJokersH = {aJD};
		Card[] lMiscH = {aHJo,aJD};
		Card[] lAllSuitsH = {aJD, aJH};
		Card[] lTrumpTestH = {aJH ,aJD};
		
		// Tests for Hearts round.
		assertTrue(toSet(lJokerH).equals(toSet(toHand(aJoker).getTrumpCards(lTrump))));
		assertTrue(toSet(lJokersH).equals(toSet(toHand(aJokers).getTrumpCards(lTrump))));
		assertTrue(toSet(lNonJokersH).equals(toSet(toHand(aNonJokers).getTrumpCards(lTrump))));
		assertTrue(toSet(lMiscH).equals(toSet(toHand(aMisc).getTrumpCards(lTrump))));
		assertTrue(toSet(lAllSuitsH).equals(toSet(toHand(aAllSuits).getTrumpCards(lTrump))));
		assertTrue(toSet(lTrumpTestH).equals(toSet(toHand(aTrumpTest).getTrumpCards(lTrump))));
		
		// Expected values in a Clubs round
		lTrump = Suit.CLUBS;
		Card[] lJokerC = {aLJo};
		Card[] lJokersC = {aHJo, aLJo};
		Card[] lNonJokersC = {a5C};
		Card[] lMiscC = {aHJo, a5C};
		Card[] lAllSuitsC = {a5C};
		Card[] lTrumpTestC = {aJC, aJS};
		
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
		// AssertionError test
		try
		{
			Hand testHand = toHand(aMisc);
			testHand.getNonTrumpCards(null);
			fail();
		}
		catch(AssertionError e)
		{
			
		}
		
		// Expected values in a Hearts round.
		Suit lTrump = Suit.HEARTS;
		Card[] lJoker = {};
		Card[] lJokers = {};
		Card[] lNonJokers = {a5C, aAS};
		Card[] lMisc = {a5C, aAS};
		Card[] lAllSuits = {a5C, aAS};
		Card[] lTrumpTest = {aJC,aJS};
		
		// Tests for Hearts round.
		assertTrue(toSet(lJoker).equals(toSet(toHand(aJoker).getNonTrumpCards(lTrump))));
		assertTrue(toSet(lJokers).equals(toSet(toHand(aJokers).getNonTrumpCards(lTrump))));
		assertTrue(toSet(lNonJokers).equals(toSet(toHand(aNonJokers).getNonTrumpCards(lTrump))));
		assertTrue(toSet(lMisc).equals(toSet(toHand(aMisc).getNonTrumpCards(lTrump))));
		assertTrue(toSet(lAllSuits).equals(toSet(toHand(aAllSuits).getNonTrumpCards(lTrump))));
		assertTrue(toSet(lTrumpTest).equals(toSet(toHand(aTrumpTest).getNonTrumpCards(lTrump))));
	}
	
	@Test
	public void testSelectLowest()
	{
		// Expected values in a Clubs round.
		Suit lTrump = Suit.CLUBS;
		Card lJoker = aLJo;
		Card lJokers = aLJo;
		Card lNonJokers = aJD;
		Card lMisc = aJD;
		Card lAllSuits = aJD;
		Card lTrumpTest = aJD;
		
		// Tests
		assertTrue(lJoker.equals(toHand(aJoker).selectLowest(lTrump)));
		assertTrue(lJokers.equals(toHand(aJokers).selectLowest(lTrump)));
		assertTrue(lNonJokers.equals(toHand(aNonJokers).selectLowest(lTrump)));
		assertTrue(lMisc.equals(toHand(aMisc).selectLowest(lTrump)));
		assertTrue(lAllSuits.equals(toHand(aAllSuits).selectLowest(lTrump)));
		assertTrue(lTrumpTest.equals(toHand(aTrumpTest).selectLowest(lTrump)));
		
		// Expected values in a no-trump round.
		lTrump = null;
		Card lJokerNT = aLJo;
		Card lJokersNT = aLJo;
		Card lNonJokersNT = a5C;
		Card lMiscNT = a5C;
		Card lAllSuitsNT = a5C;
		Card lTrumpTestNT = aJS;
		
		// Tests
		assertTrue(lJokerNT.equals(toHand(aJoker).selectLowest(lTrump)));
		assertTrue(lJokersNT.equals(toHand(aJokers).selectLowest(lTrump)));
		assertTrue(lNonJokersNT.equals(toHand(aNonJokers).selectLowest(lTrump)));
		assertTrue(lMiscNT.equals(toHand(aMisc).selectLowest(lTrump)));
		assertTrue(lAllSuitsNT.equals(toHand(aAllSuits).selectLowest(lTrump)));
		assertTrue(lTrumpTestNT.equals(toHand(aTrumpTest).selectLowest(lTrump)));
	
	}
	
	@Test
	public void testPlayableCards()
	{
		// Expected values in a no-trump round with a led Diamonds.
		Suit lLed = Suit.DIAMONDS;
		Suit lTrump = null;
		Card[] lJokerD = {aLJo};
		Card[] lJokersD = {aHJo, aLJo};
		Card[] lNonJokersD = {aJD};
		Card[] lMiscD = {aHJo, aJD};
		Card[] lAllSuitsD = {aJD};
		Card[] lTrumpTestD = {aJD};
		
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
		Card[] lJokerCC = {aLJo};
		Card[] lJokersCC = {aHJo, aLJo};
		Card[] lNonJokersCC = {a5C};
		Card[] lMiscCC = {aHJo, a5C};
		Card[] lAllSuitsCC = {a5C};
		Card[] lTrumpTestCC = {aJC, aJS};
		
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
		Card[] lJokerHS = {aLJo};
		Card[] lJokersHS = {aHJo, aLJo};
		Card[] lNonJokersHS = {aJD, aAS};
		Card[] lMiscHS = {aHJo, aJD, aAS};
		Card[] lAllSuitsHS = {aJD, aAS, aJH};
		Card[] lTrumpTestHS = {aJH, aJS, aJD};
		
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
		// Card[] aJoker = {aLJo};
		// Card[] aJokers = {aHJo, aLJo};
		// Card[] aNonJokers = {a5C, aJD, aAS};
		// Card[] aMisc = {aHJo, a5C, aJD, aAS};
		// Card[] aAllSuits = {a5C, aJD, aAS, aJH};
		// Card[] aTrumpTest = {aJC, aJH,aJS,aJD};
		
		// AssertionError test
		try
		{
			Hand testHand = toHand(aMisc);
			testHand.numberOfCards(null, null);
			fail();
		}
		catch(AssertionError e)
		{
			
		}
		
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
		assertEquals(lJokersDH, toHand(aJokers).numberOfCards(lSuit, lTrump));
		assertEquals(lNonJokersDH, toHand(aNonJokers).numberOfCards(lSuit, lTrump));
		assertEquals(lMiscDH, toHand(aMisc).numberOfCards(lSuit, lTrump));
		assertEquals(lAllSuitsDH, toHand(aAllSuits).numberOfCards(lSuit, lTrump));
		assertEquals(lTrumpTestDH, toHand(aTrumpTest).numberOfCards(lSuit, lTrump));		
		
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
		assertEquals(lJokersCS, toHand(aJokers).numberOfCards(lSuit, lTrump));
		assertEquals(lNonJokersCS, toHand(aNonJokers).numberOfCards(lSuit, lTrump));
		assertEquals(lMiscCS, toHand(aMisc).numberOfCards(lSuit, lTrump));
		assertEquals(lAllSuitsCS, toHand(aAllSuits).numberOfCards(lSuit, lTrump));
		assertEquals(lTrumpTestCS, toHand(aTrumpTest).numberOfCards(lSuit, lTrump));		
		
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
		assertEquals(lJokersCC, toHand(aJokers).numberOfCards(lSuit, lTrump));
		assertEquals(lNonJokersCC, toHand(aNonJokers).numberOfCards(lSuit, lTrump));
		assertEquals(lMiscCC, toHand(aMisc).numberOfCards(lSuit, lTrump));
		assertEquals(lAllSuitsCC, toHand(aAllSuits).numberOfCards(lSuit, lTrump));
		assertEquals(lTrumpTestCC, toHand(aTrumpTest).numberOfCards(lSuit, lTrump));		
	}

	// Helper methods
	private Hand toHand(Card[] pCardArray)
	{
		Hand lReturn = new Hand();
		for(Card c: pCardArray)
		{
			lReturn.add(c);
		}
		return lReturn;
	}
	
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
