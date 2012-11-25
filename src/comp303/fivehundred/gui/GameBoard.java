package comp303.fivehundred.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

/**
 * Board of the game (new board for every new game)
 * @author Gabrielle Germain
 */

@SuppressWarnings("serial")
public class GameBoard extends JFrame
{

	public GameBoard()
	{
		setTitle("Five Hundred");	
		
		JMenuBar menu = new Menu();
		setJMenuBar(menu); //add menu to the new window (new game)
		
		
		//add components to the board
		Container content = getContentPane();
		content.addNotify();
		
		JPanel board = new JPanel(new GridBagLayout());
		board.setBackground(Color.GREEN);
		board.setPreferredSize(new Dimension(900,600));
		board.addNotify();
	
		//New game board
		add(board,BorderLayout.CENTER); //change
		
		
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
		player1.setBorder(BorderFactory.createTitledBorder("Player1: Tricks won+Hand")); //debug
		
		JPanel player2 = new JPanel();
		player2.setLayout(new BoxLayout(player2, BoxLayout.X_AXIS)); //Tricks+Hand next to each other
		player2.setPreferredSize(new Dimension(300,200));
		player2.setBackground(Color.GREEN);
		player2.setBorder(BorderFactory.createTitledBorder("Player 2: Tricks won+Hand")); //debug
		
		JPanel player3 = new JPanel();
		player3.setLayout(new BoxLayout(player3, BoxLayout.Y_AXIS)); //Tricks+Hand one over the other
		player3.setPreferredSize(new Dimension(300,200));
		player3.setBackground(Color.GREEN);
		player3.setBorder(BorderFactory.createTitledBorder("Player 3: Tricks won+Hand")); //debug
		
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
	
		setLocation(0, 0); //Top-left corner of the screen
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Program ends when we close the window
		setVisible(true); //Makes it visible
		pack(); //minimal size to see all components
		setResizable(false); //We cannot change the dimensions of the window by hand when the program runs (components don't move)
		
		
	}
	
	//"BUILD" METHODS FOR EACH COMPONENT THAT WE USE MORE THAN ONCE
	
	public JLabel hand (/*arguments?*/)
	{
		JLabel aHand = new JLabel(/*icon?*/);
		return aHand;
	}
	
	public JLabel tricksWon(/*arguments?*/)
	{
		JLabel aTrick = new JLabel(/*icon?*/);
		return aTrick;
	}
	
	public JLabel textBubble (String aString) //get aString from an observer?
	{
		JLabel bubble = new JLabel(aString, JLabel.CENTER); //the "bubble" is in the middle of the label.
		//add a background?
		return bubble;
	}
	
	//widow?
	//discarded cards?
	
	//Eventually add JLabels in each JPanel
	//ex: game.add(textBubble(/*arguments*/), location)
	
	
	
	//TEST
	public static void main(String[] args) {
		
		JFrame Game = new GameBoard();

	}
	
}
