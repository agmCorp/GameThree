package uy.com.agm.gamethree.sprites.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.weapons.EnergyBall;
import uy.com.agm.gamethree.tools.Assets;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.Constants;
import uy.com.agm.gamethree.tools.GameThreeActorDef;

/**
 * Created by AGM on 12/3/2017.
 */

public class Hero extends Sprite {
    private static final String TAG = Hero.class.getName();

    private enum State {
        STANDING, MOVING_UP, MOVING_DOWN, DEAD
    }
    private State currentState;

    private State previousState;
    public World world;
    public PlayScreen screen;

    public Body b2body;

    private TextureRegion heroStand;
    private Animation heroMovingUp;
    private Animation heroMovingDown;
    private float stateTimer;
    private boolean heroIsDead;

    public Hero(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;

        heroMovingUp = Assets.instance.hero.heroMovingUp;
        heroMovingDown = Assets.instance.hero.heroMovingDown;
        heroStand = Assets.instance.hero.heroStand;

        // Lo invoca playscreen y ya le pasa coordenadas ficticias del mundo.
        setPosition(x, y);
        defineHero();

        setBounds(getX(), getY(), heroStand.getRegionWidth() * Constants.HERO_RESIZE / Constants.PPM, heroStand.getRegionHeight() * Constants.HERO_RESIZE / Constants.PPM);

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        heroIsDead = false;
    }

    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        // Intento controlar que no se vaya de los limites (este codigo no deberia ir aca, deberia ir en la clase del heroe)
        float camEdgeUp = screen.gameCam.position.y + screen.gameViewPort.getWorldHeight() / 2;
        float camEdgeBottom = screen.gameCam.position.y - screen.gameViewPort.getWorldHeight() / 2;
        float heroEdgeUp = getY() + getHeight();
        float heroEdgeBottom = getY();

        // Me pase el borde inferior
        if (camEdgeBottom > heroEdgeBottom) {
            // El sistema de coordenadas en b2body es en el centro
            b2body.setTransform(b2body.getPosition().x, camEdgeBottom + getHeight() / 2, b2body.getAngle());
        }

        // Me pase el borde superior
        if (camEdgeUp < heroEdgeUp) {
            // El sistema de coordenadas en b2body es en el centro
            b2body.setTransform(b2body.getPosition().x, camEdgeUp - getHeight() / 2, b2body.getAngle());
        }
        setRegion(getFrame(dt));
    }
    // ??????????????????????
    public TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region;
        switch (currentState) {
            case STANDING:
                region = heroStand;
                break;
            case MOVING_DOWN:
                region = (TextureRegion) heroMovingDown.getKeyFrame(stateTimer, true);
                break;
            case MOVING_UP:
                region = (TextureRegion) heroMovingUp.getKeyFrame(stateTimer, true);
                break;
            case DEAD:
                region = heroStand;
                AudioManager.instance.play(Assets.instance.sounds.dead, 1);
                heroIsDead = false;
                break;
            default:
                region = heroStand;
                break;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }
    // ??????????????????????
    public State getState() {
        State state;
        if (!heroIsDead) {
            state = State.STANDING;

            float y = b2body.getLinearVelocity().y;
            float x = b2body.getLinearVelocity().x;
            if (y > 0) {
                state = State.MOVING_UP;
            } else if (y < 0) {
                state = State.MOVING_DOWN;
            }
        } else {
            state = State.DEAD;
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
        shape.setRadius(Constants.HERO_CIRCLESHAPE_RADIUS / Constants.PPM);
        fdef.filter.categoryBits = Constants.HERO_BIT; // Que es
        fdef.filter.maskBits = Constants.BORDERS_BIT |
                Constants.POWERBOX_BIT |
                Constants.OBSTACLE_BIT |
                Constants.ITEM_BIT |
                Constants.ENEMY_BIT; // Con que puede colisionar

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    // ??????????????????????
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

    public void onFire() {
        Vector2 position = new Vector2(b2body.getPosition().x, b2body.getPosition().y + Constants.WEAPON_OFFSET / Constants.PPM);
        screen.creator.createGameThreeActor(new GameThreeActorDef(position, EnergyBall.class));
    }

    public void onDead() {
        heroIsDead = true;
        Gdx.app.debug(TAG, "mori");
    }
}
