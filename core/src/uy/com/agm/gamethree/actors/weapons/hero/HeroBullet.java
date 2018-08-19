package uy.com.agm.gamethree.actors.weapons.hero;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import uy.com.agm.gamethree.actors.weapons.Weapon;
import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 12/17/2017.
 */

public class HeroBullet extends Weapon {
    private static final String TAG = HeroBullet.class.getName();

    private float width;
    private float height;
    private float stateTime;
    private boolean showMuzzleFlashShoot;
    private TextureRegion muzzleFlashShootFX;
    private TextureRegion muzzleFlashImpactFX;
    private Animation heroBulletAnimation;
    private Vector2 tmp; // Temporary GC friendly vector

    public HeroBullet(PlayScreen screen, float x, float y, float width, float height, float circleShapeRadius, float angle, float velocityX, float velocityY, Animation animation) {
        super(screen, x, y, circleShapeRadius);
        this.width = width;
        this.height = height;

        // Place origin of rotation in the center of the Sprite
        setOriginCenter();

        // Determines the size of the HeroBullet's drawing on the screen
        setBounds(getX(), getY(), Assets.getInstance().getHeroBullet().MUZZLE_FLASH_WIDTH_METERS, Assets.getInstance().getHeroBullet().MUZZLE_FLASH_HEIGHT_METERS);

        velocity.set(velocityX, velocityY);
        setRotation(angle);
        heroBulletAnimation = animation;
        stateTime = 0;
        currentState = State.SHOOT;
        showMuzzleFlashShoot = true;
        muzzleFlashShootFX = Assets.getInstance().getHeroBullet().getHeroBulletMuzzleFlashShoot();
        muzzleFlashImpactFX = Assets.getInstance().getHeroBullet().getHeroBulletMuzzleFlashImpact();

        // Temporary GC friendly vector
        tmp = new Vector2();
    }

    @Override
    protected void defineWeapon() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY()); // In b2box the origin is at the center of the body
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        b2body.setFixedRotation(true);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(circleShapeRadius);
        fdef.filter.categoryBits = WorldContactListener.HERO_WEAPON_BIT; // Depicts what this fixture is
        fdef.filter.maskBits = WorldContactListener.BORDER_BIT |
                WorldContactListener.OBSTACLE_BIT |
                WorldContactListener.POWER_BOX_BIT |
                WorldContactListener.BOSS_BIT |
                WorldContactListener.ENEMY_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    protected String getClassName() {
        return this.getClass().getName();
    }

    @Override
    protected void stateShoot(float dt) {
        b2body.setLinearVelocity(velocity);

        // Get the bounding rectangle that could have been changed after applying setRotation
        getBoundingRectangle().getCenter(tmp);

        // Update this Sprite to correspond with the position of the Box2D body
        translate(b2body.getPosition().x - tmp.x, b2body.getPosition().y - tmp.y);

        if (showMuzzleFlashShoot) {
            setRegion(muzzleFlashShootFX); // Only one first frame
            showMuzzleFlashShoot = false;
        } else {
            if (stateTime == 0) { // Animation starts
                setBounds(getX(), getY(), width, height);
            }
            setRegion((TextureRegion) heroBulletAnimation.getKeyFrame(stateTime, true));
            stateTime += dt;
        }
    }

    @Override
    protected void stateOnTarget(float dt) {
        setBounds(getX(), getY(), Assets.getInstance().getHeroBullet().MUZZLE_FLASH_WIDTH_METERS, Assets.getInstance().getHeroBullet().MUZZLE_FLASH_HEIGHT_METERS);
        setRegion(muzzleFlashImpactFX); // Only one last frame

        // Destroy box2D body
        if(!world.isLocked()) {
            world.destroyBody(b2body);
        }

        currentState = State.IMPACT;
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
        currentState = State.ON_TARGET;
    }
}
