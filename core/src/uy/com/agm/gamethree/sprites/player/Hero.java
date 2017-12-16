package uy.com.agm.gamethree.sprites.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamethree.game.GameThree;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.Assets;

/**
 * Created by AGM on 12/3/2017.
 */

public class Hero extends Sprite {
    private static final String TAG = Hero.class.getName();

    public enum State {STANDING, MOVING_UP, MOVING_DOWN, SHUTTING}
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    private TextureRegion heroStand;
    private Animation heroMovingUp;
    private Animation heroMovingDown;
    private float stateTimer;

    public Hero(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;

        heroMovingUp = Assets.instance.hero.heroMovingUp;
        heroMovingDown = Assets.instance.hero.heroMovingDown;
        heroStand = Assets.instance.hero.heroStand;
        setPosition(x, y);

        defineHero();
        /* No importa el origen (0, 0) pues se redefine en el método update según la b2box.
           En esta clase se usa el bounds de Sprite que es un rectángulo. Si quisiera un círculo (limite = new Circle(x, y, radio))
           debería definirlo en la clase hero y en su método update hacer limite.setPosition(...).
         */
        // Si quisiera un círculo, debería crear mi propia clase que extienda de Sprite y maneje esa lógica.
        // setbounds es el que determina el tamano del dibujito del heroe en pantalla
        setBounds(0, 0, heroStand.getRegionWidth() / GameThree.PPM, heroStand.getRegionHeight() / GameThree.PPM);
        setRegion(heroStand);
    }

    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region;
        switch (currentState) {
            case MOVING_DOWN:
                region = (TextureRegion) heroMovingDown.getKeyFrame(stateTimer, true);
                break;
            case MOVING_UP:
                region = (TextureRegion) heroMovingUp.getKeyFrame(stateTimer, true);
                break;
            case SHUTTING:
                region = heroStand;
                break;
            case STANDING:
                region = heroStand;
                break;
            default:
                region = heroStand;
                break;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {
        State state = State.STANDING;

        float y = b2body.getLinearVelocity().y;
        float x = b2body.getLinearVelocity().x;
if ( y > 0) {
    state = State.MOVING_UP;
} else if (y < 0){
    state = State.MOVING_DOWN;
}

        Gdx.app.log(TAG, "ANGULO " + b2body.getLinearVelocity().angle());
        return state;
    }


    public void defineHero() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(29 / GameThree.PPM);
        fdef.filter.categoryBits = GameThree.HERO_BIT; // Que es
        fdef.filter.maskBits = GameThree.DEFAULT_BIT |
                GameThree.COINBOX_BIT |
                GameThree.OBSTACLE_BIT |
                GameThree.ITEM_BIT |
                GameThree.ENEMY_BIT; // Con que puede colisionar

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void draw(SpriteBatch batch) {
        // estado normal
        // esto es asi para compensar lo que hace el draw por defecto, que dibuja rotado no se por que.
        //clockwise - If true, the texture coordinates are rotated 90 degrees clockwise. If false, they are rotated 90 degrees counter clockwise.
        boolean clockwise = true;
        float angulo = 90;
        float height = this.getWidth(); // si multiplico ambos por 0.8f es el 80% del sprite del archivo .png
        float width = this.getHeight();
        float temp;

        // touch
        float vAngle = this.b2body.getLinearVelocity().angle();

// anda bien
        if (0 < vAngle && vAngle <= 90) {
            Gdx.app.debug(TAG, "VANGLE " + vAngle);
            angulo = vAngle;
            //angulo = 45;
        }

        if (90 < vAngle && vAngle <= 180) {
            Gdx.app.debug(TAG, "VANGLE " + vAngle);
            angulo = 270.0f - vAngle;
            //angulo = 135;
        }

        if (180 < vAngle && vAngle <= 270) {
            Gdx.app.debug(TAG, "VANGLE " + vAngle);
            angulo = vAngle;
            //angulo = 225;
            clockwise = false;
        }

        if (270 < vAngle && vAngle <= 360) {
            Gdx.app.debug(TAG, "VANGLE " + vAngle);
            angulo = vAngle;
            //angulo = 315;
            clockwise = false;
        }

        // Draws a rectangle with the texture coordinates rotated 90 degrees.
        batch.draw(this, this.b2body.getPosition().x - width / 2, this.b2body.getPosition().y - height / 2,
                width / 2, height / 2, width, height, 1.0f, 1.0f, angulo, clockwise);
    }
}
