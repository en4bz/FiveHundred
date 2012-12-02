package comp303.fivehundred.player;


import comp303.fivehundred.gui.GameFrame;
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
	/**
	 * Constructs a new human player.
	 * @param pName : Player name
	 */
	public HumanPlayer(String pName)
	{
		super(pName);
	}	

	/**
	 * {@inheritDoc}
	 */
	public Card play(Trick pTrick)
	{
		Card lPlayed;
		while(GameFrame.nextCard == null)
		{
			try
			{
				Thread.sleep(10);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		lPlayed = GameFrame.nextCard;
		GameFrame.nextCard = null;
		this.removeCardFromHand(lPlayed);
		return lPlayed;
	}

	/**
	 * {@inheritDoc}
	 */
	public Bid selectBid(Bid[] pPreviousBids)
	{
		return new Bid();
	} 

	/**
	 * {@inheritDoc}
	 */
	public CardList exchange(Bid[] pBids, int pIndex, Hand pWidow)
	{
		CardList toDiscard = new CardList();
		for(int i = 0; i < 6; i++)
		{
			while(GameFrame.nextCard == null)
			{
				try
				{
					Thread.sleep(10);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			toDiscard.add(GameFrame.nextCard);
			GameFrame.nextCard = null;
		}
		for(Card c: pWidow)
		{
			this.addCardToHand(c);
		}
		
		for(Card c: toDiscard)
		{
			this.removeCardFromHand(c);
		}
		return toDiscard;
	}
}