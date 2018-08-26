package uy.com.agm.gamethree.actors.enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import uy.com.agm.gamethree.actors.weapons.IShootStrategy;
import uy.com.agm.gamethree.actors.weapons.Weapon;
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
    private static final float DEBUG_TARGET_RADIUS_METERS = 10.0f / PlayScreen.PPM;
    private static final float MARGIN_X = 10.0f / PlayScreen.PPM;
    private static final float LINEAR_VELOCITY = 3.0f;
    private static final float DENSITY = 1000.0f;
    private static final float AMPLITUDE_METERS = 200.0f / PlayScreen.PPM;
    private static final float WAVELENGTH_METERS = 100.0f / PlayScreen.PPM;
    private static final int TIMES_IT_FREEZE_DEFAULT = 1;
    private static final float FROZEN_TIME_SECONDS = 4.0f;
    private static final float FIRE_DELAY_SECONDS = 3.0f;
    private static final float SPEAK_TIME_SECONDS = 3.0f;
    private static final int SCORE = 35;

    private float stateTime;
    private Animation enemyFourAnimation;
    private Animation enemyFourFrozenAnimation;
    private Animation explosionAnimation;

    private float targetX;
    private float targetY;

    private boolean frozen;
    private float frozenStateTime;
    private int timesItFreeze;
    private float velX;
    private float velY;

    public EnemyFour(PlayScreen screen, MapObject object) {
        super(screen, object);

        // Animations
        enemyFourAnimation = Assets.getInstance().getEnemyFour().getEnemyFourAnimation();
        enemyFourFrozenAnimation = Assets.getInstance().getEnemyFour().getEnemyFourFrozenAnimation();
        explosionAnimation = Assets.getInstance().getExplosionD().getExplosionDAnimation();

        // Determines the size of the EnemyFour's drawing on the screen
        setBounds(getX(), getY(), AssetEnemyFour.WIDTH_METERS, AssetEnemyFour.HEIGHT_METERS);

        // Move to (targetX, targetY) at constant speed
        targetX = getX() + (WAVELENGTH_METERS / 2) * MathUtils.randomSign();
        targetY = getY() - (AMPLITUDE_METERS / 2);
        tmp.set(getX(), getY());
        Vector2Util.goToTarget(tmp, targetX, targetY, LINEAR_VELOCITY);
        velocity.set(tmp);

        // Variables initialization
        stateTime = MathUtils.random(0, enemyFourAnimation.getAnimationDuration()); // To flap untimely with others;
        frozen = false;
        frozenStateTime = 0;
        velX = 0;
        velY = 0;

        // Indicates how many times this enemy can be frozen.
        timesItFreeze = object.getProperties().get(B2WorldCreator.KEY_TIMES_IT_FREEZE, TIMES_IT_FREEZE_DEFAULT, Integer.class);
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
        // Set velocity because It could have been changed (see reverseVelocity(), checkPath())
        b2body.setLinearVelocity(velocity);

        // Update this Sprite to correspond with the position of the Box2D body.
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        if (frozen) {
            // Preserve the flip state
            boolean isFlipX = isFlipX();
            boolean isFlipY = isFlipY();

            setRegion((TextureRegion) enemyFourFrozenAnimation.getKeyFrame(stateTime, true));
            stateTime += dt;

            // Apply previous flip state
            setFlip(isFlipX, isFlipY);

            frozenToNormal(dt);
        } else {
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
    }

    private void frozenToNormal(float dt) {
        frozenStateTime += dt;
        if (frozenStateTime >= FROZEN_TIME_SECONDS) {
            stateTime = 0;
            frozenStateTime = 0;
            frozen = false;

            // Restore velocity
            velocity.set(velX, velY);

            // Audio FX
            AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getFrozen());
        }
    }

    private void checkPath() {
        if (b2body.getLinearVelocity().x > 0) { // EnemyFour goes to the right
            if (b2body.getPosition().x >= targetX) { // EnemyFour reaches targetX
                targetX = targetX + WAVELENGTH_METERS / 2; // New targetX (right)

                float rightLimit = screen.getGameCam().position.x + screen.getGameViewPort().getWorldWidth() / 2 -
                        CIRCLE_SHAPE_RADIUS_METERS - MARGIN_X;
                if (targetX > rightLimit) {
                    targetX = rightLimit;
                }
            }
        } else { // // EnemyFour goes to the left
            if (b2body.getPosition().x <= targetX) { // EnemyFour reaches targetX
                targetX = targetX - WAVELENGTH_METERS / 2; // New targetX (left)

                float leftLimit = CIRCLE_SHAPE_RADIUS_METERS + MARGIN_X;
                if (targetX < leftLimit) {
                    targetX = leftLimit;
                }
            }
        }

        if (b2body.getLinearVelocity().y > 0) { // EnemyFour goes up
            if (b2body.getPosition().y >= targetY) { // EnemyFour reaches targetY
                targetY = targetY - AMPLITUDE_METERS; // New targetY (down)
            }
        } else { // EnemyFour goes down
            if (b2body.getPosition().y <= targetY) { // EnemyFour reaches targetY
                targetY = targetY + AMPLITUDE_METERS; // New targetY (up)
            }
        }

        // Go to target with constant velocity
        tmp.set(b2body.getPosition().x, b2body.getPosition().y);
        Vector2Util.goToTarget(tmp, targetX, targetY, LINEAR_VELOCITY);
        velocity.set(tmp);
    }

    @Override
    protected void stateInjured(float dt) {
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
    protected boolean isSpriteOutsideBottomEdge(float bottomEdge) {
        return  bottomEdge > getY() + getHeight() && // Sprite beyond bottom edge
                (velocity.y < 0 || frozen) && // Going down or frozen
                bottomEdge - targetY > AMPLITUDE_METERS; // Distance between bottomEdge and targetY > amplitude
    }

    @Override
    protected boolean isSpriteOutsideUpperEdge(float upperEdge) {
        return false; // We don't want to kill this Enemy if it's beyond the upper edge.
    }

    @Override
    protected Sound getVoice() {
        return Assets.getInstance().getSounds().getWarble();
    }

    @Override
    protected float getSpeakTimeSeconds() {
        return SPEAK_TIME_SECONDS;
    }

    @Override
    public void onHit(Weapon weapon) {
        if (frozen) {
            weapon.onTarget();
        } else {
            timesItFreeze--;
            if (timesItFreeze >= 0) {
                weapon.onTarget();

                stateTime = 0;
                frozen = true;

                // We get the linear velocity that this enemy had before being frozen
                velX = velocity.x;
                velY = velocity.y;

                // Stop motion
                velocity.set(0.0f, 0.0f);

                // Audio FX
                AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getFrozen());
            } else {
                super.onHit(weapon);
            }
        }
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
        currentState = State.KNOCK_BACK;
    }

    @Override
    public void onBump() {
        reverseVelocity(true, false);
    }

    @Override
    public void onDestroy() {
        onHit();
    }

    @Override
    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(targetX, targetY, DEBUG_TARGET_RADIUS_METERS);
        super.renderDebug(shapeRenderer);
    }
}
