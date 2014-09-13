package src.interfaces;

import java.awt.Graphics;
import java.awt.Rectangle;

// Interface for ball objects (if more than one ball was used, they would not incur a collision)
public interface EntityB {
    public void tick();
    public void render(Graphics g);
    public Rectangle getBounds();
    
    public double getX();
    public double getY();
}
