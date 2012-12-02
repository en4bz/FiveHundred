package comp303.fivehundred.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.lang.reflect.InvocationTargetException;
import java.util.ResourceBundle;

import javax.management.Notification;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import comp303.fivehundred.engine.GameEngine;
import comp303.fivehundred.engine.GameLogger;
import comp303.fivehundred.engine.GameStatistics;
import comp303.fivehundred.mvc.Observer;
import comp303.fivehundred.player.Team;

@SuppressWarnings("serial")
public class GameFrame extends JFrame implements Observer
{
    public static Color BACKGROUND_COLOR = new Color(40, 160, 20);
    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("comp303.fivehundred.gui.MessageBundle");
    private long aNotificationSequenceNumber = 0;
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

    public GameFrame()
    {
        // Build basic frame
        this.setTitle("Five Hundred");  
        this.setLayout(new FlowLayout());
        this.setLocation(0, 0); //Top-left corner of the screen
        this.setSize(new Dimension(WIDTH, HEIGHT));
   //     this.setResizable(false); //We cannot change the dimensions of the window by hand when the program runs (components don't move) 
        
        // Add Menu
        this.setJMenuBar(new Menu()); //add menu to the new window (new game)
        
        // Start the party
        aPlayerMenu = new PlayerMenu(this);
        add(aPlayerMenu);
        
        // Make visible
        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible( true );
        
    }
    
    public static void main(String[] args)
    {
        GameFrame a = new GameFrame();
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
  
        log("Game Board drawn.");
        
        log("Adding Observers.");
        aEngine.addObserver(this);
        aGameStats = new GameStatistics();
        aGameLogger = new GameLogger();
        aEngine.addObserver(aGameStats);
        aEngine.addObserver(aGameLogger);


        newGame();
    }
    
    private void newGame()
    {
        if(aGamesLeft > 0)
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
            pack();
        }
    }
    
    private void newDeal()
    {
        log("Starting new deal.");
        aEngine.deal();
        newBid();
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
        if(aEngine.isGameOver())
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
        if(pNotification.getType().equals("gui.gameframe"))
        {
            State lState = State.valueOf(pNotification.getMessage());
            switch (lState) 
            {
                case newGameSet:
                	Thread t = new Thread(new Runnable() {
                	    @Override
                	    public void run() {
                	       newGameSet();
                	    }
                	   });
                	t.start();

                    break; 
                default:
                    break;
            }
        }
        if(pNotification.getType().equals("game.engine")){
            setTitle(pNotification.getMessage());
           
            sleep();
            GameEngine lEngine = (GameEngine) (pNotification.getSource());
            switch(GameEngine.State.valueOf(pNotification.getMessage())){
            case newGame:
                log("Resetting game board scores.");
                aBoard.resetScores();
                break;
            case newDeal: 
                log("Updating card panels.");
                aBoard.updateCardPanels();
                log("Resetting tricks count.");
                aBoard.resetTricksCount();
                log("Updating widow.");
                aBoard.updateWidow(lEngine.getWidow());
                revalidate();
                repaint();
                break;
            case allPasses:
            	log("All passes!");
            	newDeal();
            	break;
            case newContract:
            	log("New contract.");
                aBoard.updateBid(lEngine.getContract());
            	play();
            	break;
            case cardsDiscarded:
                aBoard.updateCardPanel(lEngine.getCurrentPlayer());
                break;
            case cardPlayed:
                aBoard.updateCardPanel(lEngine.getCurrentPlayer());
                aBoard.updateTrick(lEngine.getCurrentPlayer() ,((GameEngine)pNotification.getSource()).getCardPlayed());
                break;
            case newTrick:
                aBoard.resetCurrentTrick();
                aBoard.updateTrick(lEngine.getCurrentPlayer() ,((GameEngine)pNotification.getSource()).getCardPlayed());
                break;
            case trickWon:
                aBoard.updateTrickCount(lEngine.getTrickWinner());
                aBoard.resetCurrentTrick();
                break;
            case roundEnd:
                aBoard.updateScores();
                break;
            case gameOver:
                break;
            default:
                break;
            }
        }
    }
    
    private void sleep()
    {
        try
        {
            Thread.sleep(100);
        }
        catch (InterruptedException e){}
    }
    
    public static void log(String message)
    {
        if(aIsLogging)
            aLogger.info(message);
    }
    
    GameEngine getGameEngine() {
        return aEngine;
    }
            
    protected long getNotificationSequenceNumber()
    {
        return aNotificationSequenceNumber++;   
    }
    
    public enum State {
        displayMenu,
        play,
        newGameSet,
        newGame,
        newDeal,
        newBid,
        newContract,
        cardsDiscarded,
        cardPlayed,
        newTrick,
        trickWon,
        roundEnd,
        gameOver
    }

}
