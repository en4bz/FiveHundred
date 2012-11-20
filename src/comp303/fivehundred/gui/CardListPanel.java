package comp303.fivehundred.gui;

import java.awt.Point;

import javax.swing.JPanel;

import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

@SuppressWarnings("serial")
public class CardListPanel extends JPanel
{
	private CardList aCards;
	private Rotation aRotation;
	private boolean aIsVisible;
	
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
			this.setLayout(new OverlapLayout(new Point(0,30)));
			aCards = aCards.reverse();
		}
		else
		{
			this.setLayout(new OverlapLayout(new Point(30,0)));
		}
		for(Card c : aCards)
		{
			this.add(new CardLabel(c,aRotation, aIsVisible));
		}
	}

	
//	@Override
//	public void paintComponent(Graphics pGraphic)
//	{
//	    Graphics2D g = (Graphics2D) pGraphic;
//	 //   g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//	 //   AffineTransform matrix = g.getTransform(); // Backup
//	    g.rotate(aRotation, this.getWidth() / 2, this.getHeight() / 2);
//
//	    this.paint(g);
//	    
//	    /* End */
//	  //  g.setTransform(matrix); // Restore
//
//	}
	
}
