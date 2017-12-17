package uy.com.agm.gamethree.sprites.weapons;

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

    protected enum State {SHOT, ONTARGET, FINISHED};

    protected State currentState;

    public Weapon(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;

        setPosition(x, y);
        defineWeapon();
        b2body.setActive(false);
    }

    public boolean isDestroyed() {
        return currentState == State.ONTARGET || currentState == State.FINISHED;
    }

    protected abstract void defineWeapon();

    public abstract void update(float dt);

    public abstract void renderDebug(ShapeRenderer shapeRenderer);

    public abstract void onTarget();
}
