package uy.com.agm.gamethree.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import uy.com.agm.gamethree.assets.sprites.AssetHero;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.tileobjects.Obstacle;

/**
 * Created by AGM on 4/1/2018.
 */

public class Landing {
    private static final String TAG = WorldContactListener.class.getName();

    // Constants
    private static final float INCREMENT_X_METERS = 1.0f; // todo
    private static final float INCREMENT_Y_METERS = 1.0f; // todo
    private static final boolean SEARCH_LEFT = false;
    private static final boolean SEARCH_UP = true;

    private PlayScreen screen;

    // Temporary GC friendly rectangle
    private Rectangle rectangleTmp;

    // Temporary GC friendly vector
    private Vector2 v;

    public Landing(PlayScreen screen) {
        this.screen = screen;
        rectangleTmp = new Rectangle();
        v = new Vector2();
    }

    // retorna -1, -1 si no hay solucion. Quien lo llama tiene que hacer el if.
    public Vector2 land() {
        float camX = screen.getGameCam().position.x;
        float camY = screen.getGameCam().position.y;
        float worldWidth = screen.getGameViewPort().getWorldWidth();
        float worldHeight = screen.getGameViewPort().getWorldHeight();

        float x0 = camX;
        float y0 = camY - worldHeight / 4;
        float xA = camX - worldWidth / 2;
        float xB = camX + worldWidth / 2;
        float yA = camY - worldHeight / 2;
        float yB = camY + worldHeight / 2;

        v.set(x0, y0);
        searchXY(v, SEARCH_LEFT, SEARCH_UP, INCREMENT_X_METERS, INCREMENT_Y_METERS, xA, xB, yA, yB);
        return v;
    }

    // precondicion la de searchY y la de searchX
    // retorna (-1, -1) si no encuentra
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

    // retorna x = -1 si no encuentra (y lo deja como estaba).
    // precondicion v.x >= xA y v.x < xB
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

    // retorna y = -1 si no encuentra (x lo deja como estaba)
    // precondicion la de searchX
    // ademas v.y >= yA y v.y < yB
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

    // true si colisiona
    // false si no colisiona
    private boolean collides(Vector2 v) {
        // BORDER EDGE OBSTACLE PATH POWERBOX
        Gdx.app.debug(TAG, "************ (" + v.x + ", " + v.y + ")");

        rectangleTmp.set(v.x, v.y, AssetHero.WIDTH_METERS, AssetHero.HEIGHT_METERS);
        boolean collision = false;
/*
        // Borders
        if (!collision) {
            for (Border border : screen.getCreator().getBorders()) {
                if (rectangleTmp.overlaps(border.getBoundsMeters())) {
                    collision = true;
                    break;
                }
            }
        }

        // Edges
        if (!collision) {
            for (Edge edge : screen.getCreator().getEdges()) {
                if (rectangleTmp.overlaps(edge.getBoundsMeters())) {
                    collision = true;
                    break;
                }
            }
        }
*/
        // Obstacles
        if (!collision) {
            for (Obstacle obstacle : screen.getCreator().getObstacles()) {
                Gdx.app.debug(TAG, "************ OBSTACULO " + obstacle.getBoundsMeters().x + ", " + obstacle.getBoundsMeters().y + ", " + obstacle.getBoundsMeters().getWidth() + ", " + obstacle.getBoundsMeters().getHeight());
                if (rectangleTmp.overlaps(obstacle.getBoundsMeters())) {
                    collision = true;
                    break;
                }
            }
        }
/*
        // Paths
        if (!collision) {
            for (Path path : screen.getCreator().getPaths()) {
                if (rectangleTmp.overlaps(path.getBoundsMeters())) {
                    collision = true;
                    break;
                }
            }
        }

        // PowerBoxes
        if (!collision) {
            for (PowerBox powerBox : screen.getCreator().getPowerBoxes()) {
                if (rectangleTmp.overlaps(powerBox.getBoundsMeters())) {
                    collision = true;
                    break;
                }
            }
        }
*/
        return !collision;
    }
}
