package comp303.fivehundred.player;

import java.util.Arrays;

import javax.management.Notification;

import comp303.fivehundred.gui.GameFrame;
import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;
import comp303.fivehundred.util.Card.Joker;

/**
 * Collects methods necessary for human player, must use a UserInterface object. Experimental for now.
 * 
 * @author Eleyine
 * 
 */
public class HumanPlayer extends APlayer
{
	GameFrame aFrame;
	CardList aPlayable = null;
	CardList aDiscardable = null;
	Trick aTrick = null;
	int aIndex = 0;
	Bid[] aPreviousBids = null;

	/**
	 * Constructs a new human player.
	 * 
	 * @param pName
	 *            : Player name
	 * @param pFrame
	 *            : the GUI controller
	 */
	public HumanPlayer(String pName, GameFrame pFrame)
	{
		super(pName);
		aFrame = pFrame;
		aPlayable = getHand();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Card play(Trick pTrick)
	{
		Card lPlayedCard;
		aTrick = pTrick;

		// Player is leading
		if (pTrick.size() == 0)
		{
			aPlayable = getHand();

			// Players cannot lead a joker when playing in no trump, except if they have no other cards left.
			if (pTrick.getTrumpSuit() == null)
			{
				boolean onlyJokersLeft = true;
				for (Card c : aPlayable)
				{
					if (!c.isJoker())
					{
						onlyJokersLeft = false;
						break;
					}
				}
				if (!onlyJokersLeft)
				{
					aPlayable.remove(new Card(Joker.LOW));
					aPlayable.remove(new Card(Joker.HIGH));
				}
			}
		}
		else if (pTrick.getFirst().isJoker())
		{
			aPlayable = getHand();
		}
		else
		{
			aPlayable = getHand().playableCards(pTrick.getSuitLed(), pTrick.getTrumpSuit());
		}

		while (aFrame.getPlayedCard() == null)
		{
			try
			{
				aFrame.update(new Notification("gui.humanplayer", this, aFrame.getNotificationSequenceNumber(),
						GameFrame.Human.playPrompt.toString()));
				synchronized (this)
				{
					this.wait();
				}
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		lPlayedCard = aFrame.getPlayedCard();
		removeCardFromHand(lPlayedCard);
		aFrame.update(new Notification("gui.humanplayer", this, aFrame.getNotificationSequenceNumber(),
				GameFrame.Human.playDone.toString()));

		return lPlayedCard;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Bid selectBid(Bid[] pPreviousBids)
	{
		assert pPreviousBids.length <= 4;
		Bid lSelectedBid;
		aPreviousBids = pPreviousBids;

		while (aFrame.getSelectedBid() == null)
		{
			try
			{
				aFrame.update(new Notification("gui.humanplayer", this, aFrame.getNotificationSequenceNumber(),
						GameFrame.Human.bidPrompt.toString()));
				synchronized (this)
				{
					this.wait();
				}
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		lSelectedBid = aFrame.getSelectedBid();
		aFrame.update(new Notification("gui.humanplayer", this, aFrame.getNotificationSequenceNumber(),
				GameFrame.Human.bidDone.toString()));
		return lSelectedBid;
	}

	@Override
	public CardList exchange(Bid[] pBids, int pIndex, Hand pWidow)
	{
		aPreviousBids = pBids;
		aIndex = pIndex;

		CardList toDiscard = null;
		aDiscardable = new CardList();

		for (Card c : pWidow)
		{
			aDiscardable.add(c);
		}
		for (Card c : pWidow)
		{
			addCardToHand(c);
		}
		aDiscardable = getHand().clone();

		while (!aFrame.isDiscardDone())
		{
			try
			{
				aFrame.update(new Notification("gui.humanplayer", this, aFrame.getNotificationSequenceNumber(),
						GameFrame.Human.discardPrompt.toString()));
				synchronized (this)
				{
					this.wait();
				}
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		toDiscard = aFrame.getDiscardedCards();

		for (Card c : toDiscard)
		{
			this.removeCardFromHand(c);
		}
		aFrame.update(new Notification("gui.humanplayer", this, aFrame.getNotificationSequenceNumber(),
				GameFrame.Human.discardDone.toString()));

		return toDiscard;
	}

	/*------------------------------------- Getters --------------------------------------------*/

	public CardList getPlayableCards()
	{
		return aPlayable.clone();
	}

	public Trick getTrick()
	{
		return aTrick;
	}

	public Bid[] getPreviousBids()
	{
		return Arrays.copyOf(aPreviousBids, aPreviousBids.length);
	}

	public int getIndex()
	{
		return aIndex;
	}

	public CardList getDiscardable()
	{
		return aDiscardable;
	}

	/**
	 * {@inheritDoc}
	 */

}