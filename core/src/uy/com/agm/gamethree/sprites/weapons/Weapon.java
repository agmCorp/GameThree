package uy.com.agm.gamethree.sprites.weapons;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.AudioManager;

/**
 * Created by AGM on 12/17/2017.
 */

public abstract class Weapon extends Sprite {
    private static final String TAG = Weapon.class.getName();

    protected World world;
    protected PlayScreen screen;
    protected float circleShapeRadius;
    protected Body b2body;

    protected Vector2 velocity;

    protected enum State {
        SHOT, ONTARGET, FINISHED
    }
    protected State currentState;

    public Weapon(PlayScreen screen, float x, float y, float circleShapeRadius) {
        this.world = screen.getWorld();
        this.screen = screen;
        this.circleShapeRadius = circleShapeRadius;
        this.velocity = new Vector2();

        /* Set this Sprite's position on the lower left vertex of a Rectangle.
        * At this moment we don't have Weapon.width and Weapon.height because this is an abstract class.
        * Width and height will be determined in classes that inherit from this one.
        * This point will be used by defineWeapon() calling getX(), getY() to center its b2body.
        * SetPosition always receives world coordinates.
        */
        setPosition(x, y);
        defineWeapon();
    }

    // This Weapon doesn't have any b2body
    public boolean isDestroyed() {
        return currentState == State.ONTARGET || currentState == State.FINISHED;
    }

    protected void checkBoundaries() {
        /* A Weapon is always shot on camera.
        * You have to be very careful because if the weapon is destroyed, its b2body does not exist and gives
        * random errors if you try to access its b2body.
        */
        if (!isDestroyed()) {
            float upperEdge = screen.gameCam.position.y + screen.gameViewPort.getWorldHeight() / 2;
            float bottomEdge = screen.gameCam.position.y - screen.gameViewPort.getWorldHeight() / 2;

            if (!(bottomEdge <= getY() + getHeight() && getY() <= upperEdge)) {
                world.destroyBody(b2body);
                currentState = State.FINISHED;
            }
        }
    }

    protected void reverseVelocity(boolean x, boolean y) {
        if (x) {
            velocity.x *= -1;
        }
        if (y) {
            velocity.y *= -1;
        }
    }

    public void onBounce() {
        AudioManager.instance.play(Assets.instance.sounds.hit);// TODO RUIDO DE REBOTE
        reverseVelocity(false, true);
    }

    // This Weapon can be removed from our game
    public boolean isDisposable() {
        return currentState == State.FINISHED;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }

    protected abstract void defineWeapon();
    public abstract void update(float dt);
    public abstract void renderDebug(ShapeRenderer shapeRenderer);
    public abstract void onTarget();
}
