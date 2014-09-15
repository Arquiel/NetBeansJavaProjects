package src.proc;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Game {
    public static final String ATTACK = "ATTACKER";
    public static final String DEFEND = "DEFENDER";
    
    public static int offset_x = 50;
    public static int offset_y = 87;
    
    public Rectangle gameWindow;
    public Player p1, p2;
    public Ball b;
    public Clock clock;

    private Cong cong;
    private History h;
    private BufferedImageLoader loader = new BufferedImageLoader();
    private BufferedImage ss_cong_image;
    private BufferedImage ss_scoreboard_image;
    private SpriteSheet cong_ss;
    private SpriteSheet scoreboard_ss;
    private SpriteSheet header_ss;    
    private int roundCount = 1;
    private int initialPosX_p1 = 0;
    private int initialPosY_p1 = 0;
    private int initialPosX_p2 = 0;
    private int initialPosY_p2 = 0;
    private int gameboard_spacing_x = 22;
    private int initialBall_velX = 4;
    private int initialBall_velY = 0;
    private int initialScoreP1 = 0;
    private int initialScoreP2 = 0;
    private int maxScore = 10;
    
    public Game(Cong cong, SpriteSheet header_ss, History h){
        
        // Load the spritesheets for the game objects and the scoreboard elements
        try{
            ss_cong_image = loader.loadImage("/sprite_sheet_cong.png");
            cong_ss = new SpriteSheet(ss_cong_image);
            ss_scoreboard_image = loader.loadImage("/sprite_sheet_scoreboard.png");
            scoreboard_ss = new SpriteSheet(ss_scoreboard_image);
        }
        catch(IOException e){}
        
        // Initialize the vars and objects
        this.cong = cong;
        this.h = h;
        this.header_ss = header_ss;
        gameWindow = new Rectangle(offset_x, offset_y, Cong.WIDTH * Cong.SCALE - 100, Cong.HEIGHT * Cong.SCALE - 145);
        initialPosX_p1 = (int)gameWindow.getX() + 5;
        initialPosY_p1 = ((int)gameWindow.getHeight() / 2) + (offset_y / 2);
        initialPosX_p2 = (int)gameWindow.getWidth() + 7 + (offset_x / 2);
        initialPosY_p2 = ((int)gameWindow.getHeight() / 2) + (offset_y / 2);
        p1 = new Player("Player 1", ATTACK, initialPosX_p1, initialPosY_p1, this, cong_ss);
        p2 = new Player("Player 2", DEFEND, initialPosX_p2, initialPosY_p2, this, cong_ss);
        b = new Ball(initialPosX_p1, gameWindow.getCenterY(), initialBall_velX, initialBall_velY, this, cong_ss);
        clock = new Clock((Cong.WIDTH * Cong.SCALE) / 2 - 30, 60);
        cong.addKeyListener(new KeyInput(this));
    }
    
    public void resetGame(){
        // Reset all data from the previous game and reposition the game objects
        roundCount = 1;
        setPlayerRoles();
        clock.reset();
        p1.setScore(initialScoreP1);
        p2.setScore(initialScoreP2);
        b.stopBouncing();
        resetPositions();
    }
    
    public void incrementRound(Player roundWinner) throws IOException{
        // Round complete, add a score to the winner
        clock.stop();
        roundWinner.setScore(roundWinner.getScore() + 1);
        if(roundWinner.getScore() < maxScore){
            // continue playing
            roundCount++;
            setPlayerRoles();
            resetPositions();
        }
        else{
            // Save game and redirect
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            h.addHistory(sdf.format(d), p1.getName(), p2.getName(), p1.getScore() + ":" + p2.getScore(), clock.getTime());
            cong.setState(Cong.STATE.FINISHED);
        }
        
    }
    
    public void resetPositions(){
        // Reposition game objects
        p1.setX(initialPosX_p1);
        p1.setY(initialPosY_p1);
        p2.setX(initialPosX_p2);
        p2.setY(initialPosY_p2);
        b.setY(gameWindow.getCenterY() - 14);
        b.setVelX(0);
        b.setVelY(0);
        
        // alternate the ball's starting side
        if((roundCount % 2) == 1){
            b.setX(p1.getX() + 15);
            b.setInitVelX(initialBall_velX);
            b.setInitVelY(initialBall_velY);
        }
        else{
            b.setX(p2.getX() - 15);
            b.setInitVelX(-initialBall_velX);
            b.setInitVelY(-initialBall_velY);
        }
    }
   
    public void setPlayerRoles(){
        // alternate who launches the ball
        if((roundCount % 2) == 1){
            p1.setRole(ATTACK);
            p2.setRole(DEFEND);
        }
        else{
            p1.setRole(DEFEND);
            p2.setRole(ATTACK);
        }
    }
    
    public void tick(){
        p1.tick();
        p2.tick();
        b.tick();
        clock.tick();
    }
    
    public void render(Graphics g){
        // render the header and footer
        g.drawImage(header_ss.grabImage(1, 4, 642, 50), 0, 50, null);
        g.drawImage(header_ss.grabImage(1, 5, 642, 50), 0, Cong.HEIGHT * Cong.SCALE - 65, null);
        
        // render the score
        for(int i = maxScore; i >= 1; i--){
            if(p1.getScore() >= i){
                // Score for p1
                g.drawImage(scoreboard_ss.grabImage(2, 1, 12, 12), (gameboard_spacing_x * i) - 15, Cong.HEIGHT * Cong.SCALE - 48, null);
            }
            else{
                // no score for p1
                g.drawImage(scoreboard_ss.grabImage(1, 1, 12, 12), (gameboard_spacing_x * i) - 15, Cong.HEIGHT * Cong.SCALE - 48, null);
            }
            
            if(p2.getScore() >= i){
                // Score for p2
                g.drawImage(scoreboard_ss.grabImage(2, 1, 12, 12), (Cong.WIDTH * Cong.SCALE) - (gameboard_spacing_x * i) + 5, Cong.HEIGHT * Cong.SCALE - 48, null);
            }
            else{
                // no score for p2
                g.drawImage(scoreboard_ss.grabImage(1, 1, 12, 12), (Cong.WIDTH * Cong.SCALE) - (gameboard_spacing_x * i) + 5, Cong.HEIGHT * Cong.SCALE - 48, null);
            }
        }
        
        p1.render(g);
        p2.render(g);
        b.render(g);
        clock.render(g);
        
        //Graphics2D g2d = (Graphics2D) g;
        //g2d.setColor(Color.green);
        //g2d.draw(gameWindow);
    }
    
    // ACTION Methods
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        // Move player 1
        if (key == KeyEvent.VK_W) {
            p1.setVelY(-5);
        } 
        else if (key == KeyEvent.VK_S) {
            p1.setVelY(5);
        }
        
        // launch ball from player 1's side
        else if ((key == KeyEvent.VK_D) && (p1.getRole() == ATTACK) && !b.isBouncing()) {
            b.setVelY(p1.getVelY());
            b.startBouncing();
            clock.start();
        } 
        
        // Move player 2
        else if (key == KeyEvent.VK_DOWN) {
            p2.setVelY(5);
        } 
        else if (key == KeyEvent.VK_UP) {
            p2.setVelY(-5);
        }
        else if (key == KeyEvent.VK_LEFT) {
            p2.setVelX(-5);
        }
        else if (key == KeyEvent.VK_RIGHT) {
            p2.setVelX(5);
        }
        
        // Launch ball from player 2's side
        else if ((key == KeyEvent.VK_SHIFT) && (p2.getRole() == ATTACK) && !b.isBouncing()) {
            b.setVelY(p2.getVelY());
            b.startBouncing();
            clock.start();
        }
    }

    public void keyReleased(KeyEvent e) {
        // Stop moving players
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            p1.setVelY(0);
        } 
        else if (key == KeyEvent.VK_S) {
            p1.setVelY(0);
        } 
        else if (key == KeyEvent.VK_DOWN) {
            p2.setVelY(0);
        } 
        else if (key == KeyEvent.VK_UP) {
            p2.setVelY(0);
        }
        else if (key == KeyEvent.VK_LEFT) {
            p2.setVelX(0);
        }
        else if (key == KeyEvent.VK_RIGHT) {
            p2.setVelX(0);
        }
    }
}
