package uy.com.agm.gamethree.actors.enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;

import uy.com.agm.gamethree.actors.backgroundObjects.IAvoidLandingObject;
import uy.com.agm.gamethree.actors.backgroundObjects.kinematicObjects.Edge;
import uy.com.agm.gamethree.actors.items.Item;
import uy.com.agm.gamethree.actors.weapons.IShootStrategy;
import uy.com.agm.gamethree.actors.weapons.ShootContext;
import uy.com.agm.gamethree.actors.weapons.Weapon;
import uy.com.agm.gamethree.actors.weapons.enemy.EnemyDefaultShooting;
import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.sprites.AssetSplat;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.B2WorldCreator;
import uy.com.agm.gamethree.tools.WorldContactListener;

import static uy.com.agm.gamethree.actors.enemies.Enemy.State.EXPLODING;
import static uy.com.agm.gamethree.actors.enemies.Enemy.State.INJURED;

/**
 * Created by AGM on 12/9/2017.
 */

public abstract class Enemy extends Sprite {
    private static final String TAG = Enemy.class.getName();

    // Constants
    private static final float RANDOM_EXPLOSION_PROB = 0.2f;
    private static final float SHAKE_DURATION = 1.0f;
    private static final float EXPLOSION_SCALE = 3.0f;
    private static final Color KNOCK_BACK_COLOR = Color.BLACK;
    private static final float KNOCK_BACK_SECONDS = 0.2f;
    private static final float KNOCK_BACK_FORCE_X = 1000.0f;
    private static final float KNOCK_BACK_FORCE_Y = 1000.0f;

    private TextureRegion splat;
    private boolean pum;
    private ShootContext shootContext;
    private boolean openFire;
    private int tiledMapId;
    private boolean knockBackStarted;
    private float knockBackTime;

    protected World world;
    protected PlayScreen screen;
    protected Body b2body;

    protected Vector2 velocity;
    protected Vector2 tmp; // Temporary GC friendly vector

    protected enum State {
        INACTIVE, ALIVE, KNOCK_BACK, INJURED, EXPLODING, SPLAT, DEAD
    }

    protected State currentState;
    protected MapObject object;
    protected float expScale;

    public Enemy(PlayScreen screen, MapObject object) {
        this.object = object;
        this.tiledMapId = object.getProperties().get(B2WorldCreator.KEY_ID, 0, Integer.class);
        this.world = screen.getWorld();
        this.screen = screen;
        this.velocity = new Vector2();

        // Shooting strategy initialization
        shootContext = new ShootContext(getShootStrategy());

        // Temporary GC friendly vector
        tmp = new Vector2();

        // Shoot properties
        MapProperties mapProperties = object.getProperties();
        openFire = mapProperties.containsKey(B2WorldCreator.KEY_ENEMY_BULLET);
        if (mapProperties.containsKey(B2WorldCreator.KEY_SHOOT_WHEN_VISIBLE)) {
            shootContext.shootWhenVisible();
        }
        if (mapProperties.containsKey(B2WorldCreator.KEY_FIRE_DELAY)) {
            shootContext.setFireDelay(mapProperties.get(B2WorldCreator.KEY_FIRE_DELAY, 0, Integer.class));
        }

        // Get the rectangle drawn in TiledEditor (pixels)
        Rectangle rect = ((RectangleMapObject) object).getRectangle();

        /* Set this Sprite's position on the lower left vertex of a Rectangle determined by TiledEditor.
        * At this moment we don't have Enemy.width and Enemy.height because this is an abstract class.
        * Width and height will be determined in classes that inherit from this one.
        * This position will be used by defineEnemy() calling getX(), getY() to center its b2body.
        * SetPosition always receives world coordinates.
        */
        setPosition(rect.getX() / PlayScreen.PPM, rect.getY() / PlayScreen.PPM);
        defineEnemy();

        // By default this Enemy doesn't interact in our world
        b2body.setActive(false);
        currentState = State.INACTIVE;

        pum = MathUtils.random() <= RANDOM_EXPLOSION_PROB;
        expScale = pum ? EXPLOSION_SCALE : 1;
        splat = Assets.getInstance().getSplat().getRandomEnemySplat();
        knockBackStarted = false;
        knockBackTime = 0;
    }

    private void checkBoundaries() {
       /* When an Enemy is on camera, it activates (it moves and can collide).
        * When an Enemy is alive and outside the camera, it dies.
        * You have to be very careful because if the enemy is destroyed, its b2body doesn't exist and it gives
        * random errors if you try to access its body.
        */
        float camY = screen.getGameCam().position.y;
        float worldHeight = screen.getGameViewPort().getWorldHeight();
        float upperEdge = camY + worldHeight / 2;
        float bottomEdge = camY - worldHeight / 2;

        switch (currentState) {
            case INACTIVE:
                if (bottomEdge <= getY() + getHeight() && getY() <= upperEdge) { // It's on camera
                    b2body.setActive(true);
                    currentState = State.ALIVE;
                    screen.getInfoScreen().showDynamicHelp(getClassName(), getHelpImage()); // Show dynamic help
                }
                break;

            case ALIVE:
            case EXPLODING:
            case SPLAT:
                if (isSpriteOutsideBottomEdge(bottomEdge) || isSpriteOutsideUpperEdge(upperEdge)) {
                    if (currentState == State.ALIVE) {
                        if (!world.isLocked()) {
                            world.destroyBody(b2body);
                        }
                        screen.enemyGetAway();
                    }
                    currentState = State.DEAD;
                }
                break;

            default:
                break;
        }
    }

    private void initKnockBack() {
        // Knock back effect
        b2body.setLinearVelocity(0.0f, 0.0f);
        b2body.applyForce(MathUtils.randomSign() * KNOCK_BACK_FORCE_X, KNOCK_BACK_FORCE_Y,
                b2body.getPosition().x, b2body.getPosition().y, true);

        // Enemy can't collide with anything
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

    // Determine whether or not a power should be released reading a property set in TiledEditor.
    protected void getItemOnHit() {
        screen.getCreator().getItemOnHit(object, b2body.getPosition().x, b2body.getPosition().y + Item.OFFSET_METERS);
    }

    protected void getItemOnHit(float x, float y) {
        screen.getCreator().getItemOnHit(object, x, y);
    }

    protected void openFire(float dt) {
        if (openFire && !isDestroyed()) {
            shoot(dt);
        }
    }

    protected void shoot(float dt) {
        // An Enemy can shoot only when it is visible
        float camX = screen.getGameCam().position.x;
        float camY = screen.getGameCam().position.y;
        float worldWidth = screen.getGameViewPort().getWorldWidth();
        float worldHeight = screen.getGameViewPort().getWorldHeight();
        float borderLeft = camX - worldWidth / 2;
        float borderRight = camX + worldWidth / 2;
        float upperEdge = camY + worldHeight / 2;
        float bottomEdge = camY - worldHeight / 2;

        if (borderLeft <= getX() && getX() + getWidth() <= borderRight && bottomEdge <= getY() + getHeight() && getY() <= upperEdge) {
            shootContext.update(dt);
            shootContext.shoot(b2body.getPosition().x, b2body.getPosition().y - EnemyDefaultShooting.DEFAULT_BULLET_OFFSET_METERS);
        }
    }

    protected void reverseVelocity(boolean x, boolean y) {
        if (x) {
            velocity.x *= -1;
        }
        if (y) {
            velocity.y *= -1;
        }
    }

    protected void speak() {
        Sound voice = getVoice();
        if (voice != null) {
            Long lastPlayingTime = AudioManager.getInstance().getLastPlayingTime(voice);
            if (lastPlayingTime == null ||
                    TimeUtils.nanosToMillis(TimeUtils.nanoTime() - lastPlayingTime) >= getSpeakTimeSeconds() * 1000) {
                AudioManager.getInstance().playSound(voice);
            }
        }
    }

    protected void pum(Sound hitSound) {
        // Audio FX and screen shake
        if (pum) {
            screen.getShaker().shake(SHAKE_DURATION);
            AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getPum());
        } else {
            AudioManager.getInstance().playSound(hitSound);
        }
    }

    protected void stateKnockBack(float dt) {
        if (!knockBackStarted) {
            initKnockBack();
        }

        knockBackTime += dt;
        if (knockBackTime > KNOCK_BACK_SECONDS) {
            currentState = INJURED;
        } else {
            // We don't let this Enemy go beyond the screen
            float camX = screen.getGameCam().position.x;
            float worldWidth = screen.getGameViewPort().getWorldWidth();
            float upperEdge = screen.getUpperEdge().getB2body().getPosition().y - Edge.HEIGHT_METERS / 2; //  Bottom edge of the upperEdge :)
            float borderLeft = camX - worldWidth / 2;
            float borderRight = camX + worldWidth / 2;
            float circleShapeRadius = getCircleShapeRadiusMeters();
            float enemyUpperEdge = b2body.getPosition().y + circleShapeRadius;
            float enemyLeftEdge = b2body.getPosition().x - circleShapeRadius;
            float enemyRightEdge = b2body.getPosition().x + circleShapeRadius;

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


    protected void stateSplat() {
        // Determines the size of the splat on the screen
        setBounds(getX() + getWidth() / 2 - AssetSplat.ENEMY_SPLAT_WIDTH_METERS / 2, getY() + getHeight() / 2 - AssetSplat.ENEMY_SPLAT_HEIGHT_METERS / 2,
                AssetSplat.ENEMY_SPLAT_WIDTH_METERS, AssetSplat.ENEMY_SPLAT_HEIGHT_METERS);
        setRegion(splat);
    }

    // Debug method
    public String getTiledMapId() {
        return String.valueOf(tiledMapId);
    }

    // Debug method
    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
    }

    // Debug method
    public String whoAmI() {
        return getClassName();
    }

    // Debug method
    public String getNameCurrentState() {
        return currentState.toString();
    }

    // Life cycle: INACTIVE->ALIVE->KNOCK_BACK->INJURED->EXPLODING->SPLAT->DEAD
    public void update(float dt) {
        checkBoundaries();

        if (currentState != State.INACTIVE) {
            switch (currentState) {
                case ALIVE:
                    speak();
                    stateAlive(dt);
                    break;
                case KNOCK_BACK:
                    stateKnockBack(dt);
                    break;
                case INJURED:
                    // This state doesn't set a Texture to draw, it uses the Texture defined
                    // in the previous state (KNOCK_BACK) instead.
                    // INJURED is a transition state used to remove the body from the world.
                    stateInjured(dt);
                    break;
                case EXPLODING:
                    stateExploding(dt);
                    break;
                case SPLAT:
                    stateSplat();
                    break;
                case DEAD:
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void draw(Batch batch) {
        if (currentState != State.DEAD && currentState != State.INACTIVE) {

            // Set the tint
            if (currentState == State.KNOCK_BACK) {
                setColor(KNOCK_BACK_COLOR); // Knock back
            } else {
                if (currentState == State.EXPLODING) {
                    setColor(Color.WHITE); // Default
                }
            }

            super.draw(batch);
        }
    }

    // Makes this Enemy disposable
    public void dispose() {
        if (!isDestroyed()) {
            if (!world.isLocked()) {
                world.destroyBody(b2body);
            }
        }
        currentState = State.DEAD;
    }

    // This Enemy doesn't have any b2body inside these states
    public boolean isDestroyed() {
        return currentState == EXPLODING || currentState == State.SPLAT || currentState == State.DEAD;
    }

    // This Enemy can be removed from our game
    public boolean isDisposable() {
        return currentState == State.DEAD;
    }

    public boolean isSplat() {
        return currentState == State.SPLAT;
    }

    public boolean isAlive() {
        return currentState == State.ALIVE;
    }

    public void onBump(IAvoidLandingObject obj) {
        onBump();
    }

    public void onHit(Weapon weapon) {
        weapon.onTarget();
        onHit();
    }

    protected abstract void defineEnemy();
    protected abstract IShootStrategy getShootStrategy();
    protected abstract float getCircleShapeRadiusMeters();
    protected abstract TextureRegion getKnockBackFrame(float dt);
    protected abstract Sound getVoice();
    protected abstract float getSpeakTimeSeconds();
    protected abstract String getClassName();
    protected abstract TextureRegion getHelpImage();
    protected abstract boolean isSpriteOutsideBottomEdge(float bottomEdge);
    protected abstract boolean isSpriteOutsideUpperEdge(float upperEdge);
    protected abstract void stateAlive(float dt);
    protected abstract void stateInjured(float dt);
    protected abstract void stateExploding(float dt);
    public abstract void onHit();
    public abstract void onBump();
    public abstract void onDestroy();
}
