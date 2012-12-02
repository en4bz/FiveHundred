package comp303.fivehundred.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.player.APlayer;
import comp303.fivehundred.player.HumanPlayer;
import comp303.fivehundred.player.Team;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

/**
 * Board of the game (new board for every new game)
 * @author Gabrielle Germain
 */

@SuppressWarnings("serial")
public class GameBoard extends JPanel
{
	private final static int XPAD = 0;
	private final static int YPAD = 0;
	private final static Font SCORE_FONT = new Font("Serif", Font.BOLD, 56);
	
	Team[] aTeams;
	
	final APlayer[] aNSTeam;
	final APlayer[] aEWTeam;
	
	final ContractPanel aContract;
	final JPanel aCenter;
	final WidowPanel aWidow;
	final TrickPanel aCurrentTrick;
	final BiddingPanel aBidding;
	
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
		rearrangeTeams();

		aNSTeam = pPlayer[0].getPlayers();
		aEWTeam = pPlayer[1].getPlayers();
		
		aCenter= new JPanel(new FlowLayout());
		aCenter.setOpaque(false);
		//Sub-Panels of game board
		
		aWidow = new WidowPanel();	
		aBidding = new BiddingPanel();
		
		aTopPlayer = new PlayerArea(aNSTeam[0], Rotation.UPSIDE_DOWN, false);
		aTopPlayer.setBorder(BorderFactory.createTitledBorder(new EmptyBorder(0,0,0,0), aNSTeam[0].getName(), TitledBorder.CENTER, TitledBorder.BELOW_TOP));
		aRightPlayer = new PlayerArea(aEWTeam[1], Rotation.LEFT, false);
		aRightPlayer.setBorder(BorderFactory.createTitledBorder(new EmptyBorder(0,0,0,0), aEWTeam[1].getName(), TitledBorder.CENTER, TitledBorder.BELOW_TOP));
		
		aBottomPlayer = new PlayerArea(aNSTeam[1], Rotation.DEFAULT, true);
		aBottomPlayer.setBorder(BorderFactory.createTitledBorder(new EmptyBorder(0,0,0,0), aNSTeam[1].getName(), TitledBorder.CENTER, TitledBorder.BELOW_BOTTOM));
		
		aLeftPlayer = new PlayerArea(aEWTeam[0], Rotation.RIGHT, false);
		aLeftPlayer.setBorder(BorderFactory.createTitledBorder(new EmptyBorder(0,0,0,0), aEWTeam[0].getName(), TitledBorder.CENTER, TitledBorder.BELOW_TOP));
		
		aContract = new ContractPanel();
		aContract.setBorder(BorderFactory.createTitledBorder(new EmptyBorder(0,0,0,0), "Contractor: ", TitledBorder.CENTER, TitledBorder.BELOW_TOP));
		
		aScore_NS = new JPanel(new FlowLayout());
		aScore_NS.setBorder(BorderFactory.createTitledBorder(new EmptyBorder(0,0,0,0), "Score North-South Team", TitledBorder.CENTER, TitledBorder.BELOW_BOTTOM));
		aScore_NS.setPreferredSize(new Dimension(200,200));
		aScore_NS.setOpaque(false);
		
		aScore_EW = new JPanel(new FlowLayout());
		aScore_EW.setBorder(BorderFactory.createTitledBorder(new EmptyBorder(0,0,0,0), "Score East-West Team", TitledBorder.CENTER, TitledBorder.BELOW_BOTTOM));
		aScore_EW.setPreferredSize(new Dimension(200,200));
		aScore_EW.setOpaque(false);
		
		this.resetScores();
		
		aCurrentTrick = new TrickPanel();
		aCurrentTrick.setVisible(false);
		
		//Set position of each element in board (using coordinates)
		
		GridBagConstraints w = new GridBagConstraints();
		w.gridx = 0;
		w.gridy = 0;
		w.ipadx = XPAD;
		w.ipady = YPAD;
		w.fill = GridBagConstraints.BOTH;
		w.fill = GridBagConstraints.CENTER;
		
		GridBagConstraints p1 = new GridBagConstraints();
		p1.gridx = 1;
		p1.gridy = 0;
		p1.ipadx = XPAD;
		p1.ipady = YPAD;
		p1.fill = GridBagConstraints.BOTH;
		p1.fill = GridBagConstraints.CENTER;
		
		GridBagConstraints p2 = new GridBagConstraints();
		p2.gridx = 2;
		p2.gridy = 1;
		p2.ipadx = XPAD;
		p2.ipady = YPAD;
		p2.fill = GridBagConstraints.BOTH;
		p2.fill = GridBagConstraints.CENTER;
		
		GridBagConstraints p3 = new GridBagConstraints();
		p3.gridx = 1;
		p3.gridy = 2;
		p3.ipadx = XPAD;
		p3.ipady = YPAD;
		p3.fill = GridBagConstraints.BOTH;
		p3.fill = GridBagConstraints.CENTER;
		
		GridBagConstraints p4 = new GridBagConstraints();
		p4.gridx = 0;
		p4.gridy = 1;
		p4.ipadx = XPAD;
		p4.ipady = YPAD;
		p4.fill = GridBagConstraints.BOTH;
		p4.fill = GridBagConstraints.CENTER;
		
		GridBagConstraints con = new GridBagConstraints();
		con.gridx = 2;
		con.gridy = 0;
		con.ipadx = XPAD;
		con.ipady = YPAD;
		con.fill = GridBagConstraints.BOTH;
		con.fill = GridBagConstraints.CENTER;
		
		GridBagConstraints s_EW = new GridBagConstraints();
		s_EW.gridx = 0;
		s_EW.gridy = 2;
		s_EW.ipadx = XPAD;
		s_EW.ipady = YPAD;
		s_EW.fill = GridBagConstraints.BOTH;
		s_EW.fill = GridBagConstraints.CENTER;
		s_EW.anchor = GridBagConstraints.LINE_START;
		
		GridBagConstraints s_NS = new GridBagConstraints();
		s_NS.gridx = 2;
		s_NS.gridy = 2;
		s_NS.ipadx = XPAD;
		s_NS.ipady = YPAD;
		s_NS.fill = GridBagConstraints.BOTH;
		s_NS.fill = GridBagConstraints.CENTER;
		s_NS.anchor = GridBagConstraints.LINE_END;
		
		GridBagConstraints g = new GridBagConstraints();
		g.gridx = 1;
		g.gridy = 1;
		g.ipadx = XPAD;
		g.ipady = YPAD;
		g.fill = GridBagConstraints.BOTH;
		g.fill = GridBagConstraints.CENTER;
		
//		//add a button to submit a trick
//		JPanel submission = new JPanel();
//		submission.setBorder(BorderFactory.createTitledBorder("Submit the trick"));
//
//		JButton submit = new JButton("submit");
//		submit.addActionListener(new ActionListener()
//		{
//			@Override
//			public void actionPerformed(ActionEvent e)
//			{
//				//Submit new info
//			}
//		});
//		
//		submission.add(submit);
//		///////////////////////////
		
		
		//add JPanels to the board
		this.add(aWidow,w);
		this.add(aTopPlayer,p1);
		this.add(aContract,con);
		//new line
		this.add(aLeftPlayer,p4);
		this.add(aCenter,g);
		this.add(aRightPlayer,p2);
		//new line
		this.add(aScore_EW, s_EW);
		this.add(aBottomPlayer, p3);
		this.add(aScore_NS,s_NS);
		//end
		aCenter.add(aCurrentTrick);
	}
	
	/**
	 * Helper method to place the human player (if there is one) at the bottom of the game board.
	 * @pre there cannot be two human players (should be taken care of during input validation)
	 */
	private void rearrangeTeams()
	{
		HumanPlayer lHuman = null;
		
		// Check to see if a player is human
		for(Team lTeam: aTeams)
		{
			for(APlayer p: lTeam.getPlayers())
			{
				if (p instanceof HumanPlayer)
				{
					lHuman = (HumanPlayer) p;
				}
			}
		}
		
		if(lHuman == null)
		{
			return;
		}
		
		
		// rearrange teams such that the human is in aTeams[0]
		if(aTeams[1].isInTeam(lHuman))
		{
			Team lTmp = aTeams[0];
			aTeams[0] = aTeams[1];
			aTeams[1] = lTmp;
		}
		
		// rearrange aTeams[0] such that player is at index 1
		if(aTeams[0].getPlayers()[0] == lHuman) // should be same reference
		{
			APlayer[] lPlayers = aTeams[0].getPlayers();
			aTeams[0] = new Team(lPlayers[1], lPlayers[0]);
		}
		
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
	
	public void setContract(Bid pContract){
		aContract.setBid(pContract);
		aContract.revalidate();
		aContract.repaint();
	}
	public void resetCurrentTrick(){
		aCurrentTrick.reset();
	}
	
	public void updateWidow(CardList pWidow, APlayer pContractor){
		aCurrentTrick.setVisible(true);
		aContract.setBorder(BorderFactory.createTitledBorder(new EmptyBorder(0,0,0,0), "Contractor: " + pContractor.getName(), TitledBorder.CENTER, TitledBorder.BELOW_TOP));
		aWidow.updateWidow(pWidow);
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
	
	public void setBiddingCentral(){
		aCenter.removeAll();
		aBidding.reset();
		aCenter.add(aBidding);
		aCenter.revalidate();
		aCenter.repaint();
	}
	
	public void setPlayingCentral(){
		aCenter.removeAll();
		aCurrentTrick.reset();
		aCenter.add(aCurrentTrick);
		aCenter.revalidate();
		aCenter.repaint();
	}
	
	public void resetScores(){
		aScore_EW.removeAll();
		aScore_NS.removeAll();
		JLabel lZero1 = new JLabel("" + 0);
		lZero1.setFont(SCORE_FONT);
		JLabel lZero2 = new JLabel("" + 0);
		lZero2.setFont(SCORE_FONT);
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
		lScore1.setFont(SCORE_FONT);
		lScore2.setFont(SCORE_FONT);
		aScore_EW.add(lScore2);
		aScore_NS.add(lScore1);
		
		aScore_EW.validate();
		aScore_EW.repaint();
		
		aScore_NS.validate();
		aScore_NS.repaint();
	}
	
	public void setPracticeMode(){
		aBottomPlayer.setVisibility(true);
		aTopPlayer.setVisibility(true);
		aRightPlayer.setVisibility(true);
		aLeftPlayer.setVisibility(true);
		for(Component c : aWidow.getComponents()){
			((CardLabel) c).setVisibility(true);
		}
	}

	private static int bidCounter = 0;
	
	public void updateBids(APlayer pPlayer, Bid[] bids)
	{
		if(aBottomPlayer.getPlayer() == pPlayer){
			aBidding.setSouth(bids[bidCounter++]);
		}
		else if(aTopPlayer.getPlayer() == pPlayer){
			aBidding.setNorth(bids[bidCounter++]);
		}
		else if(aLeftPlayer.getPlayer() == pPlayer){
			aBidding.setWest(bids[bidCounter++]);
		}
		else if(aRightPlayer.getPlayer() == pPlayer){
			aBidding.setEast(bids[bidCounter++]);
		}
		else{
			assert pPlayer == null;
		}
		if(bidCounter == 4){
			bidCounter = 0;
		}
	}
	
	public void resetBids(){
		aBidding.reset();
	}
}
