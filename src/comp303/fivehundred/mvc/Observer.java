package comp303.fivehundred.mvc;

import javax.management.Notification;

/**
 * Interface that can be implemented to provide observer support.
 * @author Gabrielle Germain
 *
 */
public interface Observer
{
	/**
	 * Determines what an observer should do upon notification that the observed object has changed.
	 * @param pNotification : Notification passed by the object being observed.
	 */
	void update(Notification pNotification);
}
