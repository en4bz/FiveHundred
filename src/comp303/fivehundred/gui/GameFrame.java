package comp303.fivehundred.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.management.Notification;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;

import comp303.fivehundred.util.Card;
import comp303.fivehundred.engine.GameEngine;
import comp303.fivehundred.mvc.Observer;
import comp303.fivehundred.player.*;

@SuppressWarnings("serial")
public class GameFrame extends JFrame implements Observer
{
	public static Color BACKGROUND_COLOR = new Color(40, 160, 20);
	private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("comp303.fivehundred.gui.MessageBundle");
	private long aNotificationSequenceNumber = 0;
	private GameEngine aEngine;
	private PlayerMenu aPlayerMenu;
	
	private boolean aPracticeModeOn;

	public GameFrame()
	{
		aPlayerMenu = new PlayerMenu(this);
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
