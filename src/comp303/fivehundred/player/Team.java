package comp303.fivehundred.player;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;


public class Team
{
    private APlayer aPlayer1;
    private APlayer aPlayer2;
    private Bid aContract;
    private int aTricksWon;
    private int aScore;
 
    public Team(APlayer pPlayer1, APlayer pPlayer2)
    {
        assert !pPlayer1.equals(pPlayer2);
        aPlayer1 = pPlayer1;
        aPlayer2 = pPlayer2;
        reset();
    }
    
    public int getTricksWon()
    {
        return aTricksWon;
    }
    
    public void reset()
    {
        aTricksWon = 0;
        aContract = new Bid();
        aScore = 0;
        aPlayer1.setHand(new Hand());
        aPlayer2.setHand(new Hand());
    }
    
    public boolean isInTeam(APlayer pPlayer)
    {
        return (pPlayer.equals(aPlayer1) || pPlayer.equals(aPlayer2));
    }
    
    public APlayer[] getPlayers()
    {
    	APlayer[] lPlayers = {aPlayer1, aPlayer2};
        return lPlayers;
    }
    
    public int getScore()
    {
        return aScore;
    }
    
    public void setScore(int pScore)
    {
        aScore = pScore;
    }
    
    public boolean isContractor()
    {
        return !aContract.isPass();
    }
    
    public void setContract(Bid pContract)
    {
        aContract = pContract;
    }

    // TODO return contract even if null?
    public Bid getContract()
    {
        return aContract;
    }

	public void setTricksWon(int pNumTricksWon)
	{
		aTricksWon = pNumTricksWon;
	}

}
