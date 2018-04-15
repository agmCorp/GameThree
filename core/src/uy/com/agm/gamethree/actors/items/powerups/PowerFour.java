package uy.com.agm.gamethree.actors.items.powerups;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.I18NBundle;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.sprites.AssetPowerFour;
import uy.com.agm.gamethree.assets.sprites.AssetToughMode;
import uy.com.agm.gamethree.screens.Hud;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.actors.items.Item;
import uy.com.agm.gamethree.actors.player.Hero;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 12/14/2017.
 */

public class PowerFour extends Item {
    private static final String TAG = PowerFour.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    private static final float CIRCLE_SHAPE_RADIUS_METERS = 29.0f / PlayScreen.PPM;
    private static final float VELOCITY_X = 0.0f;
    private static final float VELOCITY_Y = -0.7f;
    private static final float WAITING_SECONDS = 5.0f;
    private static final float FADING_SECONDS = 5.0f;
    private static final int DEFAULT_TIMER = 10;
    private static final int SCORE = 20;

    private int timer;
    private I18NBundle i18NGameThreeBundle;
    private float stateTime;
    private float stateWaitingTime;
    private float stateFadingTime;
    private Animation powerFourAnimation;

    // Tough mode
    public PowerFour(PlayScreen screen, float x, float y, int timer) {
        super(screen, x, y);
        this.timer = timer > 0 ? timer : DEFAULT_TIMER;

        // I18n
        i18NGameThreeBundle = Assets.getInstance().getI18NGameThree().getI18NGameThreeBundle();

        powerFourAnimation = Assets.getInstance().getPowerFour().getPowerFourAnimation();
        stateTime = 0;
        stateWaitingTime = 0;
        stateFadingTime = 0;

        // Setbounds is the one that determines the size of the Item's drawing on the screen
        setBounds(getX(), getY(), AssetPowerFour.WIDTH_METERS, AssetPowerFour.HEIGHT_METERS);

        velocity.set(MathUtils.randomSign() * VELOCITY_X, VELOCITY_Y);

        // Sound FX
        AudioManager.getInstance().play(Assets.getInstance().getSounds().getShowUpPowerFour());
    }

    @Override
    protected void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY()); // In b2box the origin is at the center of the body
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(CIRCLE_SHAPE_RADIUS_METERS);
        fdef.filter.categoryBits = WorldContactListener.ITEM_BIT; // Depicts what this fixture is
        fdef.filter.maskBits = WorldContactListener.BORDER_BIT |
                WorldContactListener.OBSTACLE_BIT |
                WorldContactListener.PATH_BIT |
                WorldContactListener.ENEMY_BIT |
                WorldContactListener.POWER_BOX_BIT |
                WorldContactListener.ITEM_BIT |
                WorldContactListener.HERO_BIT |
                WorldContactListener.HERO_GHOST_BIT |
                WorldContactListener.HERO_TOUGH_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    protected void stateWaiting(float dt) {
        b2body.setLinearVelocity(velocity);
        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, PowerFour may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) powerFourAnimation.getKeyFrame(stateTime, true));
        stateTime += dt;

        stateWaitingTime += dt;
        if (stateWaitingTime > WAITING_SECONDS) {
            currentState = State.FADING;
        }
    }

    @Override
    protected void stateFading(float dt) {
        b2body.setLinearVelocity(velocity);
        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, PowerFour may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) powerFourAnimation.getKeyFrame(stateTime, true));
        stateTime += dt;

        stateFadingTime += dt;
        float alpha = 1 - stateFadingTime / FADING_SECONDS;
        if (alpha >= 0) {
            // 0 invisible, 1 visible
            setAlpha(alpha);
        }

        if (stateFadingTime > FADING_SECONDS) {
            if(!world.isLocked()) {
                world.destroyBody(b2body);
            }
            currentState = State.FINISHED;
        }
    }

    @Override
    protected void stateTaken(float dt) {
        applyPowerFour();

        // Destroy its b2body
        if(!world.isLocked()) {
            world.destroyBody(b2body);
        }

        // Set the new state
        currentState = State.FINISHED;
    }

    @Override
    protected String getClassName() {
        return this.getClass().getName();
    }

    @Override
    protected TextureRegion getHelpImage() {
        return Assets.getInstance().getScene2d().getHelpPowerFour();
    }

    private void applyPowerFour() {
        // WA: Hero could have died between use method and applyPowerFour method
        if (!screen.getPlayer().isDead()) {
            // Audio FX
            AudioManager.getInstance().play(Assets.getInstance().getSounds().getPickUpPowerFour());

            // Show the power's name and its countdown
            Hud hud = screen.getHud();
            hud.showPowerInfo(i18NGameThreeBundle.format("powerFour.name"), timer);

            // Set score
            hud.addScore(SCORE);

            // Disable previous power (if any)
            Hero hero = screen.getPlayer();
            hero.powerDown();

            // Create a new Filter
            Filter filter = new Filter();
            filter.categoryBits = WorldContactListener.HERO_TOUGH_BIT; // Depicts what this fixture is
            filter.maskBits = WorldContactListener.BORDER_BIT |
                    WorldContactListener.EDGE_BIT |
                    WorldContactListener.OBSTACLE_BIT |
                    WorldContactListener.PATH_BIT |
                    WorldContactListener.POWER_BOX_BIT |
                    WorldContactListener.ITEM_BIT |
                    WorldContactListener.ENEMY_BIT |
                    WorldContactListener.FINAL_ENEMY_BIT |
                    WorldContactListener.ENEMY_WEAPON_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
            hero.setFilterData(filter);

            // Set the power's texture
            Sprite spritePower = new Sprite(Assets.getInstance().getToughMode().getToughModeStand());

            // Only to set width and height of our spritePower
            spritePower.setBounds(hero.getX(), hero.getY(), AssetToughMode.WIDTH_METERS, AssetToughMode.HEIGHT_METERS);

            // Apply effect
            hero.applyPowerFX(Assets.getInstance().getToughMode().getToughModeAnimation(), spritePower, false);
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
}
