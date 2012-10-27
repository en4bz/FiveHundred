package comp303.fivehundred.engine;

/**
 * To report any problem in the Game Engine.
 */

@SuppressWarnings("serial")
public class GameException extends RuntimeException
{	
	/**
	 * Constructor.
	 * @param pReason The reason of the exception.
	 */
	public GameException(String pReason) 
	{
		super(pReason);
	}
}
