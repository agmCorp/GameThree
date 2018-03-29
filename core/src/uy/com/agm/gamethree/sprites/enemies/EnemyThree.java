package uy.com.agm.gamethree.sprites.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyThree;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionC;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.weapons.IShootStrategy;
import uy.com.agm.gamethree.sprites.weapons.enemy.EnemyDefaultShooting;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 12/9/2017.
 */

public class EnemyThree extends Enemy {
    private static final String TAG = EnemyThree.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    private static final float CIRCLE_SHAPE_RADIUS_METERS = 29.0f / PlayScreen.PPM;
    private static final float EXPLOSION_SCALE = 3.0f;
    private static final float VELOCITY_X = 0.0f;
    private static final float VELOCITY_Y = 0.0f;
    private static final float DENSITY = 1000.0f;
    private static final float FIRE_DELAY_SECONDS = 4.0f;
    private static final Color KNOCK_BACK_COLOR = Color.BLACK;
    private static final float KNOCK_BACK_SECONDS = 0.2f;
    private static final float KNOCK_BACK_FORCE_X = 0.0f;
    private static final float KNOCK_BACK_FORCE_Y = 1000.0f;
    private static final int SCORE = 20;

    private float stateTime;
    private Animation enemyThreeAnimation;
    private Animation explosionAnimation;
    private float expScale;

    // Knock back effect
    private boolean knockBack;
    private boolean knockBackStarted;
    private float knockBackTime;
    private float initPosX;
    private float initPosY;

    public EnemyThree(PlayScreen screen, MapObject object) {
        super(screen, object);

        // Animations
        enemyThreeAnimation = Assets.getInstance().getEnemyThree().getEnemyThreeAnimation();
        explosionAnimation = Assets.getInstance().getExplosionC().getExplosionCAnimation();
        expScale = pum ? EXPLOSION_SCALE : 1;

        // Setbounds is the one that determines the size of the EnemyThree's drawing on the screen
        setBounds(getX(), getY(), AssetEnemyThree.WIDTH_METERS, AssetEnemyThree.HEIGHT_METERS);

        stateTime = MathUtils.random(0, enemyThreeAnimation.getAnimationDuration()); // To blink untimely with others
        knockBack = false;
        knockBackStarted = false;
        knockBackTime = 0;
        initPosX = 0;
        initPosY = 0;
        velocity.set(VELOCITY_X, VELOCITY_Y);
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY()); // In b2box the origin is at the center of the body
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(CIRCLE_SHAPE_RADIUS_METERS);
        fdef.filter.categoryBits = WorldContactListener.ENEMY_BIT; // Depicts what this fixture is
        fdef.filter.maskBits = WorldContactListener.BORDER_BIT |
                WorldContactListener.OBSTACLE_BIT |
                WorldContactListener.POWER_BOX_BIT |
                WorldContactListener.ITEM_BIT |
                WorldContactListener.HERO_WEAPON_BIT |
                WorldContactListener.SHIELD_BIT |
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
    protected void stateAlive(float dt) {
        // Set velocity because (at rest) It could have been changed a little due to a collision
        b2body.setLinearVelocity(velocity);

        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, EnemyThree may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        TextureRegion region = (TextureRegion) enemyThreeAnimation.getKeyFrame(stateTime, true);
        if (b2body.getLinearVelocity().x > 0 && !region.isFlipX()) {
            region.flip(true, false);
        }
        if (b2body.getLinearVelocity().x < 0 && region.isFlipX()) {
            region.flip(true, false);
        }

        setRegion(region);
        stateTime += dt;

        // Shoot time!
        super.openFire(dt);
    }

    @Override
    protected void stateInjured(float dt) {
        if (knockBack) {
            knockBack(dt);
        } else {
            // Release an item
            getItemOnHit();

            // Destroy box2D body
            world.destroyBody(b2body);

            // Explosion animation
            stateTime = 0;

            // Audio FX and screen shake
            if (pum) {
                screen.getShaker().shake(SHAKE_DURATION);
                AudioManager.getInstance().play(Assets.getInstance().getSounds().getPum());
            } else {
                AudioManager.getInstance().play(Assets.getInstance().getSounds().getSquish());
            }

            // Set score
            screen.getHud().addScore(SCORE);

            // Set the new state
            currentState = State.EXPLODING;
        }
    }

    private void knockBack(float dt) {
        if (!knockBackStarted) {
            initKnockBack();
        }
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) enemyThreeAnimation.getKeyFrame(stateTime, true));
        setColor(KNOCK_BACK_COLOR);
        stateTime += dt;

        knockBackTime += dt;
        if (knockBackTime > KNOCK_BACK_SECONDS) {
            knockBack = false;
        }
    }

    private void initKnockBack() {
        // Initial sprite position
        initPosX = b2body.getPosition().x - getWidth() / 2;
        initPosY = b2body.getPosition().y - getHeight() / 2;

        // Knock back effect
        b2body.setLinearVelocity(0.0f, 0.0f);
        b2body.applyForce(MathUtils.randomSign() * KNOCK_BACK_FORCE_X, KNOCK_BACK_FORCE_Y,
                b2body.getPosition().x, b2body.getPosition().y, true);

        // EnemyThree can't collide with anything
        Filter filter = new Filter();
        filter.maskBits = WorldContactListener.NOTHING_BIT;

        // We set the previous filter in every fixture
        for (Fixture fixture : b2body.getFixtureList()) {
            fixture.setFilterData(filter);
            fixture.setDensity(0.0f);
        }
        b2body.resetMassData();

        knockBackStarted = true;
    }

    @Override
    protected void stateExploding(float dt) {
        if (explosionAnimation.isAnimationFinished(stateTime)) {
            currentState = State.DEAD;
        } else {
            if (stateTime == 0) { // Explosion starts
                // After the knock back, we set the explosion at the point where the enemy was hit with its default tint
                setColor(Color.WHITE); // Default
                setPosition(initPosX, initPosY);

                // Setbounds is the one that determines the size of the explosion on the screen
                setBounds(getX() + getWidth() / 2 - AssetExplosionC.WIDTH_METERS * expScale / 2, getY() + getHeight() / 2 - AssetExplosionC.HEIGHT_METERS * expScale / 2,
                        AssetExplosionC.WIDTH_METERS * expScale, AssetExplosionC.HEIGHT_METERS * expScale);
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
    public void onHit() {
        /*
         * We must remove its b2body to avoid collisions.
         * This can't be done here because this method is called from WorldContactListener that is invoked
         * from PlayScreen.update.world.step(...).
         * No b2body can be removed when the simulation is occurring, we must wait for the next update cycle.
         * Therefore, we use a flag (state) in order to point out this behavior and remove it later.
         */
        currentState = State.INJURED;
        knockBack = true;
    }

    @Override
    public void onBump() {
        // Nothing to do here
    }
}
