package comp303.fivehundred.gui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardImages;

public class CardLabel extends JLabel implements MouseListener
{
	/**
	 * @author Ian Forbes
	 * Constants representing the rotation of a card. 
	 */
	public static final int DEG0 = 0;
	public static final int DEG90 = 1;
	public static final int DEG180 = 2;
	public static final int DEG270 = 3;
	
	private Card aCard;
	private boolean aIsVisible;
	private int aRotation;
	
	public CardLabel(Card pCard, int pRotation, boolean pVisibility)
	{
		super();
		/*
		 * http://stackoverflow.com/questions/4287499/rotate-jlabel-or-imageicon-on-java-swing
		 */
		Image lCardImage = CardImages.getCard(pCard).getImage();
//		Graphics2D lCardGraphic = (Graphics2D) lCardImage.getGraphics();
//		lCardGraphic.rotate(Math.PI/2);
		
		this.setIcon(new ImageIcon(lCardImage));
		aCard = pCard;
		aIsVisible = pVisibility;
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

}
