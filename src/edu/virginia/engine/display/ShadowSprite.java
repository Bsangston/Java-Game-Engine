package edu.virginia.engine.display;

//import javafx.scene.effect.Shadow;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by BrandonSangston on 3/26/17.
 */
public class ShadowSprite extends Sprite {

    private BufferedImage shadow;
    private boolean isShadow;
    private boolean onlyShadow;

    public ShadowSprite(String id, String filename, String shadow_filename) {
        super(id, filename);
        setShadow(shadow_filename);
        onlyShadow = false;

    }

    public ShadowSprite(String id, String shadow_filename) {
        super(id, null);
        setShadow(shadow_filename);
        onlyShadow = true;
        setHitbox(hitbox.x, hitbox.y, shadow.getWidth(), shadow.getHeight());
        setCollidable(false);
    }

    public ShadowSprite(String id) {
        super(id);
        onlyShadow = true;
    }

    protected void setShadow(String imageName) {
        if (imageName == null) {
            return;
        }
        shadow = readImage(imageName);
        if (shadow == null) {
            System.err.println("[DisplayObject.setImage] ERROR: " + imageName + " does not exist!");
        }
    }

    public void toggleShadow(boolean s) {
        isShadow = s;
        BufferedImage tmp = shadow;
        shadow = displayImage;
        displayImage = tmp;

        if (onlyShadow && !isShadow) {
            setCollidable(false);
        } else if (onlyShadow && isShadow) {
            setCollidable(true);
        }

    }

    public boolean isShadow() {
        return isShadow;
    }

    public void setOnlyShadow(boolean onlyShadow) {
        this.onlyShadow = onlyShadow;
        setVisible(!onlyShadow);
    }

    public boolean isOnlyShadow() {
        return onlyShadow;
    }
}
