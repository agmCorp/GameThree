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
import uy.com.agm.gamethree.assets.sprites.AssetEnemyFive;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionI;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 12/9/2017.
 */

public class EnemyFive extends Enemy {
    private static final String TAG = EnemyFive.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    private static final float CIRCLE_SHAPE_RADIUS_METERS = 29.0f / PlayScreen.PPM;
    private static final float MAX_PERIOD_SECONDS = 2.0f;
    private static final float MIN_PERIOD_SECONDS = 1.5f;
    private static final float MAX_RADIUS_METERS = 1.0f;
    private static final float MIN_RADIUS_METERS = 0.7f;
    private static final float FIRE_DELAY_SECONDS = 3.0f;
    private static final float SPEAK_TIME_SECONDS = 3.0f;
    private static final int SCORE = 15;

    private boolean damage;
    private float radius;
    private float period;
    private float stateTime;
    private boolean counterclockwise;
    private float elapsedTime;
    private float initialY;

    private Animation enemyFiveAnimation;
    private Animation enemyFiveWeakAnimation;
    private Animation explosionAnimation;

    public EnemyFive(PlayScreen screen, MapObject object) {
        super(screen, object);

        // Animations
        enemyFiveAnimation = Assets.getInstance().getEnemyFive().getEnemyFiveAnimation();
        enemyFiveWeakAnimation = Assets.getInstance().getEnemyFive().getEnemyFiveWeakAnimation();
        explosionAnimation = Assets.getInstance().getExplosionI().getExplosionIAnimation();

        // Determines the size of the EnemyFive's drawing on the screen
        setBounds(getX(), getY(), AssetEnemyFive.WIDTH_METERS, AssetEnemyFive.HEIGHT_METERS);

        // Variables initialization
        damage = false;
        radius = MathUtils.random(MIN_RADIUS_METERS, MAX_RADIUS_METERS);
        period = MathUtils.random(MIN_PERIOD_SECONDS, MAX_PERIOD_SECONDS);
        stateTime = MathUtils.random(0, enemyFiveAnimation.getAnimationDuration()); // To flap untimely with others
        counterclockwise = MathUtils.randomBoolean();
        elapsedTime = 0;
        initialY = getY();
        velocity.set(0.0f, 0.0f); // Initially at rest
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
        TextureRegion region = (TextureRegion) enemyFiveAnimation.getKeyFrame(stateTime, true);
        stateTime += dt;
        return region;
    }

    @Override
    protected void stateAlive(float dt) {
        // Set new velocity (see getNewTangentialSpeed(...))
        b2body.setLinearVelocity(velocity);

        /* Update this Sprite to correspond with the position of the Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, EnemyFive may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        // Enemy's animation
        TextureRegion region = !damage ? (TextureRegion) enemyFiveAnimation.getKeyFrame(stateTime, true) :
                (TextureRegion) enemyFiveWeakAnimation.getKeyFrame(stateTime, true);

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
        // Release an item
        getItemOnHit();

        // Explosion animation
        stateTime = 0;

        // Audio FX
        pum(Assets.getInstance().getSounds().getJuicy());

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
                setBounds(getX() + getWidth() / 2 - AssetExplosionI.WIDTH_METERS * expScale / 2, getY() + getHeight() / 2 - AssetExplosionI.HEIGHT_METERS * expScale / 2,
                        AssetExplosionI.WIDTH_METERS * expScale, AssetExplosionI.HEIGHT_METERS * expScale);
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

        if (elapsedTime >= period) {
            elapsedTime = 0;
            counterclockwise = !counterclockwise;
        }

        float w = 2 * MathUtils.PI / period;
        tmp.set((counterclockwise ? -1 : 1) * radius * w * MathUtils.sin(w * elapsedTime), -radius * w * MathUtils.cos(w * elapsedTime));
        elapsedTime += dt;
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
    protected boolean isSpriteOutsideBottomEdge(float bottomEdge) {
        return  bottomEdge > getY() + getHeight() && // Sprite beyond bottom edge
                velocity.y < 0 && // Going down
                bottomEdge - initialY > radius; // Distance between bottomEdge and initialY > radius
    }

    @Override
    protected boolean isSpriteOutsideUpperEdge(float upperEdge) {
        return false; // We don't want to kill this Enemy if it's beyond the upper edge.
    }

    @Override
    protected Sound getVoice() {
        return Assets.getInstance().getSounds().getDragon();
    }

    @Override
    protected float getSpeakTimeSeconds() {
        return SPEAK_TIME_SECONDS;
    }

    @Override
    public void onHit(Weapon weapon) {
        if (!damage) {
            weapon.onTarget();
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
        currentState = State.KNOCK_BACK;
    }

    @Override
    public void onBump() {
        counterclockwise = !counterclockwise;
    }

    @Override
    public void onDestroy() {
        onHit();
    }
}
