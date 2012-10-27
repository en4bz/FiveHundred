package comp303.fivehundred.engine;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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

/**
 * A class that can store the entire state of the game, and provides 
 * methods to modify the state according to the rules of the game five hundred.
 * @author Eleyine Zarour and Ian Forbes
 *
 */
public class GameEngine extends Observable
{
	// State fields
    private APlayer[] aPlayers; // keeps the current playing order
    private APlayer aCurrentPlayer; // player who is currently executing a game action
    private Team aTeam1;
    private Team aTeam2;
    private APlayer aDealer;
    private Bid[] aBids;
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
    
    /**
     * Constructs a new game engine for two teams of two players. The
     * players cannot change teams. Methods in the game engine will
     * have side effects on the teams.
     * @param pTeam1 a team participating in the game
     * @param pTeam2 the other team participating in the game
     */
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
    
    /**
     * Change game engine state to new game and reset all appropriate class fields.
     * The first dealer is chosen at random by rotating the player array using a random right shift.
     */
    public void newGame()
    {
        aTeam1.reset();
        aTeam2.reset();
        aAllPasses = true;
        aBids = new Bid[4];
        aContract = new Bid();
        aContractor = null;
        aWidow = new Hand();
        aTrickWinner = null;
        aCardPlayed = null;
        aContractorTotalScore = 0;
        aNonContractorTotalScore = 0;
        aContractWon = false;
        aWinningTeam = null;
        aLosingTeam = null;
        aGameOver = false;
        // choose first dealer at random
        Random lRand = new Random();
        aDealer = aPlayers[lRand.nextInt(aPlayers.length)];        
        notifyObservers(new Notification("game.engine", this, getNotificationSequenceNumber(), "newGame"));    	
    }
    /**
     * Shuffle cards, deal 10 cards to each player and place six in the form of a widow.
     * The first dealer is chosen at random, and the turn to deal rotates clockwise after each hand.
     */
    public void deal()
    {
    	// rotate current player order such that dealer is first
    	aPlayers = rotate(aPlayers, aDealer);
    	
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
        
        aWidow = new Hand();
        while(lDeck.size() > 0)
        {
            aWidow.add(lDeck.draw());
        }
        notifyObservers(new Notification("game.engine", this, getNotificationSequenceNumber(), "newDeal"));   
    
        // update playing order so that player to dealer's left starts bidding
        APlayer lFirstBidder = aPlayers[1];
        aPlayers = rotate(aPlayers, lFirstBidder);
    }
    
    /**
     * Starts a new bidding round such that the bidding begins with the player to dealer's left
     * and continues clockwise until all players have bid once and only once. 
     * Once someone has bid, each subsequent bid must be higher than the previous one. 
     * If all players pass, no contract is made and the same dealer deals a fresh round.
     * Otherwise, the highest bid becomes the contract and the turn to deal rotates clockwise.
     */
    public void bid()
    {
        aAllPasses = true;
        Bid[]lBids = new Bid[4];
        aContract = new Bid(); // default is pass
        Bid lBid = new Bid(); // last bid
        
        // make sure no team has a contract
        for(Team t: getTeams())
        {
    		t.setContract(new Bid());
        }
        
        aBids = new Bid[0];
        
        for(int i = 0; i < aPlayers.length; i++)
	    {
        	aCurrentPlayer = aPlayers[i];
        	lBid = aPlayers[i].selectBid(aBids);
            lBids[i] = lBid;
            aBids = Arrays.copyOf(lBids, i+1);
            notifyObservers(new Notification("game.engine", this, getNotificationSequenceNumber(), "newBid"));

            if (lBid.compareTo(aContract) > 0)
            {
                aContractor = aPlayers[i];
                aContract = lBid;
                aAllPasses = false;
            }
	    } 
        
        if(!allPasses())
        {
	        // update contractor team
	        Team lContractorTeam = (aTeam1.isInTeam(aContractor))? aTeam1: aTeam2;
	        lContractorTeam.setContract(aContract);
	        notifyObservers(new Notification("game.engine", this, getNotificationSequenceNumber(), "newContract"));
	        // update dealer
	        aDealer = aPlayers[0];
        }
    }
    
    /**
     * @return true if all players pass during the bidding round.
     */
    public boolean allPasses()
    {
        return aAllPasses;
    }
    
    /** The contractor takes the widow along with his hand and discards six cards of
     *  his or her choice.
     */
    public void exchange()
    {    	
    	// get contractor index (I don't even understand why this is necessary in the strategy)
    	int lContractorIndex;
    	for(lContractorIndex = 0; lContractorIndex < aPlayers.length ; lContractorIndex++)
    	{
    		if(aContractor == aPlayers[lContractorIndex]) 
    		{
    			break;
    		}
    	}
    	
        CardList lDiscarded = aContractor.exchange(aBids, lContractorIndex, aWidow);
        aWidow = new Hand();
        for(Card c: lDiscarded)
        {
        	aWidow.add(c);
        }
        notifyObservers(new Notification("game.engine", this, getNotificationSequenceNumber(), "cardsDiscarded"));

    }
    
    /**
     * Play a round of ten tricks. The contractor leads the first trick. 
     */
    public void playRound()
    {
    	aPlayers = rotate(aPlayers, aContractor);
    	aContractorRoundScore = 0;
    	aNonContractorRoundScore = 0;
    	for(aTrickCounter = 1; aTrickCounter <= 10; aTrickCounter++)
    	{
    		playTrick();
    	}
    }
    
    /**
     * Play trick such that players follow suit if they can. A trick is won by the highest trump in it, 
     * or if no trump is played by the highest card of the suit led. The winner of a trick leads to the next.
    */
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
        aPlayers = rotate(aPlayers, aTrickWinner);
        
    }
    /**
     * Helper method to rotate the circular player array such that the specified player 
     * becomes first while conserving the playing order. 
     * @param pPlayers the current playing order
     * @param pPlayer the player who will become first
     * @return the playing order such that the specified player is now at index 0
     */
    private APlayer[] rotate(APlayer[] pPlayers, APlayer pPlayer)
    {
    	List<APlayer> lPlayerList = Arrays.asList(aPlayers);
    	int lIndex = lPlayerList.indexOf(pPlayer);
        Collections.rotate(lPlayerList, -lIndex);
        return (APlayer[]) lPlayerList.toArray();
    }
    
    /**
     * Compute round scores based on number of tricks won per team and update total scores.
     * End the game if the contractor team has a total score greater than 500 or less than -500.
     * Note that even if the defenders exceed a total score of 500 points thanks to odd tricks, 
     * they can't win the game in this round.
     */
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

        // reset number of tricks won
        for(Team t: getTeams())
        {
        	t.setTricksWon(0);
        }
        
        // End game if necessary
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
    
    /**
     * Returns the current playing order. Mutable.
     * @return the current playing order. 
     */
    protected APlayer[] getPlayersInOrder()
    {
    	return Arrays.copyOf(aPlayers, aPlayers.length);
    }
    
    /**
     * Returns the participating teams. Mutable.
     * @return the participating teams in the game
     */
    protected Team[] getTeams()
    {
    	Team[] lTeams = {aTeam1, aTeam2};
    	return lTeams;
    }
    /**
     * Returns the current dealer.
     * @return the current dealer
     */
    protected APlayer getDealer()
    {
    	return aDealer;
    }
    
    /**
     * Returns current player in a trick context. Mutable.
     * @return current player in trick
     */
    protected APlayer getCurrentPlayer()
    {
    	return aCurrentPlayer;
    }
    
    /**
     * Returns the trick winner.
     * @return the trick winner.
     */
    protected APlayer getTrickWinner()
    {
    	return aTrickWinner;
    }
    
    /**
     * Returns the bids done so far.
     * @return array of bids (size 0 to 4)
     */
    protected Bid[] getBids()
    {
    	return aBids;
    }
    
    /**
     * Returns the widow generated after players were dealt cards.
     * @return widow 
     */
    protected CardList getWidow()
    {
    	return aWidow.clone();
    }
    
    /**
     * Returns the game contractor player.
     * @return the game contractor player. If there's no contractor, returns null.
     */
    protected APlayer getContractor()
    {
    	return aContractor;
    }
    
    /**
     * Returns the game contract.
     * @return the game contract. If there's no contract, returns null.
     */
    protected Bid getContract()
    {
    	return aContract;
    }
    
    /**
     * Returns the card played in a trick context. Out of a trick, returns irrelevant information.
     * @return card played in trick context by current player
     */
    protected Card getCardPlayed()
    {
    	return aCardPlayed;
    }
    
    /**
     * Returns true if the contractor team respected the contract. Returns false if the contract
     * has not been decided yet or if the contract has not been won.
     * @return true if contractor team respected the contract.
     */
    protected boolean isContractWon()
    {
    	return aContractWon;
    }
    
    /**
     * The number of tricks played in the current round.
     * @return the number of tricks played.
     */
    protected int getTrickCounter()
    {
    	return aTrickCounter;
    }
    
    /**
     * 
     * @return the contractors' round score
     */
    protected int getContractorRoundScore()
    {
    	return aContractorRoundScore;
    }
    
    /**
     * 
     * @return the defenders' round score
     */
    protected int getNonContractorRoundScore()
    {
    	return aNonContractorRoundScore;
    }
    
    /**
     * 
     * @return the contractors' total score
     */
    protected int getContractorTotalScore()
    {
    	return aContractorTotalScore;
    }
    
    /**
     * 
     * @return the defenders' total score
     */
    protected int getNonContractorTotalScore()
    {
    	return aNonContractorTotalScore;
    }
    
    /**
     * Returns the winners' team when the game is over.
     * @return the winners' team 
     */
    public Team getWinningTeam()
    {
    	return aWinningTeam;
    }
    
    /**
     * Returns the losers' team when the game is over.
     * @return the losers' team
     */
    public Team getLosingTeam()
    {
    	return aLosingTeam;
    } 
    
    /**
     * Returns true if the game is over.
     * @return true if the game is over
     */
    public boolean isGameOver()
    {
        return aGameOver;
    }
    
    /**
     * Enum class representing the different states of the game engine that can be 
     * passed as a notification message to the different class observers.
     * @author Eleyine
     *
     */
	public enum State 
	{
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
