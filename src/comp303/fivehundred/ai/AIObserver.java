package comp303.fivehundred.ai;

import javax.management.Notification;

import comp303.fivehundred.engine.GameEngine;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.mvc.Observer;
import comp303.fivehundred.player.APlayer;
import comp303.fivehundred.player.ARobotPlayer;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

public abstract class AIObserver extends ARobotPlayer implements Observer
{

	public AIObserver(String pName)
	{
		super(pName);
	}

	Hand aDiscardedWidow = new Hand();
	Hand aDiscardedCards = new Hand();
	CardList aTrickCards = new CardList();
	APlayer aContractor = null;
	
	public Hand getDiscardedCards()
	{
		return aDiscardedCards;
	}
	
	public Hand getDiscardedWidow()
	{
		return aDiscardedWidow;
		
	}
	
	public boolean isContractor()
	{
		return aContractor == this; // should be same reference
	}
	
	@Override
	public void update(Notification pNotification)
	{
		if(pNotification.getType().equals("game.engine"))
		{
			GameEngine lGame = (GameEngine) pNotification.getSource();
			GameEngine.State lState = GameEngine.State.valueOf(pNotification.getMessage());
			switch (lState) 
			{
				case newGame:
					aDiscardedWidow = new Hand();
					aDiscardedCards = new Hand();
					aTrickCards = new CardList();
					aContractor = null;
					break;
			   case cardPlayed:
				   aTrickCards.add(lGame.getCardPlayed());
				   break;
			   case newTrick:
				   // add previous trick's cards to discarded cards
				   for(Card c: aTrickCards)
				   {
					   aDiscardedCards.add(c);
				   }
				   // reset trickCards
				   aTrickCards = new CardList();
				   break;
			   case newContract:
				   aContractor = lGame.getContractor();
				   break;
			   case cardsDiscarded:
				   aDiscardedWidow = new Hand();
				   for(Card c: lGame.getWidow())
				   {
					   aDiscardedWidow.add(c);
				   }
				   break;
			default:
				break;
			}
		}
		
	}

}
