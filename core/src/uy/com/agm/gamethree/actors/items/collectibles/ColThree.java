package uy.com.agm.gamethree.actors.items.collectibles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import uy.com.agm.gamethree.actors.items.Item;
import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.scene2d.AssetGoldenHeroHead;
import uy.com.agm.gamethree.assets.sprites.AssetColThree;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by amorales on 23/1/2018.
 */

public class ColThree extends Item {
    private static final String TAG = ColThree.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    private static final float CIRCLE_SHAPE_RADIUS_METERS = 20.0f / PlayScreen.PPM;
    private static final float VELOCITY_X = 2.0f;
    private static final float VELOCITY_Y = -2.0f;
    private static final float WAITING_SECONDS = 5.0f;
    private static final float FADING_SECONDS = 3.0f;
    private static final float ENDURANCE_PROBABILITY = 0.6f;
    private static final int SCORE = 200;

    private boolean isEndurance;
    private float stateTime;
    private float stateWaitingTime;
    private float stateFadingTime;
    private Animation colThreeAnimation;

    public ColThree(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        isEndurance = MathUtils.random() <= ENDURANCE_PROBABILITY;
        colThreeAnimation = isEndurance ? Assets.getInstance().getColThree().getColThreeAnimation() : Assets.getInstance().getScene2d().getGoldenHeroHead().getGoldenHeroHeadAnimation();
        stateTime = 0;
        stateWaitingTime = 0;
        stateFadingTime = 0;

        // Determines the size of the Item's drawing on the screen
        setBounds(getX(), getY(),
                isEndurance ? AssetColThree.WIDTH_METERS : AssetGoldenHeroHead.WIDTH_PIXELS / PlayScreen.PPM,
                isEndurance ? AssetColThree.HEIGHT_METERS : AssetGoldenHeroHead.HEIGHT_PIXELS / PlayScreen.PPM);

        velocity.set(MathUtils.randomSign() * VELOCITY_X, VELOCITY_Y);

        // Sound FX
        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getShowUpColThree());
    }

    @Override
    protected void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY()); // In b2box the origin is at the center of the body
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        b2body.setFixedRotation(true);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(CIRCLE_SHAPE_RADIUS_METERS);
        fdef.filter.categoryBits = WorldContactListener.ITEM_BIT; // Depicts what this fixture is
        fdef.filter.maskBits = WorldContactListener.BORDER_BIT |
                WorldContactListener.OBSTACLE_BIT |
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
        /* Update this Sprite to correspond with the position of the Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, ColThree may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) colThreeAnimation.getKeyFrame(stateTime, true));
        stateTime += dt;

        stateWaitingTime += dt;
        if (stateWaitingTime > WAITING_SECONDS) {
            currentState = State.FADING;
        }
    }

    @Override
    protected void stateFading(float dt) {
        b2body.setLinearVelocity(velocity);
        /* Update this Sprite to correspond with the position of the Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, ColThree may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) colThreeAnimation.getKeyFrame(stateTime, true));
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
        applyColThree();

        // Destroy its b2body
        if(!world.isLocked()) {
            world.destroyBody(b2body);
        }

        // Set the new state
        currentState = State.FINISHED;
    }

    private void applyColThree() {
        if (isEndurance) {
            screen.getCreator().getHero().refillEndurance();
        } else {
            screen.getCreator().getHero().addLives();
        }

        // Audio FX
        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getPickUpColThree());

        // Set score
        screen.getHud().addScore(SCORE);
    }

    @Override
    protected String getClassName() {
        return this.getClass().getName();
    }

    @Override
    protected TextureRegion getHelpImage() {
        return Assets.getInstance().getScene2d().getHelpColThree();
    }

    @Override
    public void onUse() {
        /*
         * We must remove its b2body to avoid collisions.
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
