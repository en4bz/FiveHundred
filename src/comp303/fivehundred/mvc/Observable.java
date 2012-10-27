package comp303.fivehundred.mvc;

import java.util.ArrayList;

import javax.management.Notification;

public class Observable
{
    // MVC-related fields
	private ArrayList<Observer> aObservers = new ArrayList<Observer>();
	private long aNotificationSequenceNumber = -1;
	
	
	public void addObserver(Observer pObserver)
	{
		aObservers.add(pObserver);
	}
	
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
    	return ++aNotificationSequenceNumber;	
    }

}
