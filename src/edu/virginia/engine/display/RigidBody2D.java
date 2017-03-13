package edu.virginia.engine.display;

import edu.virginia.engine.util.GameClock;

import java.util.ArrayList;

/**
 * Created by BrandonSangston on 3/11/17.
 */
public class RigidBody2D {

    private static float gravity = 1.03f;
    private static Vector2D gravity2D;
    private boolean hasGravity;

    public Vector2D velocity;
    public Vector2D acceleration;
    public float mass;
    public float restitution;
    public ArrayList<Vector2D> constantForces;

    private DisplayObject sprite;

    public RigidBody2D(DisplayObject s) {
        sprite = s;
        velocity = new Vector2D(0, 0);
        acceleration = new Vector2D(0, gravity);
        mass = 1f;
        restitution = 0.2f;

        constantForces = new ArrayList<>();
        gravity2D = new Vector2D(1, gravity);
        constantForces.add(gravity2D);
        hasGravity = true;

    }

    //TODO: fix
    public void applyForce(Vector2D force) {
        acceleration.x = force.x/mass;
        acceleration.y = force.y/mass;
        sprite.setPosition((int)(sprite.getPosX() * acceleration.x), (int)(sprite.getPosY() * acceleration.y));

    }

    //TODO: fix
    public void applyConstantForces() {
        for (Vector2D force : constantForces) {
            acceleration.x = force.x / mass;
            acceleration.y = force.y / mass;
            sprite.setPosition((int)(sprite.getPosX() * acceleration.x), (int)(sprite.getPosY() * acceleration.y));
        }

    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public float getMass() {
        return mass;
    }

    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public void setAcceleration(Vector2D acceleration) {
        this.acceleration = acceleration;
    }

    public Vector2D getAcceleration() {
        return acceleration;
    }

    public static void setGravity(float gravity) {
        RigidBody2D.gravity = gravity;
    }

    public static float getGravity() {
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
}
