package comp303.fivehundred.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import comp303.fivehundred.ai.RandomRobot;
import comp303.fivehundred.model.Bid;
import comp303.fivehundred.player.APlayer;
import comp303.fivehundred.player.Team;

/**
 * 
 * @author Ian Forbes
 *
 */
public class TestEngine
{
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
	
	@Test
	public void testNewGame()
	{
		aTester.newGame();
		/*
	     assertNotNull(aTester.getBids());
	     assertNull(aTester.getContract());
	     assertNull(aTester.getContractor());
	     assertNotNull(aTester.getWidow());
	     assertTrue(!aTester.isGameOver());
	     assertEquals(0,aTester.getTrickCounter());
	     assertNull(aTester.getTrickWinner());
	     assertNull(aTester.getCardPlayed());
	     assertEquals(0,aTester.getContractorRoundScore());
	     assertEquals(0,aTester.getNonContractorTotalScore());
	     assertEquals(0, aTester.getContractorTotalScore());
	     assertEquals(0, aTester.getNonContractorTotalScore());
	     assertNull(aTester.getWinningTeam());
	     assertNull(aTester.getLosingTeam());
	     */
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
	}
	
	@Test 
	public void testBidding()
	{
		testNewGame();
		do{
			aTester.deal();
			aTester.bid();
		}while(aTester.allPasses());
		assertTrue(!aTester.allPasses());
		assertEquals(Bid.max(aTester.getBids()), aTester.getContract());
		assertTrue(!aTester.isGameOver());
		
	}
	
	@Test
	public void testExchange()
	{
		testBidding();
		aTester.exchange();
		assertEquals(6, aTester.getWidow().size());
		assertEquals(10, aTester.getContractor().getHand().size());
	}
	
	@Test 
	public void testTrickPlay()
	{
		testExchange();
		for(int i = 1; i <= 10; i++)
		{
			aTester.playTrick();
			for(APlayer p : aPlayers)
			{
				//assertEquals(10-i,p.getHand().size());
			}
		}
	} 
	
	@Test
	public void testRound(){
		testExchange();
		aTester.playRound();
		for(APlayer p : aPlayers)
		{
			assertEquals(0,p.getHand().size());
		}
	}
	
	@Test
	public void testScoreComputation()
	{
		testRound();
		aTester.computeScore();
		assertTrue(aTester.getContractorRoundScore() > 0 || aTester.getNonContractorRoundScore() > 0);
	}
}
