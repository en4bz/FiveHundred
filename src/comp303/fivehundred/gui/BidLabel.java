package comp303.fivehundred.gui;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.util.Card.Suit;

@SuppressWarnings("serial")
public class BidLabel extends JLabel
{
	Bid aBid;
	
	public BidLabel(Bid pBid){
		aBid = pBid;
		this.redraw();
	}
	
	private void redraw(){
		String lSuit;
		this.removeAll();
		if(aBid.isNoTrump()){
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
		String lPath = getClass().getClassLoader().getResource(".").getPath();
		ImageIcon lIcon = new ImageIcon(lPath + "../lib/TrumpImages/" + lSuit + aBid.getTricksBid() + ".png");
		this.setIcon(lIcon);
	}
}
