package uy.com.agm.gamethree.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.sprites.AssetEnemySix;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionG;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.weapons.IShootStrategy;
import uy.com.agm.gamethree.sprites.weapons.enemy.EnemyDefaultShooting;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 12/9/2017.
 */

public class EnemySix extends Enemy {
    private static final String TAG = EnemySix.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    private static final float CIRCLE_SHAPE_RADIUS_METERS = 29.0f / PlayScreen.PPM;
    private static final float VELOCITY_X = 0.0f;
    private static final float VELOCITY_Y = 0.0f;
    private static final float FIRE_DELAY_SECONDS = 3.0f;
    private static final float BEAM_INTERVAL_SECONDS = 4.0f;
    private static final int SCORE = 5;
    private static final float POLYGON_SHAPE_HEIGHT_METERS = 20.0f * 1.0f / PlayScreen.PPM;

    private float stateTime;
    private Animation enemySixIdleAnimation;
    private Animation enemySixBeamAnimation;
    private Animation explosionAnimation;

    // Power beam
    private float beamStateTime;
    private boolean beaming;
    private float beamIntervalTime;
    private Animation powerBeamAnimation;
    private Sprite beamSprite;
    private float offsetXMeters;

    public EnemySix(PlayScreen screen, MapObject object) {
        super(screen, object);

        // Main character variables initialization
        enemySixIdleAnimation = Assets.getInstance().getEnemySix().getEnemySixIdleAnimation();
        enemySixBeamAnimation = Assets.getInstance().getEnemySix().getEnemySixBeamAnimation();
        explosionAnimation = Assets.getInstance().getExplosionG().getExplosionGAnimation();
        stateTime = 0;

        // Power beam variables initialization
        beamStateTime = 0;
        beaming = false;
        beamIntervalTime = MathUtils.random(0, BEAM_INTERVAL_SECONDS);
        powerBeamAnimation = Assets.getInstance().getEnemySix().getPowerBeamAnimation();
        beamSprite = new Sprite(new Sprite(Assets.getInstance().getEnemySix().getPowerBeamStand()));

        // Setbounds is the one that determines the size of the EnemySix's drawing on the screen
        setBounds(getX(), getY(), AssetEnemySix.WIDTH_METERS, AssetEnemySix.HEIGHT_METERS);

        // Setbounds is the one that determines the size of the power beam's drawing on the screen
        offsetXMeters = getOffsetXMeters();
        beamSprite.setBounds(getX(), getY(), Math.abs(offsetXMeters), AssetEnemySix.BEAM_HEIGHT_METERS);

        velocity.set(VELOCITY_X, VELOCITY_Y); // At rest
    }

    private float getOffsetXMeters() {
        // Distance between the left border and EnemySix
        float distLeft = tmp.set(0, b2body.getPosition().y).dst(b2body.getPosition().x, b2body.getPosition().y);

        // Distance between the right border and EnemySix
        float distRight = tmp.set(screen.getGameViewPort().getWorldWidth(), b2body.getPosition().y).dst(b2body.getPosition().x, b2body.getPosition().y);

        // Max distance (left negative)
        return distRight > distLeft ? distRight : -distLeft;
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY()); // In b2box the origin is at the center of the body
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        setDefaultFixtureFilter();
    }

    @Override
    protected IShootStrategy getShootStrategy() {
        return new EnemyDefaultShooting(screen, MathUtils.random(0, FIRE_DELAY_SECONDS), FIRE_DELAY_SECONDS);
    }

    @Override
    protected void stateAlive(float dt) {
        // Set velocity (at rest) because It could have been changed a little due to a collision
        b2body.setLinearVelocity(velocity);

        // Update EnemySix to correspond with the position of its Box2D body.
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        if (beaming) {
            setRegion((TextureRegion) enemySixBeamAnimation.getKeyFrame(stateTime, true));
            stateTime += dt;
            beamToNormal(dt);
        } else {
            setRegion((TextureRegion) enemySixIdleAnimation.getKeyFrame(stateTime, true));
            stateTime += dt;
            normalToBeam(dt);
        }

        setFlip(offsetXMeters >= 0, false);

        // Shoot time!
        super.openFire(dt);
    }

    private void normalToBeam(float dt) {
        beamIntervalTime += dt;
        if (beamIntervalTime > BEAM_INTERVAL_SECONDS) {
            // Audio FX
            AudioManager.getInstance().play(Assets.getInstance().getSounds().getBeam());

            stateTime = 0;
            beamStateTime = 0;
            beamIntervalTime = 0;
            beaming = true;
            createBeam();
        }
    }

    private void createBeam() {
        PolygonShape beam = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(offsetXMeters, POLYGON_SHAPE_HEIGHT_METERS / 2);
        vertices[1] = new Vector2(0, POLYGON_SHAPE_HEIGHT_METERS / 2);
        vertices[2] = new Vector2(offsetXMeters, -POLYGON_SHAPE_HEIGHT_METERS / 2);
        vertices[3] = new Vector2(0, -POLYGON_SHAPE_HEIGHT_METERS / 2);
        beam.set(vertices);

        // The power beam is like another Enemy
        FixtureDef fdef = new FixtureDef();
        fdef.shape = beam;
        fdef.filter.categoryBits = WorldContactListener.ENEMY_BIT;  // Depicts what this fixture is
        fdef.filter.maskBits = WorldContactListener.SHIELD_BIT |
                WorldContactListener.HERO_BIT |
                WorldContactListener.HERO_TOUGH_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
        b2body.createFixture(fdef).setUserData(this);
    }

    private void beamToNormal(float dt) {
        // Update our Sprite to correspond with the position of our Box2D body
        float offset = offsetXMeters >= 0 ? 0 : offsetXMeters;
        beamSprite.setPosition(b2body.getPosition().x + offset, b2body.getPosition().y - beamSprite.getHeight() / 2);

        beamSprite.setRegion((TextureRegion) powerBeamAnimation.getKeyFrame(beamStateTime, true));
        beamStateTime += dt;

        beamIntervalTime += dt;
        if (beamIntervalTime > BEAM_INTERVAL_SECONDS) {
            setDefaultFixtureFilter();
            beamIntervalTime = 0;
            beaming = false;
        }
    }

    private void setDefaultFixtureFilter() {
        setDefaultFixture();
        setDefaultFilter();
    }

    private void setDefaultFixture() {
        // Remove all fixtures (WA - Iterators doesn't work)
        while (b2body.getFixtureList().size > 0) {
            b2body.destroyFixture(b2body.getFixtureList().first());
        }

        // Create default fixture
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(CIRCLE_SHAPE_RADIUS_METERS);
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    private void setDefaultFilter() {
        Filter filter = new Filter();
        filter.categoryBits = WorldContactListener.ENEMY_BIT; // Depicts what this fixture is
        filter.maskBits = WorldContactListener.HERO_WEAPON_BIT |
                WorldContactListener.SHIELD_BIT |
                WorldContactListener.HERO_BIT |
                WorldContactListener.HERO_TOUGH_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
        for (Fixture fixture : b2body.getFixtureList()) {
            fixture.setFilterData(filter);
        }
    }

    @Override
    protected void stateInjured(float dt) {
        // Release an item
        getItemOnHit();

        // Destroy box2D body
        world.destroyBody(b2body);

        // Explosion animation
        stateTime = 0;

        // Audio FX
        AudioManager.getInstance().play(Assets.getInstance().getSounds().getHit());

        // Set score
        screen.getHud().addScore(SCORE);

        // Set the new state
        currentState = State.EXPLODING;
    }

    @Override
    protected void stateExploding(float dt) {
        if (explosionAnimation.isAnimationFinished(stateTime)) {
            currentState = State.DEAD;
        } else {
            if (stateTime == 0) { // Explosion starts
                // Setbounds is the one that determines the size of the explosion on the screen
                setBounds(getX() + getWidth() / 2 - AssetExplosionG.WIDTH_METERS / 2, getY() + getHeight() / 2 - AssetExplosionG.HEIGHT_METERS / 2,
                        AssetExplosionG.WIDTH_METERS, AssetExplosionG.HEIGHT_METERS);
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
        return Assets.getInstance().getScene2d().getHelpEnemySix();
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
    }

    @Override
    public void onBump() {
        // Nothing to do here
    }

    @Override
    public void draw(Batch batch) {
        if (currentState != State.DEAD) {
            if (beaming) {
                beamSprite.draw(batch);
            }
            super.draw(batch);
        }
    }
}
