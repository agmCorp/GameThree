package uy.com.agm.gamethree.sprites.boxes;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.game.Constants;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.items.ItemCreator;
import uy.com.agm.gamethree.tools.AudioManager;

/**
 * Created by AGM on 12/17/2017.
 */

public class PowerBox extends Sprite {
    private static final String TAG = PowerBox.class.getName();

    private World world;
    private PlayScreen screen;
    private Body b2body;

    private TextureRegion powerBoxStand;
    private TextureRegion powerBoxDamagedLittle;
    private TextureRegion powerBoxDamagedMedium;
    private TextureRegion powerBoxDamagedHard;
    private Animation explosionAnimation;
    private float stateTimer;

    private enum State {
        WAITING, OPENED, EXPLODING, FINISHED
    }
    private State currentState;
    private MapObject object;
    private int damage;

    public PowerBox(PlayScreen screen, MapObject object) {
        this.object = object;
        this.world = screen.getWorld();
        this.screen = screen;

        // Get the rectangle drawn in TiledEditor (pixels)
        Rectangle rect = ((RectangleMapObject) object).getRectangle();

        /* Set this Sprite's bounds on the lower left vertex of a Rectangle.
        * This point will be used by definePowerBox() calling getX(), getY() to center its b2body.
        * SetBounds always receives world coordinates.
        */
        setBounds(rect.getX() / Constants.PPM, rect.getY() / Constants.PPM, Constants.POWERBOX_WIDTH_METERS, Constants.POWERBOX_HEIGHT_METERS);
        definePowerBox();

        // By default this PowerBox doesn't interact in our world
        b2body.setActive(false);

        // Textures
        switch (MathUtils.random(1, Constants.POWERBOX_MAX_TEXTURES)) {
            case 1:
                powerBoxStand = Assets.getInstance().getPowerBox().getBrickAStand();
                powerBoxDamagedLittle = Assets.getInstance().getPowerBox().getBrickADamagedLittle();
                powerBoxDamagedMedium = Assets.getInstance().getPowerBox().getBrickADamagedMedium();
                powerBoxDamagedHard = Assets.getInstance().getPowerBox().getBrickADamagedHard();
                break;
            case 2:
                powerBoxStand = Assets.getInstance().getPowerBox().getBrickBStand();
                powerBoxDamagedLittle = Assets.getInstance().getPowerBox().getBrickBDamagedLittle();
                powerBoxDamagedMedium = Assets.getInstance().getPowerBox().getBrickBDamagedMedium();
                powerBoxDamagedHard = Assets.getInstance().getPowerBox().getBrickBDamagedHard();
                break;
            case 3:
                powerBoxStand = Assets.getInstance().getPowerBox().getBrickCStand();
                powerBoxDamagedLittle = Assets.getInstance().getPowerBox().getBrickCDamagedLittle();
                powerBoxDamagedMedium = Assets.getInstance().getPowerBox().getBrickCDamagedMedium();
                powerBoxDamagedHard = Assets.getInstance().getPowerBox().getBrickCDamagedHard();
                break;
            case 4:
                powerBoxStand = Assets.getInstance().getPowerBox().getBrickDStand();
                powerBoxDamagedLittle = Assets.getInstance().getPowerBox().getBrickDDamagedLittle();
                powerBoxDamagedMedium = Assets.getInstance().getPowerBox().getBrickDDamagedMedium();
                powerBoxDamagedHard = Assets.getInstance().getPowerBox().getBrickDDamagedHard();
                break;
            case 5:
                powerBoxStand = Assets.getInstance().getPowerBox().getBrickEStand();
                powerBoxDamagedLittle = Assets.getInstance().getPowerBox().getBrickEDamagedLittle();
                powerBoxDamagedMedium = Assets.getInstance().getPowerBox().getBrickEDamagedMedium();
                powerBoxDamagedHard = Assets.getInstance().getPowerBox().getBrickEDamagedHard();
                break;
            case 6:
                powerBoxStand = Assets.getInstance().getPowerBox().getBrickFStand();
                powerBoxDamagedLittle = Assets.getInstance().getPowerBox().getBrickFDamagedLittle();
                powerBoxDamagedMedium = Assets.getInstance().getPowerBox().getBrickFDamagedMedium();
                powerBoxDamagedHard = Assets.getInstance().getPowerBox().getBrickFDamagedHard();
                break;
            case 7:
                powerBoxStand = Assets.getInstance().getPowerBox().getBrickGStand();
                powerBoxDamagedLittle = Assets.getInstance().getPowerBox().getBrickGDamagedLittle();
                powerBoxDamagedMedium = Assets.getInstance().getPowerBox().getBrickGDamagedMedium();
                powerBoxDamagedHard = Assets.getInstance().getPowerBox().getBrickGDamagedHard();
                break;
            case 8:
                powerBoxStand = Assets.getInstance().getPowerBox().getBrickHStand();
                powerBoxDamagedLittle = Assets.getInstance().getPowerBox().getBrickHDamagedLittle();
                powerBoxDamagedMedium = Assets.getInstance().getPowerBox().getBrickHDamagedMedium();
                powerBoxDamagedHard = Assets.getInstance().getPowerBox().getBrickHDamagedHard();
                break;
            case 9:
                powerBoxStand = Assets.getInstance().getPowerBox().getBrickIStand();
                powerBoxDamagedLittle = Assets.getInstance().getPowerBox().getBrickIDamagedLittle();
                powerBoxDamagedMedium = Assets.getInstance().getPowerBox().getBrickIDamagedMedium();
                powerBoxDamagedHard = Assets.getInstance().getPowerBox().getBrickIDamagedHard();
                break;
            case 10:
                powerBoxStand = Assets.getInstance().getPowerBox().getBrickJStand();
                powerBoxDamagedLittle = Assets.getInstance().getPowerBox().getBrickJDamagedLittle();
                powerBoxDamagedMedium = Assets.getInstance().getPowerBox().getBrickJDamagedMedium();
                powerBoxDamagedHard = Assets.getInstance().getPowerBox().getBrickJDamagedHard();
                break;
            case 11:
                powerBoxStand = Assets.getInstance().getPowerBox().getBrickKStand();
                powerBoxDamagedLittle = Assets.getInstance().getPowerBox().getBrickKDamagedLittle();
                powerBoxDamagedMedium = Assets.getInstance().getPowerBox().getBrickKDamagedMedium();
                powerBoxDamagedHard = Assets.getInstance().getPowerBox().getBrickKDamagedHard();
                break;
            case 12:
                powerBoxStand = Assets.getInstance().getPowerBox().getBrickLStand();
                powerBoxDamagedLittle = Assets.getInstance().getPowerBox().getBrickLDamagedLittle();
                powerBoxDamagedMedium = Assets.getInstance().getPowerBox().getBrickLDamagedMedium();
                powerBoxDamagedHard = Assets.getInstance().getPowerBox().getBrickLDamagedHard();
                break;
            case 13:
                powerBoxStand = Assets.getInstance().getPowerBox().getBrickMStand();
                powerBoxDamagedLittle = Assets.getInstance().getPowerBox().getBrickMDamagedLittle();
                powerBoxDamagedMedium = Assets.getInstance().getPowerBox().getBrickMDamagedMedium();
                powerBoxDamagedHard = Assets.getInstance().getPowerBox().getBrickMDamagedHard();
                break;
            case 14:
                powerBoxStand = Assets.getInstance().getPowerBox().getBrickNStand();
                powerBoxDamagedLittle = Assets.getInstance().getPowerBox().getBrickNDamagedLittle();
                powerBoxDamagedMedium = Assets.getInstance().getPowerBox().getBrickNDamagedMedium();
                powerBoxDamagedHard = Assets.getInstance().getPowerBox().getBrickNDamagedHard();
                break;
        }

        explosionAnimation = Assets.getInstance().getExplosionB().getExplosionBAnimation();

        currentState = State.WAITING;
        damage = 0;
        stateTimer = 0;
    }

    // This PowerBox doesn't have any b2body
    public boolean isDestroyed() {
        return currentState == State.FINISHED || currentState == State.EXPLODING;
    }

    // Determine whether or not an item should be released reading a property set in TiledEditor.
    private void getItemOnHit() {
        ItemCreator.getItemOnHit(object, screen.getCreator(), b2body.getPosition().x, b2body.getPosition().y + Constants.ITEM_OFFSET_METERS);
    }

    private void definePowerBox() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX() + getWidth() / 2 , getY() + getHeight() / 2); // In b2box the origin is at the center of the body
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(Constants.POWERBOX_CIRCLESHAPE_RADIUS_METERS);
        fdef.filter.categoryBits = Constants.POWERBOX_BIT; // Depicts what this fixture is
        fdef.filter.maskBits = Constants.ENEMY_BIT |
                Constants.ITEM_BIT |
                Constants.HERO_WEAPON_BIT |
                Constants.HERO_TOUGH_BIT |
                Constants.HERO_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
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

        // Destroy box2D body
        world.destroyBody(b2body);

        // Explosion animation
        stateTimer = 0;

        // Audio FX
        AudioManager.getInstance().play(Assets.getInstance().getSounds().getOpenPowerBox());

        // Set score
        screen.getHud().addScore(Constants.POWERBOX_SCORE);

        // Set the new state
        currentState = State.EXPLODING;
    }

    private void stateExploding(float dt) {
        if (explosionAnimation.isAnimationFinished(stateTimer)) {
            currentState = State.FINISHED;
        } else {
            if (stateTimer == 0) { // Explosion starts
                // Setbounds is the one that determines the size of the explosion on the screen
                setBounds(getX() + getWidth() / 2 - Constants.EXPLOSIONB_WIDTH_METERS / 2, getY() + getHeight() / 2 - Constants.EXPLOSIONB_HEIGHT_METERS / 2,
                        Constants.EXPLOSIONB_WIDTH_METERS, Constants.EXPLOSIONB_HEIGHT_METERS);
            }
            setRegion((TextureRegion) explosionAnimation.getKeyFrame(stateTimer, true));
            stateTimer += dt;
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
                        world.destroyBody(b2body);
                        currentState = State.FINISHED;
                    }
                }
            }
        }
    }

    public void onBump() {
        AudioManager.getInstance().play(Assets.getInstance().getSounds().getBump());
    }

    public void onHit() {
        /*
         * We must remove its b2body to avoid collisions.
         * This can't be done here because this method is called from WorldContactListener that is invoked
         * from PlayScreen.update.world.step(...).
         * No b2body can be removed when the simulation is occurring, we must wait for the next update cycle.
         * Therefore, we use a flag (state) in order to point out this behavior and remove it later.
         */
        int strength = object.getProperties().get("strength", 0, Integer.class);
        if (damage >= strength - 1) {
            currentState = State.OPENED;
        } else {
            AudioManager.getInstance().play(Assets.getInstance().getSounds().getCrack());
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
}
