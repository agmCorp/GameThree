package uy.com.agm.gamethree.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.sprites.AssetEnemySix;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.items.Item;
import uy.com.agm.gamethree.sprites.weapons.IShootStrategy;
import uy.com.agm.gamethree.sprites.weapons.ShootContext;
import uy.com.agm.gamethree.sprites.weapons.Weapon;
import uy.com.agm.gamethree.sprites.weapons.enemy.EnemyDefaultShooting;
import uy.com.agm.gamethree.tools.B2WorldCreator;

/**
 * Created by AGM on 12/9/2017.
 */

public abstract class Enemy extends Sprite {
    private static final String TAG = Enemy.class.getName();

    // Constants
    private static final float MARGIN_METERS = 3.0f;
    private static final float RANDOM_EXPLOSION_PROB = 0.2f;
    protected static final float SHAKE_DURATION = 1.0f;

    protected World world;
    protected PlayScreen screen;
    protected Body b2body;

    protected Vector2 velocity;
    protected Vector2 tmp; // Temp GC friendly vector

    private TextureRegion splat;

    protected enum State {
        INACTIVE, ALIVE, INJURED, EXPLODING, SPLAT, DEAD
    }

    protected State currentState;
    protected MapObject object;
    protected boolean pum;

    private ShootContext shootContext;
    private boolean openFire;
    private int tiledMapId;

    public Enemy(PlayScreen screen, MapObject object) {
        this.object = object;
        this.tiledMapId = object.getProperties().get("id", 0, Integer.class);
        this.world = screen.getWorld();
        this.screen = screen;
        this.velocity = new Vector2();

        // Shooting strategy initialization
        shootContext = new ShootContext(getShootStrategy());

        // Temp GC friendly vector
        tmp = new Vector2();

        // Fire property
        openFire = object.getProperties().containsKey(B2WorldCreator.KEY_ENEMY_BULLET);

        // Get the rectangle drawn in TiledEditor (pixels)
        Rectangle rect = ((RectangleMapObject) object).getRectangle();

        /* Set this Sprite's position on the lower left vertex of a Rectangle determined by TiledEditor.
        * At this moment we don't have Enemy.width and Enemy.height because this is an abstract class.
        * Width and height will be determined in classes that inherit from this one.
        * This point will be used by defineEnemy() calling getX(), getY() to center its b2body.
        * SetPosition always receives world coordinates.
        */
        setPosition(rect.getX() / PlayScreen.PPM, rect.getY() / PlayScreen.PPM);
        defineEnemy();

        // By default this Enemy doesn't interact in our world
        b2body.setActive(false);
        currentState = State.INACTIVE;

        pum = MathUtils.random() <= RANDOM_EXPLOSION_PROB;
        splat = Assets.getInstance().getEnemySix().getEnemySixBeamStand();
    }

    public String getTiledMapId() {
        return String.valueOf(tiledMapId);
    }

    // This Enemy doesn't have any b2body
    public boolean isDestroyed() {
        return currentState == State.DEAD || currentState == State.EXPLODING || currentState == State.SPLAT;
    }

    public void terminate() {
        world.destroyBody(b2body);
        currentState = State.DEAD;
    }

    protected void checkBoundaries() {
       /* When an Enemy is on camera, it activates (it moves and can collide).
        * When an Enemy is alive and outside the camera, it dies.
        * You have to be very careful because if the enemy is destroyed, its b2body doesn't exist and it gives
        * random errors if you try to access its body.
        */
        float upperEdge = screen.getGameCam().position.y + screen.getGameViewPort().getWorldHeight() / 2;
        float bottomEdge = screen.getGameCam().position.y - screen.getGameViewPort().getWorldHeight() / 2;

        switch (currentState) {
            case INACTIVE:
                if (bottomEdge <= getY() + getHeight() && getY() <= upperEdge) { // It's on camera
                    b2body.setActive(true);
                    currentState = State.ALIVE;
                    screen.getHud().showDynamicHelp(getClassName(), getHelpImage()); // Show dynamic help
                }
                break;

            case ALIVE:
            case EXPLODING:
            case SPLAT:
                // It's outside bottom edge + MARGIN_METERS or outside upperEdge + MARGIN_METERS
                // MARGIN_METERS is important because we don't want to kill an Enemy who is flying around
                // (going in and out of the camera) on a repetitive wide path
                if (bottomEdge > getY() + getHeight() + MARGIN_METERS || upperEdge < getY() - MARGIN_METERS) {
                    if (currentState == State.ALIVE) {
                        world.destroyBody(b2body);
                    }
                    currentState = State.DEAD;
                }
                break;

            default:
                break;
        }
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
            // An Enemy can shoot only when it is visible
            float upperEdge = screen.getGameCam().position.y + screen.getGameViewPort().getWorldHeight() / 2;
            float bottomEdge = screen.getGameCam().position.y - screen.getGameViewPort().getWorldHeight() / 2;
            if (bottomEdge <= getY() + getHeight() && getY() <= upperEdge) {
                shootContext.update(dt);
                shootContext.shoot(b2body.getPosition().x, b2body.getPosition().y - EnemyDefaultShooting.DEFAULT_BULLET_OFFSET_METERS);
            }
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

    // This Enemy can be removed from our game
    public boolean isDisposable() {
        return currentState == State.DEAD;
    }

    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
    }

    public void update(float dt) {
        checkBoundaries();

        if (currentState != State.INACTIVE) {
            switch (currentState) {
                case ALIVE:
                    stateAlive(dt);
                    break;
                case INJURED:
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

    protected void stateSplat() {
        // Setbounds is the one that determines the size of the splat on the screen
        setBounds(getX() + getWidth() / 2 - AssetEnemySix.WIDTH_METERS / 2, getY() + getHeight() / 2 - AssetEnemySix.HEIGHT_METERS / 2,
                AssetEnemySix.WIDTH_METERS , AssetEnemySix.HEIGHT_METERS);
        setRegion(splat);
    }

    public void onBumpWithFeint() {
        onBump();
    }

    public void onHit(Weapon weapon) {
        weapon.onTarget();
        onHit();
    }

    public String whoAmI() {
        return getClassName();
    }

    @Override
    public void draw(Batch batch) {
        if (currentState != State.DEAD && currentState != State.INACTIVE) {
            super.draw(batch);
        }
    }

    public boolean isSplat() {
        return currentState == State.SPLAT;
    }

    protected abstract void defineEnemy();
    protected abstract IShootStrategy getShootStrategy();
    protected abstract void stateAlive(float dt);
    protected abstract void stateInjured(float dt);
    protected abstract void stateExploding(float dt);
    protected abstract String getClassName();
    protected abstract TextureRegion getHelpImage();
    public abstract void onHit();
    public abstract void onBump();
}
