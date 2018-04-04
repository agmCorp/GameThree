package uy.com.agm.gamethree.sprites.tileobjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.boxes.PowerBox;
import uy.com.agm.gamethree.sprites.player.Hero;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 12/4/2017.
 */

public class Obstacle extends InteractiveTileObject {
    private static final String TAG = Obstacle.class.getName();

    public Obstacle(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(WorldContactListener.OBSTACLE_BIT);
    }

    @Override
    public void onBump() {
        AudioManager.getInstance().play(Assets.getInstance().getSounds().getBump());
        screen.getPlayer().checkSmashing();
    }


    private void checkSmash(Fixture fixture) {
        Rectangle sensor = new Rectangle(); // todo

        // ------------------------------------
        final float SENSOR_HEIGHT = 0.01f; // The thinner the better
        final float OFFSET = 1.0f;
        Rectangle boundsMeters;

        Hero hero = screen.getPlayer();
        Vector2 heroPosition = hero.getB2body().getPosition();
        Circle circleHero = new Circle(heroPosition.x, heroPosition.y, Hero.CIRCLE_SHAPE_RADIUS_METERS);
        float upperEdgeHero = heroPosition.y + Hero.CIRCLE_SHAPE_RADIUS_METERS;

        switch (fixture.getFilterData().categoryBits) {
            case WorldContactListener.OBSTACLE_BIT:
                boundsMeters = ((Obstacle) fixture.getUserData()).getBoundsMeters();
                break;
            case WorldContactListener.PATH_BIT:
                boundsMeters = ((Obstacle) fixture.getUserData()).getBoundsMeters();
                break;
            case WorldContactListener.POWER_BOX_BIT:
                boundsMeters = ((Obstacle) fixture.getUserData()).getBoundsMeters();
                break;
            default:
                boundsMeters = null;
                break;
        }
        sensor.set(boundsMeters.getX(), boundsMeters.getY() - SENSOR_HEIGHT, boundsMeters.getWidth(), SENSOR_HEIGHT);
        if (Intersector.overlaps(circleHero, sensor)) {
            float startX = screen.getBottomEdge().getB2body().getPosition().x;
            float startY = screen.getBottomEdge().getB2body().getPosition().y;

            if (Intersector.distanceLinePoint(startX, startY, startX + OFFSET, startY, heroPosition.x, upperEdgeHero) <= 2 * Hero.CIRCLE_SHAPE_RADIUS_METERS) {
                hero.onDead();
            }
        }
    }
}
