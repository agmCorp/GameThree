package uy.com.agm.gamethree.sprites.powerup.Items;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.game.Constants;
import uy.com.agm.gamethree.screens.Hud;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.player.Hero;
import uy.com.agm.gamethree.tools.AudioManager;

/**
 * Created by AGM on 12/14/2017.
 */

public class PowerFour extends Item {
    private static final String TAG = PowerFour.class.getName();

    private float stateTimer;
    private float stateWaitingTimer;
    private float stateFadingTimer;
    private Animation powerFourAnimation;

    // Tough mode
    public PowerFour(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        powerFourAnimation = Assets.instance.powerFour.powerFourAnimation;
        stateTimer = 0;
        stateWaitingTimer = 0;
        stateFadingTimer = 0;

        // Setbounds is the one that determines the size of the Item's drawing on the screen
        setBounds(getX(), getY(), Constants.POWERFOUR_WIDTH_METERS, Constants.POWERFOUR_HEIGHT_METERS);

        currentState = State.WAITING;
        velocity.set(MathUtils.randomSign() * Constants.POWERFOUR_VELOCITY_X, MathUtils.randomSign() * Constants.POWERFOUR_VELOCITY_Y);

        // Sound FX
        AudioManager.instance.play(Assets.instance.sounds.showUpPowerFour);
    }

    @Override
    protected void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY()); // In b2box the origin is at the center of the body
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(Constants.POWERFOUR_CIRCLESHAPE_RADIUS_METERS);
        fdef.filter.categoryBits = Constants.ITEM_BIT; // Depicts what this fixture is
        fdef.filter.maskBits = Constants.BORDERS_BIT |
                Constants.OBSTACLE_BIT |
                Constants.ENEMY_BIT |
                Constants.POWERBOX_BIT |
                Constants.ITEM_BIT |
                Constants.HERO_TOUGH_BIT |
                Constants.HERO_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void update(float dt) {
        switch (currentState) {
            case WAITING:
                stateWaiting(dt);
                break;
            case FADING:
                stateFading(dt);
                break;
            case TAKEN:
                stateTaken();
                break;
            case FINISHED:
                break;
            default:
                break;
        }
        super.checkBoundaries();
    }

    private void stateWaiting(float dt) {
        b2body.setLinearVelocity(velocity);
        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, PowerFour may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) powerFourAnimation.getKeyFrame(stateTimer, true));
        stateTimer += dt;

        stateWaitingTimer += dt;
        if (stateWaitingTimer > Constants.POWERFOUR_WAITING_SECONDS) {
            currentState = State.FADING;
        }
    }

    private void stateFading(float dt) {
        b2body.setLinearVelocity(velocity);
        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, PowerFour may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) powerFourAnimation.getKeyFrame(stateTimer, true));
        stateTimer += dt;

        stateFadingTimer += dt;
        float alpha = 1 - stateFadingTimer / Constants.POWERFOUR_FADING_SECONDS;
        if (alpha >= 0) {
            // 0 invisible, 1 visible
            setAlpha(alpha);
        }

        if (stateFadingTimer > Constants.POWERFOUR_FADING_SECONDS) {
            world.destroyBody(b2body);
            currentState = State.FINISHED;
        }
    }

    private void stateTaken() {
        // Destroy its b2body
        world.destroyBody(b2body);

        // Audio FX
        AudioManager.instance.play(Assets.instance.sounds.pickUpPowerTwo); // TODO

        // Show the power's name and its countdown
        Hud hud = screen.getHud();
        hud.setPowerLabel("TOUGH MODE", Constants.TIMER_POWERFOUR);

        // Set score
        hud.addScore(Constants.POWERFOUR_SCORE);

        // Disable previous power (if any)
        Hero hero = screen.getPlayer();
        hero.powerDown();

        // Create a new Filter
        Filter filter = new Filter();
        filter.categoryBits = Constants.HERO_TOUGH_BIT; // Depicts what this fixture is
        filter.maskBits = Constants.BORDERS_BIT |
                Constants.EDGES_BIT |
                Constants.OBSTACLE_BIT |
                Constants.POWERBOX_BIT |
                Constants.ITEM_BIT |
                Constants.ENEMY_BIT |
                Constants.FINAL_ENEMY_BIT |
                Constants.ENEMY_WEAPON_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
        for (Fixture fixture : hero.getB2body().getFixtureList()) {
            fixture.setFilterData(filter);
        }

        // Set the power's texture
        Sprite spritePower = new Sprite(Assets.instance.toughMode.toughModeStand);

        // Only to set width and height of our spritePower
        spritePower.setBounds(hero.getX(), hero.getY(), Constants.POWERFOUR_FX_WIDTH_METERS, Constants.POWERFOUR_FX_HEIGHT_METERS);

        // Apply effect
        hero.applyPowerFX(Assets.instance.toughMode.toughModeAnimation, spritePower, false);

        currentState = State.FINISHED;
    }

    @Override
    public void use(Hero hero) {
        /*
         * We must remove its b2body to avoid collisions and change the hero's Filter.
         * This can't be done here because this method is called from WorldContactListener that is invoked
         * from PlayScreen.update.world.step(...).
         * No b2body can be removed/changed when the simulation is occurring, we must wait for the next update cycle.
         * Therefore, we use a flag (state) in order to point out this behavior to do it later.
         */
        currentState = State.TAKEN;
    }

    @Override
    public void onBump() {
        reverseVelocity(true, true);
    }

    public void draw(Batch batch) {
        if (currentState == State.WAITING || currentState == State.FADING) {
            super.draw(batch);
        }
    }

    @Override
    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
    }
}
