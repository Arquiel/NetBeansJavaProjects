
package src.proc;

import java.awt.image.BufferedImage;

public class SpriteSheet {

    private BufferedImage image;

    public SpriteSheet(BufferedImage image){
	this.image = image;
    }

    public BufferedImage grabImage(int col, int row, int width, int height){
	// grab sub image with the width and height as the grid cell size. col and width dente which cell to grab
        BufferedImage img = image.getSubimage((col * width) - width, (row * height) - height, width, height);
	return img;
    }
}
