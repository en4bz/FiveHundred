package comp303.fivehundred.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.player.APlayer;
import comp303.fivehundred.player.Team;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Suit;

/**
 * Board of the game (new board for every new game)
 * @author Gabrielle Germain
 */

@SuppressWarnings("serial")
public class GameBoard extends JPanel
{
	final APlayer[] aTeam1;
	final APlayer[] aTeam2;
	
	final ContractPanel aContract;
	final JPanel aWidow;
	final JPanel aCurrentTrick;
	
	final PlayerArea aBottomPlayer;
	final PlayerArea aTopPlayer;
	final PlayerArea aRightPlayer;
	final PlayerArea aLeftPlayer;
	
	public GameBoard(final Team[] pPlayer)
	{
		super(new GridBagLayout());
		this.setBackground(Color.GREEN);
		
		aTeam1 = pPlayer[0].getPlayers();
		aTeam2 = pPlayer[1].getPlayers();
		
		//Sub-Panels of game board
		
		aWidow = new JPanel ();
		aWidow.setLayout(new BoxLayout(aWidow, BoxLayout.LINE_AXIS));
		aWidow.setPreferredSize(new Dimension(300,200));
		aWidow.setBackground(Color.GREEN);
		aWidow.setBorder(BorderFactory.createTitledBorder("Widow")); //debug
		
		aTopPlayer = new PlayerArea(aTeam1[0], Rotation.UPSIDE_DOWN, false);
		aTopPlayer.setBackground(Color.GREEN);
		aTopPlayer.setBorder(BorderFactory.createTitledBorder("Player1: Tricks won+Hand")); //debug
		
		aRightPlayer = new PlayerArea(aTeam2[1], Rotation.LEFT, false);
		aRightPlayer.setBackground(Color.GREEN);
		aRightPlayer.setBorder(BorderFactory.createTitledBorder("Player 2: Tricks won+Hand")); //debug
		
		aBottomPlayer = new PlayerArea(aTeam1[1], Rotation.ABOUT_CENTER, true);
		aBottomPlayer.setBackground(Color.GREEN);
		aBottomPlayer.setBorder(BorderFactory.createTitledBorder("Player 3: Tricks won+Hand")); //debug
		
		aLeftPlayer = new PlayerArea(aTeam2[0], Rotation.RIGHT, false);
		aLeftPlayer.setBackground(Color.GREEN);
		aLeftPlayer.setBorder(BorderFactory.createTitledBorder("Player4: Tricks won+Hand")); //debug
		
		aContract = new ContractPanel(new Bid(7,Suit.HEARTS));//TODO:Fix this
		aContract.setBackground(Color.GREEN);
		aContract.setBorder(BorderFactory.createTitledBorder("Contract")); //debug
		
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
		
		aCurrentTrick = new JPanel(new BorderLayout());
		this.hack();
		aCurrentTrick.setBackground(Color.GREEN);
		aCurrentTrick.setBorder(BorderFactory.createTitledBorder("Game: 3x3 grid")); //debug
		
		
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
		
		//add a button to submit a trick
		JPanel submission = new JPanel();
		submission.setBorder(BorderFactory.createTitledBorder("Submit the trick"));

		JButton submit = new JButton("submit");
		submit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//Submit new info
			}
		});
		
		submission.add(submit);
		///////////////////////////
		
		
		//add JPanels to the board
		this.add(aWidow,w);
		this.add(aTopPlayer,p1);
		this.add(aContract,con);
		//new line
		this.add(aLeftPlayer,p4);
		this.add(aCurrentTrick,g);
		this.add(aRightPlayer,p2);
		//new line
		this.add(score_EW, s_EW);
		this.add(aBottomPlayer, p3);
		this.add(score_NS,s_NS);
		//end
	}
	
	//"BUILD" METHODS FOR EACH COMPONENT THAT WE USE MORE THAN ONCE
	
	public PlayerArea getBottomPlayer()
	{
		return aBottomPlayer;
	}

	public PlayerArea getTopPlayer()
	{
		return aTopPlayer;
	}

	public PlayerArea getRightPlayer()
	{
		return aRightPlayer;
	}

	public PlayerArea getLeftPlayer()
	{
		return aLeftPlayer;
	}
	
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
	
	public void updateTrick(APlayer pPlayer, Card c){
		if(aBottomPlayer.getPlayer() == pPlayer && c != null){
			aCurrentTrick.add(new CardLabel(c,Rotation.ABOUT_CENTER,true), BorderLayout.SOUTH);
		}
		else if(aTopPlayer.getPlayer() == pPlayer  && c != null){
			aCurrentTrick.add(new CardLabel(c,Rotation.ABOUT_CENTER,true), BorderLayout.NORTH);
		}
		else if(aLeftPlayer.getPlayer() == pPlayer  && c != null){
			aCurrentTrick.add(new CardLabel(c,Rotation.ABOUT_CENTER,true), BorderLayout.WEST);
		}
		else if(aRightPlayer.getPlayer() == pPlayer  && c != null){
			aCurrentTrick.add(new CardLabel(c,Rotation.ABOUT_CENTER,true), BorderLayout.EAST);
		}
		else{
			assert pPlayer == null;
		}
		aCurrentTrick.validate();
		aCurrentTrick.repaint();
	}
	
	public void resetCurrentTrick(){
		aCurrentTrick.removeAll();
	}
	
	public void updateCardPanels(){
		aBottomPlayer.updatehand();
		aTopPlayer.updatehand();
		aRightPlayer.updatehand();
		aLeftPlayer.updatehand();
	}
	
	public void updateCardPanel(APlayer pPlayer){
		if(aBottomPlayer.getPlayer() == pPlayer){
			aBottomPlayer.updatehand();
		}
		else if(aTopPlayer.getPlayer() == pPlayer){
			aTopPlayer.updatehand();
		}
		else if(aLeftPlayer.getPlayer() == pPlayer){
			aLeftPlayer.updatehand();
		}
		else if(aRightPlayer.getPlayer() == pPlayer){
			aRightPlayer.updatehand();
		}
		else{
			assert pPlayer == null;
		}
	}

	public void updateTrickCount(APlayer pPlayer){
		if(aBottomPlayer.getPlayer() == pPlayer){
			aBottomPlayer.addTrick();
		}
		else if(aTopPlayer.getPlayer() == pPlayer){
			aTopPlayer.addTrick();
		}
		else if(aLeftPlayer.getPlayer() == pPlayer){
			aLeftPlayer.addTrick();
		}
		else if(aRightPlayer.getPlayer() == pPlayer){
			aRightPlayer.addTrick();
		}
		else{
			assert pPlayer == null;
		}
	}
	
	public void resetTricksCount(){
			aBottomPlayer.clearTricks();
			aTopPlayer.clearTricks();
			aLeftPlayer.clearTricks();
			aRightPlayer.clearTricks();
	}
	
	public void updateBid(Bid contract)
	{
		aContract.setBid(contract);
		aContract.redraw();
	}
	
	private void hack(){
		//TODO:Try not to use this.
		aCurrentTrick.removeAll();
		aCurrentTrick.add(new CardLabel(null,Rotation.ABOUT_CENTER,true), BorderLayout.SOUTH);
		aCurrentTrick.add(new CardLabel(null,Rotation.ABOUT_CENTER,true), BorderLayout.NORTH);
		aCurrentTrick.add(new CardLabel(null,Rotation.ABOUT_CENTER,true), BorderLayout.WEST);
		aCurrentTrick.add(new CardLabel(null,Rotation.ABOUT_CENTER,true), BorderLayout.EAST);
		aCurrentTrick.validate();
		aCurrentTrick.repaint();
	}
}
