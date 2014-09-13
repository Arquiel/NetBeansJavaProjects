package src.proc;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import src.interfaces.EntityA;
import src.interfaces.EntityB;
import src.libs.Animation;

public class Player extends GameObject implements EntityA{
    private double velX;
    private double velY;
    private String name;
    private int score;
    private String role;
    private BufferedImage[] img_player = new BufferedImage[6];
    private Animation anim;
    private SpriteSheet ss;
    private Game game;
    private boolean collided = false;
    private int animCount = 0;
    private int animLength = 2;
    
    public Player(String name, String role, double x, double y, Game game, SpriteSheet ss){
        super(x, y);
        this.game = game;
        this.name = name;
        this.role = role;
        this.ss = ss;
        score = 0;
        
        // build animation image array
        img_player[0] = ss.grabImage(3, 1, 14, 75 );
        img_player[1] = ss.grabImage(3, 1, 14, 75 );
        img_player[2] = ss.grabImage(3, 1, 14, 75 );
        img_player[3] = ss.grabImage(2, 1, 14, 75 );
        img_player[4] = ss.grabImage(2, 1, 14, 75 );
        img_player[5] = ss.grabImage(1, 1, 14, 75 );
        
        anim = new Animation(animLength, img_player[0], img_player[1], img_player[2], img_player[3], img_player[4], img_player[5]);
    }
    
    public String getName(){
        return name;
    }
    
    public String getRole(){
        return role;
    }
    
    public void setRole(String role){
        this.role = role;
    }
    
    public int getScore(){
        return score;
    }
    
    public void setScore(int score){
        this.score = score;
    }
    
    public double getX(){
        return x;
    }
    
    public double getY(){
        return y;
    }
    
    public void setX(double x){
        this.x = x;
    }
    
    public void setY(double y){
        this.y = y;
    }
    
    public double getVelX(){
        return velX;
    }
    
    public double getVelY(){
        return velY;
    }
    
    public void setVelX(double velX){
        this.velX = velX;
    }
    
    public void setVelY(double velY){
        this.velY = velY;
    }
    
    public Rectangle getBounds(){
        return new Rectangle((int)x, (int)y, 14, 75);
    }
    
    public void tick(){
        // move the player
        x+=velX;
        y+=velY;
        
        if(x < game.gameWindow.getX()){
            x = game.gameWindow.getX();
        }
        if(x >= game.gameWindow.getWidth() + game.offset_x){
            x = game.gameWindow.getWidth() + game.offset_x;
        }
        if(y < game.gameWindow.getY()){
            y = game.gameWindow.getY() + 2;
        }
        if(y >= game.gameWindow.getHeight() + 11){
            y = game.gameWindow.getHeight() + 11;
        }
        
        // if the ball is not bouncing, move the ball with the player
        if(role == Game.ATTACK && !game.b.isBouncing()){
            if((x >= game.gameWindow.getX()) && (x <= game.gameWindow.getWidth() + game.offset_x)){
                game.b.setVelX(velX);
            }
            if((y >= game.gameWindow.getY()) && (y <= game.gameWindow.getHeight() + 11)){
                game.b.setY(this.getBounds().getCenterY() - 7);
            }
        }
        // If the ball collides with the player's cong, flag as collided for the animation
        if(Physics.Collision(this, game.b)){
            collided = true;
        }
        
        // trigger animation if colliding with the ball
        if(collided){
            anim.runAnimation();
        }
    }
    
    public void render(Graphics g){
        if(collided){
            // run animation for its entire duration
            anim.drawAnimation(g, x, y, 0);
            animCount++;
            if(img_player.length + 1 == animCount/animLength){
                // Stop animation
                collided = false;
                animCount = 0;
            }
        }
        else{
            // draw static image
            g.drawImage(ss.grabImage(1, 1, 14, 75 ), (int)x, (int)y, null);
        }
        //Graphics2D g2d = (Graphics2D) g;
        //g2d.setColor(Color.green);
        //g2d.draw(this.getBounds());
    }
}
