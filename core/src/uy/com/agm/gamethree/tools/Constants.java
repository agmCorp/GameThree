package uy.com.agm.gamethree.tools;

/**
 * Created by amorales on 14/12/2017.
 */

public class Constants {
    // TODO HACER TODO ESTO.

    // Visible game world is 5 meters wide
    public static final float VIEWPORT_WIDTH = 5.0f;

    // Visible game world is 5 meters tall
    public static final float VIEWPORT_HEIGHT = 5.0f;

    // GUI Width
    public static final float VIEWPORT_GUI_WIDTH = 800.0f;

    // GUI Height
    public static final float VIEWPORT_GUI_HEIGHT = 480.0f;
//---------
    // Location of description file for texture atlas
    public static final String TEXTURE_ATLAS_OBJECTS = "characters/characters.pack";

    // Hero's speed restrictions
    public static final float WEIGHTING_HERO_SPEED = 0.18f;
    public static final float LEN_HERO_SPEED = 5.0f;
    public static final float MAX_LINEAR_VELOCITY = 400f;
    public static final float LINEAR_VELOCITY = 4.0f;

    // GUI Width (pixels)
    // Visible game world is V_WIDTH / PPM meters wide
    public static final int V_WIDTH = 480;

    // GUI Height (pixels)
    // Visible game world is V_WIDTH / PPM meters tall
    public static final int V_HEIGHT = 800;

    // Box2D Scale(Pixels Per Meter)
    public static final float PPM = 100;

    // Box2D Collision Bits
    public static final short BORDERS_BIT = 1;
    public static final short HERO_BIT = 2;
    public static final short OBSTACLE_BIT = 4;
    public static final short POWERBOX_BIT = 8;
    public static final short ENEMY_BIT = 16;
    public static final short ITEM_BIT = 32;

    // Debug boundaries enabled by default
    public static final boolean DEBUG_BOUNDARIES = true;

    // Game cam velocity
    public static final float GAMECAM_VELOCITY = 0.5f;
}
