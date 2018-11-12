package uy.com.agm.gamethree.actors.bosses;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import uy.com.agm.gamethree.actors.weapons.IShootStrategy;
import uy.com.agm.gamethree.actors.weapons.Weapon;
import uy.com.agm.gamethree.actors.weapons.enemy.EnemyRocketShooting;
import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.sprites.AssetBossOne;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionE;
import uy.com.agm.gamethree.assets.sprites.AssetSplat;
import uy.com.agm.gamethree.game.DebugConstants;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.Vector2Util;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 12/30/2017.
 */

public class BossOne extends Boss {
    private static final String TAG = BossOne.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    private static final String NAME = "ASTROBITSY";
    public static final float CIRCLE_SHAPE_RADIUS_METERS = 60.0f / PlayScreen.PPM;
    private static final float TARGET_RADIUS_METERS = 30.0f / PlayScreen.PPM;
    private static final float OFFSET_TARGET = 25.0f / PlayScreen.PPM;
    private static final float WORLD_WIDTH = PlayScreen.V_WIDTH / PlayScreen.PPM;
    private static final float WORLD_HEIGHT = PlayScreen.V_HEIGHT / PlayScreen.PPM;
    private static final float X_MIN = TARGET_RADIUS_METERS + OFFSET_TARGET;
    private static final float X_MAX = WORLD_WIDTH - TARGET_RADIUS_METERS - OFFSET_TARGET;
    private static final float Y_MIN = WORLD_HEIGHT * (PlayScreen.WORLD_SCREENS - 1) + TARGET_RADIUS_METERS + OFFSET_TARGET;
    private static final float Y_MAX = WORLD_HEIGHT * PlayScreen.WORLD_SCREENS - TARGET_RADIUS_METERS - OFFSET_TARGET;
    private static final float LINEAR_VELOCITY = 4.5f;
    private static final float DENSITY = 1000.0f;
    private static final int MAX_DAMAGE = 8 * (DebugConstants.DESTROY_BOSSES_ONE_HIT ? 0 : 1);
    private static final float EXPLOSION_SHAKE_DURATION = 2.0f;
    private static final float HIT_SHAKE_DURATION = 1.0f;
    private static final float CHANGE_STATE_MIN_TIME_SECONDS = 2.0f;
    private static final float CHANGE_STATE_MAX_TIME_SECONDS = 4.0f;
    private static final float IDLE_STATE_TIME_SECONDS = 5.0f;
    private static final float FIRE_DELAY_SECONDS = 0.7f;
    private static final int SCORE = 500;

    private int damage;
    private float stateBossTime;
    private float changeTime;
    private float timeToChange;

    private Animation bossOneWalkAnimation;
    private Animation bossOneIdleAnimation;
    private Animation bossOneShootAnimation;
    private Animation bossOneDeathAnimation;

    // Circle on the screen where BossTwo must go
    private Circle target;
    private Circle previousTarget;
    private Circle tmpCircle; // Temporary GC friendly circle

    // Power FX
    private Animation powerFXAnimation;
    private float powerFXStateTime;
    private Sprite powerFXSprite;

    // Explosion FX
    private Animation explosionFXAnimation;
    private float explosionFXStateTime;
    private Sprite explosionFXSprite;

    // Splat FX
    private Sprite splatFXSprite;

    public BossOne(PlayScreen screen, float x, float y) {
        super(screen, x, y, AssetBossOne.WIDTH_METERS, AssetBossOne.HEIGHT_METERS);

        // Animations
        bossOneWalkAnimation = Assets.getInstance().getBossOne().getBossOneWalkAnimation();
        bossOneIdleAnimation = Assets.getInstance().getBossOne().getBossOneIdleAnimation();
        bossOneShootAnimation = Assets.getInstance().getBossOne().getBossOneShootAnimation();
        bossOneDeathAnimation = Assets.getInstance().getBossOne().getBossOneDeathAnimation();

        // BossOne variables initialization
        damage = MathUtils.ceil(MAX_DAMAGE * screen.getCreator().getDifficultyProb());
        stateBossTime = 0;
        changeTime = 0;
        timeToChange = getNextTimeToChange();

        // Place origin of rotation in the center of the Sprite
        setOriginCenter();

        // Initialize target and previousTarget
        target = new Circle(b2body.getPosition().x, b2body.getPosition().y, TARGET_RADIUS_METERS);
        previousTarget = new Circle(0, 0, TARGET_RADIUS_METERS);

        // Temporary GC friendly circle
        tmpCircle = new Circle();

        // Move to a new target at constant speed
        getNewTarget();
        velocity.set(getSpeedTarget());

        // -------------------- PowerFX --------------------

        // PowerFX variables initialization
        powerFXAnimation = Assets.getInstance().getBossOne().getBossOnePowerAnimation();
        powerFXStateTime = 0;

        // Set the power's texture
        powerFXSprite = new Sprite(Assets.getInstance().getBossOne().getBossOnePowerStand());

        // Only to set width and height of spritePower (in powerStatePowerful(...) we set its position)
        powerFXSprite.setBounds(getX(), getY(), AssetBossOne.POWER_WIDTH_METERS, AssetBossOne.POWER_HEIGHT_METERS);

        // Place origin of rotation in the center of the Sprite
        powerFXSprite.setOriginCenter();

        // -------------------- ExplosionFX --------------------

        // ExplosionFX variables initialization
        explosionFXAnimation = Assets.getInstance().getExplosionE().getExplosionEAnimation();
        explosionFXStateTime = 0;

        // Set the explosion's texture
        Sprite spriteExplosion = new Sprite(Assets.getInstance().getExplosionE().getExplosionEStand());

        // Only to set width and height of spriteExplosion (in stateExploding(...) we set its position)
        spriteExplosion.setBounds(getX(), getY(), AssetExplosionE.WIDTH_METERS, AssetExplosionE.HEIGHT_METERS);

        // Explosion FX Sprite
        explosionFXSprite = new Sprite(spriteExplosion);

        // Place origin of rotation in the center of the Sprite
        explosionFXSprite.setOriginCenter();

        // -------------------- SplatFX --------------------

        // Set the splat's texture
        Sprite spriteSplat = new Sprite(Assets.getInstance().getSplat().getRandomBossSplat());

        // Only to set width and height of spriteSplat (in stateDead(...) we set its position)
        spriteSplat.setBounds(getX(), getY(), AssetSplat.BOSS_SPLAT_WIDTH_METERS, AssetSplat.BOSS_SPLAT_HEIGHT_METERS);

        // Splat FX Sprite
        splatFXSprite = new Sprite(spriteSplat);

        // Place origin of rotation in the center of the Sprite
        splatFXSprite.setOriginCenter();
    }

    @Override
    protected void defineBoss() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX() + getWidth() / 2 , getY() + getHeight() / 2); // In b2box the origin is at the center of the body
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        b2body.setFixedRotation(true);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(CIRCLE_SHAPE_RADIUS_METERS);
        fdef.shape = shape;
        fdef.density = DENSITY; // Hard to push
        b2body.createFixture(fdef).setUserData(this);
        setDefaultFilter();
    }

    private void setDefaultFilter() {
        Filter filter = new Filter();
        filter.categoryBits = WorldContactListener.BOSS_BIT; // Depicts what this fixture is
        filter.maskBits = WorldContactListener.BORDER_BIT |
                WorldContactListener.EDGE_BIT |
                WorldContactListener.OBSTACLE_BIT |
                WorldContactListener.HERO_WEAPON_BIT |
                WorldContactListener.HERO_SHIELD_BIT |
                WorldContactListener.HERO_TOUGH_BIT |
                WorldContactListener.HERO_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
        for (Fixture fixture : b2body.getFixtureList()) {
            fixture.setFilterData(filter);
        }
    }

    private float getNextTimeToChange() {
        return MathUtils.random(CHANGE_STATE_MIN_TIME_SECONDS, CHANGE_STATE_MAX_TIME_SECONDS);
    }

    private StateBoss getNewRandomState(float dt) {
        boolean blnOption;
        StateBoss newRandomStateBoss = currentStateBoss;
        float limit = screen.getGameCam().position.y - screen.getGameViewPort().getWorldHeight() / 8; // Arbitrary

        // Set a new currentStateBoss
        changeTime += dt;
        if (changeTime >= timeToChange) {
            // Reset random state variables
            changeTime = 0;

            // Reset variable animation
            stateBossTime = 0;

            // Random option
            blnOption = MathUtils.randomBoolean();

            // Decide which state must return
            switch (currentStateBoss) {
                case WALKING:
                    if (blnOption) {
                        if (b2body.getPosition().y >= limit) { // IDLE state is only allowed above this position
                            newRandomStateBoss = StateBoss.IDLE;
                            timeToChange = IDLE_STATE_TIME_SECONDS;
                        } else {
                            // Continues with the same state
                            timeToChange = getNextTimeToChange();
                        }
                    } else {
                        newRandomStateBoss = StateBoss.SHOOTING;
                        timeToChange = getNextTimeToChange();
                    }
                    break;
                case IDLE:
                    if (blnOption) {
                        newRandomStateBoss = StateBoss.WALKING;
                    } else {
                        newRandomStateBoss = StateBoss.SHOOTING;
                    }
                    timeToChange = getNextTimeToChange();
                    break;
                case SHOOTING:
                    if (blnOption) {
                        newRandomStateBoss = StateBoss.WALKING;
                        timeToChange = getNextTimeToChange();
                    } else {
                        if (b2body.getPosition().y >= limit) { // IDLE state is only allowed above this position
                            newRandomStateBoss = StateBoss.IDLE;
                            timeToChange = IDLE_STATE_TIME_SECONDS;
                        } else {
                            // Continues with the same state
                            timeToChange = getNextTimeToChange();
                        }
                    }
                    break;
            }
        }
        return newRandomStateBoss;
    }

    @Override
    protected IShootStrategy getShootStrategy() {
        return new EnemyRocketShooting(screen, 0, FIRE_DELAY_SECONDS);
    }

    @Override
    protected float getCircleShapeRadiusMeters() {
        return CIRCLE_SHAPE_RADIUS_METERS;
    }

    @Override
    protected TextureRegion getKnockBackFrame(float dt) {
        TextureRegion region = (TextureRegion) bossOneIdleAnimation.getKeyFrame(stateBossTime, true);
        stateBossTime += dt;
        return region;
    }

    @Override
    public void updateLogic(float dt) {
        switch (currentStateBoss) {
            case WALKING:
                stateWalking(dt);
                break;
            case IDLE:
                stateIdle(dt);
                break;
            case SHOOTING:
                stateShooting(dt);
                break;
            case KNOCK_BACK:
                stateKnockBack(dt);
                break;
            case INJURED:
                stateInjured(dt);
                break;
            case DYING:
                stateDying(dt);
                break;
            case EXPLODING:
                stateExploding(dt);
                screen.getShaker().shake(EXPLOSION_SHAKE_DURATION);
                break;
            case DEAD:
                stateDead();
                break;
            default:
                break;
        }

        // BossOne could have been destroyed
        if (!isDestroyed()) {
            switchPowerState(dt);
        }
    }

    private void switchPowerState(float dt) {
        switch (currentPowerState) {
            case NORMAL:
                powerStateNormalToPowerful();
                break;
            case POWERFUL:
                powerStatePowerfulToNormal(dt);
                break;
            default:
                break;
        }
    }

    private void stateWalking(float dt) {
        // Set velocity calculated to reach the target circle (see getSpeedTarget())
        b2body.setLinearVelocity(velocity);

        /* Update this Sprite to correspond with the position of the Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) bossOneWalkAnimation.getKeyFrame(stateBossTime, true));
        stateBossTime += dt;

        // Depending on the direction, we set the angle and the flip
        setRotationAngleAndFlip();

        if (reachTarget()) {
            getNewTarget();
        }

        velocity.set(getSpeedTarget());

        // New random state
        currentStateBoss = getNewRandomState(dt);
    }

    private void getNewTarget() {
        previousTarget.setPosition(target.x, target.y);

        do {
            int randomPoint = MathUtils.random(1, 4);
            switch (randomPoint) {
                case 1:
                    target.setPosition(X_MIN, Y_MAX);
                    break;
                case 2:
                    target.setPosition(X_MAX, Y_MAX);
                    break;
                case 3:
                    target.setPosition(X_MIN, Y_MIN);
                    break;
                case 4:
                    target.setPosition(X_MAX, Y_MIN);
                    break;
            }
        } while (previousTarget.x == target.x && previousTarget.y == target.y);
    }

    private Vector2 getSpeedTarget() {
        // Move to target
        tmp.set(b2body.getPosition().x, b2body.getPosition().y);
        Vector2Util.goToTarget(tmp, target.x, target.y, LINEAR_VELOCITY);
        return tmp;
    }

    private void setRotationAngleAndFlip() {
        boolean ceilingLeft = previousTarget.x == X_MAX && previousTarget.y == Y_MAX &&
                target.x == X_MIN && target.y == Y_MAX;

        boolean leftDown = previousTarget.x == X_MIN && previousTarget.y == Y_MAX &&
                target.x == X_MIN && target.y == Y_MIN;

        boolean floorRight = previousTarget.x == X_MIN && previousTarget.y == Y_MIN &&
                target.x == X_MAX && target.y == Y_MIN;

        boolean rightUp = previousTarget.x == X_MAX && previousTarget.y == Y_MIN &&
                target.x == X_MAX && target.y == Y_MAX;

        setRotation(b2body.getLinearVelocity().angle());
        setFlip(true, true);
        if (ceilingLeft || leftDown || floorRight || rightUp) {
            setFlip(true, false);
        }
    }

    private boolean reachTarget() {
        tmpCircle.set(b2body.getPosition().x, b2body.getPosition().y, CIRCLE_SHAPE_RADIUS_METERS);
        return tmpCircle.contains(target);
    }

    private void stateIdle(float dt) {
        // Stop
        b2body.setLinearVelocity(0.0f, 0.0f);

        // Preserve the flip and rotation state
        boolean isFlipX = isFlipX();
        boolean isFlipY = isFlipY();
        float rotation = getRotation();

        /* Update this Sprite to correspond with the position of the Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, BossOne may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) bossOneIdleAnimation.getKeyFrame(stateBossTime, true));
        stateBossTime += dt;

        // Apply previous flip and rotation state
        setFlip(isFlipX, isFlipY);
        setRotation(rotation);

        // New random state
        currentStateBoss = getNewRandomState(dt);
    }

    private void stateShooting(float dt) {
        // Stop
        b2body.setLinearVelocity(0.0f, 0.0f);

        /* Update this Sprite to correspond with the position of the Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, BossOne may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) bossOneShootAnimation.getKeyFrame(stateBossTime, true));
        stateBossTime += dt;

        // Calculate shooting angle
        Vector2 heroPosition = screen.getCreator().getHero().getB2body().getPosition();
        float angle = tmp.set(heroPosition.x, heroPosition.y)
                .sub(b2body.getPosition().x, b2body.getPosition().y).angle();
        setRotation(angle);
        setFlip(true, true);

        // Shoot time!
        openFire(dt);

        // New random state
        currentStateBoss = getNewRandomState(dt);
    }

    private void stateInjured(float dt) {
        // Stop level music
        AudioManager.getInstance().stopMusic();

        // Death animation
        stateBossTime = 0;

        // Audio FX
        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getBossExplosion());

        // Set score
        screen.getHud().addScore(SCORE);

        // Destroy box2D body
        if (!world.isLocked()) {
            world.destroyBody(b2body);
        }

        // Set the new state
        currentStateBoss = StateBoss.DYING;
    }

    private void stateDying(float dt) {
        if (bossOneDeathAnimation.isAnimationFinished(stateBossTime)) {
            // Exploding animation
            explosionFXStateTime = 0;

            // Audio FX
            AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getBossExplosion());

            // Set the new state
            currentStateBoss = StateBoss.EXPLODING;
        } else {
            // Preserve the flip and rotation state
            boolean isFlipX = isFlipX();
            boolean isFlipY = isFlipY();
            float rotation = getRotation();

            setRegion((TextureRegion) bossOneDeathAnimation.getKeyFrame(stateBossTime));
            stateBossTime += dt;

            // Apply previous flip and rotation state
            setFlip(isFlipX, isFlipY);
            setRotation(rotation);
        }
    }

   private void stateExploding(float dt) {
       if (explosionFXAnimation.isAnimationFinished(explosionFXStateTime)) {
           // Bug 30/09/2018 reported by Constantino Thamnopoulos (game over and boss death at the same second)
           if (!screen.getCreator().getHero().isGameOverState()) {
               // Show victory animation and play audio FX
               victoryFX();
           }

           // Set the new state
           currentStateBoss = StateBoss.DEAD;
       } else {
           // Animation
           explosionFXSprite.setRegion((TextureRegion) explosionFXAnimation.getKeyFrame(explosionFXStateTime, true));
           explosionFXStateTime += dt;

           // Apply rotation and flip of the main character
           explosionFXSprite.setRotation(getRotation());
           explosionFXSprite.setFlip(isFlipX(), isFlipY());

           // Get center of its bounding rectangle
           getBoundingRectangle().getCenter(tmp);

           // Center the Sprite in tmp
           explosionFXSprite.setPosition(tmp.x - explosionFXSprite.getWidth() / 2, tmp.y - explosionFXSprite.getHeight() / 2);
       }
    }

    private void stateDead() {
        // Apply rotation and flip of the explosion
        splatFXSprite.setRotation(explosionFXSprite.getRotation());
        splatFXSprite.setFlip(explosionFXSprite.isFlipX(), explosionFXSprite.isFlipY());

        // Get center of the bounding rectangle of the explosion
        explosionFXSprite.getBoundingRectangle().getCenter(tmp);

        // Center the Sprite in tmp
        splatFXSprite.setPosition(tmp.x - splatFXSprite.getWidth() / 2, tmp.y - splatFXSprite.getHeight() / 2);
    }

    private void powerStatePowerfulToNormal(float dt) {
        // If the boss is not walking nor shooting, he becomes weak
        if (currentStateBoss != StateBoss.WALKING && currentStateBoss != StateBoss.SHOOTING) {
            powerFXStateTime = 0;
            currentPowerState = PowerState.NORMAL;
            AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getBossPowerDown());
        } else {
            // Animation
            powerFXSprite.setRegion((TextureRegion) powerFXAnimation.getKeyFrame(powerFXStateTime, true));
            powerFXStateTime += dt;

            // Apply rotation and flip of the main character
            powerFXSprite.setRotation(getRotation());
            powerFXSprite.setFlip(isFlipX(), isFlipY());

            // Update this Sprite to correspond with the position of the bossOne's Box2D body
            powerFXSprite.setPosition(b2body.getPosition().x - powerFXSprite.getWidth() / 2, b2body.getPosition().y - powerFXSprite.getHeight() / 2);
        }
    }

    private void powerStateNormalToPowerful() {
        // If the boss is walking or shooting, he becomes powerful
        if (currentStateBoss == StateBoss.WALKING || currentStateBoss == StateBoss.SHOOTING) {
            powerFXStateTime = 0;
            currentPowerState = PowerState.POWERFUL;
            AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getBossPowerUp());
        }
    }

    @Override
    public void onHit(Weapon weapon) {
        if (screen.getCreator().getHero().isSilverBulletEnabled()) {
            if (currentStateBoss == StateBoss.IDLE) {
                weapon.onTarget();
                damage--;
                screen.getHud().decreaseHealth();
                AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getBossHit(), Boss.HIT_MAX_VOLUME);
                screen.getShaker().shake(HIT_SHAKE_DURATION);
                if (damage <= 0) {
                    screen.getHud().hideHealthBarInfo();
                    currentStateBoss = StateBoss.KNOCK_BACK;
                }
            } else {
                weapon.onBounce(b2body.getLinearVelocity());
            }
        } else {
            weapon.onBounce(b2body.getLinearVelocity());
        }
    }

    @Override
    protected String getBossName() {
        return NAME;
    }

    @Override
    protected int getBossDamage() {
        return damage;
    }

    @Override
    protected void setInitialState() {
        currentStateBoss = StateBoss.WALKING;
    }

    @Override
    protected TextureRegion getHelpImage() {
        return Assets.getInstance().getScene2d().getHelpBossOne();
    }

    private boolean isDrawable() {
        return currentStateBoss != StateBoss.INACTIVE &&
                currentStateBoss != StateBoss.EXPLODING &&
                currentStateBoss != StateBoss.DEAD;
    }

    private void drawPowers(Batch batch) {
        if (currentPowerState == PowerState.POWERFUL) {
            powerFXSprite.draw(batch);
        }
    }

    private void drawFxs(Batch batch) {
        switch (currentStateBoss) {
            case EXPLODING:
                explosionFXSprite.draw(batch);
                break;
            case DEAD:
                splatFXSprite.draw(batch);
                break;
        }
    }

    @Override
    public void draw(Batch batch) {
        // We draw BossOne in these states: WALKING IDLE SHOOTING INJURED DYING
        if (isDrawable()) {
            drawPowers(batch);
            super.draw(batch);
        } else {
            drawFxs(batch);
        }
    }

    @Override
    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(previousTarget.x, previousTarget.y, previousTarget.radius);
        shapeRenderer.circle(target.x, target.y, target.radius);
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
    }
}
