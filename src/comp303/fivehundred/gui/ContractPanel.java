package comp303.fivehundred.gui;

import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.util.Card.Suit;

@SuppressWarnings("serial")
public class ContractPanel extends JPanel
{
	private static final int SIZE = 150;
	private Bid aBid;
	
	ContractPanel(){
		super(new FlowLayout());
		this.setOpaque(false);
		String lPath = getClass().getClassLoader().getResource(".").getPath();
		ImageIcon lIcon = new ImageIcon(lPath + "../lib/TrumpImages/null.png");
		lIcon = new ImageIcon( lIcon.getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
		this.add(new JLabel(lIcon));
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
		lIcon = new ImageIcon( lIcon.getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
		this.add(new JLabel(lIcon));
		this.validate();
		this.repaint();
	}
	
	public void setBid(Bid pBid){
		aBid = pBid;
		this.redraw();
	}
}
