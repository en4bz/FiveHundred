package comp303.fivehundred.gui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.management.Notification;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;


/**
 * Menu visible at the top of a GameBoard window
 * @author Gabrielle Germain
 */


@SuppressWarnings("serial")
public class Menu extends JMenuBar
{
	
	JMenu game = new JMenu("Game");
	JMenu stats = new JMenu ("Statistics");
	JMenu help = new JMenu ("Help");
	
	JMenuItem quit = new JMenuItem ("Quit");
	JMenuItem show = new JMenuItem ("Show Statistics");
	JMenuItem reset = new JMenuItem ("Reset Statistics");
	JMenuItem ask = new JMenuItem ("Ask for help...");
	
	public Menu() 
	{
		//MENU BAR
		//Items
		this.add(game);
		this.add(stats);
		this.add(help);
		//SubItems
		game.add(quit);
		stats.add(show);
		stats.add(reset);
		help.add(ask);
		
		//File -> quit = The program terminates**WORKS
		quit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		
		//Statistics -> Show Stats
		show.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				GameFrame lEngine = (GameFrame) Menu.this.getTopLevelAncestor();
				lEngine.update(new Notification("stats.show",this,lEngine.getNotificationSequenceNumber(), ""));
			}

		});
		
		reset.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				GameFrame lEngine = (GameFrame) Menu.this.getTopLevelAncestor();
				lEngine.update(new Notification("stats.reset",this,lEngine.getNotificationSequenceNumber(), ""));
			}

		});
		
		
		//Help -> ask for help...OK
		ask.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(getRootPane(), "Please refer to the instructions of the game");
			}
		});

	}
	
	
	
}
