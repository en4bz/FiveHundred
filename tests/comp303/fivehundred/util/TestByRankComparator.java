package comp303.fivehundred.util;

import static comp303.fivehundred.util.*;
import static comp303.fivehundred.util.AllCards.*;
import static org.junit.Assert.*;

import org.junit.Test;

import comp303.fivehundred.util.Card.ByRankComparator;
import comp303.fivehundred.util.Card.BySuitComparator;
import comp303.fivehundred.util.Card.Suit;


/*
 * @author Ian Forbes
 */
public class TestByRankComparator
{
	private ByRankComparator tester = new Card.ByRankComparator();
	@Test
	public void testCompareTo()
	{
		assertEquals(0, tester.compare(a5C, a5C));
		assertEquals(-1, tester.compare(a4D, a7C));
		assertEquals(1, tester.compare(aAC, a6C));
		assertEquals(1, tester.compare(aHJo, aLJo));
		assertEquals(1, tester.compare(a8H, a8C));
	}
}
