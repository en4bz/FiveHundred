package comp303.fivehundred.gui;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.management.Notification;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

import comp303.fivehundred.engine.GameEngine;
import comp303.fivehundred.mvc.Observer;
import comp303.fivehundred.player.Team;

@SuppressWarnings("serial")
public class GameFrame extends JFrame implements Observer
{
	public static Color BACKGROUND_COLOR = new Color(40, 160, 20);
//	private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("comp303.fivehundred.gui.MessageBundle");
	private long aNotificationSequenceNumber = 0;
	private GameEngine aEngine;
	private PlayerMenu aPlayerMenu;
	
	private boolean aPracticeModeOn;

	public GameFrame()
	{
//		aPlayerMenu = new PlayerMenu(this);
		this.setTitle("Five Hundred");	

		this.setJMenuBar(new Menu()); //add menu to the new window (new game)
		
		this.setLayout(new FlowLayout());
		this.add(new GameBoard());
		this.setLocation(0, 0); //Top-left corner of the screen
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Program ends when we close the window
		this.setVisible(true); //Makes it visible
		this.pack(); //minimal size to see all components
		this.setResizable(false); //We cannot change the dimensions of the window by hand when the program runs (components don't move)	
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
					aEngine = new GameEngine(lTeams[0], lTeams[1]);
					aPracticeModeOn = aPlayerMenu.isPracticeModeOn();
					break;
				default:
					break;
			}
		}
	}
			
	protected long getNotificationSequenceNumber()
	{
		return aNotificationSequenceNumber++;	
	}
	
	public enum State {
		newGame
	}

}
