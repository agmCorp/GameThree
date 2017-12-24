package uy.com.agm.gamethree.sprites.player;

import com.badlogic.gdx.Gdx;
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
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.powerup.Items.PowerOne;
import uy.com.agm.gamethree.sprites.weapons.EnergyBall;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.Constants;
import uy.com.agm.gamethree.tools.GameThreeActorDef;

/**
 * Created by AGM on 12/3/2017.
 */

public class Hero extends Sprite {
    private static final String TAG = Hero.class.getName();

    public enum HeroState {
        STANDING, MOVING_UP, MOVING_DOWN, DYING_UP, DYING_DOWN, DEAD
    }
    private enum PowerState {
        NORMAL_MODE, GHOST_MODE
    }

    public World world;
    public PlayScreen screen;
    public Body b2body;

    // Hero
    public HeroState currentHeroState;
    private TextureRegion heroStand;
    private Animation heroMovingUpAnimation;
    private Animation heroMovingDownAnimation;
    private Animation heroDeadAnimation;
    private float heroStateTimer;
    private float gameOverTime;

    // Power FX
    private PowerState currentPowerState;
    private Animation powerFXAnimation;
    private float powerFXStateTimer;
    private Sprite powerFXSprite;

    public Hero(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;

        /* Set this Sprite's bounds on the lower left vertex of a Rectangle.
        * This point will be used by defineHero() calling getX(), getY() to center its b2body.
        * SetBounds always receives world coordinates.
        */
        setBounds(x, y, Constants.HERO_WIDTH_METERS, Constants.HERO_HEIGHT_METERS);
        defineHero();

        // Hero variables initialization
        currentHeroState = HeroState.STANDING;
        heroStand = Assets.instance.hero.heroStand;
        heroMovingUpAnimation = Assets.instance.hero.heroMovingUpAnimation;
        heroMovingDownAnimation = Assets.instance.hero.heroMovingDownAnimation;
        heroDeadAnimation = Assets.instance.hero.heroDeadAnimation;
        heroStateTimer = 0;
        gameOverTime = 0;

        // PowerFX variables initialization
        currentPowerState = PowerState.NORMAL_MODE;
        powerFXAnimation = null; // we don't know yet
        powerFXStateTimer = 0;
        powerFXSprite = new Sprite();
    }

    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
    }

    public void update(float dt) {
        Gdx.app.debug(TAG, "*** ESTADO " + currentHeroState);

        switch (currentHeroState) {
            case STANDING:
                stateHeroStanding();
                break;
            case MOVING_UP:
                stateHeroMovingUp(dt);
                break;
            case MOVING_DOWN:
                stateHeroMovingDown(dt);
                break;
            case DYING_UP:
                stateHeroDyingUp(dt);
                break;
            case DYING_DOWN:
                stateHeroDyingDown(dt);
                break;
            case DEAD:
                stateDead(dt);
                break;
            default:
                break;
        }

        switch (currentPowerState) {
            case NORMAL_MODE:
                break;
            case GHOST_MODE:
                statePowerGhostMode(dt);
                break;
            default:
                break;
        }
   }

   private void statePowerGhostMode(float dt) {
       powerFXSprite.setRegion((TextureRegion) powerFXAnimation.getKeyFrame(powerFXStateTimer, true));
       powerFXStateTimer += dt;

       if (screen.getHud().isPowerTimeUp()) {
           setDefaultFilter();
           currentPowerState = PowerState.NORMAL_MODE;
       }
   }

   private void stateHeroStanding() {
        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, Hero may have collided with sth. and therefore it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(heroStand);

       // If our Hero is standing, he should be dragged when the cam moves
        checkUpperBound();
        checkBottomBound();
    }

    private void stateHeroMovingUp(float dt) {
       /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, Hero may have collided with sth. and therefore it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) heroMovingUpAnimation.getKeyFrame(heroStateTimer, true));
        heroStateTimer += dt;

        checkUpperBound();
    }

    private void stateHeroMovingDown(float dt) {
       /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, Hero may have collided with sth. and therefore it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) heroMovingDownAnimation.getKeyFrame(heroStateTimer, true));
        heroStateTimer += dt;

        checkBottomBound();
    }

    private void stateHeroDyingUp(float dt) {
       /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, Hero may have collided with sth. and therefore it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) heroDeadAnimation.getKeyFrame(heroStateTimer, true));
        heroStateTimer += dt;

        // We reach the center of the screen
        if (b2body.getPosition().y >= screen.gameCam.position.y) {
            // Stop
            b2body.setLinearVelocity(0, 0);

            // Start dying down
            currentHeroState = HeroState.DYING_DOWN;
        } else {
            /* We move Hero from the actual position to the middle of the screen.
             * origin = (b2body.getPosition().x, b2body.getPosition().y)
             * destination = (b2body.getPosition().x, screen.gameCam.position.y)
             *
             * To go from origin to destination we must subtract their position vectors: destination - origin.
             * Get the direction of the previous vector (normalization) and finally apply constant velocity on that direction
            */
            Vector2 newVelocity = new Vector2(0, screen.gameCam.position.y - b2body.getPosition().y);
            newVelocity.nor();
            newVelocity.x = newVelocity.x * Constants.HERO_DEATH_LINEAR_VELOCITY;
            newVelocity.y = newVelocity.y * Constants.HERO_DEATH_LINEAR_VELOCITY;
            b2body.setLinearVelocity(newVelocity);
        }
    }

    private void stateHeroDyingDown(float dt) {
       /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, Hero may have collided with sth. and therefore it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) heroDeadAnimation.getKeyFrame(heroStateTimer, true));
        heroStateTimer += dt;

        /* We move Hero from the actual position to the bottom of the screen.
        * origin = (b2body.getPosition().x, b2body.getPosition().y)
        * destination = (b2body.getPosition().x, screen.gameCam.position.y - screen.gameViewPort.getWorldHeight() / 2 - getHeight())
        *
        * To go from origin to destination we must subtract their position vectors: destination - origin.
        * Get the direction of the previous vector (normalization) and finally apply constant velocity on that direction
        */
        Vector2 newVelocity = new Vector2(0, screen.gameCam.position.y - screen.gameViewPort.getWorldHeight() / 2 - getHeight() - b2body.getPosition().y);
        newVelocity.nor();
        newVelocity.x = newVelocity.x * Constants.HERO_DEATH_LINEAR_VELOCITY;
        newVelocity.y = newVelocity.y * Constants.HERO_DEATH_LINEAR_VELOCITY;
        b2body.setLinearVelocity(newVelocity);

        // If we reach the bottom edge of the screen, we set Hero as not active in the simulation.
        float camBottomEdge = screen.gameCam.position.y - screen.gameViewPort.getWorldHeight() / 2;
        float heroUpperEdge = getY() + getHeight();

        // Beyond bottom edge
        if (camBottomEdge > heroUpperEdge) {
            b2body.setActive(false);
            currentHeroState = Hero.HeroState.DEAD;
        }
    }

    public void stateDead(float dt) {
        gameOverTime += dt;
    }

    public void onDead() {
        AudioManager.instance.stopMusic();
        AudioManager.instance.play(Assets.instance.sounds.dead, 1);

        // Hero can't collide with anything
        Filter filter = new Filter();
        filter.maskBits = Constants.NOTHING_BIT;

        for (Fixture fixture : b2body.getFixtureList()) {
            fixture.setFilterData(filter);
        }

        // Stop
        b2body.setLinearVelocity(0, 0);

        // Start dying up
        currentHeroState = HeroState.DYING_UP;
    }

    // It prevents Hero from going beyond the upper limit of the game
    private void checkUpperBound() {
        float camUpperEdge = screen.gameCam.position.y + screen.gameViewPort.getWorldHeight() / 2;
        float heroUpperEdge = getY() + getHeight();

        // Beyond upper edge
        if (camUpperEdge < heroUpperEdge) {
            // Be carefull, we broke the simulation because Hero is teleporting.
            b2body.setTransform(b2body.getPosition().x, camUpperEdge - getHeight() / 2, b2body.getAngle());
        }
    }

    // It prevents Hero from going beyond the bottom limit of the game
    private void checkBottomBound() {
        float camBottomEdge = screen.gameCam.position.y - screen.gameViewPort.getWorldHeight() / 2;
        float heroBottomEdge = getY();

        // Beyond bottom edge
        if (camBottomEdge > heroBottomEdge) {
            // Be carefull, we broke the simulation because Hero is teleporting.
            b2body.setTransform(b2body.getPosition().x, camBottomEdge + getHeight() / 2, b2body.getAngle());
        }
    }

    private void setDefaultFilter() {
        Filter filter = new Filter();
        filter.categoryBits = Constants.HERO_BIT; // Depicts what this fixture is
        filter.maskBits = Constants.BORDERS_BIT |
                Constants.POWERBOX_BIT |
                Constants.OBSTACLE_BIT |
                Constants.ITEM_BIT |
                Constants.ENEMY_BIT |
                Constants.ENEMY_WEAPON_BIT; // Depicts what can this Fixture collide with (see WorldContactListener)
        for (Fixture fixture : b2body.getFixtureList()) {
            fixture.setFilterData(filter);
        }
    }

    private void defineHero() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(Constants.HERO_CIRCLESHAPE_RADIUS_METERS);
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        setDefaultFilter();
    }

    public void draw(SpriteBatch batch) {
        // Clockwise - If true, the texture coordinates are rotated 90 degrees clockwise. If false, they are rotated 90 degrees counter clockwise.
        // Thus by default (no velocity), our Sprite will be drawn rotated 90 degrees counter clockwise.
        boolean clockwise = true;
        float angle = 90;

        // If we draw our Texture (heroStand) rotated 90 degrees, the newHeight is the width of the Texture (analogous with newWidth).
        float newHeight = getWidth();
        float newWidth = getHeight();

        // If Hero is moving, we must calculate his new angle
        if (b2body.getLinearVelocity().len() > 0.0f) {
            float velAngle = this.b2body.getLinearVelocity().angle();

            if (0 < velAngle && velAngle <= 90) {
                angle = velAngle;
            }
            if (90 < velAngle && velAngle <= 180) {
                angle = 270.0f - velAngle;
            }
            if (180 < velAngle && velAngle <= 270) {
                angle = velAngle;
                clockwise = false;
            }
            if (270 < velAngle && velAngle <= 360) {
                angle = velAngle;
                clockwise = false;
            }
        }

        // Draws a rectangle with the texture coordinates rotated 90 degrees.
        batch.draw(this, this.b2body.getPosition().x - newWidth / 2, this.b2body.getPosition().y - newHeight / 2,
                newWidth / 2, newHeight / 2, newWidth, newHeight, 1.0f, 1.0f, angle, clockwise);

        if (currentPowerState != PowerState.NORMAL_MODE) {
            // We do the same with powerFXSprite
            float w = powerFXSprite.getHeight();
            float h = powerFXSprite.getWidth();

            powerFXSprite.setPosition(this.b2body.getPosition().x - newWidth / 2, this.b2body.getPosition().y - newHeight / 2);
            batch.setColor(1, 1, 1, Constants.POWERONE_FX_ALPHA); // Transparency
            batch.draw(powerFXSprite, this.b2body.getPosition().x - w / 2, this.b2body.getPosition().y - h / 2,
                    w / 2, h / 2, powerFXSprite.getHeight(), powerFXSprite.getWidth(), 1.0f, 1.0f, angle, clockwise);
            batch.setColor(1, 1, 1, 1);
        }
    }

    public void openFire() {
        Vector2 position = new Vector2(b2body.getPosition().x, b2body.getPosition().y + Constants.WEAPON_OFFSET_METERS);
        screen.creator.createGameThreeActor(new GameThreeActorDef(position, EnergyBall.class));
    }

    public void applyPower(Class<?> type) {
        if (type == PowerOne.class) {
            onGhostMode();
        }
    }

    private void onGhostMode() {
        // Show the power's name and its countdown
        screen.getHud().setPowerLabel("GHOST MODE", Constants.TIMER_POWERONE);

        // Hero can't collide with enemies nor bullets
        Filter filter = new Filter();
        filter.categoryBits = Constants.HERO_BIT;
        filter.maskBits = Constants.BORDERS_BIT |
                Constants.POWERBOX_BIT |
                Constants.OBSTACLE_BIT |
                Constants.ITEM_BIT;
        for (Fixture fixture : b2body.getFixtureList()) {
            fixture.setFilterData(filter);
        }

        // Flag
        currentPowerState = PowerState.GHOST_MODE;

        // Set the power's animation
        powerFXAnimation = Assets.instance.ghostMode.ghostModeAnimation;
        Sprite power = new Sprite(Assets.instance.ghostMode.ghostModeStand);

        // Only to set width and height (in draw(...) we set its position)
        power.setBounds(getX(), getY(), Constants.POWERONE_FX_WIDTH_METERS, Constants.POWERONE_FX_HEIGHT_METERS);

        // Set the sprite
        powerFXSprite.set(power);
    }

    public void onMovingUp() {
        currentHeroState = HeroState.MOVING_UP;
    }

    public void onMovingDown() {
        currentHeroState = HeroState.MOVING_DOWN;
    }

    public void onStanding() {
        currentHeroState = HeroState.STANDING;
    }

    public float getStateTimer() {
        return heroStateTimer;
    }

    public boolean isHeroDead() {
        return  currentHeroState == HeroState.DEAD ||
                currentHeroState == HeroState.DYING_UP ||
                currentHeroState == HeroState.DYING_DOWN;
    }

    public boolean isGameOver() {
        return currentHeroState == HeroState.DEAD && gameOverTime > 3.0;
    }
}
