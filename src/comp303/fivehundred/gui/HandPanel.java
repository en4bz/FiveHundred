package comp303.fivehundred.gui;

import java.awt.BorderLayout;
import java.awt.Point;

import javax.swing.JPanel;

import comp303.fivehundred.gui.external.OverlapLayout;
import comp303.fivehundred.util.CardList;

@SuppressWarnings("serial")
public class HandPanel extends JPanel
{
	private static final int GAP = 5;
	
	private Rotation aRotation;
	private boolean aIsVisible;
	
	private CardListPanel aHand;
	private JPanel aTricksWon;
	
	public HandPanel(CardList pHand, Rotation pRotation, boolean pVisibility){
		super(new BorderLayout());
		aHand = new CardListPanel(pHand, pRotation, pVisibility);
		aRotation = pRotation;
		aIsVisible = pVisibility;
		
		if(aRotation == Rotation.LEFT)
		{
			aTricksWon = new JPanel(new OverlapLayout(new Point(0,50)));
			this.add(aHand, BorderLayout.WEST);
			this.add(aTricksWon, BorderLayout.EAST);
			((BorderLayout) this.getLayout()).setHgap(GAP);
		}
		else if(aRotation == Rotation.RIGHT)
		{
			aTricksWon = new JPanel(new OverlapLayout(new Point(0,50)));
			this.add(aHand, BorderLayout.EAST);
			this.add(aTricksWon, BorderLayout.WEST);
			((BorderLayout) this.getLayout()).setHgap(GAP);
		}
		else
		{
			aTricksWon = new JPanel(new OverlapLayout(new Point(50,0)));
			this.add(aHand, BorderLayout.NORTH);
			this.add(aTricksWon, BorderLayout.SOUTH);
			((BorderLayout) this.getLayout()).setVgap(GAP);
		}
		aTricksWon.setSize(aTricksWon.getWidth()/2, aTricksWon.getHeight()/2);
	}
	
	public void addTrick(){
		CardLabel lTemp = new CardLabel(null, aRotation, false);
		lTemp.getIcon();
		this.aTricksWon.add(lTemp);
	}
}
