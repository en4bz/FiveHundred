package comp303.fivehundred.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import comp303.fivehundred.util.AllCards;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;
import static org.junit.Assert.*;
import org.junit.Test;

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
	public void testSelectLowest()
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
	
	public void testPlayableCards()
	{
		
	}
	public void testNumberOfCards()
	{
		
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
