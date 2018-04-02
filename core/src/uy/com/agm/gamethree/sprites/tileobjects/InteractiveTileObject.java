package uy.com.agm.gamethree.sprites.tileobjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/4/2017.
 */

public abstract class InteractiveTileObject {
    private static final String TAG = InteractiveTileObject.class.getName();

    protected PlayScreen screen;
    protected World world;
    protected MapObject object;
    protected TiledMap map;
    protected Rectangle bounds;
    protected Body b2body;
    protected Fixture fixture;

    // Temporary GC friendly rectangle
    private Rectangle rectangleTmp;

    public InteractiveTileObject(PlayScreen screen, MapObject object) {
        this.screen = screen;
        this.world = screen.getWorld();
        this.object = object;
        this.map = screen.getMap();
        this.bounds = ((RectangleMapObject) object).getRectangle();

        // We set bounds in meters (see getBoundsMeters())
        bounds.set(bounds.x / PlayScreen.PPM, bounds.y / PlayScreen.PPM, bounds.width / PlayScreen.PPM, bounds.height / PlayScreen.PPM);

        defineInteractiveTileObject();
    }

    private void defineInteractiveTileObject() {
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(bounds.getX() + bounds.getWidth() / 2, bounds.getY() + bounds.getHeight() / 2);

        b2body = world.createBody(bdef);
        shape.setAsBox(bounds.getWidth() / 2, bounds.getHeight() / 2);
        fdef.shape = shape;
        fixture = b2body.createFixture(fdef);
    }

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit; // Depicts what this fixture is
        fixture.setFilterData(filter);
    }

    public Rectangle getBoundsMeters() {
        return bounds;
    }

    public abstract void onBump();
}
