package src.proc;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Clock {
    private boolean ticking;
    private BufferedImageLoader loader = new BufferedImageLoader();
    private BufferedImage ss_clock_image;
    private SpriteSheet clock_ss;
    private int millisecondsExpired;
    private int minutesExpired;
    private int secondsExpired;
    private int charSpacing = 14;
    private double x;
    private double y;
    
    public Clock(double x, double y){
        // Instanciate the clock spritesheet and vars
        try{
            ss_clock_image = loader.loadImage("/sprite_sheet_clock.png");
            clock_ss = new SpriteSheet(ss_clock_image);
        }
        catch(IOException e){}
        this.x = x;
        this.y = y;
        millisecondsExpired = 0;
        ticking = false;
    }
    
    public void start(){
        ticking = true;
    }
    
    public void stop(){
        ticking = false;
    }
    public void reset(){
        millisecondsExpired = 0;
        ticking = false;
    }
    
    public String getTime(){
        // return a formatted string 
        int minutes1 = minutesExpired / 10;
        int minutes2 = minutesExpired % 10;
        int seconds1 = secondsExpired / 10;
        int seconds2 = secondsExpired % 10;
        return minutes1 + "" + minutes2 + ":" + seconds1 + "" + seconds2;
    }

    public void tick(){
        // add time if ticking
        if(ticking){
           millisecondsExpired++; 
        }
    }
    public void render(Graphics g){
        // Redraw the clock with the current elapsed time
        minutesExpired = millisecondsExpired / 60 / 60;
        secondsExpired = (millisecondsExpired / 60) % 60;
        int minutes1 = minutesExpired / 10;
        int minutes2 = minutesExpired % 10;
        int seconds1 = secondsExpired / 10;
        int seconds2 = secondsExpired % 10;
        
        // The "0" character is #10, so re-assign the value if equals 0
        if(minutes1 == 0){
            minutes1 = 10;
        }

        if(minutes2 == 0){
            minutes2 = 10;
        }

        if(seconds1 == 0){
            seconds1 = 10;
        }

        if(seconds2 == 0){
            seconds2 = 10;
        }
        
        // Draw the 5 characters
        // Minutes
        g.drawImage(clock_ss.grabImage(minutes1, 1, 14, 13 ), (int)x + (charSpacing * 0), (int)y, null);
        g.drawImage(clock_ss.grabImage(minutes2, 1, 14, 13 ), (int)x + (charSpacing * 1), (int)y, null);
        // Colon
        g.drawImage(clock_ss.grabImage(11, 1, 14, 13 ), (int)x + (charSpacing * 2), (int)y, null);
        // Seconds
        g.drawImage(clock_ss.grabImage(seconds1, 1, 14, 13 ), (int)x + (charSpacing * 3), (int)y, null);
        g.drawImage(clock_ss.grabImage(seconds2, 1, 14, 13 ), (int)x + (charSpacing * 4), (int)y, null);  
    }
}
