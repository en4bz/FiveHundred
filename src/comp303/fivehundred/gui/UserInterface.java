package comp303.fivehundred.gui;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

/**
 * Experimental draft, outside of milestone 2.
 *
 */
public interface UserInterface
{

	Card play(Trick pTrick);
	CardList exchange(Bid[] pBids, int pIndex, Hand pWidow);
	Bid selectBid(Bid[] pPreviousBids);

}
