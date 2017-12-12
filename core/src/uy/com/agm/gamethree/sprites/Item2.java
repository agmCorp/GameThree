package uy.com.agm.gamethree.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/11/2017.
 */

public abstract class Item2 extends Sprite {
    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity;
    private enum State {WAITING, TOUCHED, FADING, TAKEN};
    protected Body body;
// video
    public Item2(PlayScreen screen, float x, float y) {
        /*
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x, y);
        setBounds(getX(), getY(), item2.getRegionWidth() / GameThree.PPM, enemyOne.getRegionHeight() / GameThree.PPM));
        */
    }
}
