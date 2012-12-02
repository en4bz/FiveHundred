package comp303.fivehundred.gui;

import java.awt.BorderLayout;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import comp303.fivehundred.gui.external.OverlapLayout;
import comp303.fivehundred.gui.external.RotatedIcon;
import comp303.fivehundred.player.APlayer;

/**
 * A JPanel that represents a player's hand and the trick they've won.
 * @author Ian Forbes
 */
@SuppressWarnings("serial")
public class PlayerArea extends JPanel
{
	private static final int GAP = 5;
	private static final int TRICK_OVERLAP = 25;
	private final Rotation aRotation;
	private final APlayer aPlayer;
	private final CardListPanel aHand;
	private JPanel aTricksWon;
	
	public PlayerArea(final APlayer pPlayer, final Rotation pRotation, boolean pVisibility){
		super(new BorderLayout());
		aPlayer = pPlayer;
		aHand = new CardListPanel(aPlayer.getHandRef(), pRotation, pVisibility);
		aRotation = pRotation;
		
		if(aRotation == Rotation.LEFT)
		{
			aTricksWon = new JPanel(new OverlapLayout(new Point(0,TRICK_OVERLAP)));
			this.hack();
			this.add(aHand, BorderLayout.WEST);
			this.add(aTricksWon, BorderLayout.EAST);
			((BorderLayout) this.getLayout()).setHgap(GAP);
		}
		else if(aRotation == Rotation.RIGHT)
		{
			aTricksWon = new JPanel(new OverlapLayout(new Point(0,TRICK_OVERLAP)));
			this.hack();
			this.add(aHand, BorderLayout.EAST);
			this.add(aTricksWon, BorderLayout.WEST);
			((BorderLayout) this.getLayout()).setHgap(GAP);
		}
		else if(aRotation == Rotation.UPSIDE_DOWN)
		{
			aTricksWon = new JPanel(new OverlapLayout(new Point(TRICK_OVERLAP,0)));
			this.hack();
			this.add(aHand, BorderLayout.SOUTH);
			this.add(aTricksWon, BorderLayout.NORTH);
			((BorderLayout) this.getLayout()).setVgap(GAP);
		}
		else
		{
			aTricksWon = new JPanel(new OverlapLayout(new Point(TRICK_OVERLAP,0)));
			this.hack();
			this.add(aHand, BorderLayout.NORTH);
			this.add(aTricksWon, BorderLayout.SOUTH);
			((BorderLayout) this.getLayout()).setVgap(GAP);
		}
		aTricksWon.setOpaque(false);
		this.setOpaque(false);
	}
	
	/**
	 * Adds a trick to the trick panel.
	 */
	
	public APlayer getPlayer(){
		return aPlayer;
	}
	
	public void addTrick(){
		String lPath = getClass().getClassLoader().getResource(".").getPath();
		RotatedIcon lIcon = new RotatedIcon(new ImageIcon(lPath + "../src/images/tinyBack.gif"), aRotation);
		aTricksWon.add(new JLabel(lIcon));
		this.validate();
		this.repaint();
	}
	
	public void setVisibility(boolean pVisibility)
	{
		aHand.setVisibility(pVisibility);
	}

	public void updatehand()
	{
		aHand.redraw();
	}

	public void clearTricks()
	{
		aTricksWon.removeAll();
		this.hack();
		aTricksWon.validate();
		aTricksWon.repaint();
	}
	
	private void hack(){
		//TODO: Try not to use this
		String lPath = getClass().getClassLoader().getResource(".").getPath();
		RotatedIcon lIcon = new RotatedIcon(new ImageIcon(lPath + "../src/images/tinyBack.gif"), aRotation);
		JLabel lTemp = new JLabel(lIcon);
		lTemp.setVisible(false);
		aTricksWon.add(lTemp);
	}
}
