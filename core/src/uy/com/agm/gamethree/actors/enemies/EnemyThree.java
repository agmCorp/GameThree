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
import uy.com.agm.gamethree.assets.sprites.AssetEnemyThree;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionN;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 12/9/2017.
 */

public class EnemyThree extends Enemy {
    private static final String TAG = EnemyThree.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    private static final float CIRCLE_SHAPE_RADIUS_METERS = 29.0f / PlayScreen.PPM;
    private static final float MIN_VELOCITY_X = 1.0f;
    private static final float MAX_VELOCITY_X = 2.0f;
    private static final float VELOCITY_Y = 0.0f;
    private static final float DENSITY = 1000.0f;
    private static final float FIRE_DELAY_SECONDS = 4.0f;
    private static final float SPEAK_TIME_SECONDS = 2.5f;
    private static final int SCORE = 20;

    private float stateTime;
    private Animation enemyThreeAnimation;
    private Animation explosionAnimation;

    public EnemyThree(PlayScreen screen, MapObject object) {
        super(screen, object);

        // Animations
        enemyThreeAnimation = Assets.getInstance().getEnemyThree().getEnemyThreeAnimation();
        explosionAnimation = Assets.getInstance().getExplosionN().getExplosionNAnimation();

        // Determines the size of the EnemyThree's drawing on the screen
        setBounds(getX(), getY(), AssetEnemyThree.WIDTH_METERS, AssetEnemyThree.HEIGHT_METERS);

        // Variables initialization
        stateTime = MathUtils.random(0, enemyThreeAnimation.getAnimationDuration()); // To blink untimely with others
        velocity.set(MathUtils.randomSign() * MathUtils.random(MIN_VELOCITY_X, MAX_VELOCITY_X), VELOCITY_Y);
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
        // EnemyThree doesn't collide with HERO_SHIELD_BIT, because the shield is too big.
        // EnemyThree only collides with Hero (HERO_BIT, HERO_GHOST_BIT and HERO_TOUGH_BIT).
        fdef.filter.categoryBits = WorldContactListener.ENEMY_BIT; // Depicts what this fixture is
        fdef.filter.maskBits = WorldContactListener.BORDER_BIT |
                WorldContactListener.OBSTACLE_BIT |
                WorldContactListener.POWER_BOX_BIT |
                WorldContactListener.ITEM_BIT |
                WorldContactListener.HERO_WEAPON_BIT |
                WorldContactListener.ENEMY_BIT |
                WorldContactListener.HERO_BIT |
                WorldContactListener.HERO_GHOST_BIT |
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
        TextureRegion region = (TextureRegion) enemyThreeAnimation.getKeyFrame(stateTime, true);
        stateTime += dt;
        return region;
    }

    @Override
    protected void stateAlive(float dt) {
        // Set velocity because (at rest) It could have been changed a little due to a collision
        b2body.setLinearVelocity(velocity);

        /* Update this Sprite to correspond with the position of the Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, EnemyThree may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        TextureRegion region = (TextureRegion) enemyThreeAnimation.getKeyFrame(stateTime, true);
        setRegion(region);
        stateTime += dt;

        // Shoot time!
        super.openFire(dt);
    }

    @Override
    protected void stateInjured(float dt) {
        // Release an item
        getItemOnHit();

        // Explosion animation
        stateTime = 0;

        // Audio FX
        pum(Assets.getInstance().getSounds().getSquish());

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
                setBounds(getX() + getWidth() / 2 - AssetExplosionN.WIDTH_METERS * expScale / 2, getY() + getHeight() / 2 - AssetExplosionN.HEIGHT_METERS * expScale / 2,
                        AssetExplosionN.WIDTH_METERS * expScale, AssetExplosionN.HEIGHT_METERS * expScale);
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
        return Assets.getInstance().getScene2d().getHelpEnemyThree();
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
        return Assets.getInstance().getSounds().getBite();
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
    public void onBump() {
        reverseVelocity(true, false);
    }

    @Override
    public void onDestroy() {
        onHit();
    }
}
