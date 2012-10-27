package comp303.fivehundred.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import comp303.fivehundred.ai.RandomBiddingStrategyTest;
import comp303.fivehundred.ai.RandomCardExchangeStrategyTest;
import comp303.fivehundred.ai.RandomPlayingStrategyTest;
import comp303.fivehundred.engine.TestEngine;
import comp303.fivehundred.model.TestBid;
import comp303.fivehundred.model.TestHand;
import comp303.fivehundred.model.TestTrick;
import comp303.fivehundred.util.TestCard;
import comp303.fivehundred.util.TestCardList;
import comp303.fivehundred.util.TestComparators;
import comp303.fivehundred.util.TestDeck;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	TestCard.class, 
	TestDeck.class,
	TestComparators.class,
	TestBid.class,
	TestHand.class,
	TestTrick.class,
	TestCardList.class,
	RandomBiddingStrategyTest.class,
	RandomCardExchangeStrategyTest.class,
	RandomPlayingStrategyTest.class,
	TestEngine.class,
	})
public class AllTests
{

}
