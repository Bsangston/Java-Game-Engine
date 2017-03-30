package edu.virginia.engine.display;

import java.util.ArrayList;

/**
 * Created by BrandonSangston on 3/11/17.
 */
public class RigidBody2D {

    private static double gravity = Game.GRAVITY;
    private static Vec2 gravity2D;
    private boolean hasGravity;

    public Vec2 velocity;
    public Vec2 acceleration;
    public double mass;
    public double restitution;
    public ArrayList<Vec2> constantForces;

    private DisplayObject sprite;

    public RigidBody2D(DisplayObject s) {
        sprite = s;
        velocity = new Vec2(0, 0);
        acceleration = new Vec2(1, gravity);
        mass = 1;
        restitution = 0.2;

        constantForces = new ArrayList<>();
        gravity2D = new Vec2(1, gravity);
        constantForces.add(gravity2D);
        hasGravity = true;

    }

    //TODO: fix
    public void applyForce(Vec2 force) {
        acceleration.x = force.x/mass;
        acceleration.y = force.y/mass;
        sprite.setPosition((int)(sprite.getPosX() * acceleration.x), (int)(sprite.getPosY() * acceleration.y));

    }

    //TODO: fix
    public void applyConstantForces() {
        for (Vec2 force : constantForces) {
            applyForce(force);
        }

    }

    public void updateVelocity(Vec2 velocity) {
        this.velocity = velocity;
    }

    public void addAcceleration(Vec2 accel) {
        acceleration.x += accel.x;
        acceleration.y += accel.y;
    }

    public void applyDrag(double drag)
    {
        this.velocity.x = (drag * this.velocity.x);
        this.velocity.y = (drag * this.velocity.y);
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getMass() {
        return mass;
    }

    public void setVelocity(Vec2 velocity) {
        this.velocity = velocity;
    }

    public Vec2 getVelocity() {
        return velocity;
    }

    public void setAcceleration(Vec2 acceleration) {
        this.acceleration = acceleration;
    }

    public Vec2 getAcceleration() {
        return acceleration;
    }

    public static void setGravity(double gravity) {
        RigidBody2D.gravity = gravity;
    }

    public static double getGravity() {
        return gravity;
    }

    public void toggleGravity(boolean hasGravity) {
        this.hasGravity = hasGravity;
        if (!hasGravity) {
            constantForces.remove(gravity2D);
        } else {
            constantForces.add(gravity2D);
        }
    }

    public boolean hasGravity() {
        return hasGravity;
    }

    public boolean isJumping() {
        return getVelocity().y > 0;
    }
}
