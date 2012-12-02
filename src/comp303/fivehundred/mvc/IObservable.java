package comp303.fivehundred.mvc;
import javax.management.Notification;


public interface IObservable
{
	
	/**
	 * Add an observer to the observable object.
	 * @param pObserver : Observer to add
	 */
	public void addObserver(Observer pObserver);
	
	/**
	 * Notifies the observers of this of a change in state.
	 * @param pNotification : Notification to send to observers.
	 */
	public void notifyObservers(Notification pNotification);
	
    /**
     * Get the notification sequence number within the source object.
     * @return Serial number identifying a particular instance of notification in the context of the notification source.
     */
    public long getNotificationSequenceNumber();
}
