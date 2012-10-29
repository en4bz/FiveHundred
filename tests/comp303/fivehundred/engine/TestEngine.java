package comp303.fivehundred.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import comp303.fivehundred.ai.RandomRobot;
import comp303.fivehundred.model.Bid;
import comp303.fivehundred.player.APlayer;
import comp303.fivehundred.player.Team;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

/**
 * 
 * @author Ian Forbes
 *
 */
public class TestEngine
{
	private static final int NUMBER_OF_SIMULATIONS = 1000;
	private APlayer[] aPlayers;
	private GameEngine aTester;

	@Before
	public void setup()
	{
		aPlayers = new APlayer[4];
		aPlayers[0] = new RandomRobot("Rob1");
		aPlayers[1] = new RandomRobot("Bob2");
		aPlayers[2] = new RandomRobot("Rob11");
		aPlayers[3] = new RandomRobot("Bob22");
		
		Team One = new Team(aPlayers[0],aPlayers[1]);
		Team Two = new Team(aPlayers[2],aPlayers[3]);
		aTester = new GameEngine(One,Two);
	}
	
	@Test
	public void testInitialization()
	{
		assertNotNull(aTester.getPlayersInOrder());
		assertNotNull(aTester.getTeams());
	}
	

	public void testNewGame()
	{
		aTester.newGame();
		assertNull(aTester.getBids());
	    assertNull(aTester.getContract());
	    assertNull(aTester.getContractor());
	    assertTrue(!aTester.isGameOver());
	    assertEquals(0,aTester.getNonContractorTotalScore());
	    assertEquals(0, aTester.getContractorTotalScore());
	    assertNull(aTester.getWinningTeam());
	    assertNull(aTester.getLosingTeam());
	}
	
	@Test
	public void testDealOut()
	{
		testNewGame();
		aTester.deal();
		for(APlayer p : aPlayers)
		{
			assertEquals(10,p.getHand().size());
		}
		assertEquals(6,aTester.getWidow().size());
		CardList lReconstructedDeck = new CardList();
		for(APlayer p : aPlayers)
		{
			for(Card c : p.getHand())
			{
				lReconstructedDeck.add(c);
			}
		}
		for(Card c : aTester.getWidow())
		{
			lReconstructedDeck.add(c);
		}
		while(lReconstructedDeck.size() > 0)//Ensure no duplicates
		{
			Card c = lReconstructedDeck.getFirst();
			lReconstructedDeck.remove(c);
			if(Arrays.asList(lReconstructedDeck).indexOf(c) > 0)
			{
				fail();
			}
		}
	}
	
	public void testBidding()
	{
		do{
			for(APlayer p : aPlayers){
				if(p.getHand().size() > 0)
				{
					fail();
				}
			}
			aTester.deal();
			for(APlayer p : aPlayers){
				if(p.getHand().size() != 10)
				{
					fail();
				}
			}
			aTester.bid();
			
		}while(aTester.allPasses());
		assertTrue(!aTester.allPasses());
		assertTrue(!aTester.getContract().equals(new Bid()));//Contract Can't be a pass
		assertEquals(Bid.max(aTester.getBids()), aTester.getContract());
		assertTrue(!aTester.isGameOver());
	}
	
	public void testExchange()
	{
		aTester.exchange();
		assertEquals(6, aTester.getWidow().size());
		assertEquals(10, aTester.getContractor().getHand().size());
	}
	
	@Test
	public void testTrickPlay()
	{
		testNewGame();
		testBidding();
		testExchange();
		for(int i = 1; i <= 10; i++)
		{
			aTester.playTrick();
			for(APlayer p : aPlayers)
			{
				assertEquals(10-i,p.getHand().size());
			}
		}
	} 
	
	public void testRound(){
		aTester.playRound();
		for(APlayer p : aPlayers)
		{
			assertEquals(0,p.getHand().size());
		}
		assertNotNull(aTester.getTrickWinner());
	}
	
	public void testScoreComputation()
	{
		int teamOneInitialScore  = aTester.getContractorTotalScore();
		int teamTwoInitialScore = aTester.getNonContractorRoundScore();
		aTester.computeScore();
		//Assert someone gained/lost points 
		assertTrue(teamOneInitialScore != aTester.getContractorTotalScore()
				|| teamTwoInitialScore != aTester.getNonContractorRoundScore()); 
		if(aTester.getContractorTotalScore() >= 500 || aTester.getContractorTotalScore() <= -500)
		{
			assertTrue(aTester.isGameOver());
		}
		else
		{
			assertTrue(!aTester.isGameOver());
		}
	}
	
	@Test 
	public void simulate(){
		for(int i = 0; i < NUMBER_OF_SIMULATIONS; i++){ //Simulate X Games
			testNewGame();
			while(!aTester.isGameOver())
			{
				testBidding();
				testExchange();
				testRound();
				testScoreComputation();
			}
			testEndGame();
		}
	}
	
	public void testEndGame()
	{
		assertTrue(aTester.isGameOver());
		assertNotNull(aTester.getWinningTeam());
		assertNotNull(aTester.getLosingTeam());
		assertTrue(!aTester.getWinningTeam().equals(aTester.getLosingTeam()));
		assertTrue(aTester.getContractorTotalScore() >= 500 || aTester.getContractorTotalScore() <= -500);
	}
	
	@Test
	public void letsThrowSomeExceptions()
	{
		testNewGame();
		try{
			aTester.bid();
			fail();
		}catch(Exception e){}
		try{
			aTester.exchange();
			fail();
		}catch(Exception e){}
		try{
			aTester.playRound();
			fail();
		}
		catch(Exception e){}
	}
}
