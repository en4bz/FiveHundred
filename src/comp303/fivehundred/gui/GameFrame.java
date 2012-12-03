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
	public static Color BACKGROUND_COLOR = new Color(40, 160, 20);
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

	//public static Logger log = Logger.getRootLogger();
	private final static Logger log = Logger.getLogger(GameFrame.class);
	private static boolean aIsLogging = true;

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
		// Build basic frame
		setTitle("Five Hundred");
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setLocation(5, 5); // Top-left corner of the screen

		// Add Menu
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
		log.info("Starting new game set.");
		Team[] lTeams = aPlayerMenu.getTeams();
		System.out.println(lTeams[0]);
		System.out.println(lTeams[1]);

		aPracticeModeOn = aPlayerMenu.isPracticeModeOn();
		aGamesLeft = aPlayerMenu.getNumberOfGamesToPlay();

		// make new game engine
		aEngine = new GameEngine(lTeams[0], lTeams[1]);

		this.remove(aPlayerMenu);
		aBoard = new GameBoard(lTeams);
		this.add(aBoard);
		aActionToolbar = new ActionToolbar(this);
		this.add(aActionToolbar);
		this.invalidate();
		this.validate();
		this.repaint();
		log.info("Game Board drawn.");

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
			log.info("Starting new game.");
			aEngine.newGame();
			newDeal();
		}
		else
		{
			log.info("Game Over.");
			aGameStats.printStatistics();
			remove(aBoard);
			remove(aActionToolbar);
			JOptionPane.showMessageDialog(this, MESSAGES.getString("comp303.fivehundred.gui.GameFrame.GameSetOver"));
			aPlayerMenu = new PlayerMenu(this);
			setSize(Toolkit.getDefaultToolkit().getScreenSize()); // Force Full Screen
			pack();
		}
	}

	private void newDeal()
	{
		log.info("Starting new deal.");
		aEngine.deal();
		newBid();
	}

	private void newBid()
	{
		log.info("Starting new bid.");
		aEngine.bid();
	}

	private void play()
	{
		log.info("Play!");
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
		if (pNotification.getType().equals("game.engine"))
		{
			setTitle(pNotification.getMessage());

			GameEngine lEngine = (GameEngine) (pNotification.getSource());
			switch (GameEngine.State.valueOf(pNotification.getMessage()))
			{
			case newGame:
				log.info("Resetting game board scores.");
				aBoard.resetScores();
				if (aPracticeModeOn)
				{
					aBoard.setPracticeMode();
				}
				break;
			case newDeal:
				log.info("Updating card panels.");
				aBoard.updateCardPanels();
				log.info("Resetting tricks count.");
				aBoard.resetTricksCount();
				aBoard.setBiddingCentral();
				sleep();
				break;
			case newBid:
				aBoard.updateBids(lEngine.getCurrentPlayer(), aEngine.getBids());
				sleep();
				break;
			case allPasses:
				log.info("All passes!");
				newDeal();
				break;
			case newContract:
				log.info("New contract.");
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
				log.info("Human player is prompted to play.");
				if (aAutoPlay)
				{
					log.info("Autoplaying card.");
					aPlayedCard = aPlayingStrategy.play(aHuman.getTrick(), aHuman.getHand());
					sync();
					notifyObservers(new Notification("gui.gameframe", this, getNotificationSequenceNumber(),
							GameFrame.Human.playValidated.toString()));
				}
				else
				{
					aPlayableCards = aHuman.getPlayableCards();
					log.info("Human can play: " + aPlayableCards.toString());

					notifyObservers(new Notification("gui.gameframe", this, getNotificationSequenceNumber(),
							GameFrame.Human.playPrompt.toString()));
				}
				aCurrentPrompt = PromptState.play;

				break;
			case playDone:
				log.info("Human has played the card");
				aCurrentPrompt = PromptState.none;
				aPlayedCard = null;
				aPlayableCards = null;
				break;
			case bidDone:
				log.info("Human has bid.");
				aCurrentPrompt = PromptState.none;
				aSelectedBid = null;
				break;
			case bidPrompt:
				if (aCurrentPrompt != PromptState.bid)
					log.info("Human player is prompted to bid.");
				aCurrentPrompt = PromptState.bid;
				aPreviousBids = aHuman.getPreviousBids();
				if (aAutoPlay)
				{
					log.info("Autoplaying bid.");
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
					log.info("Human player is prompted to discard.");
				aCurrentPrompt = PromptState.discard;
				if (aAutoPlay)
				{
					log.info("Autoplaying discard.");
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
				log.info("Human has discarded cards.");
				aCurrentPrompt = PromptState.none;
				aDiscardedCards = null;
				break;
			default:
				break;
			}
		}
		if (pNotification.getType().equals("gui.actiontoolbar"))
		{
			log.info(pNotification.getMessage());

			ActionToolbar lBar = (ActionToolbar) (pNotification.getSource());
			switch (GameFrame.Human.valueOf(pNotification.getMessage()))
			{
			case bidDone:
				log.info("Human has clicked on \"Bid\".");
				if (aCurrentPrompt == PromptState.bid)
				{
					aSelectedBid = lBar.geBid();
					if (aPreviousBids.length == 0 || aSelectedBid.isPass()
							|| aSelectedBid.compareTo(aPreviousBids[aPreviousBids.length - 1]) > 0)
					{
						log.info("Bid valid.");
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
				log.info("Human has clicked on \"Discard\"");
				if (aCurrentPrompt == PromptState.discard)
				{

					if (getDiscardedCards() != null && getDiscardedCards().size() == 6)
					{

						log.info("Discard valid.");
						aDiscardDone = true;
						notifyObservers(new Notification("gui.gameframe", this, getNotificationSequenceNumber(),
								GameFrame.Human.discardValidated.toString()));
						sync();

						aCurrentPrompt = PromptState.none;
					}
					else
					{
						log.info("Discard invalid.");
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
				log.info("Human player has clicked a card.");
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
						log.info("Invalid card played.");

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

	public static void log(String message)
	{
		if (aIsLogging)
			log.info(message);
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
		log.info("Synchronizing with HumanPlayer...");
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
