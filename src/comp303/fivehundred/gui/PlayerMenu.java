package comp303.fivehundred.gui;

import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.management.Notification;
import java.util.Random;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JCheckBoxMenuItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import comp303.fivehundred.engine.GameEngine;
import comp303.fivehundred.engine.GameEngine.State;
import comp303.fivehundred.player.*;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.ai.BasicRobot;
import comp303.fivehundred.ai.RandomRobot;

/**
 * 
 * @author Eleyine
 * Sources:
 * 		docs.oracle.com for code on GridBagLayout
 */
@SuppressWarnings("serial")
public class PlayerMenu
{
	private final Logger aLogger = LoggerFactory.getLogger("PlayerMenuLogger");
	
	public static Color BACKGROUND_COLOR = new Color(40, 160, 20);
	private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("comp303.fivehundred.gui.MessageBundle");
	private int MAX_CHAR_PLAYER = 12;
	private int NUM_PLAYERS = 4;
	private int NUM_TEAMS = 2;
	private GameEngine aEngine;
	private GameFrame aFrame;
	private PlayerType[] aPlayerTypes = new PlayerType[NUM_PLAYERS];
	private String[] aPlayerNames = new String[NUM_PLAYERS];
	private boolean practiceModeOn = false;
	
	
	private void setPlayerType(int pIndex, PlayerType pType)
	{
		if(pIndex >=0 && pIndex < PlayerType.values().length)
			aPlayerTypes[pIndex] = pType;
	}
	
	private void setPlayerName(int pIndex, String pName)
	{
		if(pIndex >=0 && pIndex < PlayerType.values().length)
			aPlayerNames[pIndex] = pName;
	}
	
	/**
	 * Displays dialog box prompting user to 
	 * @param pFrame
	 * @post 
	 */
	public PlayerMenu(GameFrame pFrame)
	{
		aFrame = pFrame;
		aFrame.setTitle("Game settings");
		Container pane = pFrame.getContentPane();
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		// Build select team label
		log("Build select team label.");
		JLabel lLabel = new JLabel(MESSAGES.getString("comp303.fivehundred.gui.PlayerMenu.SelectTeam"));
		c.fill = GridBagConstraints.NORTH;
		c.gridx = 0;
		c.gridy = 0;
		pane.add(lLabel, c);
		
		// Build team 1 and team 2 labels
		log("Build team 1 and team 2 labels.");
		buildTeamLabel(pane, c, 1);		
		buildTeamLabel(pane, c, 2);	
	
		// Build drop-down boxes to select player type
		for(int i = 0; i < NUM_PLAYERS; i++)
		{
			buildDropDown(pane, c, i);
		}
		
		// Build text fields to select player names
		for(int i = 0; i < NUM_PLAYERS; i++)
		{
			buildTextField(pane, c, i);
		}
		
		// Build practice mode checkbox.
		log("Build practice mode checkbox.");
		final JCheckBoxMenuItem lCBox = new JCheckBoxMenuItem(MESSAGES.getString("comp303.fivehundred.gui.PlayerMenu.PracticeMode"));
		lCBox.setSelected(practiceModeOn);
		lCBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				practiceModeOn = lCBox.getState();
				log("practiceModeOn is now " + practiceModeOn);
			}
		});
		c.weightx = 0.5;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 5;
		pane.add(lCBox, c);
		
		
		// Build play button
		JButton lButton = new JButton(MESSAGES.getString("comp303.fivehundred.gui.PlayerMenu.Play"));
		lButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				
				startGame();
			}
		});
		c.fill = GridBagConstraints.SOUTH;
		c.weightx = 0.5;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 6;
		pane.add(lButton, c);
		
		aFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		aFrame.setLocationRelativeTo(null);
		aFrame.pack();
		aFrame.setVisible( true );
	}
	
	private void buildTeamLabel(Container pPane, GridBagConstraints pConstraint, int pIndex)
	{
		JLabel lLabel = new JLabel(MESSAGES.getString("comp303.fivehundred.gui.PlayerMenu.Team" + pIndex));
		pConstraint.fill = GridBagConstraints.VERTICAL;
		pConstraint.gridx = 0;
		pConstraint.gridy = 1 + 2 * (pIndex - 1);
		pConstraint.weightx = 0.5;
		pConstraint.gridheight = 2;
		pPane.add(lLabel, pConstraint);
	}
	
	/**
	 * @return
	 */
	private boolean validatePlayers()
	{
		int lHumans = 0;
		for(int i = 0; i < 4; i++)
		{
			if(aPlayerTypes[i] == null || aPlayerNames[i] == null)
			{
				return false;
			}
			if(aPlayerTypes[i] == PlayerType.Human)
			{
				lHumans++;
				if(lHumans > 1)
				{
					return false;
				}
			}
		}
		return true;
	}
	
	private void startGame()
	{
		if(validatePlayers())
		{
			log("Starting game.");
			aFrame.update(new Notification("gui.gameframe", this, aFrame.getNotificationSequenceNumber(), GameFrame.State.newGame.toString()));
		}
		else
		{
			log("Error!");
			//showErrorDialog();
		}
	}
	
	protected Team[] getTeams()
	{
		APlayer[] lPlayers = new APlayer[NUM_PLAYERS];
		Team[] lTeams = new Team[NUM_TEAMS];
		if(validatePlayers())
		{
			for(int i = 0; i < NUM_PLAYERS; i++)
			{
				switch(aPlayerTypes[i])
				{
				case BasicRobot:
					lPlayers[i] = new BasicRobot(aPlayerNames[i]);
					break;
				case RandomRobot:
					lPlayers[i] = new RandomRobot(aPlayerNames[i]);
					break;
				case Human:
					lPlayers[i] = new HumanPlayer(aPlayerNames[i]);
				case Advanced:
					// TODO change robot to advanced once the functionality is implemented
					lPlayers[i] = new BasicRobot(aPlayerNames[i]);
					break;
				default:
					// should never happen
					throw new GUIException("Player type " + aPlayerTypes[i] + " not recognized");
				}
			}
			lTeams[0] = new Team(lPlayers[0], lPlayers[1]);
			lTeams[1] = new Team(lPlayers[2], lPlayers[3]);
		}
		else
		{
			throw new GUIException("Trying to get teams from incorrectly configurated player settings.");
		}
		return lTeams;
	}
	
	protected boolean isPracticeModeOn()
	{
		return practiceModeOn;
	}
	
	private void buildDropDown(Container pPane, GridBagConstraints pConstraint, final int pPlayerIndex)
	{
		Random lRand = new Random();
		PlayerType lPlayerType = PlayerType.values()[lRand.nextInt(PlayerType.values().length)];
		final JComboBox lDropDown = new JComboBox(PlayerType.values());
		lDropDown.setEditable(false);
		lDropDown.setSelectedItem(lPlayerType);
		setPlayerType(pPlayerIndex, lPlayerType);
		lDropDown.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				setPlayerType(pPlayerIndex, (PlayerType) lDropDown.getSelectedItem());
				log(aPlayerTypes[pPlayerIndex]+ " was selected!");
			}
		});
		pConstraint.gridx = 1;
		pConstraint.gridy = 1 + pPlayerIndex;
		pConstraint.weightx = 0.5;
		pConstraint.gridheight = 1;
		pPane.add(lDropDown, pConstraint);
	}
	
	private void buildTextField(Container pPane, GridBagConstraints pConstraint, final int pPlayerIndex)
	{

		PlayerType lPlayerType = aPlayerTypes[pPlayerIndex];
		String lPlayerName = lPlayerType.toString() + pPlayerIndex;
		final JTextField lTField = new JTextField(MAX_CHAR_PLAYER);
		lTField.setText(lPlayerName);
		lTField.setToolTipText("Enter the player's name. Up to " + MAX_CHAR_PLAYER + " characters.");
		setPlayerName(pPlayerIndex, lPlayerName);
		lTField.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				setPlayerName(pPlayerIndex, lTField.getText());
				log(aPlayerTypes[pPlayerIndex]+ " has new name: " + aPlayerNames[pPlayerIndex]);
			}
		});
		pConstraint.gridx = 2;
		pConstraint.gridy = 1 + pPlayerIndex;
		pConstraint.weightx = 0.5;
		pConstraint.gridheight = 1;
		pPane.add(lTField, pConstraint);
	}
	
	public void log(String message)
	{
		aLogger.info(message);
	}
	

	
	public enum PlayerType
	{
		BasicRobot,
		RandomRobot,
		Advanced,
		Human // Human must always be the last type!
	}

}
