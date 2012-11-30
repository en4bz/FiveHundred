package comp303.fivehundred.gui;

import java.awt.BorderLayout;
import java.awt.Point;

import javax.swing.JPanel;

import comp303.fivehundred.gui.external.OverlapLayout;
import comp303.fivehundred.player.APlayer;
import comp303.fivehundred.util.CardList;

/**
 * A JPanel that represents a player's hand and the trick they've won.
 * @author Ian Forbes
 */
@SuppressWarnings("serial")
public class PlayerArea extends JPanel
{
	private static final int GAP = 5;
	
	private Rotation aRotation;
	private boolean aIsVisible;
	
	private final APlayer aPlayer;
	private CardListPanel aHand;
	private JPanel aTricksWon;
	
	public PlayerArea(final APlayer pPlayer, Rotation pRotation, boolean pVisibility){
		super(new BorderLayout());
		aPlayer = pPlayer;
		aHand = new CardListPanel(aPlayer.getHand(), pRotation, pVisibility);
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
		else if(aRotation == Rotation.UPSIDE_DOWN)
		{
			aTricksWon = new JPanel(new OverlapLayout(new Point(50,0)));
			this.add(aHand, BorderLayout.SOUTH);
			this.add(aTricksWon, BorderLayout.NORTH);
			((BorderLayout) this.getLayout()).setVgap(GAP);
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
	
	/**
	 * Adds a trick to the trick panel.
	 */
	public void addTrick(){
		CardLabel lTemp = new CardLabel(null, aRotation, false);
		lTemp.getIcon();
		this.aTricksWon.add(lTemp);
	}
	
	public void setVisibility(boolean pVisibility)
	{
		aHand.setVisibility(pVisibility);
	}
}
