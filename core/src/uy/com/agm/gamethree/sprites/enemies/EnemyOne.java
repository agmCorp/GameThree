package uy.com.agm.gamethree.sprites.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.Assets;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.Constants;

/**
 * Created by AGM on 12/9/2017.
 */

public class EnemyOne extends Enemy {
    private static final String TAG = EnemyOne.class.getName();

    private float stateTime;
    private Animation enemyOneAnimation;
    private Animation explosionAnimation;
    private Vector2 velocity;

    public EnemyOne(PlayScreen screen, MapObject object) {
        super(screen, object);

        // Animations
        enemyOneAnimation = Assets.instance.enemyOne.enemyOneAnimation;
        explosionAnimation = Assets.instance.enemyOne.explosionAnimation;

        // Setbounds is the one that determines the size of the EnemyOne's drawing on the screen
        TextureRegion enemyOne = Assets.instance.enemyOne.enemyOneStand;
        setBounds(0, 0, enemyOne.getRegionWidth() * Constants.ENEMYONE_RESIZE / Constants.PPM, enemyOne.getRegionHeight() * Constants.ENEMYONE_RESIZE / Constants.PPM);

        stateTime = 0;
        currentState = State.ALIVE;
        velocity = new Vector2(Constants.ENEMYONE_VELOCITY_X, Constants.ENEMYONE_VELOCITY_Y);
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY()); // Center
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(Constants.ENEMYONE_CIRCLESHAPE_RADIUS / Constants.PPM);
        fdef.filter.categoryBits = Constants.ENEMY_BIT; // Depicts what this fixture is
        fdef.filter.maskBits = Constants.BORDERS_BIT |
                Constants.OBSTACLE_BIT |
                Constants.POWERBOX_BIT |
                Constants.ITEM_BIT |
                Constants.WEAPON_BIT |
                Constants.ENEMY_BIT |
                Constants.HERO_BIT; // Depicts what can this Fixture collide with (see WorldContactListener)
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void update(float dt) {
        switch (currentState) {
            case ALIVE:
                updateEnemyOne(dt);
                break;
            case INJURED:
                destroyEnemyOne();
                break;
            case EXPLODING:
                explodeEnemyOne(dt);
                break;
            case DEAD:
                break;
            default:
                break;
        }

        /* When an Enemy is on camara, it activates (it moves and can collide).
        * You have to be very careful because if the enemy is destroyed, its b2body does not exist and gives
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
    public void onHit() {
        /*crc:
        Debemos remove sus b2boxbody asi no tiene mas colisiones con nadie.
        Esto no se puede hacer aca porque esta siendo llamado desde el WorldContactListener que
        es invocado desde el PlayScreen/update/world.step(1 / 60f, 6, 2);
        No se puede borrar ningun tipo de b2boxbody cuando la simulacion esta ocurriendo, hay que esperar al siguiente
        ciclo de update, por eso se cambio el estado.
         */

        super.getItemOnHit();
        currentState = State.INJURED;
        Gdx.app.debug(TAG, "Enemy collision");
    }

    public void draw(Batch batch) {
        if (currentState != State.DEAD) {
            super.draw(batch);
        }
    }

    @Override
    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
    }

    @Override
    public void reverseVelocity(boolean x, boolean y) {
        if (x) {
            velocity.x *= -1;
        }
        if (y) {
            velocity.y *= -1;
        }
    }

    private void destroyEnemyOne() {
        currentState = State.EXPLODING;
        AudioManager.instance.play(Assets.instance.sounds.hit, 1, MathUtils.random(1.0f, 1.1f));
        world.destroyBody(b2body);
        stateTime = 0;
    }

    private void updateEnemyOne(float dt) {
        stateTime += dt;
        b2body.setLinearVelocity(velocity);
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) enemyOneAnimation.getKeyFrame(stateTime, true));
    }

    private void explodeEnemyOne(float dt) {
        if (explosionAnimation.isAnimationFinished(stateTime)) {
            currentState = State.DEAD;
        } else {
            stateTime += dt;
            setRegion((TextureRegion) explosionAnimation.getKeyFrame(stateTime, true));
        }
    }
}



