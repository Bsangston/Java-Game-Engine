package edu.virginia.engine.display;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * A very basic display object for a java based gaming engine
 * 
 * */
public class DisplayObject {

	/* All DisplayObject have a unique id */
	private String id;

	/* The image that is displayed by this object */
	private BufferedImage displayImage;
	private boolean visible;
	private Point position;
	private Point pivot;
	private double scaleX = 1;
	private double scaleY = 1;
	private float rotation = 0f; //in degrees
	private float alpha = 1.0f;
	private boolean facingRight = true;

	/* Display tree functionality */
	private DisplayObjectContainer parent;

	/**
	 * Constructors: can pass in the id OR the id and image's file path and
	 * position OR the id and a buffered image and position
	 */
	public DisplayObject(String id) {
		this.setId(id);
		visible = false;
		position = new Point(0,0);
		pivot = new Point(position);
	}

	public DisplayObject(String id, String fileName) {
		this.setId(id);
		this.setImage(fileName);
		visible = true;
		position = new Point(0,0);
		pivot = new Point(position);
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}


	/**
	 * Returns the unscaled width and height of this display object
	 * */
	public int getUnscaledWidth() {
		if(displayImage == null) return 0;
		return displayImage.getWidth();
	}

	public int getUnscaledHeight() {
		if(displayImage == null) return 0;
		return displayImage.getHeight();
	}

	public int getScaledWidth() {
		if(displayImage == null) return 0;
		return (int)(displayImage.getWidth()*scaleX);
	}

	public int getScaledHeight() {
		if(displayImage == null) return 0;
		return (int)(displayImage.getHeight()*scaleY);
	}

	public BufferedImage getDisplayImage() {
		return this.displayImage;
	}

	protected void setImage(String imageName) {
		if (imageName == null) {
			return;
		}
		displayImage = readImage(imageName);
		if (displayImage == null) {
			System.err.println("[DisplayObject.setImage] ERROR: " + imageName + " does not exist!");
		}
	}

	/**
	 * Helper function that simply reads an image from the given image name
	 * (looks in resources\\) and returns the bufferedimage for that filename
	 * */
	public BufferedImage readImage(String imageName) {
		BufferedImage image = null;
		try {
			String file = ("resources" + File.separator + imageName);
			image = ImageIO.read(new File(file));
		} catch (IOException e) {
			System.out.println("[Error in DisplayObject.java:readImage] Could not read image " + imageName);
			e.printStackTrace();
		}
		return image;
	}

	public void setImage(BufferedImage image) {
		if(image == null) return;
		displayImage = image;
	}


	/**
	 * Invoked on every frame before drawing. Used to update this display
	 * objects state before the draw occurs. Should be overridden if necessary
	 * to update objects appropriately.
	 * */
	protected void update(ArrayList<Integer> pressedKeys) {
		
	}

	/**
	 * Draws this image. This should be overloaded if a display object should
	 * draw to the screen differently. This method is automatically invoked on
	 * every frame.
	 * */
	public void draw(Graphics g) {
		
		if (displayImage != null) {
			
			/*
			 * Get the graphics and apply this objects transformations
			 * (rotation, etc.)
			 */
			Graphics2D g2d = (Graphics2D) g;
			applyTransformations(g2d);

			/* Actually draw the image, perform the pivot point translation here */
			if (visible) {
				g2d.drawImage(displayImage, 0, 0, getUnscaledWidth(), getUnscaledHeight(), null);
				//g2d.drawRect(getPivotX() - 10, getPivotY() - 10, 20, 20); //for pivot point debugging

			}
			/*
			 * undo the transformations so this doesn't affect other display
			 * objects
			 */
			reverseTransformations(g2d);

		}
	}

	/**
	 * Applies transformations for this display object to the given graphics
	 * object
	 * */
	protected void applyTransformations(Graphics2D g2d) {

		g2d.translate(position.getX(), position.getY());

		//g2d.translate(getUnscaledWidth()/2, getUnscaledHeight());
		g2d.scale(scaleX, scaleY);
		//g2d.translate(getUnscaledWidth()/2, getUnscaledHeight());

		AlphaComposite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		g2d.setComposite(comp);

		g2d.rotate(Math.toRadians(rotation), pivot.getX(), pivot.getY());


	}

	/**
	 * Reverses transformations for this display object to the given graphics
	 * object
	 * */
	protected void reverseTransformations(Graphics2D g2d) {

		g2d.rotate(Math.toRadians(-rotation), pivot.getX(), pivot.getY());

		AlphaComposite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
		g2d.setComposite(comp);

		//g2d.translate(-getUnscaledWidth()/2, -getUnscaledWidth()/2);
		g2d.scale(1/scaleX, 1/scaleY);
		//g2d.translate(getUnscaledWidth()/2, getUnscaledWidth()/2);

		g2d.translate(-position.getX(), -position.getY());


	}

	public void flip() {
		setScaleX(-getScaleX());
		setPosX(getPosX() - getScaledWidth());
		facingRight = !facingRight;
	}

	public boolean isFacingRight() {
		return facingRight;
	}

	public boolean wasClicked(double mouse_x, double mouse_y) {
		if (mouse_y > getPosY() && mouse_y < getScaledHeight() + getPosY()) {
			if (facingRight) {
				if (mouse_x > getPosX() && mouse_x < getScaledWidth() + getPosX()) {
					return true;
				}
			} else {
				if (mouse_x < getPosX() && mouse_x > getScaledWidth() + getPosX()) {
					return true;
				}
			}
		}
		return false;
	}

	//Bounded for use in update
	public void increaseAlpha(float amt) {
		if (alpha < 1.0f) {
			if (alpha + amt > 1.0f) {
				alpha = 1.0f;
			} else {
				alpha += amt;
			}
		}
	}

	//Bounded for use in update
	public void decreaseAlpha(float amt) {
		if (alpha > 0.05f) {
			if (alpha - amt < 0.05f) {
				alpha = 0.05f;
			} else {
				alpha -= amt;
			}
		}
	}

	@Override
	public boolean equals(Object o) {
		DisplayObject d = (DisplayObject) o;
		return id.equalsIgnoreCase(d.getId());
	}

	public void centerPivot() {
		if (displayImage != null) {
			setPivotX(getUnscaledWidth() / 2);
			setPivotY(getUnscaledHeight() / 2);
		}
		setPosition(getPosX() - getPivotX(),getPosY() - getPivotY());
	}

	public Point getGlobalPosition() {
		int x = getPosX(), y = getPosY();
		DisplayObject p = parent;
		while (p != null) {
			x += p.getPosX() + getPosX();
			y += p.getPosY() + getPosY();
			p = p.getParent();
		}
		return new Point(x, y);
	}



	public void setParent(DisplayObjectContainer parent) {
		this.parent = parent;
	}

	public DisplayObjectContainer getParent() {
		return parent;
	}

	public void setFacingRight(boolean val) { facingRight = val; }

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Point getPosition() {
		return position;
	}

	public int getPosX() {
		return (int)position.getX();
	}

	public int getPosY() {
		return (int)position.getY();
	}

	public void setPosition(int x, int y) {
		position.setLocation(x, y);
	}

	public void setPosition(Point p) { this.position = p; }

	public void setPosX(int x) {
		position.setLocation(x, position.getY());
	}

	public void setPosY(int y) {
		position.setLocation(position.getX(), y);
	}

	public Point getPivot() {
		return pivot;
	}

	public int getPivotX() {
		return (int)pivot.getX();
	}

	public int getPivotY() {
		return (int)pivot.getY();
	}

	public void setPivot(Point pivot) {
		this.pivot = pivot;
	}

	public void setPivotX(int x) {
		pivot.setLocation(x, pivot.getY());
	}

	public void setPivotY(int y) {
		pivot.setLocation(pivot.getX(), y);
	}

	public double getScale() { return scaleX; }

	public double getScaleX() {
		return scaleX;
	}


	public void setScaleX(double scaleX) {
		this.scaleX = scaleX;
	}

	public void setScale(double scale) { this.scaleX = scale; this.scaleY = scale; }

	public double getScaleY() {
		return scaleY;
	}

	public void setScaleY(double scaleY) {
		this.scaleY = scaleY;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}


}