package comp303.fivehundred.engine;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.management.Notification;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.mvc.Observable;
import comp303.fivehundred.player.APlayer;
import comp303.fivehundred.player.Team;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;
import comp303.fivehundred.util.Deck;

/**
 * A class that can store the entire state of the game, and provides 
 * methods to modify the state according to the rules of the game five hundred.
 * @author Eleyine Zarour and Ian Forbes (Design)
 * @author Gabrielle Germain (Exception handling)
 */
public class GameEngine extends Observable
{
    //Constants
    private static final int MAXTRICKS = 10;
    private static final int CARDSINHAND = 10;
    private static final int WINSCORE = 500;
    private static final int LOSESCORE = -500;
    private static final int SLAM = 250;
    private static final int TRICKSCORE = 10;
    
	// State fields
    private APlayer[] aPlayers; // keeps the current playing order
    private APlayer aCurrentPlayer; // player who is currently executing a game action
    final private Team aTeam1;
    final private Team aTeam2;
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
        aBids = null;
        aContract = null;
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
        notifyObservers(new Notification("game.engine", this, getNotificationSequenceNumber(), State.newGame.toString()));    	
    }
    /**
     * Shuffle cards, deal 10 cards to each player and place six in the form of a widow.
     * The first dealer is chosen at random, and the turn to deal rotates clockwise after each hand.
     */
    public void deal()
    {
    	// rotate current player order such that dealer is first
    	aPlayers = rotate(aPlayers, aDealer);
    	
    	// initialize deck
    	Deck lDeck = new Deck();
    	lDeck.shuffle();
    	
    	// distribute the cards
        for(APlayer p: aPlayers)
        {
            for(int i = 0; i < CARDSINHAND; i++)
            {
               p.addCardToHand(lDeck.draw());
            }
        }
        
        // make the widow with remaining cards
        aWidow = new Hand();
        while(lDeck.size() > 0)
        {
            aWidow.add(lDeck.draw());
        }
        notifyObservers(new Notification("game.engine", this, getNotificationSequenceNumber(), State.newDeal.toString()));   
    
        // update playing order so that player to dealer's left starts bidding
        aPlayers = rotate(aPlayers, aPlayers[1]);
    }
    
    /**
     * Starts a new bidding round such that the bidding begins with the player to dealer's left
     * and continues clockwise until all players have bid once and only once. 
     * Once someone has bid, each subsequent bid must be higher than the previous one. 
     * If all players pass, no contract is made and the same dealer deals a fresh round.
     * Otherwise, the highest bid becomes the contract and the turn to deal rotates clockwise.
     * @throws GameException if deal() is not called before bid()
     * @throws GameException if players make an invalid bid
     * @throws GameException if teams have a contract before calling bid()
     * @post one team holds a contract and turn to deal rotates clockwise if !allPasses()
     * @post same dealer if allPasses()
     */
    public void bid()
    {
    	// check that each player has 10 cards in hand
    	for(APlayer p: aPlayers)
    	{
    		if (p.getHand().size() != CARDSINHAND)
    			{
    				throw new GameException("Cards must be dealt before the player deals");
    			}
    	}
    	
        aAllPasses = true;
        Bid[]lBids = new Bid[4];
        aContract = new Bid(); // default is pass
        Bid lBid = new Bid(); // last bid
        
        // make sure no team has a contract
        for(Team t: getTeams())
        {
    		t.setContract(new Bid());
        }
        
        // construct previousBids array to pass on to APlayer.selectBid
        aBids = new Bid[0];
        
        for(int i = 0; i < aPlayers.length; i++)
	    {
        	aCurrentPlayer = aPlayers[i];
        	lBid = aPlayers[i].selectBid(aBids);
        	
        	// validate bid
        	if(aBids.length > 0 && !lBid.isPass() && lBid.compareTo(Bid.max(aBids)) <= 0)
        	{
    			throw new GameException("Player must make a higher bid or pass." + lBid +" <= " + Bid.max(aBids));
        	}
        	
            lBids[i] = lBid;
            aBids = Arrays.copyOf(lBids, i+1);
            notifyObservers(new Notification("game.engine", this, getNotificationSequenceNumber(), State.newBid.toString()));

            // update contract if bid is not pass
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
	        Team lContractorTeam = getTeamFromPlayer(aContractor);
	        lContractorTeam.setContract(aContract);
	        notifyObservers(new Notification("game.engine", this, getNotificationSequenceNumber(), State.newContract.toString()));
	        // update dealer
	        aDealer = aPlayers[0];
        }
        else
        {
        	for(APlayer p : aPlayers)
        	{
        		p.resetHand();
        	}
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
     *  @throws GameException if the game has no contractor yet
     *  @throws GameException if cards discarded were neither in the hand nor the widow
     *  @post Contractor has 10 cards in hand
     */
    public void exchange()
    {    	
    	if (aContractor == null)
    	{
    		throw new GameException("Every game needs a contractor");
    	}
    	
    	int lContractorIndex = Arrays.asList(aPlayers).indexOf(aContractor);    	
    	Hand oldHand = aContractor.getHand();
        CardList lDiscarded = aContractor.exchange(aBids, lContractorIndex, aWidow);
        
        for(Card c: lDiscarded)
        {
        	if (!oldHand.contains(c) && !aWidow.contains(c))
        	{
        		throw new GameException("Player discarded cards that were neither in hand nor widow.");
        	}
        }
        
        aWidow = new Hand();
        for(Card c: lDiscarded)
        {
        	aWidow.add(c);
        }
        
        assert aContractor.getHand().size() == CARDSINHAND;
        notifyObservers(new Notification("game.engine", this, getNotificationSequenceNumber(), State.cardsDiscarded.toString()));

    }
    
    /**
     * Play a round of ten tricks. The contractor leads the first trick. 
     * @throws GameException if one player does not have 10 cards before playing the first trick
     * @throws GameException if contractor or contract is not specified
     * @throws GameException if players do not have an empty hand after playing 10 tricks
     */
    public void playRound()
    {
    	if(aContractor == null || aContract == null)
    	{
    		throw new GameException("There must be a contractor and a contract before playing a round.");
    	}
    	
    	// contractor leads
    	aPlayers = rotate(aPlayers, aContractor);
    	aContractorRoundScore = 0;
    	aNonContractorRoundScore = 0;
    	
    	// all players must have 10 cards before the first trick
    	for(APlayer p: aPlayers)
    	{
    		if (p.getHand().size() != CARDSINHAND)
    			{
    				throw new GameException("Each player must start with 10 cards before they play the first trick of the game");
    			}
    	}
    	
    	for(aTrickCounter = 1; aTrickCounter <= MAXTRICKS; aTrickCounter++)
    	{
    		playTrick();
    	}
    	
    	// When all the tricks were played, the hand of the players must be empty since they played 1 card per trick
    	for(APlayer p: aPlayers)
    	{
    		if (aTrickCounter == MAXTRICKS+1 && p.getHand().size() != 0)
            {
                throw new GameException("At this point in the game, all the players must have an empty hand");
            }	
    	}
        
    }
    
    /**
     * Play trick such that players follow suit if they can. A trick is won by the highest trump in it, 
     * or if no trump is played by the highest card of the suit led. The winner of a trick leads to the next.
     * @throws GameException if allowed trick number is exceeded
     * @throws GameException if player played a card that is not allowed according to the rules of the game.
     * @post updates the player order such that the trick winner leads
    */
    public void playTrick()
    {
    	
    	if (aTrickCounter > MAXTRICKS)
    	{
    		throw new GameException("You cannot play more than 10 tricks per round since each hand contains 10 cards");
    	}
    	
        notifyObservers(new Notification("game.engine", this, getNotificationSequenceNumber(), State.newTrick.toString()));
       
        Trick lTrick = new Trick(aContract);
        for(APlayer p: aPlayers)
        {
        	aCurrentPlayer = p;
        	aCardPlayed = p.play(lTrick);
        	if(aCardPlayed.isJoker() && lTrick.size() == 0 && aContract.isNoTrump() && p.getHand().size() > 2)
        	{
        		throw new GameException("Can't play this Card");
        	}
        	notifyObservers(new Notification("game.engine", this, getNotificationSequenceNumber(), State.cardPlayed.toString()));
            lTrick.add(aCardPlayed);
        }
        
        // update tricks won
        int lWinnerIndex = lTrick.winnerIndex(); 
        aTrickWinner = aPlayers[lWinnerIndex];
        Team lWinningTeam = getTeamFromPlayer(aTrickWinner);
        lWinningTeam.setTricksWon(lWinningTeam.getTricksWon()+1);
        notifyObservers(new Notification("game.engine", this, getNotificationSequenceNumber(), State.trickWon.toString()));
        
        // winner now leads
        aPlayers = rotate(aPlayers, aTrickWinner);
        
    }
    
    /**
     * Compute round scores based on number of tricks won per team and update total scores.
     * End the game if the contractor team has a total score greater than 500 or less than -500.
     * Note that even if the defenders exceed a total score of 500 points thanks to odd tricks, 
     * they can't win the game in this round.
     * @throws GameException if the round score is computed before the 10 tricks are played
     * @throws GameException if winning or losing team are already set
     * @throws GameException if attempt to compute score while no contract has been set
     * @post number of tricks won per team are reset to 0
     * @post game ends if team wins or loses according to the rules of the game i.e. sets aWinningTeam, aLosingTeam
     * 
     */
    public void computeScore()
    {
        Team lContractorTeam = getTeamFromPlayer(aContractor);
        Team lNonContractorTeam = getOpponentTeam(lContractorTeam);
        
        if(lContractorTeam == null || lNonContractorTeam == null)
        {
        	throw new GameException("Cannot compute score if no contract has been set.");
        }
        
        if(aWinningTeam == null & aLosingTeam != null)
        {
        	throw new GameException("Winning or losing team are already set.");
        }
        
        // round score cannot be computed before the 10 tricks are played
        if (aTrickCounter < MAXTRICKS)
        {
        	throw new GameException("The round score must be computed after the 10 tricks were played");
        }
        
        // compute round scores
        aContractorRoundScore = lContractorTeam.getContract().getScore();
        aContractWon = lContractorTeam.getTricksWon() >= lContractorTeam.getContract().getTricksBid();        
        if(aContractWon)
        {
            // if there's a slam
            if(lContractorTeam.getTricksWon() == MAXTRICKS && aContractorRoundScore < SLAM)
            {
                aContractorRoundScore = SLAM;
            }
        } 
        else
        {
            aContractorRoundScore = -aContractorRoundScore;
        }

        aNonContractorRoundScore = lNonContractorTeam.getTricksWon() * TRICKSCORE;
        
        // compute total scores
        aContractorTotalScore = lContractorTeam.getScore() + aContractorRoundScore;
        lContractorTeam.setScore(aContractorTotalScore);        
        aNonContractorTotalScore = lNonContractorTeam.getScore() + aNonContractorRoundScore;
        lNonContractorTeam.setScore(aNonContractorTotalScore);
        notifyObservers(new Notification("game.engine", this, getNotificationSequenceNumber(), State.roundEnd.toString()));

        // reset number of tricks won
        for(Team t: getTeams())
        {
        	t.setTricksWon(0);
        }
        
        // end game if necessary
        if(aContractorTotalScore >= WINSCORE)
        {
            endGame(lContractorTeam); 
        } 
        
        if(aContractorTotalScore <= LOSESCORE)
        {
            endGame(lNonContractorTeam);
        }
    }

    private void endGame(Team pWinningTeam)
    {
        
    	aWinningTeam = pWinningTeam;
    	aLosingTeam = getOpponentTeam(aWinningTeam);
    	aGameOver = true;
        
        notifyObservers(new Notification("game.engine", this, getNotificationSequenceNumber(), State.gameOver.toString()));   
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
    	return aPlayers;
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
    public APlayer getCurrentPlayer()
    {
    	return aCurrentPlayer;
    }
    
    /**
     * Returns the trick winner.
     * @return the trick winner.
     */
    public APlayer getTrickWinner()
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
    public CardList getWidow()
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
    public Bid getContract()
    {
    	return aContract;
    }
    
    /**
     * Returns the card played in a trick context. Out of a trick, returns irrelevant information.
     * @return card played in trick context by current player
     */
    public Card getCardPlayed()
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
    protected Team getWinningTeam()
    {
    	return aWinningTeam;
    }
    
    /**
     * Returns the losers' team when the game is over.
     * @return the losers' team
     */
    protected Team getLosingTeam()
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
    
    /*----------------- Helper methods --------------------*/
    /**
     * Returns the team to which the player belongs.
     * @param pPlayer the player who belongs to the team
     * @return the team that contains the player; null if player belongs to no team
     */
    private Team getTeamFromPlayer(APlayer pPlayer)
    {
    	Team lTeam = null;
    	if(aTeam1.isInTeam(pPlayer))
    	{
    		lTeam = aTeam1;
    	}
    	if(aTeam2.isInTeam(pPlayer))
    	{
    		lTeam = aTeam2;
    	}
    	return lTeam;
    }
    
    /**
     * Helper method to rotate the circular player array such that the specified player 
     * becomes first while conserving the playing order. 
     * @param pPlayers the current playing order
     * @param pPlayer the player who will become first
     * @return the playing order such that the specified player is now at index 0; if pPlayer is not in pPlayers, return array unchanged
     */
    private APlayer[] rotate(APlayer[] pPlayers, APlayer pPlayer)
    {
    	List<APlayer> lPlayerList = Arrays.asList(aPlayers);
    	int lIndex = lPlayerList.indexOf(pPlayer);
    	if(lIndex < 0)
    	{
    		return pPlayers;
    	}
        Collections.rotate(lPlayerList, -lIndex);
        return (APlayer[]) lPlayerList.toArray();
    }
    
    /**
     * Helper method to get the team opposing pTeam.
     * @param pTeam
     * @return the team opposing pTeam; null if pTeam is null
     */
    private Team getOpponentTeam(Team pTeam)
    {
    	if(pTeam == aTeam1)
    	{
    		return aTeam2;
    	}
    	if(pTeam == aTeam2)
    	{
    		return aTeam1;
    	}
    	return null;
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
