package uy.com.agm.gamethree.actors.finals;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import uy.com.agm.gamethree.actors.backgroundObjects.kinematicObjects.Edge;
import uy.com.agm.gamethree.actors.weapons.IShootStrategy;
import uy.com.agm.gamethree.actors.weapons.Weapon;
import uy.com.agm.gamethree.actors.weapons.enemy.EnemyRocketShooting;
import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionE;
import uy.com.agm.gamethree.assets.sprites.AssetFinalEnemyLevelOne;
import uy.com.agm.gamethree.assets.sprites.AssetSplat;
import uy.com.agm.gamethree.game.DebugConstants;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.Vector2Util;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 12/30/2017.
 */

public class FinalEnemyLevelOne extends FinalEnemy {
    private static final String TAG = FinalEnemyLevelOne.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    private static final String NAME = "ASTROBITSY";
    public static final float CIRCLE_SHAPE_RADIUS_METERS = 60.0f / PlayScreen.PPM;
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

    private enum StateWalking {
        CEILING_LEFT, CEILING_RIGHT,
        LEFT_UP, LEFT_DOWN,
        FLOOR_LEFT, FLOOR_RIGHT,
        RIGHT_UP, RIGHT_DOWN,
        SLASH_UP, SLASH_DOWN,
        BACKSLASH_UP, BACKSLASH_DOWN
    }
    // This is a slash: /
    // This is a backslash: \
    // This is a happy guy \(^-^)/

    private StateWalking currentStateWalking;
    private int damage;
    private float stateFinalEnemyTime;
    private float changeTime;
    private float timeToChange;

    private Animation finalEnemyLevelOneWalkAnimation;
    private Animation finalEnemyLevelOneIdleAnimation;
    private Animation finalEnemyLevelOneShootAnimation;
    private Animation finalEnemyLevelOneDyingAnimation;

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

    public FinalEnemyLevelOne(PlayScreen screen, float x, float y) {
        super(screen, x, y, AssetFinalEnemyLevelOne.WIDTH_METERS, AssetFinalEnemyLevelOne.HEIGHT_METERS);

        // Animations
        finalEnemyLevelOneWalkAnimation = Assets.getInstance().getFinalEnemyLevelOne().getFinalEnemyLevelOneWalkAnimation();
        finalEnemyLevelOneIdleAnimation = Assets.getInstance().getFinalEnemyLevelOne().getFinalEnemyLevelOneIdleAnimation();
        finalEnemyLevelOneShootAnimation = Assets.getInstance().getFinalEnemyLevelOne().getFinalEnemyLevelOneShootAnimation();
        finalEnemyLevelOneDyingAnimation = Assets.getInstance().getFinalEnemyLevelOne().getFinalEnemyLevelOneDeathAnimation();

        // FinalEnemyLevelOne variables initialization
        damage = MAX_DAMAGE;
        stateFinalEnemyTime = 0;
        changeTime = 0;
        timeToChange = getNextTimeToChange();

        // Place origin of rotation in the center of the Sprite
        setOriginCenter();

        // Initial movement (left or right)
        int direction = MathUtils.randomSign();
        velocity.set(direction * LINEAR_VELOCITY, 0);
        if (direction < 0) {
            currentStateWalking = StateWalking.CEILING_LEFT;
        } else {
            currentStateWalking = StateWalking.CEILING_RIGHT;
        }

        // -------------------- PowerFX --------------------

        // PowerFX variables initialization
        powerFXAnimation = Assets.getInstance().getFinalEnemyLevelOne().getFinalEnemyLevelOnePowerAnimation();
        powerFXStateTime = 0;

        // Set the power's texture
        powerFXSprite = new Sprite(Assets.getInstance().getFinalEnemyLevelOne().getFinalEnemyLevelOnePowerStand());

        // Only to set width and height of our spritePower (in powerStatePowerful(...) we set its position)
        powerFXSprite.setBounds(getX(), getY(), AssetFinalEnemyLevelOne.POWER_WIDTH_METERS, AssetFinalEnemyLevelOne.POWER_HEIGHT_METERS);

        // Place origin of rotation in the center of the Sprite
        powerFXSprite.setOriginCenter();

        // -------------------- ExplosionFX --------------------

        // ExplosionFX variables initialization
        explosionFXAnimation = Assets.getInstance().getExplosionE().getExplosionEAnimation();
        explosionFXStateTime = 0;

        // Set the explosion's texture
        Sprite spriteExplosion = new Sprite(Assets.getInstance().getExplosionE().getExplosionEStand());

        // Only to set width and height of our spriteExplosion (in stateExploding(...) we set its position)
        spriteExplosion.setBounds(getX(), getY(), AssetExplosionE.WIDTH_METERS, AssetExplosionE.HEIGHT_METERS);

        // Explosion FX Sprite
        explosionFXSprite = new Sprite(spriteExplosion);

        // Place origin of rotation in the center of the Sprite
        explosionFXSprite.setOriginCenter();

        // -------------------- SplatFX --------------------

        // Set the splat's texture
        Sprite spriteSplat = new Sprite(Assets.getInstance().getSplat().getRandomFinalEnemySplat());

        // Only to set width and height of our spriteSplat (in stateDead(...) we set its position)
        spriteSplat.setBounds(getX(), getY(), AssetSplat.FINAL_ENEMY_SPLAT_WIDTH_METERS, AssetSplat.FINAL_ENEMY_SPLAT_HEIGHT_METERS);

        // Splat FX Sprite
        splatFXSprite = new Sprite(spriteSplat);

        // Place origin of rotation in the center of the Sprite
        splatFXSprite.setOriginCenter();
    }

    @Override
    protected void defineFinalEnemy() {
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
        filter.categoryBits = WorldContactListener.FINAL_ENEMY_BIT; // Depicts what this fixture is
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

    private StateFinalEnemy getNewRandomState(float dt) {
        boolean blnOption;
        StateFinalEnemy newRandomStateFinalEnemy = currentStateFinalEnemy;
        float limit = screen.getGameCam().position.y - screen.getGameViewPort().getWorldHeight() / 8; // Arbitrary

        // Set a new currentStateFinalEnemy
        changeTime += dt;
        if (changeTime >= timeToChange) {
            // Reset random state variables
            changeTime = 0;

            // Reset variable animation
            stateFinalEnemyTime = 0;

            // Random option
            blnOption = MathUtils.randomBoolean();

            // Decide which state must return
            switch (currentStateFinalEnemy) {
                case WALKING:
                    if (blnOption) {
                        if (b2body.getPosition().y >= limit) { // IDLE state is only allowed above this position
                            newRandomStateFinalEnemy = StateFinalEnemy.IDLE;
                            timeToChange = IDLE_STATE_TIME_SECONDS;
                        } else {
                            // Continues with the same state
                            timeToChange = getNextTimeToChange();
                        }
                    } else {
                        newRandomStateFinalEnemy = StateFinalEnemy.SHOOTING;
                        timeToChange = getNextTimeToChange();
                    }
                    break;
                case IDLE:
                    if (blnOption) {
                        newRandomStateFinalEnemy = StateFinalEnemy.WALKING;
                    } else {
                        newRandomStateFinalEnemy = StateFinalEnemy.SHOOTING;
                    }
                    timeToChange = getNextTimeToChange();
                    break;
                case SHOOTING:
                    if (blnOption) {
                        newRandomStateFinalEnemy = StateFinalEnemy.WALKING;
                        timeToChange = getNextTimeToChange();
                    } else {
                        if (b2body.getPosition().y >= limit) { // IDLE state is only allowed above this position
                            newRandomStateFinalEnemy = StateFinalEnemy.IDLE;
                            timeToChange = IDLE_STATE_TIME_SECONDS;
                        } else {
                            // Continues with the same state
                            timeToChange = getNextTimeToChange();
                        }
                    }
                    break;
            }
        }
        return newRandomStateFinalEnemy;
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
        TextureRegion region = (TextureRegion) finalEnemyLevelOneIdleAnimation.getKeyFrame(stateFinalEnemyTime, true);
        stateFinalEnemyTime += dt;
        return region;
    }

    @Override
    public void updateLogic(float dt) {
        switch (currentStateFinalEnemy) {
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

        // FinalEnemyLevelOne could have been destroyed
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
        // Set velocity because It could have been changed (see onHitWall)
        b2body.setLinearVelocity(velocity);

        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, FinalEnemyLevelOne may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) finalEnemyLevelOneWalkAnimation.getKeyFrame(stateFinalEnemyTime, true));
        stateFinalEnemyTime += dt;

        // Depending on the direction, we set the angle and the flip
        switch (currentStateWalking) {
            case CEILING_LEFT:
                setRotation(0);
                setFlip(false, true);
                velocity.set(-LINEAR_VELOCITY, 0);
                break;
            case CEILING_RIGHT:
                setRotation(0);
                setFlip(true, true);
                velocity.set(LINEAR_VELOCITY, 0);
                break;
            case LEFT_DOWN:
                setRotation(90);
                setFlip(false, true);
                velocity.set(0, -LINEAR_VELOCITY);
                break;
            case LEFT_UP:
                setRotation(90);
                setFlip(true, true);
                velocity.set(0, LINEAR_VELOCITY);
                break;
            case RIGHT_DOWN:
                setRotation(90);
                setFlip(false, false);
                velocity.set(0, -LINEAR_VELOCITY);
                break;
            case RIGHT_UP:
                setRotation(90);
                setFlip(true, false);
                velocity.set(0, LINEAR_VELOCITY);
                break;
            case FLOOR_LEFT:
                setRotation(0);
                setFlip(false, false);
                velocity.set(-LINEAR_VELOCITY, 0);
                break;
            case FLOOR_RIGHT:
                setRotation(0);
                setFlip(true, false);
                velocity.set(LINEAR_VELOCITY, 0);
                break;
            case SLASH_DOWN:
                // It's not exactly 45 degrees because we are walking along the diagonal of a rectangle
                // But it does the trick
                setRotation(45);
                setFlip(false, false);
                tmp.set(b2body.getPosition().x, b2body.getPosition().y);
                Vector2Util.goToTarget(tmp, screen.getBottomEdge().getB2body().getPosition().x - Edge.WIDTH_METERS / 2 +
                                CIRCLE_SHAPE_RADIUS_METERS / 2,
                                screen.getBottomEdge().getB2body().getPosition().y + Edge.HEIGHT_METERS / 2,
                        LINEAR_VELOCITY);
                velocity.set(tmp);
                break;
            case SLASH_UP:
                // It's not exactly 45 degrees because we are walking along the diagonal of a rectangle
                // But it does the trick
                setRotation(45);
                setFlip(true, true);
                tmp.set(b2body.getPosition().x, b2body.getPosition().y);
                Vector2Util.goToTarget(tmp, screen.getUpperEdge().getB2body().getPosition().x + Edge.WIDTH_METERS / 2 -
                                CIRCLE_SHAPE_RADIUS_METERS / 2,
                                screen.getUpperEdge().getB2body().getPosition().y - Edge.HEIGHT_METERS / 2,
                        LINEAR_VELOCITY);
                velocity.set(tmp);
                break;
            case BACKSLASH_DOWN:
                // It's not exactly 135 degrees because we are walking along the diagonal of a rectangle
                // But it does the trick
                setRotation(135);
                setFlip(false, false);
                tmp.set(b2body.getPosition().x, b2body.getPosition().y);
                Vector2Util.goToTarget(tmp, screen.getBottomEdge().getB2body().getPosition().x + Edge.WIDTH_METERS / 2 -
                                CIRCLE_SHAPE_RADIUS_METERS / 2,
                                screen.getBottomEdge().getB2body().getPosition().y + Edge.HEIGHT_METERS / 2,
                        LINEAR_VELOCITY);
                velocity.set(tmp);
                break;
            case BACKSLASH_UP:
                // It's not exactly 135 degrees because we are walking along the diagonal of a rectangle
                // But it does the trick
                setRotation(135);
                setFlip(true, true);
                tmp.set(b2body.getPosition().x, b2body.getPosition().y);
                Vector2Util.goToTarget(tmp, screen.getUpperEdge().getB2body().getPosition().x - Edge.WIDTH_METERS / 2 +
                                CIRCLE_SHAPE_RADIUS_METERS / 2,
                                screen.getUpperEdge().getB2body().getPosition().y - Edge.HEIGHT_METERS / 2,
                        LINEAR_VELOCITY);
                velocity.set(tmp);
                break;
        }

        // New random state
        currentStateFinalEnemy = getNewRandomState(dt);
    }

    private void stateIdle(float dt) {
        // Stop
        b2body.setLinearVelocity(0.0f, 0.0f);

        // Preserve the flip and rotation state
        boolean isFlipX = isFlipX();
        boolean isFlipY = isFlipY();
        float rotation = getRotation();

        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, FinalEnemyLevelOne may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) finalEnemyLevelOneIdleAnimation.getKeyFrame(stateFinalEnemyTime, true));
        stateFinalEnemyTime += dt;

        // Apply previous flip and rotation state
        setFlip(isFlipX, isFlipY);
        setRotation(rotation);

        // New random state
        currentStateFinalEnemy = getNewRandomState(dt);
    }

    private void stateShooting(float dt) {
        // Stop
        b2body.setLinearVelocity(0.0f, 0.0f);

        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, FinalEnemyLevelOne may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) finalEnemyLevelOneShootAnimation.getKeyFrame(stateFinalEnemyTime, true));
        stateFinalEnemyTime += dt;

        // Calculate shooting angle
        Vector2 heroPosition = screen.getCreator().getHero().getB2body().getPosition();
        float angle = tmp.set(heroPosition.x, heroPosition.y)
                .sub(b2body.getPosition().x, b2body.getPosition().y).angle();
        setRotation(angle);
        setFlip(true, true);

        // Shoot time!
        openFire(dt);

        // New random state
        currentStateFinalEnemy = getNewRandomState(dt);
    }

    private void stateInjured(float dt) {
        // Stop level music
        AudioManager.getInstance().stopMusic();

        // Death animation
        stateFinalEnemyTime = 0;

        // Audio FX
        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getFinalEnemyExplosion());

        // Set score
        screen.getHud().addScore(SCORE);

        // Destroy box2D body
        if (!world.isLocked()) {
            world.destroyBody(b2body);
        }

        // Set the new state
        currentStateFinalEnemy = StateFinalEnemy.DYING;
    }

    private void stateDying(float dt) {
        if (finalEnemyLevelOneDyingAnimation.isAnimationFinished(stateFinalEnemyTime)) {
            // Exploding animation
            explosionFXStateTime = 0;

            // Audio FX
            AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getFinalEnemyExplosion());

            // Set the new state
            currentStateFinalEnemy = StateFinalEnemy.EXPLODING;
        } else {
            // Preserve the flip and rotation state
            boolean isFlipX = isFlipX();
            boolean isFlipY = isFlipY();
            float rotation = getRotation();

            setRegion((TextureRegion) finalEnemyLevelOneDyingAnimation.getKeyFrame(stateFinalEnemyTime));
            stateFinalEnemyTime += dt;

            // Apply previous flip and rotation state
            setFlip(isFlipX, isFlipY);
            setRotation(rotation);
        }
    }

   private void stateExploding(float dt) {
       if (explosionFXAnimation.isAnimationFinished(explosionFXStateTime)) {
           // Show victory animation and play audio FX
           victoryFX();

           // Set the new state
           currentStateFinalEnemy = StateFinalEnemy.DEAD;
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
        // If our final enemy is not walking nor shooting, he becomes weak
        if (currentStateFinalEnemy != StateFinalEnemy.WALKING && currentStateFinalEnemy != StateFinalEnemy.SHOOTING) {
            powerFXStateTime = 0;
            currentPowerState = PowerState.NORMAL;
            AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getFinalEnemyPowerDown());
        } else {
            // Animation
            powerFXSprite.setRegion((TextureRegion) powerFXAnimation.getKeyFrame(powerFXStateTime, true));
            powerFXStateTime += dt;

            // Apply rotation and flip of the main character
            powerFXSprite.setRotation(getRotation());
            powerFXSprite.setFlip(isFlipX(), isFlipY());

            // Update our Sprite to correspond with the position of our finalEnemyLevelOne's Box2D body
            powerFXSprite.setPosition(b2body.getPosition().x - powerFXSprite.getWidth() / 2, b2body.getPosition().y - powerFXSprite.getHeight() / 2);
        }
    }

    private void powerStateNormalToPowerful() {
        // If our final enemy is walking or shooting, he becomes powerful
        if (currentStateFinalEnemy == StateFinalEnemy.WALKING || currentStateFinalEnemy == StateFinalEnemy.SHOOTING) {
            powerFXStateTime = 0;
            currentPowerState = PowerState.POWERFUL;
            AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getFinalEnemyPowerUp());
        }
    }

    @Override
    public void onHit(Weapon weapon) {
        if (screen.getCreator().getHero().isSilverBulletEnabled()) {
            if (currentStateFinalEnemy == StateFinalEnemy.IDLE) {
                weapon.onTarget();
                damage--;
                screen.getHud().decreaseHealth();
                AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getFinalEnemyHit(), FinalEnemy.HIT_MAX_VOLUME);
                screen.getShaker().shake(HIT_SHAKE_DURATION);
                if (damage <= 0) {
                    screen.getHud().hideHealthBarInfo();
                    currentStateFinalEnemy = StateFinalEnemy.KNOCK_BACK;
                }
            } else {
                weapon.onBounce();
            }
        } else {
            weapon.onBounce();
        }
    }

    @Override
    public void onHitWall(boolean isBorder) {
        /* Please don't use currentStateWalking to decide which one is the new currentStateWalking.
        * That logic is a bit cheating because onHitWall can execute anytime.
        * See this example below:
        * Suppose currentStateWalking = SLASH_UP and FinalEnemyLevelOne collides against the upper edge, but immediately
        * (because the circle shape radius is too big) against the right border.
        * 1) After onHitWall(false), for instance, currentStateWalking = CEILING_LEFT.
        * 2) But after onHitWall(true) executes, for instance, currentStateWalking = BACKSLASH_DOWN.
         * That is incorrect because FinalEnemyLevelOne is still on the right-upper corner and it gets stuck!
        */
        float velY = b2body.getLinearVelocity().y;
        float velX = b2body.getLinearVelocity().x;
        float x = b2body.getPosition().x;
        float y = b2body.getPosition().y;
        int option = MathUtils.random(1, 3);

        if (velY > 0.0f) {
            velYGT0(x, isBorder, option);
        } else { // velY <= 0.0f
            if (velY < 0.0f) {
                velYLT0(x, isBorder, option);
            } else { // velY == 0
                if (velX != 0.0f) {
                    if (velX > 0.0f) {
                        velY0VelXGT0(y, isBorder, option);
                    } else { // velX < 0
                        velY0VelXLT0(y, isBorder, option);
                    }
                } // velY == 0, velX == 0
            }
        }
    }

    private void velYGT0(float x, boolean isBorder, int option) {
        if (x < screen.getGameCam().position.x) {
            // We are walking UP along the LEFT BORDER or along the BACKSLASH
            if (!isBorder) { // We must collide only with an edge
                switch (option) {
                    case 1: // go back
                        currentStateWalking = StateWalking.LEFT_DOWN;
                        break;
                    case 2:
                        currentStateWalking = StateWalking.CEILING_RIGHT;
                        break;
                    case 3:
                        currentStateWalking = StateWalking.BACKSLASH_DOWN;
                        break;
                }
            }
        } else {
            // We are walking UP along the RIGHT BORDER or along the SLASH
            if (!isBorder) { // We must collide only with an edge
                switch (option) {
                    case 1: // go back
                        currentStateWalking = StateWalking.RIGHT_DOWN;
                        break;
                    case 2:
                        currentStateWalking = StateWalking.CEILING_LEFT;
                        break;
                    case 3:
                        currentStateWalking = StateWalking.SLASH_DOWN;
                        break;
                }
            }
        }
    }

    private void velYLT0(float x, boolean isBorder, int option) {
        if (x < screen.getGameCam().position.x) {
            // We are walking DOWN along the LEFT BORDER or along the SLASH
            if (!isBorder) { // We must collide only with an edge
                switch (option) {
                    case 1: // go back
                        currentStateWalking = StateWalking.LEFT_UP;
                        break;
                    case 2:
                        currentStateWalking = StateWalking.FLOOR_RIGHT;
                        break;
                    case 3:
                        currentStateWalking = StateWalking.SLASH_UP;
                        break;
                }
            }
        } else {
            // We are walking DOWN along the RIGHT BORDER or along the BACKSLASH
            if (!isBorder) { // We must collide only with an edge
                switch (option) {
                    case 1: // go back
                        currentStateWalking = StateWalking.RIGHT_UP;
                        break;
                    case 2:
                        currentStateWalking = StateWalking.FLOOR_LEFT;
                        break;
                    case 3:
                        currentStateWalking = StateWalking.BACKSLASH_UP;
                        break;
                }
            }
        }
    }

    private void velY0VelXGT0(float y, boolean isBorder, int option) {
        if (y < screen.getGameCam().position.y) {
            // We are walking to the RIGHT along the FLOOR EDGE
            if (isBorder) { // We must collide only with a border
                switch (option) {
                    case 1: // go back
                        currentStateWalking = StateWalking.FLOOR_LEFT;
                        break;
                    case 2:
                        currentStateWalking = StateWalking.RIGHT_UP;
                        break;
                    case 3:
                        currentStateWalking = StateWalking.BACKSLASH_UP;
                        break;
                }
            }
        } else {
            // We are walking to the RIGHT along the CEILING EDGE
            if (isBorder) { // We must collide only with a border
                switch (option) {
                    case 1: // go back
                        currentStateWalking = StateWalking.CEILING_LEFT;
                        break;
                    case 2:
                        currentStateWalking = StateWalking.RIGHT_DOWN;
                        break;
                    case 3:
                        currentStateWalking = StateWalking.SLASH_DOWN;
                        break;
                }
            }
        }
    }

    private void velY0VelXLT0(float y, boolean isBorder, int option) {
        if (y < screen.getGameCam().position.y) {
            // We are walking to the LEFT along the FLOOR EDGE
            if (isBorder) { // We must collide only with a border
                switch (option) {
                    case 1: // go back
                        currentStateWalking = StateWalking.FLOOR_RIGHT;
                        break;
                    case 2:
                        currentStateWalking = StateWalking.LEFT_UP;
                        break;
                    case 3:
                        currentStateWalking = StateWalking.SLASH_UP;
                        break;
                }
            }
        } else {
            // We are walking to the LEFT along the CEILING EDGE
            if (isBorder) { // We must collide only with a border
                switch (option) {
                    case 1: // go back
                        currentStateWalking = StateWalking.CEILING_RIGHT;
                        break;
                    case 2:
                        currentStateWalking = StateWalking.LEFT_DOWN;
                        break;
                    case 3:
                        currentStateWalking = StateWalking.BACKSLASH_DOWN;
                        break;
                }
            }
        }
    }

    @Override
    protected String getFinalEnemyName() {
        return NAME;
    }

    @Override
    protected int getFinalEnemyDamage() {
        return damage;
    }

    @Override
    protected void setInitialState() {
        currentStateFinalEnemy = StateFinalEnemy.WALKING;
    }

    @Override
    protected TextureRegion getHelpImage() {
        return Assets.getInstance().getScene2d().getHelpFinalEnemyLevelOne();
    }

    private boolean isDrawable() {
        return currentStateFinalEnemy != StateFinalEnemy.INACTIVE &&
                currentStateFinalEnemy != StateFinalEnemy.EXPLODING &&
                currentStateFinalEnemy != StateFinalEnemy.DEAD;
    }

    private void drawPowers(Batch batch) {
        if (currentPowerState == PowerState.POWERFUL) {
            powerFXSprite.draw(batch);
        }
    }

    private void drawFxs(Batch batch) {
        switch (currentStateFinalEnemy) {
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
        // We draw FinalEnemyLevelOne in these states: WALKING IDLE SHOOTING INJURED DYING
        if (isDrawable()) {
            drawPowers(batch);
            super.draw(batch);
        } else {
            drawFxs(batch);
        }
    }
}
