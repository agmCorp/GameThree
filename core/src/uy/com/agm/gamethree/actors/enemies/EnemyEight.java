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
import uy.com.agm.gamethree.actors.weapons.Weapon;
import uy.com.agm.gamethree.actors.weapons.enemy.EnemyDefaultShooting;
import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyEight;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionF;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.Vector2Util;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 12/9/2017.
 */

public class EnemyEight extends Enemy {
    private static final String TAG = EnemyEight.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    private static final float CIRCLE_SHAPE_RADIUS_METERS = 29.0f / PlayScreen.PPM;
    private static final float LINEAR_VELOCITY = 5.0f;
    private static final float BOOST_VELOCITY = 100.0f;
    private static final float DENSITY = 1000.0f;
    private static final float PERIOD_SECONDS = 1.6f;
    private static final float RADIUS_METERS = 2.4f;
    private static final float FIRE_DELAY_SECONDS = 2.0f;
    private static final float SPEAK_TIME_SECONDS = 1.5f;
    private static final float MARGIN_UPPER_METERS = 0.3f;
    private static final float MARGIN_BOTTOM_METERS = 1.1f;
    private static final int SCORE = 15;

    private boolean damage;
    private float stateTime;
    private Animation enemyEightAnimation;
    private Animation explosionAnimation;

    // The whole path is divided into three sections
    private boolean path1;
    private boolean path2;
    private boolean path3;
    private int sign;
    private float elapsedTime;

    public EnemyEight(PlayScreen screen, MapObject object) {
        super(screen, object);

        // Determines the size of the EnemyEight's drawing on the screen
        setBounds(getX(), getY(), AssetEnemyEight.WIDTH_METERS, AssetEnemyEight.HEIGHT_METERS);

        // Animations
        enemyEightAnimation = Assets.getInstance().getEnemyEight().getEnemyEightAnimation();
        explosionAnimation = Assets.getInstance().getExplosionF().getExplosionFAnimation();

        // Variables initialization
        damage = false;
        stateTime = MathUtils.random(0, enemyEightAnimation.getAnimationDuration()); // To flap untimely with others
        path1 = true;
        path2 = false;
        path3 = false;
        sign = b2body.getPosition().x < screen.getGameCam().position.x ? -1 : 1;
        elapsedTime = 0;
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
        fdef.filter.maskBits = WorldContactListener.HERO_WEAPON_BIT |
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
        TextureRegion region = (TextureRegion) enemyEightAnimation.getKeyFrame(stateTime, true);
        stateTime += dt;
        return region;
    }

    @Override
    protected void stateAlive(float dt) {
        // Set new velocity (see checkPath(dt))
        b2body.setLinearVelocity(velocity);

        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, EnemyEight may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        TextureRegion region = (TextureRegion) enemyEightAnimation.getKeyFrame(stateTime, true);
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

        checkPath(dt);
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
                setBounds(getX() + getWidth() / 2 - AssetExplosionF.WIDTH_METERS * expScale / 2, getY() + getHeight() / 2 - AssetExplosionF.HEIGHT_METERS * expScale / 2,
                        AssetExplosionF.WIDTH_METERS * expScale, AssetExplosionF.HEIGHT_METERS * expScale);
            }
            setRegion((TextureRegion) explosionAnimation.getKeyFrame(stateTime, true));
            stateTime += dt;
        }
    }

    private void checkPath(float dt) {
        if (path1) {
            checkPath1();
        } else {
            if (path2) {
                checkPath2(dt);
            } else {
                if (path3) {
                    checkPath3();
                } else {
                    getAway();
                }
            }
        }
    }

    private void checkPath1() {
        // We don't use a variable (targetY) because the cam is always moving and we want a "dynamic" targetY
        if (b2body.getPosition().y <= screen.getGameCam().position.y - screen.getGameViewPort().getWorldHeight() / 4) { // EnemyEight reaches target
            path1 = false;
            path2 = true;
        } else {
            // Move to (targetX, targetY) at constant speed
            float targetX = screen.getGameCam().position.x + sign * (CIRCLE_SHAPE_RADIUS_METERS - screen.getGameViewPort().getWorldWidth() / 2);
            float targetY = screen.getGameCam().position.y - screen.getGameViewPort().getWorldHeight() / 4;

            tmp.set(b2body.getPosition().x, b2body.getPosition().y);
            Vector2Util.goToTarget(tmp, targetX, targetY, LINEAR_VELOCITY);
            velocity.set(tmp);
        }
    }

    private void checkPath2(float dt) {
        // We don't use a variable (targetY) because the cam is always moving and we want a "dynamic" targetY
        if (b2body.getPosition().y >= screen.getGameCam().position.y - screen.getGameViewPort().getWorldHeight() / 4) { // EnemyEight reaches target
            path2 = false;
            path3 = true;
        } else {
            elapsedTime += dt;
            float w = 2 * MathUtils.PI / PERIOD_SECONDS;
            velocity.set(sign * RADIUS_METERS * w * MathUtils.sin(w * elapsedTime), -RADIUS_METERS * w * MathUtils.cos(w * elapsedTime));
        }
    }

    private void checkPath3() {
        // We don't use a variable (targetY) because the cam is always moving and we want a "dynamic" targetY
        if (b2body.getPosition().y >= screen.getGameCam().position.y + screen.getGameViewPort().getWorldHeight() / 2) { // EnemyEight reaches target
            path3 = false;
        } else {
            // Move to (targetX, targetY) at constant speed
            float targetX = screen.getGameCam().position.x - sign * screen.getGameViewPort().getWorldWidth() / 2;
            float targetY = screen.getGameCam().position.y + screen.getGameViewPort().getWorldHeight() / 2;

            tmp.set(b2body.getPosition().x, b2body.getPosition().y);
            Vector2Util.goToTarget(tmp, targetX, targetY, LINEAR_VELOCITY); // Corner of the screen
            velocity.set(tmp);
        }
    }

    private void getAway() {
        // Beyond upperEdge (see Enemy.checkBoundaries())
        velocity.set(0, LINEAR_VELOCITY * BOOST_VELOCITY);
    }

    @Override
    protected String getClassName() {
        return this.getClass().getName();
    }

    @Override
    protected TextureRegion getHelpImage() {
        return Assets.getInstance().getScene2d().getHelpEnemyEight();
    }

    @Override
    protected boolean isOutsideBottomEdge(float bottomEdge) {
        // Margin is important because we don't want to kill this Enemy who is flying around
        // (going in and out of the camera).
        return bottomEdge > getY() + getHeight() + MARGIN_BOTTOM_METERS;
    }

    @Override
    protected boolean isOutsideUpperEdge(float upperEdge) {
        // Margin is important because we don't want to kill this Enemy who is flying around
        // (going in and out of the camera).
        return upperEdge < getY() - MARGIN_UPPER_METERS;
    }

    @Override
    protected Sound getVoice() {
        return Assets.getInstance().getSounds().getBuzz();
    }

    @Override
    protected float getSpeakTimeSeconds() {
        return SPEAK_TIME_SECONDS;
    }

    @Override
    public void onHit(Weapon weapon) {
        if (!damage) {
            weapon.onBounce();
            damage = true;
        } else {
            super.onHit(weapon);
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
        // Nothing to do here
    }

    @Override
    public void onDestroy() {
        onHit();
    }
}
