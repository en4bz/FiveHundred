package comp303.fivehundred.gui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


@SuppressWarnings("serial")
public class Menu extends JMenuBar
{

	//TODO: link each menu component to what they refer to
	
	JMenu game = new JMenu("Game");
	JMenu stats = new JMenu ("Statistics");
	JMenu settings = new JMenu ("Settings");
	JMenu help = new JMenu ("Help");
	
	JMenuItem quit = new JMenuItem ("Quit");
	JMenuItem newGame = new JMenuItem ("New Game");
	JMenuItem show = new JMenuItem ("Show Statistics");
	JMenuItem reset = new JMenuItem ("Reset Statistics");
	JMenuItem logFile = new JMenuItem ("Change log file directory");
	JMenuItem changeName = new JMenuItem ("Change name of the file");
	JMenuItem ask = new JMenuItem ("Ask for help...");
	
	public Menu() 
	{

		//MENU BAR

		//Items
		add(game);
		add(stats);
		add(settings);
		add(help);
		
		//SubItems
		game.add(newGame);
		game.add(quit);
		
		stats.add(show);
		stats.add(reset);
		
		settings.add(logFile);
		settings.add(changeName);
		
		help.add(ask);
		
		
		//Link menu components
		
		//File -> quit = The program terminates
		quit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		
		//File -> New Game = create a new Game (Create a new stats file, etc)
		newGame.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				new GameFrame();
			}
		});
		
		//Statistics -> Show Stats
		show.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//Read the stats file
			}
		});
		
		//Statistics -> Reset Stats
		reset.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//Reset stats file
			}
		});
		
		//Settings -> Change log file
		logFile.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//Change directory
			}
		});
		
		//Settings -> Change file name
		changeName.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//Change name of the log file
			}
		});
		
		//Help -> ask for help...
		ask.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//"Instruction guide" that contains all the rules of the game? (TextFile)
			}
		});

	}
	
	
	
}
