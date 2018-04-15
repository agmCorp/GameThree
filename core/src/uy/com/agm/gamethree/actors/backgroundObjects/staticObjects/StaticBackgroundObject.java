package uy.com.agm.gamethree.actors.backgroundObjects.staticObjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.actors.backgroundObjects.IAvoidLandingObject;

/**
 * Created by AGM on 12/4/2017.
 */

public abstract class StaticBackgroundObject implements IAvoidLandingObject {
    private static final String TAG = StaticBackgroundObject.class.getName();

    protected PlayScreen screen;
    protected World world;
    protected MapObject object;
    protected Body b2body;
    protected Fixture fixture;
    protected Rectangle boundsMeters;

    public StaticBackgroundObject(PlayScreen screen, MapObject object) {
        this.screen = screen;
        this.world = screen.getWorld();
        this.object = object;
        Rectangle bounds = ((RectangleMapObject) object).getRectangle();
        this.boundsMeters = new Rectangle(bounds.getX() / PlayScreen.PPM, bounds.getY() / PlayScreen.PPM, bounds.getWidth() / PlayScreen.PPM, bounds.getHeight() / PlayScreen.PPM);
        defineStaticBackgroundObject();
    }

    private void defineStaticBackgroundObject() {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(boundsMeters.getX() + boundsMeters.getWidth() / 2, boundsMeters.getY() + boundsMeters.getHeight() / 2);
        b2body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(boundsMeters.getWidth() / 2, boundsMeters.getHeight() / 2);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fixture = b2body.createFixture(fdef);
    }

    protected void setCategoryFilter(short filterBit) {
        // The default value is 0xFFFF for maskBits, or in other words this fixture will collide
        // with every other fixture as long as the other fixture has this categoryBit in its maskBits list.
        Filter filter = new Filter();
        filter.categoryBits = filterBit; // Depicts what this fixture is
        fixture.setFilterData(filter);
    }

    public abstract void onBump();

    @Override
    public Rectangle getBoundsMeters() {
        return boundsMeters;
    }
}
