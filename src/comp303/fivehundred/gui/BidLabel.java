package comp303.fivehundred.gui;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.util.Card.Suit;

@SuppressWarnings("serial")
public class BidLabel extends JLabel
{
	private static final int SIZE = 100;
	
	public BidLabel(Bid pBid){
		String lSuit;
		String lPath = getClass().getClassLoader().getResource(".").getPath();
		if(pBid.isPass()){
			ImageIcon lIcon = new ImageIcon(lPath + "../lib/TrumpImages/10NT.png");
			this.setIcon(new ImageIcon( lIcon.getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH)));
			return;
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
		ImageIcon lIcon = new ImageIcon(lPath + "../lib/TrumpImages/" + lSuit + pBid.getTricksBid() + ".png");
		this.setIcon(new ImageIcon( lIcon.getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH)));
	}
}
