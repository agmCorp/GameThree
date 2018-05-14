package uy.com.agm.gamethree.actors.enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import uy.com.agm.gamethree.actors.backgroundObjects.IAvoidLandingObject;
import uy.com.agm.gamethree.actors.weapons.IShootStrategy;
import uy.com.agm.gamethree.actors.weapons.enemy.EnemyDefaultShooting;
import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyOne;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionA;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 12/9/2017.
 */

public class EnemyOne extends Enemy {
    private static final String TAG = EnemyOne.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    private static final float CIRCLE_SHAPE_RADIUS_METERS = 29.0f / PlayScreen.PPM;
    private static final float VELOCITY_X = 1.0f;
    private static final float BOOST_VELOCITY = 2.0f;
    private static final float VELOCITY_Y = -1.0f;
    private static final float FIRE_DELAY_SECONDS = 3.0f;
    private static final float CHANGE_DIRECTION_SECONDS = 1.0f;
    private static final float SPEAK_TIME_SECONDS = 4.5f;
    private static final float MARGIN_UPPER_METERS = 0.16f;
    private static final float MARGIN_BOTTOM_METERS = 0.16f;
    private static final int SCORE = 5;

    private float stateTime;
    private Animation enemyOneAnimation;
    private Animation explosionAnimation;
    private boolean changeDirection;
    private float changeDirectionTime;

    public EnemyOne(PlayScreen screen, MapObject object) {
        super(screen, object);

        // Animations
        enemyOneAnimation = Assets.getInstance().getEnemyOne().getEnemyOneAnimation();
        explosionAnimation = Assets.getInstance().getExplosionA().getExplosionAAnimation();

        // Determines the size of the EnemyOne's drawing on the screen
        setBounds(getX(), getY(), AssetEnemyOne.WIDTH_METERS, AssetEnemyOne.HEIGHT_METERS);

        // Variables initialization
        stateTime = MathUtils.random(0, enemyOneAnimation.getAnimationDuration()); // To walk untimely with others
        velocity.set(MathUtils.randomSign() * VELOCITY_X, VELOCITY_Y);
        changeDirection = false;
        changeDirectionTime = 0;
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
                WorldContactListener.OBSTACLE_BIT |
                WorldContactListener.PATH_BIT |
                WorldContactListener.POWER_BOX_BIT |
                WorldContactListener.ITEM_BIT |
                WorldContactListener.HERO_WEAPON_BIT |
                WorldContactListener.HERO_SHIELD_BIT |
                WorldContactListener.ENEMY_BIT |
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
        TextureRegion region = (TextureRegion) enemyOneAnimation.getKeyFrame(stateTime, true);
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
        setRegion((TextureRegion) enemyOneAnimation.getKeyFrame(stateTime, true));
        stateTime += dt;

        // Shoot time!
        super.openFire(dt);

        if (changeDirection) {
            changeDirectionTime += dt;
            if (changeDirectionTime > CHANGE_DIRECTION_SECONDS) {
                velocity.x = MathUtils.randomSign() * VELOCITY_X;
                velocity.y *= -1;
                changeDirection = false;
                changeDirectionTime = 0;
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
                // Determines the size of the explosion on the screen
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
        return Assets.getInstance().getScene2d().getHelpEnemyOne();
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
        return Assets.getInstance().getSounds().getGrowl();
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
        currentState = State.KNOCK_BACK;
    }

    @Override
    public void onBump(IAvoidLandingObject obj) {
        velocity.x = MathUtils.randomSign() * VELOCITY_X * BOOST_VELOCITY;
        velocity.y *= -1;
        changeDirection = true;
        changeDirectionTime = 0;
    }

    @Override
    public void onBump() {
        reverseVelocity(true, false);
    }

    @Override
    public void onDestroy() {
        onHit();
    }
}
