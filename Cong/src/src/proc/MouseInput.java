package src.proc;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import src.ui.Menu;

public class MouseInput implements MouseListener{
    private Cong cong;
    
    public MouseInput(Cong cong){
        this.cong = cong;
    }
    
    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        // check every menu item in the current array to see if the click was within its bounds
        int mx = e.getX();
        int my = e.getY();
        
        for(int i = 0; i < cong.menu.mi.length; i++){
            if(mx >= cong.menu.mi[i].getX() && mx <= cong.menu.mi[i].getX() + Menu.ITEMWIDTH){
                if(my >= cong.menu.mi[i].getY() && my <= cong.menu.mi[i].getY() + Menu.ITEMHEIGHT){
                    // Pressed button
                    // Update the program state
                    cong.setState(cong.menu.mi[i].getAssignedState());
                    // Run any extra code
                    cong.menu.mi[i].clickAction();
                }
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
    
}
