package comp303.fivehundred.gui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.*;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


/**
 * Menu visible at the top of a GameBoard window
 * @author Gabrielle Germain
 */


@SuppressWarnings("serial")
public class Menu extends JMenuBar
{
	
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
	
	String directory = "c:\\"; //default location?
	String logName = "work"; //where is it supposed to be?
	String log = ".log";
	String fileName = directory+logName+log;
	
	//TODO: CREATE A STAT FILE
	//TODO: CHANGE DIRECTORY
	//TODO: CHANGE NAME OF THE FILE

	
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
		
		//File -> quit = The program terminates**WORKS
		quit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		
		//File -> New Game = create a new Game (Create a new stats file, etc)*WORKS
		newGame.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				new GameFrameMartin();
			}
		});
		
		//Statistics -> Show Stats
		show.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//TODO: Show stat in a dialog box?
			}

			});
		
		//Statistics -> Reset Stats**WORKS
		reset.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				File f = new File (fileName);
				f.delete();
				
				File aFile=new File(fileName);
				if(!aFile.exists()){
					try{
						aFile.createNewFile();
					}
					catch (Exception creation)
					{
						System.out.println("Error");
					}
					
				  }
				
			}
		});
		
		//Settings -> Change log file directory
		logFile.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				directory = (String)JOptionPane.showInputDialog(getRootPane(), "New directory:", "Customized Dialog", JOptionPane.PLAIN_MESSAGE, null,null, directory);
				//change the directory
			}
		});
		
		//Settings -> Change file name
		changeName.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				logName = (String)JOptionPane.showInputDialog(getRootPane(), "New name:", "Customized Dialog", JOptionPane.PLAIN_MESSAGE, null,null, logName);
				//change the name of the file
			
			}
		});
		
		//Help -> ask for help...OK
		ask.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(getRootPane(), "WRITE SOMETHING HERE");
			}
		});

	}
	
	
	
}
