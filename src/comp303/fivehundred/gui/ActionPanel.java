package comp303.fivehundred.gui;

import javax.swing.JPanel;
import comp303.fivehundred.mvc.Observer;

@SuppressWarnings("serial")
public abstract class ActionPanel extends JPanel implements Observer
{
	public ActionPanel()
	{
		super();
		build();
		hide();
	}
	
	public abstract void build();
	public abstract void show();
	public abstract void hide();
}
