package org.jpedal.objects.acroforms;

import java.awt.*;
import javax.swing.*;

/* This class is used by CustomIconDemo.java. */
public class FixImageIcon extends ImageIcon implements Icon, SwingConstants {
    
    private int width = -1;
    private int height = -1;
    private Image image=null;

    public FixImageIcon(Image inImage) {
        image = inImage;
    }
    
    public void setWH(int newWidth,int newHeight){
        width = newWidth;
        height = newHeight;
    }
    
    public int getIconHeight() {
    	
    	if(image==null)
			return height;
        if(height==-1)
            return image.getHeight(null);
        else
            return height;
    }

    public int getIconWidth() {
    	
    		if(image==null)
    			return width;
    		
        if(width==-1)
            return image.getWidth(null);
        else
            return width;
    }
    
    public Image getImage(){
        return image;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        if(image==null)
            return;
        
        if (c.isEnabled()) {
            g.setColor(c.getBackground());
        } else {
            g.setColor(Color.gray);
        }

//        g.translate(x, y);
         if(width>0 && height>0)
             g.drawImage(image,0,0,width,height,null);
         else
             g.drawImage(image,0,0, null);
        g.translate(-x,-y);
    }
}
