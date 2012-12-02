package comp303.fivehundred.gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.util.Card.Suit;

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
		
		this.add(new JLabel(generateIcon(aEast)));
		aNorthSouth.removeAll();
		aNorthSouth.add(new JLabel(generateIcon(aSouth)));
		aNorthSouth.add(new JLabel(generateIcon(aNorth)));
		this.add(aNorthSouth);
		this.add(new JLabel(generateIcon(aWest)));

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
	
	private ImageIcon generateIcon(Bid pBid){
		String lSuit;
		this.removeAll();
		if(pBid.isPass()){
			
		}
		else if(pBid.isNoTrump()){
			lSuit = "NT";
		}
		else if(pBid.getSuit().equals(Suit.CLUBS)){
			lSuit = "C";
		}
		else if(pBid.getSuit().equals(Suit.SPADES)){
			lSuit = "S";
		}
		else if(pBid.getSuit().equals(Suit.DIAMONDS)){
			lSuit = "D";
		}
		else{
			lSuit = "H";
		}
		String lPath = getClass().getClassLoader().getResource(".").getPath();
//		ImageIcon lIcon = new ImageIcon(lPath + "../lib/TrumpImages/" + lSuit + pBid.getTricksBid() + ".png");
		return null;
	}
}
