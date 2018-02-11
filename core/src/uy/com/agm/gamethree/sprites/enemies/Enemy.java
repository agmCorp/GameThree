package uy.com.agm.gamethree.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamethree.game.Constants;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.items.ItemCreator;
import uy.com.agm.gamethree.sprites.weapons.EnemyBullet;
import uy.com.agm.gamethree.tools.GameThreeActorDef;

/**
 * Created by AGM on 12/9/2017.
 */

public abstract class Enemy extends Sprite {
    private static final String TAG = Enemy.class.getName();

    private boolean hasEnemyBulletProp;

    protected World world;
    protected PlayScreen screen;
    protected Body b2body;
    protected Vector2 velocity;
    protected Vector2 tmp; // Temp GC friendly vector

    protected enum State {
        ALIVE, INJURED, EXPLODING, DEAD
    }

    protected State currentState;
    protected MapObject object;

    public Enemy(PlayScreen screen, MapObject object) {
        this.object = object;
        this.world = screen.getWorld();
        this.screen = screen;
        this.velocity = new Vector2();

        // Temp GC friendly vector
        tmp = new Vector2();

        // Fire property
        hasEnemyBulletProp = object.getProperties().containsKey("enemyBullet");

        // Get the rectangle drawn in TiledEditor (pixels)
        Rectangle rect = ((RectangleMapObject) object).getRectangle();

        /* Set this Sprite's position on the lower left vertex of a Rectangle determined by TiledEditor.
        * At this moment we don't have Enemy.width and Enemy.height because this is an abstract class.
        * Width and height will be determined in classes that inherit from this one.
        * This point will be used by defineEnemy() calling getX(), getY() to center its b2body.
        * SetPosition always receives world coordinates.
        */
        setPosition(rect.getX() / Constants.PPM, rect.getY() / Constants.PPM);
        defineEnemy();

        // By default this Enemy doesn't interact in our world
        b2body.setActive(false);
    }

    // This Enemy doesn't have any b2body
    public boolean isDestroyed() {
        return currentState == State.DEAD || currentState == State.EXPLODING;
    }

    protected void checkBoundaries() {
        /* When an Enemy is on camera, it activates (it moves and can collide).
        * You have to be very careful because if the enemy is destroyed, its b2body does not exist and gives
        * random errors if you try to active it.
        */
        if (!isDestroyed()) {
            float upperEdge = screen.getGameCam().position.y + screen.getGameViewPort().getWorldHeight() / 2;
            float bottomEdge = screen.getGameCam().position.y - screen.getGameViewPort().getWorldHeight() / 2;

            if (bottomEdge <= getY() + getHeight() && getY() <= upperEdge) {
                b2body.setActive(true);
            } else {
                if (b2body.isActive()) { // Was on camera...
                    // It's outside bottom edge
                    if (bottomEdge > getY() + getHeight()) {
                        world.destroyBody(b2body);
                        currentState = State.DEAD;
                    }
                }
            }
        }
    }

    // Determine whether or not a power should be released reading a property set in TiledEditor.
    protected void getItemOnHit() {
        ItemCreator.getItemOnHit(object, screen.getCreator(), b2body.getPosition().x, b2body.getPosition().y + Constants.ITEM_OFFSET_METERS);
    }

    protected void openFire() {
        if (hasEnemyBulletProp) {
            if (!isDestroyed()) {
                if (b2body.isActive()) {
                    screen.getCreator().createGameThreeActor(new GameThreeActorDef(b2body.getPosition().x, b2body.getPosition().y - Constants.ENEMYBULLET_OFFSET_METERS, EnemyBullet.class));
                }
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
            case DEAD:
                break;
            default:
                break;
        }
        checkBoundaries();
    }

    @Override
    public void draw(Batch batch) {
        if (currentState != State.DEAD) {
            super.draw(batch);
        }
    }

    protected abstract void defineEnemy();
    protected abstract void stateAlive(float dt);
    protected abstract void stateInjured(float dt);
    protected abstract void stateExploding(float dt);
    public abstract void onHit();
    public abstract void onBump();
}
