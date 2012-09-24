package comp303.fivehundred.util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import comp303.fivehundred.util.TestComparators;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	TestByRankComparator.class, 
	TestBySuitComparator.class,
	TestBySuitNoTrumpComparator.class
	})
public class TestComparators
{
	// TODO
}
