package comp303.fivehundred.gui;

import java.awt.GridLayout;

import javax.swing.JPanel;

import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

@SuppressWarnings("serial")
public class WidowPanel extends JPanel
{
	private CardList aWidowCards;
	private boolean aIsVisible;
	
	public WidowPanel(boolean pVisibility){
		super((new GridLayout(2,3,5,5)));
		aIsVisible = pVisibility;
		this.setOpaque(false);
		for(int i = 0; i < 6; i++){
			this.add(new CardLabel(null,Rotation.DEFAULT,false));
		}
	}
	
	public void updateWidow(CardList pWidow){
		aWidowCards = pWidow;
		this.redraw();
	}
	
	private void redraw(){
		this.removeAll();
		for(Card c : aWidowCards){
				this.add(new CardLabel(c,Rotation.DEFAULT, aIsVisible));
		}
		this.validate();
		this.repaint();
	}
	
	public void setVisibility(boolean pVisibility){
		aIsVisible = pVisibility;
	}
}
