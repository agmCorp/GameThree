package uy.com.agm.gamethree.actors.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamethree.actors.backgroundObjects.kinematicObjects.Edge;
import uy.com.agm.gamethree.actors.weapons.IShootStrategy;
import uy.com.agm.gamethree.actors.weapons.ShootContext;
import uy.com.agm.gamethree.actors.weapons.hero.HeroDefaultShooting;
import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.sprites.AssetHero;
import uy.com.agm.gamethree.game.GameSettings;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.Landing;
import uy.com.agm.gamethree.tools.Vector2Util;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 12/3/2017.
 */

public class Hero extends Sprite {
    private static final String TAG = Hero.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float CIRCLE_SHAPE_RADIUS_METERS = 32.0f / PlayScreen.PPM;
    public static final float LINEAR_VELOCITY = 5.2f;
    public static final int LIVES_START = 30;
    private static final float DEATH_LINEAR_VELOCITY = 5.0f;
    private static final float PLAY_AGAIN_WARM_UP_TIME = 2.0f;
    private static final float SPRITE_BLINKING_INTERVAL_SECONDS = 0.1f;
    private static final float GAME_OVER_DELAY_SECONDS = 5.0f;
    private static final float PLAY_AGAIN_DELAY_SECONDS = 4.0f;
    private static final float SENSOR_HEIGHT_METERS = 0.1f; // The thinner the better
    private static final float SENSOR_OFFSET_METERS = 0.1f;

    private enum HeroState {
        STANDING, MOVING_UP, MOVING_DOWN, MOVING_LEFT_RIGHT, DYING_UP, DYING_DOWN, DEAD
    }

    private World world;
    private PlayScreen screen;
    private Body b2body;

    // Hero
    private HeroState currentHeroState;
    private HeroState previousHeroState;
    private TextureRegion heroStandUp;
    private Animation heroMovingUpAnimation;
    private Animation heroMovingDownAnimation;
    private Animation heroMovingLeftRightAnimation;
    private Animation heroDeadAnimation;
    private float heroStateTime;
    private boolean applyNewFilters;
    private float playAgainTime;
    private float gameOverTime;
    private float setDefaultFilterTime;
    private boolean isPlayingAgain;
    private boolean shootingEnabled;
    private int lives;

    // Silver bullets
    private int silverBullets;
    private boolean silverBulletEnabled;

    // Ability power FX
    private Animation abilityPowerFXAnimation;
    private float abilityPowerFXStateTime;
    private Sprite abilityPowerFXSprite;
    private boolean abilityPowerFXAllowRotation;

    // Shooting strategy
    private ShootContext shootContext;
    private IShootStrategy heroDefaultShooting;

    // Indicates type of power
    private boolean abilityPower;
    private boolean weaponPower;

    // Blink
    private float blinkingTime;
    private boolean alpha;

    // Temporary GC friendly vector
    private Vector2 tmp;

    // Landing helper
    private Landing landing;

    public Hero(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;

        /* Set this Sprite's bounds on the lower left vertex of a Rectangle.
        * This point will be used by defineHero() calling getX(), getY() to center its b2body.
        * SetBounds always receives world coordinates.
        */
        setBounds(x, y, AssetHero.WIDTH_METERS, AssetHero.HEIGHT_METERS);
        defineHero();

        // Place origin of rotation in the center of the Sprite
        setOriginCenter();

        // Hero variables initialization
        currentHeroState = HeroState.STANDING;
        previousHeroState = HeroState.STANDING;
        heroStandUp = Assets.getInstance().getHero().getHeroStandUp();
        heroMovingUpAnimation = Assets.getInstance().getHero().getHeroMovingUpAnimation();
        heroMovingDownAnimation = Assets.getInstance().getHero().getHeroMovingDownAnimation();
        heroMovingLeftRightAnimation = Assets.getInstance().getHero().getHeroMovingLeftRightAnimation();
        heroDeadAnimation = Assets.getInstance().getHero().getHeroDeadAnimation();
        heroStateTime = 0;
        applyNewFilters = false;
        playAgainTime = 0;
        gameOverTime = 0;
        setDefaultFilterTime = 0;
        isPlayingAgain = false;
        shootingEnabled = true;
        lives = LIVES_START;

        // SilverBullets variables initialization
        silverBullets = 0;
        silverBulletEnabled = false;

        // Ability power FX variables initialization (we don't know which ability power will be yet)
        abilityPowerFXAnimation = null;
        abilityPowerFXStateTime = 0;
        abilityPowerFXSprite = null;
        abilityPowerFXAllowRotation = false;

        // Power state initialization
        abilityPower = false;
        weaponPower = false;

        // Shooting strategy initialization
        heroDefaultShooting = new HeroDefaultShooting(screen);
        shootContext = new ShootContext(heroDefaultShooting);

        // Temporary GC friendly vector
        tmp = new Vector2();

        // Blink
        blinkingTime = 0;
        alpha = false;

        // Landing helper
        landing = new Landing(screen);
    }

    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
    }

    public void update(float dt) {
        if (!isDead()) {
            // Time is up : too late our Hero dies T_T
            checkLevelTimeUp();

            // Have escaped too many enemies, Hero dies
            checkSkulls();

            // If Hero is playing again, set his default filter after a few seconds.
            // Hero can collide with powerBoxes, borders, edges, paths and obstacles after reviving, so at this moment he
            // could have died crushed.
            // If Hero is dead we don't set default filters.
            timeToSetDefaultFilter(dt);
        }

        switch (currentHeroState) {
            case STANDING:
                heroPowerState(dt);
                heroStateStanding(dt);
                break;
            case MOVING_UP:
                heroPowerState(dt);
                heroStateMovingUp(dt);
                break;
            case MOVING_DOWN:
                heroPowerState(dt);
                heroStateMovingDown(dt);
                break;
            case MOVING_LEFT_RIGHT:
                heroPowerState(dt);
                heroStateMovingLeftRight(dt);
                break;
            case DYING_UP:
                heroStateDyingUp(dt);
                break;
            case DYING_DOWN:
                heroStateDyingDown(dt);
                break;
            case DEAD:
                heroStateDead(dt);
                break;
            default:
                break;
        }

        // Shoot time!
        shootContext.update(dt);
        openFireAutomatic();
    }

    private void heroPowerState(float dt) {
        if (abilityPower) {
            powerStateAbilityPower(dt);
        }

        if (weaponPower) {
            powerStateWeaponPower(dt);
        }
    }

    private void openFireAutomatic() {
        if (!GameSettings.getInstance().isManualShooting() && !isSilverBulletEnabled()) {
            if (!isDead() && !screen.getFinalEnemy().isDestroyed()) {
                openFire();
            }
        }
    }

    private void powerStateAbilityPower(float dt) {
        abilityPowerFXSprite.setRegion((TextureRegion) abilityPowerFXAnimation.getKeyFrame(abilityPowerFXStateTime, true));
        abilityPowerFXStateTime += dt;

        // Update our Sprite to correspond with the position of our Hero's Box2D body
        abilityPowerFXSprite.setPosition(b2body.getPosition().x - abilityPowerFXSprite.getWidth() / 2, b2body.getPosition().y - abilityPowerFXSprite.getHeight() / 2);

        // Apply rotation of the main character
        if (abilityPowerFXAllowRotation) {
            abilityPowerFXSprite.setRotation(getRotation());
        }

        // When Hero's ability power is running out, power FX blinks
        if (screen.getHud().isAbilityPowerRunningOut()) {
            activateBlink(dt, abilityPowerFXSprite);
        }

        if (screen.getHud().isAbilityPowerTimeUp()) {
            abilityPowerDown();
            AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getAbilityPowerDown());
        }
    }

    private void powerStateWeaponPower(float dt) {
        if (screen.getHud().isWeaponPowerTimeUp()) {
            weaponPowerDown();
            AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getWeaponPowerDown());
        }
    }

    private void powersDown() {
        abilityPowerDown();
        weaponPowerDown();
    }

    public void abilityPowerDown() {
        setDefaultFixtureFilter();
        deactivateBlink(abilityPowerFXSprite);
        abilityPowerFXSprite = null;
        abilityPowerFXStateTime = 0;
        abilityPower = false;
    }

    public void weaponPowerDown() {
        shootContext.setStrategy(heroDefaultShooting);
        weaponPower = false;
    }

    private void heroStateStanding(float dt) {
        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, Hero may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(heroStandUp);

        // Reset rotation
        setRotation(0.0f);
    }

    private void heroStateMovingLeftRight(float dt) {
       /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, Hero may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        // if the current state is the same as the previous state increase the state time.
        // otherwise the state has changed and we need to reset time.
        heroStateTime = currentHeroState == previousHeroState ? heroStateTime + dt : 0;

        // Update previous state
        previousHeroState = currentHeroState;

        // Update Hero with the correct frame
        setRegion((TextureRegion) heroMovingLeftRightAnimation.getKeyFrame(heroStateTime, true));

        // Reset rotation
        setRotation(0.0f);
    }

    private void heroStateMovingUp(float dt) {
       /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, Hero may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        // if the current state is the same as the previous state increase the state time.
        // otherwise the state has changed and we need to reset time.
        heroStateTime = currentHeroState == previousHeroState ? heroStateTime + dt : 0;

        // Update previous state
        previousHeroState = currentHeroState;

        // Update Hero with the correct frame
        setRegion((TextureRegion) heroMovingUpAnimation.getKeyFrame(heroStateTime, true));

        // Calculate rotation
        setRotationAngle();
    }

    private void heroStateMovingDown(float dt) {
       /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, Hero may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        // if the current state is the same as the previous state increase the state time.
        // otherwise the state has changed and we need to reset time.
        heroStateTime = currentHeroState == previousHeroState ? heroStateTime + dt : 0;

        // Update previous state
        previousHeroState = currentHeroState;

        // Update Hero with the correct frame
        setRegion((TextureRegion) heroMovingDownAnimation.getKeyFrame(heroStateTime, true));

        // Calculate rotation
        setRotationAngle();
    }

    private void heroStateDyingUp(float dt) {
        if (applyNewFilters) {
            // Stop motion
            stop();

            // We take away all his powers and force powerTimeUp
            powersDown();
            screen.getHud().forcePowersTimeUp();

            // Hero can't collide with anything
            Filter filter = new Filter();
            filter.maskBits = WorldContactListener.NOTHING_BIT;

            // We set the previous filter in every fixture
            setFilterData(filter);

            applyNewFilters = false;
        }

       /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, Hero may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) heroDeadAnimation.getKeyFrame(heroStateTime, true));
        heroStateTime += dt;

        // We reach the center of the screen
        if (b2body.getPosition().y >= screen.getGameCam().position.y) {
            // Stop motion
            stop();

            // Start dying down
            heroStateTime = 0;
            currentHeroState = HeroState.DYING_DOWN;
        } else {
            // We move Hero from the actual position to the middle of the screen.
            tmp.set(b2body.getPosition().x, b2body.getPosition().y);
            Vector2Util.goToTarget(tmp, b2body.getPosition().x, screen.getGameCam().position.y, DEATH_LINEAR_VELOCITY);
            b2body.setLinearVelocity(tmp);
        }
    }

    private void heroStateDyingDown(float dt) {
       /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, Hero may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) heroDeadAnimation.getKeyFrame(heroStateTime, true));
        heroStateTime += dt;

        // We move Hero from the actual position to the bottom of the screen.
        tmp.set(b2body.getPosition().x, b2body.getPosition().y);
        Vector2Util.goToTarget(tmp, b2body.getPosition().x, screen.getGameCam().position.y - screen.getGameViewPort().getWorldHeight() / 2 - getHeight(), DEATH_LINEAR_VELOCITY);
        b2body.setLinearVelocity(tmp);

        // If we reach the bottom edge of the screen, we set Hero as not active in the simulation.
        float camBottomEdge = screen.getGameCam().position.y - screen.getGameViewPort().getWorldHeight() / 2;
        float heroUpperEdge = getY() + getHeight();

        // Beyond bottom edge
        if (camBottomEdge > heroUpperEdge) {
            b2body.setActive(false);
            lives--;
            screen.getHud().decreaseLives(1);
            playAgainTime = 0;
            currentHeroState = Hero.HeroState.DEAD;
        }
    }

    private void heroStateDead(float dt) {
        if (lives > 0) {
            playAgainTime += dt;
        } else {
            gameOverTime += dt;
        }
    }

    public void playAgain() {
        // Play music again
        AudioManager.getInstance().resumeMusic();

        // Our Hero can collide with powerBoxes, borders, edges, paths and obstacles only.
        // However, he could die crushed.
        Filter filter = new Filter();
        filter.categoryBits = WorldContactListener.HERO_BIT; // Depicts what this fixture is
        filter.maskBits = WorldContactListener.POWER_BOX_BIT |
                WorldContactListener.BORDER_BIT |
                WorldContactListener.EDGE_BIT |
                WorldContactListener.PATH_BIT |
                WorldContactListener.OBSTACLE_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
        setFilterData(filter);

        setDefaultFilterTime = 0;
        isPlayingAgain = true;

        // Stop motion
        stop();

        // Find an appropriate spot to land
        tmp = landing.land();
        if (tmp.x == -1 && tmp.y == -1) { // There is no such place, so, good luck Hero! (use your blink temporary power)
            tmp.set(screen.getGameCam().position.x, screen.getGameCam().position.y - screen.getGameViewPort().getWorldHeight() / 4);
        }

        // Be careful, we broke the simulation because Hero is teleported on tmp position
        b2body.setTransform(tmp.x, tmp.y, b2body.getAngle());

        // Set Hero active with his initial state
        b2body.setActive(true);
        currentHeroState = HeroState.STANDING;

        screen.getInfoScreen().showAnimation(heroMovingDownAnimation, 70, 70, 10);
    }

    public boolean isWarmingUp() {
        return  isPlayingAgain;
    }

    private void checkLevelTimeUp() {
        if (screen.getHud().isTimeIsUp()) {
            // Audio FX
            AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getTimeIsUp());
            screen.getInfoScreen().showTimeIsUpMessage();
            forceGameOver();
        }
    }

    private void checkSkulls() {
        if (screen.getHud().getSkulls() <= 0) {
            if (!screen.getInfoScreen().isRedFlashVisible()) { // We wait until red flash finishes
                screen.getInfoScreen().showEmptySkullsAnimation();
                forceGameOver();
            }
        }
    }

    private void forceGameOver() {
        lives = 1;
        onDead();
    }

    private void activateBlink(float dt, Sprite sprite) {
        if (sprite != null) {
            blinkingTime += dt;
            if (blinkingTime >= SPRITE_BLINKING_INTERVAL_SECONDS) {
                alpha = !alpha;
                blinkingTime = 0;
            }
            if (alpha) {
                sprite.setAlpha(1.0f);
            } else {
                sprite.setAlpha(0.0f);
            }
        }
    }

    private void deactivateBlink(Sprite sprite) {
        if (sprite != null) {
            sprite.setAlpha(1.0f);
            blinkingTime = 0;
            alpha = false;
        }
    }

    private void timeToSetDefaultFilter(float dt) {
        if (isPlayingAgain) {
            activateBlink(dt, this);

            setDefaultFilterTime += dt;
            if (setDefaultFilterTime > PLAY_AGAIN_WARM_UP_TIME) {
                setDefaultFilter();
                deactivateBlink(this);
                isPlayingAgain = false;
            }
        }
    }

    private void defineHero() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        b2body.setFixedRotation(true);

        setDefaultFixtureFilter();
    }

    private void setDefaultFixtureFilter() {
        setDefaultFixture();
        setDefaultFilter();
    }

    private void setDefaultFixture() {
        /* Removes all the fixtures to create their default versions.
         * If the sensor was partially below the Edge, when the sensor is destroyed the framework (b2body.destroyFixture(sensorFixture))
         * executes WorldContactListener.endContact(...) killing Hero.
         * To avoid this inconvenience, userData is set to null before destroying the sensor.
         * That way, the endContact event can determine if it should validate the position of the sensor (case userData != null)
         * or not validate it (case userData == null).
        */
        Fixture fixture;
        while (b2body.getFixtureList().size > 0) {
            fixture = b2body.getFixtureList().first();
            if (fixture.isSensor()) {
                fixture.setUserData(null);
            }
            b2body.destroyFixture(fixture);
        }

        // Create default (main) fixture
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(CIRCLE_SHAPE_RADIUS_METERS);
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        // Create the sensor
        // Although the sensor has the same categoryBit as the main fixture, this fixture only collides with Edge.
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(SENSOR_OFFSET_METERS, SENSOR_OFFSET_METERS, new Vector2(0, -CIRCLE_SHAPE_RADIUS_METERS - SENSOR_OFFSET_METERS), 0);
        FixtureDef sensor = new FixtureDef();
        sensor.shape = polygonShape;
        sensor.filter.categoryBits = WorldContactListener.HERO_BIT;  // Depicts what this fixture is
        sensor.filter.maskBits = WorldContactListener.EDGE_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
        sensor.isSensor = true;
        b2body.createFixture(sensor).setUserData(this);
    }

    private void setDefaultFilter() {
        Filter filter = new Filter();
        filter.categoryBits = WorldContactListener.HERO_BIT; // Depicts what this fixture is
        filter.maskBits = WorldContactListener.BORDER_BIT |
                WorldContactListener.EDGE_BIT |
                WorldContactListener.OBSTACLE_BIT |
                WorldContactListener.PATH_BIT |
                WorldContactListener.POWER_BOX_BIT |
                WorldContactListener.ITEM_BIT |
                WorldContactListener.ENEMY_BIT |
                WorldContactListener.FINAL_ENEMY_BIT |
                WorldContactListener.ENEMY_WEAPON_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
        setFilterData(filter);
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

    public void setFilterData(Filter filter) {
        for (Fixture fixture : b2body.getFixtureList()) {
            if (!fixture.isSensor()) {
                fixture.setFilterData(filter);
            }
        }
    }

    // Check if Hero should be smashed by a Path, Obstacle or PowerBox.
    public void checkSmashingCollision() {
        if (!isDead()) {
            float sensor = b2body.getPosition().y - CIRCLE_SHAPE_RADIUS_METERS - SENSOR_HEIGHT_METERS / 2; // Center of the sensor
            float edge = screen.getBottomEdge().getB2body().getPosition().y + Edge.HEIGHT_METERS / 2; // Upper edge of the bottom edge

            if (sensor <= edge) {
                onDead();
            }
        }
    }

    public void onDead() {
        /*
         * We must change his b2body to avoid collisions.
         * This can't be done here because this method is called from WorldContactListener that is invoked
         * from PlayScreen.update.world.step(...).
         * No b2body can be changed when the simulation is occurring, we must wait for the next update cycle.
         * Therefore, we use a flag in order to point out this behavior and change it later.
         */
        applyNewFilters = true;

        // Pause music and play sound effect
        AudioManager.getInstance().pauseMusic();
        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getDead());

        // Reset rotation
        setRotation(0.0f);

        // Hero could have died crushed after reviving (see playAgain()), so we must deactivate blinking (ensuring in
        // this way alpha = 1 at the beginning of DYING_UP state).
        deactivateBlink(this);

        // Start dying up
        heroStateTime = 0;
        currentHeroState = HeroState.DYING_UP;
    }

    public void draw(SpriteBatch batch) {
        // Ability power FX
        if (abilityPower) {
            abilityPowerFXSprite.draw(batch);
        }
        // Hero
        super.draw(batch);
    }

    public void disableShooting() {
        shootingEnabled = false;
    }

    public void enableShooting() {
        shootingEnabled = true;
    }

    public boolean isShootingEnabled() {
        return shootingEnabled;
    }

    public void openFire() {
        if (isShootingEnabled()) {
            shootContext.shoot(b2body.getPosition().x, b2body.getPosition().y);
        }
    }

    public void onMovingUp() {
        currentHeroState = HeroState.MOVING_UP;
    }

    public void onMovingDown() {
        currentHeroState = HeroState.MOVING_DOWN;
    }

    public void onMovingLeftRight() {
        currentHeroState = HeroState.MOVING_LEFT_RIGHT;
    }

    public void onStanding() {
        currentHeroState = HeroState.STANDING;
    }

    public boolean isDead() {
        return currentHeroState == HeroState.DEAD ||
                currentHeroState == HeroState.DYING_UP ||
                currentHeroState == HeroState.DYING_DOWN;
    }

    public boolean isGameOver() {
        return currentHeroState == HeroState.DEAD &&
                lives <= 0 &&
                gameOverTime > GAME_OVER_DELAY_SECONDS;
    }

    public boolean isTimeToPlayAgain() {
        return currentHeroState == HeroState.DEAD &&
                lives > 0 &&
                playAgainTime > PLAY_AGAIN_DELAY_SECONDS;
    }

    public Body getB2body() {
        return b2body;
    }

    public void applyAbilityPowerFX(Animation abilityPowerAnimation, Sprite spriteAbilityPower, boolean allowRotation) {
        abilityPower = true;

        // Set the power animation
        abilityPowerFXAnimation = abilityPowerAnimation;

        // Set the sprite (if null, we don't draw it (see .draw(...))
        abilityPowerFXSprite = spriteAbilityPower;

        // Place origin of rotation in the center of the Sprite
        abilityPowerFXSprite.setOriginCenter();

        // Indicates if this Sprite must rotate just like our Hero does
        abilityPowerFXAllowRotation = allowRotation;
    }

    public void applyFirePower(IShootStrategy shootStrategy) {
        weaponPower = true;
        shootContext.setStrategy(shootStrategy);
    }

    public void applySilverBullet() {
        silverBulletEnabled = true;
    }

    public void stop() {
        b2body.setLinearVelocity(0.0f, 0.0f);
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void addLives() {
        lives++;
        screen.getHud().increaseLives(1);
    }

    public void addSilverBullet() {
        silverBullets++;
        screen.getHud().increaseSilverBullets(1);
    }

    public void decreaseSilverBullets() {
        silverBullets--;
        screen.getHud().decreaseSilverBullets(1);
    }

    public boolean hasSilverBullets() {
        return silverBullets > 0;
    }

    public boolean isSilverBulletEnabled() {
        return silverBulletEnabled;
    }
}
