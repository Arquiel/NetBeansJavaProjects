/*
    ----------------------------------------------------------------------------------------
    IMPORTANT!! Do NOT run this Jar file outside of the project directory structure. 
    The history log file is not compiled within it and the program will break if done so

    This program was built with Java 7. Please ensure you are running the latest copy.
    ----------------------------------------------------------------------------------------
*/

package src.proc;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JFrame;
import src.ui.Menu;

public class Cong extends Canvas implements Runnable {
    // VARIABLES
    // Constant vars
    public static final int WIDTH =  320; //320
    public static final int HEIGHT = WIDTH / 12 * 9; // Maintains a 12 X 9 aspect ratio relative to the the provided width
    public static final int SCALE = 2;
    public final String TITLE = "Cong";
    public Menu menu;
    public Menu sub_menu;
    public Rectangle mRegion;
    public Game game;
    public static History h; 
    
    public static enum STATE{
        MENU,       // First screen shown when program is run 
        HISTORY,    // Screen accessible from menu
        SETUP,      // Screen accessible from menu (Currently not in use)
        GAME,       // Screen accessible from setup
        FINISHED,   // Screen resolved from game
        EXIT        // Screen before close
    };
    // Possible program states: These represent each screen state the user will view when running the program
    
    // Private vars
    private static JFrame frame;
    private boolean running;
    private Thread thread;
    private Player p1;
    private Player p2;
    private BufferedImage background = null;
    private static STATE State = STATE.MENU;
    private SpriteSheet header_ss;
       
    // MAIN Method
    public static void main(String[] args) throws IOException {
        // Instanciate new program
        Cong cong = new Cong();
        h = new History("History.csv");
        //h = new History("/History.txt");
        // Define canvas dimensions
        cong.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        cong.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        cong.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        
        
        // Build frame and add Cong instance (canvas)
        frame = new JFrame(cong.TITLE);
        frame.add(cong);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        // Start the thread
	cong.start();
    }
    
    
    // GAME Methods
    private void init(){
        // Focus on the screen automatically
        requestFocus();
        
        BufferedImageLoader loader = new BufferedImageLoader();

	try{
	    BufferedImage headers_image = loader.loadImage("/sprite_sheet_header.png");
            header_ss = new SpriteSheet(headers_image);
            background = loader.loadImage("/background.jpg");
	}
	catch(IOException e){
            System.out.println("error loading background and spritesheet");
	}
        
        menu = new Menu(this);
        
        mRegion = new Rectangle(10,10);
        game = new Game(this, header_ss, h);
        addMouseListener(new MouseInput(this));
    }
    
    private synchronized void start(){
        if(running){
            return;
        }

        running = true;
        thread = new Thread(this);
        thread.start();
    }
    
    private synchronized void stop(){
        if(!running){
            return;
        }

        running = false;
        try {
            thread.join();
        }
        catch (InterruptedException e) {
	    e.printStackTrace();
        }
        try {
            System.exit(0);
        }
        catch (Exception e) {
	   // e.printStackTrace();
        }
    }
    
    // run() is called when a new thread is created. start() creates a new thread 
    public void run(){
        init();
	long lastTime = System.nanoTime();
        final double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
	int updates = 0;
	int frames = 0;
	long timer = System.currentTimeMillis();

        while(running){
	    long now = System.nanoTime();
	    delta += (now - lastTime) / ns;
	    lastTime = now;
	    if(delta >= 1){
		tick();
		updates++;
		delta--;
	    }
	    render();
	    frames++;

	    if(System.currentTimeMillis() - timer > 1000){
		timer += 1000;
		System.out.println(updates + " Ticks, FPS " + frames);
		updates = 0;
		frames = 0;
	    }
        }

        stop();
    }
    
    private void tick(){
        if(State == STATE.GAME){
            game.tick();
        }
    }
    
    private void render(){
        BufferStrategy bs = this.getBufferStrategy();
	if(bs == null){
	    createBufferStrategy(3);
	    return;
	}
	Graphics g = bs.getDrawGraphics();

	g.drawImage(background, 0, 0, null); 
	
        Point mCoordinates = MouseInfo.getPointerInfo().getLocation();
        Point wCoordinates = this.getLocationOnScreen();
        int x = (int) mCoordinates.getX() - (int) wCoordinates.getX();
        int y = (int) mCoordinates.getY() - (int) wCoordinates.getY();
        mRegion.setLocation(x-mRegion.width/2, y-mRegion.height/2); 
        menu.rebuildMenu();
        menu.render(g);
        switch(State){
            case MENU:
                g.drawImage(header_ss.grabImage(1, 1, 642, 50), 0, 50, null);
                break;
            case HISTORY:
                g.drawImage(header_ss.grabImage(1, 2, 642, 50), 0, 50, null);
                h.render(g);
                break;
            case SETUP:
                g.drawImage(header_ss.grabImage(1, 3, 642, 50), 0, 50, null);
                break;
            case GAME:
                game.render(g);
                break;
            case FINISHED:
                g.drawImage(header_ss.grabImage(1, 6, 642, 50), 0, 50, null);
                break;
            case EXIT:
                break;
        }
        
        g.dispose();
	bs.show();
    }
    
    // GETTER & SETTER Methods
    public STATE getState() {
        return State;
    }
    
    public void setState(STATE State) {
        this.State = State;
    }

}
