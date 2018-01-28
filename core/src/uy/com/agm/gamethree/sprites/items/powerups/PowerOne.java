package uy.com.agm.gamethree.sprites.items.powerups;

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
import uy.com.agm.gamethree.sprites.items.Item;
import uy.com.agm.gamethree.sprites.player.Hero;
import uy.com.agm.gamethree.tools.AudioManager;

/**
 * Created by AGM on 12/14/2017.
 */

public class PowerOne extends Item {
    private static final String TAG = PowerOne.class.getName();

    private float stateTimer;
    private float stateWaitingTimer;
    private float stateFadingTimer;
    private Animation powerOneAnimation;

    // Ghost mode
    public PowerOne(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        powerOneAnimation = Assets.getInstance().getPowerOne().getPowerOneAnimation();
        stateTimer = 0;
        stateWaitingTimer = 0;
        stateFadingTimer = 0;

        // Setbounds is the one that determines the size of the Item's drawing on the screen
        setBounds(getX(), getY(), Constants.POWERONE_WIDTH_METERS, Constants.POWERONE_HEIGHT_METERS);

        currentState = State.WAITING;
        velocity.set(MathUtils.randomSign() * Constants.POWERONE_VELOCITY_X, MathUtils.randomSign() * Constants.POWERONE_VELOCITY_Y);

        // Sound FX
        AudioManager.getInstance().play(Assets.getInstance().getSounds().getShowUpPowerOne());
    }

    @Override
    protected void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY()); // In b2box the origin is at the center of the body
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(Constants.POWERONE_CIRCLESHAPE_RADIUS_METERS);
        fdef.filter.categoryBits = Constants.ITEM_BIT; // Depicts what this fixture is
        fdef.filter.maskBits = Constants.BORDERS_BIT |
                Constants.OBSTACLE_BIT |
                Constants.ENEMY_BIT |
                Constants.POWERBOX_BIT |
                Constants.ITEM_BIT |
                Constants.HERO_BIT |
                Constants.HERO_GHOST_BIT |
                Constants.HERO_TOUGH_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
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
        * At this time, PowerOne may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) powerOneAnimation.getKeyFrame(stateTimer, true));
        stateTimer += dt;

        stateWaitingTimer += dt;
        if (stateWaitingTimer > Constants.POWERONE_WAITING_SECONDS) {
            currentState = State.FADING;
        }
    }

    private void stateFading(float dt) {
        b2body.setLinearVelocity(velocity);
        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, PowerOne may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) powerOneAnimation.getKeyFrame(stateTimer, true));
        stateTimer += dt;

        stateFadingTimer += dt;
        float alpha = 1 - stateFadingTimer / Constants.POWERONE_FADING_SECONDS;
        if (alpha >= 0) {
            // 0 invisible, 1 visible
            setAlpha(alpha);
        }

        if (stateFadingTimer > Constants.POWERONE_FADING_SECONDS) {
            world.destroyBody(b2body);
            currentState = State.FINISHED;
        }
    }

    private void stateTaken() {
        // Destroy its b2body
        world.destroyBody(b2body);
        applyPowerOne();
        currentState = State.FINISHED;
    }

    private void applyPowerOne() {
        // WA: Hero could have died between use method and applyPowerOne method
        if (!screen.getPlayer().isHeroDead()) {
            // Audio FX
            AudioManager.getInstance().play(Assets.getInstance().getSounds().getPickUpPowerOne());

            // Show the power's name and its countdown
            Hud hud = screen.getHud();
            hud.setPowerLabel("GHOST MODE", Constants.TIMER_POWERONE);

            // Set score
            hud.addScore(Constants.POWERONE_SCORE);

            // Disable previous power (if any)
            Hero hero = screen.getPlayer();
            hero.powerDown();

            // Hero can't collide with enemies nor bullets
            Filter filter = new Filter();
            filter.categoryBits = Constants.HERO_GHOST_BIT;  // Depicts what this fixture is
            filter.maskBits = Constants.BORDERS_BIT |
                    Constants.EDGES_BIT |
                    Constants.OBSTACLE_BIT |
                    Constants.POWERBOX_BIT |
                    Constants.ITEM_BIT |
                    Constants.ENEMY_BIT;  // Depicts what this Fixture can collide with (see WorldContactListener)
            for (Fixture fixture : hero.getB2body().getFixtureList()) {
                fixture.setFilterData(filter);
            }

            // Set the power's texture
            Sprite spritePower = new Sprite(Assets.getInstance().getGhostMode().getGhostModeStand());

            // Only to set width and height of our spritePower
            spritePower.setBounds(hero.getX(), hero.getY(), Constants.POWERONE_FX_WIDTH_METERS, Constants.POWERONE_FX_HEIGHT_METERS);

            // Apply effect
            hero.applyPowerFX(Assets.getInstance().getGhostMode().getGhostModeAnimation(), spritePower, true);
        }
    }

    @Override
    public void onUse() {
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
