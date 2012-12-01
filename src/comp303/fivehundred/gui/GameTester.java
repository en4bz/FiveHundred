package comp303.fivehundred.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import comp303.fivehundred.ai.RandomRobot;
import comp303.fivehundred.model.AllCards;
import comp303.fivehundred.model.Bid;
import comp303.fivehundred.player.APlayer;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;

/**
 * Testing Class for CardListPanel and HandPanel
 * @author Ian Forbes
 */
@SuppressWarnings("serial")
public class GameTester extends JFrame
{
	public GameTester()
	{
		setTitle("Five Hundred");
		setLayout(new BorderLayout());
		((BorderLayout) this.getLayout()).setHgap(50);
		((BorderLayout) this.getLayout()).setVgap(50);
		
		APlayer p1 = new RandomRobot("p1");
		CardList cList = new CardList();
		cList.add(AllCards.aHJo);
		cList.add(AllCards.a4D);
		cList.add(AllCards.a4H);
		cList.add(AllCards.aLJo);
		for(Card c : cList){
			p1.addCardToHand(c);
		}
		
		PlayerArea Derp1 = new PlayerArea(p1,Rotation.UPSIDE_DOWN, true);
		Derp1.addTrick();
		CardListPanel Derp2 = new CardListPanel(cList,Rotation.RIGHT, true);
		CardListPanel Derp3 = new CardListPanel(cList,Rotation.LEFT, true);
		CardListPanel Derp4 = new CardListPanel(cList,Rotation.ABOUT_CENTER, true);
		
		
		this.add(new ContractPanel(new Bid(8, Suit.CLUBS)), BorderLayout.CENTER);
		this.add(Derp1, BorderLayout.PAGE_START);
		this.add(Derp2, BorderLayout.LINE_START);
		this.add(Derp3, BorderLayout.LINE_END);
		this.add(Derp4, BorderLayout.PAGE_END);
		
		
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		this.setLocationRelativeTo(null);
		this.pack();
		this.setVisible( true );
		try
		{
			Thread.sleep(2000);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Derp1.addTrick();
		Derp2.setVisibility(false);
	}
	
	public static void main(String[] args){
		new GameTester();
	}
}
