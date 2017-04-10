package edu.virginia.engine.display;

import edu.virginia.engine.events.*;

import edu.virginia.engine.controller.GamePad;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * A very basic display object for a java based gaming engine
 * 
 * */
public class DisplayObject extends EventDispatcher {

	/* All DisplayObject have a unique id */
	private String id;

	/* The image that is displayed by this object */
	protected BufferedImage displayImage;
	private boolean visible = true;
	private Vec2 position;
	private Vec2 pivot;
	private float rotation = 0f; //in degrees
	private double scaleX = 1;
	private double scaleY = 1;
	private float alpha = 1.0f;
	private boolean facingRight = true;

	/* Display tree functionality */
	private DisplayObjectContainer parent;

	/* Collision detection */
	protected Rectangle hitbox;
	private boolean collidable = true;

	/* Physics */
	private boolean hasRigidBody = false;
	protected RigidBody2D rb2d;


	/**
	 * Constructors: can pass in the id OR the id and image's file path and
	 * position OR the id and a buffered image and position
	 */
	public DisplayObject(String id) {
		super();
		this.setId(id);
		visible = false;
		position = new Vec2(0,0);
		pivot = new Vec2(position);
	}

	public DisplayObject(String id, String fileName) {
		super();
		this.setId(id);
		this.setImage(fileName);
		visible = true;
		position = new Vec2(getUnscaledWidth()/2, getUnscaledHeight()/2);
		pivot = new Vec2(position);

		hitbox = new Rectangle(0, 0, getUnscaledWidth(), getUnscaledHeight());

	}

	public DisplayObject(DisplayObject d) {
		eventListeners = d.eventListeners;
		id = d.getId();
		displayImage = d.getDisplayImage();
		visible = d.isVisible();
		position = d.getPosition();
		pivot = d.getPivot();
		rotation = d.getRotation();
		scaleX = d.getScaleX();
		scaleY = d.getScaleY();
		alpha = d.getAlpha();
		facingRight = d.isFacingRight();

		collidable = d.isCollidable();
		hitbox = d.getLocalHitbox();

		hasRigidBody = d.hasRigidBody();
		rb2d = d.rb2d;

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
		int flipped = 1;
		if (!isFacingRight()) {
			flipped = -1;
		}
		if(displayImage == null) return 0;
		return (int)(flipped*displayImage.getWidth()*scaleX);
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
	protected void update(ArrayList<Integer> pressedKeys, ArrayList<GamePad> gamePads) {
		if (hasRigidBody) {
			rb2d.applyConstantForces();
		}

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

			//Draw Hitbox:
			g2d.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);

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

		g2d.scale(scaleX, scaleY);
		g2d.translate(-getUnscaledWidth() / 2, -getUnscaledHeight() / 2);

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

		g2d.translate(getUnscaledWidth()/2, getUnscaledHeight()/2);
		g2d.scale(1/scaleX, 1/scaleY);

		g2d.translate(-position.getX(), -position.getY());

	}

	public void flip() {
		setScaleX(-getScaleX());
		facingRight = !facingRight;
	}

	public boolean isFacingRight() {
		return facingRight;
	}

	public boolean wasClicked(double mouse_x, double mouse_y) {
		if (mouse_y <= getPosY()+getScaledHeight()/2 && mouse_y >= getPosY()-getScaledHeight()/2) {
			if (mouse_x <= getPosX() + getScaledWidth()/2 && mouse_x >= getPosX()-getScaledWidth()/2) {
				return true;
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

	public Vec2 getGlobalPosition() {
		int x = getPosX(), y = getPosY();
		DisplayObject p = parent;
		while (p != null) {
			x += p.getPosX() + getPosX();
			y += p.getPosY() + getPosY();
			p = p.getParent();
		}
		return new Vec2(x, y);
	}

	public boolean collidesWith(DisplayObject other) {

		return isCollidable() && this.getHitbox().intersects(other.getHitbox());
	}

	public Rectangle getHitbox() {
		//Calculate global hitbox
		Rectangle hitbox_global = new Rectangle(hitbox);
		hitbox_global.x = (int) (hitbox.x + position.x - halfWidth());
		hitbox_global.y = (int)(hitbox.y + position.y - halfHeight());
		if (facingRight) {
			hitbox_global.width = (int) (hitbox.width * scaleX);
		} else {
			hitbox_global.width = (int) (hitbox.width * -scaleX);
		}
		hitbox_global.height = (int)(hitbox.height*scaleY);


		return hitbox_global;
	}

	public Rectangle getLocalHitbox() {
		return hitbox;
	}

	public void setHitbox(int x, int y, int width, int height) {
		hitbox.setBounds(x, y, width, height);
	}

	public void setHitbox(Rectangle hitbox) {
		this.hitbox = hitbox;
	}

	public void addRigidBody2D() {
		this.rb2d = new RigidBody2D(this);
		hasRigidBody = true;
	}

	public RigidBody2D getRigidBody() {
		return rb2d;
	}

	public void removeRigidBody2D() {
		this.rb2d = null;
		hasRigidBody = false;
	}

	public int halfWidth() {
		return getScaledWidth()/2;
	}

	public int halfHeight() {
		return getScaledHeight()/2;
	}

	public boolean hasRigidBody() {
		return hasRigidBody;
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

	public Vec2 getPosition() {
		return position;
	}

	public int getPosX() {
		return (int)position.getX();
	}

	public int getPosY() {
		return (int)position.getY();
	}

	public void setPosition(int x, int y) {
		position.setPosition(x, y);
	}

	public void setPosition(Vec2 p) { this.position = p; }

	public void setPosX(int x) {
		position.setX(x);
	}

	public void setPosY(int y) { position.setY(y); }

	public Vec2 getPivot() {
		return pivot;
	}

	public int getPivotX() {
		return (int)pivot.getX();
	}

	public int getPivotY() {
		return (int)pivot.getY();
	}

	public void setPivot(Vec2 pivot) {
		this.pivot = pivot;
	}

	public void setPivotX(int x) {
		pivot.setPosition(x, pivot.getY());
	}

	public void setPivotY(int y) {
		pivot.setPosition(pivot.getX(), y);
	}

	public double getScale() { return scaleX; }

	public double getScaleX() {
		return scaleX;
	}

	public void setScaleX(double scaleX) {
		this.scaleX = scaleX;
	}

	public void setScale(double scale) {
		this.scaleX = scale; this.scaleY = scale;
	}

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

	public int getTop() { return getHitbox().y; }

	public int getBottom() {
		return getHitbox().y + getHitbox().height;
	}

	public int getLeft() {
		return getHitbox().x;
	}

	public int getRight() {
		return getHitbox().x + getHitbox().width;
	}

	public boolean isCollidable() {
		return collidable;
	}

	public void setCollidable(boolean collidable) {
		this.collidable = collidable;
	}

	public void updatePosition(Vec2 position) {
		this.position.x += position.x;
		this.position.y += position.y;
	}

	public void moveRight(int speed) {
		if (!isFacingRight()) {
			flip();
		}

		setPosX(getPosX() + speed);
		//if (background != null) background.setPosX(background.getPosX() - 1);
	}

	public void moveLeft(int speed) {
		if (isFacingRight()) {
			flip();
		}

		setPosX(getPosX() - speed);
		//if (background != null) background.setPosX(background.getPosX() + 1);
	}

	public void moveUp(int speed) {
		setPosY(getPosY() - speed);
	}

	public void moveDown(int speed) {
		setPosY(getPosY() + speed);
	}

	public void translateRight(int speed) {
		setPosX(getPosX() + speed);
	}

	public void translateLeft(int speed) {
		setPosX(getPosX() - speed);
	}
}
