package uy.com.agm.gamethree.sprites.Items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.player.Hero;

/**
 * Created by AGM on 12/11/2017.
 */

public abstract class Item extends Sprite {
    private static final String TAG = Item.class.getName();

    protected World world;
    protected PlayScreen screen;
    protected Body b2body;

    protected Vector2 velocity;
    protected enum State {WAITING, FADING, TAKEN, FINISHED};
    protected State currentState;

    public Item(PlayScreen screen, float x, float y) {
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x, y);
        defineItem();
    }

    public void reverseVelocity(boolean x, boolean y){
        if(x)
            velocity.x = -velocity.x;
        if(y)
            velocity.y = -velocity.y;
    }

    protected abstract void defineItem();
    public abstract void update(float dt);
    public abstract void renderDebug(ShapeRenderer shapeRenderer);
    public abstract void use(Hero hero);
}
