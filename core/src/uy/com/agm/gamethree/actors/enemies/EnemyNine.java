package uy.com.agm.gamethree.actors.enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import uy.com.agm.gamethree.actors.weapons.IShootStrategy;
import uy.com.agm.gamethree.actors.weapons.Weapon;
import uy.com.agm.gamethree.actors.weapons.enemy.EnemyDefaultShooting;
import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyNine;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionH;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.Vector2Util;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 12/9/2017.
 */

public class EnemyNine extends Enemy {
    private static final String TAG = EnemyNine.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    private static final float CIRCLE_SHAPE_RADIUS_METERS = 29.0f / PlayScreen.PPM;
    private static final float PERIOD_SECONDS = 1.3f;
    private static final float RADIUS_METERS = 1.0f;
    private static final float RAID_LINEAR_VELOCITY = 8.0f;
    private static final float DENSITY = 1000.0f;
    private static final float FIRE_DELAY_SECONDS = 2.0f;
    private static final float SPEAK_TIME_SECONDS = 2.5f;
    private static final int SCORE = 15;

    private float stateTime;
    private Animation enemyNineAnimation;
    private Animation enemyNineRaidAnimation;
    private Animation explosionAnimation;

    private boolean raid;
    private boolean goingLeft;
    private boolean damage;
    private float elapsedTime;

    public EnemyNine(PlayScreen screen, MapObject object) {
        super(screen, object);

        // Determines the size of the EnemyNine's drawing on the screen
        setBounds(getX(), getY(), AssetEnemyNine.WIDTH_METERS, AssetEnemyNine.HEIGHT_METERS);

        // Animations
        enemyNineAnimation = Assets.getInstance().getEnemyNine().getEnemyNineAnimation();
        enemyNineRaidAnimation = Assets.getInstance().getEnemyNine().getEnemyNineRaidAnimation();
        explosionAnimation = Assets.getInstance().getExplosionH().getExplosionHAnimation();

        // Variables initialization
        raid = false;
        goingLeft = MathUtils.randomBoolean();
        damage = false;
        stateTime = MathUtils.random(0, enemyNineAnimation.getAnimationDuration()); // To flap untimely with others
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
        TextureRegion region = (TextureRegion) enemyNineRaidAnimation.getKeyFrame(stateTime, true);
        stateTime += dt;
        return region;
    }

    @Override
    protected void stateAlive(float dt) {
        // Set new velocity (see checkPath(dt))
        b2body.setLinearVelocity(velocity);

        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, EnemyNine may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        TextureRegion region;
        if (raid) {
            region = (TextureRegion) enemyNineRaidAnimation.getKeyFrame(stateTime, true);
        } else {
            region = (TextureRegion) enemyNineAnimation.getKeyFrame(stateTime, true);
            if (b2body.getLinearVelocity().x > 0 && !region.isFlipX()) {
                region.flip(true, false);
            }
            if (b2body.getLinearVelocity().x < 0 && region.isFlipX()) {
                region.flip(true, false);
            }

            // Set new tangential speed (derivative d/dt of the parametric equation of a Circle)
            elapsedTime += dt;
            float w = 2 * MathUtils.PI / PERIOD_SECONDS;
            velocity.set(goingLeft ? -1 : 1, -RADIUS_METERS * w * MathUtils.cos(w * elapsedTime));

            // Change direction
            if (b2body.getPosition().x - CIRCLE_SHAPE_RADIUS_METERS <= 0 && goingLeft ||
                    b2body.getPosition().x + CIRCLE_SHAPE_RADIUS_METERS >= screen.getGameViewPort().getWorldWidth() && !goingLeft) {
                goingLeft = !goingLeft;
            }
        }

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
                setBounds(getX() + getWidth() / 2 - AssetExplosionH.WIDTH_METERS * expScale / 2, getY() + getHeight() / 2 - AssetExplosionH.HEIGHT_METERS * expScale / 2,
                        AssetExplosionH.WIDTH_METERS * expScale, AssetExplosionH.HEIGHT_METERS * expScale);
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
        return Assets.getInstance().getScene2d().getHelpEnemyNine();
    }

    @Override
    protected Sound getVoice() {
        return Assets.getInstance().getSounds().getWhistle();
    }

    @Override
    protected float getSpeakTimeSeconds() {
        return SPEAK_TIME_SECONDS;
    }

    @Override
    public void onHit(Weapon weapon) {
        if (raid) {
            if (!damage) {
                weapon.onBounce();
                damage = true;
            } else {
                super.onHit(weapon);
            }
        } else {
            weapon.onTarget();
            raid = true;
            stateTime = 0;

            // Raid
            Vector2 heroPosition = screen.getCreator().getHero().getB2body().getPosition();
            velocity.set(b2body.getPosition().x, b2body.getPosition().y);
            Vector2Util.goToTarget(velocity, heroPosition.x, heroPosition.y, RAID_LINEAR_VELOCITY);

            // Audio FX
            AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getChirp());
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
