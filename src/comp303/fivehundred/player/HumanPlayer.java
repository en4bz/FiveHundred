package comp303.fivehundred.player;


import comp303.fivehundred.engine.GameException;
import comp303.fivehundred.gui.ConsoleInterface;
import comp303.fivehundred.gui.UserInterface;
import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

/**
 * Collects methods necessary for human player, must use a UserInterface object.
 * Experimental for now.
 * @author Eleyine
 *
 */
public class HumanPlayer extends APlayer
{
		
	UserInterface aInterface;
	
	/**
	 * Constructs a new human player.
	 * @param pName
	 */
	public HumanPlayer(String pName)
	{
		super(pName);
		// @TODO prompt user for name
		// CHANGE THIS FOR DIFFERENT USER INTERFACE 
		aInterface = new ConsoleInterface();
	}	

	/**
	 * {@inheritDoc}
	 */
	public Card play(Trick pTrick)
	{
		Card playedCard = aInterface.play(pTrick);
		Hand lHand = getHand();
		lHand.remove(playedCard);
		setHand(lHand);
		return playedCard;
	}

	/**
	 * {@inheritDoc}
	 */
	public Bid selectBid(Bid[] pPreviousBids)
	{
		Bid b = aInterface.selectBid(pPreviousBids);
		return b;
	}

	/**
	 * {@inheritDoc}
	 */
	public CardList exchange(Bid[] pBids, int pIndex, Hand pWidow)
	{
		Hand lMerged = getHand();
		for(Card c: pWidow)
		{
			lMerged.add(c);
		}
		CardList lDiscarded = aInterface.exchange(pBids, pIndex, pWidow);
		for(Card c: lDiscarded)
		{
			lMerged.remove(c);
		}
		setHand(lMerged);
		return lDiscarded;
	}

}
