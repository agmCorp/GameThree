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
import uy.com.agm.gamethree.assets.sprites.AssetEnemyTwelve;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionP;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 12/9/2017.
 */

public class EnemyTwelve extends Enemy {
    private static final String TAG = EnemyTwelve.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    private static final float CIRCLE_SHAPE_RADIUS_METERS = 29.0f / PlayScreen.PPM;
    private static final float DEBUG_TARGET_RADIUS_METERS = 10.0f / PlayScreen.PPM;
    private static final float VELOCITY_X = 4.0f;
    private static final float VELOCITY_Y = 0.0f;
    private static final float DENSITY = 1000.0f;
    private static final float PERIOD_SECONDS = 2.0f;
    private static final float RADIUS_METERS = 1.0f;
    private static final float FIRE_DELAY_SECONDS = 2.0f;
    private static final float SPEAK_TIME_SECONDS = 2.5f;
    private static final int SCORE = 15;

    private float stateTime;
    private Animation enemyTwelveAnimation;
    private Animation enemyTwelveWeakAnimation;
    private Animation explosionAnimation;

    // The whole path is divided into two sections
    private boolean path1;
    private boolean path2;
    private float elapsedTime;
    private float velX;
    private float velY;
    private float targetX;
    private float debugTargetY;
    private boolean targetAhead;

    public EnemyTwelve(PlayScreen screen, MapObject object) {
        super(screen, object);

        // Determines the size of the EnemyTwelve's drawing on the screen
        setBounds(getX(), getY(), AssetEnemyTwelve.WIDTH_METERS, AssetEnemyTwelve.HEIGHT_METERS);

        // Animations
        enemyTwelveAnimation = Assets.getInstance().getEnemyTwelve().getEnemyTwelveAnimation();
        enemyTwelveWeakAnimation = Assets.getInstance().getEnemyTwelve().getEnemyTwelveWeakAnimation();
        explosionAnimation = Assets.getInstance().getExplosionP().getExplosionPAnimation();
        stateTime = MathUtils.random(0, enemyTwelveAnimation.getAnimationDuration()); // To flap untimely with others

        // Variables initialization
        path1 = true;
        path2 = false;
        elapsedTime = 0;
        velX = MathUtils.randomSign() * VELOCITY_X;
        velY = VELOCITY_Y;
        velocity.set(velX, velY);
        targetX = getRandomTargetX();
        debugTargetY = b2body.getPosition().y;
        targetAhead = false;
        if (velX > 0) {
            if (b2body.getPosition().x <= targetX) {
                targetAhead = true;
            }
        } else {
            if (b2body.getPosition().x >= targetX) {
                targetAhead = true;
            }
        }
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
        TextureRegion region = (TextureRegion) enemyTwelveAnimation.getKeyFrame(stateTime, true);
        stateTime += dt;
        return region;
    }

    @Override
    protected void stateAlive(float dt) {
        // Set new velocity (see checkPath(dt))
        b2body.setLinearVelocity(velocity);

        /* Update this Sprite to correspond with the position of the Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, EnemyTwelve may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        // Enemy's animation
        TextureRegion region = !path2 ? (TextureRegion) enemyTwelveAnimation.getKeyFrame(stateTime, true) :
                (TextureRegion) enemyTwelveWeakAnimation.getKeyFrame(stateTime, true);

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
                setBounds(getX() + getWidth() / 2 - AssetExplosionP.WIDTH_METERS * expScale / 2, getY() + getHeight() / 2 - AssetExplosionP.HEIGHT_METERS * expScale / 2,
                        AssetExplosionP.WIDTH_METERS * expScale, AssetExplosionP.HEIGHT_METERS * expScale);
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
            }
        }
    }

    private void checkPath1() {
        if (velocity.x > 0) {
            if (targetAhead) {
                if (b2body.getPosition().x >= targetX) { // Enemy reaches targetX
                    // We get the linear velocity that this enemy had before it changes paths
                    velX = velocity.x;
                    velY = velocity.y;

                    // Change path
                    path1 = false;
                    path2 = true;
                    elapsedTime = 0;
                }
            } else {
                if (getX() >= screen.getGameViewPort().getWorldWidth()) { // Right border
                    // Teleported left
                    b2body.setTransform(-getWidth() / 2, b2body.getPosition().y, b2body.getAngle());
                    targetAhead = true;
                }
            }
        } else {
            if (targetAhead) {
                if (b2body.getPosition().x <= targetX) { // Enemy reaches targetX
                    // We get the linear velocity that this enemy had before it changes paths
                    velX = velocity.x;
                    velY = velocity.y;

                    // Change path
                    path1 = false;
                    path2 = true;
                    elapsedTime = 0;
                }
            } else {
                if (getX() + getWidth() <= 0) { // Left border
                    // Teleported right
                    b2body.setTransform(screen.getGameViewPort().getWorldWidth() + getWidth() / 2, b2body.getPosition().y, b2body.getAngle());
                    targetAhead = true;
                }
            }
        }
    }

    private void checkPath2(float dt) {
        float worldWidth = screen.getGameViewPort().getWorldWidth();
        if (getX() >= worldWidth) { // Right border
            // Teleported left
            b2body.setTransform(-getWidth() / 2, b2body.getPosition().y, b2body.getAngle());
        } else {
            if (getX() + getWidth() <= 0) { // Left border
                // Teleported right
                b2body.setTransform(worldWidth + getWidth() / 2, b2body.getPosition().y, b2body.getAngle());
            }
        }

        if (elapsedTime >= PERIOD_SECONDS) {
            path1 = true;
            path2 = false;
            velocity.set(-velX, velY);
            targetX = getRandomTargetX();
            debugTargetY = b2body.getPosition().y;
            targetAhead = false;
        } else {
            elapsedTime += dt;
            float w = 2 * MathUtils.PI / PERIOD_SECONDS;
            velocity.set(RADIUS_METERS * w * MathUtils.sin(w * elapsedTime), -RADIUS_METERS * w * MathUtils.cos(w * elapsedTime));
        }
    }

    private float getRandomTargetX() {
        return MathUtils.random(0, screen.getGameViewPort().getWorldWidth());
    }

    @Override
    protected String getClassName() {
        return this.getClass().getName();
    }

    @Override
    protected TextureRegion getHelpImage() {
        return Assets.getInstance().getScene2d().getHelpEnemyTwelve();
    }

    @Override
    protected boolean isSpriteOutsideBottomEdge(float bottomEdge) {
        return bottomEdge > getY() + getHeight();
    }

    @Override
    protected boolean isSpriteOutsideUpperEdge(float upperEdge) {
        return false; // This Enemy never goes beyond the upper edge
    }

    @Override
    protected Sound getVoice() {
        return Assets.getInstance().getSounds().getFlutter();
    }

    @Override
    protected float getSpeakTimeSeconds() {
        return SPEAK_TIME_SECONDS;
    }

    @Override
    public void onHit(Weapon weapon) {
        if (!path2) {
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
        shapeRenderer.circle(targetX, debugTargetY, DEBUG_TARGET_RADIUS_METERS);
        super.renderDebug(shapeRenderer);
    }
}
