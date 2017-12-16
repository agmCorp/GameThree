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
    protected World world;
    protected PlayScreen screen;
    public Body b2body;

    public Enemy(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        b2body.setActive(false);
    }

    protected abstract void defineEnemy();
    public abstract void update(float dt);
    public abstract void renderDebug(ShapeRenderer shapeRenderer);
    public abstract void onHit();
}
