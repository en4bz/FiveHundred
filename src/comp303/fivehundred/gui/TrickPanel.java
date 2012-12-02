package comp303.fivehundred.gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

import comp303.fivehundred.util.Card;

/**
 * Panel that displays a trick
 * @author Ian Forbes
 *
 */
@SuppressWarnings("serial")
public class TrickPanel extends JPanel
{
	private Card aNorth;
	private Card aSouth;
	private Card aEast;
	private Card aWest;
	
	JPanel aNorthSouth;
	
	public TrickPanel(){
		super(new FlowLayout());
		aNorthSouth = new JPanel(new GridLayout(2,1,0,5));
		aNorthSouth.setOpaque(false);
		this.setOpaque(false);
		this.redraw();
	}
	
	private void redraw(){
		this.removeAll();
		this.add(new CardLabel(aWest,Rotation.DEFAULT, true));
		aNorthSouth.removeAll();
		aNorthSouth.add(new CardLabel(aNorth,Rotation.DEFAULT, true));
		aNorthSouth.add(new CardLabel(aSouth,Rotation.DEFAULT, true));
		this.add(aNorthSouth);
		this.add(new CardLabel(aEast,Rotation.DEFAULT, true));

		this.validate();
		this.repaint();
	}
	
	public void setNorth(Card pCard){
		aNorth = pCard;
		this.redraw();
	}
	
	public void setSouth(Card pCard){
		aSouth = pCard;
		this.redraw();
	}
	
	public void setEast(Card pCard){
		aEast = pCard;
		this.redraw();
	}
	
	public void setWest(Card pCard){
		aWest = pCard;
		this.redraw();
	}
	
	public void reset(){
		aNorth = null;
		aSouth = null;
		aEast = null;
		aWest = null;
		this.redraw();
	}
}

