package uy.com.agm.gamethree.sprites.powerup.boxes;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.powerup.Items.PowerOne;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.Constants;
import uy.com.agm.gamethree.tools.GameThreeActorDef;

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
    private float stateTime;

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
        powerBoxStand = Assets.instance.powerBox.powerBoxStand;
        powerBoxDamagedLittle = Assets.instance.powerBox.powerBoxDamagedLittle;
        powerBoxDamagedMedium = Assets.instance.powerBox.powerBoxDamagedMedium;
        powerBoxDamagedHard = Assets.instance.powerBox.powerBoxDamagedHard;
        explosionAnimation = Assets.instance.explosionB.explosionBAnimation;

        currentState = State.WAITING;
        damage = 0;
        stateTime = 0;
    }

    // This PowerBox doesn't have any b2body
    public boolean isDestroyed() {
        return currentState == State.FINISHED || currentState == State.EXPLODING;
    }

    // Determine whether or not a power should be released reading a property set in TiledEditor.
    protected void getItemOnHit() {
        if (object.getProperties().containsKey("powerOne")) {
            Vector2 position = new Vector2(b2body.getPosition().x, b2body.getPosition().y + Constants.ITEM_OFFSET_METERS);
            screen.getCreator().createGameThreeActor(new GameThreeActorDef(position, PowerOne.class));
        }
    }

    protected void definePowerBox() {
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
        // Destroy box2D body
        world.destroyBody(b2body);

        // Explosion animation
        stateTime = 0;

        // Audio FX
        AudioManager.instance.play(Assets.instance.sounds.openPowerBox, 1);

        // Set the new state
        currentState = State.EXPLODING;
    }

    private void stateExploding(float dt) {
        if (explosionAnimation.isAnimationFinished(stateTime)) {
            currentState = State.FINISHED;
        } else {
            if (stateTime == 0) { // Explosion starts
                // Setbounds is the one that determines the size of the explosion on the screen
                setBounds(getX(), getY(), Constants.EXPLOSIONB_WIDTH_METERS, Constants.EXPLOSIONB_HEIGHT_METERS);
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
            float upperEdge = screen.gameCam.position.y + screen.gameViewPort.getWorldHeight() / 2;
            float bottomEdge = screen.gameCam.position.y - screen.gameViewPort.getWorldHeight() / 2;

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
        AudioManager.instance.play(Assets.instance.sounds.bump, 1);
    }

    public void onHit() {
        /*
         * We must remove its b2body to avoid collisions.
         * This can't be done here because this method is called from WorldContactListener that is invoked
         * from PlayScreen.update.world.step(...).
         * No b2body can be removed when the simulation is occurring, we must wait for the next update cycle.
         * Therefore we use a flag (state) in order to point out this behavior and remove it later.
         */
        int strength = object.getProperties().get("strength", 0, Integer.class);
        if (damage >= strength - 1) {
            getItemOnHit();
            currentState = State.OPENED;
        } else {
            AudioManager.instance.play(Assets.instance.sounds.crack, 1);
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
