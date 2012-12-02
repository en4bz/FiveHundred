package comp303.fivehundred.gui;

import java.awt.Color;
import java.awt.FlowLayout;
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
import comp303.fivehundred.mvc.IObservable;
import comp303.fivehundred.mvc.Observer;
import comp303.fivehundred.player.Team;
import comp303.fivehundred.util.Card;

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
    
    private int aSpeed = 100;
    private boolean aAutoPlay = false;
    
    // MVC-related fields
	private ArrayList<Observer> aObservers = new ArrayList<Observer>();
	private long aNotificationSequenceNumber = 0;

    public GameFrame()
    {
        // Build basic frame
        this.setTitle("Five Hundred");  
        this.setLayout(new FlowLayout());
        this.setLocation(5, 5); //Top-left corner of the screen
     //   this.setSize(Toolkit.getDefaultToolkit().getScreenSize()); //Force Full Screen 
        
        // Add Menu
        this.setJMenuBar(new Menu()); //add menu to the new window (new game)
        
        // Start the party
        aPlayerMenu = new PlayerMenu(this);
        this.add(aPlayerMenu);
        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
 //       this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
         
        this.setVisible( true );
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
                if(aPracticeModeOn){
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
                break;
            case newBid:
            	aBoard.updateBids(lEngine.getCurrentPlayer(),aEngine.getBids());
            	break;
            case allPasses:
            	log("All passes!");
            	aBoard.resetBids();
            	newDeal();
            	break;
            case newContract:
            	log("New contract.");
            	aBoard.updateWidow(lEngine.getWidow(),lEngine.getContractor());
            	aBoard.setWidowCentral();
            	aBoard.setContract(lEngine.getContract());
            	invalidate();
                validate();
            	repaint();
            	play();
            	break;
            case cardsDiscarded:
            	aBoard.updateWidow(lEngine.getWidow(),lEngine.getContractor());
            	aBoard.setPlayingCentral();
                aBoard.updateCardPanel(lEngine.getCurrentPlayer());
                invalidate();
                validate();
            	repaint();
                break;
            case cardPlayed:
                aBoard.updateCardPanel(lEngine.getCurrentPlayer());
                aBoard.updateTrick(lEngine.getCurrentPlayer() ,((GameEngine)pNotification.getSource()).getCardPlayed());
                break;
            case newTrick:
                aBoard.resetCurrentTrick();
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
            Thread.sleep(aSpeed);
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

    public enum Human {
    	playDone,
    	playPrompt,
    	playValidated,
    	discardDone,
    	discardPrompt,
    	discardValidated,
    	bidDone,
    	bidPrompt,
    	bidValidated
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
		if(lBit)
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
		if(!aObservers.contains(pObserver))
		{
			aObservers.add(pObserver);
		}		
	}

	@Override
	public void notifyObservers(Notification pNotification)
	{
		for(Observer observer : aObservers)
		{
			observer.update(pNotification);
		}		
	}
	
    public long getNotificationSequenceNumber()
    {
    	return aNotificationSequenceNumber++;	
    }
    
    public boolean inPracticeMode(){
    	return aPracticeModeOn;
    }
}
