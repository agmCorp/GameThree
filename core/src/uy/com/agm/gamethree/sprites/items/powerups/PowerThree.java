package uy.com.agm.gamethree.sprites.items.powerups;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.I18NBundle;

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

public class PowerThree extends Item {
    private static final String TAG = PowerThree.class.getName();

    private int timer;
    private I18NBundle i18NGameThreeBundle;
    private float stateTimer;
    private float stateWaitingTimer;
    private float stateFadingTimer;
    private Animation powerThreeAnimation;
    private Animation bulletAnimation;

    private float manualFireDelay;
    private int numberBullets;

    // Fire power
    public PowerThree(PlayScreen screen, float x, float y, int timer) {
        super(screen, x, y);
        this.timer = timer;

        // I18n
        i18NGameThreeBundle = Assets.getInstance().getI18NGameThree().getI18NGameThreeBundle();

        powerThreeAnimation = Assets.getInstance().getPowerThree().getPowerThreeAnimation();
        bulletAnimation = Assets.getInstance().getBulletA().getBulletAAnimation();
        manualFireDelay = Constants.POWERTHREE_MANUAL_FIRE_DELAY_SECONDS;
        numberBullets = MathUtils.random(2, Constants.POWERTHREE_MAX_BULLETS);
        stateTimer = 0;
        stateWaitingTimer = 0;
        stateFadingTimer = 0;

        // Setbounds is the one that determines the size of the Item's drawing on the screen
        setBounds(getX(), getY(), Constants.POWERTHREE_WIDTH_METERS, Constants.POWERTHREE_HEIGHT_METERS);

        currentState = State.WAITING;
        velocity.set(MathUtils.randomSign() * Constants.POWERTHREE_VELOCITY_X, MathUtils.randomSign() * Constants.POWERTHREE_VELOCITY_Y);

        // Sound FX
        AudioManager.getInstance().play(Assets.getInstance().getSounds().getShowUpPowerThree());
    }

    @Override
    protected void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY()); // In b2box the origin is at the center of the body
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(Constants.POWERTHREE_CIRCLESHAPE_RADIUS_METERS);
        fdef.filter.categoryBits = Constants.ITEM_BIT; // Depicts what this fixture is
        fdef.filter.maskBits = Constants.BORDER_BIT |
                Constants.OBSTACLE_BIT |
                Constants.PATH_BIT |
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
    protected void stateWaiting(float dt) {
        b2body.setLinearVelocity(velocity);
        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, PowerThree may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) powerThreeAnimation.getKeyFrame(stateTimer, true));
        stateTimer += dt;

        stateWaitingTimer += dt;
        if (stateWaitingTimer > Constants.POWERTHREE_WAITING_SECONDS) {
            currentState = State.FADING;
        }
    }

    @Override
    protected void stateFading(float dt) {
        b2body.setLinearVelocity(velocity);
        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, PowerThree may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) powerThreeAnimation.getKeyFrame(stateTimer, true));
        stateTimer += dt;

        stateFadingTimer += dt;
        float alpha = 1 - stateFadingTimer / Constants.POWERTHREE_FADING_SECONDS;
        if (alpha >= 0) {
            // 0 invisible, 1 visible
            setAlpha(alpha);
        }

        if (stateFadingTimer > Constants.POWERTHREE_FADING_SECONDS) {
            world.destroyBody(b2body);
            currentState = State.FINISHED;
        }
    }

    @Override
    protected void stateTaken(float dt) {
        // Destroy its b2body
        world.destroyBody(b2body);
        applyPowerThree();
        currentState = State.FINISHED;
    }

    @Override
    protected String getClassName() {
        return this.getClass().getName();
    }

    @Override
    protected TextureRegion getHelpImage() {
        return Assets.getInstance().getScene2d().getHelpPowerThree();
    }

    private void applyPowerThree() {
        // WA: Hero could have died between use method and applyPowerThree method
        if (!screen.getPlayer().isHeroDead()) {
            // Audio FX
            AudioManager.getInstance().play(Assets.getInstance().getSounds().getPickUpPowerThree());

            // Show the power's name and its countdown
            Hud hud = screen.getHud();
            hud.showPowerInfo(i18NGameThreeBundle.format("powerThree.name"), timer > 0 ? timer : Constants.TIMER_POWERTHREE);

            // Set score
            hud.addScore(Constants.POWERTHREE_SCORE);

            // Disable previous power (if any)
            Hero hero = screen.getPlayer();
            hero.powerDown();

            // Apply fire power
            screen.getPlayer().applyFirePower(Constants.POWERTHREE_BULLET_WIDTH_METERS, Constants.POWERTHREE_BULLET_HEIGHT_METERS,
                    Constants.POWERTHREE_BULLET_CIRCLESHAPERADIUS_METERS, manualFireDelay, numberBullets, bulletAnimation);
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
