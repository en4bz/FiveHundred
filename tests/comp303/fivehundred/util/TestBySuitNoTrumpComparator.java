package comp303.fivehundred.util;

import static comp303.fivehundred.util.AllCards.*;
import static org.junit.Assert.*;

import org.junit.Test;
import comp303.fivehundred.util.Card.BySuitNoTrumpComparator;
import comp303.fivehundred.util.Card.Suit;

/*
 * @author Ian Forbes
 */

public class TestBySuitNoTrumpComparator
{
	private BySuitNoTrumpComparator tester = new BySuitNoTrumpComparator();
	
	@Test
	public void testCompare()
	{
		assertEquals(true, tester.compare(a4D, a4D) == 0);
		assertEquals(true, tester.compare(a4S, a8D) > 0);
		assertEquals(true, tester.compare(a9D, a5S) > 0);
		assertEquals(true, tester.compare(a4H, a4H) == 0);
		assertEquals(true, tester.compare(aHJo, a4D) > 0);
		assertEquals(true, tester.compare(a9C, aLJo) < 0);
		assertEquals(true, tester.compare(aHJo, aLJo) > 0);
	}
}
