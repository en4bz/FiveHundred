package comp303.fivehundred.ai;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.player.ARobotPlayer;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

/**
 * Plays correctly but basically
 */

public class BasicRobot extends ARobotPlayer
{
	private IBiddingStrategy aBiddingStrategy = new BasicBiddingStrategy();
	private ICardExchangeStrategy aCardExchangeStrategy = new BasicCardExchangeStrategy();
	private IPlayingStrategy aPlayingStrategy = new BasicPlayingStrategy();
	
	// TODO ask TA if it is okay to add this constructor
	// Fall gracefully back if no parameter specified
	
	/**
	 * Creates a BasicRobot with default name "BasicRObot".
	 */
	public BasicRobot()
	{
		super("BasicRobot");
	}
	
	/**
	 * Creates a BasicRobot with name pName.
	 * @param pName : Robot's name
	 */
	public BasicRobot(String pName)
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
