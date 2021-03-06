package uy.com.agm.gamethree.actors.items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/11/2017.
 */

public abstract class Item extends Sprite {
    private static final String TAG = Item.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float OFFSET_METERS = 40.0f / PlayScreen.PPM;
    private static final float MARGIN_METERS = 1.0f;

    protected World world;
    protected PlayScreen screen;
    protected Body b2body;

    protected Vector2 velocity;

    protected enum State {
        INACTIVE, WAITING, FADING, TAKEN, FINISHED
    }

    protected State currentState;

    public Item(PlayScreen screen, float x, float y) {
        this.screen = screen;
        this.world = screen.getWorld();
        this.velocity = new Vector2();

        /* Set this Sprite's position on the lower left vertex of a Rectangle.
        * At this moment we don't have Item.width and Item.height because this is an abstract class.
        * Width and height will be determined in classes that inherit from this one.
        * This point will be used by defineItem() calling getX(), getY() to center its b2body.
        * SetPosition always receives world coordinates.
        */
        setPosition(x, y);
        defineItem();

        // By default this Item doesn't interact in our world
        b2body.setActive(false);
        currentState = State.INACTIVE;
    }

    protected void reverseVelocity(boolean x, boolean y) {
        if (x)
            velocity.x = -velocity.x;
        if (y)
            velocity.y = -velocity.y;
    }

    // This Item doesn't have any b2body inside these states
    public boolean isDestroyed() {
        return currentState == State.FINISHED;
    }

    protected void checkBoundaries() {
        /* When an Item is on camera, it activates (it can collide).
        * You have to be very careful because if the item is destroyed, its b2body does not exist and gives
        * random errors if you try to active it.
        */
        if (!isDestroyed()) {
            float upperEdge = screen.getGameCam().position.y + screen.getGameViewPort().getWorldHeight() / 2;
            float bottomEdge = screen.getGameCam().position.y - screen.getGameViewPort().getWorldHeight() / 2;

            if (bottomEdge <= getY() + getHeight() && getY() <= upperEdge) {
                if (currentState == State.INACTIVE) { // Wasn't on camera...
                    b2body.setActive(true);
                    currentState = State.WAITING;
                }
            } else {
                if (currentState != State.INACTIVE) { // Was on camera...
                    // It's outside bottom edge + OFFSET or outside upperEdge + OFFSET
                    if (bottomEdge > getY() + getHeight() + MARGIN_METERS || upperEdge < getY() - MARGIN_METERS) {
                        if(!world.isLocked()) {
                            world.destroyBody(b2body);
                        }
                        currentState = State.FINISHED;
                    }
                }
            }
        }
    }

    // This Item can be removed from the game
    public boolean isDisposable() {
        return currentState == State.FINISHED;
    }

    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
    }

    public void update(float dt) {
        checkBoundaries();

        if (currentState != State.INACTIVE) {
            switch (currentState) {
                case WAITING:
                    stateWaiting(dt);
                    break;
                case FADING:
                    stateFading(dt);
                    break;
                case TAKEN:
                    screen.getInfoScreen().showDynamicHelp(getClassName(), getHelpImage());
                    stateTaken(dt);
                    break;
                case FINISHED:
                    break;
                default:
                    break;
            }
        }
    }

    public String whoAmI() {
        return getClassName();
    }

    public String getCurrentState() {
        return currentState.toString();
    }

    @Override
    public void draw(Batch batch) {
        if (currentState == State.WAITING || currentState == State.FADING) {
            super.draw(batch);
        }
    }

    protected abstract void defineItem();
    protected abstract void stateWaiting(float dt);
    protected abstract void stateFading(float dt);
    protected abstract void stateTaken(float dt);
    protected abstract String getClassName();
    protected abstract TextureRegion getHelpImage();
    public abstract void onUse();
    public abstract void onBump();
}
