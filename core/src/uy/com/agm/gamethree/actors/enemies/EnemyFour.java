package uy.com.agm.gamethree.actors.enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import uy.com.agm.gamethree.actors.weapons.IShootStrategy;
import uy.com.agm.gamethree.actors.weapons.enemy.EnemyDefaultShooting;
import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyFour;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionD;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.B2WorldCreator;
import uy.com.agm.gamethree.tools.Vector2Util;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 12/9/2017.
 */

public class EnemyFour extends Enemy {
    private static final String TAG = EnemyFour.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    private static final float CIRCLE_SHAPE_RADIUS_METERS = 20.0f / PlayScreen.PPM;
    private static final float LINEAR_VELOCITY = 3.0f;
    private static final float DENSITY = 1000.0f;
    private static final float AMPLITUDE_METERS = 200.0f / PlayScreen.PPM;
    private static final float WAVELENGTH_METERS = 100.0f / PlayScreen.PPM;
    private static final float FIRE_DELAY_SECONDS = 3.0f;
    private static final float FROZEN_TIME_SECONDS = 4.0f;
    private static final float SPEAK_TIME_SECONDS = 3.0f;
    private static final int SCORE = 35;

    private float stateTime;
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
    private float stateFrozenTime;
    private int timesItFreeze;

    public EnemyFour(PlayScreen screen, MapObject object) {
        super(screen, object);

        // Animations
        enemyFourAnimation = Assets.getInstance().getEnemyFour().getEnemyFourAnimation();
        enemyFourFrozenAnimation = Assets.getInstance().getEnemyFour().getEnemyFourFrozenAnimation();
        explosionAnimation = Assets.getInstance().getExplosionD().getExplosionDAnimation();

        // Determines the size of the EnemyFour's drawing on the screen
        setBounds(getX(), getY(), AssetEnemyFour.WIDTH_METERS, AssetEnemyFour.HEIGHT_METERS);

        // Move to (b2bodyTargetX, b2bodyTargetY) at constant speed
        b2bodyTargetX = getX() + (WAVELENGTH_METERS / 2) * MathUtils.randomSign();
        b2bodyTargetY = getY() + (AMPLITUDE_METERS / 2) * MathUtils.randomSign();

        // Variables initialization
        stateTime = MathUtils.random(0, enemyFourAnimation.getAnimationDuration()); // To flap untimely with others;
        tmp.set(getX(), getY());
        Vector2Util.goToTarget(tmp, b2bodyTargetX, b2bodyTargetY, LINEAR_VELOCITY);
        velocity.set(tmp);

        // Frozen state variables initialization
        currentFrozenState = FrozenState.INITIAL;
        b2bodyLinearVelX = 0;
        stateFrozenTime = 0;

        // Indicates how many times this enemy can be frozen.
        timesItFreeze = object.getProperties().get(B2WorldCreator.KEY_TIMES_IT_FREEZE, 1, Integer.class);
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY()); // In b2box the origin is at the center of the body
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        b2body.setFixedRotation(true);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(CIRCLE_SHAPE_RADIUS_METERS);
        fdef.filter.categoryBits = WorldContactListener.ENEMY_BIT; // Depicts what this fixture is
        fdef.filter.maskBits = WorldContactListener.BORDER_BIT |
                WorldContactListener.HERO_WEAPON_BIT |
                WorldContactListener.HERO_SHIELD_BIT |
                WorldContactListener.HERO_BIT |
                WorldContactListener.HERO_TOUGH_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
        fdef.shape = shape;
        fdef.density = DENSITY; // Hard to push
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    protected IShootStrategy getShootStrategy() {
        return new EnemyDefaultShooting(screen, MathUtils.random(0, FIRE_DELAY_SECONDS), FIRE_DELAY_SECONDS);
    }

    @Override
    protected float getCircleShapeRadiusMeters() {
        return CIRCLE_SHAPE_RADIUS_METERS;
    }

    @Override
    protected TextureRegion getKnockBackFrame(float dt) {
        TextureRegion region = (TextureRegion) enemyFourAnimation.getKeyFrame(stateTime, true);
        stateTime += dt;
        return region;
    }

    @Override
    protected void stateAlive(float dt) {
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

        TextureRegion region = (TextureRegion) enemyFourAnimation.getKeyFrame(stateTime, true);
        if (b2body.getLinearVelocity().x > 0 && region.isFlipX()) {
            region.flip(true, false);
        }
        if (b2body.getLinearVelocity().x < 0 && !region.isFlipX()) {
            region.flip(true, false);
        }

        setRegion(region);
        stateTime += dt;

        // Shoot time!
        super.openFire(dt);

        checkPath();
    }

    @Override
    protected void stateInjured(float dt) {
        switch (currentFrozenState) {
            case INITIAL:
                stateTime = 0;
                currentFrozenState = FrozenState.FROZEN;
                stateFrozen(dt);
                break;
            case FROZEN:
                stateFrozen(dt);
                break;
            case DEFROSTED:
                stateDefrosted(dt);
                break;
            default:
                break;
        }
    }

    private void stateFrozen(float dt) {
        // Set velocity because It could have been changed (see reverseVelocity)
        b2body.setLinearVelocity(velocity);

        if (stateTime == 0) { // Frozen state starts
            // Determines the size of the frozen sprite on the screen
            setBounds(getX() + getWidth() / 2 - AssetEnemyFour.FROZEN_WIDTH_METERS / 2, getY() + getHeight() / 2 - AssetEnemyFour.FROZEN_HEIGHT_METERS / 2,
                    AssetEnemyFour.FROZEN_WIDTH_METERS, AssetEnemyFour.FROZEN_HEIGHT_METERS);

            // We get the linear velocity it had before being frozen
            b2bodyLinearVelX = b2body.getLinearVelocity().x;

            // Audio FX
            AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getFrozen());

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
        TextureRegion region = (TextureRegion) enemyFourFrozenAnimation.getKeyFrame(stateTime, true);
        if (b2bodyLinearVelX > 0 && region.isFlipX()) {
            region.flip(true, false);
        }
        if (b2bodyLinearVelX < 0 && !region.isFlipX()) {
            region.flip(true, false);
        }

        setRegion(region);
        stateTime += dt;

        // Check if it's time to defrost it
        checkDefrost(dt);
    }

    private void checkDefrost(float dt) {
        stateFrozenTime += dt;
        if (stateFrozenTime >= FROZEN_TIME_SECONDS) {
            // Audio FX
            AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getFrozen());

            // Determines the size of the EnemyFour's drawing on the screen
            setBounds(b2body.getPosition().x, b2body.getPosition().y, AssetEnemyFour.WIDTH_METERS, AssetEnemyFour.HEIGHT_METERS);

            timesItFreeze--;
            if (timesItFreeze > 0) {
                currentFrozenState = FrozenState.INITIAL;
            } else {
                currentFrozenState = FrozenState.DEFROSTED;
            }
            currentState = State.ALIVE;

            // Return movement
            tmp.set(b2body.getPosition().x, b2body.getPosition().y);
            Vector2Util.goToTarget(tmp, b2bodyTargetX, b2bodyTargetY, LINEAR_VELOCITY);
            velocity.set(tmp);

            stateTime = 0;
            stateFrozenTime = 0;
        }
    }

    private void stateDefrosted(float dt) {
        // Release an item
        getItemOnHit();

        // Explosion animation
        stateTime = 0;

        // Audio FX
        pum(Assets.getInstance().getSounds().getHit());

        // Set score
        screen.getHud().addScore(SCORE);

        // Destroy box2D body
        if(!world.isLocked()) {
            world.destroyBody(b2body);
        }

        // Set the new state
        currentState = State.EXPLODING;
    }

    @Override
    protected void stateExploding(float dt) {
        if (explosionAnimation.isAnimationFinished(stateTime)) {
            currentState = State.SPLAT;
        } else {
            if (stateTime == 0) { // Explosion starts
                // Determines the size of the explosion on the screen
                setBounds(getX() + getWidth() / 2 - AssetExplosionD.WIDTH_METERS * expScale / 2, getY() + getHeight() / 2 - AssetExplosionD.HEIGHT_METERS * expScale / 2,
                        AssetExplosionD.WIDTH_METERS * expScale, AssetExplosionD.HEIGHT_METERS * expScale);
            }
            setRegion((TextureRegion) explosionAnimation.getKeyFrame(stateTime, true));
            stateTime += dt;
        }
    }

    @Override
    protected String getClassName() {
        return this.getClass().getName();
    }

    @Override
    protected TextureRegion getHelpImage() {
        return Assets.getInstance().getScene2d().getHelpEnemyFour();
    }

    @Override
    protected Sound getVoice() {
        return Assets.getInstance().getSounds().getWarble();
    }

    @Override
    protected float getSpeakTimeSeconds() {
        return SPEAK_TIME_SECONDS;
    }

    private void checkPath() {
        if (b2body.getLinearVelocity().y > 0) { // EnemyFour goes up
            if (b2body.getPosition().y >= b2bodyTargetY) { // EnemyFour reaches target
                b2bodyTargetY = b2bodyTargetY - AMPLITUDE_METERS; // New targetY (down)
            }
        } else { // EnemyFour goes down
            if (b2body.getPosition().y <= b2bodyTargetY) { // EnemyFour reaches target
                b2bodyTargetY = b2bodyTargetY + AMPLITUDE_METERS; // New targetY (up)
            }
        }

        if (b2body.getLinearVelocity().x > 0) { // EnemyFour goes to the right
            if (b2body.getPosition().x >= b2bodyTargetX) { // EnemyFour reaches target
                b2bodyTargetX = b2bodyTargetX + WAVELENGTH_METERS / 2; // New targetX (right)
            }
        } else { // // EnemyFour goes to the left
            if (b2body.getPosition().x <= b2bodyTargetX) { // EnemyFour reaches target
                b2bodyTargetX = b2bodyTargetX - WAVELENGTH_METERS / 2; // New targetX (left)
            }
        }

        // Go to target with constant velocity
        tmp.set(b2body.getPosition().x, b2body.getPosition().y);
        Vector2Util.goToTarget(tmp, b2bodyTargetX, b2bodyTargetY, LINEAR_VELOCITY);
        velocity.set(tmp);
    }

    private void stop() {
        velocity.set(0.0f, 0.0f);
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
        currentState = currentFrozenState == FrozenState.DEFROSTED ? State.KNOCK_BACK : State.INJURED;
    }

    @Override
    public void onBump() {
        reverseVelocity(true, false);
    }
}
