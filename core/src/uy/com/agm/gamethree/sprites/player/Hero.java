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
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.weapons.EnergyBall;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.Constants;
import uy.com.agm.gamethree.tools.GameThreeActorDef;
import uy.com.agm.gamethree.tools.Vector2Util;

/**
 * Created by AGM on 12/3/2017.
 */

public class Hero extends Sprite {
    private static final String TAG = Hero.class.getName();

    public enum HeroState {
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
    private float heroStateTimer;
    private float gameOverTimer;
    private float openFiretimer;

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
        previousHeroState = HeroState.STANDING;
        heroStand = Assets.instance.hero.heroStand;
        heroMovingUpAnimation = Assets.instance.hero.heroMovingUpAnimation;
        heroMovingDownAnimation = Assets.instance.hero.heroMovingDownAnimation;
        heroMovingLeftRightAnimation = Assets.instance.hero.heroMovingLeftRightAnimation;
        heroDeadAnimation = Assets.instance.hero.heroDeadAnimation;
        heroStateTimer = 0;
        gameOverTimer = 0;
        openFiretimer = 0;

        // PowerFX variables initialization (we don't know yet which power will be)
        currentPowerState = PowerState.NORMAL;
        powerFXAnimation = null;
        powerFXStateTimer = 0;
        powerFXSprite = new Sprite();
    }

    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
    }

    public void update(float dt) {
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

        // Shoot timer
        openFiretimer += dt;
    }

    private void powerStatePowerful(float dt) {
        powerFXSprite.setRegion((TextureRegion) powerFXAnimation.getKeyFrame(powerFXStateTimer, true));
        powerFXStateTimer += dt;

        if (screen.getHud().isPowerTimeUp()) {
            setDefaultFilter();
            powerFXStateTimer = 0;
            currentPowerState = PowerState.NORMAL;
            AudioManager.instance.play(Assets.instance.sounds.powerDown, 1);
        }
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

        // if the current state is the same as the previous state increase the state timer.
        // otherwise the state has changed and we need to reset timer.
        heroStateTimer = currentHeroState == previousHeroState ? heroStateTimer + dt : 0;

        // Update previous state
        previousHeroState = currentHeroState;

        // Update Hero with the correct frame
        setRegion((TextureRegion) heroMovingLeftRightAnimation.getKeyFrame(heroStateTimer, true));

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

        // if the current state is the same as the previous state increase the state timer.
        // otherwise the state has changed and we need to reset timer.
        heroStateTimer = currentHeroState == previousHeroState ? heroStateTimer + dt : 0;

        // Update previous state
        previousHeroState = currentHeroState;

        // Update Hero with the correct frame
        setRegion((TextureRegion) heroMovingUpAnimation.getKeyFrame(heroStateTimer, true));

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

        // if the current state is the same as the previous state increase the state timer.
        // otherwise the state has changed and we need to reset timer.
        heroStateTimer = currentHeroState == previousHeroState ? heroStateTimer + dt : 0;

        // Update previous state
        previousHeroState = currentHeroState;

        // Update Hero with the correct frame
        setRegion((TextureRegion) heroMovingDownAnimation.getKeyFrame(heroStateTimer, true));

        // If our Hero is moving down, he could be smashed between an object and the bottomEdge when the camera moves dragging him.
        checkCrashing();
    }

    private void heroStateDyingUp(float dt) {
       /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, Hero may have collided with sth., and therefore, it has a new position after running the physical simulation.
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
            heroStateTimer = 0;
            currentHeroState = HeroState.DYING_DOWN;
        } else {
            // We move Hero from the actual position to the middle of the screen.
            Vector2 newVelocity = new Vector2(b2body.getPosition().x, b2body.getPosition().y);
            Vector2Util.goToTarget(newVelocity, b2body.getPosition().x, screen.gameCam.position.y, Constants.HERO_DEATH_LINEAR_VELOCITY);
            b2body.setLinearVelocity(newVelocity);
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
        setRegion((TextureRegion) heroDeadAnimation.getKeyFrame(heroStateTimer, true));
        heroStateTimer += dt;

        // We move Hero from the actual position to the bottom of the screen.
        Vector2 newVelocity = new Vector2(b2body.getPosition().x, b2body.getPosition().y);
        Vector2Util.goToTarget(newVelocity, b2body.getPosition().x, screen.gameCam.position.y - screen.gameViewPort.getWorldHeight() / 2 - getHeight(), Constants.HERO_DEATH_LINEAR_VELOCITY);
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

    private void heroStateDead(float dt) {
        gameOverTimer += dt;
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

        // We take away his powers
        currentPowerState = PowerState.NORMAL;

        // Start dying up
        heroStateTimer = 0;
        currentHeroState = HeroState.DYING_UP;
    }

    // if Hero goes beyond the lower limit, he must have been crushed by an object.
    private void checkCrashing() {
        float bottomEdge = screen.getBottomEdge().getB2body().getPosition().y + Constants.EDGE_HEIGHT_METERS / 2; //  Upper edge of the bottomEdge :)
        float heroUpperEdge = getY() + getHeight();

        // Beyond bottom edge
        if (bottomEdge > heroUpperEdge) {
            onDead();
        }
    }

    private void setDefaultFilter() {
        Filter filter = new Filter();
        filter.categoryBits = Constants.HERO_BIT; // Depicts what this fixture is
        filter.maskBits = Constants.BORDERS_BIT |
                Constants.EDGES_BIT |
                Constants.OBSTACLE_BIT |
                Constants.POWERBOX_BIT |
                Constants.ITEM_BIT |
                Constants.ENEMY_BIT |
                Constants.FINAL_ENEMY_LEVEL_ONE_BIT |
                Constants.ENEMY_WEAPON_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
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
        // Thus, by default (no velocity our Sprite will be drawn rotated 90 degrees counter clockwise.
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

        if (currentPowerState != PowerState.NORMAL) {
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
        if (openFiretimer > Constants.HERO_FIRE_DELAY_SECONDS) {
            screen.getCreator().createGameThreeActor(new GameThreeActorDef(b2body.getPosition().x, b2body.getPosition().y + Constants.WEAPON_OFFSET_METERS, EnergyBall.class));
            openFiretimer = 0;
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

    public float getStateTimer() {
        return heroStateTimer;
    }

    public boolean isHeroDead() {
        return currentHeroState == HeroState.DEAD ||
                currentHeroState == HeroState.DYING_UP ||
                currentHeroState == HeroState.DYING_DOWN;
    }

    public boolean isGameOver() {
        return currentHeroState == HeroState.DEAD && gameOverTimer > Constants.GAME_OVER_DELAY_SECONDS;
    }

    public Body getB2body() {
        return b2body;
    }

    public void applyPower(Animation animation, Sprite power) {
        currentPowerState = PowerState.POWERFUL;

        // Set the animation
        powerFXAnimation = animation;

        // Set the sprite
        powerFXSprite.set(power);
    }
}
