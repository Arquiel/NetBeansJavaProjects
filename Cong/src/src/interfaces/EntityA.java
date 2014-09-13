package src.interfaces;

import java.awt.Graphics;
import java.awt.Rectangle;

// Interface for player objects (if 2 axis of movement were used, they would not incur a collision)
public interface EntityA {
    public void tick();
    public void render(Graphics g);
    public Rectangle getBounds();
    
    public double getX();
    public double getY();
}
