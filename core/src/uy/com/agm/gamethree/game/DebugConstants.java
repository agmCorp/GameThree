package uy.com.agm.gamethree.game;

/**
 * Created by AGM on 5/31/2018.
 */

public class DebugConstants {
    // Boxes around sprites, box2d bodies and scene2d tables
    // Sets the log level to debug
    public static final boolean DEBUG_MODE = false;

    // Show/hide background image
    public static final boolean HIDE_BACKGROUND = false;

    // Show/hide FPS counter
    public static final boolean SHOW_FPS = false;

    // Print box2d bodies count
    public static final boolean DEBUG_BODY_COUNT = false;

    // Enable all levels
    public static final boolean DEBUG_LEVELS = true;

    // The initial position of the camera is set on this value if it's greater than zero
    public static float INITIAL_GAME_CAM_POSITION_Y_METERS = 0.0f;

    // Freezes the camera
    public static boolean STOP_GAME_CAMERA = false;
}
