package uy.com.agm.gamethree.sprites.boundary;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamethree.screens.AbstractScreen;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 12/31/2017.
 */

public class Edge {
    private static final String TAG = Edge.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = AbstractScreen.V_WIDTH / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 1.0f * 1.0f / PlayScreen.PPM;

    private PlayScreen screen;
    private World world;
    private Rectangle boundsMeters;
    private Body b2body;

    public Edge(PlayScreen screen, boolean isUpper) {
        this.screen = screen;
        this.world = screen.getWorld();

        if (isUpper) {
            this.boundsMeters = new Rectangle(0, screen.getGameCam().position.y + screen.getGameViewPort().getWorldHeight() / 2 - HEIGHT_METERS, WIDTH_METERS, HEIGHT_METERS);
        } else {
            this.boundsMeters = new Rectangle(0, screen.getGameCam().position.y - screen.getGameViewPort().getWorldHeight() / 2, WIDTH_METERS, HEIGHT_METERS);
        }

        defineEdge();
        start();
    }

    private void defineEdge() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(boundsMeters.getX() + boundsMeters.getWidth() / 2, boundsMeters.getY() + boundsMeters.getHeight() / 2);
        bdef.type = BodyDef.BodyType.KinematicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(boundsMeters.getWidth() / 2, boundsMeters.getHeight() / 2);

        fdef.filter.categoryBits = WorldContactListener.EDGE_BIT; // Depicts what this fixture is
        fdef.filter.maskBits = WorldContactListener.FINAL_ENEMY_BIT |
                            WorldContactListener.HERO_BIT |
                            WorldContactListener.HERO_GHOST_BIT |
                            WorldContactListener.HERO_TOUGH_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    public Body getB2body() {
        return b2body;
    }

    public Rectangle getBoundsMeters() {
        return boundsMeters;
    }

    public void stop() {
        b2body.setLinearVelocity(0.0f, 0.0f);
    }

    public void start() {
        b2body.setLinearVelocity(0.0f, PlayScreen.GAMECAM_VELOCITY);
    }

    public void speedUp() {
        b2body.setLinearVelocity(0.0f, PlayScreen.GAMECAM_VELOCITY * 5);
    }

    public void onBump() {}
}
