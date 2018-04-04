package uy.com.agm.gamethree.sprites.finals;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.sprites.AssetSilverBullet;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.boundary.Edge;
import uy.com.agm.gamethree.sprites.enemies.Enemy;
import uy.com.agm.gamethree.sprites.weapons.IShootStrategy;
import uy.com.agm.gamethree.sprites.weapons.ShootContext;
import uy.com.agm.gamethree.sprites.weapons.Weapon;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.B2WorldCreator;

/**
 * Created by AGM on 1/20/2018.
 */

public abstract class FinalEnemy extends Sprite {
    private static final String TAG = FinalEnemy.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    private static final float INTRO_TIME_SECONDS = 5.0f;
    private static final int MAX_AID_SILVER_BULLET = 3;
    private static final float AID_SILVER_BULLET_TIME_SECONDS = 5.0f;
    private static final float AID_SILVER_BULLET_OFFSET_METERS = 100.0f / PlayScreen.PPM;
    protected static final float HIT_MAX_VOLUME = 0.3f;

    protected World world;
    protected PlayScreen screen;
    protected Body b2body;
    protected Vector2 velocity;
    protected Vector2 tmp; // Temporary GC friendly vector
    protected StateFinalEnemy currentStateFinalEnemy;
    private float introTime;
    private boolean playingIntro;
    private float aidSilverBulletTime;
    private ShootContext shootContext;

    protected enum StateFinalEnemy {
        INACTIVE, WALKING, IDLE, SHOOTING, INJURED, DYING, EXPLODING, DEAD
    }

    protected enum PowerState {
        NORMAL, POWERFUL
    }

    public FinalEnemy(PlayScreen screen, float x, float y, float width, float height) {
        this.world = screen.getWorld();
        this.screen = screen;
        this.velocity = new Vector2();

        // Temporary GC friendly vector
        tmp = new Vector2();

        /* Set this Sprite's bounds on the lower left vertex of a Rectangle.
        * This point will be used by defineFinalEnemy() calling getX(), getY() to center its b2body.
        * SetBounds always receives world coordinates.
        */
        setBounds(x, y, width, height);
        defineFinalEnemy();

        introTime = 0;
        playingIntro = false;
        aidSilverBulletTime = 0;

        // Shooting strategy initialization
        shootContext = new ShootContext(getShootStrategy());

        // By default this FinalEnemy doesn't interact in our world
        b2body.setActive(false);
        currentStateFinalEnemy = StateFinalEnemy.INACTIVE;
    }

    // This FinalEnemy can be removed from our game
    public boolean isDisposable() {
        return currentStateFinalEnemy == StateFinalEnemy.DEAD;
    }

    // This FinalEnemy doesn't have any b2body inside these states
    public boolean isDestroyed() {
        return currentStateFinalEnemy == StateFinalEnemy.DYING ||
                currentStateFinalEnemy == StateFinalEnemy.EXPLODING ||
                currentStateFinalEnemy == StateFinalEnemy.DEAD;
    }

    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
    }

    public void update(float dt) {
        if (currentStateFinalEnemy == StateFinalEnemy.INACTIVE) {
            // When our final enemy is on camera, it activates
            if (finalEnemyStarts()) {
                // Pause music for a few seconds
                AudioManager.getInstance().pauseMusic();
                introTime = 0;
                playingIntro = true;

                // Audio FX
                AudioManager.getInstance().play(Assets.getInstance().getSounds().getFinalEnemyIntro());

                // Fight message
                screen.getHud().showFightMessage();

                // Destroy the surviving enemies
                for (Enemy enemy : screen.getCreator().getEnemies()) {
                    enemy.terminate();
                }

                // Enable shooting
                screen.getPlayer().enableShooting();

                // HealthBar
                screen.getHud().showHealthBarInfo(getFinalEnemyName(), getFinalEnemyDamage());

                // Initial state
                setInitialState();

                // update method
                updateLogic(dt);
            }
        } else {
            if (playingIntro) {
                introTime += dt;
                if (introTime > INTRO_TIME_SECONDS) {
                    if (!screen.getPlayer().isDead() && !isDestroyed()) {
                        AudioManager.getInstance().resumeMusic();
                    }
                    screen.getHud().hideMessage();
                    playingIntro = false;
                }
            }

            // Release silver bullets if needed
            if (!screen.getPlayer().isDead() && !screen.getPlayer().hasSilverBullets() && !isDestroyed()) {
                aidSilverBulletTime += dt;
                if (aidSilverBulletTime > AID_SILVER_BULLET_TIME_SECONDS) {
                    releaseSilverBullet();
                    aidSilverBulletTime = 0;
                }
            } else {
                aidSilverBulletTime = 0;
            }

            updateLogic(dt);
        }
    }

    private void releaseSilverBullet() {
        MapObject object;
        int max = MathUtils.random(1, MAX_AID_SILVER_BULLET);

        for(int i = 0; i < max; i++) {
            object = new MapObject();
            object.getProperties().put(B2WorldCreator.KEY_COL_SILVER_BULLET, "");

            float upperEdge = screen.getUpperEdge().getB2body().getPosition().y + Edge.HEIGHT_METERS / 2; //  Upper edge of the upperEdge :)
            float silverBulletCandidatePosition = b2body.getPosition().y + AID_SILVER_BULLET_OFFSET_METERS;

            if (silverBulletCandidatePosition + AssetSilverBullet.COLLECTIBLE_HEIGHT_METERS / 2 >= upperEdge) {
                silverBulletCandidatePosition = b2body.getPosition().y - AID_SILVER_BULLET_OFFSET_METERS;
            }
            screen.getCreator().getItemOnHit(object, b2body.getPosition().x, silverBulletCandidatePosition);
        }
    }

    public void showChallengeBeginHelp() {
        screen.getHud().showModalImage(getHelpImage());
    }

    protected void openFire(float dt) {
        shootContext.update(dt);
        shootContext.shoot(b2body.getPosition().x, b2body.getPosition().y);
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }

    protected abstract void defineFinalEnemy();
    protected abstract IShootStrategy getShootStrategy();
    protected abstract void updateLogic(float dt);
    public abstract void onHit(Weapon weapon);
    public abstract void onHitWall(boolean isBorder);
    protected abstract boolean finalEnemyStarts();
    protected abstract String getFinalEnemyName();
    protected abstract int getFinalEnemyDamage();
    protected abstract void setInitialState();
    protected abstract TextureRegion getHelpImage();
}
