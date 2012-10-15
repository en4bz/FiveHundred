package comp303.fivehundred.player;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

public class HumanPlayer extends Player
{

	public HumanPlayer(String pName)
	{
		super(pName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Card play(Trick pTrick, Hand pHand)
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
	public CardList selectCardsToDiscard(Bid[] pBids, int pIndex, Hand pHand)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
