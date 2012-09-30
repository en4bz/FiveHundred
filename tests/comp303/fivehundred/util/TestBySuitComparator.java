package comp303.fivehundred.util;

import static comp303.fivehundred.util.AllCards.*;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import comp303.fivehundred.util.Card.BySuitComparator;
import comp303.fivehundred.util.Card.Suit;

/*
 * @author Ian Forbes
 */

public class TestBySuitComparator
{
	private BySuitComparator tester = new BySuitComparator(Suit.HEARTS);
	
	@Test
	public void testCompare()
	{
		assertTrue(tester.compare(a4H, a4H) == 0);
		assertTrue(tester.compare(a4H, aTC) > 0);
		assertTrue(tester.compare(aLJo, aHJo) < 0);
		assertTrue(tester.compare(aHJo, aKC) > 0);
		assertTrue(tester.compare(aJH, aJD) > 0);
		assertTrue(tester.compare(aJD, aJS) > 0);
	}
}
