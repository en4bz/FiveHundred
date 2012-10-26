package comp303.fivehundred.engine;
import javax.management.Notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.mvc.Observer;
import comp303.fivehundred.player.APlayer;
import comp303.fivehundred.player.Team;

public class GameLogger implements Observer
{
	final Logger logger = LoggerFactory.getLogger(GameLogger.class);
	
	public void update(Notification pNotification)
	{
		if(pNotification.getType().equals("game.engine"))
		{
			GameEngine lGame = (GameEngine) pNotification.getSource();
			GameEngine.State lState = GameEngine.State.valueOf(pNotification.getMessage());
			switch (lState) 
			{
			   case newGame: 
				   logger.info("Game initialized. Initial dealer: {}", lGame.getPlayersInOrder()[0].getName());
				   logger.info("============================== NEW GAME ==============================");
				   break;
			   case newDeal: 
				   logger.info("******************** NEW DEAL ********************");
				   logger.info("Players dealt cards");
				   for(APlayer p: lGame.getPlayersInOrder())
				   {
					   logger.info("{}\t cards:  {}", p.getName(), p.getHand());
				   }
				   logger.info("The widow contains:  {}", lGame.getWidow());
				   break;
			   case newBid:
				   APlayer lCurrentPlayer = lGame.getCurrentPlayer();
				   Bid lCurrentBid = lGame.getBids()[lGame.getBids().length-1];
				   logger.info(lCurrentPlayer.getName() + "\t cards: " + lCurrentPlayer.getHand() + " bids " + lCurrentBid);
				   break;
			   case newContract:
				   logger.info("{} has the contract of {}", lGame.getContractor().getName(), lGame.getContract());
				   break;
			   case cardsDiscarded:
				   logger.info("{} discards {}", lGame.getContractor().getName(), lGame.getWidow());
				   logger.info("{}\t cards:  {}", lGame.getContractor().getName(), lGame.getContractor().getHand());
				   break;
			   case newTrick:
				   logger.info("---- TRICK {} ----", lGame.getTrickCounter());
				   break;
			   case cardPlayed:
				   APlayer lPlayer = lGame.getCurrentPlayer();
				   logger.info(lPlayer.getName()+ "\t cards: " + lPlayer.getHand() + " plays " + lGame.getCardPlayed());
				   break;
			   case trickWon:
				   logger.info("{} wins the trick", lGame.getTrickWinner().getName());
				   logger.info("{} has the contract of {}", lGame.getContractor().getName(), lGame.getContract());
				   Team[] lTeams = lGame.getTeams();
				   logger.info(lTeams[0].getPlayers()[0].getName() + " and "+ lTeams[0].getPlayers()[1].getName() + " won " + lTeams[0].getTricksWon() + " tricks");
				   logger.info(lTeams[1].getPlayers()[0].getName() + " and "+ lTeams[1].getPlayers()[1].getName() + " won " + lTeams[1].getTricksWon() + " tricks");
				   break;
			   case roundEnd:
				   logger.info("Contractor round score: {} \t Total score: {}", lGame.getContractorRoundScore(), lGame.getContractorTotalScore());
				   logger.info("Defenders round score: {} \t Total score: {}", lGame.getNonContractorRoundScore(), lGame.getNonContractorTotalScore());
				   break;
			   case gameOver:
				   logger.info("------------------------- GAME OVER --------------------------");
				   Team lWinners = lGame.getWinningTeam();
				   Team lLosers = lGame.getLosingTeam();
				   logger.info("{} and {} win the game", lWinners.getPlayers()[0].getName(), lWinners.getPlayers()[1].getName());
				   logger.info("{} and {} lose the game", lLosers.getPlayers()[0].getName(), lLosers.getPlayers()[1].getName());
				   break;
			default:
				break;
			}
		}
		
	}
	



}
