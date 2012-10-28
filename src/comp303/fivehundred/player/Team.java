package comp303.fivehundred.player;

import comp303.fivehundred.model.Bid;

/**
 * A collection of methods to keep track of a team's state in a game of five hundred.
 * @author Eleyine Zarour
 *
 */
public class Team
{
    private APlayer aPlayer1;
    private APlayer aPlayer2;
    private Bid aContract;
    private int aTricksWon;
    private int aScore;
    
    /**
     * Constructs a new team composed of two players.
     * @param pPlayer1 a player in the team
     * @param pPlayer2 the other player in the team
     */
    public Team(APlayer pPlayer1, APlayer pPlayer2)
    {
        assert !pPlayer1.equals(pPlayer2);
        aPlayer1 = pPlayer1;
        aPlayer2 = pPlayer2;
        reset();
    }
    

    
    /**
     * Reset all this team's state before playing a game.
     */
    public void reset()
    {
        aTricksWon = 0;
        aContract = new Bid();
        aScore = 0;
        aTricksWon = 0;
        aPlayer1.resetHand();
        aPlayer2.resetHand();
    }
    
    /**
     * Returns true if the player belongs to this team. 
     * @param pPlayer the player who may or may not belong to the team
     * @return true if this team contains pPlayer
     */
    public boolean isInTeam(APlayer pPlayer)
    {
        return pPlayer.equals(aPlayer1) || pPlayer.equals(aPlayer2);
    }
    
    /**
     * Returns an array of the players composing this team.
     * @return the array of players in this team
     */
    public APlayer[] getPlayers()
    {
    	APlayer[] lPlayers = {aPlayer1, aPlayer2};
        return lPlayers;
    }
    
    /**
     * Returns this team's game total score.
     * @return this team's game score
     */
    public int getScore()
    {
        return aScore;
    }
    
    /**
     * Sets this team's game total score.
     * @param pScore the number to set the team's score to
     */
    public void setScore(int pScore)
    {
        aScore = pScore;
    }
    
    /**
     * Returns true if this team is the contractor.
     * @return true if the team is currently the contractor
     */
    public boolean isContractor()
    {
        return !aContract.isPass();
    }
    
    /**
     * Sets this team's contract. If the contract is a passing bid, it
     * means the team was not assigned the contract for this round.
     * @param pContract the team's new contract (pass if no contract)
     */
    public void setContract(Bid pContract)
    {
        aContract = pContract;
    }

    /**
     * Returns this team's contract. If the contract is a passing bid,
     * it means the team was not assigned the contract for this round.
     * @return the team's contract (pass if no contract)
     */
    public Bid getContract()
    {
        return aContract;
    }

    /**
     * Get the number of tricks won by this team in a round (note: not a game).
     * @return the number of tricks won
     */
    public int getTricksWon()
    {
        return aTricksWon;
    }
    
    /**
     * Set the number of tricks won by this team in a round (note: not a game).
     * @param pNumTricksWon the new number of tricks won
     */
	public void setTricksWon(int pNumTricksWon)
	{
		aTricksWon = pNumTricksWon;
	}

}
