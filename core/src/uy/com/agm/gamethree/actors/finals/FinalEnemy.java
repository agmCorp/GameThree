package uy.com.agm.gamethree.actors.finals;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamethree.actors.backgroundObjects.kinematicObjects.Edge;
import uy.com.agm.gamethree.actors.enemies.Enemy;
import uy.com.agm.gamethree.actors.weapons.IShootStrategy;
import uy.com.agm.gamethree.actors.weapons.ShootContext;
import uy.com.agm.gamethree.actors.weapons.Weapon;
import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.sprites.AssetSilverBullet;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.B2WorldCreator;
import uy.com.agm.gamethree.tools.WorldContactListener;

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
    private static final Color KNOCK_BACK_COLOR = Color.BLACK;
    private static final float KNOCK_BACK_SECONDS = 0.2f;
    private static final float KNOCK_BACK_FORCE_X = 1000.0f;
    private static final float KNOCK_BACK_FORCE_Y = 1000.0f;

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
    private boolean knockBackStarted;
    private float knockBackTime;

    protected enum StateFinalEnemy {
        INACTIVE, WALKING, IDLE, SHOOTING, KNOCK_BACK, INJURED, DYING, EXPLODING, DEAD
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
        knockBackStarted = false;
        knockBackTime = 0;
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
            if (screen.isTheEndOfTheWorld()) {
                b2body.setActive(true);

                // Stop music for a few seconds
                AudioManager.getInstance().stopMusic();
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
                        AudioManager.getInstance().play(Assets.getInstance().getMusic().getSongFinalEnemyFight());
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
        setColor(currentStateFinalEnemy == StateFinalEnemy.KNOCK_BACK ? KNOCK_BACK_COLOR : Color.WHITE);
        super.draw(batch);
    }

    protected void stateKnockBack(float dt) {
        if (!knockBackStarted) {
            initKnockBack();
        }

        knockBackTime += dt;
        if (knockBackTime > KNOCK_BACK_SECONDS) {
            currentStateFinalEnemy = StateFinalEnemy.INJURED;
        } else {
            // We don't let this FinalEnemy go beyond the screen
            float upperEdge = screen.getUpperEdge().getB2body().getPosition().y - Edge.HEIGHT_METERS / 2; //  Bottom edge of the upperEdge :)
            float borderLeft = screen.getGameCam().position.x - screen.getGameViewPort().getWorldWidth() / 2;;
            float borderRight = screen.getGameCam().position.x + screen.getGameViewPort().getWorldWidth() / 2;
            float enemyUpperEdge = b2body.getPosition().y + getCircleShapeRadiusMeters();
            float enemyLeftEdge = b2body.getPosition().x - getCircleShapeRadiusMeters();
            float enemyRightEdge = b2body.getPosition().x + getCircleShapeRadiusMeters();

            if (upperEdge <= enemyUpperEdge || enemyLeftEdge <= borderLeft || borderRight <= enemyRightEdge) {
                b2body.setLinearVelocity(0.0f, 0.0f); // Stop
            }

            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

            // Preserve the flip and rotation state
            boolean isFlipX = isFlipX();
            boolean isFlipY = isFlipY();
            float rotation = getRotation();

            setRegion(getKnockBackFrame(dt));

            // Apply previous flip and rotation state
            setFlip(isFlipX, isFlipY);
            setRotation(rotation);
        }
    }

    private void initKnockBack() {
        // Knock back effect
        b2body.setLinearVelocity(0.0f, 0.0f);
        b2body.applyForce(MathUtils.randomSign() * KNOCK_BACK_FORCE_X, KNOCK_BACK_FORCE_Y,
                b2body.getPosition().x, b2body.getPosition().y, true);

        // FinalEnemy can't collide with anything
        Filter filter = new Filter();
        filter.maskBits = WorldContactListener.NOTHING_BIT;

        // We set the previous filter in every fixture
        for (Fixture fixture : b2body.getFixtureList()) {
            fixture.setFilterData(filter);
            fixture.setDensity(0.0f); // No density
        }
        b2body.resetMassData();

        knockBackStarted = true;
    }


    protected abstract void defineFinalEnemy();
    protected abstract IShootStrategy getShootStrategy();
    protected abstract float getCircleShapeRadiusMeters();
    protected abstract TextureRegion getKnockBackFrame(float dt);
    protected abstract void updateLogic(float dt);
    public abstract void onHit(Weapon weapon);
    public abstract void onHitWall(boolean isBorder);
    protected abstract String getFinalEnemyName();
    protected abstract int getFinalEnemyDamage();
    protected abstract void setInitialState();
    protected abstract TextureRegion getHelpImage();
}
