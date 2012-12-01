package comp303.fivehundred.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ResourceBundle;

import javax.management.Notification;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import comp303.fivehundred.ai.BasicRobot;
import comp303.fivehundred.ai.RandomRobot;
import comp303.fivehundred.player.APlayer;
import comp303.fivehundred.player.HumanPlayer;
import comp303.fivehundred.player.Team;

/**
 * 
 * @author Eleyine
 * Sources:
 * 		docs.oracle.com for code on GridBagLayout
 */
@SuppressWarnings("serial")
public class PlayerMenu extends JPanel
{
	private final Logger aLogger = LoggerFactory.getLogger("PlayerMenuLogger");
	
	private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("comp303.fivehundred.gui.MessageBundle");
	private GameFrame aFrame;
	
	// Layout constants
	public static Color BACKGROUND_COLOR = new Color(40, 160, 20);
	private int MIN_WIDTH = 600;
	private int MIN_HEIGHT = 300;
	private int PRF_WIDTH = 600;
	private int PRF_HEIGHT = 300;
	private int MAX_WIDTH = Integer.MAX_VALUE;
	private int MAX_HEIGHT = Integer.MAX_VALUE;
	private int TXT_HEIGHT = 40;
	private int H_GAP = PRF_WIDTH;
	private int V_GAP = 10;
	
	// Default values
	private PlayerType[] aPlayerTypes = {PlayerType.BasicRobot, PlayerType.BasicRobot, PlayerType.RandomRobot, PlayerType.RandomRobot};
	private String[] aPlayerNames = {"Alice", "Alex", "John", "Jannice"};
	private boolean practiceModeOn = true;
	
	// Game constants
	private int MAX_CHAR_PLAYER = 12;
	private int NUM_PLAYERS = 4;
	private int NUM_TEAMS = 2;
	
		
	/**
	 * Displays dialog box prompting user to 
	 * @param pFrame
	 * @post 
	 */
	public PlayerMenu(GameFrame pFrame)
	{
		aFrame = pFrame;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//		setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
//		setPreferredSize(new Dimension(GameFrame.WIDTH, GameFrame.HEIGHT));	
		// TODO wonder if I shouldn't just go ahead and create a smaller wrapper panel.
//		setMaximumSize(new Dimension(MAX_WIDTH, MAX_HEIGHT));
		
		buildSelectTeamBox();
		buildPracticeCheckBox();
		buildPlayButton();
		

	}

	private void buildSelectTeamBox()
	{
		log("Build select team box.");
		JPanel teamBox = new JPanel();
		teamBox.setLayout(new BoxLayout(teamBox, BoxLayout.Y_AXIS));
		teamBox.setBorder(BorderFactory.createTitledBorder(MESSAGES.getString("comp303.fivehundred.gui.PlayerMenu.SelectTeam")));
		for(int i = 0; i < NUM_TEAMS; i++)
		{
			buildTeamBox(teamBox, i);
		}
		add(teamBox);
	}

	private void buildTeamBox(JPanel pBox, int pTeamIndex)
	{
		JPanel teamGrid = new JPanel();
		teamGrid.setBorder(BorderFactory.createTitledBorder(MESSAGES.getString("comp303.fivehundred.gui.PlayerMenu.Team" + (pTeamIndex+1))));
		teamGrid.setLayout(new GridLayout(2,2));
		teamGrid.setPreferredSize(new Dimension(MIN_WIDTH, (int) (MIN_HEIGHT*0.2)));
		
		teamGrid.add(getTypeDropDown(pTeamIndex*2));
		teamGrid.add(getNameTextField(pTeamIndex*2));
		teamGrid.add(getTypeDropDown(pTeamIndex*2+1));
		teamGrid.add(getNameTextField(pTeamIndex*2+1));		
		pBox.add(teamGrid);
	}
	
	private void buildPracticeCheckBox()
	{
		log("Build practice mode checkbox.");
		JPanel lCheckBoxPanel = new JPanel();
		lCheckBoxPanel.setLayout(new GridLayout(1,1));
//		lCheckBoxPanel.setMinimumSize(new Dimension(MIN_WIDTH, (int) (MIN_HEIGHT * 0.15)));
//		lCheckBoxPanel.setPreferredSize(new Dimension(MIN_WIDTH, (int) (MIN_HEIGHT * 0.15)));
//		lCheckBoxPanel.setMaximumSize(new Dimension(MIN_WIDTH, (int) (MIN_HEIGHT * 0.15)));
		add(lCheckBoxPanel);
		
		final JCheckBox lCBox = new JCheckBox(MESSAGES.getString("comp303.fivehundred.gui.PlayerMenu.PracticeMode"));
		lCBox.setSelected(practiceModeOn);
//		lCBox.setMinimumSize(new Dimension(pPanel.getMinimumSize().width, TXT_HEIGHT));
//		lCBox.setMaximumSize(new Dimension(pPanel.getMaximumSize().width, TXT_HEIGHT));	
		lCBox.setHorizontalAlignment(JCheckBox.CENTER);
		
		lCBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				practiceModeOn = lCBox.isSelected();
				log("practiceModeOn is now " + practiceModeOn);
			}
		});
		lCheckBoxPanel.add(lCBox);	
	}
	
	private void buildPlayButton()
	{
		JPanel lButtonPanel = new JPanel();
		lButtonPanel.setLayout(new GridLayout(1,1));
		lButtonPanel.setMinimumSize(new Dimension(MIN_WIDTH, (int) (MIN_HEIGHT * 0.15)));
		lButtonPanel.setPreferredSize(new Dimension(MIN_WIDTH, (int) (MIN_HEIGHT * 0.15)));
		lButtonPanel.setMaximumSize(new Dimension(MIN_WIDTH, (int) (MIN_HEIGHT * 0.15)));
		add(lButtonPanel, BorderLayout.SOUTH);
		JButton lButton = new JButton(MESSAGES.getString("comp303.fivehundred.gui.PlayerMenu.Play"));
		lButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				startGame();
			}
		});
		lButtonPanel.add(lButton);
	}
	
	
	private JComboBox getTypeDropDown(final int pPlayerIndex)
	{
		final JComboBox lDropDown = new JComboBox(PlayerType.values());
		lDropDown.setEditable(false);
		lDropDown.setSelectedItem(aPlayerTypes[pPlayerIndex]);
		lDropDown.setMinimumSize(new Dimension(lDropDown.getMinimumSize().width, TXT_HEIGHT));
		lDropDown.setPreferredSize(new Dimension(lDropDown.getPreferredSize().width, TXT_HEIGHT));
		lDropDown.setMaximumSize(new Dimension(lDropDown.getMaximumSize().width, TXT_HEIGHT));
		lDropDown.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				aPlayerTypes[pPlayerIndex] = (PlayerType) lDropDown.getSelectedItem();
				log(aPlayerTypes[pPlayerIndex]+ " was selected!");
			}
		});
		return lDropDown;
	}
	
	private JTextField getNameTextField(final int pPlayerIndex)
	{

		final JTextField lTField = new JTextField(MAX_CHAR_PLAYER);
		lTField.setText(aPlayerNames[pPlayerIndex]);
		lTField.setToolTipText("Enter the player's name. Up to " + MAX_CHAR_PLAYER + " characters.");
		lTField.setMinimumSize(new Dimension(lTField.getMinimumSize().width, TXT_HEIGHT));
		lTField.setPreferredSize(new Dimension(lTField.getPreferredSize().width, TXT_HEIGHT));
		lTField.setMaximumSize(new Dimension(lTField.getMaximumSize().width, TXT_HEIGHT));
		lTField.addKeyListener(new KeyListener()
		{
			
			@Override
			public void keyTyped(KeyEvent arg0)
			{
				aPlayerNames[pPlayerIndex] = lTField.getText();
				log(aPlayerTypes[pPlayerIndex]+ " has new name: " + aPlayerNames[pPlayerIndex]);
			}

			@Override
			public void keyPressed(KeyEvent arg0){}

			@Override
			public void keyReleased(KeyEvent arg0)
			{
				aPlayerNames[pPlayerIndex] = lTField.getText();
				log(aPlayerTypes[pPlayerIndex]+ " has new name: " + aPlayerNames[pPlayerIndex]);
			}
		});
		return lTField;
	}
	
	private void startGame()
	{
		if(validatePlayers())
		{
			log("Starting game.");
			aFrame.update(new Notification("gui.gameframe", this, aFrame.getNotificationSequenceNumber(), GameFrame.State.newGame.toString()));
			aFrame.setEnabled(true);
			this.setVisible(false);
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
					throw new GUIException("Player type " + aPlayerTypes[i] + " not recognized.");
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
					JOptionPane.showMessageDialog(this, MESSAGES.getString("comp303.fivehundred.gui.PlayerMenu.TooManyHumansError"));
					return false;
				}
			}
			if(aPlayerNames[i].equals(""))
			{
				JOptionPane.showMessageDialog(this, MESSAGES.getString("comp303.fivehundred.gui.PlayerMenu.EmptyNameError"));
				return false;
			}
			for(int j = 0; j < NUM_PLAYERS; j++)
			{
				if(j != i && aPlayerNames[j].equals(aPlayerNames[i]))
				{
					JOptionPane.showMessageDialog(this, MESSAGES.getString("comp303.fivehundred.gui.PlayerMenu.SameNamesError") + " \""+ aPlayerNames[i] +"\"");
					return false;
				}
			}
			
		}
		return true;
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
