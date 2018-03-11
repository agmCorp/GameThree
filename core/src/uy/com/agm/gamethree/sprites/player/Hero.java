package uy.com.agm.gamethree.sprites.player;

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
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.sprites.AssetHero;
import uy.com.agm.gamethree.game.GameSettings;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.boundary.Edge;
import uy.com.agm.gamethree.sprites.weapons.IShootStrategy;
import uy.com.agm.gamethree.sprites.weapons.ShootContext;
import uy.com.agm.gamethree.sprites.weapons.hero.HeroDefaultShooting;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.Vector2Util;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 12/3/2017.
 */

public class Hero extends Sprite {
    private static final String TAG = Hero.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float LINEAR_VELOCITY = 5.2f;
    public static final float CIRCLE_SHAPE_RADIUS_METERS = 32.0f / PlayScreen.PPM;
    private static final float DEATH_LINEAR_VELOCITY = 5.0f;
    private static final int LIVES_START = 3;
    private static final float PLAY_AGAIN_WARM_UP_TIME = 2.0f;
    private static final float SPRITE_BLINKING_INTERVAL_SECONDS = 0.1f;
    private static final float GAME_OVER_DELAY_SECONDS = 3.0f;
    private static final float PLAY_AGAIN_DELAY_SECONDS = 4.0f;

    private enum HeroState {
        STANDING, MOVING_UP, MOVING_DOWN, MOVING_LEFT_RIGHT, DYING_UP, DYING_DOWN, DEAD
    }

    private enum PowerState {
        NORMAL, POWERFUL
    }

    private World world;
    private PlayScreen screen;
    private Body b2body;

    // Hero
    private HeroState currentHeroState;
    private HeroState previousHeroState;
    private TextureRegion heroStand;
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

    // Power FX
    private PowerState currentPowerState;
    private Animation powerFXAnimation;
    private float powerFXStateTime;
    private Sprite powerFXSprite;
    private boolean powerFXAllowRotation;

    // Shooting strategy
    private ShootContext shootContext;
    private IShootStrategy heroDefaultShooting;

    // Blink
    private float blinkingTime;
    private boolean alpha;

    // Temp GC friendly vector
    private Vector2 tmp;

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
        heroStand = Assets.getInstance().getHero().getHeroStandUp();
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

        // PowerFX variables initialization (we don't know which power will be yet)
        currentPowerState = PowerState.NORMAL;
        powerFXAnimation = null;
        powerFXStateTime = 0;
        powerFXSprite = null;
        powerFXAllowRotation = false;

        // Shooting strategy initialization
        heroDefaultShooting = new HeroDefaultShooting(screen);
        shootContext = new ShootContext(heroDefaultShooting);

        // Temp GC friendly vector
        tmp = new Vector2();

        // Blink
        blinkingTime = 0;
        alpha = false;
    }

    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
    }

    public void update(float dt) {
        // Time is up : too late our Hero dies T_T
        checkLevelTimeUp();

        // If Hero is playing again, set his default filter after a few seconds
        timeToSetDefaultFilter(dt);

        switch (currentHeroState) {
            case STANDING:
                heroStateStanding(dt);
                break;
            case MOVING_UP:
                heroStateMovingUp(dt);
                break;
            case MOVING_DOWN:
                heroStateMovingDown(dt);
                break;
            case MOVING_LEFT_RIGHT:
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

        switch (currentPowerState) {
            case NORMAL:
                break;
            case POWERFUL:
                powerStatePowerful(dt);
                break;
            default:
                break;
        }

        // Shoot time!
        shootContext.update(dt);
        openFireAutomatic();
    }

    private void openFireAutomatic() {
        if (!GameSettings.getInstance().isManualShooting() && !isSilverBulletEnabled()) {
            if (!isHeroDead() && !screen.getFinalEnemy().isDestroyed()) {
                openFire();
            }
        }
    }

    private boolean visualPowerFX() {
        return powerFXSprite != null;
    }

    private void powerStatePowerful(float dt) {
        if (visualPowerFX()) { // if powerFXSprite is null (for instance, Hero has a fire power) we don't need to set any TextureRegion
            powerFXSprite.setRegion((TextureRegion) powerFXAnimation.getKeyFrame(powerFXStateTime, true));
            powerFXStateTime += dt;

            // Update our Sprite to correspond with the position of our Hero's Box2D body
            powerFXSprite.setPosition(b2body.getPosition().x - powerFXSprite.getWidth() / 2, b2body.getPosition().y - powerFXSprite.getHeight() / 2);

            // Apply rotation of the main character
            if (powerFXAllowRotation) {
                powerFXSprite.setRotation(getRotation());
            }

            // When Hero's power is running out, power FX blinks
            if (screen.getHud().isPowerRunningOut()) {
                activateBlink(dt, powerFXSprite);
            }
        }
        if (screen.getHud().isPowerTimeUp()) {
            powerDown();
            AudioManager.getInstance().play(Assets.getInstance().getSounds().getPowerDown());
        }
    }

    public void powerDown() {
        setDefaultFixtureFilter();
        deactivateBlink(powerFXSprite);
        powerFXSprite = null;
        powerFXStateTime = 0;
        shootContext.setStrategy(heroDefaultShooting);
        currentPowerState = PowerState.NORMAL;
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
        setRegion(heroStand);

        // Reset rotation
        setRotation(0.0f);

        // If our Hero is standing, he could be smashed between an object and the bottomEdge when the camera moves dragging him.
        checkCrashing();
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

        // If our Hero is moving to the left or to the right, he could be smashed between an object and the bottomEdge
        // when the camera moves dragging him.
        checkCrashing();
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

        // If our Hero is moving up, he could be smashed between an object and the bottomEdge when the camera moves dragging him.
        checkCrashing();
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

        // If our Hero is moving down, he could be smashed between an object and the bottomEdge when the camera moves dragging him.
        checkCrashing();
    }

    private void heroStateDyingUp(float dt) {
        if (applyNewFilters) {
            // Hero can't collide with anything
            Filter filter = new Filter();
            filter.maskBits = WorldContactListener.NOTHING_BIT;

            // We set the previous filter in every fixture
            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setFilterData(filter);
            }
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
        screen.playLevelMusic();

        // We take away his powers and force powerTimeUp
        powerDown();
        screen.getHud().forcePowerTimeUp();

        // Our Hero can collide with powerBoxes, borders, edges and obstacles only (not with paths)
        Filter filter = new Filter();
        filter.categoryBits = WorldContactListener.HERO_BIT; // Depicts what this fixture is
        filter.maskBits = WorldContactListener.POWERBOX_BIT |
                WorldContactListener.BORDER_BIT |
                WorldContactListener.EDGE_BIT |
                WorldContactListener.OBSTACLE_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
        for (Fixture fixture : b2body.getFixtureList()) {
            fixture.setFilterData(filter);
        }

        setDefaultFilterTime = 0;
        isPlayingAgain = true;

        // Stop motion
        stop();

        // Be careful, we broke the simulation because Hero is teleported.
        b2body.setTransform(screen.getGameCam().position.x, screen.getGameCam().position.y - screen.getGameViewPort().getWorldHeight() / 4, b2body.getAngle());

        // Set Hero active with his initial state
        b2body.setActive(true);
        currentHeroState = HeroState.STANDING;
    }

    private void checkLevelTimeUp() {
        if (screen.getHud().isTimeIsUp() && !isHeroDead()) {
            // Audio FX
            AudioManager.getInstance().play(Assets.getInstance().getSounds().getTimeIsUp());
            screen.getHud().showTimeIsUpMessage();
            lives = 1; // Force game over
            onDead();
        }
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

    public void onDead() {
        // Pause music and play sound effect
        AudioManager.getInstance().pauseMusic();
        AudioManager.getInstance().play(Assets.getInstance().getSounds().getDead());

        /*
         * We must change his b2body to avoid collisions.
         * This can't be done here because this method is called from WorldContactListener that is invoked
         * from PlayScreen.update.world.step(...).
         * No b2body can be changed when the simulation is occurring, we must wait for the next update cycle.
         * Therefore, we use a flag in order to point out this behavior and change it later.
         */
        applyNewFilters = true;

        // Stop motion
        stop();

        // Reset rotation
        setRotation(0.0f);

        // We take away his power effect
        currentPowerState = PowerState.NORMAL;

        // Start dying up
        heroStateTime = 0;
        currentHeroState = HeroState.DYING_UP;
    }

    // if Hero goes beyond the lower limit, he must have been crushed by an object.
    private void checkCrashing() {
        float bottomEdge = screen.getBottomEdge().getB2body().getPosition().y + Edge.HEIGHT_METERS / 2; //  Upper edge of the bottomEdge :)
        float heroUpperEdge = getY() + getHeight();

        // Beyond bottom edge
        if (bottomEdge > heroUpperEdge) {
            onDead();
        }
    }

    private void setDefaultFilter() {
        Filter filter = new Filter();
        filter.categoryBits = WorldContactListener.HERO_BIT; // Depicts what this fixture is
        filter.maskBits = WorldContactListener.BORDER_BIT |
                WorldContactListener.EDGE_BIT |
                WorldContactListener.OBSTACLE_BIT |
                WorldContactListener.PATH_BIT |
                WorldContactListener.POWERBOX_BIT |
                WorldContactListener.ITEM_BIT |
                WorldContactListener.ENEMY_BIT |
                WorldContactListener.FINAL_ENEMY_BIT |
                WorldContactListener.ENEMY_WEAPON_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
        for (Fixture fixture : b2body.getFixtureList()) {
            fixture.setFilterData(filter);
        }
    }

    private void defineHero() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        setDefaultFixtureFilter();
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

    public void draw(SpriteBatch batch) {
        if (currentPowerState != PowerState.NORMAL) {
            if (visualPowerFX()) {
                // Power FX
                powerFXSprite.draw(batch);
            }
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

    public boolean isHeroDead() {
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

    public void applyPowerFX(Animation animation, Sprite power, boolean allowRotation) {
        currentPowerState = PowerState.POWERFUL;

        // Set the animation
        powerFXAnimation = animation;

        // Set the sprite (if null, we don't draw it (see .draw(...))
        powerFXSprite = power;

        // Place origin of rotation in the center of the Sprite
        powerFXSprite.setOriginCenter();

        // Indicates if this Sprite must rotate just like our Hero does
        powerFXAllowRotation = allowRotation;
    }

    public void applyFirePower(IShootStrategy shootStrategy) {
        currentPowerState = PowerState.POWERFUL;
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
