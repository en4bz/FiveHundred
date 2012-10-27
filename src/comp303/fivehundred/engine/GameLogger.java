package comp303.fivehundred.engine;
import javax.management.Notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.mvc.Observer;
import comp303.fivehundred.player.APlayer;
import comp303.fivehundred.player.Team;
import comp303.fivehundred.util.CardList;

public class GameLogger implements Observer
{
	final Logger logger = LoggerFactory.getLogger("GameLogger");
	
	/**
	 *	Update the GameLogger according to the new game state (amongst the enum values specified in GameEngine.State).
	 *  @param pNotification the Notification object which holds information about the game state change
	 */
	public void update(Notification pNotification)
	{
		if(pNotification.getType().equals("game.engine"))
		{
			GameEngine lGame = (GameEngine) pNotification.getSource();
			GameEngine.State lState = GameEngine.State.valueOf(pNotification.getMessage());
			switch (lState) 
			{
			   case newGame: 
				   logger.info("Game initialized. Initial dealer: {}", lGame.getDealer().getName());
				   logger.info("============================== NEW GAME ==============================");
				   break;
			   case newDeal: 
				   logger.info("******************** NEW DEAL ********************");
				   logger.info("Players dealt cards by {}", lGame.getDealer().getName());
				   for(APlayer p: lGame.getPlayersInOrder())
				   {
					   logger.info(String.format("%-10s cards:  %s", p.getName(), p.getHand()));
				   }
				   logger.info("The widow contains:  {}", lGame.getWidow());
				   break;
			   case newBid:
				   APlayer lCurrentPlayer = lGame.getCurrentPlayer();
				   Bid lCurrentBid = lGame.getBids()[lGame.getBids().length-1];
				   logger.info(String.format("%-10s cards: %s bids %s", lCurrentPlayer.getName(), lCurrentPlayer.getHand(), lCurrentBid));
				   break;
			   case newContract:
				   logger.info("{} has the contract of {}", lGame.getContractor().getName(), lGame.getContract());
				   break;
			   case cardsDiscarded:
				   logger.info("{} discards {}", lGame.getContractor().getName(), lGame.getWidow());
				   logger.info(String.format("%-10s cards:  %s", lGame.getContractor().getName(), lGame.getContractor().getHand()));
				   break;
			   case newTrick:
				   logger.info("---- TRICK {} ----", lGame.getTrickCounter());
				   break;
			   case cardPlayed:
				   APlayer lPlayer = lGame.getCurrentPlayer();
				   CardList lCards = lPlayer.getHand();
				   lCards.add(lGame.getCardPlayed());
				   logger.info(String.format("%-10s cards: %s plays %s", lPlayer.getName(), lCards, lGame.getCardPlayed()));
				   break;
			   case trickWon:
				   logger.info("{} wins the trick", lGame.getTrickWinner().getName());
				   logger.info("{} has the contract of {}", lGame.getContractor().getName(), lGame.getContract());
				   Team[] lTeams = lGame.getTeams();
				   logger.info(String.format("%-10s and %-10s won %d tricks", lTeams[0].getPlayers()[0].getName(), lTeams[0].getPlayers()[1].getName(), lTeams[0].getTricksWon()));
				   logger.info(String.format("%-10s and %-10s won %d tricks", lTeams[0].getPlayers()[1].getName(), lTeams[1].getPlayers()[1].getName(), lTeams[1].getTricksWon()));
				   break;
			   case roundEnd:
				   logger.info("Contractor round score: {} \t Total score: {}", lGame.getContractorRoundScore(), lGame.getContractorTotalScore());
				   logger.info("Defenders round score: {} \t Total score: {}", lGame.getNonContractorRoundScore(), lGame.getNonContractorTotalScore());
				   break;
			   case gameOver:
				   logger.info("------------------------- GAME OVER --------------------------");
				   Team lWinners = lGame.getWinningTeam();
				   Team lLosers = lGame.getLosingTeam();
				   logger.info(String.format("%-10s and %-10s win the game", lWinners.getPlayers()[0].getName(), lWinners.getPlayers()[1].getName()));
				   logger.info(String.format("%-10s and %-10s lose the game", lLosers.getPlayers()[0].getName(), lLosers.getPlayers()[1].getName()));
				   break;
			default:
				break;
			}
		}
		
	}

}
