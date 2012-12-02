package comp303.fivehundred.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.management.Notification;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.mvc.Observer;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;

@SuppressWarnings("serial")
public class ActionToolbar extends JPanel
{
	private final GameFrame aFrame;
	private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("comp303.fivehundred.gui.MessageBundle");
	private int aSpeed;
	private boolean aPlay;
	private boolean aWait;
	private Bid aBid;
	private CardList aDiscarded;
	private int aHEIGHT;
	private int aWIDTH;

	private ActionPanel aPlayPanel;
	private ActionPanel aBidPanel;
	private ActionPanel aDiscardPanel;
	private JPanel aActionPanel;

	public ActionToolbar()
	{
		aFrame = (GameFrame) this.getTopLevelAncestor();
		setLayout(new BorderLayout());

		// set default values
		aSpeed = aFrame.getSpeed();
		aBid = new Bid();
		aHEIGHT = 100;
		aWIDTH = aFrame.getWidth();
		
		//

		buildAutoPlayPanel();
		buildSpeedBox();
		
		aPlayPanel = getPlayPanel();
		aFrame.addObserver(aPlayPanel);
		
		aBidPanel = getBidPanel();
		aFrame.addObserver(aBidPanel);
		
		aDiscardPanel = getDiscardPanel();
		aFrame.addObserver(aDiscardPanel);

	}


	private ActionPanel getPlayPanel()
	{
		return new ActionPanel()
		{

			private final JButton playButton = new JButton(
					MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.Play"));

			public void build()
			{
				setLayout(new GridLayout(1, 1));
				int buttonWidth = 200;
				setMinimumSize(new Dimension(buttonWidth, aHEIGHT / 4 * 5));
				setPreferredSize(new Dimension(buttonWidth, aHEIGHT / 4 * 5));
				setMaximumSize(new Dimension(buttonWidth, aHEIGHT / 4 * 5));
				
				playButton.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						if (playButton.isSelected())
						{
							aFrame.update(new Notification("gui.gameframe", this, aFrame.getNotificationSequenceNumber(), GameFrame.Human.playDone.toString()));
						}
					}
				});
				add(playButton);
			}
			
			public void show()
			{
				ActionToolbar.this.add(this, BorderLayout.CENTER);
				ActionToolbar.this.repaint();
			}
			
			public void hide()
			{
				ActionToolbar.this.remove(this);
				ActionToolbar.this.repaint();
			}

			@Override
			public void update(Notification pNotification)
			{
				if(pNotification.getType().equals("game.actiontoolbar")){
		           
		            switch(GameFrame.Human.valueOf(pNotification.getMessage())){
		            case playPrompt:
		            	show();
		            	break;
		            case playValidated:
		            	hide();
		            	break;
		            default:
		            	break;
		            }
				}
			}
		};
	}
	
	public Bid geBid()
	{
		return aBid;
	}
	
	private void setBid(Bid pBid)
	{
		aBid = pBid;
	}
	
	private ActionPanel getBidPanel()
	{
		return new ActionPanel()
		{


			private final HashMap<String, Bid> allBids = new HashMap<String, Bid>();
			
			private final JButton bidButton = new JButton(
					MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.Bid"));
			private JComboBox bidDropDown;

			public void build()
			{
				setLayout(new GridLayout(1, 2));
				int height = aHEIGHT / 4 * 5;
				int width = 500;
				setMinimumSize(new Dimension(width, height));
				setPreferredSize(new Dimension(width, height));
				setMaximumSize(new Dimension(width, height));
				
				
		        // No I have not typed all this, I used a python script
		        allBids.put(MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.Bid.PASS"), new Bid());
		        allBids.put(MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.Bid.6Spades"), new Bid(6, Suit.SPADES));
		        allBids.put(MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.Bid.6Clubs"), new Bid(6, Suit.CLUBS));
		        allBids.put(MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.Bid.6Diamonds"), new Bid(6, Suit.DIAMONDS));
		        allBids.put(MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.Bid.6Hearts"), new Bid(6, Suit.HEARTS));
		        allBids.put(MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.Bid.6NoTrump"), new Bid(6, null));
		        allBids.put(MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.Bid.7Spades"), new Bid(7, Suit.SPADES));
		        allBids.put(MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.Bid.7Clubs"), new Bid(7, Suit.CLUBS));
		        allBids.put(MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.Bid.7Diamonds"), new Bid(7, Suit.DIAMONDS));
		        allBids.put(MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.Bid.7Hearts"), new Bid(7, Suit.HEARTS));
		        allBids.put(MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.Bid.7NoTrump"), new Bid(7, null));
		        allBids.put(MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.Bid.8Spades"), new Bid(8, Suit.SPADES));
		        allBids.put(MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.Bid.8Clubs"), new Bid(8, Suit.CLUBS));
		        allBids.put(MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.Bid.8Diamonds"), new Bid(8, Suit.DIAMONDS));
		        allBids.put(MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.Bid.8Hearts"), new Bid(8, Suit.HEARTS));
		        allBids.put(MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.Bid.8NoTrump"), new Bid(8, null));
		        allBids.put(MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.Bid.9Spades"), new Bid(9, Suit.SPADES));
		        allBids.put(MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.Bid.9Clubs"), new Bid(9, Suit.CLUBS));
		        allBids.put(MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.Bid.9Diamonds"), new Bid(9, Suit.DIAMONDS));
		        allBids.put(MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.Bid.9Hearts"), new Bid(9, Suit.HEARTS));
		        allBids.put(MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.Bid.9NoTrump"), new Bid(9, null));
		        allBids.put(MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.Bid.10Spades"), new Bid(10, Suit.SPADES));
		        allBids.put(MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.Bid.10Clubs"), new Bid(10, Suit.CLUBS));
		        allBids.put(MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.Bid.10Diamonds"), new Bid(10, Suit.DIAMONDS));
		        allBids.put(MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.Bid.10Hearts"), new Bid(10, Suit.HEARTS));
		        allBids.put(MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.Bid.10NoTrump"), new Bid(10, null));

				bidDropDown = new JComboBox(allBids.keySet().toArray());
				bidDropDown.setEditable(false);
				
				// update bid value
				setBid(allBids.get(bidDropDown.getSelectedItem()));
				bidDropDown.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						setBid(allBids.get(bidDropDown.getSelectedItem()));
					}
				});
				
				
				// trigger bid event
				bidButton.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						if (bidButton.isSelected())
						{
							aFrame.update(new Notification("gui.gameframe", this, aFrame.getNotificationSequenceNumber(), GameFrame.Human.bidDone.toString()));
						}
					}
				});
				add(bidButton);
			}
			
			public Bid getBid()
			{
				return aBid;
			}
			
			public void show()
			{
				ActionToolbar.this.add(this, BorderLayout.CENTER);
				ActionToolbar.this.repaint();
			}
			
			public void hide()
			{
				ActionToolbar.this.remove(this);
				ActionToolbar.this.repaint();
			}

			@Override
			public void update(Notification pNotification)
			{
				if(pNotification.getType().equals("game.actiontoolbar")){
		           
		            switch(GameFrame.Human.valueOf(pNotification.getMessage())){
		            case bidPrompt:
		            	show();
		            	break;
		            case bidValidated:
		            	hide();
		            	break;
		            default:
		            	break;
		            }
				}
			}
		};
	}
	
	private ActionPanel getDiscardPanel()
	{
		return new ActionPanel()
		{

			private final JButton discardButton = new JButton(
					MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.Discard"));

			public void build()
			{
				setLayout(new GridLayout(1, 1));
				int buttonWidth = 200;
				setMinimumSize(new Dimension(buttonWidth, aHEIGHT / 4 * 5));
				setPreferredSize(new Dimension(buttonWidth, aHEIGHT / 4 * 5));
				setMaximumSize(new Dimension(buttonWidth, aHEIGHT / 4 * 5));
								
				discardButton.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						if (discardButton.isSelected())
						{
							aFrame.update(new Notification("gui.gameframe", this, aFrame.getNotificationSequenceNumber(), GameFrame.Human.discardDone.toString()));
						}
					}
				});
				add(discardButton);
			}
			
			public void show()
			{
				ActionToolbar.this.add(this, BorderLayout.CENTER);
				ActionToolbar.this.repaint();
			}
			
			public void hide()
			{
				ActionToolbar.this.remove(this);
				ActionToolbar.this.repaint();
			}

			@Override
			public void update(Notification pNotification)
			{
				if(pNotification.getType().equals("game.actiontoolbar")){
		           
		            switch(GameFrame.Human.valueOf(pNotification.getMessage())){
		            case discardPrompt:
		            	show();
		            	break;
		            case discardValidated:
		            	hide();
		            	break;
		            default:
		            	break;
		            }
				}
			}
		};
	}


	private void buildSpeedBox()
	{
		int min = 50;
		int max = 3000;
		int step = 100;
		int def = 1000;
		JPanel lSpeedBox = new JPanel();
		lSpeedBox.setBorder(BorderFactory.createTitledBorder(MESSAGES
				.getString("comp303.fivehundred.gui.ActionToolbar.Speed")));
		lSpeedBox.setLayout(new GridLayout(1, 1));
		int buttonWidth = aWIDTH / 5;
		lSpeedBox.setMinimumSize(new Dimension(buttonWidth, aHEIGHT));
		lSpeedBox.setPreferredSize(new Dimension(buttonWidth, aHEIGHT));
		lSpeedBox.setMaximumSize(new Dimension(buttonWidth, aHEIGHT));

		// Make Slider
		JSlider lSlider = new JSlider(JSlider.HORIZONTAL, min, max, def);

		// Turn on labels at major tick marks.
		lSlider.setMajorTickSpacing(step);
		lSlider.setMinorTickSpacing(10);
		lSlider.setPaintTicks(true);
		lSlider.setPaintLabels(true);

		lSlider.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting())
				{
					setSpeed((int) source.getValue());
				}
			}
		});
		lSpeedBox.add(lSlider);

		add(lSpeedBox, BorderLayout.EAST);
	}

	private void buildAutoPlayPanel()
	{
		JPanel lAutoPlayBox = new JPanel();
		lAutoPlayBox.setLayout(new GridLayout(1, 1));
		int buttonWidth = aWIDTH / 5;
		lAutoPlayBox.setMinimumSize(new Dimension(buttonWidth, aHEIGHT));
		lAutoPlayBox.setPreferredSize(new Dimension(buttonWidth, aHEIGHT));
		lAutoPlayBox.setMaximumSize(new Dimension(buttonWidth, aHEIGHT));

		final JToggleButton lButton = new JToggleButton(
				MESSAGES.getString("comp303.fivehundred.gui.ActionToolbar.AutoPlay"), false);
		lButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if (lButton.isSelected())
				{
					aFrame.setAutoPlay(true);
				}
				else
				{
					aFrame.setAutoPlay(false);
				}
			}
		});
		lAutoPlayBox.add(lButton);

		add(lAutoPlayBox, BorderLayout.WEST);
	}

	private void setSpeed(int pSpeed)
	{
		aSpeed = pSpeed;
		aFrame.setSpeed(aSpeed);
	}

}
