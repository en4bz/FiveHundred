package comp303.fivehundred.gui;

import java.awt.Component;
import java.awt.Point;

import javax.management.Notification;
import javax.swing.JPanel;

import comp303.fivehundred.gui.external.OverlapLayout;
import comp303.fivehundred.mvc.Observer;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

/**
 * A JPanel that represents a list of overlapped cards.
 * @author Ian Forbes
 */

@SuppressWarnings("serial")
public class CardListPanel extends JPanel implements Observer
{
	private final CardList aCards;
	private Rotation aRotation;
	private boolean aIsVisible;
	
	/**
	 * Creates a new CardList Panel
	 * @param pCardList : CardList to display
	 * @param pRotation : Rotation of the list on the display
	 * @param pVisibility : Whether the card is facing up
	 */
	public CardListPanel(CardList pCardList, Rotation pRotation, boolean pVisibility)
	{
		super();
		aCards = pCardList;
		aRotation = pRotation;
		aIsVisible = pVisibility;
		if(aRotation == Rotation.RIGHT)
		{
			this.setLayout(new OverlapLayout(new Point(0,30)));
		}
		else if (aRotation == Rotation.LEFT)
		{
			this.setLayout(new OverlapLayout(new Point(0,30),false));
		}
		else
		{
			this.setLayout(new OverlapLayout(new Point(30,0)));
		}
		this.reDraw();
	}
	
	private void reDraw()
	{
		for(Card c : aCards)
		{
			this.add(new CardLabel(c,aRotation, aIsVisible));
		}
	}
	
	public void setVisibility(boolean pVisibility)
	{
		for(Component c : this.getComponents())
		{
			((CardLabel) c).setVisibility(pVisibility);
		}
	}

	/**
	 * In the case of an update redraw the card list.
	 */
	@Override
	public void update(Notification pNotification)
	{
		this.removeAll();
		this.reDraw();
	}
}
