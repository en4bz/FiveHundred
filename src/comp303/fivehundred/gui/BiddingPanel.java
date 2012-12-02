package comp303.fivehundred.gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

import comp303.fivehundred.model.Bid;

/**
 * Graphical representation of the bidding play. Similar to trick play but with contract labels rather than cards.
 * @author Ian Forbes
 *
 */
@SuppressWarnings("serial")
public class BiddingPanel extends JPanel
{
	private Bid aNorth;
	private Bid aSouth;
	private Bid aEast;
	private Bid aWest;
	
	JPanel aNorthSouth;
	
	public BiddingPanel(){
		super(new FlowLayout());
		aNorthSouth = new JPanel(new GridLayout(2,1,0,5));
		aNorthSouth.setOpaque(false);
		this.setOpaque(false);

		this.redraw();
	}
	
	private void redraw(){
		this.removeAll();
		this.add(new BidLabel(aWest));
		aNorthSouth.removeAll();
		aNorthSouth.add(new BidLabel(aNorth));
		aNorthSouth.add(new BidLabel(aSouth));
		this.add(aNorthSouth);
		this.add(new BidLabel(aEast));
		this.validate();
		this.repaint();
	}
	
	public void setNorth(Bid pBid){
		aNorth = pBid;
		this.redraw();
	}
	
	public void setSouth(Bid pBid){
		aSouth = pBid;
		this.redraw();
	}
	
	public void setEast(Bid pBid){
		aEast = pBid;
		this.redraw();
	}
	
	public void setWest(Bid pBid){
		aWest = pBid;
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
