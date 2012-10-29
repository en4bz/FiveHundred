package comp303.fivehundred.mvc;

import java.util.ArrayList;

import javax.management.Notification;

/**
 * Implements a class which can be extended to provide observer support.
 * @author Gabrielle Germain
 *
 */
public class Observable
{
    // MVC-related fields
	private ArrayList<Observer> aObservers = new ArrayList<Observer>();
	private long aNotificationSequenceNumber = 0;
	
	/**
	 * Add an observer to the observable object.
	 * @param pObserver : Observer to add
	 */
	public void addObserver(Observer pObserver)
	{
		if(!aObservers.contains(pObserver))
		{
			aObservers.add(pObserver);
		}
	}
	
	/**
	 * Notifies the observers of this of a change in state.
	 * @param pNotification : Notification to send to observers.
	 */
	protected void notifyObservers(Notification pNotification)
	{
		for(Observer observer : aObservers)
		{
			observer.update(pNotification);
		}
	}
	
    /**
     * Get the notification sequence number within the source object.
     * @return Serial number identifying a particular instance of notification in the context of the notification source.
     */
    protected long getNotificationSequenceNumber()
    {
    	return aNotificationSequenceNumber++;	
    }
}
