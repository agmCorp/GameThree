package uy.com.agm.gamethree.sprites.powerup.Items;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.scenes.Hud;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.player.Hero;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.Constants;

/**
 * Created by AGM on 12/14/2017.
 */

public class PowerThree extends Item {
    private static final String TAG = PowerThree.class.getName();

    private float stateTimer;
    private float stateWaiting;
    private float stateFading;
    private Animation powerThreeAnimation;

    // Triple mode (fire power)
    public PowerThree(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        powerThreeAnimation = Assets.instance.powerOne.powerOneAnimation; // todo
        stateTimer = 0;
        stateWaiting = 0;
        stateFading = 0;

        // Setbounds is the one that determines the size of the Item's drawing on the screen
        setBounds(getX(), getY(), Constants.POWERONE_WIDTH_METERS, Constants.POWERONE_HEIGHT_METERS); // todo

        currentState = State.WAITING;
        velocity = new Vector2(MathUtils.randomSign() * Constants.POWERONE_VELOCITY_X, MathUtils.randomSign() * Constants.POWERONE_VELOCITY_Y); // todo

        // Sound FX
        AudioManager.instance.play(Assets.instance.sounds.showUpPowerOne, 1); // todo
    }

    @Override
    protected void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY()); // In b2box the origin is at the center of the body
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(Constants.POWERONE_CIRCLESHAPE_RADIUS_METERS); // todo
        fdef.filter.categoryBits = Constants.ITEM_BIT; // Depicts what this fixture is
        fdef.filter.maskBits = Constants.BORDERS_BIT |
                Constants.OBSTACLE_BIT |
                Constants.ENEMY_BIT |
                Constants.POWERBOX_BIT |
                Constants.ITEM_BIT |
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
        * At this time, PowerThree may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) powerThreeAnimation.getKeyFrame(stateTimer, true));
        stateTimer += dt;

        stateWaiting += dt;
        if (stateWaiting > Constants.POWERONE_WAITING_SECONDS) { // todo
            currentState = State.FADING;
        }
    }

    private void stateFading(float dt) {
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

        stateFading += dt;
        float alpha = 1 - stateFading / Constants.POWERONE_FADING_SECONDS; // todo
        if (alpha >= 0) {
            // 0 invisible, 1 visible
            setAlpha(alpha);
        }

        if (stateFading > Constants.POWERONE_FADING_SECONDS) { // todo
            world.destroyBody(b2body);
            currentState = State.FINISHED;
        }
    }

    private void stateTaken() {
        // Destroy its b2body
        world.destroyBody(b2body);

        // Audio FX
        AudioManager.instance.play(Assets.instance.sounds.pickUpPowerOne, 1); // todo

        // Show the power's name and its countdown
        Hud hud = screen.getHud();
        hud.setPowerLabel("TRIPLE MODE", Constants.TIMER_POWERONE);

        // Set score
        hud.addScore(Constants.POWERONE_SCORE); // TODO

        // Disable previous power (if any)
        Hero hero = screen.getPlayer();
        hero.powerDown();

        // Apply fire power
       /*
       creo que aca lo mejor seria que el powerthree sea fijo quien sabe que arma es y que frecuencia y cuantas balas.
       todos los poderes de armas varian en esos parametros (animacion, frecuencia, cantidad)

       Posibles otros valores a variar: tamano (mas grande mata mas), tipo de movimiento (boomerang).
       Entonces lo que puede hacer es hacer clases derivadas de esta powerthree para cada uno de los poderes de armas variando
       solo estos parametros de frecuencia, cantidad y animacion (que seria esta siguiente linea)
       En las derivadas seria override statetaken, dentro super.statetaken() y luego esta linea.
        */
        screen.getPlayer().applyFirePower(Assets.instance.explosionC.explosionCAnimation, 0.1f, 2);

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
