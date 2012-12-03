package comp303.fivehundred.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.management.Notification;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private GameBoard aBoard;
    public static int WIDTH = 900;
    public static int HEIGHT = 600;
    private int aGamesLeft = 10;
    private boolean aPracticeModeOn = true;

    private final static Logger aLogger = LoggerFactory.getLogger("GameFrameLogger");
    private static boolean aIsLogging = true;

    private int aSpeed = 500;
    private boolean aAutoPlay = false;

    // MVC-related fields
    private ArrayList<Observer> aObservers = new ArrayList<Observer>();
    private long aNotificationSequenceNumber = 0;

    // Human player fields
    private boolean aPlayPrompted = false;
    private boolean aBidPrompted = false;
    private boolean aDiscardPrompted = false;
    private HumanPlayer aHuman;
    private CardList aPlayableCards;
    private Card aPlayedCard = null;
    private Bid aSelectedBid = null;
    private Bid[] aPreviousBids = null;
    private CardList aDiscardedCards = null;

    public GameFrame()
    {
        // Build basic frame
        this.setTitle("Five Hundred");
        this.setLayout(new GridLayout(2,1));
        this.setLocation(5, 5); // Top-left corner of the screen
    //    this.setSize(Toolkit.getDefaultToolkit().getScreenSize()); // Force Full Screen

        // Add Menu
        this.setJMenuBar(new Menu()); // add menu to the new window (new game)

        // Start the party
        aPlayerMenu = new PlayerMenu(this);
        this.add(aPlayerMenu);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.pack();
        this.setVisible(true);
    }

    public static void main(String[] args)
    {
        new GameFrame();
    }

    private void newGameSet()
    {
        log("Starting new game set.");
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
        this.add(new ActionToolbar(this));
        this.invalidate();
        this.validate();
        log("Game Board drawn.");

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
            log("Starting new game.");
            aEngine.newGame();
            newDeal();
        }
        else
        {
            log("Game Over.");
            aGameStats.printStatistics();
            remove(aBoard);
            JOptionPane.showMessageDialog(this, MESSAGES.getString("comp303.fivehundred.gui.GameFrame.GameSetOver"));
            aPlayerMenu = new PlayerMenu(this);
            setSize(Toolkit.getDefaultToolkit().getScreenSize()); // Force Full Screen
            pack();
        }
    }

    private void newDeal()
    {
        log("Starting new deal.");
        aEngine.deal();
        log("Starting new bid.");
        aEngine.bid();
    }

    private void newBid()
    {
        log("Starting new bid.");
        aEngine.bid();
    }

    private void play()
    {
        log("Play!");
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
                log("Resetting game board scores.");
                aBoard.resetScores();
                if (aPracticeModeOn)
                {
                    aBoard.setPracticeMode();
                }
             
                break;
            case newDeal:
                log("Updating card panels.");
                aBoard.updateCardPanels();
                log("Resetting tricks count.");
                aBoard.resetTricksCount();
                aBoard.setBiddingCentral();
                invalidate();
                validate();
                repaint();
                sleep();
                break;
            case newBid:
                aBoard.updateBids(lEngine.getCurrentPlayer(), aEngine.getBids());
                sleep();
                break;
            case allPasses:
                log("All passes!");
                newDeal();
                sleep();
                break;
            case newContract:
                log("New contract.");
                aBoard.updateWidow(lEngine.getWidow(), lEngine.getContractor());
                aBoard.setWidowCentral();
            	aBoard.setContract(lEngine.getContract());
            	invalidate();
                validate();
            	repaint();                
            	play();
            	sleep();
                break;
            case cardsDiscarded:
                aBoard.updateWidow(lEngine.getWidow(), lEngine.getContractor());
                aBoard.updateCardPanel(lEngine.getCurrentPlayer());
                aBoard.setPlayingCentral();
                invalidate();
                validate();
            	repaint();
            	sleep();
                break;
            case cardPlayed:
                aBoard.updateCardPanel(lEngine.getCurrentPlayer());
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
            aPlayableCards = aHuman.getPlayableCards();
            aPreviousBids = aHuman.getPreviousBids();
            
            switch (GameFrame.Human.valueOf(pNotification.getMessage()))
            {
            case playPrompt:
                if(!aPlayPrompted)
                    log("Human player is prompted to play.");
                notifyObservers(new Notification("gui.gameframe", this, getNotificationSequenceNumber(),
                        GameFrame.Human.playPrompt.toString()));
                aPlayPrompted = true;
                break;
            case playDone:
                log("Human has played the card");
                aPlayPrompted = false;
                aPlayedCard = null;
                aPlayableCards = null;
                notifyObservers(new Notification("gui.gameframe", this, getNotificationSequenceNumber(),
                        GameFrame.Human.playDone.toString()));
                break;
            case bidDone:
                log("Human has bid.");
                aBidPrompted = false;
                aSelectedBid = null;
                notifyObservers(new Notification("gui.gameframe", this, getNotificationSequenceNumber(),
                        GameFrame.Human.bidDone.toString()));
                break;
            case bidPrompt:
                if(!aBidPrompted)
                    log("Human player is prompted to bid.");
                aBidPrompted = true;
                aPreviousBids = aHuman.getPreviousBids(); 
                notifyObservers(new Notification("gui.gameframe", this, getNotificationSequenceNumber(),
                        GameFrame.Human.bidPrompt.toString()));
                break;
            case discardPrompt:
                if(!aDiscardPrompted)
                    log("Human player is prompted to discard.");
                notifyObservers(new Notification("gui.gameframe", this, getNotificationSequenceNumber(),
                        GameFrame.Human.discardPrompt.toString()));
                aDiscardPrompted = true;
                break;
            case discardDone:
                log("Human has discarded cards.");
                aDiscardPrompted = false;
                aDiscardedCards = null;
                notifyObservers(new Notification("gui.gameframe", this, getNotificationSequenceNumber(),
                        GameFrame.Human.discardDone.toString()));
                break;
            default:
                break;
            }
        }
        if (pNotification.getType().equals("gui.actiontoolbar"))
        {
        	log(pNotification.getMessage());

            ActionToolbar lBar = (ActionToolbar) (pNotification.getSource());
            switch (GameFrame.Human.valueOf(pNotification.getMessage()))
            {
            case bidDone:
                log("Human has bid.");
                if (aBidPrompted)
                {
                	log(aPreviousBids.toString());
                	aSelectedBid = lBar.geBid();
                    if (aPreviousBids.length == 0
                            || aSelectedBid.isPass() || aSelectedBid.compareTo(aPreviousBids[aPreviousBids.length - 1]) > 0)
                    {
                        log("Bid valid.");
                        aSelectedBid = lBar.geBid();
                        notifyObservers(new Notification("gui.gameframe", this, getNotificationSequenceNumber(),
                                GameFrame.Human.bidValidated.toString()));
                        synchronized (aHuman)
                        {
                        	aHuman.notify();
                        }
                    }
                    else
                    {
                        // TODO send invalidBid notification
                        aSelectedBid = null;
                    }
                }
                aBidPrompted = false;
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
                log("Human player has clicked a card.");
                if (aPlayPrompted)
                {
                    if (aPlayableCards.contains(lCardLabel.getCard()))
                    {
                        aPlayedCard = lCardLabel.getCard();
                        notifyObservers(new Notification("gui.gameframe", this, getNotificationSequenceNumber(),
                                GameFrame.Human.playValidated.toString()));
                    }
                    else
                    {
                        // TODO send invalid play notification
                        aPlayedCard = null;
                    }
                }
                if(aDiscardPrompted)
                {
                    // how to check if card clicked came from widow TODO
                    if(aDiscardedCards == null)
                    {
                        aDiscardedCards = new CardList();
                    }
                }
                aPlayPrompted = false;
                break;
            default:
                break;
            }
        }
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
            aLogger.info(message);
    }

    GameEngine getGameEngine()
    {
        return aEngine;
    }

    public enum State
    {
        displayMenu, play, newGameSet, newGame, newDeal, newBid, newContract, cardsDiscarded, cardPlayed, newTrick, trickWon, roundEnd, gameOver
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
        }
        else
        {
            aAutoPlay = false;
        }

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
