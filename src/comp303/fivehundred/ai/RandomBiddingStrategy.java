package comp303.fivehundred.ai;

import java.util.Random;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;

/**
 * Enters a valid but random bid. Passes a configurable number of times.
 * If the robot does not pass, it uses a universal probability
 * distribution across all bids permitted.
 */
public class RandomBiddingStrategy implements IBiddingStrategy
{

	private static final int MAX_INCLUSIVE = 100;
	private static final int MIN_PERCENTAGE = 50;
	private Bid aCurrent;
	//private Bid lastBid;
	
	// Creates a random object
	private Random aRand = new Random();
	
	/**
	 * Builds a robot that passes 50% of the time and bids randomly otherwise.
	 */
	public RandomBiddingStrategy()
	{
		
		// Finds a random number between 0 and 100
	    int prob = aRand.nextInt(MAX_INCLUSIVE + 1);
		
	    if (prob < MIN_PERCENTAGE)
	    {
	    	
	    	
	    	
	    }
	    
	    else
	    {
	    	
	    	
	    }
		
	}
	
	/** 
	 * Builds a robot that passes the specified percentage number of the time.
	 * @param pPassFrequency A percentage point (e.g., 50 for 50%) of the time the robot 
	 * will pass. Must be between 0 and 100 inclusive. 
	 * Whether the robot passes is determined at random.
	 */
	public RandomBiddingStrategy(int pPassFrequency)
	{

		// Finds a random number between 0 and 100
	    int prob = aRand.nextInt(MAX_INCLUSIVE + 1);
	    
	    // Robot never passes
	    if (pPassFrequency == 0) 
	    {
	    
	    	//aCurrent = 
	    	
	    }
	    
	    else if (pPassFrequency == MAX_INCLUSIVE) 
	    {
	    	
	    	aCurrent = new Bid();
	    	return;
	    	
	    }
	    
	    // Passes if random number is greater than 50
	    else if (prob < pPassFrequency) 
	    {
	    	//aCurrent = selectBid(super.pPreviousBids, Hand pHand);
	    	return;
	    }
		
	}
	
	@Override
	public Bid selectBid(Bid[] pPreviousBids, Hand pHand)
	{
		return null;
	}

}
