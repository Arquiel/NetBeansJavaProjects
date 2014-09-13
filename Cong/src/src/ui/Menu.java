package src.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import src.proc.BufferedImageLoader;
import src.proc.Cong;
import src.proc.SpriteSheet;

public class Menu {
    
    public static final int ITEMWIDTH =  120;
    public static final int ITEMHEIGHT =  20;
    
    public MenuItem[] mi;
    
    private Cong cong;
    private BufferedImageLoader loader = new BufferedImageLoader();
    private BufferedImage ss_image;
    private SpriteSheet ss;
    private MenuItem item;
    private int initialPosition_y = 175;
    private int position_y = initialPosition_y;
    private int spacing_y = 50;
    
    public Menu(Cong cong){
        this.cong = cong;
        // load the menu spritesheet
        try{
            ss_image = loader.loadImage("/sprite_sheet_menu.png");
            ss = new SpriteSheet(ss_image);
        }
        catch(IOException e){
	}
        
        // build the menu for the current state
        rebuildMenu();
    }
    
    public void rebuildMenu(){
        // build the menu for the current state
        switch(cong.getState()){
            case MENU:
                mi = new MenuItem[3];
                
                for(int i = 0; i < mi.length; i++){
                    Cong.STATE state = Cong.STATE.MENU;

                    switch(i){
                        case 0:
                            state = Cong.STATE.SETUP;
                            break;
                        case 1:
                            state = Cong.STATE.HISTORY;
                            break;
                        case 2:
                            state = Cong.STATE.EXIT;
                            break;
                    }
                    position_y = initialPosition_y + (spacing_y * i);
                    mi[i] = new MenuItem(cong, ss, i+1, state, position_y);
                }
                break;
                
            case SETUP:
                // only one menu item, but the mouse click handler uses an array, so it is saved as an array
                mi = new MenuItem[1];
                position_y = 440;
                mi[0] = new MenuItem(cong, ss, 4, Cong.STATE.MENU, position_y);
                break;
                
            case HISTORY:
                // only one menu item, but the mouse click handler uses an array, so it is saved as an array
                mi = new MenuItem[1];
                position_y = 440;
                mi[0] = new MenuItem(cong, ss, 4, Cong.STATE.MENU, position_y);
                break;
            case GAME:
                // only one menu item, but the mouse click handler uses an array, so it is saved as an array
                mi = new MenuItem[1];
                position_y = 440;
                mi[0] = new MenuItem(cong, ss, 4, Cong.STATE.MENU, position_y);
                break;
            case FINISHED:
                mi = new MenuItem[3];
                position_y = 175;
                for(int i = 0; i < mi.length; i++){
                    Cong.STATE state = Cong.STATE.MENU;

                    switch(i){
                        case 0:
                            state = Cong.STATE.SETUP;
                            break;
                        case 1:
                            state = Cong.STATE.HISTORY;
                            break;
                    }
                    position_y = initialPosition_y + (spacing_y * i);
                    mi[i] = new MenuItem(cong, ss, i+1, state, position_y);

                }
                
                position_y = 440;
                mi[2] = new MenuItem(cong, ss, 4, Cong.STATE.MENU, position_y);
                
                break;
        }
    }
    
    public void render(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        
        // Render the menu items
        for(int i = 0; i < mi.length; i++){
	    item = mi[i];
	    item.render(g);
	}
    }
}
