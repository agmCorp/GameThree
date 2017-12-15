package uy.com.agm.gamethree.sprites.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import uy.com.agm.gamethree.game.GameThree;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.Assets;
import uy.com.agm.gamethree.tools.AudioManager;

import static uy.com.agm.gamethree.sprites.Items.Item.State.TAKEN;
import static uy.com.agm.gamethree.sprites.Items.Item.State.TOUCHED;
import static uy.com.agm.gamethree.sprites.Items.Item.State.WASTED;

/**
 * Created by AGM on 12/14/2017.
 */

public class PowerOne extends Item {
    private static final String TAG = PowerOne.class.getName();

    private float stateTime;
    private float stateWaiting;
    private float stateFading;
    private Animation powerOneAnimation;
    private State currentState;
    private Vector2 velocity;

    public PowerOne(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        powerOneAnimation = Assets.instance.powerOne.powerOneAnimation;
        stateTime = 0;
        stateWaiting = 0;
        stateFading = 0;

        // Si quisiera un círculo, debería crear mi propia clase que extienda de Sprite y maneje esa lógica.
        TextureRegion powerOne = Assets.instance.powerOne.powerOneStand;
        // setbounds es el que determina el tamano del dibujito del enemigo en pantalla
        setBounds(getX(), getY(), powerOne.getRegionWidth() / GameThree.PPM, powerOne.getRegionHeight() / GameThree.PPM);

        currentState = State.WAITING;
        velocity = new Vector2(0,0);
    }

    @Override
    protected void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(29 / GameThree.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void update(float dt) {
        float alpha;

        switch (currentState) {
            case WAITING:
                stateTime += dt;
                stateWaiting += dt;
                b2body.setLinearVelocity(velocity);
                setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
                setRegion((TextureRegion) powerOneAnimation.getKeyFrame(stateTime, true));
                // Cinco segundos en estado WAITING
                if (stateWaiting > 5.0f) {
                    currentState = State.FADING;
                }
                break;
            case TOUCHED:
                currentState = TOUCHED;
                AudioManager.instance.play(Assets.instance.sounds.hit, 1, MathUtils.random(1.0f, 1.1f));
                stateTime = 0;
                break;
            case FADING:
                stateTime += dt;
                stateFading += dt;
                b2body.setLinearVelocity(velocity);
                setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
                setRegion((TextureRegion) powerOneAnimation.getKeyFrame(stateTime, true));
                // 0 invisible, 1 visible

                alpha = 1 - stateFading / 5.0f;
                if (alpha >= 0) {
                    Gdx.app.debug(TAG, "SETEO ALFA " + alpha);
                    this.setAlpha(alpha);
                }
                // maximo 5 segundos de fading
                if (stateFading > 5.0f) {
                    currentState = WASTED;
                }
                break;
            case TAKEN:
                // TODO HABRIA QUE DESTRUIRLO NO?
                break;
            case WASTED:
                world.destroyBody(b2body);
                break;
            default:
                break;
        }


    }

    @Override
    public void use() {
        currentState = TOUCHED;
        Gdx.app.debug(TAG, "TOMO ITEM");
    }

    public void draw(Batch batch) {
        if (currentState != TAKEN && currentState != WASTED) {
            super.draw(batch);
        }
    }

    @Override
    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
        Gdx.app.debug(TAG, "** TAMANO RENDERDEBUG X, Y, WIDTH, EIGHT" + getBoundingRectangle().x + " " + getBoundingRectangle().y + " " + getBoundingRectangle().width + " " + getBoundingRectangle().height);
    }
}
