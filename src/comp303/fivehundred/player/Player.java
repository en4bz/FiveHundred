package comp303.fivehundred.player;

import comp303.fivehundred.ai.IBiddingStrategy;
import comp303.fivehundred.ai.ICardExchangeStrategy;
import comp303.fivehundred.ai.IPlayingStrategy;
import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

public  class Player implements IPlayingStrategy, IBiddingStrategy, ICardExchangeStrategy
{
	private final String aName;
	private Hand aHand;
	private Player aTeammate;//Possibly final so you can't change teams mid game
	
	public Player(String pName)
	{
		this.aName = pName;
	}
	public String getName(){return aName;}
	
	public Player getTeammate(){return this.aTeammate;}
	public void setTeammate(Player pNewTeammate){this.aTeammate = pNewTeammate;}

	public Hand getHand(){return this.aHand;}
	public void setHand(Hand pNewHand){this.aHand = pNewHand;}
	
	@Override
	public CardList selectCardsToDiscard(Bid[] pBids, int pIndex, Hand pHand)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Bid selectBid(Bid[] pPreviousBids, Hand pHand)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Card play(Trick pTrick, Hand pHand)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
}
