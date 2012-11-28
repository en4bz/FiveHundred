package comp303.fivehundred.gui;

import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import comp303.fivehundred.model.Bid;

@SuppressWarnings("serial")
public class ContractPanel extends JPanel
{
	private Bid aBid;
	
	ContractPanel(Bid pBid){
		super(new FlowLayout());
	}
	
	private void reDraw(){
		switch(aBid.toIndex()){
		case 0:
			this.add(new JLabel(new ImageIcon("FilePath")));
		}
	}
}
