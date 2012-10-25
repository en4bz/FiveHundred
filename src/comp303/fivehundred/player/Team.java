package comp303.fivehundred.player;

import java.util.ArrayList;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;

public class Team
{
    private Player aPlayer1;
    private Player aPlayer2;
    private Bid aContract;
    private int aTricksWon;
    private int aScore;
 
    public Team(Player pPlayer1, Player pPlayer2)
    {
        assert !pPlayer1.equals(pPlayer2);
        reset();
        aPlayer1 = pPlayer1;
        aPlayer2 = pPlayer2;
    }
    
    public int getTricksWon()
    {
        return aTricksWon;
    }
    
    public void reset()
    {
        aTricksWon = 0;
        aContract = null;
        aScore = 0;
        aPlayer1.setHand(new Hand());
        aPlayer2.setHand(new Hand());
    }
    
    public boolean isInTeam(Player pPlayer)
    {
        return (pPlayer.equals(aPlayer1) || pPlayer.equals(aPlayer2));
    }
    
    public ArrayList<Player> getPlayers()
    {
        ArrayList<Player> lPlayers = new ArrayList<Player>();
        lPlayers.add(aPlayer1);
        lPlayers.add(aPlayer2);
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
        return (aContract != null);
    }
    
    public void setContract(Bid pContract)
    {
        aContract = pContract;
    }
}
