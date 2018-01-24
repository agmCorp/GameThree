package uy.com.agm.gamethree.sprites.items.collectibles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.game.Constants;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.items.Item;
import uy.com.agm.gamethree.sprites.player.Hero;
import uy.com.agm.gamethree.tools.AudioManager;

/**
 * Created by amorales on 23/1/2018.
 */

public class ColOne extends Item {
    private static final String TAG = ColOne.class.getName();

    private float stateTimer;
    private float stateWaitingTimer;
    private float stateFadingTimer;
    private Animation colOneAnimation;

    public ColOne(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        colOneAnimation = Assets.getInstance().getColOne().getColOneAnimation();
        stateTimer = 0;
        stateWaitingTimer = 0;
        stateFadingTimer = 0;

        // Setbounds is the one that determines the size of the Item's drawing on the screen
        setBounds(getX(), getY(), Constants.COLONE_WIDTH_METERS, Constants.COLONE_HEIGHT_METERS);

        currentState = State.WAITING;
        velocity.set(MathUtils.randomSign() * Constants.COLONE_VELOCITY_X, MathUtils.randomSign() * Constants.COLONE_VELOCITY_Y);

        // Sound FX
        AudioManager.getInstance().play(Assets.getInstance().getSounds().getShowUpColOne());
    }

    @Override
    protected void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY()); // In b2box the origin is at the center of the body
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(Constants.COLONE_CIRCLESHAPE_RADIUS_METERS);
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
        * At this time, ColOne may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) colOneAnimation.getKeyFrame(stateTimer, true));
        stateTimer += dt;

        stateWaitingTimer += dt;
        if (stateWaitingTimer > Constants.COLONE_WAITING_SECONDS) {
            currentState = State.FADING;
        }
    }

    private void stateFading(float dt) {
        b2body.setLinearVelocity(velocity);
        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, ColOne may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) colOneAnimation.getKeyFrame(stateTimer, true));
        stateTimer += dt;

        stateFadingTimer += dt;
        float alpha = 1 - stateFadingTimer / Constants.COLONE_FADING_SECONDS;
        if (alpha >= 0) {
            // 0 invisible, 1 visible
            setAlpha(alpha);
        }

        if (stateFadingTimer > Constants.COLONE_FADING_SECONDS) {
            world.destroyBody(b2body);
            currentState = State.FINISHED;
        }
    }

    private void stateTaken() {
        // Destroy its b2body
        world.destroyBody(b2body);
        currentState = State.FINISHED;
    }

    @Override
    public void use(Hero hero) {
        /*
         * We must remove its b2body to avoid collisions.
         * This can't be done here because this method is called from WorldContactListener that is invoked
         * from PlayScreen.update.world.step(...).
         * No b2body can be removed/changed when the simulation is occurring, we must wait for the next update cycle.
         * Therefore, we use a flag (state) in order to point out this behavior to do it later.
         */
        currentState = State.TAKEN;

        // Audio FX
        AudioManager.getInstance().play(Assets.getInstance().getSounds().getPickUpColOne());

        // Set score
        screen.getHud().addScore(Constants.COLONE_SCORE);
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
