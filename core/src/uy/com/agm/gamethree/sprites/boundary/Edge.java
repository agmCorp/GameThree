package uy.com.agm.gamethree.sprites.boundary;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.Constants;

/**
 * Created by AGM on 12/31/2017.
 */

public class Edge {
    private static final String TAG = Edge.class.getName();
    private PlayScreen screen;
    private World world;
    private Rectangle bounds;

    private Body b2body;
    private Fixture fixture;

    public Edge(PlayScreen screen, boolean isUpper) {
        this.screen = screen;
        this.world = screen.getWorld();
        if (isUpper) {
            this.bounds = new Rectangle(0, screen.gameCam.position.y + screen.gameViewPort.getWorldHeight() / 2 - Constants.EDGE_HEIGHT_METERS, Constants.EDGE_WIDTH_METERS, Constants.EDGE_HEIGHT_METERS);
        } else {
            this.bounds = new Rectangle(0, screen.gameCam.position.y - screen.gameViewPort.getWorldHeight() / 2, Constants.EDGE_WIDTH_METERS, Constants.EDGE_HEIGHT_METERS);
        }
        defineEdge();

        b2body.setLinearVelocity(0, Constants.GAMECAM_VELOCITY);
    }

    private void defineEdge() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(bounds.getX() + bounds.getWidth() / 2, bounds.getY() + bounds.getHeight() / 2);
        bdef.type = BodyDef.BodyType.KinematicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(bounds.getWidth() / 2, bounds.getHeight() / 2);

        fdef.filter.categoryBits = Constants.EDGES_BIT; // Depicts what this fixture is
        fdef.filter.maskBits = Constants.FINAL_ENEMY_LEVEL_ONE_BIT |
                            Constants.HERO_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void onHit() {
        AudioManager.instance.play(Assets.instance.sounds.bump, 0.3f);
    }

    public Body getB2body() {
        return b2body;
    }

    public void stop() {
        b2body.setLinearVelocity(0.0f, 0.0f);
    }
}