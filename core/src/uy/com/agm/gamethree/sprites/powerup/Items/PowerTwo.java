package uy.com.agm.gamethree.sprites.powerup.Items;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.scenes.Hud;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.player.Hero;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.game.Constants;

/**
 * Created by AGM on 12/14/2017.
 */

public class PowerTwo extends Item {
    private static final String TAG = PowerTwo.class.getName();

    private float stateTimer;
    private float stateWaitingTimer;
    private float stateFadingTimer;
    private Animation powerTwoAnimation;

    // Shield
    public PowerTwo(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        powerTwoAnimation = Assets.instance.powerTwo.powerTwoAnimation;
        stateTimer = 0;
        stateWaitingTimer = 0;
        stateFadingTimer = 0;

        // Setbounds is the one that determines the size of the Item's drawing on the screen
        setBounds(getX(), getY(), Constants.POWERTWO_WIDTH_METERS, Constants.POWERTWO_HEIGHT_METERS);

        currentState = State.WAITING;
        velocity.set(MathUtils.randomSign() * Constants.POWERTWO_VELOCITY_X, MathUtils.randomSign() * Constants.POWERTWO_VELOCITY_Y);

        // Sound FX
        AudioManager.instance.play(Assets.instance.sounds.showUpPowerTwo, 1);
    }

    @Override
    protected void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY()); // In b2box the origin is at the center of the body
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(Constants.POWERTWO_CIRCLESHAPE_RADIUS_METERS);
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
        * At this time, PowerTwo may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) powerTwoAnimation.getKeyFrame(stateTimer, true));
        stateTimer += dt;

        stateWaitingTimer += dt;
        if (stateWaitingTimer > Constants.POWERTWO_WAITING_SECONDS) {
            currentState = State.FADING;
        }
    }

    private void stateFading(float dt) {
        b2body.setLinearVelocity(velocity);
        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, PowerTwo may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) powerTwoAnimation.getKeyFrame(stateTimer, true));
        stateTimer += dt;

        stateFadingTimer += dt;
        float alpha = 1 - stateFadingTimer / Constants.POWERTWO_FADING_SECONDS;
        if (alpha >= 0) {
            // 0 invisible, 1 visible
            setAlpha(alpha);
        }

        if (stateFadingTimer > Constants.POWERTWO_FADING_SECONDS) {
            world.destroyBody(b2body);
            currentState = State.FINISHED;
        }
    }

    private void stateTaken() {
        // Destroy its b2body
        world.destroyBody(b2body);

        // Audio FX
        AudioManager.instance.play(Assets.instance.sounds.pickUpPowerTwo, 1);

        // Show the power's name and its countdown
        Hud hud = screen.getHud();
        hud.setPowerLabel("SHIELD", Constants.TIMER_POWERTWO);

        // Set score
        hud.addScore(Constants.POWERTWO_SCORE);

        // Disable previous power (if any)
        Hero hero = screen.getPlayer();
        hero.powerDown();

        // Create the Shield
        PolygonShape shield = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(-Constants.SHIELD_OFFSETX_METERS, Constants.SHIELD_OFFSETY_METERS + Constants.SHIELD_HEIGHT_METERS);
        vertices[1] = new Vector2(Constants.SHIELD_OFFSETX_METERS, Constants.SHIELD_OFFSETY_METERS + Constants.SHIELD_HEIGHT_METERS);
        vertices[2] = new Vector2(-Constants.SHIELD_OFFSETX_METERS, Constants.SHIELD_OFFSETY_METERS);
        vertices[3] = new Vector2(Constants.SHIELD_OFFSETX_METERS, Constants.SHIELD_OFFSETY_METERS);
        shield.set(vertices);

        // Shield only collide with enemies' bullets
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shield;
        fdef.filter.categoryBits = Constants.SHIELD_BIT;  // Depicts what this fixture is
        fdef.filter.maskBits = Constants.ENEMY_BIT |
                Constants.FINAL_ENEMY_LEVEL_ONE_BIT |
                Constants.ENEMY_WEAPON_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
        hero.getB2body().createFixture(fdef).setUserData(hero);

        // Set the power's texture
        Sprite spritePower = new Sprite(Assets.instance.shield.shieldStand);

        // Only to set width and height of our spritePower
        spritePower.setBounds(hero.getX(), hero.getY(), Constants.POWERTWO_FX_WIDTH_METERS, Constants.POWERTWO_FX_HEIGHT_METERS);

        // Apply effect
        hero.applyPowerFX(Assets.instance.shield.shieldAnimation, spritePower, false);

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
