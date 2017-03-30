package edu.virginia.engine.display;

/**
 * Created by BrandonSangston on 3/11/17.
 */
public class Vec2 {

    public double x;
    public double y;
    public double angle;
    public double magnitude;

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
        this.angle = Math.atan2(y, x);
        this.magnitude = Math.sqrt((x * x) + (y * y));
    }

    public Vec2(Vec2 v) {
        this.x = v.x;
        this.y = v.y;
        this.angle = Math.atan2(v.y, v.x);
        this.magnitude = Math.sqrt((v.x * v.x) + (v.y * v.y));
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        this.angle = Math.atan2(y, x);
        this.magnitude = Math.sqrt((x * x) + (y * y));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAngle() {
        return angle;
    }

    public double getMagnitude() {
        return magnitude;
    }
}

