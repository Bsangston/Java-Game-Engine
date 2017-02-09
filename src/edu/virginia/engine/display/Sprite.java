package edu.virginia.engine.display;

import sun.awt.image.BufferedImageDevice;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Nothing in this class (yet) because there is nothing specific to a Sprite yet that a DisplayObject
 * doesn't already do. Leaving it here for convenience later. you will see!
 * */
public class Sprite extends DisplayObjectContainer {

	protected static BufferedImage[] global_sprites;

	public Sprite(String id) {
		super(id);
	}

	public Sprite(String id, String imageFileName) {
		super(id, imageFileName);
	}

	public Sprite(String id, int index) {
		super(id);
		if (global_sprites != null) { setImage(global_sprites[index]); }
	}

	//Reads sprite sheet into BufferedImage array
	public static BufferedImage[] parseSpriteSheet(BufferedImage sprite_sheet, int cols, int rows) {
		int w = sprite_sheet.getWidth()/cols;
		int h = sprite_sheet.getHeight()/rows;
		int num = 0;
		BufferedImage sprites[] = new BufferedImage[w*h];
		for(int y = 0; y < rows; y++) {
			for(int x = 0; x < cols; x++) {
				sprites[num] = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = sprites[num].createGraphics();
				g.drawImage(sprite_sheet, 0, 0, w, h, w*x, h*y, w*x+w, h*y+h, null);
				g.dispose();
				num++;
			}
		}
		return sprites;
	}

	public static void loadSpriteSheet(BufferedImage sprite_sheet, int cols, int rows) {
		global_sprites = parseSpriteSheet(sprite_sheet, cols, rows);
	}

	@Override
	public void update(ArrayList<Integer> pressedKeys) { super.update(pressedKeys);
	}

}