package comp303.fivehundred.player;

import comp303.fivehundred.ai.IBiddingStrategy;
import comp303.fivehundred.ai.ICardExchangeStrategy;
import comp303.fivehundred.ai.IPlayingStrategy;
import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;

public interface IPlayer extends IPlayingStrategy, IBiddingStrategy, ICardExchangeStrategy
{
	String getName();
	
	Hand getHand();
	void setHand(Hand pNewHand);
	
	Player getTeammate();
	void setTeammate(Player pNewTeammate);
	
}
