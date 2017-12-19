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
        TextureRegion enemyOneStand = Assets.instance.enemyOne.enemyOneStand;
        setBounds(0, 0, enemyOneStand.getRegionWidth() * Constants.ENEMYONE_RESIZE / Constants.PPM, enemyOneStand.getRegionHeight() * Constants.ENEMYONE_RESIZE / Constants.PPM);

        stateTime = 0;
        currentState = State.ALIVE;
        velocity = new Vector2(Constants.ENEMYONE_VELOCITY_X, Constants.ENEMYONE_VELOCITY_Y);
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY()); // Center of the circle
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
            float edgeUp = screen.gameCam.position.y + screen.gameViewPort.getWorldHeight() / 2;
            float edgeBottom = screen.gameCam.position.y - screen.gameViewPort.getWorldHeight() / 2;

            if (edgeBottom <= getY() && getY() <= edgeUp) {
                b2body.setActive(true);
            } else {
                b2body.setActive(false);
            }
        }
    }

    @Override
    public void onHit() {
        /*
         * We must remove its b2body to avoid collisions.
         * This can't be done here because this method is called from the WorldContactListener that is invoked
         * from PlayScreen.update.world.step(...).
         * No b2body can be removed when the simulation is occurring, we must wait for the next update cycle.
         * Therefore we use a flag (state) in order to point out this behavior.
         */
        super.getItemOnHit();
        currentState = State.INJURED;
        Gdx.app.debug(TAG, "Enemy collision");
    }

    public void draw(Batch batch) {
        if (currentState != State.DEAD) {
           // super.draw(batch);
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



