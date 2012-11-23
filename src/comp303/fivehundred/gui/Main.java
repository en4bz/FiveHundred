package comp303.fivehundred.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		
		//File -> Quit = exit the program
		Listener ec = new Listener();
		quit.addActionListener(ec);
		
		addGame();
		
		setLocationRelativeTo(null); //put it in the center of the screen
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //end program when we close the window
		setVisible(true); //make it visible
	}
	
	public void addGame()
	{
		//add components to the menu
		Container content = getContentPane();
		content.addNotify();
		
		JPanel board = new JPanel(new GridBagLayout());
		board.setMinimumSize(new Dimension(400,200));
		board.setBackground(Color.GREEN);
		board.setPreferredSize(new Dimension(700,500));
		board.addNotify();
	
		//New game board
		content.add(board,BorderLayout.CENTER);
		
		//Sub-Panels of game board
		
		JPanel widow = new JPanel ();
		widow.setLayout(new BoxLayout(widow, BoxLayout.LINE_AXIS));
		
		JPanel player1 = new JPanel();
		player1.setLayout(new BoxLayout(player1, BoxLayout.Y_AXIS)); //Tricks+Hand one over the other
		
		JPanel player2 = new JPanel();
		player2.setLayout(new BoxLayout(player2, BoxLayout.X_AXIS)); //Tricks+Hand one over the other
		
		JPanel player3 = new JPanel();
		player3.setLayout(new BoxLayout(player3, BoxLayout.Y_AXIS)); //Tricks+Hand one over the other
		
		JPanel player4 = new JPanel();
		player4.setLayout(new BoxLayout(player4, BoxLayout.X_AXIS)); //Tricks+Hand one over the other
		
		JPanel contract = new JPanel();
		contract.setLayout(new BoxLayout(contract, BoxLayout.X_AXIS)); //only one element = no alignment
		
		JPanel score_NS = new JPanel();
		score_NS.setLayout(new BoxLayout(score_NS, BoxLayout.X_AXIS)); // only one element (textbox??)
		
		JPanel score_EW = new JPanel();
		score_EW.setLayout(new BoxLayout(score_EW, BoxLayout.X_AXIS)); // only one element (textbox??)
		
		JPanel game = new JPanel();
		game.setLayout(new GridBagLayout());
		
		//Set position of each element in board (using coordinates)
		
		GridBagConstraints w = new GridBagConstraints();
		w.gridx = 0;
		w.gridy = 0;
		
		GridBagConstraints p1 = new GridBagConstraints();
		p1.gridx = 1;
		p1.gridy = 0;
		
		GridBagConstraints p2 = new GridBagConstraints();
		p2.gridx = 2;
		p2.gridy = 1;
		
		GridBagConstraints p3 = new GridBagConstraints();
		p3.gridx = 1;
		p3.gridy = 2;
		
		GridBagConstraints p4 = new GridBagConstraints();
		p4.gridx = 0;
		p4.gridy = 1;
		
		GridBagConstraints con = new GridBagConstraints();
		con.gridx = 2;
		con.gridy = 0;
		
		GridBagConstraints s_EW = new GridBagConstraints();
		s_EW.gridx = 0;
		s_EW.gridy = 2;
		
		GridBagConstraints s_NS = new GridBagConstraints();
		s_NS.gridx = 2;
		s_NS.gridy = 2;
		
		GridBagConstraints g = new GridBagConstraints();
		g.gridx = 1;
		g.gridy = 1;
		
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
	
	
	class Listener implements ActionListener{
		public void actionPerformed(ActionEvent e)
		{
		System.exit(0);
		}
		} 
	
	//TEST
	public static void main(String[] args) {
		JFrame frame = new Main();
		//TODO: Find a way to create the board game independently (not at the same time as menu)
		frame.pack(); //minimal size to see all components
	}

	
}
