package comp303.fivehundred.engine;
import javax.management.Notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.mvc.Observer;
import comp303.fivehundred.player.APlayer;
import comp303.fivehundred.player.Team;
import comp303.fivehundred.util.CardList;

/**
 * Game Logger.
 * @author Gabrielle Germain
 *
 */
public class GameLogger implements Observer
{
	private final Logger aLogger = LoggerFactory.getLogger("GameLogger");
	
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
				   aLogger.info("Game initialized. Initial dealer: {}", lGame.getDealer().getName());
				   aLogger.info("============================== NEW GAME ==============================");
				   break;
			   case newDeal: 
				   aLogger.info("******************** NEW DEAL ********************");
				   aLogger.info("Players dealt cards by {}", lGame.getDealer().getName());
				   for(APlayer p: lGame.getPlayersInOrder())
				   {
					   aLogger.info(String.format("%-10s cards:  %s", p.getName(), p.getHand()));
				   }
				   aLogger.info("The widow contains:  {}", lGame.getWidow());
				   break;
			   case newBid:
				   APlayer lCurrentPlayer = lGame.getCurrentPlayer();
				   Bid lCurrentBid = lGame.getBids()[lGame.getBids().length-1];
				   aLogger.info(String.format("%-10s cards: %s bids %s", lCurrentPlayer.getName(), lCurrentPlayer.getHand(), lCurrentBid));
				   break;
			   case newContract:
				   aLogger.info("{} has the contract of {}", lGame.getContractor().getName(), lGame.getContract());
				   break;
			   case cardsDiscarded:
				   aLogger.info("{} discards {}", lGame.getContractor().getName(), lGame.getWidow());
				   aLogger.info(String.format("%-10s cards:  %s", lGame.getContractor().getName(), lGame.getContractor().getHand()));
				   break;
			   case newTrick:
				   aLogger.info("---- TRICK {} ----", lGame.getTrickCounter());
				   break;
			   case cardPlayed:
				   APlayer lPlayer = lGame.getCurrentPlayer();
				   CardList lCards = lPlayer.getHand();
				   lCards.add(lGame.getCardPlayed());
				   aLogger.info(String.format("%-10s cards: %s plays %s", lPlayer.getName(), lCards, lGame.getCardPlayed()));
				   break;
			   case trickWon:
				   aLogger.info("{} wins the trick", lGame.getTrickWinner().getName());
				   aLogger.info("{} has the contract of {}", lGame.getContractor().getName(), lGame.getContract());
				   Team[] lTeams = lGame.getTeams();
				   aLogger.info(String.format("%-10s and %-10s won %d tricks", lTeams[0].getPlayers()[0].getName(), lTeams[0].getPlayers()[1].getName(), lTeams[0].getTricksWon()));
				   aLogger.info(String.format("%-10s and %-10s won %d tricks", lTeams[0].getPlayers()[1].getName(), lTeams[1].getPlayers()[1].getName(), lTeams[1].getTricksWon()));
				   break;
			   case roundEnd:
				   aLogger.info("Contractor round score: {} \t Total score: {}", lGame.getContractorRoundScore(), lGame.getContractorTotalScore());
				   aLogger.info("Defenders round score: {} \t Total score: {}", lGame.getNonContractorRoundScore(), lGame.getNonContractorTotalScore());
				   break;
			   case gameOver:
				   aLogger.info("------------------------- GAME OVER --------------------------");
				   Team lWinners = lGame.getWinningTeam();
				   Team lLosers = lGame.getLosingTeam();
				   aLogger.info(String.format("%-10s and %-10s win the game", lWinners.getPlayers()[0].getName(), lWinners.getPlayers()[1].getName()));
				   aLogger.info(String.format("%-10s and %-10s lose the game", lLosers.getPlayers()[0].getName(), lLosers.getPlayers()[1].getName()));
				   break;
			default:
				break;
			}
		}
		
	}

}
