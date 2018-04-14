package uy.com.agm.gamethree.sprites.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyFive;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionF;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.boundary.Edge;
import uy.com.agm.gamethree.sprites.weapons.IShootStrategy;
import uy.com.agm.gamethree.sprites.weapons.Weapon;
import uy.com.agm.gamethree.sprites.weapons.enemy.EnemyDefaultShooting;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 12/9/2017.
 */

public class EnemyFive extends Enemy {
    private static final String TAG = EnemyFive.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float CIRCLE_SHAPE_RADIUS_METERS = 29.0f / PlayScreen.PPM;
    private static final float PERIOD_SECONDS = 2.0f;
    private static final float RADIUS_METERS = 1.3f;
    private static final float FIRE_DELAY_SECONDS = 3.0f;
    private static final Color KNOCK_BACK_COLOR = Color.BLACK;
    private static final float KNOCK_BACK_SECONDS = 0.2f;
    private static final float KNOCK_BACK_FORCE_X = 1000.0f;
    private static final float KNOCK_BACK_FORCE_Y = 1000.0f;
    private static final boolean CENTER_EXPLOSION_ON_HIT = false;
    private static final int SCORE = 15;

    private boolean damage;
    private float stateTime;
    private boolean counterclockwise;
    private float elapsedTime;
    private Animation enemyFiveAnimation;
    private Animation explosionAnimation;

    // Knock back effect
    private boolean knockBack;
    private boolean knockBackStarted;
    private float knockBackTime;
    private float hitX;
    private float hitY;

    public EnemyFive(PlayScreen screen, MapObject object) {
        super(screen, object);

        // Animations
        enemyFiveAnimation = Assets.getInstance().getEnemyFive().getEnemyFiveAnimation();
        explosionAnimation = Assets.getInstance().getExplosionF().getExplosionFAnimation();

        // Setbounds is the one that determines the size of the EnemyFive's drawing on the screen
        setBounds(getX(), getY(), AssetEnemyFive.WIDTH_METERS, AssetEnemyFive.HEIGHT_METERS);

        damage = false;
        stateTime = 0;
        counterclockwise = MathUtils.randomBoolean();
        elapsedTime = 0;
        knockBack = false;
        knockBackStarted = false;
        knockBackTime = 0;
        hitX = 0;
        hitY = 0;
        velocity.set(0.0f, 0.0f); // Initially at rest
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
        fdef.filter.maskBits = WorldContactListener.HERO_WEAPON_BIT |
                WorldContactListener.SHIELD_BIT |
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
    protected void stateAlive(float dt) {
        // Set new velocity (see getNewTangentialSpeed(...))
        b2body.setLinearVelocity(velocity);

        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, EnemyFive may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        TextureRegion region = (TextureRegion) enemyFiveAnimation.getKeyFrame(stateTime, true);
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

        velocity.set(getNewTangentialSpeed(dt));
    }

    @Override
    protected void stateInjured(float dt) {
        if (knockBack) {
            knockBack(dt);
        } else {
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
    }

    private void knockBack(float dt) {
        if (!knockBackStarted) {
            initKnockBack();
        }

        // We don't let this Enemy go beyond the upper edge
        float upperEdge = screen.getUpperEdge().getB2body().getPosition().y - Edge.HEIGHT_METERS / 2; //  Bottom edge of the upperEdge :)
        if (upperEdge <= b2body.getPosition().y + CIRCLE_SHAPE_RADIUS_METERS) {
            b2body.setLinearVelocity(0.0f, 0.0f); // Stop
        }

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) enemyFiveAnimation.getKeyFrame(stateTime, true));
        setColor(KNOCK_BACK_COLOR);
        stateTime += dt;

        knockBackTime += dt;
        if (knockBackTime > KNOCK_BACK_SECONDS) {
            knockBack = false;
        }
    }

    private void initKnockBack() {
        // Initial sprite position
        hitX = b2body.getPosition().x - getWidth() / 2;
        hitY = b2body.getPosition().y - getHeight() / 2;

        // Knock back effect
        b2body.setLinearVelocity(0.0f, 0.0f);
        b2body.applyForce(MathUtils.randomSign() * KNOCK_BACK_FORCE_X, KNOCK_BACK_FORCE_Y,
                b2body.getPosition().x, b2body.getPosition().y, true);

        // EnemyFive can't collide with anything
        Filter filter = new Filter();
        filter.maskBits = WorldContactListener.NOTHING_BIT;

        // We set the previous filter in every fixture
        for (Fixture fixture : b2body.getFixtureList()) {
            fixture.setFilterData(filter);
            fixture.setDensity(0.0f); // No density
        }
        b2body.resetMassData();

        knockBackStarted = true;
    }

    @Override
    protected void stateExploding(float dt) {
        if (explosionAnimation.isAnimationFinished(stateTime)) {
            currentState = State.SPLAT;
        } else {
            if (stateTime == 0) { // Explosion starts
                setColor(Color.WHITE); // Default tint
                // After the knock back, we set the explosion at the point where the enemy was hit
                if (CENTER_EXPLOSION_ON_HIT) {
                    setPosition(hitX, hitY);
                }

                // Setbounds is the one that determines the size of the explosion on the screen
                setBounds(getX() + getWidth() / 2 - AssetExplosionF.WIDTH_METERS * expScale / 2, getY() + getHeight() / 2 - AssetExplosionF.HEIGHT_METERS * expScale / 2,
                        AssetExplosionF.WIDTH_METERS * expScale, AssetExplosionF.HEIGHT_METERS * expScale);
            }
            setRegion((TextureRegion) explosionAnimation.getKeyFrame(stateTime, true));
            stateTime += dt;
        }
    }

    private Vector2 getNewTangentialSpeed(float dt) {
        /* Parametric equation of a Circle:
         * x = center_x + radius * cos(angle)
         * y = center_y + radius * sin(angle)
         *
         * Here 'angle' is the fraction of angular velocity (w) traveled in deltaTime (t).
         * Therefore:
         * w = 2 * PI / PERIOD
         *
         * Thus:
         * x = center_x + radius * cos(w * t)
         * y = center_y + radius * sin(w * t)
         *
         * Velocity (derivative d/dt)
         * x = -r * w * sin(w * t)
         * y = r * w * cos(w * t)
         *
         * Here, the negative sign indicates counterclockwise movement
         *
         */

        elapsedTime += dt;

        if (elapsedTime >= PERIOD_SECONDS) {
            elapsedTime = 0;
            counterclockwise = !counterclockwise;
        }

        float w = 2 * MathUtils.PI / PERIOD_SECONDS;
        tmp.set((counterclockwise ? -1 : 1) * RADIUS_METERS * w * MathUtils.sin(w * elapsedTime), RADIUS_METERS * w * MathUtils.cos(w * elapsedTime));
        return tmp;
    }

    @Override
    protected String getClassName() {
        return this.getClass().getName();
    }

    @Override
    protected TextureRegion getHelpImage() {
        return Assets.getInstance().getScene2d().getHelpEnemyFive();
    }

    @Override
    public void onHit(Weapon weapon) {
        if (!damage) {
            weapon.onBounce();
            counterclockwise = !counterclockwise;
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
        currentState = State.INJURED;
        knockBack = true;
    }

    @Override
    public void onBump() {
        // Nothing to do here
    }
}
