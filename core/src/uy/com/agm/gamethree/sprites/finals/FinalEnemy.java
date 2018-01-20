package uy.com.agm.gamethree.sprites.finals;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.weapons.Weapon;

/**
 * Created by AGM on 1/20/2018.
 */

public abstract class FinalEnemy extends Sprite {
    private static final String TAG = FinalEnemy.class.getName();

    protected World world;
    protected PlayScreen screen;
    protected Body b2body;
    protected Vector2 velocity;
    protected Vector2 tmp; // Temp GC friendly vector
    protected StateFinalEnemy currentStateFinalEnemy;

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

        // Temp GC friendly vector
        tmp = new Vector2();

        /* Set this Sprite's bounds on the lower left vertex of a Rectangle.
        * This point will be used by defineFinalEnemy() calling getX(), getY() to center its b2body.
        * SetBounds always receives world coordinates.
        */
        setBounds(x, y, width, height);
        defineFinalEnemy();

        // By default this FinalEnemy doesn't interact in our world
        b2body.setActive(false);
    }

    // This FinalEnemy can be removed from our game
    public boolean isDisposable() {
        return currentStateFinalEnemy == StateFinalEnemy.DEAD;
    }

    // This FinalEnemy doesn't have any b2body
    public boolean isDestroyed() {
        return currentStateFinalEnemy == StateFinalEnemy.DYING ||
                currentStateFinalEnemy == StateFinalEnemy.EXPLODING ||
                currentStateFinalEnemy == StateFinalEnemy.DEAD;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }

    protected abstract void defineFinalEnemy();
    public abstract void update(float dt);
    public abstract void onHit(Weapon weapon);
    public abstract void renderDebug(ShapeRenderer shapeRenderer);
}
