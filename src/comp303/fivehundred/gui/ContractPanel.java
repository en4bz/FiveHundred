package comp303.fivehundred.gui;

import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.util.Card.Suit;

@SuppressWarnings("serial")
public class ContractPanel extends JPanel
{
	private Bid aBid;
	
	ContractPanel(Bid pBid){
		super(new FlowLayout());
		this.aBid = pBid;
		this.reDraw();
	}
	
	private void reDraw(){
		String lSuit;
		if(aBid.getSuit() == null){
			lSuit = "NT";
		}
		else if(aBid.getSuit().equals(Suit.CLUBS)){
			lSuit = "C";
		}
		else if(aBid.getSuit().equals(Suit.SPADES)){
			lSuit = "S";
		}
		else if(aBid.getSuit().equals(Suit.DIAMONDS)){
			lSuit = "D";
		}
		else{
			lSuit = "H";
		}
		this.removeAll();
		String lPath = getClass().getClassLoader().getResource(".").getPath();
		ImageIcon lIcon = new ImageIcon(lPath + "../lib/TrumpImages/" + lSuit + aBid.getTricksBid() + ".png");
		this.add(new JLabel(lIcon));
	}
	
	public void setBid(Bid pBid){
		aBid = pBid;
		this.reDraw();
	}
}
