package uy.com.agm.gamethree.actors.backgroundObjects.staticObjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamethree.actors.backgroundObjects.IAvoidLandingObject;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.B2WorldCreator;

/**
 * Created by AGM on 12/4/2017.
 */

public abstract class StaticBackgroundObject extends Sprite implements IAvoidLandingObject {
    private static final String TAG = StaticBackgroundObject.class.getName();

    protected PlayScreen screen;
    protected World world;
    protected MapObject object;
    protected Body b2body;
    protected Fixture fixture;
    protected Rectangle boundsMeters;
    protected int tiledMapId;

    public StaticBackgroundObject(PlayScreen screen, MapObject object) {
        this.screen = screen;
        this.world = screen.getWorld();
        this.object = object;
        this.tiledMapId = object.getProperties().get(B2WorldCreator.KEY_ID, 0, Integer.class);
        Rectangle bounds = ((RectangleMapObject) object).getRectangle();
        this.boundsMeters = new Rectangle(bounds.getX() / PlayScreen.PPM, bounds.getY() / PlayScreen.PPM, bounds.getWidth() / PlayScreen.PPM, bounds.getHeight() / PlayScreen.PPM);
        defineStaticBackgroundObject();
    }

    private void defineStaticBackgroundObject() {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(boundsMeters.getX() + boundsMeters.getWidth() / 2, boundsMeters.getY() + boundsMeters.getHeight() / 2);
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = getShape();
        fixture = b2body.createFixture(fdef);
    }

    public String getTiledMapId() {
        return String.valueOf(tiledMapId);
    }

    @Override
    public Rectangle getBoundsMeters() {
        return boundsMeters;
    }

    public abstract void onBump();
    protected abstract Shape getShape();
}
