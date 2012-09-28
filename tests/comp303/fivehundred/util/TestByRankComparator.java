package comp303.fivehundred.util;

import static comp303.fivehundred.util.AllCards.a4D;
import static comp303.fivehundred.util.AllCards.a5C;
import static comp303.fivehundred.util.AllCards.a6C;
import static comp303.fivehundred.util.AllCards.a7C;
import static comp303.fivehundred.util.AllCards.a8C;
import static comp303.fivehundred.util.AllCards.a8H;
import static comp303.fivehundred.util.AllCards.aAC;
import static comp303.fivehundred.util.AllCards.aHJo;
import static comp303.fivehundred.util.AllCards.aLJo;
import static org.junit.Assert.assertEquals;

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
		assertEquals(true, tester.compare(a5C, a5C) == 0);
		assertEquals(true, tester.compare(a4D, a7C) < 0);
		assertEquals(true, tester.compare(aAC, a6C) > 0);
		assertEquals(true, tester.compare(aHJo, aLJo) > 0);
		assertEquals(true, tester.compare(a8H, a8C) > 0);
	}
}
