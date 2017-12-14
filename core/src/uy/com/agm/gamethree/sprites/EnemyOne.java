package uy.com.agm.gamethree.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.game.GameThree;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.Assets;
import uy.com.agm.gamethree.tools.AudioManager;

/**
 * Created by AGM on 12/9/2017.
 */

public class EnemyOne extends Enemy {
    private static final String TAG = Obstacle.class.getName();
    private float stateTime;
    private Animation enemyOneAnimation;
    private Animation explosionAnimation;
    // Usar un enumerado porque es un estado
    private enum State {ALIVE, INJURED, EXPLODING, DEAD};
    private State currentState;
    private Vector2 velocity;

    public EnemyOne(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        Gdx.app.debug(TAG, "** TAMANO constructor X, Y " + x + " " + y );

        enemyOneAnimation = Assets.instance.enemyOne.enemyOneAnimation;
        explosionAnimation = Assets.instance.enemyOne.explosionAnimation;

        stateTime = 0;

        // Si quisiera un círculo, debería crear mi propia clase que extienda de Sprite y maneje esa lógica.
        TextureRegion enemyOne = Assets.instance.enemyOne.enemyOneStand;
        // setbounds es el que determina el tamano del dibujito del enemigo en pantalla
        Gdx.app.debug(TAG, "** TAMANO bounds X, Y, WIDTH, EIGHT " + getX() + " " + getY() + " " + enemyOne.getRegionWidth() / GameThree.PPM + " " + enemyOne.getRegionHeight() / GameThree.PPM);
        setBounds(getX(), getY(), enemyOne.getRegionWidth() / GameThree.PPM, enemyOne.getRegionHeight() / GameThree.PPM);

        currentState = State.ALIVE;

        velocity = new Vector2(1,-1);
    }

    public void reverseVelocity(boolean x, boolean y) {
        if (x) {
            velocity.x *= -1;
        }
        if (y) {
            velocity.y *= -1;
        }
    }


    public void update(float dt) {
        switch (currentState) {
            case ALIVE:
                stateTime += dt;
                b2body.setLinearVelocity(velocity);
                setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
                setRegion((TextureRegion) enemyOneAnimation.getKeyFrame(stateTime, true));
                break;
            case INJURED:
                world.destroyBody(b2body);
                currentState = State.EXPLODING;
                AudioManager.instance.play(Assets.instance.sounds.hit, 1, MathUtils.random(1.0f, 1.1f));
                stateTime = 0;
                break;
            case EXPLODING:
                // Tiempo total de animacion
                if (stateTime > 1.0f) {
                    currentState = State.DEAD;
                } else {
                    stateTime += dt;
                    setRegion((TextureRegion) explosionAnimation.getKeyFrame(stateTime, true));
                }
                break;
            case DEAD:
                break;
            default:
                break;
        }
    }

    public void draw(Batch batch) {
        if (currentState != State.DEAD) {
            super.draw(batch);
        }
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(29 / GameThree.PPM);
        fdef.filter.categoryBits = GameThree.ENEMY_BIT; // Indica que es
        fdef.filter.maskBits = GameThree.DEFAULT_BIT |
                GameThree.OBSTACLE_BIT |
                GameThree.ENEMY_BIT |
                GameThree.HERO_BIT; // Con que puede colisionar

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void onHit() {
        /*crc:
        Debemos remove sus b2boxbody asi no tiene mas colisiones con nadie.
        Esto no se puede hacer aca porque esta siendo llamado desde el WorldContactListener que
        es invocado desde el PlayScreen/update/world.step(1 / 60f, 6, 2);
        No se puede borrar ningun tipo de b2boxbody cuando la simulacion esta ocurriendo.
         */
        currentState = State.INJURED;
        Gdx.app.debug(TAG, "Enemy collision");
    }

    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
        Gdx.app.debug(TAG, "** TAMANO RENDERDEBUG X, Y, WIDTH, EIGHT" + getBoundingRectangle().x + " " + getBoundingRectangle().y + " " + getBoundingRectangle().width + " " + getBoundingRectangle().height);
    }


}
