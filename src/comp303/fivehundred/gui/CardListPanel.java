package comp303.fivehundred.gui;

import java.awt.Component;
import java.awt.Point;

import javax.swing.JPanel;

import comp303.fivehundred.gui.external.OverlapLayout;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

/**
 * A JPanel that represents a list of overlapped cards.
 * @author Ian Forbes
 */

@SuppressWarnings("serial")
public class CardListPanel extends JPanel
{
	private static final int HORIZONTAL_OVERLAP = 30;
	private static final int VERTICAL_OVERLAP = 20;
	
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
			this.setLayout(new OverlapLayout(new Point(0,VERTICAL_OVERLAP)));
		}
		else if (aRotation == Rotation.LEFT)
		{
			this.setLayout(new OverlapLayout(new Point(0,VERTICAL_OVERLAP),false));
		}
		else
		{
			this.setLayout(new OverlapLayout(new Point(HORIZONTAL_OVERLAP,0)));
		}
		this.redraw();
	}
	
	protected void redraw()
	{
		this.removeAll();
		for(Card c : aCards)
		{
			this.add(new CardLabel(c,aRotation, aIsVisible));
		}
		this.validate();
		this.repaint();
	}
	
	public void setVisibility(boolean pVisibility)
	{
		for(Component c : this.getComponents())
		{
			((CardLabel) c).setVisibility(pVisibility);
		}
	}
}
