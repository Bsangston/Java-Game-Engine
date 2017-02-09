package edu.virginia.engine.display;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by BrandonSangston on 2/8/17.
 */
public class DisplayObjectContainer extends DisplayObject{

    private ArrayList<DisplayObject> children;

    public DisplayObjectContainer(String id) {
        super(id);
        children = new ArrayList<>();
    }

    public DisplayObjectContainer(String id, String fileName) {
        super(id, fileName);
        children = new ArrayList<>();
    }

    @Override
    public void update(ArrayList<Integer> pressedKeys) {
        super.update(pressedKeys); //update myself

        for (DisplayObject child : children) { //update each child
            child.update(pressedKeys);
        }

    }

    @Override
    public void draw(Graphics g) {
        super.draw(g); //draw this object

        Graphics2D g2d = (Graphics2D)g;

        applyTransformations(g2d); //apply transformations to children

        for (DisplayObject child : children) { //draw each child
            child.draw(g);
        }
        reverseTransformations(g2d); //reverse transformations

    }


    /*Returns true if child successfully added, false if not*/
    public boolean addChild(DisplayObject d) {
        return children.add(d);
    }

    /*Returns true if child successfully added, false if not*/
    public void addChildAtIndex(DisplayObject d, int index) {
        children.add(index, d);
    }

    /*Returns true if child successfully removed false if not*/
    public boolean removeChild(DisplayObject d) {
        return children != null && children.remove(d);
    }

    /*Returns true if child successfully removed false if not*/
    public DisplayObject removeChildAtIndex(int index) {
        return children.remove(index);
    }

    /*Returns true if child successfully removed false if not*/
    public boolean removeChildren() {
        if (!children.isEmpty()) {
            children.clear();
            return true;
        }
        return false;
    }

    public boolean contains(DisplayObject d) {
        return children != null && children.contains(d);
    }


    public ArrayList<DisplayObject> getChildren() {
        return children;
    }

    public DisplayObject getChild(String id) {
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).getId().equals(id)) {
                return children.get(i);
            }
        }
        return null;
    }

    public DisplayObject getChild(DisplayObject d) {
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).equals(d)) {
                return children.get(i);
            }
        }
        return null;
    }

    public DisplayObject getChild(int index) {
        return children.get(index);
    }
}
