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

	private static float jumpForce = 0.85f;

	protected static BufferedImage[] global_sprites;

	public Sprite(String id) {
		super(id);
	}

	public Sprite(String id, String imageFileName) {
		super(id, imageFileName);
		centerPivot();
	}

	public Sprite(String id, int index) {
		super(id);
		if (global_sprites != null) {
			setImage(global_sprites[index]);
		}
	}

	public Sprite(Sprite s) {
		super(s);
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

	public boolean onTriggerEnter(Sprite other) {
		if ((other.getPosY() <= getPosY()+getScaledHeight()/2 && other.getPosY() >= getPosY()-getScaledHeight()/2)
				&& (other.getPosX() <= getPosX() + getScaledWidth()/2
				&& other.getPosX() >= getPosX() - getScaledWidth()/2)) {
			return true;
		}
		return false;
	}

	public boolean onTriggerExit(Sprite other) {
		if (onTriggerEnter(other)) {
			int flipped = 1;
			if (!isFacingRight()) {
				flipped = -1;
			}
			if ((other.getPosY() > getPosY()+getScaledHeight()/2 && other.getPosY() < getPosY()-getScaledHeight()/2)
					&& (other.getPosX() > getPosX() + flipped * getScaledWidth()/2
					&& other.getPosX() < getPosX() - flipped * getScaledWidth()/2)) {
				return true;
			}
		}
		return false;
	}



	public boolean inBounds(Game game) {
		return getPosX() <= game.getMainFrame().getWidth() - getScaledWidth()/2 &&
				getPosX() >= getScaledWidth()/2 && getPosY() >= getScaledHeight()/2 &&
				getPosY() <= game.getMainFrame().getHeight() - getScaledHeight()/2;
	}

	public void jump() {
		if (rb2d != null) {
			rb2d.applyForce(new Vector2D(1f, jumpForce));
		}
	}

	@Override
	public void update(ArrayList<Integer> pressedKeys) {
		super.update(pressedKeys);
	}

	@Override
	public void draw(Graphics g) { super.draw(g); }

}