package uy.com.agm.gamethree.sprites.weapons;

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

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.game.Constants;

/**
 * Created by AGM on 12/17/2017.
 */

public class HeroBullet extends Weapon {
    private static final String TAG = HeroBullet.class.getName();

    private float stateTimer;
    private Animation heroBulletAnimation;

    public HeroBullet(PlayScreen screen, float x, float y, float width, float height, float circleShapeRadius, float angle, Animation animation) {
        super(screen, x, y, circleShapeRadius > 0 ? circleShapeRadius : Constants.HEROBULLET_CIRCLESHAPE_RADIUS_METERS);

        // Place origin of rotation in the center of the Sprite
        setOriginCenter();

        width = width > 0 ? width : Constants.HEROBULLET_WIDTH_METERS;
        height = height > 0 ? height : Constants.HEROBULLET_HEIGHT_METERS;

        // Setbounds is the one that determines the size of the HeroBullet's drawing on the screen
        setBounds(getX(), getY(), width, height);

        velocity = new Vector2(Constants.HEROBULLET_VELOCITY_X, Constants.HEROBULLET_VELOCITY_Y);
        if (angle > 0) {
            velocity.rotate(angle);
            setRotation(angle);
        }
        if (animation != null) {
            heroBulletAnimation = animation;
        } else {
            heroBulletAnimation = Assets.instance.heroBullet.heroBulletAnimation;
        }

        stateTimer = 0;
        currentState = State.SHOT;

        // Sound FX
        AudioManager.instance.play(Assets.instance.sounds.heroShoot, 0.2f, MathUtils.random(1.0f, 1.1f));
    }

    @Override
    protected void defineWeapon() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY()); // In b2box the origin is at the center of the body
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(circleShapeRadius);
        fdef.filter.categoryBits = Constants.HERO_WEAPON_BIT; // Depicts what this fixture is
        fdef.filter.maskBits = Constants.BORDERS_BIT |
                Constants.OBSTACLE_BIT |
                Constants.POWERBOX_BIT |
                Constants.FINAL_ENEMY_LEVEL_ONE_BIT |
                Constants.ENEMY_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void update(float dt) {
        switch (currentState) {
            case SHOT:
                stateShot(dt);
                break;
            case ONTARGET:
                stateOnTarget();
                break;
            case FINISHED:
                break;
            default:
                break;
        }
        super.checkBoundaries();
    }

    private void stateShot(float dt) {
        b2body.setLinearVelocity(velocity);
        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, HeroBullet may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) heroBulletAnimation.getKeyFrame(stateTimer, true));
        stateTimer += dt;
    }

    private void stateOnTarget() {
        world.destroyBody(b2body);
        currentState = State.FINISHED;
    }

    @Override
    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
    }

    @Override
    public void onTarget() {
        /*
         * We must remove its b2body to avoid collisions.
         * This can't be done here because this method is called from WorldContactListener that is invoked
         * from PlayScreen.update.world.step(...).
         * No b2body can be removed when the simulation is occurring, we must wait for the next update cycle.
         * Therefore, we use a flag (state) in order to point out this behavior and remove it later.
         */
        currentState = State.ONTARGET;
    }

    public void draw(Batch batch) {
        if (currentState == State.SHOT) {
            super.draw(batch);
        }
    }
}
