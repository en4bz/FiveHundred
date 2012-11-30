package comp303.fivehundred.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import java.awt.FlowLayout;

import javax.management.Notification;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import comp303.fivehundred.engine.GameEngine;
import comp303.fivehundred.mvc.Observer;
import comp303.fivehundred.player.Team;

@SuppressWarnings("serial")
public class GameFrame extends JFrame implements Observer
{
	public static Color BACKGROUND_COLOR = new Color(40, 160, 20);
	private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("comp303.fivehundred.gui.MessageBundle");
	private long aNotificationSequenceNumber = 0;
	private GameEngine aEngine;
	private PlayerMenu aPlayerMenu;
	private GameBoard aBoard;
	public static int WIDTH = 900;
	public static int HEIGHT = 600;
	
	private boolean aPracticeModeOn = true;

	public GameFrame()
	{
		// Build basic frame
		this.setTitle("Five Hundred");	
		this.setLayout(new FlowLayout());
		this.setLocation(0, 0); //Top-left corner of the screen
		this.setSize(new Dimension(WIDTH, HEIGHT));
		this.setResizable(false); //We cannot change the dimensions of the window by hand when the program runs (components don't move)	
		
		// Add Menu
		this.setJMenuBar(new Menu()); //add menu to the new window (new game)
		
		// Add panel to configure game settings
		aPlayerMenu = new PlayerMenu(this);
		this.add(aPlayerMenu);
				
		// Make visible
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		this.setLocationRelativeTo(null);
		this.pack();
		this.setVisible( true );
	}
	
	public static void main(String[] args)
	{
		new GameFrame();
	}

	@Override
	public void update(Notification pNotification)
	{
		if(pNotification.getType().equals("gui.gameframe"))
		{
			State lState = State.valueOf(pNotification.getMessage());
			switch (lState) 
			{
				case newGame:
					Team[] lTeams = aPlayerMenu.getTeams();
					System.out.println(lTeams[0]);
					System.out.println(lTeams[1]);
					aEngine = new GameEngine(lTeams[0], lTeams[1]);
					aPracticeModeOn = aPlayerMenu.isPracticeModeOn();
					this.remove(aPlayerMenu);
					aBoard = new GameBoard(); // TODO pass frame
					this.add(aBoard);
					this.pack();
					
//					JPanel contentPane = (JPanel) getContentPane();
//					contentPane.removeAll();
//					setTitle("Five Hundred");
//					this.getContentPane().invalidate(); 
//					this.getContentPane().repaint();
					break;
				case newDeal:
					// TODO redraw handpanels Hand.update(
					break;
				
				default:
					break;
			}
		}
	}
	
	GameEngine getGameEngine() {
		return aEngine;
	}
			
	protected long getNotificationSequenceNumber()
	{
		return aNotificationSequenceNumber++;	
	}
	
	public enum State {
		newGame,
		newDeal,
		newBid,
		newContract,
		cardsDiscarded,
		cardPlayed,
		newTrick,
		trickWon,
		roundEnd,
		gameOver
	}

}
