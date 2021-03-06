package uy.com.agm.gamethree.actors.bosses;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import uy.com.agm.gamethree.actors.weapons.IShootStrategy;
import uy.com.agm.gamethree.actors.weapons.Weapon;
import uy.com.agm.gamethree.actors.weapons.enemy.EnemyBlastShooting;
import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.sprites.AssetBossThree;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionE;
import uy.com.agm.gamethree.assets.sprites.AssetSplat;
import uy.com.agm.gamethree.game.DebugConstants;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 12/30/2017.
 */

public class BossThree extends Boss {
    private static final String TAG = BossThree.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    private static final String NAME = "THE BEHOLDER";
    public static final float CIRCLE_SHAPE_RADIUS_METERS = 60.0f / PlayScreen.PPM;
    private static final float HORIZONTAL_PERIOD_SECONDS = 2.0f;
    private static final float VERTICAL_PERIOD_SECONDS = 3.0f;
    private static final float HORIZONTAL_RADIUS_METERS = 1.0f;
    private static final float VERTICAL_RADIUS_METERS = 2.0f;
    private static final float DENSITY = 1000.0f;
    private static final int MAX_DAMAGE = 16 * (DebugConstants.DESTROY_BOSSES_ONE_HIT ? 0 : 1);
    private static final float EXPLOSION_SHAKE_DURATION = 2.0f;
    private static final float HIT_SHAKE_DURATION = 1.0f;
    private static final float CHANGE_STATE_MIN_TIME_SECONDS = 2.0f;
    private static final float CHANGE_STATE_MAX_TIME_SECONDS = 4.0f;
    private static final float IDLE_STATE_TIME_SECONDS = 5.0f;
    private static final float DYING_STATE_TIME_SECONDS = 2.0f;
    private static final float FIRE_DELAY_SECONDS = 0.7f;
    private static final int SCORE = 1500;

    private int damage;
    private float stateBossTime;
    private float changeTime;
    private float timeToChange;
    private float agonyTime;
    private float elapsedTime;
    private boolean horizontalCounterclockwise;
    private boolean verticalCounterclockwise;
    private boolean movingHorizontal;

    private Animation bossThreeWalkAnimation;
    private Animation bossThreeIdleAnimation;
    private Animation bossThreeShootAnimation;
    private Animation bossThreeDeathAnimation;

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

    public BossThree(PlayScreen screen, float handicap, float x, float y) {
        super(screen, x, y, AssetBossThree.WIDTH_METERS, AssetBossThree.HEIGHT_METERS);

        // Animations
        bossThreeWalkAnimation = Assets.getInstance().getBossThree().getBossThreeWalkAnimation();
        bossThreeIdleAnimation = Assets.getInstance().getBossThree().getBossThreeIdleAnimation();
        bossThreeShootAnimation = Assets.getInstance().getBossThree().getBossThreeShootAnimation();
        bossThreeDeathAnimation = Assets.getInstance().getBossThree().getBossThreeDeathAnimation();

        // BossThree variables initialization
        damage = MathUtils.ceil(MAX_DAMAGE * handicap);
        stateBossTime = 0;
        changeTime = 0;
        timeToChange = getNextTimeToChange();
        agonyTime = 0;
        elapsedTime = 0;
        horizontalCounterclockwise = MathUtils.randomBoolean();
        verticalCounterclockwise = MathUtils.randomBoolean();
        movingHorizontal = MathUtils.randomBoolean();
        velocity.set(0.0f, 0.0f); // Initially at rest

        // -------------------- PowerFX --------------------

        // PowerFX variables initialization
        powerFXAnimation = Assets.getInstance().getBossThree().getBossThreePowerAnimation();
        powerFXStateTime = 0;

        // Set the power's texture
        powerFXSprite = new Sprite(Assets.getInstance().getBossThree().getBossThreePowerStand());

        // Only to set width and height of spritePower (in powerStatePowerful(...) we set its position)
        powerFXSprite.setBounds(getX(), getY(), AssetBossThree.POWER_WIDTH_METERS, AssetBossThree.POWER_HEIGHT_METERS);

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

        // -------------------- SplatFX --------------------

        // Set the splat's texture
        Sprite spriteSplat = new Sprite(Assets.getInstance().getSplat().getRandomBossSplat());

        // Only to set width and height of spriteSplat (in stateDead(...) we set its position)
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
        return new EnemyBlastShooting(screen, 0, FIRE_DELAY_SECONDS);
    }

    @Override
    protected float getCircleShapeRadiusMeters() {
        return CIRCLE_SHAPE_RADIUS_METERS;
    }

    @Override
    protected TextureRegion getKnockBackFrame(float dt) {
        TextureRegion region = (TextureRegion) bossThreeIdleAnimation.getKeyFrame(stateBossTime, true);
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

        // BossThree could have been destroyed
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
        // Set new velocity (see getNewTangentialSpeed(...))
        b2body.setLinearVelocity(velocity);

        /* Update this Sprite to correspond with the position of the Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        TextureRegion region = (TextureRegion) bossThreeWalkAnimation.getKeyFrame(stateBossTime);
        setRegionFlip(region);
        setRegion(region);
        stateBossTime += dt;

        velocity.set(getNewTangentialSpeed(dt));

        // New random state
        currentStateBoss = getNewRandomState(dt);
    }

    private void setRegionFlip(TextureRegion region) {
        float heroX = screen.getCreator().getHero().getB2body().getPosition().x;
        float pivot = b2body.getPosition().x;

        if (heroX <= pivot && !region.isFlipX()) {
            region.flip(true, false);
        }
        if (heroX > pivot && region.isFlipX()) {
            region.flip(true, false);
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
         * Here, the negative sign indicates counterclockwise movement.
         * We play with counterclockwise, tmp.set(x, y) and tmp.set(y, x) to decorate this movement.
         */

        float w;
        if (movingHorizontal) {
            if (elapsedTime >= HORIZONTAL_PERIOD_SECONDS) {
                elapsedTime = 0;
                horizontalCounterclockwise = MathUtils.randomBoolean();
                movingHorizontal = MathUtils.randomBoolean();
            }
            w = 2 * MathUtils.PI / HORIZONTAL_PERIOD_SECONDS;
            tmp.set((horizontalCounterclockwise ? -1 : 1) * HORIZONTAL_RADIUS_METERS * w * MathUtils.sin(w * elapsedTime), HORIZONTAL_RADIUS_METERS * w * MathUtils.cos(w * elapsedTime));
        } else {
            if (elapsedTime >= VERTICAL_PERIOD_SECONDS) {
                elapsedTime = 0;
                verticalCounterclockwise = MathUtils.randomBoolean();
                movingHorizontal = MathUtils.randomBoolean();
            }
            w = 2 * MathUtils.PI / VERTICAL_PERIOD_SECONDS;
            tmp.set(VERTICAL_RADIUS_METERS * w * MathUtils.cos(w * elapsedTime), (verticalCounterclockwise ? -1 : 1) * VERTICAL_RADIUS_METERS * w * MathUtils.sin(w * elapsedTime));
        }
        elapsedTime += dt;
        return tmp;
    }

    private void stateIdle(float dt) {
        // Stop
        b2body.setLinearVelocity(0.0f, 0.0f);

        /* Update this Sprite to correspond with the position of the Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        TextureRegion region = (TextureRegion) bossThreeIdleAnimation.getKeyFrame(stateBossTime);
        setRegionFlip(region);
        setRegion(region);
        stateBossTime += dt;

        // New random state
        currentStateBoss = getNewRandomState(dt);
    }

    private void stateShooting(float dt) {
        // Stop
        b2body.setLinearVelocity(0.0f, 0.0f);

        /* Update this Sprite to correspond with the position of v Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        TextureRegion region = (TextureRegion) bossThreeShootAnimation.getKeyFrame(stateBossTime);
        setRegionFlip(region);
        setRegion(region);
        stateBossTime += dt;

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

            setRegion((TextureRegion) bossThreeDeathAnimation.getKeyFrame(stateBossTime, true));
            stateBossTime += dt;

            // Apply previous flip state
            setFlip(isFlipX, isFlipY);
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
        // If the boss is not walking nor shooting, he becomes weak
        if (currentStateBoss != StateBoss.WALKING && currentStateBoss != StateBoss.SHOOTING) {
            powerFXStateTime = 0;
            currentPowerState = PowerState.NORMAL;
            AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getBossPowerDown());
        } else {
            // Animation
            powerFXSprite.setRegion((TextureRegion) powerFXAnimation.getKeyFrame(powerFXStateTime, true));
            powerFXStateTime += dt;

            // Update this Sprite to correspond with the position of the BossThree's Box2D body
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
        return Assets.getInstance().getScene2d().getHelpBossThree();
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
        // We draw BossThree in these states: WALKING IDLE SHOOTING INJURED DYING
        if (isDrawable()) {
            drawPowers(batch);
            super.draw(batch);
        } else {
            drawFxs(batch);
        }
    }

    @Override
    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
    }
}
