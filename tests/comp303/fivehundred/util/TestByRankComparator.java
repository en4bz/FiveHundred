package comp303.fivehundred.util;

import static comp303.fivehundred.util.AllCards.*;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import comp303.fivehundred.util.Card.ByRankComparator;


/*
 * @author Ian Forbes
 */
public class TestByRankComparator
{
	private ByRankComparator tester = new Card.ByRankComparator();
	@Test
	public void testCompareTo()
	{
		assertTrue(tester.compare(a5C, a5C) == 0);
		assertTrue(tester.compare(a4D, a7C) < 0);
		assertTrue(tester.compare(aAC, a6C) > 0);
		assertTrue(tester.compare(aHJo, aLJo) > 0);
		assertTrue(tester.compare(a8H, a8C) > 0);
		assertTrue(tester.compare(aJH, aJD) > 0);
		assertTrue(tester.compare(aJD, aJS) > 0);
		assertTrue(tester.compare(aKC, aJC) > 0);
	}
}
