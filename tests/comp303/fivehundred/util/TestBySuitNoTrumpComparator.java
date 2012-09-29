package comp303.fivehundred.util;

import static comp303.fivehundred.util.AllCards.a4D;
import static comp303.fivehundred.util.AllCards.a4H;
import static comp303.fivehundred.util.AllCards.a4S;
import static comp303.fivehundred.util.AllCards.a5S;
import static comp303.fivehundred.util.AllCards.a8D;
import static comp303.fivehundred.util.AllCards.a9C;
import static comp303.fivehundred.util.AllCards.a9D;
import static comp303.fivehundred.util.AllCards.aHJo;
import static comp303.fivehundred.util.AllCards.aLJo;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import comp303.fivehundred.util.Card.BySuitNoTrumpComparator;

/**
 * @author Ian Forbes
 */

public class TestBySuitNoTrumpComparator
{
	private BySuitNoTrumpComparator tester = new BySuitNoTrumpComparator();
	
	@Test
	public void testCompare()
	{
		assertTrue(tester.compare(a4D, a4D) == 0);
		assertTrue(tester.compare(a4S, a8D) > 0);
		assertTrue(tester.compare(a9D, a5S) > 0);
		assertTrue(tester.compare(a4H, a4H) == 0);
		assertTrue(tester.compare(aHJo, a4D) > 0);
		assertTrue(tester.compare(a9C, aLJo) < 0);
		assertTrue(tester.compare(aHJo, aLJo) > 0);
	}
}
