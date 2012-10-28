package comp303.fivehundred.gui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

/**
 * Experimental text interface for playing in interactive mode. Not very robust.
 * Does not work for jokers yet. To not be considered in Milestone 2.
 * @author Eleyine
 *
 */
public class ConsoleInterface implements UserInterface
{

	private final Logger aLogger = LoggerFactory.getLogger("PROMPT");
	private Marker noNewLineMarker = MarkerFactory.getMarker("NO_NEW_LINE");
	private InputStreamReader istream = new InputStreamReader(System.in) ;
    private BufferedReader bufRead = new BufferedReader(istream) ;
    private HashMap<String, String> aCardMap;
    
    public ConsoleInterface()
    {
        aCardMap = new HashMap<String, String>();
        aCardMap.put("4", "FOUR");
        aCardMap.put("5", "FIVE");
        aCardMap.put("6", "SIX");
        aCardMap.put("7", "SEVEN");
        aCardMap.put("8", "EIGHT");
        aCardMap.put("9", "NINE");
        aCardMap.put("T", "TEN");
        aCardMap.put("J", "JACK");
        aCardMap.put("Q", "QUEEN");
        aCardMap.put("K", "KING");
        aCardMap.put("A", "ACE");
        aCardMap.put("H", "HEARTS");
        aCardMap.put("S", "SPADES");
        aCardMap.put("C", "CLUBS");
        aCardMap.put("D", "DIAMONDS");  
    }
    
	public Card play(Trick pTrick)
	{
		Card c;
		while(true)
		{
			try 
			{
				aLogger.info(noNewLineMarker, "Please enter card to play: ");
				String input = bufRead.readLine();
				Pattern pattern = Pattern.compile( ".*([\\d\\w])((\\w))", Pattern.CASE_INSENSITIVE );
			    Matcher matcher = pattern.matcher(input);
			    matcher.matches();
			    Card.Rank rank = Card.Rank.valueOf(aCardMap.get(matcher.group(1).toUpperCase()));
			    Card.Suit suit = Card.Suit.valueOf(aCardMap.get(matcher.group(2).toUpperCase()));
			    c = new Card(rank, suit);
			    break;
			}
			catch(Exception err)
			{
				aLogger.warn("Something went wrong. Try again.");
			}
		}
		return c;
	}
	
	@Override
	public CardList exchange(Bid[] pBids, int pIndex, Hand pWidow)
	{
		CardList list = new CardList();
		for(int i = 0; i < 6; i++)
		{
			Card c;
			while(true)
			{
				try 
				{
					aLogger.info(noNewLineMarker, "Please enter card to discard: ");
					String input = bufRead.readLine();
					Pattern pattern = Pattern.compile( ".*([\\d\\w])((\\w))", Pattern.CASE_INSENSITIVE );
				    Matcher matcher = pattern.matcher(input);
				    matcher.matches();
				    Card.Rank rank = Card.Rank.valueOf(aCardMap.get(matcher.group(1).toUpperCase()));
				    Card.Suit suit = Card.Suit.valueOf(aCardMap.get(matcher.group(2).toUpperCase()));
				    c = new Card(rank, suit);
				    break;
					
				}
				catch(Exception err)
				{
					aLogger.warn("Something went wrong. Try again.");
				}
			}
			list.add(c);
		}
		return list;
	}
	
	@Override
	public Bid selectBid(Bid[] pPreviousBids)
	{
		Bid b;
		while(true)
		{
			try 
			{
				aLogger.info(noNewLineMarker, "Please enter your bid (0P for pass, *NT for notrump): ");
				String input = bufRead.readLine();
				Pattern pattern = Pattern.compile( "^([0-9]{1,2})((\\w)).*");
			    Matcher matcher = pattern.matcher(input);
			    matcher.matches();
			    int tricks = Integer.parseInt(matcher.group(1));
			    if(tricks == 0)
			    {

			    	b = new Bid();
			    }
			    else
			    {
				    String m = matcher.group(2);
				    if(m.equals("N"))
				    {
				    	b = new Bid(tricks, null);
				    }
				    else
				    {
					    Card.Suit suit = Card.Suit.valueOf(aCardMap.get(m.toUpperCase()));
				    	b = new Bid(tricks, suit);
				    }
			    }
			    break;
				
			}
			catch(Exception err)
			{
				aLogger.warn("Something went wrong. Try again.");
			}
		}
		return b;
	}

}
