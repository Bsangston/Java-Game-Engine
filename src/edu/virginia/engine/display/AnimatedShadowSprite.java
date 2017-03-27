package edu.virginia.engine.display;

import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Created by BrandonSangston on 3/26/17.
 */
public class AnimatedShadowSprite extends AnimatedSprite{

    private BufferedImage shadow;
    private BufferedImage[] shadowFrameList;
    private boolean isShadow;

    public AnimatedShadowSprite(String id, String imageFileName, String shadowFileName, int cols, int rows)
    {
        super(id, imageFileName, cols, rows);
        isShadow = false;
        setShadow(shadowFileName);
        shadowFrameList = parseSpriteSheet(shadow, cols, rows);

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
        BufferedImage[] tmp = shadowFrameList;
        shadowFrameList = frameList;
        frameList = tmp;

    }

    public boolean isShadow() {
        return isShadow;
    }

}
