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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

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
	private int numGamesToPlay = 1;
	
	// Game constants
	private int MAX_CHAR_PLAYER = 12;
	private int NUM_PLAYERS = 4;
	private int NUM_TEAMS = 2;
	
	// Set to true to log
	private boolean aIsLogging = false;
	
		
	/**
	 * Displays dialog box prompting user to 
	 * @param pFrame
	 * @post 
	 */
	public PlayerMenu(GameFrame pFrame)
	{
		aFrame = pFrame;
		setLayout(new BorderLayout());
		setMinimumSize(new Dimension(GameFrame.WIDTH, GameFrame.HEIGHT));
		setPreferredSize(new Dimension(GameFrame.WIDTH, GameFrame.HEIGHT));	
		// TODO wonder if I shouldn't just go ahead and create a smaller wrapper panel.
		setMaximumSize(new Dimension(GameFrame.WIDTH, GameFrame.HEIGHT));
		
		JPanel lWrapper = new JPanel();
		lWrapper.setLayout(new BoxLayout(lWrapper, BoxLayout.Y_AXIS));
//		lWrapper.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
//		lWrapper.setPreferredSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
//		lWrapper.setMaximumSize(new Dimension(MAX_WIDTH, MAX_HEIGHT));
		int lTopPadding = (int) (GameFrame.HEIGHT * 0.3); 
		System.out.println(lTopPadding);
		lWrapper.setBorder(new EmptyBorder( (int) (GameFrame.HEIGHT * 0.2),
											(int) (GameFrame.WIDTH * 0.2),
											(int) (GameFrame.HEIGHT * 0.2),
											(int) (GameFrame.WIDTH * 0.2)));
		
		add(lWrapper, BorderLayout.CENTER);
		
		buildSelectTeamBox(lWrapper);
		buildNumberOfGamesBox(lWrapper);
		buildPracticeCheckBox(lWrapper);
		buildPlayButton(lWrapper);
		
		aFrame.add(this);
		aFrame.pack();
		

	}

	private void buildSelectTeamBox(JPanel lWrapper)
	{
		log("Build select team box.");
		JPanel teamBox = new JPanel();
		double ratio = 1;
		teamBox.setLayout(new BoxLayout(teamBox, BoxLayout.Y_AXIS));
//		teamBox.setMinimumSize(new Dimension(MIN_WIDTH, (int) (MIN_HEIGHT*ratio)));
//		teamBox.setPreferredSize(new Dimension(MIN_WIDTH, (int) (MIN_HEIGHT*ratio)));
//		teamBox.setMaximumSize(new Dimension(MAX_WIDTH, (int) (MAX_HEIGHT*ratio)));
		teamBox.setBorder(BorderFactory.createTitledBorder(MESSAGES.getString("comp303.fivehundred.gui.PlayerMenu.SelectTeam")));
		for(int i = 0; i < NUM_TEAMS; i++)
		{
			buildTeamBox(teamBox, i);
		}
		lWrapper.add(teamBox);
	}

	private void buildTeamBox(JPanel pBox, int pTeamIndex)
	{
		JPanel teamGrid = new JPanel();
		teamGrid.setBorder(BorderFactory.createTitledBorder(MESSAGES.getString("comp303.fivehundred.gui.PlayerMenu.Team" + (pTeamIndex+1))));
		teamGrid.setLayout(new GridLayout(2,2));
//		teamGrid.setPreferredSize(new Dimension(MIN_WIDTH, (int) (MIN_HEIGHT*0.2)));
		
		teamGrid.add(getTypeDropDown(pTeamIndex*2));
		teamGrid.add(getNameTextField(pTeamIndex*2));
		teamGrid.add(getTypeDropDown(pTeamIndex*2+1));
		teamGrid.add(getNameTextField(pTeamIndex*2+1));		
		pBox.add(teamGrid);
	}
	
	private void buildNumberOfGamesBox(JPanel lWrapper)
	{
		JPanel lTextBoxPanel = new JPanel();
		lTextBoxPanel.setLayout(new GridLayout(1,2));
		lTextBoxPanel.setMinimumSize(new Dimension(MIN_WIDTH, (int) (MIN_HEIGHT * 0.15)));
		lTextBoxPanel.setPreferredSize(new Dimension(MIN_WIDTH, (int) (MIN_HEIGHT * 0.15)));
		lTextBoxPanel.setMaximumSize(new Dimension(MIN_WIDTH, (int) (MIN_HEIGHT * 0.15)));
		lWrapper.add(lTextBoxPanel);
		
		JLabel lLabel = new JLabel(MESSAGES.getString("comp303.fivehundred.gui.PlayerMenu.NumGames"));
		lTextBoxPanel.add(lLabel);
		
		final JTextField lTField = new JTextField(MAX_CHAR_PLAYER);
		lTField.setText(Integer.toString(numGamesToPlay));
		lTField.setMinimumSize(new Dimension(lTField.getMinimumSize().width, TXT_HEIGHT));
		lTField.setPreferredSize(new Dimension(lTField.getPreferredSize().width, TXT_HEIGHT));
		lTField.setMaximumSize(new Dimension(lTField.getMaximumSize().width, TXT_HEIGHT));
		lTField.addKeyListener(new KeyListener()
		{
			
			@Override
			public void keyTyped(KeyEvent arg0)
			{
				if(lTField.getText().equals(""))
				{
					numGamesToPlay = 0;
				}
				else
				{
					try {
						numGamesToPlay = Integer.parseInt(lTField.getText());
					} 
					catch(NumberFormatException nFE)
					{
						JOptionPane.showMessageDialog(PlayerMenu.this, MESSAGES.getString("comp303.fivehundred.gui.PlayerMenu.NumberFormatError"));
						lTField.setText(Integer.toString(numGamesToPlay));
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent arg0){}

			@Override
			public void keyReleased(KeyEvent arg0)
			{
				if(lTField.getText().equals(""))
				{
					numGamesToPlay = 0;
				}
				else
				{
					try {
						numGamesToPlay = Integer.parseInt(lTField.getText());
					} 
					catch(NumberFormatException nFE)
					{
						JOptionPane.showMessageDialog(PlayerMenu.this, MESSAGES.getString("comp303.fivehundred.gui.PlayerMenu.NumberFormatError"));
						lTField.setText(Integer.toString(numGamesToPlay));
					}
				}		
			}
		});
		lTextBoxPanel.add(lTField);
		
	}
	
	private void buildPracticeCheckBox(JPanel lWrapper)
	{
		log("Build practice mode checkbox.");
		JPanel lCheckBoxPanel = new JPanel();
		lCheckBoxPanel.setLayout(new GridLayout(1,1));
		lCheckBoxPanel.setMinimumSize(new Dimension(MIN_WIDTH, (int) (MIN_HEIGHT * 0.15)));
		lCheckBoxPanel.setPreferredSize(new Dimension(MIN_WIDTH, (int) (MIN_HEIGHT * 0.15)));
		lCheckBoxPanel.setMaximumSize(new Dimension(MIN_WIDTH, (int) (MIN_HEIGHT * 0.15)));
		lWrapper.add(lCheckBoxPanel);
		
		final JCheckBox lCBox = new JCheckBox(MESSAGES.getString("comp303.fivehundred.gui.PlayerMenu.PracticeMode"));
		lCBox.setSelected(practiceModeOn);
//		lCBox.setMinimumSize(new Dimension(lWrapper.getMinimumSize().width, TXT_HEIGHT));
//		lCBox.setMaximumSize(new Dimension(lWrapper.getMaximumSize().width, TXT_HEIGHT));	
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
	
	private void buildPlayButton(JPanel lWrapper)
	{
		JPanel lButtonPanel = new JPanel();
		lButtonPanel.setLayout(new GridLayout(1,1));
		int buttonWidth = 200;
		lButtonPanel.setMinimumSize(new Dimension(buttonWidth, (int) (MIN_HEIGHT * 0.15)));
		lButtonPanel.setPreferredSize(new Dimension(buttonWidth, (int) (MIN_HEIGHT * 0.15)));
		lButtonPanel.setMaximumSize(new Dimension(buttonWidth, (int) (MIN_HEIGHT * 0.15)));
		lWrapper.add(lButtonPanel, BorderLayout.SOUTH);
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
			aFrame.update(new Notification("gui.gameframe", this, aFrame.getNotificationSequenceNumber(), GameFrame.State.newGameSet.toString()));
			//aFrame.setEnabled(true);
			//this.setVisible(false);
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
					break;
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
	
	protected int getNumberOfGamesToPlay()
	{
		return numGamesToPlay;
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
		
		if(numGamesToPlay <= 0)
		{
			JOptionPane.showMessageDialog(PlayerMenu.this, MESSAGES.getString("comp303.fivehundred.gui.PlayerMenu.NumberFormatError"));
			return false;
		}
		
		return true;
	}
	
	private void log(String message)
	{
		if(aIsLogging)
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
