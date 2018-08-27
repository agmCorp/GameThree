package uy.com.agm.gamethree.game;

/**
 * Created by AGM on 5/31/2018.
 */

public class DebugConstants {
    // Master variable: turns debug mode on or off
    public static final boolean TURN_ON_DEBUG = false;

    // Sets the log level to debug
    public static final boolean DEBUG_MODE = true && TURN_ON_DEBUG;

    // Boxes around sprites, box2d bodies and scene2d tables
    public static final boolean DEBUG_LINES = false && TURN_ON_DEBUG;

    // Shows/hides background image
    public static final boolean HIDE_BACKGROUND = false && TURN_ON_DEBUG;

    // Shows/hides FPS counter
    public static final boolean SHOW_FPS = false && TURN_ON_DEBUG;

    // Prints box2d bodies count
    public static final boolean DEBUG_BODY_COUNT = false && TURN_ON_DEBUG;

    // Enables all levels
    public static final boolean DEBUG_LEVELS = true && TURN_ON_DEBUG;

    // if it's greater than zero it sets the initial position of the camera (75.0f to reach the end of the stage)
    public static final float GAME_CAM_Y_METERS = 0.0f * (TURN_ON_DEBUG ? 1 : 0);

    // When it's true, endurance count is ignored
    public static final boolean DISABLE_ENDURANCE_COUNT = false && TURN_ON_DEBUG;

    // Freezes the camera
    public static final boolean STATIC_GAME_CAM = false && TURN_ON_DEBUG;

    // Allows to destroy the FinalEnemies with one hit
    public static final boolean DESTROY_BOSSES_ONE_HIT = false && TURN_ON_DEBUG;
}
