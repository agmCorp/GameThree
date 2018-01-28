package uy.com.agm.gamethree.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.game.Constants;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.AudioManager;

/**
 * Created by AGM on 12/9/2017.
 */

public class EnemyOne extends Enemy {
    private static final String TAG = EnemyOne.class.getName();

    private float stateTimer;
    private float openFireTimer;
    private Animation enemyOneAnimation;
    private Animation explosionAnimation;

    public EnemyOne(PlayScreen screen, MapObject object) {
        super(screen, object);

        // Animations
        enemyOneAnimation = Assets.getInstance().getEnemyOne().getEnemyOneAnimation();
        explosionAnimation = Assets.getInstance().getExplosionA().getExplosionAAnimation();

        // Setbounds is the one that determines the size of the EnemyOne's drawing on the screen
        setBounds(getX(), getY(), Constants.ENEMYONE_WIDTH_METERS, Constants.ENEMYONE_HEIGHT_METERS);

        stateTimer = 0;
        openFireTimer = 0;
        currentState = State.ALIVE;
        velocity.set(MathUtils.randomSign() * Constants.ENEMYONE_VELOCITY_X, Constants.ENEMYONE_VELOCITY_Y);
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY()); // In b2box the origin is at the center of the body
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(Constants.ENEMYONE_CIRCLESHAPE_RADIUS_METERS);
        fdef.filter.categoryBits = Constants.ENEMY_BIT; // Depicts what this fixture is
        fdef.filter.maskBits = Constants.BORDERS_BIT |
                Constants.OBSTACLE_BIT |
                Constants.POWERBOX_BIT |
                Constants.ITEM_BIT |
                Constants.HERO_WEAPON_BIT |
                Constants.SHIELD_BIT |
                Constants.ENEMY_BIT |
                Constants.HERO_BIT |
                Constants.HERO_TOUGH_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void update(float dt) {
        switch (currentState) {
            case ALIVE:
                stateAlive(dt);
                break;
            case INJURED:
                stateInjured();
                break;
            case EXPLODING:
                stateExploding(dt);
                break;
            case DEAD:
                break;
            default:
                break;
        }
        super.checkBoundaries();
    }

    @Override
    public void onHit() {
        /*
         * We must remove its b2body to avoid collisions.
         * This can't be done here because this method is called from WorldContactListener that is invoked
         * from PlayScreen.update.world.step(...).
         * No b2body can be removed when the simulation is occurring, we must wait for the next update cycle.
         * Therefore, we use a flag (state) in order to point out this behavior and remove it later.
         */
        currentState = State.INJURED;
    }

    @Override
    public void onBump() {
        reverseVelocity(true, false);
    }

    @Override
    public void draw(Batch batch) {
        if (currentState != State.DEAD) {
           super.draw(batch);
        }
    }

    @Override
    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
    }

    private void stateInjured() {
        // Release an item
        getItemOnHit();

        // Destroy box2D body
        world.destroyBody(b2body);

        // Explosion animation
        stateTimer = 0;

        // Audio FX
        AudioManager.getInstance().play(Assets.getInstance().getSounds().getHit());

        // Set score
        screen.getHud().addScore(Constants.ENEMYONE_SCORE);

        // Set the new state
        currentState = State.EXPLODING;
    }

    private void stateAlive(float dt) {
        // Set velocity because It could have been changed (see reverseVelocity)
        b2body.setLinearVelocity(velocity);

        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, EnemyOne may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) enemyOneAnimation.getKeyFrame(stateTimer, true));
        stateTimer += dt;

        openFireTimer += dt;
        if (openFireTimer > Constants.ENEMYONE_FIRE_DELAY_SECONDS) {
            super.openFire();
            openFireTimer = 0;
        }
    }

    private void stateExploding(float dt) {
        if (explosionAnimation.isAnimationFinished(stateTimer)) {
            currentState = State.DEAD;
        } else {
            if (stateTimer == 0) { // Explosion starts
                // Setbounds is the one that determines the size of the explosion on the screen
                setBounds(getX() + getWidth() / 2 - Constants.EXPLOSIONA_WIDTH_METERS / 2, getY() + getHeight() / 2 - Constants.EXPLOSIONA_HEIGHT_METERS / 2,
                        Constants.EXPLOSIONA_WIDTH_METERS, Constants.EXPLOSIONA_HEIGHT_METERS);
            }
            setRegion((TextureRegion) explosionAnimation.getKeyFrame(stateTimer, true));
            stateTimer += dt;
        }
    }
}
