package comp303.fivehundred.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import comp303.fivehundred.player.APlayer;
import comp303.fivehundred.player.Team;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;

/**
 * Board of the game (new board for every new game)
 * @author Gabrielle Germain
 */

@SuppressWarnings("serial")
public class GameBoard extends JPanel
{
	final Team[] aTeams;
	
	final APlayer[] aNSTeam;
	final APlayer[] aEWTeam;
	
	final ContractPanel aContract;
	final JPanel aWidow;
	final TrickPanel aCurrentTrick;
	
	final JPanel aScore_NS;
	final JPanel aScore_EW;
	
	final PlayerArea aBottomPlayer;
	final PlayerArea aTopPlayer;
	final PlayerArea aRightPlayer;
	final PlayerArea aLeftPlayer;
	
	public GameBoard(final Team[] pPlayer)
	{
		super(new GridBagLayout());
		this.setBackground(Color.GREEN);
		aTeams = pPlayer;
		aNSTeam = pPlayer[0].getPlayers();
		aEWTeam = pPlayer[1].getPlayers();
		
		//Sub-Panels of game board
		
		aWidow = new JPanel (new GridLayout(2,3));
		aWidow.setOpaque(false);
		for(int i = 0; i < 6; i++){
			aWidow.add(new CardLabel(null,Rotation.ABOUT_CENTER,false));
		}
		aWidow.setBorder(BorderFactory.createTitledBorder("Widow")); //debug
		
		aTopPlayer = new PlayerArea(aNSTeam[0], Rotation.UPSIDE_DOWN, false);
		aTopPlayer.setBorder(BorderFactory.createTitledBorder("Player1: Tricks won+Hand")); //debug
		
		aRightPlayer = new PlayerArea(aEWTeam[1], Rotation.LEFT, false);
		aRightPlayer.setBorder(BorderFactory.createTitledBorder("Player 2: Tricks won+Hand")); //debug
		
		aBottomPlayer = new PlayerArea(aNSTeam[1], Rotation.ABOUT_CENTER, true);
		aBottomPlayer.setBorder(BorderFactory.createTitledBorder("Player 3: Tricks won+Hand")); //debug
		
		aLeftPlayer = new PlayerArea(aEWTeam[0], Rotation.RIGHT, false);
		aLeftPlayer.setBorder(BorderFactory.createTitledBorder("Player4: Tricks won+Hand")); //debug
		
		aContract = new ContractPanel(new Bid(7,Suit.HEARTS));//TODO:Fix this
	
		aContract.setBorder(BorderFactory.createTitledBorder("Contract")); //debug
		
		aScore_NS = new JPanel(new FlowLayout());
		aScore_NS.setBorder(BorderFactory.createTitledBorder("Score North-South Team")); //set title
		aScore_NS.setPreferredSize(new Dimension(200,200));
		aScore_NS.setOpaque(false);
		
		aScore_EW = new JPanel(new FlowLayout());
		aScore_EW.setBorder(BorderFactory.createTitledBorder("Score East-West Team")); //set title
		aScore_EW.setPreferredSize(new Dimension(200,200));
		aScore_EW.setOpaque(false);
		
		this.resetScores();
		
		aCurrentTrick = new TrickPanel();
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
		this.add(aScore_EW, s_EW);
		this.add(aBottomPlayer, p3);
		this.add(aScore_NS,s_NS);
		//end
	}
	
	//"BUILD" METHODS FOR EACH COMPONENT THAT WE USE MORE THAN ONCE
	
	public JLabel textBubble (String aString) //get aString from an observer?
	{
		JLabel bubble = new JLabel(aString, JLabel.CENTER); //the "bubble" is in the middle of the label.
		//add a background?
		return bubble;
	}
	
	public void updateTrick(APlayer pPlayer, Card c){
		if(aBottomPlayer.getPlayer() == pPlayer && c != null){
			aCurrentTrick.setSouth(c);
		}
		else if(aTopPlayer.getPlayer() == pPlayer  && c != null){
			aCurrentTrick.setNorth(c);
		}
		else if(aLeftPlayer.getPlayer() == pPlayer  && c != null){
			aCurrentTrick.setWest(c);
		}
		else if(aRightPlayer.getPlayer() == pPlayer  && c != null){
			aCurrentTrick.setEast(c);
		}
		else{
			assert pPlayer == null;
		}
	}
	
	public void resetCurrentTrick(){
		aCurrentTrick.reset();
	}
	
	public void updateWidow(CardList pWidow){
		aWidow.removeAll();
		for(Card c : pWidow){
			aWidow.add(new CardLabel(c,Rotation.ABOUT_CENTER,true));
		}
		aWidow.validate();
		aWidow.repaint();
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
	
	public void resetScores(){
		aScore_EW.removeAll();
		aScore_NS.removeAll();
		JLabel lZero1 = new JLabel("" + 0);
		lZero1.setFont(new Font("Serif", Font.BOLD, 56));
		JLabel lZero2 = new JLabel("" + 0);
		lZero2.setFont(new Font("Serif", Font.BOLD, 56));
		aScore_EW.add(lZero1);
		aScore_NS.add(lZero2);
		
		aScore_EW.validate();
		aScore_EW.repaint();
		
		aScore_NS.validate();
		aScore_NS.repaint();
	}
	
	public void updateScores(){

		aScore_EW.removeAll();
		aScore_NS.removeAll();
		JLabel lScore1 = new JLabel("" + aTeams[1].getScore());
		JLabel lScore2 = new JLabel("" + aTeams[0].getScore());
		lScore1.setFont(new Font("Serif", Font.BOLD, 56));
		lScore2.setFont(new Font("Serif", Font.BOLD, 56));
		aScore_EW.add(lScore2);
		aScore_NS.add(lScore1);
		
		aScore_EW.validate();
		aScore_EW.repaint();
		
		aScore_NS.validate();
		aScore_NS.repaint();
	}
}
