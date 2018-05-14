package uy.com.agm.gamethree.actors.enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import uy.com.agm.gamethree.actors.weapons.IShootStrategy;
import uy.com.agm.gamethree.actors.weapons.Weapon;
import uy.com.agm.gamethree.actors.weapons.enemy.EnemyHalfMoonShooting;
import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyEleven;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionK;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.Vector2Util;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 12/9/2017.
 */

public class EnemyEleven extends Enemy {
    private static final String TAG = EnemyEleven.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    private static final float CIRCLE_SHAPE_RADIUS_METERS = 29.0f / PlayScreen.PPM;
    private static final float TARGET_RADIUS_METERS = 10.0f / PlayScreen.PPM;
    private static final float LINEAR_VELOCITY = 3.0f;
    private static final float DENSITY = 1000.0f;
    private static final float SHOOT_TIME_SECONDS = 3.0f;
    private static final float FIRE_DELAY_SECONDS = 3.0f;
    private static final int MIN_BULLETS = 2;
    private static final int MAX_BULLETS = 3;
    private static final float SPEAK_TIME_SECONDS = 2.0f;
    private static final float MARGIN_UPPER_METERS = 0.16f;
    private static final float MARGIN_BOTTOM_METERS = 0.16f;
    private static final int SCORE = 20;

    private float stateTime;
    private Animation enemyElevenAnimation;
    private Animation enemyElevenShootAnimation;
    private Animation explosionAnimation;
    private float shootTime;
    private boolean isShooting;

    // Circle on the screen where EnemyEleven must go
    private Circle target;
    private Circle tmpCircle; // Temporary GC friendly circle

    public EnemyEleven(PlayScreen screen, MapObject object) {
        super(screen, object);

        // Animations
        enemyElevenAnimation = Assets.getInstance().getEnemyEleven().getEnemyElevenAnimation();
        enemyElevenShootAnimation = Assets.getInstance().getEnemyEleven().getEnemyElevenShootAnimation();
        explosionAnimation = Assets.getInstance().getExplosionK().getExplosionKAnimation();

        // Determines the size of the EnemyEleven's drawing on the screen
        setBounds(getX(), getY(), AssetEnemyEleven.WIDTH_METERS, AssetEnemyEleven.HEIGHT_METERS);

        // Move to target at constant speed
        tmp.set(b2body.getPosition().x, b2body.getPosition().y);
        float worldHeight = screen.getGameViewPort().getWorldHeight();
        float x = MathUtils.random(0 + CIRCLE_SHAPE_RADIUS_METERS, screen.getGameViewPort().getWorldWidth() - CIRCLE_SHAPE_RADIUS_METERS);
        float y = tmp.y - MathUtils.random(worldHeight / 4, 3 * worldHeight / 4);
        target = new Circle(x, y, TARGET_RADIUS_METERS);
        Vector2Util.goToTarget(tmp, target.x, target.y, LINEAR_VELOCITY);
        velocity.set(tmp);

        // Variables initialization
        stateTime = MathUtils.random(0, enemyElevenAnimation.getAnimationDuration()); // To walk untimely with others
        shootTime = 0;
        isShooting = false;

        // Temporary GC friendly circle
        tmpCircle = new Circle();
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
        return new EnemyHalfMoonShooting(screen, FIRE_DELAY_SECONDS, FIRE_DELAY_SECONDS, MathUtils.random(MIN_BULLETS, MAX_BULLETS));
    }

    @Override
    protected float getCircleShapeRadiusMeters() {
        return CIRCLE_SHAPE_RADIUS_METERS;
    }

    @Override
    protected TextureRegion getKnockBackFrame(float dt) {
        TextureRegion region = (TextureRegion) enemyElevenShootAnimation.getKeyFrame(stateTime, true);
        stateTime += dt;
        return region;
    }

    @Override
    protected void stateAlive(float dt) {
        // Set velocity because It could have been changed in this method
        b2body.setLinearVelocity(velocity);

        // Update our Sprite to correspond with the position of our Box2D body.
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        if (isShooting) {
            // Preserve the flip state
            boolean isFlipX = isFlipX();
            boolean isFlipY = isFlipY();

            setRegion((TextureRegion) enemyElevenShootAnimation.getKeyFrame(stateTime, true));
            stateTime += dt;

            // Apply previous flip state
            setFlip(isFlipX, isFlipY);

            shootTime += dt;
            if (shootTime >= SHOOT_TIME_SECONDS) {
                isShooting = false;
                stateTime = 0;

                // Move to new target at constant speed
                float gameCamY = screen.getGameCam().position.y;
                float worldHeight = screen.getGameViewPort().getWorldHeight();
                float x = MathUtils.random(0 + CIRCLE_SHAPE_RADIUS_METERS, screen.getGameViewPort().getWorldWidth() - CIRCLE_SHAPE_RADIUS_METERS);
                float y = MathUtils.random(gameCamY - worldHeight / 2, gameCamY + worldHeight / 2);
                target.setPosition(x, y);
                tmp.set(b2body.getPosition().x, b2body.getPosition().y);
                Vector2Util.goToTarget(tmp, target.x, target.y, LINEAR_VELOCITY);
                velocity.set(tmp);
            } else {
                // Shoot time!
                shoot(dt);
            }
        } else {
            TextureRegion region = (TextureRegion) enemyElevenAnimation.getKeyFrame(stateTime, true);

            if (b2body.getLinearVelocity().x > 0 && region.isFlipX()) {
                region.flip(true, false);
            }
            if (b2body.getLinearVelocity().x < 0 && !region.isFlipX()) {
                region.flip(true, false);
            }

            setRegion(region);
            stateTime += dt;

            // EnemyEleven reaches target
            tmpCircle.set(b2body.getPosition().x, b2body.getPosition().y, CIRCLE_SHAPE_RADIUS_METERS);
            if (target.overlaps(tmpCircle)) {
                velocity.set(0.0f, 0.0f); // Stop motion
                isShooting = true;
                stateTime = 0;
                shootTime = 0;
            }
        }
    }

    @Override
    protected void stateInjured(float dt) {
        // Release an item
        getItemOnHit();

        // Explosion animation
        stateTime = 0;

        // Audio FX
        pum(Assets.getInstance().getSounds().getPebbles());

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
                setBounds(getX() + getWidth() / 2 - AssetExplosionK.WIDTH_METERS * expScale / 2, getY() + getHeight() / 2 - AssetExplosionK.HEIGHT_METERS * expScale / 2,
                        AssetExplosionK.WIDTH_METERS * expScale, AssetExplosionK.HEIGHT_METERS * expScale);
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
        return Assets.getInstance().getScene2d().getHelpEnemyEleven();
    }

    @Override
    protected boolean isOutsideBottomEdge(float bottomEdge) {
        // MARGIN_BOTTOM_METERS is tolerance
        return bottomEdge > getY() + getHeight() + MARGIN_BOTTOM_METERS;
    }

    @Override
    protected boolean isOutsideUpperEdge(float upperEdge) {
        // MARGIN_UPPER_METERS is tolerance
        return upperEdge < getY() - MARGIN_UPPER_METERS;
    }

    @Override
    protected Sound getVoice() {
        return Assets.getInstance().getSounds().getRockScrape();
    }

    @Override
    protected float getSpeakTimeSeconds() {
        return SPEAK_TIME_SECONDS;
    }

    @Override
    public void onHit(Weapon weapon) {
        if (!isShooting) {
            weapon.onTarget();
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

    @Override
    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(target.x, target.y, target.radius);
        super.renderDebug(shapeRenderer);
    }
}
