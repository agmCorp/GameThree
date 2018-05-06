package uy.com.agm.gamethree.actors.backgroundObjects.staticObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Shape;

import uy.com.agm.gamethree.actors.items.Item;
import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionB;
import uy.com.agm.gamethree.assets.sprites.AssetPowerBox;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.B2WorldCreator;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 12/17/2017.
 */

public class PowerBox extends StaticBackgroundObject {
    private static final String TAG = PowerBox.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    private static final float CIRCLE_SHAPE_RADIUS_METERS = 29.0f / PlayScreen.PPM;
    private static final int SCORE = 10;

    private TextureRegion powerBoxStand;
    private TextureRegion powerBoxDamagedLittle;
    private TextureRegion powerBoxDamagedMedium;
    private TextureRegion powerBoxDamagedHard;
    private Animation explosionAnimation;
    private float stateTime;
    private enum State {
        WAITING, OPENED, EXPLODING, FINISHED
    }
    private State currentState;
    private int damage;

    public PowerBox(PlayScreen screen, MapObject object) {
        super(screen, object);

        Filter filter = new Filter();
        filter.categoryBits = WorldContactListener.POWER_BOX_BIT; // Depicts what this fixture is
        filter.maskBits = WorldContactListener.ENEMY_BIT |
                WorldContactListener.ITEM_BIT |
                WorldContactListener.HERO_WEAPON_BIT |
                WorldContactListener.HERO_BIT |
                WorldContactListener.HERO_GHOST_BIT |
                WorldContactListener.HERO_TOUGH_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
        fixture.setFilterData(filter);
        fixture.setUserData(this);

        // Set this Sprite's bounds to correspond with the position of our Box2D body
        setBounds(b2body.getPosition().x - AssetPowerBox.WIDTH_METERS / 2, b2body.getPosition().y - AssetPowerBox.HEIGHT_METERS / 2, AssetPowerBox.WIDTH_METERS, AssetPowerBox.HEIGHT_METERS);

        // Me must redefine its boundsMeters to correspond with the size of the sprite instead of the size of the
        // rectangle drawn in TiledEditor
        boundsMeters = getBoundingRectangle();

        // By default this PowerBox doesn't interact in our world
        b2body.setActive(false);

        // Textures
        int index = Assets.getInstance().getPowerBox().getRandomIndexBrick();
        powerBoxStand = Assets.getInstance().getPowerBox().getBrickStand(index);
        powerBoxDamagedLittle = Assets.getInstance().getPowerBox().getBrickDamagedLittle(index);
        powerBoxDamagedMedium = Assets.getInstance().getPowerBox().getBrickDamagedMedium(index);
        powerBoxDamagedHard = Assets.getInstance().getPowerBox().getBrickDamagedHard(index);

        explosionAnimation = Assets.getInstance().getExplosionB().getExplosionBAnimation();
        currentState = State.WAITING;
        damage = 0;
        stateTime = 0;
    }

    // This PowerBox doesn't have any b2body inside these states
    public boolean isDestroyed() {
        return currentState == State.EXPLODING || currentState == State.FINISHED;
    }

    // Determine whether or not an item should be released reading a property set in TiledEditor.
    private void getItemOnHit() {
        screen.getCreator().getItemOnHit(object, b2body.getPosition().x, b2body.getPosition().y + Item.OFFSET_METERS);
    }

    public void update(float dt) {
        switch (currentState) {
            case WAITING:
                stateWaiting();
                break;
            case OPENED:
                stateOpened();
                break;
            case EXPLODING:
                stateExploding(dt);
                break;
            case FINISHED:
                break;
            default:
                break;
        }
        checkBoundaries();
    }

    private void stateWaiting() {
        switch (damage) {
            case 0:
                setRegion(powerBoxStand);
                break;
            case 1:
                setRegion(powerBoxDamagedLittle);
                break;
            case 2:
                setRegion(powerBoxDamagedMedium);
                break;
            case 3:
                setRegion(powerBoxDamagedHard);
                break;
            default:
                break;
        }
    }

    private void stateOpened() {
        // Release an item
        getItemOnHit();

        // Explosion animation
        stateTime = 0;

        // Audio FX
        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getOpenPowerBox());

        // Set score
        screen.getHud().addScore(SCORE);

        // Destroy box2D body
        if(!world.isLocked()) {
            world.destroyBody(b2body);
        }

        // Set the new state
        currentState = State.EXPLODING;
    }

    private void stateExploding(float dt) {
        if (explosionAnimation.isAnimationFinished(stateTime)) {
            currentState = State.FINISHED;
        } else {
            if (stateTime == 0) { // Explosion starts
                // Determines the size of the explosion on the screen
                setBounds(getX() + getWidth() / 2 - AssetExplosionB.WIDTH_METERS / 2, getY() + getHeight() / 2 - AssetExplosionB.HEIGHT_METERS / 2,
                        AssetExplosionB.WIDTH_METERS, AssetExplosionB.HEIGHT_METERS);
            }
            setRegion((TextureRegion) explosionAnimation.getKeyFrame(stateTime, true));
            stateTime += dt;
        }
    }

    private void checkBoundaries() {
        /* When a PowerBox is on camera, it activates (it can collide).
        * You have to be very careful because if the power box is destroyed, its b2body does not exist and gives
        * random errors if you try to active it.
        */
        if (!isDestroyed()) {
            float upperEdge = screen.getGameCam().position.y + screen.getGameViewPort().getWorldHeight() / 2;
            float bottomEdge = screen.getGameCam().position.y - screen.getGameViewPort().getWorldHeight() / 2;

            if (bottomEdge <= getY() + getHeight() && getY() <= upperEdge) {
                b2body.setActive(true);
            } else {
                if (b2body.isActive()) { // Was on camera...
                    // It's outside bottom edge
                    if (bottomEdge > getY() + getHeight()) {
                        if(!world.isLocked()) {
                            world.destroyBody(b2body);
                        }
                        currentState = State.FINISHED;
                    }
                }
            }
        }
    }

    public void onBump() {
        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getBump());
    }

    @Override
    protected Shape getShape() {
        CircleShape shape = new CircleShape();
        shape.setRadius(CIRCLE_SHAPE_RADIUS_METERS);
        return shape;
    }

    public void onHit() {
        /*
         * We must remove its b2body to avoid collisions.
         * This can't be done here because this method is called from WorldContactListener that is invoked
         * from PlayScreen.update.world.step(...).
         * No b2body can be removed when the simulation is occurring, we must wait for the next update cycle.
         * Therefore, we use a flag (state) in order to point out this behavior and remove it later.
         */
        int strength = object.getProperties().get(B2WorldCreator.KEY_STRENGTH, 0, Integer.class);
        if (damage >= strength - 1) {
            currentState = State.OPENED;
        } else {
            AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getCrack());
            damage++;
        }
    }

    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
    }

    public void draw(Batch batch) {
        if (currentState != State.FINISHED) {
           super.draw(batch);
        }
    }

    // This PowerBox can be removed from our game
    public boolean isDisposable() {
        return currentState == State.FINISHED;
    }

    public String whoAmI() {
        return this.getClass().getName();
    }

    public String getCurrentState() {
        return currentState.toString();
    }
}
