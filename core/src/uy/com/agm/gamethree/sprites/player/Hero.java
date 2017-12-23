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
        STANDING, MOVING_UP, MOVING_DOWN, DEAD
    }
    private enum PowerState {
        NORMAL_MODE, GHOST_MODE
    }

    public World world;
    public PlayScreen screen;
    public Body b2body;

    // Hero
    public HeroState currentHeroState;
    private HeroState previousHeroState;
    private TextureRegion heroStand;
    private Animation heroMovingUpAnimation;
    private Animation heroMovingDownAnimation;
    private Animation heroDeadAnimation;
    private float heroStateTimer;
    private boolean heroIsDead;
    private boolean dyingDown;

    // Power FX
    private PowerState currentPowerState;
    private PowerState previousPowerState;
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
        heroDeadAnimation = Assets.instance.hero.heroDeadAnimation;
        heroStateTimer = 0;
        heroIsDead = false;
        dyingDown = false;

        // PowerFX variables initialization
        currentPowerState = PowerState.NORMAL_MODE;
        previousPowerState = PowerState.NORMAL_MODE;
        powerFXAnimation = null; // we don't know yet
        powerFXStateTimer = 0;
        powerFXSprite = new Sprite();
    }

    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
    }

    public void update(float dt) {
        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, Hero may have collided with sth. and therefore it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        // Update Hero with the correct frame depending on Hero's current action
        setRegion(getHeroFrame(dt));

        if (!heroIsDead) {
            // It prevents Hero from going beyond the limits of the game
            checkBoundaries();

            // Analyze the power state and update it (for instance if the power has run out...)
            updatePowerState();

            // If he is still using a power, update the special effect with the correct frame
            if (currentPowerState != PowerState.NORMAL_MODE) {
                powerFXSprite.setRegion(getPowerFXFrame(dt));
            }
        } else {
            deathMovement();
        }
    }

    private void deathMovement() {
        b2body.setLinearVelocity(0, 0);
        if (!dyingDown) {
            b2body.applyLinearImpulse(new Vector2(0.0f, 10.0f), b2body.getWorldCenter(), true);
            // Center of the screen
            if (b2body.getPosition().y >= screen.gameCam.position.y) {
                dyingDown = true;
            }
        } else {
            b2body.applyLinearImpulse(new Vector2(0, -10.0f), b2body.getWorldCenter(), true);
        }
    }

    private void checkBoundaries() {
        float camUpperEdge = screen.gameCam.position.y + screen.gameViewPort.getWorldHeight() / 2;
        float camBottomEdge = screen.gameCam.position.y - screen.gameViewPort.getWorldHeight() / 2;
        float heroUpperEdge = getY() + getHeight();
        float heroBottomEdge = getY();

        // Beyond bottom edge
        if (camBottomEdge > heroBottomEdge) {
            // Be carefull, we broke the simulation because Hero is teleporting.
            b2body.setTransform(b2body.getPosition().x, camBottomEdge + getHeight() / 2, b2body.getAngle());
        }

        // Beyond upper edge
        if (camUpperEdge < heroUpperEdge) {
            // Be carefull, we broke the simulation because Hero is teleporting.
            b2body.setTransform(b2body.getPosition().x, camUpperEdge - getHeight() / 2, b2body.getAngle());
        }
    }

    private TextureRegion getHeroFrame(float dt) {
        // Get Hero's current state. ie. SANDING, MOVING_DOWN...
        currentHeroState = getHeroState();
        TextureRegion region;
        // Depending on the state, get corresponding animation keyFrame.
        switch (currentHeroState) {
            case STANDING:
                region = heroStand;
                break;
            case MOVING_DOWN:
                region = (TextureRegion) heroMovingDownAnimation.getKeyFrame(heroStateTimer, true);
                break;
            case MOVING_UP:
                region = (TextureRegion) heroMovingUpAnimation.getKeyFrame(heroStateTimer, true);
                break;
            case DEAD:
                region = (TextureRegion) heroDeadAnimation.getKeyFrame(heroStateTimer, true);
                break;
            default:
                region = heroStand;
                break;
        }
        Gdx.app.debug(TAG, "ESTADO!!! " + currentHeroState);

        // if the current state is the same as the previous state increase the state timer.
        // otherwise the state has changed and we need to reset timer.
        heroStateTimer = currentHeroState == previousHeroState ? heroStateTimer + dt : 0;

        // Update previous state
        previousHeroState = currentHeroState;

        // Return our final adjusted frame
        return region;
    }

    private HeroState getHeroState() {
        HeroState heroState;
        if (!heroIsDead) {
            // Test to Box2D for velocity on the y-axis.
            // If Hero is going positive in y-axis he is moving up.
            // If Hero is going negative in y-axis he is moving down.
            // Otherwise he is standing.
            float y = b2body.getLinearVelocity().y;
            if (y > 0) {
                heroState = HeroState.MOVING_UP;
            } else if (y < 0) {
                heroState = HeroState.MOVING_DOWN;
            } else {
                heroState = HeroState.STANDING;
            }
        } else {
            heroState = HeroState.DEAD;
        }
        return heroState;
    }

    private TextureRegion getPowerFXFrame(float dt) {
        TextureRegion region = null;

        // Depending on the state, get corresponding animation keyFrame.
        switch (currentPowerState) {
            case GHOST_MODE:
                region = (TextureRegion) powerFXAnimation.getKeyFrame(powerFXStateTimer, true);
                break;
            default:
                break;
        }

        // if the current state is the same as the previous state increase the state timer.
        // otherwise the state has changed and we need to reset timer.
        powerFXStateTimer = currentPowerState == previousPowerState ? powerFXStateTimer + dt : 0;

        // Update previous state
        previousPowerState = currentPowerState;

        // Return our final adjusted frame
        return region;
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

    public void onDead() {
        heroIsDead = true;
        AudioManager.instance.stopMusic();
        AudioManager.instance.play(Assets.instance.sounds.dead, 1);

        // Hero can't collide with anything
        Filter filter = new Filter();
        filter.maskBits = Constants.NOTHING_BIT;

        for (Fixture fixture : b2body.getFixtureList()) {
            fixture.setFilterData(filter);
        }
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

    private void updatePowerState() {
        if (screen.getHud().isPowerTimeUp()) {
            setDefaultFilter();
            currentPowerState = PowerState.NORMAL_MODE;
        }
    }
}
