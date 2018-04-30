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
import uy.com.agm.gamethree.assets.sprites.AssetEnemySeven;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionA;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.B2WorldCreator;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 12/9/2017.
 */

public class EnemySeven extends Enemy {
    private static final String TAG = EnemySeven.class.getName();

    // Constants
    private static final float CIRCLE_SHAPE_RADIUS_METERS = 29.0f / PlayScreen.PPM;
    private static final float RESIZE_FACTOR = 0.5f;
    private static final float VELOCITY_X = 3.0f;
    private static final float VELOCITY_Y = -3.0f;
    private static final float FIRE_DELAY_SECONDS = 1.0f;
    public static final int MIN_CLONE = 2;
    public static final int MAX_CLONE = 5;
    private static final float CHANGE_HORIZONTAL_SECONDS = 2.0f;
    private static final float CHANGE_VERTICAL_SECONDS = 0.2f;
    private static final float SPEAK_TIME_SECONDS = 2.3f;
    private static final int SCORE = 5;

    private float stateTime;
    private Animation enemySevenAnimation;
    private Animation explosionAnimation;
    private float changeHorizontalTime;
    private float changeVerticalTime;
    private boolean isTiny;

    public EnemySeven(PlayScreen screen, MapObject object) {
        super(screen, object);

        // Animations
        enemySevenAnimation = Assets.getInstance().getEnemySeven().getEnemySevenAnimation();
        explosionAnimation = Assets.getInstance().getExplosionA().getExplosionAAnimation();

        // Setbounds is the one that determines the size of the EnemyOne's drawing on the screen
        if (isTiny) {
            setBounds(getX(), getY(), AssetEnemySeven.WIDTH_METERS * RESIZE_FACTOR, AssetEnemySeven.HEIGHT_METERS * RESIZE_FACTOR);
        } else {
            setBounds(getX(), getY(), AssetEnemySeven.WIDTH_METERS, AssetEnemySeven.HEIGHT_METERS);
        }

        // Variables initialization
        stateTime = MathUtils.random(0, enemySevenAnimation.getAnimationDuration()); // To walk untimely with others
        velocity.set(0, VELOCITY_Y);
        changeHorizontalTime = 0;
        changeVerticalTime = 0;
    }

    @Override
    protected void defineEnemy() {
        float circleShapeRadiusMeters;
        if (object.getProperties().containsKey(B2WorldCreator.KEY_ENEMY_SEVEN)) {
            circleShapeRadiusMeters = CIRCLE_SHAPE_RADIUS_METERS;
            isTiny = false;
        } else {
            circleShapeRadiusMeters = CIRCLE_SHAPE_RADIUS_METERS * RESIZE_FACTOR;
            isTiny = true;
        }

        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY()); // In b2box the origin is at the center of the body
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        b2body.setFixedRotation(true);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(circleShapeRadiusMeters);
        fdef.filter.categoryBits = WorldContactListener.ENEMY_BIT; // Depicts what this fixture is
        fdef.filter.maskBits = WorldContactListener.BORDER_BIT |
                WorldContactListener.ITEM_BIT |
                WorldContactListener.ENEMY_BIT |
                WorldContactListener.HERO_WEAPON_BIT |
                WorldContactListener.HERO_SHIELD_BIT |
                WorldContactListener.HERO_BIT |
                WorldContactListener.HERO_TOUGH_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
        fdef.shape = shape;
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
        TextureRegion region = (TextureRegion) enemySevenAnimation.getKeyFrame(stateTime, true);
        stateTime += dt;
        return region;
    }

    @Override
    protected void stateAlive(float dt) {
        // Set velocity because It could have been changed (see reverseVelocity)
        b2body.setLinearVelocity(velocity);

        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, EnemyOne may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) enemySevenAnimation.getKeyFrame(stateTime, true));
        stateTime += dt;

        // Shoot time!
        super.openFire(dt);

        if (velocity.x != 0) {
            changeHorizontalTime += dt;
            if (changeHorizontalTime > CHANGE_HORIZONTAL_SECONDS) {
                velocity.set(0.0f, VELOCITY_Y);
                changeHorizontalTime = 0;

            }
        }
        if (velocity.y != 0) {
            changeVerticalTime += dt;
            if (changeVerticalTime > CHANGE_VERTICAL_SECONDS) {
                velocity.set(MathUtils.randomSign() * VELOCITY_X, 0.0f);
                changeVerticalTime = 0;
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
                // Setbounds is the one that determines the size of the explosion on the screen
                setBounds(getX() + getWidth() / 2 - AssetExplosionA.WIDTH_METERS * expScale / 2, getY() + getHeight() / 2 - AssetExplosionA.HEIGHT_METERS * expScale / 2,
                        AssetExplosionA.WIDTH_METERS * expScale, AssetExplosionA.HEIGHT_METERS * expScale);
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
        return Assets.getInstance().getScene2d().getHelpEnemySeven();
    }

    @Override
    protected Sound getVoice() {
        return Assets.getInstance().getSounds().getMarching();
    }

    @Override
    protected float getSpeakTimeSeconds() {
        return SPEAK_TIME_SECONDS;
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
        currentState = isTiny ? State.KNOCK_BACK : State.INJURED;
    }

    @Override
    public void onBump() {
        reverseVelocity(true, false);
    }
}
