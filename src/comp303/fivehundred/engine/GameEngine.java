package comp303.fivehundred.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import comp303.fivehundred.mvc.Observable;
import javax.management.Notification;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.player.APlayer;
import comp303.fivehundred.player.Team;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;
import comp303.fivehundred.util.Deck;


public class GameEngine extends Observable
{
	// State fields
    private APlayer[] aPlayers; // keeps the current playing order
    private APlayer aCurrentPlayer; // player who is currently executing a game action
    private Team aTeam1;
    private Team aTeam2;
    private ArrayList<Bid> aBids;
    private boolean aAllPasses;
    private Bid aContract;
    private APlayer aContractor;
    private Hand aWidow;
    private boolean aGameOver;
    private int aTrickCounter;
    private Card aCardPlayed;
    private APlayer aTrickWinner;
    private boolean aContractWon;
    private int aContractorRoundScore;
    private int aNonContractorRoundScore;
    private int aContractorTotalScore;
    private int aNonContractorTotalScore;
    private Team aWinningTeam;
    private Team aLosingTeam;
    

    
    public GameEngine(Team pTeam1, Team pTeam2)
    {
        aTeam1 = pTeam1;
        aTeam2 = pTeam2;
        APlayer[] lTeam1Players = aTeam1.getPlayers();
        APlayer[] lTeam2Players = aTeam2.getPlayers();
        // TODO write method to customize initial playing order 
        // e.g. flip a coin, table disposition etc.
        aPlayers = new APlayer[4];
        aPlayers[0] = lTeam1Players[0];
        aPlayers[1] = lTeam2Players[0];
        aPlayers[2] = lTeam1Players[1];
        aPlayers[3] = lTeam2Players[1];
    }
    

    // --------------------- Game flow methods ---------------------
    
    
    public void newCustomGame(boolean shuffle, boolean automatic)
    {
        aAllPasses = true;
        aBids = new ArrayList<Bid>();
        aContract = null;
        aContractor = null;
        aWidow = new Hand();
        aGameOver = false;
        aTrickCounter = 0;
        aTrickWinner = null;
        aCardPlayed = null;
        aContractorRoundScore = 0;
        aNonContractorRoundScore = 0;
        aContractorTotalScore = 0;
        aNonContractorTotalScore = 0;
        aWinningTeam = null;
        aLosingTeam = null;
        
        if(automatic)
        {
            if(shuffle)
            {
            	// TODO CODESTYLE find more elegant way to shuffle array
            	List<APlayer> lPlayersList = Arrays.asList(aPlayers);
            	Collections.shuffle(lPlayersList);
                aPlayers = (APlayer[]) lPlayersList.toArray();
                aTeam1 = new Team(aPlayers[0], aPlayers[2]);
                aTeam1 = new Team(aPlayers[1], aPlayers[3]);
            }
            else
            {
                aTeam1.reset();
                aTeam2.reset();
            }
        }
        else
        {
            // TODO INTERACTIVE prompt to assign teams
            // TODO INTERACTIVE prompt to shuffle
        }
        // TODO some new game cases
        // Case 1: teams stay the same
        // Case 2: teams get randomly shuffled
        // Case 3: human chooses new team
    }
    
    public void newGame()
    {
        newCustomGame(false, true);
        notifyObservers(new Notification("game.engine", this, getNotificationSequenceNumber(), "newGame"));
    }

    public void deal()
    {

    	Deck lDeck = new Deck();
        for(APlayer p: aPlayers)
        {
            Hand lHand = new Hand();
            for(int i = 0; i < 10; i++)
            {
               lHand.add(lDeck.draw());
            }
            p.setHand(lHand);
        }
        
        while(lDeck.size() > 0)
        {
            aWidow.add(lDeck.draw());
        }
        
        notifyObservers(new Notification("game.engine", this, getNotificationSequenceNumber(), "newDeal"));
    }
    

    public void bid()
    {
        aAllPasses = true;
        aBids = new ArrayList<Bid>();
        aContract = new Bid(); // default is pass
        Bid lBid = new Bid(); // last bid
        do
        {
	        for(APlayer p: aPlayers)
	        {
	        	aCurrentPlayer = p;
	        	lBid = p.selectBid(aBids.toArray(new Bid[aBids.size()]));
	            aBids.add(lBid);
	            notifyObservers(new Notification("game.engine", this, getNotificationSequenceNumber(), "newBid"));
	
	            if (lBid.compareTo(aContract) > 0)
	            {
	                aContractor = p;
	                aContract = lBid;
	                aAllPasses = false;
	            }
	        }
        } while (!lBid.isPass() && !allPasses());
        
        if(!allPasses())
        {
	        // update contractor team
	        Team lContractorTeam = (aTeam1.isInTeam(aContractor))? aTeam1: aTeam2;
	        lContractorTeam.setContract(aContract);
	        notifyObservers(new Notification("game.engine", this, getNotificationSequenceNumber(), "newContract"));
        }
    }
    
    public boolean allPasses()
    {
        return aAllPasses;
    }
    
    public void exchange()
    {
    	Bid[] lastFourBids = aBids.subList(aBids.size()-4, aBids.size()).toArray(new Bid[4]);
    	
    	// get contractor index (I don't even understand why this is necessary in the strategy)
    	int lContractorIndex;
    	for(lContractorIndex = 0; lContractorIndex < aPlayers.length ; lContractorIndex++)
    	{
    		if(aContractor == aPlayers[lContractorIndex]) 
    		{
    			break;
    		}
    	}
        CardList lDiscarded = aContractor.exchange(lastFourBids, lContractorIndex, aWidow);
        aWidow = new Hand();
        for(Card c: lDiscarded)
        {
        	aWidow.add(c);
        }
        notifyObservers(new Notification("game.engine", this, getNotificationSequenceNumber(), "cardsDiscarded"));

    }
    
    public void playRound()
    {
    	for(aTrickCounter = 1; aTrickCounter <= 10; aTrickCounter++)
    	{
    		playTrick();
    	}
    }
    
    public void playTrick()
    {
        notifyObservers(new Notification("game.engine", this, getNotificationSequenceNumber(), "newTrick"));
        Trick lTrick = new Trick(aContract);
        for(APlayer p: aPlayers)
        {
        	aCurrentPlayer = p;
        	aCardPlayed = p.play(lTrick);
            lTrick.add(aCardPlayed);
            notifyObservers(new Notification("game.engine", this, getNotificationSequenceNumber(), "cardPlayed"));
        }
        
        // update tricks won
        int lWinnerIndex = lTrick.winnerIndex(); 
        aTrickWinner = aPlayers[lWinnerIndex];
        Team lWinningTeam = (aTeam1.isInTeam(aTrickWinner))? aTeam1: aTeam2;
        lWinningTeam.setTricksWon(lWinningTeam.getTricksWon()+1);
        notifyObservers(new Notification("game.engine", this, getNotificationSequenceNumber(), "trickWon"));
        
        // winner now leads
        aPlayers = rotate(aPlayers, -lWinnerIndex);
        
    }
    /**
     * Helper method to rotate the player array such that the winner now leads.
     * @param pPlayers the playing order during the trick
     * @param pWinnerIndex the trick's winner index
     * @return the playing order after the trick such that the winner now leads
     */
    private APlayer[] rotate(APlayer[] pPlayers, int pWinnerIndex)
    {
    	List<APlayer> lPlayerList = Arrays.asList(aPlayers);
        Collections.rotate(lPlayerList, -pWinnerIndex);
        return (APlayer[]) lPlayerList.toArray();
    }
    
    public void computeScore()
    {
        Team lContractorTeam = (aTeam1.isContractor())? aTeam1: aTeam2;
        Team lNonContractorTeam = (aTeam1.isContractor())? aTeam2: aTeam1;
        
        // compute round scores
        aContractorRoundScore = lContractorTeam.getContract().getScore();
        aContractWon = lContractorTeam.getTricksWon() >= lContractorTeam.getContract().getTricksBid();        
        if(aContractWon)
        {
            // If there's a slam
            if(lContractorTeam.getTricksWon() == 10 && aContractorRoundScore < 250)
            {
                aContractorRoundScore = 250;
            }
        } 
        else
        {
            aContractorRoundScore = -aContractorRoundScore;
        }

        aNonContractorRoundScore = lNonContractorTeam.getTricksWon() * 10;
        
        
        // compute total scores
        aContractorTotalScore = lContractorTeam.getScore() + aContractorRoundScore;
        lContractorTeam.setScore(aContractorTotalScore);        
        aNonContractorTotalScore = lNonContractorTeam.getScore() + aNonContractorRoundScore;
        lNonContractorTeam.setScore(aNonContractorTotalScore);
        
        notifyObservers(new Notification("game.engine", this, getNotificationSequenceNumber(), "roundEnd"));

        // End game if necessary
        // Note: According to the rules, NonContractor can't win the game by simply winning odd tricks.
        if(aContractorTotalScore >= 500)
        {
            endGame(lContractorTeam); 
        } 
        
        if(aContractorTotalScore <= -500)
        {
            endGame(lNonContractorTeam);
        }
    }

    // can come in handy for logging
    private void endGame(Team pWinningTeam)
    {
    	aWinningTeam = pWinningTeam;
    	aLosingTeam = aTeam1.isInTeam(pWinningTeam.getPlayers()[0])? aTeam2: aTeam1;
    	aGameOver = true;
        notifyObservers(new Notification("game.engine", this, getNotificationSequenceNumber(), "gameOver"));   
    }
    
    // --------------------- Game state methods ------------
    // TODO CODESTYLE wonder if this violates encapsulation laws...
    // makes it possible for any observer to change players and teams through setters
    // consider making clones
    
    public APlayer[] getPlayersInOrder()
    {
    	return Arrays.copyOf(aPlayers, aPlayers.length);
    }
    
    public Team[] getTeams()
    {
    	Team[] lTeams = {aTeam1, aTeam2};
    	return lTeams;
    }
    
    public APlayer getCurrentPlayer()
    {
    	return aCurrentPlayer;
    }
    
    public APlayer getTrickWinner()
    {
    	return aTrickWinner;
    }
    
    public Bid[] getBids()
    {
    	return aBids.toArray(new Bid[aBids.size()]);
    }
    
    public CardList getWidow()
    {
    	return aWidow.clone();
    }
    
    public APlayer getContractor()
    {
    	return aContractor;
    }
    
    public Bid getContract()
    {
    	return aContract;
    }
    
    public Card getCardPlayed()
    {
    	return aCardPlayed;
    }
    
    public boolean isContractWon()
    {
    	return aContractWon;
    }
    
    public int getTrickCounter()
    {
    	return aTrickCounter;
    }
    
    public int getContractorRoundScore()
    {
    	return aContractorRoundScore;
    }
    public int getNonContractorRoundScore()
    {
    	return aNonContractorRoundScore;
    }
    public int getContractorTotalScore()
    {
    	return aContractorTotalScore;
    }
    public int getNonContractorTotalScore()
    {
    	return aNonContractorTotalScore;
    }
    
    public Team getWinningTeam()
    {
    	return aWinningTeam;
    }
    
    public Team getLosingTeam()
    {
    	return aLosingTeam;
    } 
    
    public boolean isGameOver()
    {
        return aGameOver;
    }
    
	public enum State {
		newGame,
		newDeal,
		newBid,
		newContract,
		cardsDiscarded,
		cardPlayed,
		newTrick,
		trickWon,
		roundEnd,
		gameOver
	}
}
