package comp303.fivehundred.util;

import static comp303.fivehundred.util.AllCards.*;
import static comp303.fivehundred.util.*;
import static org.junit.Assert.*;

import org.junit.Test;
import comp303.fivehundred.util.Card.BySuitNoTrumpComparator;
import comp303.fivehundred.util.Card.Suit;

/*
 * @author Ian Forbes
 */

public class TestBySuitNoTrumpComparator
{
	private BySuitNoTrumpComparator tester = new BySuitNoTrumpComparator(Suit.SPADES);
	
	@Test
	public void testCompare()
	{
		assertEquals(0, tester.compare(a4D, a4D));
		assertEquals(1, tester.compare(a4S, a8D));
		assertEquals(-1, tester.compare(a9D, a5S));
		assertEquals(0, tester.compare(a4D, a4D));
		assertEquals(1, tester.compare(aHJo, a4D));
		assertEquals(-1, tester.compare(a9C, aLJo));
		assertEquals(1, tester.compare(aHJo, aLJo));
	}
}
