package comp303.fivehundred.gui;

public class GUIException extends RuntimeException
{	
	/**
	 * Constructor.
	 * @param pReason The reason of the exception.
	 */
	public GUIException(String pReason) 
	{
		super(pReason);
	}
}
