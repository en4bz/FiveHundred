package comp303.fivehundred.ai;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.player.ARobotPlayer;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

public class AdvancedRobot extends AIObserver
{
	private IBiddingStrategy aBiddingStrategy = new AdvancedBiddingStrategy();
	private ICardExchangeStrategy aCardExchangeStrategy = new AdvancedCardExchangeStrategy();
	private IPlayingStrategy aPlayingStrategy = new AdvancedPlayingStrategy(this);
	
	public AdvancedRobot(String pName)
	{
		super(pName);
	}
	
	@Override
	public Bid selectBid(Bid[] pPreviousBids, Hand pHand)
	{
		return aBiddingStrategy.selectBid(pPreviousBids, pHand);
	}

	@Override
	public CardList selectCardsToDiscard(Bid[] pBids, int pIndex, Hand pHand)
	{
		return aCardExchangeStrategy.selectCardsToDiscard(pBids, pIndex, pHand);
	}

	@Override
	public Card play(Trick pTrick, Hand pHand)
	{
		return aPlayingStrategy.play(pTrick, pHand);
	}
}
