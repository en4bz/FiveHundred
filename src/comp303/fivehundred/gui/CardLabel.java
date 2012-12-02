package comp303.fivehundred.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import comp303.fivehundred.gui.external.RotatedIcon;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardImages;

/**
 * Class for rendering playing cards as JLabels.
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
	 * @param pCard : Card to render. A <code>null</code> value will draw the back of the card.
	 * @param pRotation : Rotation <code>enum</code>
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
		if(aIsVisible && aCard != null)
		{
			this.setIcon(new RotatedIcon(CardImages.getCard(aCard), aRotation));
		}
		else if(aIsVisible){
			String lPath = getClass().getClassLoader().getResource(".").getPath();
			ImageIcon lIcon = new ImageIcon(lPath + "../src/images/null.png");
			this.setIcon(lIcon);
		}
		else
		{
			this.setIcon(new RotatedIcon(CardImages.getBack(), aRotation));
		}
	}
	
	/**
	 * @return Whether the card's face is visible.
	 */
	public boolean getVisibility()
	{
		return aIsVisible;
	}
	
	/**
	 * @param pVisibility : Indicates whether the card's face is visible.
	 */
	public void setVisibility(boolean pVisibility)
	{
		aIsVisible = pVisibility;
		drawCard();
	}
	
	/**
	 * @return The current rotaion of the card.
	 */
	public Rotation getRotation()
	{
		return aRotation;
	}
	
	/**
	 * Sets the rotation of the card
	 * @param pRotation
	 */
	public void setRotation(Rotation pRotation)
	{
		aRotation = pRotation;
		drawCard();
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		if(this.getParent().getParent() instanceof PlayerArea && ((CardListPanel)this.getParent()).getRotation() == Rotation.DEFAULT){
			GameFrame.nextCard = aCard;
		}
	}

	@Override
	public void mousePressed(MouseEvent e){}

	@Override
	public void mouseReleased(MouseEvent e){}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		if(this.getParent().getParent() instanceof PlayerArea && ((CardListPanel)this.getParent()).getRotation() == Rotation.DEFAULT){
			this.setLocation(this.getX(), this.getY() - 10);
			this.repaint();
		}
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		if(this.getParent().getParent() instanceof PlayerArea && ((CardListPanel)this.getParent()).getRotation() == Rotation.DEFAULT){
			this.setLocation(this.getX(), this.getY() + 10);
			this.repaint();
		}
	}
}
