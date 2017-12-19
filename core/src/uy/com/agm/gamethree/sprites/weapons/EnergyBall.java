package uy.com.agm.gamethree.sprites.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.Assets;
import uy.com.agm.gamethree.tools.Constants;

/**
 * Created by AGM on 12/17/2017.
 */

public class EnergyBall extends Weapon {
    private static final String TAG = EnergyBall.class.getName();

    private float stateTime;
    private Animation energyBallAnimation;
    private Vector2 velocity;

    public EnergyBall(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        energyBallAnimation = Assets.instance.energyBall.energyBallAnimation;
        stateTime = 0;

        TextureRegion energyBall = Assets.instance.energyBall.energyBallStand;
        // setbounds es el que determina el tamano del dibujito del enemigo en pantalla
        // todo constantes
        setBounds(getX(), getY(), energyBall.getRegionWidth() * 0.8f / Constants.PPM, energyBall.getRegionHeight() * 0.8f / Constants.PPM);

        currentState = State.SHOT;

        // todo constantes
        velocity = new Vector2(0, 6);
    }
    @Override
    protected void defineWeapon() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(20 / Constants.PPM);
        fdef.filter.categoryBits = Constants.WEAPON_BIT; // Indica que es
        fdef.filter.maskBits = Constants.BORDERS_BIT |
                Constants.OBSTACLE_BIT |
                Constants.POWERBOX_BIT |
                Constants.ENEMY_BIT; // cON QUE PUEDE COLISIONAR

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

    }

    @Override
    public void update(float dt) {
        switch (currentState) {
            case SHOT:
                stateTime += dt;
                b2body.setLinearVelocity(velocity);
                setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
                setRegion((TextureRegion) energyBallAnimation.getKeyFrame(stateTime, true));
                break;
            case ONTARGET:
                world.destroyBody(b2body);
                currentState = State.FINISHED;
                break;
            case FINISHED:
                break;
            default:
                break;
        }

        /* When a energyBall is on camara, it activates (it moves and can colide).
        * You have to be very careful because if the energyBall is finished or ontarget, its b2body does not exist and gives
        * random errors if you try to active it.
        */
        if (!isDestroyed()) {
            if (getY() - getWidth() <= screen.gameCam.position.y + screen.gameViewPort.getWorldHeight() / 2) {
                b2body.setActive(true);
            }
            if (getY() + getWidth() <= screen.gameCam.position.y - screen.gameViewPort.getWorldHeight() / 2) {
                b2body.setActive(false);
            }
        }
    }

    @Override
    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
        Gdx.app.debug(TAG, "** TAMANO RENDERDEBUG X, Y, WIDTH, EIGHT" + getBoundingRectangle().x + " " + getBoundingRectangle().y + " " + getBoundingRectangle().width + " " + getBoundingRectangle().height);
    }

    @Override
    public void onTarget() {
       /*crc:
        Debemos remove sus b2boxbody asi no tiene mas colisiones con nadie.
        Esto no se puede hacer aca porque esta siendo llamado desde el WorldContactListener que
        es invocado desde el PlayScreen/update/world.step(1 / 60f, 6, 2);
        No se puede borrar ningun tipo de b2boxbody cuando la simulacion esta ocurriendo, hay que esperar al siguiente
        ciclo de update, por eso se cambio el estado.
         */

        currentState = State.ONTARGET;
        Gdx.app.debug(TAG, "Enemy collision");
    }

    public void draw(Batch batch) {
        if (currentState == State.SHOT) {
            // ACA SE CAE SEGUN ANDROID
            super.draw(batch);
        }
    }
}
