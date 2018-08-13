package uy.com.agm.gamethree.actors.finals;

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
import uy.com.agm.gamethree.actors.weapons.enemy.EnemyMagicShooting;
import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionE;
import uy.com.agm.gamethree.assets.sprites.AssetBossFour;
import uy.com.agm.gamethree.assets.sprites.AssetSplat;
import uy.com.agm.gamethree.game.DebugConstants;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.Vector2Util;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 12/30/2017.
 */

public class BossFour extends Boss {
    private static final String TAG = BossFour.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    private static final String NAME = "DARKHEART";
    public static final float CIRCLE_SHAPE_RADIUS_METERS = 60.0f / PlayScreen.PPM;
    private static final float TARGET_RADIUS_METERS = 30.0f / PlayScreen.PPM;
    private static final float WORLD_WIDTH = PlayScreen.V_WIDTH / PlayScreen.PPM;
    private static final float WORLD_HEIGHT = PlayScreen.V_HEIGHT / PlayScreen.PPM;
    private static final float X_MIN = TARGET_RADIUS_METERS;
    private static final float X_MAX = WORLD_WIDTH - TARGET_RADIUS_METERS;
    private static final float X_HALF = WORLD_WIDTH / 2;
    private static final float Y_MIN = WORLD_HEIGHT * (PlayScreen.WORLD_SCREENS - 1) + TARGET_RADIUS_METERS;
    private static final float Y_MAX = WORLD_HEIGHT * PlayScreen.WORLD_SCREENS - TARGET_RADIUS_METERS;
    private static final float Y_HALF = WORLD_HEIGHT * PlayScreen.WORLD_SCREENS - WORLD_HEIGHT / 2;
    private static final float RADIUS_METERS = 1.5f;
    private static final float LINEAR_VELOCITY = 4.5f;
    private static final float PERIOD_SECONDS = 2 * MathUtils.PI * RADIUS_METERS / LINEAR_VELOCITY;
    private static final float W = 2 * MathUtils.PI / PERIOD_SECONDS;
    private static final float CIRCULAR_PATH_PROBABILITY = 0.3f;  // 30%
    private static final float DENSITY = 1000.0f;
    private static final int MAX_DAMAGE = 20 * (DebugConstants.DESTROY_BOSSES_ONE_HIT ? 0 : 1);
    private static final float EXPLOSION_SHAKE_DURATION = 2.0f;
    private static final float HIT_SHAKE_DURATION = 1.0f;
    private static final float CHANGE_STATE_MIN_TIME_SECONDS = 2.0f;
    private static final float CHANGE_STATE_MAX_TIME_SECONDS = 4.0f;
    private static final float IDLE_STATE_TIME_SECONDS = 5.0f;
    private static final float DYING_STATE_TIME_SECONDS = 3.5f;
    private static final float FIRE_DELAY_SECONDS = 0.7f;
    private static final int SCORE = 500;

    private int damage;
    private float stateBossTime;
    private float changeTime;
    private float timeToChange;
    private float agonyTime;
    private float elapsedTime;
    private boolean circularPath;

    private Animation bossFourWalkAnimation;
    private Animation bossFourIdleAnimation;
    private Animation bossFourShootAnimation;
    private Animation bossFourDyingAnimation;

    // Circle on the screen where BossFour must go
    private Circle target;
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

    public BossFour(PlayScreen screen, float x, float y) {
        super(screen, x, y, AssetBossFour.WIDTH_METERS, AssetBossFour.HEIGHT_METERS);

        // Animations
        bossFourWalkAnimation = Assets.getInstance().getBossFour().getBossFourWalkAnimation();
        bossFourIdleAnimation = Assets.getInstance().getBossFour().getBossFourIdleAnimation();
        bossFourShootAnimation = Assets.getInstance().getBossFour().getBossFourShootAnimation();
        bossFourDyingAnimation = Assets.getInstance().getBossFour().getBossFourDeathAnimation();

        // BossFour variables initialization
        damage = MAX_DAMAGE;
        stateBossTime = 0;
        changeTime = 0;
        timeToChange = getNextTimeToChange();
        agonyTime = 0;

        // Initialize target
        target = new Circle(0, 0, TARGET_RADIUS_METERS);

        // Temporary GC friendly circle
        tmpCircle = new Circle();

        // Move to a new target at constant speed
        setNewTarget();
        tmp = getSpeedTarget();
        velocity.set(tmp);
        initCircularPath();

        // -------------------- PowerFX --------------------

        // PowerFX variables initialization
        powerFXAnimation = Assets.getInstance().getBossFour().getBossFourPowerAnimation();
        powerFXStateTime = 0;

        // Set the power's texture
        powerFXSprite = new Sprite(Assets.getInstance().getBossFour().getBossFourPowerStand());

        // Only to set width and height of our spritePower (in powerStatePowerful(...) we set its position)
        powerFXSprite.setBounds(getX(), getY(), AssetBossFour.POWER_WIDTH_METERS, AssetBossFour.POWER_HEIGHT_METERS);

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

        // -------------------- SplatFX --------------------

        // Set the splat's texture
        Sprite spriteSplat = new Sprite(Assets.getInstance().getSplat().getRandomBossSplat());

        // Only to set width and height of our spriteSplat (in stateDead(...) we set its position)
        spriteSplat.setBounds(getX(), getY(), AssetSplat.BOSS_SPLAT_WIDTH_METERS, AssetSplat.BOSS_SPLAT_HEIGHT_METERS);

        // Splat FX Sprite
        splatFXSprite = new Sprite(spriteSplat);
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
        return new EnemyMagicShooting(screen, 0, FIRE_DELAY_SECONDS);
    }

    @Override
    protected float getCircleShapeRadiusMeters() {
        return CIRCLE_SHAPE_RADIUS_METERS;
    }

    @Override
    protected TextureRegion getKnockBackFrame(float dt) {
        TextureRegion region = (TextureRegion) bossFourIdleAnimation.getKeyFrame(stateBossTime, true);
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

        // BossFour could have been destroyed
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
        // Set new velocity (see getSpeed(...))
        b2body.setLinearVelocity(velocity);

        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        // Preserve the flip state
        boolean isFlipX = isFlipX();
        boolean isFlipY = isFlipY();

        TextureRegion region = (TextureRegion) bossFourWalkAnimation.getKeyFrame(stateBossTime);
        setRegion(region);
        stateBossTime += dt;

        // Determines where BossFour is looking
        setFlipState(isFlipX, isFlipY);

        velocity.set(getSpeed(dt));

        // New random state
        currentStateBoss = getNewRandomState(dt);
    }

    private void setFlipState(boolean isFlipX, boolean isFlipY) {
        float heroX = screen.getCreator().getHero().getB2body().getPosition().x;
        float pivotRight = b2body.getPosition().x + CIRCLE_SHAPE_RADIUS_METERS;
        float pivotLeft = b2body.getPosition().x - CIRCLE_SHAPE_RADIUS_METERS;

        if (heroX < pivotLeft) {
            setFlip(true, false);
        }
        if (pivotLeft <= heroX && heroX <= pivotRight) {
            setFlip(isFlipX, isFlipY);
        }
        if (heroX > pivotRight) {
            setFlip(false, false);
        }
    }

    private void setNewTarget() {
        int randomPoint = MathUtils.random(1, 9);
        switch (randomPoint) {
            case 1:
                target.setPosition(X_MIN, Y_HALF);
                break;
            case 2:
                target.setPosition(X_HALF, Y_MAX);
                break;
            case 3:
                target.setPosition(X_MAX, Y_HALF);
                break;
            case 4:
                target.setPosition(X_HALF, Y_MIN);
                break;
            case 5:
                target.setPosition(X_HALF, Y_HALF);
                break;
            case 6:
                target.setPosition(X_MIN, Y_MAX);
                break;
            case 7:
                target.setPosition(X_MAX, Y_MAX);
                break;
            case 8:
                target.setPosition(X_MIN, Y_MIN);
                break;
            case 9:
                target.setPosition(X_MAX, Y_MIN);
                break;
        }
    }

    private Vector2 getSpeed(float dt) {
        if (circularPath) {
            if (finishCircularPath()) {
                initCircularPath();
                tmp = getNewPath(dt);
            } else {
                tmp = getSpeedCircularPath(dt);
            }
        } else {
            if (reachTarget()) {
                tmp = getNewPath(dt);
            } else {
                tmp = getSpeedTarget();
            }
        }
        return tmp;
    }

    private void initCircularPath() {
        elapsedTime = 0;
        circularPath = false;
    }

    private Vector2 getNewPath(float dt) {
        circularPath = startCircularPath();
        if (circularPath) {
            tmp = getSpeedCircularPath(dt);
        } else	{
            setNewTarget();
            tmp = getSpeedTarget();
        }
        return tmp;
    }

    private boolean finishCircularPath() {
        return elapsedTime >= PERIOD_SECONDS;
    }

    private Vector2 getSpeedTarget() {
        tmp.set(b2body.getPosition().x, b2body.getPosition().y);
        Vector2Util.goToTarget(tmp, target.x, target.y, LINEAR_VELOCITY);
        return tmp;
    }

    private Vector2 getSpeedCircularPath(float dt) {
        if (target.x == X_MIN && target.y == Y_HALF) {
            tmp.set(RADIUS_METERS * W * MathUtils.sin(W * elapsedTime), -RADIUS_METERS * W * MathUtils.cos(W * elapsedTime));
        } else if (target.x == X_MAX && target.y == Y_HALF) {
            tmp.set(-1 * RADIUS_METERS * W * MathUtils.sin(W * elapsedTime), -RADIUS_METERS * W * MathUtils.cos(W * elapsedTime));
        } else if (target.x == X_HALF && target.y == Y_MAX) {
            tmp.set(-RADIUS_METERS * W * MathUtils.cos(W * elapsedTime), -1 * RADIUS_METERS * W * MathUtils.sin(W * elapsedTime));
        } else if (target.x == X_HALF && target.y == Y_MIN) {
            tmp.set(-RADIUS_METERS * W * MathUtils.cos(W * elapsedTime), RADIUS_METERS * W * MathUtils.sin(W * elapsedTime));
        }
        elapsedTime += dt;
        return tmp;
    }

    private boolean startCircularPath() {
        return 	((target.x == X_MIN && target.y == Y_HALF) ||
                (target.x == X_HALF && target.y == Y_MAX) ||
                (target.x == X_MAX && target.y == Y_HALF) ||
                (target.x == X_HALF && target.y == Y_MIN)) &&
                MathUtils.random() <= CIRCULAR_PATH_PROBABILITY;
    }

    private boolean reachTarget() {
        tmpCircle.set(b2body.getPosition().x, b2body.getPosition().y, CIRCLE_SHAPE_RADIUS_METERS);
        return target.overlaps(tmpCircle);
    }

    private void stateIdle(float dt) {
        // Stop
        b2body.setLinearVelocity(0.0f, 0.0f);

        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        // Preserve the flip state
        boolean isFlipX = isFlipX();
        boolean isFlipY = isFlipY();

        TextureRegion region = (TextureRegion) bossFourIdleAnimation.getKeyFrame(stateBossTime);
        setRegion(region);
        stateBossTime += dt;

        // Determines where BossFour is looking
        setFlipState(isFlipX, isFlipY);

        // New random state
        currentStateBoss = getNewRandomState(dt);
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

        // Preserve the flip state
        boolean isFlipX = isFlipX();
        boolean isFlipY = isFlipY();

        TextureRegion region = (TextureRegion) bossFourShootAnimation.getKeyFrame(stateBossTime);
        setRegion(region);
        stateBossTime += dt;

        // Determines where BossFour is looking
        setFlipState(isFlipX, isFlipY);

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
        agonyTime += dt;
        if (agonyTime >= DYING_STATE_TIME_SECONDS) {
            // Exploding animation
            explosionFXStateTime = 0;

            // Audio FX
            AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getBossExplosion());

            // Set the new state
            currentStateBoss = StateBoss.EXPLODING;
        } else {
            // Preserve the flip state
            boolean isFlipX = isFlipX();
            boolean isFlipY = isFlipY();

            setRegion((TextureRegion) bossFourDyingAnimation.getKeyFrame(stateBossTime));
            stateBossTime += dt;

            // Apply previous flip state
            setFlip(isFlipX, isFlipY);
        }
    }

   private void stateExploding(float dt) {
       if (explosionFXAnimation.isAnimationFinished(explosionFXStateTime)) {
           // Show victory animation and play audio FX
           victoryFX();

           // Set the new state
           currentStateBoss = StateBoss.DEAD;
       } else {
           // Animation
           explosionFXSprite.setRegion((TextureRegion) explosionFXAnimation.getKeyFrame(explosionFXStateTime, true));
           explosionFXStateTime += dt;

           // Get center of its bounding rectangle
           getBoundingRectangle().getCenter(tmp);

           // Center the Sprite in tmp
           explosionFXSprite.setPosition(tmp.x - explosionFXSprite.getWidth() / 2, tmp.y - explosionFXSprite.getHeight() / 2);
       }
    }

    private void stateDead() {
        // Get center of the bounding rectangle of the explosion
        explosionFXSprite.getBoundingRectangle().getCenter(tmp);

        // Center the Sprite in tmp
        splatFXSprite.setPosition(tmp.x - splatFXSprite.getWidth() / 2, tmp.y - splatFXSprite.getHeight() / 2);
    }

    private void powerStatePowerfulToNormal(float dt) {
        // If our final enemy is not walking nor shooting, he becomes weak
        if (currentStateBoss != StateBoss.WALKING && currentStateBoss != StateBoss.SHOOTING) {
            powerFXStateTime = 0;
            currentPowerState = PowerState.NORMAL;
            AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getBossPowerDown());
        } else {
            // Animation
            powerFXSprite.setRegion((TextureRegion) powerFXAnimation.getKeyFrame(powerFXStateTime, true));
            powerFXStateTime += dt;

            // Update our Sprite to correspond with the position of our BossFour's Box2D body
            powerFXSprite.setPosition(b2body.getPosition().x - powerFXSprite.getWidth() / 2, b2body.getPosition().y - powerFXSprite.getHeight() / 2);
        }
    }

    private void powerStateNormalToPowerful() {
        // If our final enemy is walking or shooting, he becomes powerful
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
        return Assets.getInstance().getScene2d().getHelpBossFour();
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
        // We draw BossFour in these states: WALKING IDLE SHOOTING INJURED DYING
        if (isDrawable()) {
            drawPowers(batch);
            super.draw(batch);
        } else {
            drawFxs(batch);
        }
    }

    @Override
    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(target.x, target.y, target.radius);
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
    }
}
