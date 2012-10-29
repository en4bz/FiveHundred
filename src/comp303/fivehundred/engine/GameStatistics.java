package comp303.fivehundred.engine;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Set;

import comp303.fivehundred.player.APlayer;
import comp303.fivehundred.player.Team;

import javax.management.Notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import comp303.fivehundred.mvc.Observer;

/**
 * Observer statistics class that keeps track of the performance of the players during many game runs.
 * @author Eleyine Zarour
 *
 */
public class GameStatistics implements Observer
{
	private final Logger aLogger = LoggerFactory.getLogger("GameStatistics");
	
	private HashMap<APlayer, EnumMap<Stats, Integer>> aNumbers = new HashMap<APlayer, EnumMap<Stats, Integer>>();
	private HashMap<APlayer, EnumMap<Stats, Double>> aRatios = new HashMap<APlayer, EnumMap<Stats, Double>>();
	private GameEngine aGame;
		
	@Override
	public void update(Notification pNotification)
	{
		if(pNotification.getType().equals("game.engine"))
		{
			aGame = (GameEngine) pNotification.getSource();
			GameEngine.State lState = GameEngine.State.valueOf(pNotification.getMessage());
			assertPlayerKeysSet(aGame);
			switch (lState) 
			{
			   case newContract:
				   incrementNumber(aGame.getContractor(), Stats.contractsWon, 1);
				   break;
			   case trickWon:
				   incrementNumber(aGame.getTrickWinner(), Stats.tricksWon, 1);
				   break;
			   case roundEnd:
				   if(aGame.isContractWon())
				   {
					   incrementNumber(aGame.getContractor(), Stats.contractsMade, 1);
				   }
				   break;
			   case gameOver:
				   Team lWinningTeam = aGame.getWinningTeam();
				   for(APlayer p: lWinningTeam.getPlayers())
				   {
					   incrementNumber(p, Stats.gamesWon, 1);
					   incrementNumber(p, Stats.accumulatedScore, lWinningTeam.getScore());
				   }
				   Team lLosingTeam = aGame.getLosingTeam();
				   for(APlayer p: lLosingTeam.getPlayers())
				   {
					   incrementNumber(p, Stats.accumulatedScore, lLosingTeam.getScore());
				   }
				   break;
			   default:
				   break;
			}
		}
	}
	
	/**
	 * Print statistical summary of the game runs for each player namely the ratio of tricks 
	 * won, the ratio of contacts won, the ratio of contracts made, the ratio of won games
	 * and a score index.
	 */
	public void printStatistics()
	{
		Set<APlayer> lPlayers = aNumbers.keySet();
		aLogger.info("==================== STATISTICS =====================");
		aLogger.info(String.format("%-10s Trick\t Cont\t Made\t Game\t Score", ""));
		for(APlayer p: lPlayers)
		{
			if(!aRatios.containsKey(p))
			{
				aRatios.put(p, new EnumMap<Stats, Double>(Stats.class));
			}
			
			// update fields
			updateRatio(p, Stats.tricksWon);
			updateRatio(p, Stats.contractsWon);
			updateRatio(p, Stats.contractsMade);
			updateRatio(p, Stats.gamesWon);	
			double lAccScore = (double) aNumbers.get(p).get(Stats.accumulatedScore)/(getSum(Stats.gamesWon) * 500);
			aRatios.get(p).put(Stats.accumulatedScore, lAccScore);
			
			aLogger.info(String.format("%-10s %2.1f%%\t %2.1f%%\t %2.1f%%\t %2.1f%%\t %1.2f" , p.getName()
					, aRatios.get(p).get(Stats.tricksWon)
					, aRatios.get(p).get(Stats.contractsWon)
					, aRatios.get(p).get(Stats.contractsMade)
					, aRatios.get(p).get(Stats.gamesWon) 
					, aRatios.get(p).get(Stats.accumulatedScore)));
		}
		
	}
	
	// ---------------------- Helper Methods ----------------------------------
	
	private void incrementNumber(APlayer pPlayer, Stats pStatField, int pValue)
	{
		int lOldValue = 0;
		if(aNumbers.get(pPlayer).containsKey(pStatField))
		{
			lOldValue = aNumbers.get(pPlayer).get(pStatField);
		}
		aNumbers.get(pPlayer).put(pStatField, lOldValue + pValue);
	}
	
	private void assertPlayerKeysSet(GameEngine pGame)
	{
		for(APlayer p: pGame.getPlayersInOrder())
		{
			if(!aNumbers.containsKey(p))
			{
				EnumMap<Stats, Integer> lStatFields = new EnumMap<Stats, Integer>(Stats.class);
				lStatFields.put(Stats.tricksWon, 0);
				lStatFields.put(Stats.contractsWon, 0);
				lStatFields.put(Stats.contractsMade, 0);
				lStatFields.put(Stats.gamesWon, 0);
				lStatFields.put(Stats.accumulatedScore, 0);
				aNumbers.put(p, lStatFields);
			}
		}
	}
	
	private void updateRatio(APlayer pPlayer, Stats pStatField)
	{
		int lSum = getSum(pStatField);
		double lRatio = 0;
		if (lSum != 0)
		{

				lRatio = (double) aNumbers.get(pPlayer).get(pStatField) / getSum(pStatField) * 100;

		}
		aRatios.get(pPlayer).put(pStatField, lRatio);
	}
	
	private int getSum(Stats pStatField)
	{
		Set<APlayer> lPlayers = aNumbers.keySet();
		int lSum = 0;
		for(APlayer p: lPlayers)
		{
			lSum += aNumbers.get(p).get(pStatField);
		}
		if(pStatField == Stats.gamesWon)
		{
			lSum = lSum /2;
		}
		return lSum;
	}
	
	/**
	 * Describes the statistics fields to populate during many game runs.
	 * @author Eleyine
	 */
	private enum Stats 
	{
		contractsWon,
		tricksWon,
		contractsMade,
		gamesWon,
		accumulatedScore
	}

}