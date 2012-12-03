package comp303.fivehundred.gui;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

class BackgroundPanel extends JComponent {
	
    private ImageIcon image;
    
    public BackgroundPanel(ImageIcon pImage) {
        image = pImage;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
    	super.paintComponent(g);
        g.drawImage(image.getImage(), 0, 0, this.getTopLevelAncestor().getWidth(), this.getTopLevelAncestor().getHeight(), this);
    }
}
