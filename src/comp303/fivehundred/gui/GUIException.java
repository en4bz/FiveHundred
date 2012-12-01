package comp303.fivehundred.gui;

@SuppressWarnings("serial")
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
