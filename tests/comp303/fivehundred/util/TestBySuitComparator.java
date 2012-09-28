package comp303.fivehundred.util;

import static comp303.fivehundred.util.AllCards.a4H;
import static comp303.fivehundred.util.AllCards.aHJo;
import static comp303.fivehundred.util.AllCards.aKC;
import static comp303.fivehundred.util.AllCards.aLJo;
import static comp303.fivehundred.util.AllCards.aTC;
import static org.junit.Assert.assertEquals;

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
		assertEquals(0, tester.compare(a4H, a4H));
		assertEquals(1, tester.compare(a4H, aTC));
		assertEquals(-1, tester.compare(aLJo, aHJo));
		assertEquals(1, tester.compare(aHJo, aKC));
	}
}
