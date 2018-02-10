package uy.com.agm.gamethree.sprites.weapons;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.game.Constants;
import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/17/2017.
 */

public class HeroBullet extends Weapon {
    private static final String TAG = HeroBullet.class.getName();

    private float stateTimer;
    private Animation heroBulletAnimation;
    private Vector2 tmp; // Temp GC friendly vector

    public HeroBullet(PlayScreen screen, float x, float y, float width, float height, float circleShapeRadius, float angle, Animation animation) {
        super(screen, x, y, circleShapeRadius > 0 ? circleShapeRadius : Constants.HEROBULLET_CIRCLESHAPE_RADIUS_METERS);

        // Place origin of rotation in the center of the Sprite
        setOriginCenter();

        width = width > 0 ? width : Constants.HEROBULLET_WIDTH_METERS;
        height = height > 0 ? height : Constants.HEROBULLET_HEIGHT_METERS;

        // Setbounds is the one that determines the size of the HeroBullet's drawing on the screen
        setBounds(getX(), getY(), width, height);

        velocity.set(Constants.HEROBULLET_VELOCITY_X, Constants.HEROBULLET_VELOCITY_Y);
        if (angle > 0) {
            velocity.rotate(angle);
            setRotation(angle);
        }
        if (animation != null) {
            heroBulletAnimation = animation;
        } else {
            heroBulletAnimation = Assets.getInstance().getHeroBullet().getHeroBulletAnimation();
        }

        stateTimer = 0;
        currentState = State.SHOT;

        // Sound FX
        screen.getPlayer().playSoundHeroShoot();

        // Temp GC friendly vector
        tmp = new Vector2();
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
                Constants.FINAL_ENEMY_BIT |
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

        // Get the bounding rectangle that could have been changed after applying setRotation
        getBoundingRectangle().getCenter(tmp);

        // Update our Sprite to correspond with the position of our Box2D body
        translate(b2body.getPosition().x - tmp.x, b2body.getPosition().y - tmp.y);

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

    @Override
    public void draw(Batch batch) {
        if (currentState == State.SHOT) {
            super.draw(batch);
        }
    }
}
