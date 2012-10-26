package comp303.fivehundred.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import comp303.fivehundred.player.APlayer;
import comp303.fivehundred.player.Team;

import javax.management.Notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import comp303.fivehundred.mvc.Observer;

public class GameStatistics implements Observer
{
	final Logger logger = LoggerFactory.getLogger(GameLogger.class);
	
	HashMap<APlayer, HashMap<String, Integer>> aNumbers = new HashMap<APlayer, HashMap<String, Integer>>();
	HashMap<APlayer, HashMap<String, Double>> aRatios = new HashMap<APlayer, HashMap<String, Double>>();
	
	// fields to populate for each player
	private String aContractsWon = "contractsWon";
	private String aTricksWon = "tricksWon";
	private String aContractsMade = "contractsMade";
	private String aGamesWon = "gamesWon";
	private String aAccumulatedScore = "accumulatedScore";
	
	
	GameEngine aGame;
		
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
				   incrementNumber(aGame.getContractor(), aContractsWon, 1);
				   break;
			   case trickWon:
				   incrementNumber(aGame.getTrickWinner(), aTricksWon, 1);
				   break;
			   case roundEnd:
				   if(aGame.isContractWon())
				   {
					   incrementNumber(aGame.getContractor(), aContractsMade, 1);
				   }
				   break;
			   case gameOver:
				   Team lWinningTeam = aGame.getWinningTeam();
				   for(APlayer p: lWinningTeam.getPlayers())
				   {
					   incrementNumber(p, aGamesWon, 1);
					   incrementNumber(p, aAccumulatedScore, lWinningTeam.getScore());
				   }
				   Team lLosingTeam = aGame.getLosingTeam();
				   for(APlayer p: lLosingTeam.getPlayers())
				   {
					   incrementNumber(p, aAccumulatedScore, lLosingTeam.getScore());
				   }
				   break;
			   default:
				   break;
			}
		}
	}
	// ---------------------- Helper Methods ----------------------------------
	
	private void incrementNumber(APlayer pPlayer, String pStatField, int pValue)
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
				HashMap<String, Integer> lStatFields = new HashMap<String, Integer>();
				lStatFields.put(aTricksWon, 0);
				lStatFields.put(aContractsWon, 0);
				lStatFields.put(aContractsMade, 0);
				lStatFields.put(aGamesWon, 0);
				lStatFields.put(aAccumulatedScore, 0);
				aNumbers.put(p, lStatFields);
			}
		}
	}
	
	private void updateRatio(APlayer pPlayer, String pStatField)
	{
		int lSum = getSum(pStatField);
		double lRatio = 0;
		if (lSum != 0)
		{
			lRatio = (double) aNumbers.get(pPlayer).get(pStatField) / getSum(pStatField) * 100;
		}
		aRatios.get(pPlayer).put(pStatField, lRatio);
	}
	
	private int getSum(String pStatField)
	{
		Set<APlayer> lPlayers = aNumbers.keySet();
		int lSum = 0;
		for(APlayer p: lPlayers)
		{
			lSum += aNumbers.get(p).get(pStatField);
		}
		return lSum;
	}
	
	public void printStatistics()
	{
		Set<APlayer> lPlayers = aNumbers.keySet();
		logger.info(String.format("%-10s Trick\t Cont\t Made\t Game\t Score", ""));
		for(APlayer p: lPlayers)
		{
			if(!aRatios.containsKey(p))
			{
				aRatios.put(p, new HashMap<String, Double>());
			}
			
			// update fields
			updateRatio(p, aTricksWon);
			updateRatio(p, aContractsWon);
			updateRatio(p, aContractsMade);
			updateRatio(p, aGamesWon);	
			double lAccScore = (double) aNumbers.get(p).get(aAccumulatedScore)/(getSum(aGamesWon) * 500);
			aRatios.get(p).put(aAccumulatedScore, lAccScore);
			
			logger.info(String.format("%-10s %2.1f\t %2.1f\t %2.1f\t %2.1f\t %2.1f" , p.getName()
					, aRatios.get(p).get(aTricksWon)
					, aRatios.get(p).get(aContractsWon)
					, aRatios.get(p).get(aContractsMade)
					, aRatios.get(p).get(aGamesWon) 
					, aRatios.get(p).get(aAccumulatedScore)));
		}
		
	}

}