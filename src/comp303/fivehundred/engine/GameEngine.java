package comp303.fivehundred.engine;

import java.util.Arrays;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.player.IPlayer;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;
import comp303.fivehundred.util.Deck;

public class GameEngine
{
	private boolean aIsOver;
	private final IPlayer[] aPlayers;
	private int[] aTricksWon;
	private int[] aScores;
	private Bid[] aBids;
	private CardList aWidow;
	private Bid aContract;
	private IPlayer aContractor;
	private int aStartIndex;
	private IPlayer[] aWinners;
	
	/**
	 * 
	 * @param pPlayer1 : Player 1 on team one
	 * @param pPlayer2 : Player 1 on team two
	 * @param pPlayer3 : Player 2 on team one
	 * @param pPlayer4 : Player 2 on team two
	 */
	public GameEngine(IPlayer pPlayer1, IPlayer pPlayer2, IPlayer pPlayer3, IPlayer pPlayer4)
	{
		aBids = new Bid[4];
		aTricksWon = new int[2];
		aScores = new int[2];
		
		aPlayers = new IPlayer[]{pPlayer1,pPlayer2, pPlayer3, pPlayer4};
	}
	
	public void newGame()
	{
		this.aIsOver = false;
		for(IPlayer p : aPlayers)
		{
			p.setHand(new Hand());
		}
	}
	
	public boolean gameOver()
	{
		return this.aIsOver;
	}
	
	public void dealCards()
	{
		Deck lTempDeck = new Deck();
		lTempDeck.shuffle();
		for(IPlayer p : aPlayers)
		{
			p.setHand(new Hand()); //Clear old hand in case of left over cards
			for(int j = 0; j < 10; j++)
			{
				p.getHand().add(lTempDeck.draw());
			}
		}
		aWidow = new CardList(); //Reset
		for(int i = 0; i < 6; i++)
		{
			aWidow.add(lTempDeck.draw());
		}
		//Re-Initialize
		aBids = new Bid[4];
		aTricksWon = new int[2];
		aContract = null;
		aContractor = null;
	}
	
	public void bid()
	{
		boolean allPasses;
		do{
			allPasses = true;
			for(int i = 0; i < aPlayers.length; i++)
			{
				aBids[i] = aPlayers[i].selectBid(aBids, aPlayers[i].getHand());
				if(aBids[i] == null)
				{
					continue;
				}
				allPasses = false;//Someone has made a non-passing bid
				if(aContract == null || aBids[i].compareTo(aContract) > 0)
				{
					aContract = aBids[i];
					aContractor = aPlayers[i];//Highest Bidder
					aStartIndex = i;//Set StartingIndex to the contractor for first round
				}
			}
		}while(!allPasses && !aContract.equals(new Bid(10, Suit.HEARTS)));//Max Bid Reached	
	}
	
	public void exchange()
	{
		for(Card c : aWidow){
			aContractor.getHand().add(c);
		}
		CardList lToDiscard = aContractor.selectCardsToDiscard(aBids, 0, aContractor.getHand());//What does index do?
		for(Card c : lToDiscard)
		{
			aContractor.getHand().remove(c);
		}
	}
	
	public void playTrick()
	{
		Trick lCurrentTrick = new Trick(aContract);
		for(IPlayer p : aPlayers)
		{
			p.play(lCurrentTrick, p.getHand());
		}
		aStartIndex = lCurrentTrick.winnerIndex() + aStartIndex % 4;
		aTricksWon[aStartIndex % 2] += 1;
	}
	
	public void computeScore()
	{
		//Contractor Index
		int lContractorIndex = Arrays.asList(aPlayers).indexOf(aContractor) % 2; //Mod 2 to get right team
		if(aTricksWon[lContractorIndex] >= aContract.getTricksBid())
		{	
			if(aTricksWon[lContractorIndex] == 10 && aContract.getScore() < 250)
			{
				aScores[lContractorIndex] += 250; //Slam Value
			}
			else
			{
				aScores[lContractorIndex] += aContract.getScore();
			}
		}
		else{
			aScores[lContractorIndex] -= aContract.getScore();
		}
		aScores[(lContractorIndex + 1) % 2] += 10 * aTricksWon[(lContractorIndex +1) % 2];
		for(int i : aScores)
		{
			if(i >= 500)
			{
				aWinners = new IPlayer[]{aPlayers[i], aPlayers[i+2]};
				aIsOver = true;
			}
			else if( i <= -500)
			{
				aWinners = new IPlayer[]{aPlayers[i+1], aPlayers[(i+3) % 2]};
				aIsOver = true;
			}
		}
	}
	
	public IPlayer[] declareWinner()
	{
		return aWinners; //Do something with aWinners
	}
}
