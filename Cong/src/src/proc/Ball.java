package src.proc;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import src.interfaces.EntityB;
import src.libs.Animation;

public class Ball extends GameObject implements EntityB{
    private double velX, velY, initVelX, initVelY;
    private boolean bouncing;
    public BufferedImage[] img_ball = new BufferedImage[6];
    Animation anim;
    Game game;
    SpriteSheet ss;
    
    public Ball(double x, double y, double initVelX, double initVelY, Game game, SpriteSheet ss){
        super(x, y);
        this.game = game;
        this.ss = ss;
        
        // build animation image array
        img_ball[0] = ss.grabImage(4, 1, 14, 14 );
        img_ball[1] = ss.grabImage(4, 2, 14, 14 );
        img_ball[2] = ss.grabImage(4, 3, 14, 14 );
        img_ball[3] = ss.grabImage(4, 4, 14, 14 );
        img_ball[4] = ss.grabImage(4, 3, 14, 14 );
        img_ball[5] = ss.grabImage(4, 2, 14, 14 );
        bouncing = false;
        this.initVelX = initVelX;
        this.initVelY = initVelY;
        
        anim = new Animation(1, img_ball[0], img_ball[1], img_ball[2], img_ball[3], img_ball[4], img_ball[5]);
    }
    
    public boolean isBouncing(){
        return bouncing;
    }
    
    public void startBouncing(){
        bouncing = true;
        
        // start bouncing with the velocity from the player added
        setVelX(velX + initVelX);
        setVelY(velY + initVelY);
    }
    
    public void stopBouncing(){
        bouncing = false;
        setVelX(0);
        setVelY(0);
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
    
    public void setInitVelX(double initVelX){
        this.initVelX = initVelX;
    }
    
    public void setInitVelY(double initVelY){
        this.initVelY = initVelY;
    }
    
    public void setVelX(double velX){
        this.velX = velX;
    }
    
    public void setVelY(double velY){
        this.velY = velY;
    }
    
    public Rectangle getBounds(){
        return new Rectangle((int)x, (int)y, 14, 14);
    }
    
    public void tick(){
        
        if(bouncing){
            x+=velX;
            y+=velY;

            // If the ball passes out of the screen, stop movement
            if(x < -10){
                x = game.gameWindow.getX();
                bouncing = false;
                try {
                    game.incrementRound(game.p2);
                } 
                catch (IOException ex) {}
            }
            if(x > (Cong.WIDTH * Cong.SCALE) + 10){
                x = game.gameWindow.getWidth() + game.offset_x;
                bouncing = false;
                try {
                    game.incrementRound(game.p1);
                } 
                catch (IOException ex) {}
            }

            // If the ball reaches the top or bottom gamewindow boundary, reverse the Y velocity
            if(y < game.gameWindow.getY()){
                velY = -velY;
            }
            if(y >= game.gameWindow.getHeight() + game.offset_y - 14){
                velY = -velY;
            }

            // If the ball collides with the player's congs, reverse the X velocity and increase by 1
            // Player 1
            if((Physics.Collision(this, game.p1) && this.getX() >= game.p1.getX() + 6) 
                //||(Physics.Collision(this, game.p2) && this.getX() <= game.p2.getX() + 1)
              ){
                velY = velY + (game.p1.getVelY() / 5);
                velX = -velX;
                if (Math.abs(velX) < 13){
                    if(velX < 0){
                        velX -= 1;
                    }
                    else{
                        velX += 1;
                    }
                }
            }
            // Player 2
            if((Physics.Collision(this, game.p2) && this.getX() <= game.p2.getX() + 1)){
                velY = velY + (game.p2.getVelY() / 5);
                velX = -velX;
                if (Math.abs(velX) < 13){
                    if(velX < 0){
                        velX -= 1;
                    }
                    else{
                        velX += 1;
                    }
                }
            }
        }
        else{
            x+=velX;
            y+=velY;
        }
        // Always trigger the animation
        anim.runAnimation();
    }
    
    public void render(Graphics g){
        // draw the next frame
        anim.drawAnimation(g, x, y, 0);
        
        //Graphics2D g2d = (Graphics2D) g;
        //g2d.setColor(Color.green);
        //g2d.draw(this.getBounds());
    }
}
