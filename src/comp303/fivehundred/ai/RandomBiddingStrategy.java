package comp303.fivehundred.ai;

import java.util.Random;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;

/**
 * @author Rayyan Khoury
 * Enters a valid but random bid. Passes a configurable number of times.
 * If the robot does not pass, it uses a universal probability
 * distribution across all bids permitted.
 * 
 * DESIGN DECISION 1: if the pass frequency is not within the 0 to 100 range, 
 * the constructor creates a robot which passes 50% of the time.
 * 
 * DESIGN DECISION 2: The robot bids if THIS robot's possibility of passing is 
 * LESS THAN (and not equal to) the computed probability of passing in the bid method.
 * The robot passes otherwise (greater than or equal to).
 * 
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

		// If the pass frequency is not within the range of 0 to 100, sets the robot at a 50 percent passing rate
		if ((pPassFrequency < MIN_PERCENT_INCLUSIVE) || (pPassFrequency > MAX_PERCENT_INCLUSIVE))
		{
			
			aProbPass = FIFTY_PERCENT;
			return;
			
		}
		
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
	    
	    
	    /* Switch statements for the max and minimum probability of this robot bidding. 
	     * EG if this robot's tendency to pass is 100%, this switch statement makes sure 
	     * that this robot will pass, and if this robot's tendency to pass is 0%, this robot
	     * will never pass
	     * */
	    
	    switch (aProbPass)
	    {
	    
	    case MIN_PERCENT_INCLUSIVE: return new Bid(bid);
	    case MAX_PERCENT_INCLUSIVE: return new Bid();
	    default: break;

	    }
	    
	    // The robot bids if THIS robot's possibility of passing is LESS THAN the computed probability of passing in this method
	    if (aProbPass < pass) 
	    {
	    	
	    	return new Bid(bid);
	    	
	    }
	    
	    // The robot passes
	    else
	    {
	    	
	    	return new Bid();

	    	
	    }
	    
	}

}
