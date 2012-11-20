package comp303.fivehundred.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import comp303.fivehundred.gui.external.RotatedIcon;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardImages;
/**
 * @author Ian Forbes
 */
@SuppressWarnings("serial")
public class CardLabel extends JLabel implements MouseListener
{
	private Card aCard;
	private Rotation aRotation;
	private boolean aIsVisible;
	
	/**
	 * Create a new CardLabel
	 * @param pCard : Card to Render
	 * @param pRotation : Rotation enum
	 * @param pVisibility : Whether the card's front is visible
	 */
	public CardLabel(Card pCard, Rotation pRotation, boolean pVisibility)
	{
		super();
		aCard = pCard;
		aRotation = pRotation;
		aIsVisible = pVisibility;
		drawCard();
		this.addMouseListener(this);
	}
	
	private void drawCard()
	{
		if(aIsVisible)
		{
			this.setIcon(new RotatedIcon(CardImages.getCard(aCard), aRotation));
		}
		else
		{
			this.setIcon(new RotatedIcon(CardImages.getBack(), aRotation));
		}
	//	this.repaint();
	}
	
	public boolean getVisibility()
	{
		return aIsVisible;
	}
	
	public void setVisibility(boolean pVisibility)
	{
		aIsVisible = pVisibility;
		drawCard();
	}
	
	public Rotation getRotation()
	{
		return aRotation;
	}
	
	public void setRotation(Rotation pRotation)
	{
		aRotation = pRotation;
		drawCard();
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		this.setVisibility(!aIsVisible);
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
