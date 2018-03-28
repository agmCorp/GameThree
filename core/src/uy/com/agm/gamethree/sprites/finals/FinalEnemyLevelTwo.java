package uy.com.agm.gamethree.sprites.finals;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionE;
import uy.com.agm.gamethree.assets.sprites.AssetFinalEnemyLevelTwo;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.boundary.Edge;
import uy.com.agm.gamethree.sprites.weapons.IShootStrategy;
import uy.com.agm.gamethree.sprites.weapons.Weapon;
import uy.com.agm.gamethree.sprites.weapons.enemy.EnemySwordShooting;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.Vector2Util;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 12/30/2017.
 */

public class FinalEnemyLevelTwo extends FinalEnemy {
    private static final String TAG = FinalEnemyLevelTwo.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    private static final String NAME = "SKULLYHOOD";
    public static final float CIRCLE_SHAPE_RADIUS_METERS = 60.0f / PlayScreen.PPM;
    private static final float TARGET_RADIUS_METERS = 30.0f / PlayScreen.PPM;
    private static final float LINEAR_VELOCITY = 4.0f;
    private static final float DENSITY = 1000.0f;
    private static final int MAX_DAMAGE = 15;
    private static final float EXPLOSION_SHAKE_DURATION = 2.0f;
    private static final float HIT_SHAKE_DURATION = 1.0f;
    private static final float CHANGE_STATE_MIN_TIME_SECONDS = 2.0f;
    private static final float CHANGE_STATE_MAX_TIME_SECONDS = 4.0f;
    private static final float IDLE_STATE_TIME_SECONDS = 5.0f;
    private static final float DYING_STATE_TIME_SECONDS = 2.0f;
    private static final float FIRE_DELAY_SECONDS = 0.7f;
    private static final int SCORE = 500;

    private int damage;
    private float stateFinalEnemyTime;
    private float changeTime;
    private float timeToChange;
    private float agonyTime;

    private Animation finalEnemyLevelTwoMovingUpAnimation;
    private Animation finalEnemyLevelTwoMovingDownAnimation;
    private Animation finalEnemyLevelTwoMovingLeftRightAnimation;
    private Animation finalEnemyLevelTwoIdleAnimation;
    private Animation finalEnemyLevelTwoShootingUpAnimation;
    private Animation finalEnemyLevelTwoShootingDownAnimation;
    private Animation finalEnemyLevelTwoDyingAnimation;

    // Circle on the screen where FinalEnemyLevelTwo must go
    private Circle target;
    private Circle tmpCircle; // Temp GC friendly circle

    // Power FX
    private PowerState currentPowerState;
    private Animation powerFXAnimation;
    private float powerFXStateTime;
    private Sprite powerFXSprite;

    // Explosion FX
    private Animation explosionFXAnimation;
    private float explosionFXStateTime;
    private Sprite explosionFXSprite;

    public FinalEnemyLevelTwo(PlayScreen screen, float x, float y) {
        super(screen, x, y, AssetFinalEnemyLevelTwo.WIDTH_METERS, AssetFinalEnemyLevelTwo.HEIGHT_METERS);

        // Animations
        finalEnemyLevelTwoMovingUpAnimation = Assets.getInstance().getFinalEnemyLevelTwo().getFinalEnemyLevelTwoMovingUpAnimation();
        finalEnemyLevelTwoMovingDownAnimation = Assets.getInstance().getFinalEnemyLevelTwo().getFinalEnemyLevelTwoMovingDownAnimation();
        finalEnemyLevelTwoMovingLeftRightAnimation = Assets.getInstance().getFinalEnemyLevelTwo().getFinalEnemyLevelTwoMovingLeftRightAnimation();
        finalEnemyLevelTwoIdleAnimation = Assets.getInstance().getFinalEnemyLevelTwo().getFinalEnemyLevelTwoIdleAnimation();
        finalEnemyLevelTwoShootingUpAnimation = Assets.getInstance().getFinalEnemyLevelTwo().getFinalEnemyLevelTwoShootingUpAnimation();
        finalEnemyLevelTwoShootingDownAnimation = Assets.getInstance().getFinalEnemyLevelTwo().getFinalEnemyLevelTwoShootingDownAnimation();
        finalEnemyLevelTwoDyingAnimation = Assets.getInstance().getFinalEnemyLevelTwo().getFinalEnemyLevelTwoDeathAnimation();

        // FinalEnemyLevelTwo variables initialization
        damage = MAX_DAMAGE;
        stateFinalEnemyTime = 0;
        changeTime = 0;
        timeToChange = getNextTimeToChange();
        agonyTime = 0;

        // Place origin of rotation in the center of the Sprite
        setOriginCenter();

        // Initialize target
        target = new Circle(0, 0, TARGET_RADIUS_METERS);

        // Temp GC friendly circle
        tmpCircle = new Circle();

        // Move to a new target at constant speed
        moveToNewTarget();

        // -------------------- PowerFX --------------------

        // PowerFX variables initialization
        currentPowerState = PowerState.NORMAL;
        powerFXAnimation = Assets.getInstance().getFinalEnemyLevelTwo().getFinalEnemyLevelTwoPowerAnimation();
        powerFXStateTime = 0;

        // Set the power's texture
        powerFXSprite = new Sprite(Assets.getInstance().getFinalEnemyLevelTwo().getFinalEnemyLevelTwoPowerStand());

        // Only to set width and height of our spritePower (in powerStatePowerful(...) we set its position)
        powerFXSprite.setBounds(getX(), getY(), AssetFinalEnemyLevelTwo.POWER_WIDTH_METERS, AssetFinalEnemyLevelTwo.POWER_HEIGHT_METERS);

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
                WorldContactListener.SHIELD_BIT |
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
                        newRandomStateFinalEnemy = StateFinalEnemy.IDLE;
                        timeToChange = IDLE_STATE_TIME_SECONDS;
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
                        newRandomStateFinalEnemy = StateFinalEnemy.IDLE;
                        timeToChange = IDLE_STATE_TIME_SECONDS;
                    }
                    break;
            }
        }
        return newRandomStateFinalEnemy;
    }

    @Override
    protected IShootStrategy getShootStrategy() {
        return new EnemySwordShooting(screen, 0, FIRE_DELAY_SECONDS);
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
                break;
            default:
                break;
        }

        // FinalEnemyLevelTwo could have been destroyed
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
        // Set velocity calculated to reach the target circle
        b2body.setLinearVelocity(velocity);

        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        // Depending on the velocity, set the sprite's rotation angle
        setRotationAngle();

        // Depending on the velocity, set the appropriate sprite animation
        setAnimation(dt);

        if (reachTarget()) {
            moveToNewTarget();
        }

        // New random state
        currentStateFinalEnemy = getNewRandomState(dt);
    }

    private void moveToNewTarget() {
        // We cannot use screen.getGameCam().position.x because our finalEnemy is created at the end
        // of the level when the camera is still moving.
        float worldWidth = screen.getGameViewPort().getWorldWidth();
        float worldHeight = screen.getGameViewPort().getWorldHeight();
        float xMin = TARGET_RADIUS_METERS;
        float xMax = worldWidth - TARGET_RADIUS_METERS;
        float xHalf = worldWidth / 2;
        float yMin = worldHeight * (PlayScreen.WORLD_SCREENS - 1) + TARGET_RADIUS_METERS;
        float yMax = worldHeight * PlayScreen.WORLD_SCREENS - TARGET_RADIUS_METERS;
        float yHalf = worldHeight * PlayScreen.WORLD_SCREENS - worldHeight / 2;

        int randomPoint = MathUtils.random(1, 5);
        switch (randomPoint) {
            case 1:
                target.setPosition(xMin, yHalf);
                break;
            case 2:
                target.setPosition(xHalf, yMax);
                break;
            case 3:
                target.setPosition(xMax, yHalf);
                break;
            case 4:
                target.setPosition(xHalf, yMin);
                break;
            case 5:
                target.setPosition(xHalf, yHalf);
                break;
        }

        // Move to target
        tmp.set(b2body.getPosition().x, b2body.getPosition().y);
        Vector2Util.goToTarget(tmp, target.x, target.y, LINEAR_VELOCITY);
        velocity.set(tmp);
    }

    private void setRotationAngle() {
        if (b2body.getLinearVelocity().len() > 0.0f) {
            setRotation(90.0f);
            float velAngle = this.b2body.getLinearVelocity().angle();
            if (0 <= velAngle && velAngle <= 180.0f) {
                setRotation(270.0f);
            }
            rotate(velAngle);
        }
    }

    private void setAnimation(float dt) {
        float vy = b2body.getLinearVelocity().y;
        if (vy > 0.0f) {
            setRegion((TextureRegion) finalEnemyLevelTwoMovingUpAnimation.getKeyFrame(stateFinalEnemyTime, true));
        } else {
            if (vy < 0.0f) {
                setRegion((TextureRegion) finalEnemyLevelTwoMovingDownAnimation.getKeyFrame(stateFinalEnemyTime, true));
            } else { // vy == 0
                setRegion((TextureRegion) finalEnemyLevelTwoMovingLeftRightAnimation.getKeyFrame(stateFinalEnemyTime, true));
            }
        }
        stateFinalEnemyTime += dt;
    }

    private boolean reachTarget() {
        tmpCircle.set(b2body.getPosition().x, b2body.getPosition().y, CIRCLE_SHAPE_RADIUS_METERS);
        return target.overlaps(tmpCircle);
    }

    private void stateIdle(float dt) {
        // Stop
        b2body.setLinearVelocity(0.0f, 0.0f);

        // Preserve the rotation state
        float rotation = getRotation();

        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) finalEnemyLevelTwoIdleAnimation.getKeyFrame(stateFinalEnemyTime, true));
        stateFinalEnemyTime += dt;

        // Apply previous rotation state
        setRotation(rotation);

        // New random state
        currentStateFinalEnemy = getNewRandomState(dt);
    }

    private void stateShooting(float dt) {
        // Stop
        b2body.setLinearVelocity(0.0f, 0.0f);

        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        // Calculate shooting angle
        float angle = tmp.set(screen.getPlayer().getB2body().getPosition().x, screen.getPlayer().getB2body().getPosition().y)
                .sub(b2body.getPosition().x, b2body.getPosition().y).angle();

        // Depending on the angle, set the sprite's rotation angle and animation
        setRotation(90.0f);
        if (0 <= angle && angle <= 180.0f) {
            setRegion((TextureRegion) finalEnemyLevelTwoShootingUpAnimation.getKeyFrame(stateFinalEnemyTime, true));
            setRotation(270.0f);
        } else {
            setRegion((TextureRegion) finalEnemyLevelTwoShootingDownAnimation.getKeyFrame(stateFinalEnemyTime, true));
        }
        rotate(angle);
        stateFinalEnemyTime += dt;

        // Shoot time!
        openFire(dt);

        // New random state
        currentStateFinalEnemy = getNewRandomState(dt);
    }

    private void stateInjured(float dt) {
        // Destroy box2D body
        world.destroyBody(b2body);

        // Stop level music
        AudioManager.getInstance().stopMusic();

        // Death animation
        stateFinalEnemyTime = 0;

        // Audio FX
        AudioManager.getInstance().play(Assets.getInstance().getSounds().getFinalEnemyExplosion());

        // Set score
        screen.getHud().addScore(SCORE);

        // Set the new state
        currentStateFinalEnemy = StateFinalEnemy.DYING;
    }

    private void stateDying(float dt) {
        agonyTime += dt;
        if (agonyTime >= DYING_STATE_TIME_SECONDS) {
            // Exploding animation
            explosionFXStateTime = 0;

            // Audio FX
            AudioManager.getInstance().play(Assets.getInstance().getSounds().getFinalEnemyExplosion());

            // Set the new state
            currentStateFinalEnemy = StateFinalEnemy.EXPLODING;
        } else {
            // Preserve the rotation state
            float rotation = getRotation();

            setRegion((TextureRegion) finalEnemyLevelTwoDyingAnimation.getKeyFrame(stateFinalEnemyTime, true));
            stateFinalEnemyTime += dt;

            // Apply previous rotation state
            setRotation(rotation);
        }
    }

   private void stateExploding(float dt) {
       if (explosionFXAnimation.isAnimationFinished(explosionFXStateTime)) {
           // Audio FX
           AudioManager.getInstance().play(Assets.getInstance().getSounds().getLevelCompleted());

           // Set the new state
           currentStateFinalEnemy = StateFinalEnemy.DEAD;
       } else {
           // Animation
           explosionFXSprite.setRegion((TextureRegion) explosionFXAnimation.getKeyFrame(explosionFXStateTime, true));
           explosionFXStateTime += dt;

           // Apply rotation of the main character
           explosionFXSprite.setRotation(getRotation());

           // Get center of main character bounding rectangle
           getBoundingRectangle().getCenter(tmp);

           // Center the Sprte with respect to the center of FinalEnemyLevelTwo
           explosionFXSprite.setPosition( tmp.x - explosionFXSprite.getWidth() / 2, tmp.y - explosionFXSprite.getHeight() / 2);
       }
    }

    private void powerStatePowerfulToNormal(float dt) {
        // If our final enemy is not walking nor shooting, he becomes weak
        if (currentStateFinalEnemy != StateFinalEnemy.WALKING && currentStateFinalEnemy != StateFinalEnemy.SHOOTING) {
            powerFXStateTime = 0;
            currentPowerState = PowerState.NORMAL;
            AudioManager.getInstance().play(Assets.getInstance().getSounds().getFinalEnemyPowerDown());
        } else {
            // Animation
            powerFXSprite.setRegion((TextureRegion) powerFXAnimation.getKeyFrame(powerFXStateTime, true));
            powerFXStateTime += dt;

            // Apply rotation of the main character
            powerFXSprite.setRotation(getRotation());

            // Update our Sprite to correspond with the position of our finalEnemyLevelTwo's Box2D body
            powerFXSprite.setPosition(b2body.getPosition().x - powerFXSprite.getWidth() / 2, b2body.getPosition().y - powerFXSprite.getHeight() / 2);
        }
    }

    private void powerStateNormalToPowerful() {
        // If our final enemy is walking or shooting, he becomes powerful
        if (currentStateFinalEnemy == StateFinalEnemy.WALKING || currentStateFinalEnemy == StateFinalEnemy.SHOOTING) {
            powerFXStateTime = 0;
            currentPowerState = PowerState.POWERFUL;
            AudioManager.getInstance().play(Assets.getInstance().getSounds().getFinalEnemyPowerUp());
        }
    }

    @Override
    public void onHit(Weapon weapon) {
        if (screen.getPlayer().isSilverBulletEnabled()) {
            if (currentStateFinalEnemy == StateFinalEnemy.IDLE) {
                weapon.onTarget();
                damage--;
                screen.getHud().decreaseHealth();
                AudioManager.getInstance().play(Assets.getInstance().getSounds().getFinalEnemyHit(), FinalEnemy.HIT_MAX_VOLUME);
                screen.getShaker().shake(HIT_SHAKE_DURATION);
                if (damage <= 0) {
                    screen.getHud().hideHealthBarInfo();
                    currentStateFinalEnemy = StateFinalEnemy.INJURED;
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
        // Nothing to do here
    }

    @Override
    protected boolean finalEnemyStarts() {
        checkBoundaries();
        return b2body.isActive();
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
        return Assets.getInstance().getScene2d().getHelpFinalEnemyLevelTwo();
    }

    private void checkBoundaries() {
        /* When a FinalEnemyLevelTwo is on camera, it activates (it can collide).
        * You have to be very careful because if the final level Two enemy is destroyed, its b2body does not exist and gives
        * random errors if you try to active it.
        */
        float upperEdge = screen.getUpperEdge().getB2body().getPosition().y + Edge.HEIGHT_METERS / 2; //  Upper edge of the upperEdge :)

        if (upperEdge > b2body.getPosition().y + CIRCLE_SHAPE_RADIUS_METERS) {
            b2body.setActive(true);
        }
    }

    private boolean isDrawable() {
        return currentStateFinalEnemy != StateFinalEnemy.DEAD &&
                currentStateFinalEnemy != StateFinalEnemy.EXPLODING &&
                currentStateFinalEnemy != StateFinalEnemy.INACTIVE;
    }

    private void drawPowers(Batch batch) {
        if (currentPowerState == PowerState.POWERFUL) {
            powerFXSprite.draw(batch);
        }
    }

    private void drawExplosion(Batch batch) {
        if (currentStateFinalEnemy == StateFinalEnemy.EXPLODING) {
            explosionFXSprite.draw(batch);
        }
    }

    @Override
    public void draw(Batch batch) {
        // We draw FinalEnemyLevelTwo in these states: WALKING IDLE SHOOTING INJURED DYING
        if (isDrawable()) {
            drawPowers(batch);
            super.draw(batch);
        } else {
            drawExplosion(batch);
        }
    }

    @Override
    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(target.x, target.y, target.radius);
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
    }
}
