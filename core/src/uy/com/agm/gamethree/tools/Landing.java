package uy.com.agm.gamethree.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.boxes.PowerBox;
import uy.com.agm.gamethree.sprites.tileobjects.IMotionlessObject;
import uy.com.agm.gamethree.sprites.tileobjects.Obstacle;
import uy.com.agm.gamethree.sprites.tileobjects.Path;

/**
 * Created by AGM on 4/1/2018.
 */

public class Landing {
    private static final String TAG = WorldContactListener.class.getName();

    // Constants
    private static final boolean SEARCH_LEFT = false; // Arbitrary
    private static final boolean SEARCH_UP = true; // Arbitrary
    private static final float INCREMENT_X_METERS = 0.4f; // Arbitrary (the thinner the more accurate)
    private static final float INCREMENT_Y_METERS = 0.4f; // Arbitrary (the thinner the more accurate)
    private static final float MARGIN_METERS = 0.2f; // Arbitrary

    private PlayScreen screen;
    private Circle circleHero;
    private WorldQueryAABB worldQueryAABB;
    private Array<Short> categoryBits;
    private Array<Rectangle> boundBodies;

    // Temporary GC friendly vector
    private Vector2 v;

    public Landing(PlayScreen screen) {
        this.screen = screen;
        this.circleHero = new Circle();
        this.circleHero.setRadius(screen.getPlayer().CIRCLE_SHAPE_RADIUS_METERS + MARGIN_METERS);
        this.worldQueryAABB = new WorldQueryAABB(screen);

        // We only check collisions against obstacles, paths and powerBoxes
        this.categoryBits = new Array<Short>();
        this.categoryBits.add(WorldContactListener.OBSTACLE_BIT);
        this.categoryBits.add(WorldContactListener.PATH_BIT);
        this.categoryBits.add(WorldContactListener.POWER_BOX_BIT);

        this.boundBodies = new Array<Rectangle>();
        v = new Vector2();
    }

     /* Check collisions against obstacles, paths and powerBoxes (that is, actors at rest scattered on the current screen).
      * Returns a safe position in meters where to land (collision free) or (-1, -1) otherwise.
      * To work this out, this method considers that Hero is a circle (like its b2body but a bit arbitrarily bigger -see MARGIN_METERS-) and tests
      * every position inside xA, xB, yA, yB starting at (x0, y0) and moving left/right and up/down (see SEARCH_LEFT and SEARCH_UP)
      * using increments defined on INCREMENT_X_METERS and INCREMENT_Y_METERS.
      * Thus, if a solution exists, it's near (x0, y0).
     */
    public Vector2 land() {
        float camX = screen.getGameCam().position.x;
        float camY = screen.getGameCam().position.y;
        float worldWidth = screen.getGameViewPort().getWorldWidth();
        float worldHeight = screen.getGameViewPort().getWorldHeight();

        // Find bodies on the entire current screen
        Array<Fixture> foundBodies = worldQueryAABB.findBodies(categoryBits, camX - worldWidth / 2, camY - worldHeight / 2, camX + worldWidth / 2, camY + worldHeight / 2);
        boundBodies.clear();
        for(Fixture fixture : foundBodies) {
            boundBodies.add(((IMotionlessObject) fixture.getUserData()).getBoundsMeters());
        }

        float x0 = camX;
        float y0 = camY - worldHeight / 4;

        // Excludes Borders and Edges
        float xA = camX - worldWidth / 2 + circleHero.radius;
        float xB = camX + worldWidth / 2 - circleHero.radius;
        float yA = camY - worldHeight / 2 + circleHero.radius;
        float yB = camY + worldHeight / 2 - circleHero.radius;

        v.set(x0, y0);
        searchXY(v, SEARCH_LEFT, SEARCH_UP, INCREMENT_X_METERS, INCREMENT_Y_METERS, xA, xB, yA, yB);
        return v;
    }

    // Returns a safe position where to land, (-1, -1) otherwise.
    // Precondition: see searchY(...) and searchX(...)
    private void searchXY(Vector2 v, boolean left, boolean up, float offsetX, float offsetY, float xA, float xB, float yA, float yB) {
        float x0 = v.x;
        float y0 = v.y;
        boolean end;

        searchY(v, left, up, offsetX, offsetY, xA, xB, yA, yB);
        if (v.y == -1) {
            v.set(x0, y0);
            if (!up) {
                v.y = v.y + offsetY;
                end = v.y > yB - offsetY;
            } else {
                v.y = v.y - offsetY;
                end = v.y < yA;
            }
            if (!end) {
                searchY(v, left, !up, offsetX, offsetY, xA, xB, yA, yB);
                if (v.y == -1) {
                    v.x = -1;
                }
            } else {
                v.x = -1;
                v.y = -1;
            }
        }
    }

    // Returns a safe position where to land, (-1, v.y) otherwise (this method doesn't change v.y)
    // Precondition: v.x >= xA and v.x < xB
    private void searchX(Vector2 v, boolean left, float offset, float xA, float xB) {
        boolean end = false;
        boolean collision = collides(v);

        while (collision && !end) {
            if (left) {
                v.x = v.x - offset;
                end = v.x < xA;
            } else {
                v.x = v.x + offset;
                end = v.x > xB - offset ;
            }
            if (!end) {
                collision = collides(v);
            } else {
                v.x = -1;
            }
        }
    }

    // Returns a safe position where to land, (v.x, -1) otherwise (this method doesn't change v.x)
    // Precondition: v.y >= yA y v.y < yB
    private void searchY(Vector2 v, boolean left, boolean up, float offsetX, float offsetY, float xA, float xB, float yA, float yB) {
        float x0 = v.x;
        boolean end;

        do {
            searchX(v, left, offsetX, xA, xB);
            end = v.x != -1;
            if (!end) {
                if (!left) {
                    v.x = x0 - offsetX;
                    end = v.x < xA;
                } else {
                    v.x = x0 + offsetX;
                    end = v.x > xB - offsetX ;
                }
                if (!end) {
                    searchX(v, !left, offsetX, xA, xB);
                    end = v.x != -1;
                }
                if (!end) {
                    v.x = x0;
                    if (up) {
                        v.y = v.y + offsetY;
                        end = v.y > yB - offsetY;
                    } else {
                        v.y = v.y - offsetY;
                        end = v.y < yA;
                    }
                    if (end) {
                        v.y = -1;
                    }
                }
            }
        } while (!end);
    }

    // Returns true if and only if circleHero overlaps any bound in boundBodies
    private boolean collides(Vector2 v) {
        // Candidate position
        circleHero.setPosition(v.x, v.y);
        boolean collision = false;

        for(Rectangle rectangle : boundBodies) {
            if (Intersector.overlaps(circleHero, rectangle)) {
                collision = true;
                break;
            }
        }
        return collision;
    }
}
