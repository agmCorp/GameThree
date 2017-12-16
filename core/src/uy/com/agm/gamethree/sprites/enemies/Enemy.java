package uy.com.agm.gamethree.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/9/2017.
 */

public abstract class Enemy extends Sprite {
    private static final String TAG = Enemy.class.getName();

    protected World world;
    protected PlayScreen screen;
    public Body b2body;

    protected enum State {ALIVE, INJURED, EXPLODING, DEAD};
    protected State currentState;

    public Enemy(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        b2body.setActive(false);
    }

    public boolean isDestroyed() {
        return currentState == State.DEAD || currentState == State.EXPLODING;
    }

    protected abstract void defineEnemy();
    public abstract void update(float dt);
    public abstract void renderDebug(ShapeRenderer shapeRenderer);
    public abstract void onHit();
}
