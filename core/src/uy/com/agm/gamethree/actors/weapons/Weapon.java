package uy.com.agm.gamethree.actors.weapons;

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
        SHOOT, ON_TARGET, IMPACT, FINISHED
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

    // This Weapon doesn't have any b2body inside these states
    public boolean isDestroyed() {
        return currentState == State.IMPACT || currentState == State.FINISHED;
    }

    protected void checkBoundaries() {
        /* A Weapon is always shoot on camera.
        * You have to be very careful because if the weapon is destroyed, its b2body does not exist and gives
        * random errors if you try to access its b2body.
        */
        if (!isDestroyed()) {
            float upperEdge = screen.getGameCam().position.y + screen.getGameViewPort().getWorldHeight() / 2;
            float bottomEdge = screen.getGameCam().position.y - screen.getGameViewPort().getWorldHeight() / 2;
            float weaponCenter = b2body.getPosition().y;

            if (!(bottomEdge <= weaponCenter && weaponCenter <= upperEdge)) {
                if(!world.isLocked()) {
                    world.destroyBody(b2body);
                }
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

    public void onBounce(Vector2 barrierVelocity) {
        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getBoing());

        reverseVelocity((barrierVelocity.x >= 0 && velocity.x < 0) || (barrierVelocity.x <= 0 && velocity.x > 0) ? true : false,
                (barrierVelocity.y >= 0 && velocity.y < 0) || (barrierVelocity.y <= 0 && velocity.y > 0) ? true : false);

        // Set the new rotation angle
        float angle = velocity.angle();
        angle = (angle >= 90.0f) ? angle - 90.0f : 270.0f + angle;
        setRotation(angle);
    }

    // This Weapon can be removed from our game
    public boolean isDisposable() {
        return currentState == State.FINISHED;
    }

    public void update(float dt) {
        switch (currentState) {
            case SHOOT:
                stateShoot(dt);
                break;
            case ON_TARGET:
                stateOnTarget(dt);
                break;
            case IMPACT:
                currentState = State.FINISHED;
                break;
            case FINISHED:
                break;
            default:
                break;
        }
        checkBoundaries();
    }

    public String whoAmI() {
        return getClassName();
    }

    public String getCurrentState() {
        return currentState.toString();
    }

    @Override
    public void draw(Batch batch) {
        if (currentState != State.FINISHED) {
            super.draw(batch);
        }
    }

    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
    }

    protected abstract void defineWeapon();
    protected abstract String getClassName();
    protected abstract void stateShoot(float dt);
    protected abstract void stateOnTarget(float dt);
    public abstract void onTarget();
}
