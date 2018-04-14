package uy.com.agm.gamethree.sprites.boundary;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 4/14/2018.
 */

public class KinematicBridge {
    private static final String TAG = KinematicBridge.class.getName();

    private PlayScreen screen;
    private World world;
    private MapObject object;
    private Rectangle boundsMeters;
    private Body b2body;
    private Vector2 tmp; // Temporary GC friendly vector

    public KinematicBridge(PlayScreen screen, MapObject object) {
        // Temporary GC friendly vector
        tmp = new Vector2();

        this.screen = screen;
        this.world = screen.getWorld();
        this.object = object;
        Rectangle bounds = ((RectangleMapObject) object).getRectangle();
        this.boundsMeters = new Rectangle(bounds.getX() / PlayScreen.PPM, bounds.getY() / PlayScreen.PPM, bounds.getWidth() / PlayScreen.PPM, bounds.getHeight() / PlayScreen.PPM);
        defineKinematicBridge();
    }

    private void defineKinematicBridge() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(boundsMeters.getX() + boundsMeters.getWidth() / 2, boundsMeters.getY() + boundsMeters.getHeight() / 2);
        bdef.type = BodyDef.BodyType.KinematicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(boundsMeters.getWidth() / 2, boundsMeters.getHeight() / 2);

        fdef.filter.categoryBits = WorldContactListener.NOTHING_BIT; // Depicts what this fixture is
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        // Distance between the left border of the screen and the center of the bridge
        float distLeft = tmp.set(1/*0*/, b2body.getPosition().y).dst(b2body.getPosition().x, b2body.getPosition().y);
        defineShape(-distLeft);

        // Distance between the right border of the screen and the center of the bridge
        float distRight = tmp.set(3.8f/*screen.getGameViewPort().getWorldWidth()*/, b2body.getPosition().y).dst(b2body.getPosition().x, b2body.getPosition().y);
        defineShape(distRight);
    }

    private void defineShape(float offsetXMeters) {
        float polygonShapeHalfHeightMeters = boundsMeters.getHeight() / 2;
        float polygonShapeHalfWidthMeters = boundsMeters.getWidth() / 2;

        PolygonShape shape = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(offsetXMeters, polygonShapeHalfHeightMeters);
        vertices[1] = new Vector2(polygonShapeHalfWidthMeters, polygonShapeHalfHeightMeters);
        vertices[2] = new Vector2(offsetXMeters, -polygonShapeHalfHeightMeters);
        vertices[3] = new Vector2(polygonShapeHalfWidthMeters, -polygonShapeHalfHeightMeters);
        shape.set(vertices);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        setFilters(fdef);
        b2body.createFixture(fdef).setUserData(this);
    }

    private void setFilters(FixtureDef fixtureDef) {
        fixtureDef.filter.categoryBits = WorldContactListener.KINEMATIC_BRIDGE_BIT;  // Depicts what this fixture is
        fixtureDef.filter.maskBits = WorldContactListener.HERO_BIT |
                WorldContactListener.HERO_GHOST_BIT |
                WorldContactListener.HERO_TOUGH_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
    }
}
