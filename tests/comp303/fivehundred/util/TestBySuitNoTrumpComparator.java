package comp303.fivehundred.util;

import static comp303.fivehundred.util.AllCards.*;
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
		assertTrue(tester.compare(a9D, a9S) > 0);
		assertTrue(tester.compare(a4H, a4H) == 0);
		assertTrue(tester.compare(aHJo, a4D) > 0);
		assertTrue(tester.compare(a9C, aLJo) < 0);
		assertTrue(tester.compare(aHJo, aLJo) > 0);
	}
}
