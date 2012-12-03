package comp303.fivehundred.gui;

import java.awt.Component;
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
	private final static Font SCORE_FONT = new Font("Serif", Font.BOLD, 56);
	private static int bidCounter = 0;
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
		bidCounter = 0;
		aTeams = pPlayer;
		rearrangeTeams();

		aNSTeam = pPlayer[0].getPlayers();
		aEWTeam = pPlayer[1].getPlayers();
		
		aCenter= new JPanel(new FlowLayout());
		aCenter.setOpaque(false);
		//Sub-Panels of game board
		
		aWidow = new WidowPanel(false);	
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
		aScore_NS.setOpaque(false);
		
		aScore_EW = new JPanel(new FlowLayout());
		aScore_EW.setBorder(BorderFactory.createTitledBorder(new EmptyBorder(0,0,0,0), "Score East-West Team", TitledBorder.CENTER, TitledBorder.BELOW_BOTTOM));
		aScore_EW.setOpaque(false);
		
		this.resetScores();
		
		aCurrentTrick = new TrickPanel();
		//Set position of each element in board (using coordinates)
		
		GridBagConstraints p1 = getPlayerTrickConstraints(1, 0);
		GridBagConstraints p2 = getPlayerTrickConstraints(2, 1);
		GridBagConstraints p3 = getPlayerTrickConstraints(1, 2);
		GridBagConstraints p4 = getPlayerTrickConstraints(0, 1);
		GridBagConstraints con = getPlayerTrickConstraints(2, 0);
		GridBagConstraints s_EW = getPlayerTrickConstraints(0, 2, GridBagConstraints.LINE_START);
		GridBagConstraints s_NS = getPlayerTrickConstraints(2, 2, GridBagConstraints.LINE_END);
		GridBagConstraints g = getPlayerTrickConstraints(1, 1);

		//add JPanels to the board
	// 	this.add(aWidow,w);
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
		//newline
		
	}

	private GridBagConstraints getPlayerTrickConstraints(int x, int y)
	{
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.fill = GridBagConstraints.HORIZONTAL;
		return c;
	}
	
	private GridBagConstraints getPlayerTrickConstraints(int x, int y, int anchor)
	{
		GridBagConstraints c = getPlayerTrickConstraints(x,y);
		c.anchor = anchor;
		return c;
	}
	
	protected void addActionToolBar(ActionToolbar lActionToolbar)
	{
		GridBagConstraints lActionToolbarConstraints = new GridBagConstraints();
		lActionToolbarConstraints.gridx = 0;
		lActionToolbarConstraints.gridy = 3;
		lActionToolbarConstraints.gridwidth = this.getWidth();
		lActionToolbarConstraints.fill = GridBagConstraints.HORIZONTAL;
		lActionToolbarConstraints.anchor = GridBagConstraints.PAGE_END;
		lActionToolbarConstraints.ipady = 0;
		lActionToolbarConstraints.weighty = 0.5;
		this.add(lActionToolbar, lActionToolbarConstraints);
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
		aContract.setBorder(BorderFactory.createTitledBorder(new EmptyBorder(0,0,0,0), "Contractor: " + pContractor.getName(), TitledBorder.CENTER, TitledBorder.BELOW_TOP));
		if(pContractor == aBottomPlayer.getPlayer() || ((GameFrame)this.getTopLevelAncestor()).inPracticeMode() ){
			aWidow.setVisibility(true);
		}
		else{
			aWidow.setVisibility(false);
		}
		aWidow.updateWidow(pWidow);
	}
	
	public void updateCardPanels(){
		aBottomPlayer.updatehand();
		aTopPlayer.updatehand();
		aRightPlayer.updatehand();
		aLeftPlayer.updatehand();
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
	
	public void setWidowCentral(){
		aCenter.removeAll();
		aCenter.add(aWidow);
		aCenter.validate();
		aCenter.repaint();
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
