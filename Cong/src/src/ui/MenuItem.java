package src.ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import src.proc.Cong;
import src.proc.Game;
import src.proc.SpriteSheet;

public class MenuItem {
    
    public Rectangle item;
    private Cong cong;
    private int state;
    private int itemIndex;
    private SpriteSheet ss;
    private int x,y;
    private Cong.STATE assignedState;
    
    public MenuItem(Cong cong, SpriteSheet ss, int itemIndex, Cong.STATE assignedState, int position_y){
        // Instanciate the item
        state = 1;
        this.cong = cong;
        this.itemIndex = itemIndex;
        this.ss = ss;
        this.assignedState = assignedState;
        item = new Rectangle(Cong.WIDTH / 2 + 105, 
                             position_y, 
                             Menu.ITEMWIDTH, 
                             Menu.ITEMHEIGHT);
        
    }
    
    public void setState(int state){
        this.state = state;
    }
    
    public int getState(){
        return state;
    }
    
    public Cong.STATE getAssignedState(){
        return assignedState;
    }
    
    public int getX(){
        return item.x;
    }
    
    public int getY(){
        return item.y;
    }
    
    public void clickAction(){
        // extra code that runs after a successful state change
        switch(assignedState){
            case SETUP:
                cong.setState(Cong.STATE.GAME);
                cong.game.resetGame(); //Move this to "GAME" when the "SETUP" window is used
                break;
            case GAME:
                break;
            case HISTORY:
                break;
            case EXIT:
                try {
                    System.exit(0);
                }
                catch (Exception e) {
                    //e.printStackTrace();
                }
                break;
        }
    }
    
    public Rectangle getBounds(){
        return new Rectangle(item.x, item.y, Menu.ITEMWIDTH, Menu.ITEMHEIGHT);
    }
    
    public void render(Graphics g){
        // change the hover state
        if(cong.mRegion.intersects(this.getBounds())){
            state = 2;
        }
        else{
            state = 1;
        }
        
        // render the appropriate image
        g.drawImage(ss.grabImage(itemIndex, state, Menu.ITEMWIDTH, Menu.ITEMHEIGHT ), item.x, item.y, null);

//        Graphics2D g2d = (Graphics2D) g;
//        g2d.setColor(Color.green);
//        g2d.draw(item);
    }  
}

