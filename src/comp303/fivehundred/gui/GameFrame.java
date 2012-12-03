package comp303.fivehundred.gui;

import java.awt.Color;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.management.Notification;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;



import org.apache.log4j.Logger;

import comp303.fivehundred.ai.BasicBiddingStrategy;
import comp303.fivehundred.ai.BasicCardExchangeStrategy;
import comp303.fivehundred.ai.BasicPlayingStrategy;
import comp303.fivehundred.ai.IBiddingStrategy;
import comp303.fivehundred.ai.ICardExchangeStrategy;
import comp303.fivehundred.ai.IPlayingStrategy;
import comp303.fivehundred.engine.GameEngine;
import comp303.fivehundred.engine.GameLogger;
import comp303.fivehundred.engine.GameStatistics;
import comp303.fivehundred.model.Bid;
import comp303.fivehundred.mvc.IObservable;
import comp303.fivehundred.mvc.Observer;
import comp303.fivehundred.player.HumanPlayer;
import comp303.fivehundred.player.Team;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

@SuppressWarnings("serial")
public class GameFrame extends JFrame implements Observer, IObservable
{
	public static Color BG_COLOR = Color.GREEN;
	public static Card nextCard;
	private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("comp303.fivehundred.gui.MessageBundle");
	private GameEngine aEngine;
	private GameStatistics aGameStats;
	private GameLogger aGameLogger;
	
	private PlayerMenu aPlayerMenu;
	private ActionToolbar aActionToolbar;
	private GameBoard aBoard;
	public static int WIDTH = 900;
	public static int HEIGHT = 600;
	private int aGamesLeft = 10;
	private boolean aPracticeModeOn = true;

	private final static Logger log = Logger.getLogger(GameFrame.class);

	private int aSpeed = 500;

	// MVC-related fields
	private ArrayList<Observer> aObservers = new ArrayList<Observer>();
	private long aNotificationSequenceNumber = 0;

	// Human player fields
	private PromptState aCurrentPrompt = PromptState.none;
	private HumanPlayer aHuman;
	private CardList aPlayableCards;
	private Card aPlayedCard = null;
	private Bid aSelectedBid = null;
	private Bid[] aPreviousBids = null;
	private CardList aDiscardedCards = null;
	private boolean aDiscardDone = false;

	// AutoPlay mode
	private boolean aAutoPlay = false;
	private IBiddingStrategy aBiddingStrategy = new BasicBiddingStrategy();
	private ICardExchangeStrategy aCardExchangeStrategy = new BasicCardExchangeStrategy();
	private IPlayingStrategy aPlayingStrategy = new BasicPlayingStrategy();

	public GameFrame()
	{
		/**
		 * TO TURN LOGGING ON COMMENT THE THE NEXT LINE
		 */
		Logger.getRootLogger().removeAllAppenders();

		setTitle("Five Hundred");
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setLocation(5, 5); // Top-left corner of the screen
		this.setJMenuBar(new Menu()); // add menu to the new window (new game)
		// Start the party
		aPlayerMenu = new PlayerMenu(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.pack();
		this.setVisible(true);
	}

	public static void main(String[] args)
	{
		new GameFrame();
	}

	private void newGameSet()
	{
		log.debug("Starting new game set.");
		Team[] lTeams = aPlayerMenu.getTeams();

		aPracticeModeOn = aPlayerMenu.isPracticeModeOn();
		aGamesLeft = aPlayerMenu.getNumberOfGamesToPlay();

		// make new game engine
		aEngine = new GameEngine(lTeams[0], lTeams[1]);

		this.remove(aPlayerMenu);
		aBoard = new GameBoard(lTeams);
		aBoard.setBackground(BG_COLOR);
		this.add(aBoard);
		aActionToolbar = new ActionToolbar(this);
		this.add(aActionToolbar);
		this.invalidate();
		this.validate();
		this.repaint();
		log.debug("Game Board drawn.");

		aEngine.addObserver(this);
		aGameStats = new GameStatistics();
		aGameLogger = new GameLogger();
		aEngine.addObserver(aGameStats);
		aEngine.addObserver(aGameLogger);

		newGame();
	}

	private void newGame()
	{
		if (aGamesLeft > 0)
		{
			log.debug("Starting new game.");
			aEngine.newGame();
			newDeal();
		}
		else
		{
			log.debug("Game Over.");
			aGameStats.printStatistics();
			aGameStats.statString();
			remove(aBoard);
			remove(aActionToolbar);
			JOptionPane.showMessageDialog(this, MESSAGES.getString("comp303.fivehundred.gui.GameFrame.GameSetOver"));
			aPlayerMenu = new PlayerMenu(this);
			setSize(Toolkit.getDefaultToolkit().getScreenSize()); // Force Full Screen
			pack();
		}
	}
	
	protected void resetFrame()
	{
		Thread t = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				aPlayerMenu = new PlayerMenu(this);
				setSize(Toolkit.getDefaultToolkit().getScreenSize()); // Force Full Screen
				pack();
			}
		});
		t.start();
	}

	private void newDeal()
	{
		log.debug("Starting new deal.");
		aEngine.deal();
		log.debug("Starting new bid.");
		aEngine.bid();
	}

	private void play()
	{
		log.debug("Play!");
		aEngine.exchange();
		aEngine.playRound();
		aEngine.computeScore();
		if (aEngine.isGameOver())
		{
			aGamesLeft--;
			newGame();
		}
		else
		{
			newDeal();
		}
	}

	@Override
	public void update(Notification pNotification)
	{
		if (pNotification.getType().equals("gui.playermenu"))
		{
			State lState = State.valueOf(pNotification.getMessage());
			switch (lState)
			{
			case newGameSet:
			
				Thread t = new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						newGameSet();
					}
				});
				t.start();

				break;
			default:
				break;
			}
		}
		if (pNotification.getType().equals("stats.show")){
			JOptionPane.showMessageDialog(this, this.aGameStats.statString());
		}
		if (pNotification.getType().equals("stats.reset")){
			JOptionPane.showMessageDialog(this, "Game statistics have been reset.");
			this.aGameStats.resetStats();
		}
		if (pNotification.getType().equals("game.engine"))
		{
			GameEngine lEngine = (GameEngine) (pNotification.getSource());
			switch (GameEngine.State.valueOf(pNotification.getMessage()))
			{
			case newGame:
				log.debug("Resetting game board scores.");
				aBoard.resetScores();
				if (aPracticeModeOn)
				{
					aBoard.setPracticeMode();
				}
				break;
			case newDeal:
				log.debug("Updating card panels.");
				aBoard.updateCardPanels();
				log.debug("Resetting tricks count.");
				aBoard.resetTricksCount();
				aBoard.setBiddingCentral();
				sleep();
				break;
			case newBid:
				aBoard.updateBids(lEngine.getCurrentPlayer(), aEngine.getBids());
				sleep();
				break;
			case allPasses:
				log.debug("All passes!");
				newDeal();
				break;
			case newContract:
				log.debug("New contract.");
				aBoard.updateWidow(lEngine.getWidow(), lEngine.getContractor());
				aBoard.setWidowCentral();
				aBoard.setContract(lEngine.getContract());
				sleep();
				play();
				sleep();
				break;
			case cardsDiscarded:
				aBoard.updateWidow(lEngine.getWidow(), lEngine.getContractor());
				aBoard.updateCardPanels();
				aBoard.setPlayingCentral();
				sleep();
				break;
			case cardPlayed:
				aBoard.updateCardPanels();
				aBoard.updateTrick(lEngine.getCurrentPlayer(), ((GameEngine) pNotification.getSource()).getCardPlayed());
				sleep();
				break;
			case newTrick:
				aBoard.resetCurrentTrick();
				sleep();
				break;
			case trickWon:
				aBoard.updateTrickCount(lEngine.getTrickWinner());
				aBoard.resetCurrentTrick();
				sleep();
				break;
			case roundEnd:
				aBoard.updateScores();
				sleep();
				break;
			case gameOver:
				break;
			default:
				break;
			}
		}
		if (pNotification.getType().equals("gui.humanplayer"))
		{

			aHuman = (HumanPlayer) (pNotification.getSource());
			this.aActionToolbar.setAutoPlayEnabled(true);
			switch (GameFrame.Human.valueOf(pNotification.getMessage()))
			{
			case playPrompt:
				log.debug("Human player is prompted to play.");
				if (aAutoPlay)
				{
					log.debug("Autoplaying card.");
					aPlayedCard = aPlayingStrategy.play(aHuman.getTrick(), aHuman.getHand());
					sync();
					notifyObservers(new Notification("gui.gameframe", this, getNotificationSequenceNumber(),
							GameFrame.Human.playValidated.toString()));
				}
				else
				{
					aPlayableCards = aHuman.getPlayableCards();
					log.debug("Human can play: " + aPlayableCards.toString());

					notifyObservers(new Notification("gui.gameframe", this, getNotificationSequenceNumber(),
							GameFrame.Human.playPrompt.toString()));
				}
				aCurrentPrompt = PromptState.play;

				break;
			case playDone:
				log.debug("Human has played the card");
				aCurrentPrompt = PromptState.none;
				aPlayedCard = null;
				aPlayableCards = null;
				break;
			case bidDone:
				log.debug("Human has bid.");
				aCurrentPrompt = PromptState.none;
				aSelectedBid = null;
				break;
			case bidPrompt:
				if (aCurrentPrompt != PromptState.bid)
					log.debug("Human player is prompted to bid.");
				aCurrentPrompt = PromptState.bid;
				aPreviousBids = aHuman.getPreviousBids();
				if (aAutoPlay)
				{
					log.debug("Autoplaying bid.");
					aSelectedBid = aBiddingStrategy.selectBid(aPreviousBids, aHuman.getHand());
					notifyObservers(new Notification("gui.gameframe", this, getNotificationSequenceNumber(),
							GameFrame.Human.bidValidated.toString()));
					sync();
					aCurrentPrompt = PromptState.none;
				}
				else
				{
					notifyObservers(new Notification("gui.gameframe", this, getNotificationSequenceNumber(),
							GameFrame.Human.bidPrompt.toString()));
				}
				break;
			case discardPrompt:
				if (aCurrentPrompt != PromptState.discard)
					log.debug("Human player is prompted to discard.");
				aCurrentPrompt = PromptState.discard;
				if (aAutoPlay)
				{
					log.debug("Autoplaying discard.");
					aDiscardedCards = aCardExchangeStrategy.selectCardsToDiscard(aHuman.getPreviousBids(),
							aHuman.getIndex(), aHuman.getHand());
					aDiscardDone = true;

					notifyObservers(new Notification("gui.gameframe", this, getNotificationSequenceNumber(),
							GameFrame.Human.discardValidated.toString()));
					sync();
					aCurrentPrompt = PromptState.none;

				}
				else
				{
					notifyObservers(new Notification("gui.gameframe", this, getNotificationSequenceNumber(),
							GameFrame.Human.discardPrompt.toString()));
				}
				break;
			case discardDone:
				log.debug("Human has discarded cards.");
				aCurrentPrompt = PromptState.none;
				aDiscardedCards = null;
				break;
			default:
				break;
			}
		}
		if (pNotification.getType().equals("gui.actiontoolbar"))
		{
			log.debug(pNotification.getMessage());

			ActionToolbar lBar = (ActionToolbar) (pNotification.getSource());
			switch (GameFrame.Human.valueOf(pNotification.getMessage()))
			{
			case bidDone:
				log.debug("Human has clicked on \"Bid\".");
				if (aCurrentPrompt == PromptState.bid)
				{
					aSelectedBid = lBar.geBid();
					if (aPreviousBids.length == 0 || aSelectedBid.isPass()
							|| aSelectedBid.compareTo(aPreviousBids[aPreviousBids.length - 1]) > 0)
					{
						log.debug("Bid valid.");
						aSelectedBid = lBar.geBid();
						notifyObservers(new Notification("gui.gameframe", this, getNotificationSequenceNumber(),
								GameFrame.Human.bidValidated.toString()));
						synchronized (aHuman)
						{
							aHuman.notify();
						}
						aCurrentPrompt = PromptState.none;

					}
					else
					{
						// TODO send invalidBid notification
						aSelectedBid = null;
					}
				}
				break;
			case discardDone:
				log.debug("Human has clicked on \"Discard\"");
				if (aCurrentPrompt == PromptState.discard)
				{

					if (getDiscardedCards() != null && getDiscardedCards().size() == 6)
					{

						log.debug("Discard valid.");
						aDiscardDone = true;
						notifyObservers(new Notification("gui.gameframe", this, getNotificationSequenceNumber(),
								GameFrame.Human.discardValidated.toString()));
						sync();

						aCurrentPrompt = PromptState.none;
					}
					else
					{
						log.debug("Discard invalid.");
						JOptionPane.showMessageDialog(this,
								MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.InvalidDiscardError"));
						aCurrentPrompt = PromptState.discard;
					}
				}
				break;

			default:
				break;
			}
		}
		if (pNotification.getType().equals("gui.cardLabel"))
		{

			CardLabel lCardLabel = (CardLabel) (pNotification.getSource());
			switch (GameFrame.Human.valueOf(pNotification.getMessage()))
			{
			case cardClicked:
				log.debug("Human player has clicked a card.");
				if (aCurrentPrompt == PromptState.play)
				{
					if (aPlayableCards.contains(lCardLabel.getCard()))
					{
						aPlayedCard = lCardLabel.getCard();
						notifyObservers(new Notification("gui.gameframe", this, getNotificationSequenceNumber(),
								GameFrame.Human.playValidated.toString()));
						synchronized (aHuman)
						{
							aHuman.notify();
						}
						aCurrentPrompt = PromptState.none;
					}
					else
					{
						log.debug("Invalid card played.");

						JOptionPane.showMessageDialog(this,
								MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.InvalidPlayError"));
						aPlayedCard = null;
					}
				}
				if (aCurrentPrompt == PromptState.discard)
				{
					if (aDiscardedCards == null)
					{
						aDiscardedCards = new CardList();
					}
					if (lCardLabel.getVisibility())
						aDiscardedCards.remove(lCardLabel.getCard());
					else
					{
						aDiscardedCards.add(lCardLabel.getCard());
					}
					if (aDiscardedCards.size() == 6)
					{
						this.aActionToolbar.setDiscardEnabled(true);
					}
					else
					{
						this.aActionToolbar.setDiscardEnabled(false);
					}
				}
				break;
			default:
				break;
			}
		}
	}

	public boolean isDiscardDone()
	{
		return aDiscardDone;
	}

	public CardList getDiscardedCards()
	{
		return aDiscardedCards;
	}

	public Card getPlayedCard()
	{
		return aPlayedCard;
	}

	private void sleep()
	{
		try
		{
			Thread.sleep(aSpeed);
		}
		catch (InterruptedException e)
		{
		}
	}

	GameEngine getGameEngine()
	{
		return aEngine;
	}

	public enum State
	{
		displayMenu, play, newGameSet, newGame, newDeal, newBid, newContract, cardsDiscarded, cardPlayed, newTrick, trickWon, roundEnd, gameOver
	}

	public enum PromptState
	{
		bid, discard, play, none
	}

	public PromptState getCurrentPrompt()
	{
		return aCurrentPrompt;
	}

	public enum Human
	{
		playDone, playPrompt, playValidated, discardDone, discardPrompt, discardValidated, bidDone, bidPrompt, bidValidated, cardClicked
	}

	protected void setSpeed(int pSpeed)
	{
		aSpeed = pSpeed;
	}

	protected int getSpeed()
	{
		return aSpeed;
	}

	protected void setAutoPlay(boolean lBit)
	{
		if (lBit)
		{
			aAutoPlay = true;
			sync();
		}
		else
		{
			aAutoPlay = false;
		}
	}
	
	private void sync()
	{
		log.debug("Synchronizing with HumanPlayer...");
		Thread t = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				if (aHuman != null)
				{
					synchronized (aHuman)
					{
						aHuman.notify();
					}
				}			}
		});
		t.start();

	}

	@Override
	public void addObserver(Observer pObserver)
	{
		if (!aObservers.contains(pObserver))
		{
			aObservers.add(pObserver);
		}
	}

	@Override
	public void notifyObservers(Notification pNotification)
	{
		for (Observer observer : aObservers)
		{
			observer.update(pNotification);
		}
	}

	public long getNotificationSequenceNumber()
	{
		return aNotificationSequenceNumber++;
	}

	public Bid getSelectedBid()
	{
		return aSelectedBid;
	}

	public boolean inPracticeMode()
	{
		return aPracticeModeOn;
	}

}
