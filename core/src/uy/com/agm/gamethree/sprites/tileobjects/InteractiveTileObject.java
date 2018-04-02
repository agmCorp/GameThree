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

        defineInteractiveTileObject();

        rectangleTmp = new Rectangle();
    }

    private void defineInteractiveTileObject() {
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / PlayScreen.PPM, (bounds.getY() + bounds.getHeight() / 2) / PlayScreen.PPM);

        b2body = world.createBody(bdef);
        shape.setAsBox(bounds.getWidth() / 2 / PlayScreen.PPM, bounds.getHeight() / 2 / PlayScreen.PPM);
        fdef.shape = shape;
        fixture = b2body.createFixture(fdef);
    }

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit; // Depicts what this fixture is
        fixture.setFilterData(filter);
    }

    public Rectangle getBoundsMeters() {
        float x = bounds.x / PlayScreen.PPM;
        float y = bounds.y / PlayScreen.PPM;
        float width = bounds.width / PlayScreen.PPM;
        float height = bounds.height / PlayScreen.PPM;

        return rectangleTmp.set(x, y, width, height);
    }

    public abstract void onBump();
}
