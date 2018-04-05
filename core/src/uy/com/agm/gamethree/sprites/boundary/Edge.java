package uy.com.agm.gamethree.sprites.boundary;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamethree.screens.AbstractScreen;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.player.Hero;
import uy.com.agm.gamethree.tools.WorldContactListener;
import uy.com.agm.gamethree.tools.WorldQueryAABB;


/**
 * Created by AGM on 12/31/2017.
 */

public class Edge {
    private static final String TAG = Edge.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = AbstractScreen.V_WIDTH / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 1.0f * 1.0f / PlayScreen.PPM;
    private static final float SENSOR_HEIGHT_METERS = 0.01f; // The thinner the better

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

    public void onBump() {
        Hero hero = screen.getPlayer();
        Vector2 heroPosition = hero.getB2body().getPosition();
        Gdx.app.debug(TAG, "******************** ENTRO AL CHOQUE   ");

        if (b2body.getPosition().y < screen.getGameCam().position.y) { // Bottom edge
            Vector3 v1 = new Vector3(heroPosition.x - Hero.CIRCLE_SHAPE_RADIUS_METERS, heroPosition.y + Hero.CIRCLE_SHAPE_RADIUS_METERS, 0);
            screen.getGameCam().unproject(v1);

            Vector3 v2 = new Vector3(2 * Hero.CIRCLE_SHAPE_RADIUS_METERS, 3, 0);
            screen.getGameCam().unproject(v2);

            world.QueryAABB(WorldQueryAABB.getInstance(), v1.x, v1.y, v2.x, v2.y);

            //heroPosition.x - Hero.CIRCLE_SHAPE_RADIUS_METERS, heroPosition.y + Hero.CIRCLE_SHAPE_RADIUS_METERS, 2 * Hero.CIRCLE_SHAPE_RADIUS_METERS, SENSOR_HEIGHT_METERS);

//            while (foundBodies.size() > 0) {
//                hero.checkSmashing(foundBodies.poll()); // Poll is similar to pop but for a queue, removes the element
//            }
        }
    }
}
