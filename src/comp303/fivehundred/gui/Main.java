package comp303.fivehundred.gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class Main extends JFrame
{

	//TODO: link each menu component to what they refer to
	
	JMenu game = new JMenu("Game");
	JMenu stats = new JMenu ("Statistics");
	JMenu settings = new JMenu ("Settings");
	JMenu help = new JMenu ("Help");
	
	JMenuItem quit = new JMenuItem ("Quit");
	JMenuItem newGame = new JMenuItem ("New Game");
	JMenuItem show = new JMenuItem ("Show Statistics");
	JMenuItem reset = new JMenuItem ("Reset Statistics");
	JMenuItem logFile = new JMenuItem ("Change log file directory");
	JMenuItem changeName = new JMenuItem ("Change name of the file");
	JMenuItem ask = new JMenuItem ("Ask for help...");
	
	public Main()
	{
		//General setup
		setTitle("Five Hundred ");

		//MENU BAR
		JMenuBar aBar = new JMenuBar();
		
		aBar.add(game);
		aBar.add(stats);
		aBar.add(settings);
		aBar.add(help);
		
		game.add(newGame);
		game.add(quit);
		
		stats.add(show);
		stats.add(reset);
		
		settings.add(logFile);
		settings.add(changeName);
		
		help.add(ask);
		
		// add menu to the current frame
		this.setJMenuBar(aBar); 
		
		//Link menu components
		
		//File -> quit = The program terminates
		quit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		
		//File -> New Game = create a new Game (Create a new stats file, etc)
		newGame.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				new GameFrame();
			}
		});
		
		//Statistics -> Show Stats
		show.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//Read the stats file
			}
		});
		
		//Statistics -> Reset Stats
		reset.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//Reset stats file
			}
		});
		
		//Settings -> Change log file
		logFile.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//Change directory
			}
		});
		
		//Settings -> Change file name
		changeName.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//Change name of the log file
			}
		});
		
		//Help -> ask for help...
		ask.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//"Instruction guide" that contains all the rules of the game? (TextFile)
			}
		});
		
		
		addGame();//debug
		
		setLocation(0, 0); //Top-left corner of the screen
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Program ends when we close the window
		setVisible(true); //Makes it visible
		pack(); //minimal size to see all components
		setResizable(false); //We cannot change the dimensions of the window by hand when the program runs (components don't move)
	}
	
	public void addGame()
	{
		//add components to the menu
		Container content = getContentPane();
		content.addNotify();
		
		JPanel board = new JPanel(new GridBagLayout());
		board.setBackground(Color.GREEN);
		board.setPreferredSize(new Dimension(900,600));
		board.addNotify();
	
		//New game board
		content.add(board,BorderLayout.CENTER);
		
		//Sub-Panels of game board
		
		JPanel widow = new JPanel ();
		widow.setLayout(new BoxLayout(widow, BoxLayout.LINE_AXIS));
		widow.setPreferredSize(new Dimension(300,200));
		widow.setBackground(Color.GREEN);
		widow.setBorder(BorderFactory.createTitledBorder("Widow")); //debug
		
		JPanel player1 = new JPanel();
		player1.setLayout(new BoxLayout(player1, BoxLayout.Y_AXIS)); //Tricks+Hand one over the other
		player1.setPreferredSize(new Dimension(300,200));
		player1.setBackground(Color.GREEN);
		player1.setBorder(BorderFactory.createTitledBorder("player1: Tricks won+Hand")); //debug
		
		JPanel player2 = new JPanel();
		player2.setLayout(new BoxLayout(player2, BoxLayout.X_AXIS)); //Tricks+Hand next to each other
		player2.setPreferredSize(new Dimension(300,200));
		player2.setBackground(Color.GREEN);
		player2.setBorder(BorderFactory.createTitledBorder("player 2: Tricks won+Hand")); //debug
		
		JPanel player3 = new JPanel();
		player3.setLayout(new BoxLayout(player3, BoxLayout.Y_AXIS)); //Tricks+Hand one over the other
		player3.setPreferredSize(new Dimension(300,200));
		player3.setBackground(Color.GREEN);
		player3.setBorder(BorderFactory.createTitledBorder("PLayer 3: Tricks won+Hand")); //debug
		
		JPanel player4 = new JPanel();
		player4.setLayout(new BoxLayout(player4, BoxLayout.X_AXIS)); //Tricks+Hand next to each other
		player4.setPreferredSize(new Dimension(300,200));
		player4.setBackground(Color.GREEN);
		player4.setBorder(BorderFactory.createTitledBorder("Player4: Tricks won+Hand")); //debug
		
		JPanel contract = new JPanel();
		contract.setLayout(new BoxLayout(contract, BoxLayout.X_AXIS)); //only one element = no alignment
		contract.setPreferredSize(new Dimension(300,200));
		contract.setBackground(Color.GREEN);
		contract.setBorder(BorderFactory.createTitledBorder("Contract")); //debug
		
		JPanel score_NS = new JPanel();
		score_NS.setBorder(BorderFactory.createTitledBorder("Score North-South Team")); //set title
		score_NS.setLayout(new BoxLayout(score_NS, BoxLayout.X_AXIS)); // only one element 
		score_NS.setPreferredSize(new Dimension(200,200));
		score_NS.setBackground(Color.GREEN);
		
		JPanel score_EW = new JPanel();
		score_EW.setBorder(BorderFactory.createTitledBorder("Score East-West Team")); //set title
		score_EW.setLayout(new BoxLayout(score_EW, BoxLayout.X_AXIS)); // only one element
		score_EW.setPreferredSize(new Dimension(200,200));
		score_EW.setBackground(Color.GREEN);
		
		JPanel game = new JPanel();
		game.setLayout(new GridBagLayout());
		game.setPreferredSize(new Dimension(300,200));
		game.setBackground(Color.GREEN);
		game.setBorder(BorderFactory.createTitledBorder("Game: 3x3 grid")); //debug
		
		
		//Set position of each element in board (using coordinates)
		
		GridBagConstraints w = new GridBagConstraints();
		w.gridx = 0;
		w.gridy = 0;
		w.fill = GridBagConstraints.BOTH;
		w.fill = GridBagConstraints.CENTER;
		
		GridBagConstraints p1 = new GridBagConstraints();
		p1.gridx = 1;
		p1.gridy = 0;
		p1.fill = GridBagConstraints.BOTH;
		p1.fill = GridBagConstraints.CENTER;
		
		GridBagConstraints p2 = new GridBagConstraints();
		p2.gridx = 2;
		p2.gridy = 1;
		p2.fill = GridBagConstraints.BOTH;
		p2.fill = GridBagConstraints.CENTER;
		
		GridBagConstraints p3 = new GridBagConstraints();
		p3.gridx = 1;
		p3.gridy = 2;
		p3.fill = GridBagConstraints.BOTH;
		p3.fill = GridBagConstraints.CENTER;
		
		GridBagConstraints p4 = new GridBagConstraints();
		p4.gridx = 0;
		p4.gridy = 1;
		p4.fill = GridBagConstraints.BOTH;
		p4.fill = GridBagConstraints.CENTER;
		
		GridBagConstraints con = new GridBagConstraints();
		con.gridx = 2;
		con.gridy = 0;
		con.fill = GridBagConstraints.BOTH;
		con.fill = GridBagConstraints.CENTER;
		
		GridBagConstraints s_EW = new GridBagConstraints();
		s_EW.gridx = 0;
		s_EW.gridy = 2;
		s_EW.fill = GridBagConstraints.BOTH;
		s_EW.fill = GridBagConstraints.CENTER;
		s_EW.anchor = GridBagConstraints.LINE_START;
		
		GridBagConstraints s_NS = new GridBagConstraints();
		s_NS.gridx = 2;
		s_NS.gridy = 2;
		s_NS.fill = GridBagConstraints.BOTH;
		s_NS.fill = GridBagConstraints.CENTER;
		s_NS.anchor = GridBagConstraints.LINE_END;
		
		GridBagConstraints g = new GridBagConstraints();
		g.gridx = 1;
		g.gridy = 1;
		g.fill = GridBagConstraints.BOTH;
		g.fill = GridBagConstraints.CENTER;
		
		//add JPanels to the board
		board.add(widow,w);
		board.add(player1,p1);
		board.add(contract,con);
		//new line
		board.add(player4,p4);
		board.add(game,g);
		board.add(player2,p2);
		//new line
		board.add(score_EW, s_EW);
		board.add(player3, p3);
		board.add(score_NS,s_NS);
		//end
		
		
		
	}

	
	//TEST
	public static void main(String[] args) {
		JFrame frame = new Main();
		//TODO: Create the board game independently (not at the same time as menu)
	}

	
}
