package comp303.fivehundred.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import comp303.fivehundred.util.TestCard;
import comp303.fivehundred.util.TestComparators;
import comp303.fivehundred.util.TestDeck;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	TestCard.class, 
	TestDeck.class,
	TestComparators.class,
	})
public class AllTests
{

}
