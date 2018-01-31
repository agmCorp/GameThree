package uy.com.agm.gamethree.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.sun.org.apache.bcel.internal.generic.SWITCH;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.game.Constants;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.Vector2Util;

/**
 * Created by AGM on 12/9/2017.
 */

public class EnemyFour extends Enemy {
    private static final String TAG = EnemyFour.class.getName();

    private float stateTimer;
    private float openFireTimer;
    private Animation enemyFourAnimation;
    private Animation enemyFourFrozenAnimation;
    private Animation explosionAnimation;

    private float b2bodyTargetX;
    private float b2bodyTargetY;

    private enum FrozenState {
        INITIAL, FROZEN, DEFROSTED
    }
    private FrozenState currentFrozenState;
    private float b2bodyLinearVelX;
    private float stateFrozenTimer;
    private int timesItFreeze;

    public EnemyFour(PlayScreen screen, MapObject object) {
        super(screen, object);

        // Animations
        enemyFourAnimation = Assets.getInstance().getEnemyFour().getEnemyFourAnimation();
        enemyFourFrozenAnimation = Assets.getInstance().getEnemyFour().getEnemyFourFrozenAnimation();
        explosionAnimation = Assets.getInstance().getExplosionD().getExplosionDAnimation();

        // Setbounds is the one that determines the size of the EnemyFour's drawing on the screen
        setBounds(getX(), getY(), Constants.ENEMYFOUR_WIDTH_METERS, Constants.ENEMYFOUR_HEIGHT_METERS);

        // State variables initialization
        stateTimer = 0;
        openFireTimer = 0;
        currentState = State.ALIVE;

        // Move to (b2bodyTargetX, b2bodyTargetY) at constant speed
        b2bodyTargetX = getX() + (Constants.ENEMYFOUR_WAVELENGTH_METERS / 2) * MathUtils.randomSign();
        b2bodyTargetY = getY() + (Constants.ENEMYFOUR_AMPLITUDE_METERS / 2) * MathUtils.randomSign();

        tmp.set(getX(), getY());
        Vector2Util.goToTarget(tmp, b2bodyTargetX, b2bodyTargetY, Constants.ENEMYFOUR_LINEAR_VELOCITY);
        velocity.set(tmp);

        // Frozen state variables initialization
        currentFrozenState = FrozenState.INITIAL;
        b2bodyLinearVelX = 0;
        stateFrozenTimer = 0;

        // Indicates how many times this enemy can be frozen.
        timesItFreeze = object.getProperties().get("timesItFreeze", 1, Integer.class);
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY()); // In b2box the origin is at the center of the body
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(Constants.ENEMYFOUR_CIRCLESHAPE_RADIUS_METERS);
        fdef.filter.categoryBits = Constants.ENEMY_BIT; // Depicts what this fixture is
        fdef.filter.maskBits = Constants.BORDERS_BIT |
                Constants.HERO_WEAPON_BIT |
                Constants.SHIELD_BIT |
                Constants.HERO_BIT |
                Constants.HERO_TOUGH_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void update(float dt) {
        switch (currentState) {
            case ALIVE:
                stateAlive(dt);
                break;
            case INJURED:
                stateInjured(dt);
                break;
            case EXPLODING:
                stateExploding(dt);
                break;
            case DEAD:
                break;
            default:
                break;
        }
        super.checkBoundaries();
    }

    @Override
    public void onHit() {
        /*
         * We must remove its b2body to avoid collisions.
         * This can't be done here because this method is called from WorldContactListener that is invoked
         * from PlayScreen.update.world.step(...).
         * No b2body can be removed when the simulation is occurring, we must wait for the next update cycle.
         * Therefore, we use a flag (state) in order to point out this behavior and remove it later.
         */
        currentState = State.INJURED;
    }

    @Override
    public void onBump() {
        reverseVelocity(true, false);
    }

    @Override
    public void draw(Batch batch) {
        if (currentState != State.DEAD) {
           super.draw(batch);
        }
    }

    @Override
    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
    }

    private void stateAlive(float dt) {
        // Set velocity because It could have been changed (see reverseVelocity)
        b2body.setLinearVelocity(velocity);

        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, EnemyFour may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        TextureRegion region = (TextureRegion) enemyFourAnimation.getKeyFrame(stateTimer, true);
        if (b2body.getLinearVelocity().x > 0 && region.isFlipX()) {
            region.flip(true, false);
        }
        if (b2body.getLinearVelocity().x < 0 && !region.isFlipX()) {
            region.flip(true, false);
        }

        setRegion(region);
        stateTimer += dt;

        openFireTimer += dt;
        if (openFireTimer > Constants.ENEMYFOUR_FIRE_DELAY_SECONDS) {
            super.openFire();
            openFireTimer = 0;
        }

        checkPath();
    }

    private void stateInjured(float dt) {
        switch (currentFrozenState) {
            case INITIAL:
                stateTimer = 0;
                currentFrozenState = FrozenState.FROZEN;
                frozenStateFrozen(dt);
                break;
            case FROZEN:
                frozenStateFrozen(dt);
                break;
            case DEFROSTED:
                frozenStateDefrosted();
                break;
            default:
                break;
        }
    }

    private void frozenStateFrozen(float dt) {
        // Set velocity because It could have been changed (see reverseVelocity)
        b2body.setLinearVelocity(velocity);

        if (stateTimer == 0) { // Frozen state starts
            // Setbounds is the one that determines the size of the frozen sprite on the screen
            setBounds(getX() + getWidth() / 2 - Constants.ENEMYFOUR_FROZEN_WIDTH_METERS / 2, getY() + getHeight() / 2 - Constants.ENEMYFOUR_FROZEN_HEIGHT_METERS / 2,
                    Constants.ENEMYFOUR_FROZEN_WIDTH_METERS, Constants.ENEMYFOUR_FROZEN_HEIGHT_METERS);

            // We get the linear velocity that it had before being frozen
            b2bodyLinearVelX = b2body.getLinearVelocity().x;

            // Stop motion
            stop();
        }

        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, EnemyFour may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established previously (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        // We set its frozen animation
        TextureRegion region = (TextureRegion) enemyFourFrozenAnimation.getKeyFrame(stateTimer, true);
        if (b2bodyLinearVelX > 0 && region.isFlipX()) {
            region.flip(true, false);
        }
        if (b2bodyLinearVelX < 0 && !region.isFlipX()) {
            region.flip(true, false);
        }

        setRegion(region);
        stateTimer += dt;

        // Check if it's time to defrost it
        checkDefrost(dt);

    }

    private void checkDefrost(float dt) {
        stateFrozenTimer += dt;
        if (stateFrozenTimer >= Constants.ENEMYFOUR_FROZEN_TIME_SECONDS) {
            // Setbounds is the one that determines the size of the EnemyFour's drawing on the screen
            setBounds(b2body.getPosition().x, b2body.getPosition().y, Constants.ENEMYFOUR_WIDTH_METERS, Constants.ENEMYFOUR_HEIGHT_METERS);

            timesItFreeze--;
            if (timesItFreeze > 0) {
                currentFrozenState = FrozenState.INITIAL;
            } else {
                currentFrozenState = FrozenState.DEFROSTED;
            }
            currentState = State.ALIVE;

            // Return movement
            tmp.set(b2body.getPosition().x, b2body.getPosition().y);
            Vector2Util.goToTarget(tmp, b2bodyTargetX, b2bodyTargetY, Constants.ENEMYFOUR_LINEAR_VELOCITY);
            velocity.set(tmp);

            stateTimer = 0;
            stateFrozenTimer = 0;
        }
    }

    private void frozenStateDefrosted() {
        // Release an item
        getItemOnHit();

        // Destroy box2D body
        world.destroyBody(b2body);

        // Explosion animation
        stateTimer = 0;

        // Audio FX
        AudioManager.getInstance().play(Assets.getInstance().getSounds().getHit());

        // Set score
        screen.getHud().addScore(Constants.ENEMYFOUR_SCORE);

        // Set the new state
        currentState = State.EXPLODING;
    }

    private void stateExploding(float dt) {
        if (explosionAnimation.isAnimationFinished(stateTimer)) {
            currentState = State.DEAD;
        } else {
            if (stateTimer == 0) { // Explosion starts
                // Setbounds is the one that determines the size of the explosion on the screen
                setBounds(getX() + getWidth() / 2 - Constants.EXPLOSIOND_WIDTH_METERS / 2, getY() + getHeight() / 2 - Constants.EXPLOSIOND_HEIGHT_METERS / 2,
                        Constants.EXPLOSIOND_WIDTH_METERS, Constants.EXPLOSIOND_HEIGHT_METERS);
            }
            setRegion((TextureRegion) explosionAnimation.getKeyFrame(stateTimer, true));
            stateTimer += dt;
        }
    }

    private void checkPath() {
        if (b2body.getLinearVelocity().y > 0) { // EnemyFour goes up
            if (b2body.getPosition().y >= b2bodyTargetY) { // EnemyFour reaches target
                b2bodyTargetY = b2bodyTargetY - Constants.ENEMYFOUR_AMPLITUDE_METERS; // New targetY (down)
            }
        } else { // EnemyFour goes down
            if (b2body.getPosition().y <= b2bodyTargetY) { // EnemyFour reaches target
                b2bodyTargetY = b2bodyTargetY + Constants.ENEMYFOUR_AMPLITUDE_METERS; // New targetY (up)
            }
        }

        if (b2body.getLinearVelocity().x > 0) { // EnemyFour goes to the right
            if (b2body.getPosition().x >= b2bodyTargetX) { // EnemyFour reaches target
                b2bodyTargetX = b2bodyTargetX + Constants.ENEMYFOUR_WAVELENGTH_METERS / 2; // New targetX (right)
            }
        } else { // // EnemyFour goes to the left
            if (b2body.getPosition().x <= b2bodyTargetX) { // EnemyFour reaches target
                b2bodyTargetX = b2bodyTargetX - Constants.ENEMYFOUR_WAVELENGTH_METERS / 2; // New targetX (left)
            }
        }

        // Go to target with constant velocity
        tmp.set(b2body.getPosition().x, b2body.getPosition().y);
        Vector2Util.goToTarget(tmp, b2bodyTargetX, b2bodyTargetY, Constants.ENEMYFOUR_LINEAR_VELOCITY);
        velocity.set(tmp);
    }

    private void stop() {
        velocity.set(0.0f, 0.0f);
    }
}
