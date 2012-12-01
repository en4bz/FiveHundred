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
		this.setOpaque(false);
		this.redraw();
	}
	
	protected void redraw(){
		String lSuit;
		this.removeAll();
		try{ //TODO: Fix this ugly shit
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
			String lPath = getClass().getClassLoader().getResource(".").getPath();
			ImageIcon lIcon = new ImageIcon(lPath + "../lib/TrumpImages/" + lSuit + aBid.getTricksBid() + ".png");
			this.add(new JLabel(lIcon));
		}
		catch(Exception e){}
		finally{
			this.validate();
			this.repaint();
		}
	}
	
	public void setBid(Bid pBid){
		aBid = pBid;
		this.redraw();
	}
}
