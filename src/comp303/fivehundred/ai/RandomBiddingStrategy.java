package comp303.fivehundred.ai;

import java.util.Random;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;

/**
 * @author Rayyan Khoury
 * Enters a valid but random bid. Passes a configurable number of times.
 * If the robot does not pass, it uses a universal probability
 * distribution across all bids permitted.
 */
public class RandomBiddingStrategy implements IBiddingStrategy
{

	// Maximum pass percentage
	private static final int MAX_PERCENT_INCLUSIVE = 100;
	
	// Minimum pass percentage
	private static final int MIN_PERCENT_INCLUSIVE = 0;
	
	// Maximum bid possible
	private static final int MAX_BID_INCLUSIVE = 24;
	
	// For the initial constructor which passes 50 percent of the time
	private static final int FIFTY_PERCENT = 50;
	
	// Creates a random object
	private static Random aRand = new Random();
	
	// The probability of this robot passing
	private int aProbPass;
	
	/**
	 * Builds a robot that passes 50% of the time and bids randomly otherwise.
	 */
	public RandomBiddingStrategy()
	{
		
		aProbPass = FIFTY_PERCENT;
		
	}
	
	/** 
	 * Builds a robot that passes the specified percentage number of the time.
	 * @param pPassFrequency A percentage point (e.g., 50 for 50%) of the time the robot 
	 * will pass. Must be between 0 and 100 inclusive. 
	 * Whether the robot passes is determined at random.
	 */
	public RandomBiddingStrategy(int pPassFrequency)
	{

		assert (pPassFrequency >= MIN_PERCENT_INCLUSIVE) && (pPassFrequency <= MAX_PERCENT_INCLUSIVE);
		aProbPass = pPassFrequency;
		
	}
	
	@Override
	public Bid selectBid(Bid[] pPreviousBids, Hand pHand)
	{
		
	    // The index of the highest bid
	    int highestBidIndex = Bid.max(pPreviousBids).toIndex();
	    
	    // If the maximum bid has already been bid
	    if (highestBidIndex == MAX_BID_INCLUSIVE)
	    {
	    	
	    	return new Bid();
	    	
	    }
		
		// Finds a random number between 0 and 100 to determine whether this robot will bid or not
	    int pass = aRand.nextInt(MAX_PERCENT_INCLUSIVE + 1);
	    
	    // Finds a random index between lowest bid and maximum bid to bid should the robot bid
	    int bid = aRand.nextInt(MAX_BID_INCLUSIVE - highestBidIndex) + highestBidIndex + 1;
	    
	    // The robot passes
	    if (pass == MIN_PERCENT_INCLUSIVE || pass <= aProbPass) 
	    {
	    	
	    	return new Bid();
	    	
	    }
	    
	    // The robot passes 
	    else if (pass == MAX_PERCENT_INCLUSIVE || pass > aProbPass)
	    {
	    	
	    	return new Bid(bid);

	    	
	    }
	    
	    // Contingency case
	    else
	    {
	    	
	    	return new Bid();
	    	
	    }
	    
	}

}
