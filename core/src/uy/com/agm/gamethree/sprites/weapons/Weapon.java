package uy.com.agm.gamethree.sprites.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/17/2017.
 */

public abstract class Weapon extends Sprite {
    private static final String TAG = Weapon.class.getName();

    protected World world;
    protected PlayScreen screen;
    public Body b2body;

    protected enum State {
        SHOT, ONTARGET, FINISHED
    }
    protected State currentState;

    public Weapon(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;

        /* Set this Sprite's position on the lower left vertex of a Rectangle.
        * At this moment we don't have Weapon.width and Weapon.height because this is an abstract class.
        * Width and height will be determined in classes that inherit from this one.
        * This point will be used by defineWeapon() calling getX(), getY() to center its b2body.
        * SetPosition always receives world coordinates.
        */
        setPosition(x, y);
        defineWeapon();

        // By default this Weapon doesn't interact in our world
        b2body.setActive(false);
    }

    public boolean isDestroyed() {
        return currentState == State.ONTARGET || currentState == State.FINISHED;
    }

    protected void controlBoundaries() {
        /* When a Weapon is on camera, it activates (it moves and can collide).
        * You have to be very careful because if the weapon is destroyed, its b2body does not exist and gives
        * random errors if you try to active it.
        */
        if (!isDestroyed()) {
            float edgeUp = screen.gameCam.position.y + screen.gameViewPort.getWorldHeight() / 2 - 0.8f;
            float edgeBottom = screen.gameCam.position.y - screen.gameViewPort.getWorldHeight() / 2 + 0.8f;

            if (edgeBottom <= getY() && getY() <= edgeUp) {
                Gdx.app.error(TAG, "ACTIVO");
                b2body.setActive(true);
            } else {
                Gdx.app.error(TAG, "DESACTIVO");
                b2body.setActive(false);
            }
        }
    }

    protected abstract void defineWeapon();
    public abstract void update(float dt);
    public abstract void renderDebug(ShapeRenderer shapeRenderer);
    public abstract void onTarget();
}
