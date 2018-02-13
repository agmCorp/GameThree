package uy.com.agm.gamethree.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
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

public class EnemyTwo extends Enemy {
    private static final String TAG = EnemyTwo.class.getName();

    private float stateTimer;
    private float openFireTimer;
    private Animation enemyTwoAnimation;
    private Animation explosionAnimation;

    public EnemyTwo(PlayScreen screen, MapObject object) {
        super(screen, object);

        // Animations
        enemyTwoAnimation = Assets.getInstance().getEnemyTwo().getEnemyTwoAnimation();
        explosionAnimation = Assets.getInstance().getExplosionA().getExplosionAAnimation();

        // Setbounds is the one that determines the size of the EnemyTwo's drawing on the screen
        setBounds(getX(), getY(), Constants.ENEMYTWO_WIDTH_METERS, Constants.ENEMYTWO_HEIGHT_METERS);

        stateTimer = 0;
        openFireTimer = 0;
        currentState = State.ALIVE;
        velocity.set(Constants.ENEMYTWO_VELOCITY_X, Constants.ENEMYTWO_VELOCITY_Y);
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY()); // In b2box the origin is at the center of the body
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(Constants.ENEMYTWO_CIRCLESHAPE_RADIUS_METERS);
        fdef.filter.categoryBits = Constants.ENEMY_BIT; // Depicts what this fixture is
        fdef.filter.maskBits = Constants.BORDER_BIT |
                Constants.HERO_WEAPON_BIT |
                Constants.SHIELD_BIT |
                Constants.HERO_BIT |
                Constants.HERO_TOUGH_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    protected void stateAlive(float dt) {
        // Set velocity because It could have been changed (see reverseVelocity)
        b2body.setLinearVelocity(velocity);

        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, EnemyTwo may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        TextureRegion region = (TextureRegion) enemyTwoAnimation.getKeyFrame(stateTimer, true);
        if (b2body.getLinearVelocity().x > 0 && !region.isFlipX()) {
            region.flip(true, false);
        }
        if (b2body.getLinearVelocity().x < 0 && region.isFlipX()) {
            region.flip(true, false);
        }

        setRegion(region);
        stateTimer += dt;

        openFireTimer += dt;
        if (openFireTimer > Constants.ENEMYTWO_FIRE_DELAY_SECONDS) {
            super.openFire();
            openFireTimer = 0;
        }
    }

    @Override
    protected void stateInjured(float dt) {
        // Release an item
        getItemOnHit();

        // Destroy box2D body
        world.destroyBody(b2body);

        // Explosion animation
        stateTimer = 0;

        // Audio FX
        AudioManager.getInstance().play(Assets.getInstance().getSounds().getHit());

        // Set score
        screen.getHud().addScore(Constants.ENEMYTWO_SCORE);

        // Set the new state
        currentState = State.EXPLODING;
    }

    @Override
    protected void stateExploding(float dt) {
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

    @Override
    protected TextureRegion getHelpImage() {
        return Assets.getInstance().getScene2d().getHelpEnemyTwo();
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
}
