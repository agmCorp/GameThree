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
import uy.com.agm.gamethree.sprites.weapons.HeroBullet;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.game.Constants;
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
    private boolean powerFXAllowRotation;

    // Fire power
    private boolean fireEnhancement;
    private float bulletWidth;
    private float bulletHeight;
    private float bulletCircleShapeRadius;
    private float fireDelay;
    private int numberBullets;
    private Animation bulletAnimation;

    // Temp GC friendly vector
    private Vector2 tmp;

    public Hero(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;

        /* Set this Sprite's bounds on the lower left vertex of a Rectangle.
        * This point will be used by defineHero() calling getX(), getY() to center its b2body.
        * SetBounds always receives world coordinates.
        */
        setBounds(x, y, Constants.HERO_WIDTH_METERS, Constants.HERO_HEIGHT_METERS);
        defineHero();

        // Place origin of rotation in the center of the Sprite
        setOriginCenter();

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
        powerFXSprite = null;
        powerFXAllowRotation = false;

        // Fire power variables initialization (we don't know yet which fire power will be)
        fireEnhancement = false;
        bulletWidth = 0;
        bulletHeight = 0;
        fireDelay = 0;
        numberBullets = 0;
        bulletAnimation = null;

        // Temp GC friendly vector
        tmp = new Vector2();
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

    private boolean visualPowerFX() {
        return powerFXSprite != null;
    }

    private void powerStatePowerful(float dt) {
        if (visualPowerFX()) { // if powerFXSprite is null (for instance, Hero has a fire power) we don't need to set any TextureRegion
            powerFXSprite.setRegion((TextureRegion) powerFXAnimation.getKeyFrame(powerFXStateTimer, true));
            powerFXStateTimer += dt;

            // Update our Sprite to correspond with the position of our Hero's Box2D body
            powerFXSprite.setPosition(b2body.getPosition().x - powerFXSprite.getWidth() / 2, b2body.getPosition().y - powerFXSprite.getHeight() / 2);

            // Apply rotation of the main character
            if (powerFXAllowRotation) {
                powerFXSprite.setRotation(getRotation());
            }
        }
        if (screen.getHud().isPowerTimeUp()) {
            powerDown();
            AudioManager.instance.play(Assets.instance.sounds.powerDown, 1);
        }
    }

    public void powerDown() {
        setDefaultFixtureFilter();
        powerFXSprite = null;
        powerFXStateTimer = 0;
        fireEnhancement = false;
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

        // if the current state is the same as the previous state increase the state timer.
        // otherwise the state has changed and we need to reset timer.
        heroStateTimer = currentHeroState == previousHeroState ? heroStateTimer + dt : 0;

        // Update previous state
        previousHeroState = currentHeroState;

        // Update Hero with the correct frame
        setRegion((TextureRegion) heroMovingLeftRightAnimation.getKeyFrame(heroStateTimer, true));

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

        // if the current state is the same as the previous state increase the state timer.
        // otherwise the state has changed and we need to reset timer.
        heroStateTimer = currentHeroState == previousHeroState ? heroStateTimer + dt : 0;

        // Update previous state
        previousHeroState = currentHeroState;

        // Update Hero with the correct frame
        setRegion((TextureRegion) heroMovingUpAnimation.getKeyFrame(heroStateTimer, true));

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

        // if the current state is the same as the previous state increase the state timer.
        // otherwise the state has changed and we need to reset timer.
        heroStateTimer = currentHeroState == previousHeroState ? heroStateTimer + dt : 0;

        // Update previous state
        previousHeroState = currentHeroState;

        // Update Hero with the correct frame
        setRegion((TextureRegion) heroMovingDownAnimation.getKeyFrame(heroStateTimer, true));

        // Calculate rotation
        setRotationAngle();

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
            // Stop motion
            stop();

            // Start dying down
            heroStateTimer = 0;
            currentHeroState = HeroState.DYING_DOWN;
        } else {
            // We move Hero from the actual position to the middle of the screen.
            tmp.set(b2body.getPosition().x, b2body.getPosition().y);
            Vector2Util.goToTarget(tmp, b2body.getPosition().x, screen.gameCam.position.y, Constants.HERO_DEATH_LINEAR_VELOCITY);
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
        setRegion((TextureRegion) heroDeadAnimation.getKeyFrame(heroStateTimer, true));
        heroStateTimer += dt;

        // We move Hero from the actual position to the bottom of the screen.
        tmp.set(b2body.getPosition().x, b2body.getPosition().y);
        Vector2Util.goToTarget(tmp, b2body.getPosition().x, screen.gameCam.position.y - screen.gameViewPort.getWorldHeight() / 2 - getHeight(), Constants.HERO_DEATH_LINEAR_VELOCITY);
        b2body.setLinearVelocity(tmp);

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

        // We set the previous filter in every fixture because a power could have added one
        for (Fixture fixture : b2body.getFixtureList()) {
            fixture.setFilterData(filter);
        }

        // Stop motion
        stop();

        // Reset rotation
        setRotation(0.0f);

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
        shape.setRadius(Constants.HERO_CIRCLESHAPE_RADIUS_METERS);
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

    public void openFire() {
        if (fireEnhancement) {
            if (openFiretimer > fireDelay) {
                float directionDegrees = 180.0f / (numberBullets + 1);
                float angle;
                for(int i = 1; i <= numberBullets; i++) {
                    angle = directionDegrees * i;
                    angle = (angle >= 90.0f) ? angle - 90.0f : 270.0f + angle;
                    screen.getCreator().createGameThreeActor(new GameThreeActorDef(b2body.getPosition().x,
                                                                    b2body.getPosition().y + Constants.WEAPON_OFFSET_METERS,
                                                                    bulletWidth,
                                                                    bulletHeight,
                                                                    bulletCircleShapeRadius,
                                                                    angle,
                                                                    bulletAnimation,
                                                                    HeroBullet.class));
                }
                openFiretimer = 0;
            }
        } else {
            if (openFiretimer > Constants.HERO_FIRE_DELAY_SECONDS) {
                screen.getCreator().createGameThreeActor(new GameThreeActorDef(b2body.getPosition().x, b2body.getPosition().y + Constants.WEAPON_OFFSET_METERS, HeroBullet.class));
                openFiretimer = 0;
            }
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

    public void applyFirePower(float width, float height, float circleShapeRadius, float delay, int bullets, Animation animation) {
        currentPowerState = PowerState.POWERFUL;

        fireEnhancement = true;
        bulletWidth = width;
        bulletHeight = height;
        bulletCircleShapeRadius = circleShapeRadius;
        fireDelay = delay;
        numberBullets = bullets;
        bulletAnimation = animation;
    }

    public void stop() {
        b2body.setLinearVelocity(0.0f, 0.0f);
    }
}
