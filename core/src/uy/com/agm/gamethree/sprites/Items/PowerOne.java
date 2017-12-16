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
import uy.com.agm.gamethree.sprites.player.Hero;
import uy.com.agm.gamethree.tools.Assets;
import uy.com.agm.gamethree.tools.AudioManager;

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
        velocity = new Vector2(0.7f, 0);
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
        fdef.filter.categoryBits = GameThree.ITEM_BIT; // Indica que es
        fdef.filter.maskBits = GameThree.DEFAULT_BIT |
                GameThree.OBSTACLE_BIT |
                GameThree.ENEMY_BIT |
                GameThree.COINBOX_BIT |
                GameThree.ITEM_BIT |
                GameThree.HERO_BIT; // Con que puede colisionar
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void update(float dt) {
        float alpha;

        switch (currentState) {
            case WAITING:
                stateTime += dt;
                //velocity.y = b2body.getLinearVelocity().y; // TODO QUE MIERDA ES ESTO
                b2body.setLinearVelocity(velocity);
                setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
                setRegion((TextureRegion) powerOneAnimation.getKeyFrame(stateTime, true));

                // Cinco segundos en estado WAITING
                stateWaiting += dt;
                if (stateWaiting > 5.0f) {
                    currentState = State.FADING;
                }
                break;
            case FADING:
                stateTime += dt;
                // velocity.y = b2body.getLinearVelocity().y; // TODO QUE MIERDA ES ESTO
                b2body.setLinearVelocity(velocity);
                setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
                setRegion((TextureRegion) powerOneAnimation.getKeyFrame(stateTime, true));

                stateFading += dt;
                alpha = 1 - stateFading / 5.0f;
                if (alpha >= 0) {
                    Gdx.app.debug(TAG, "SETEO ALFA " + alpha);
                    // 0 invisible, 1 visible
                    setAlpha(alpha);
                }
                // maximo 5 segundos de fading
                if (stateFading > 5.0f) {
                    world.destroyBody(b2body);
                    currentState = State.FINISHED;
                }
                break;
            case TAKEN:
                world.destroyBody(b2body);
                currentState = State.FINISHED;
                AudioManager.instance.play(Assets.instance.sounds.hit, 1, MathUtils.random(1.0f, 1.1f));
                break;
            case FINISHED:
                break;
            default:
                break;
        }
    }

    @Override
    public void use(Hero hero) {
        /*crc:
        Debemos remove sus b2boxbody asi no tiene mas colisiones con nadie.
        Esto no se puede hacer aca porque esta siendo llamado desde el WorldContactListener que
        es invocado desde el PlayScreen/update/world.step(1 / 60f, 6, 2);
        No se puede borrar ningun tipo de b2boxbody cuando la simulacion esta ocurriendo.
         */
        currentState = State.TAKEN;
        Gdx.app.debug(TAG, "TOMO ITEM");
    }

    public void draw(Batch batch) {
        if (currentState == State.WAITING || currentState == State.FADING) {
            super.draw(batch);
        }
    }

    @Override
    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
        Gdx.app.debug(TAG, "** TAMANO RENDERDEBUG X, Y, WIDTH, EIGHT" + getBoundingRectangle().x + " " + getBoundingRectangle().y + " " + getBoundingRectangle().width + " " + getBoundingRectangle().height);
    }
}
