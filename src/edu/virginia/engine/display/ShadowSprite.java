package edu.virginia.engine.display;

import javafx.scene.effect.Shadow;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by BrandonSangston on 3/26/17.
 */
public class ShadowSprite extends Sprite {

    private BufferedImage shadow;
    private boolean isShadow;
    private boolean invisible;

    public ShadowSprite(String id, String filename, String shadow_filename) {
        super(id, filename);
        setShadow(shadow_filename);
        invisible = false;

    }

    public ShadowSprite(String id, String shadow_filename) {
        super(id, null);
        setShadow(shadow_filename);
        invisible = true;
    }

    public ShadowSprite(String id) {
        super(id);
        invisible = true;
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

    }

    public boolean isShadow() {
        return isShadow;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public boolean isInvisible() {
        return invisible;
    }
}
