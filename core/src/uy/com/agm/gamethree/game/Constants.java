package uy.com.agm.gamethree.game;

/**
 * Created by amorales on 14/12/2017.
 */

public class Constants {
    // ---- Game -----

    // GUI Width (pixels)
    // Visible game world is V_WIDTH / PPM meters wide
    public static final int V_WIDTH = 480;

    // GUI Height (pixels)
    // Visible game world is V_WIDTH / PPM meters tall
    public static final int V_HEIGHT = 800;

    // World step parameters
    public static final float WORLD_TIME_STEP = 1 / 60.0f;
    public static final int WORLD_VELOCITY_ITERATIONS = 6;
    public static final int WORLD_POSITION_ITERATIONS = 2;

    // Each screen is 800px height, so the whole world (TiledEditor) is 8000px.
    public static final int WORLD_SCREENS = 10;

    // Box2D Scale(Pixels Per Meter)
    public static final float PPM = 100;

    // Game cam velocity (m/s)
    public static final float GAMECAM_VELOCITY = 1.0f;

    // Debug mode enabled by default
    public static final boolean DEBUG_MODE = true;

    // ---- Assets ----

    // Location of description file for texture atlas
    public static final String TEXTURE_ATLAS_OBJECTS = "dinamicObjects/dinamicObjects.pack";

    // Location of description file for Bitmap fonts
    public static final String FONT_FILE = "fonts/fonts.fnt";

    // Font sizes
    public static final float FONT_SMALL = 0.8f;
    public static final float FONT_NORMAL = 1.0f;
    public static final float FONT_BIG = 1.5f;

    // Sound FXs
    public static final String FX_FILE_BUMP = "audio/sounds/bump.ogg";
    public static final String FX_FILE_CRACK = "audio/sounds/crack.ogg";
    public static final String FX_FILE_DEAD = "audio/sounds/dead.ogg";
    public static final String FX_FILE_ENEMY_SHOOT = "audio/sounds/enemyShoot.ogg";
    public static final String FX_FILE_HERO_SHOOT = "audio/sounds/heroShoot.ogg";
    public static final String FX_FILE_HIT = "audio/sounds/hit.ogg";
    public static final String FX_FILE_OPEN_POWERBOX = "audio/sounds/openPowerBox.ogg";
    public static final String FX_FILE_PICK_UP_POWERONE = "audio/sounds/pickUpPowerOne.ogg";
    public static final String FX_FILE_PICK_UP_POWERTWO = "audio/sounds/pickUpPowerOne.ogg"; // TODO BUSCAR UN AUDIO? capaz que no es necesario
    public static final String FX_FILE_PICK_UP_POWERTHREE = "audio/sounds/pickUpPowerOne.ogg"; // TODO BUSCAR UN AUDIO? capaz que no es necesario
    public static final String FX_FILE_POWER_DOWN = "audio/sounds/powerDown.ogg";
    public static final String FX_FILE_POWER_TIMER = "audio/sounds/powerTimer.ogg";
    public static final String FX_FILE_SHOW_UP_POWERONE = "audio/sounds/showUpPowerOne.ogg";
    public static final String FX_FILE_SHOW_UP_POWERTWO = "audio/sounds/showUpPowerOne.ogg"; // TODO BUSCAR UN AUDIO
    public static final String FX_FILE_SHOW_UP_POWERTHREE = "audio/sounds/showUpPowerOne.ogg"; // TODO BUSCAR UN AUDIO
    public static final String FX_FILE_FINAL_LEVEL_ONE_POWER_UP = "audio/sounds/finalEnemyLevelOnePowerUp.ogg";
    public static final String FX_FILE_FINAL_LEVEL_ONE_POWER_DOWN = "audio/sounds/finalEnemyLevelOnePowerDown.ogg";

    // Music
    public static final String MUSIC_FILE_LEVEL_ONE = "audio/music/levelOne.ogg";

    // ---- Level ----

    public static final int TIMER_LEVEL_ONE = 180;
    public static final float GAME_OVER_DELAY_SECONDS = 3.0f;

    // ---- Dinamic objects -----

    // Box2D Collision Bits
    public static final short NOTHING_BIT = 0;
    public static final short BORDERS_BIT = 1;
    public static final short EDGES_BIT = 2;
    public static final short OBSTACLE_BIT = 4;
    public static final short HERO_BIT = 8;
    public static final short POWERBOX_BIT = 16;
    public static final short ENEMY_BIT = 32;
    public static final short ITEM_BIT = 64;
    public static final short HERO_WEAPON_BIT = 128;
    public static final short ENEMY_WEAPON_BIT = 256;
    public static final short FINAL_ENEMY_LEVEL_ONE_BIT = 512;
    public static final short SHIELD_BIT = 1024;

    // Hero (meters = pixels * resizeFactor / PPM)
    public static final float HERO_LINEAR_VELOCITY = 5.2f;
    public static final float HERO_CIRCLESHAPE_RADIUS_METERS = 29.0f / PPM;
    public static final float HERO_WIDTH_METERS = 57.0f * 1.0f / PPM;
    public static final float HERO_HEIGHT_METERS = 71.0f * 1.0f / PPM;
    public static final float HERO_DEATH_LINEAR_VELOCITY = 5.0f;
    public static final float HERO_ALPHA_LERP = 0.2f;
    public static final float HERO_FIRE_DELAY_SECONDS = 0.3f;

    // Edge (meters = pixels * resizeFactor / PPM)
    public static final float EDGE_WIDTH_METERS = Constants.V_WIDTH / PPM;
    public static final float EDGE_HEIGHT_METERS = 1.0f * 1.0f / PPM;

    // PowerBox (meters = pixels * resizeFactor / PPM)
    public static final float POWERBOX_CIRCLESHAPE_RADIUS_METERS = 29.0f / PPM;
    public static final float POWERBOX_WIDTH_METERS = 80.0f * 0.6f / PPM;
    public static final float POWERBOX_HEIGHT_METERS = 86.0f * 0.6f / PPM;
    public static final int POWERBOX_SCORE = 10;

    // Item (meters = pixels * resizeFactor / PPM)
    public static final float ITEM_OFFSET_METERS = 40.0f / PPM;

    // Notification (seconds) before power down
    public static final int TIMER_NOTIFICATION = 3;

    // PowerOne (meters = pixels * resizeFactor / PPM)
    public static final float POWERONE_CIRCLESHAPE_RADIUS_METERS = 29.0f / PPM;
    public static final float POWERONE_VELOCITY_X = 0.7f;
    public static final float POWERONE_VELOCITY_Y = 0.0f;
    public static final float POWERONE_WAITING_SECONDS = 5.0f;
    public static final float POWERONE_FADING_SECONDS = 5.0f;
    public static final float POWERONE_WIDTH_METERS = 52.0f * 1.0f / PPM;
    public static final float POWERONE_HEIGHT_METERS = 52.0f * 1.0f / PPM;
    public static final int TIMER_POWERONE = 200;
    public static final int POWERONE_SCORE = 15;
    public static final float POWERONE_FX_WIDTH_METERS = 139.0f * 1.4f / PPM;
    public static final float POWERONE_FX_HEIGHT_METERS = 137.0f * 1.4f / PPM;

    // PowerTwo (meters = pixels * resizeFactor / PPM)
    public static final float POWERTWO_CIRCLESHAPE_RADIUS_METERS = 29.0f / PPM;
    public static final float POWERTWO_VELOCITY_X = 0.0f;
    public static final float POWERTWO_VELOCITY_Y = 0.7f;
    public static final float POWERTWO_WAITING_SECONDS = 5.0f;
    public static final float POWERTWO_FADING_SECONDS = 5.0f;
    public static final float POWERTWO_WIDTH_METERS = 150.0f * 0.5f / PPM;
    public static final float POWERTWO_HEIGHT_METERS = 158.0f * 0.5f / PPM;
    public static final int TIMER_POWERTWO = 20;
    public static final int POWERTWO_SCORE = 30;
    public static final float POWERTWO_FX_WIDTH_METERS = 139.0f * 1.4f / PPM;
    public static final float POWERTWO_FX_HEIGHT_METERS = 137.0f * 1.4f / PPM;

    // PowerThree (meters = pixels * resizeFactor / PPM)
    public static final float POWERTHREE_CIRCLESHAPE_RADIUS_METERS = 29.0f / PPM;
    public static final float POWERTHREE_VELOCITY_X = 1.0f;
    public static final float POWERTHREE_VELOCITY_Y = 1.0f;
    public static final float POWERTHREE_WAITING_SECONDS = 5.0f;
    public static final float POWERTHREE_FADING_SECONDS = 5.0f;
    public static final float POWERTHREE_WIDTH_METERS = 90.0f * 1.0f / PPM;
    public static final float POWERTHREE_HEIGHT_METERS = 120.0f * 1.0f / PPM;
    public static final int TIMER_POWERTHREE = 20;
    public static final int POWERTHREE_SCORE = 30;
    public static final float POWERTHREE_FIRE_DELAY = 0.3f;
    public static final int POWERTHREE_FIRE_BULLETS = 3;
    public static final float POWERTHREE_FIRE_WIDTH_METERS = 90.0f * 0.5f / PPM;
    public static final float POWERTHREE_FIRE_HEIGHT_METERS = 120.0f * 0.5f / PPM;

    // Shield (meters = pixels * resizeFactor / PPM)
    public static final float SHIELD_HEIGHT_METERS = 10.0f * 1.0f / PPM;
    public static final float SHIELD_OFFSETX_METERS = 50.0f * 1.0f / PPM + Constants.HERO_CIRCLESHAPE_RADIUS_METERS;
    public static final float SHIELD_OFFSETY_METERS = 40.0f * 1.0f / PPM + Constants.HERO_CIRCLESHAPE_RADIUS_METERS;

    // EnemyOne (meters = pixels * resizeFactor / PPM)
    public static final float ENEMYONE_CIRCLESHAPE_RADIUS_METERS = 29.0f / PPM;
    public static final float ENEMYONE_VELOCITY_X = 1.0f;
    public static final float ENEMYONE_VELOCITY_Y = -1.0f;
    public static final float ENEMYONE_WIDTH_METERS = 66.0f * 1.0f / PPM;
    public static final float ENEMYONE_HEIGHT_METERS = 55.0f * 1.0f / PPM;
    public static final float ENEMYONE_FIRE_DELAY_SECONDS = 1.0f;
    public static final int ENEMYONE_SCORE = 5;

    // EnemyTwo (meters = pixels * resizeFactor / PPM)
    public static final float ENEMYTWO_CIRCLESHAPE_RADIUS_METERS = 29.0f / PPM;
    public static final float ENEMYTWO_VELOCITY_X = -5.0f;
    public static final float ENEMYTWO_VELOCITY_Y = -2.0f;
    public static final float ENEMYTWO_WIDTH_METERS = 100.0f * 0.8f / PPM;
    public static final float ENEMYTWO_HEIGHT_METERS = 100.0f * 0.8f / PPM;
    public static final float ENEMYTWO_FIRE_DELAY_SECONDS = 1.0f;
    public static final int ENEMYTWO_SCORE = 7;

    // FinalEnemyLevelOne (meters = pixels * resizeFactor / PPM)
    public static final float FINALLEVELONE_CIRCLESHAPE_RADIUS_METERS = 60.0f / PPM;
    public static final float FINALLEVELONE_LINEAR_VELOCITY = 5.0f;
    public static final float FINALLEVELONE_WIDTH_METERS = 177.0f * 1.0f / PPM;
    public static final float FINALLEVELONE_HEIGHT_METERS = 169.0f * 1.0f / PPM;
    public static final float FINALLEVELONE_STATE_MAX_DELAY_SECONDS = 10.0f;
    public static final float FINALLEVELONE_FIRE_DELAY_SECONDS = 0.7f;
    public static final float FINALLEVELONE_POWER_WIDTH_METERS = 192.0f * 1.6f / PPM;
    public static final float FINALLEVELONE_POWER_HEIGHT_METERS = 192.0f * 1.6f / PPM;
    public static final int FINALLEVELONE_MAX_DAMAGE = 3;
    public static final int FINALLEVELONE_SCORE = 200;

    // Weapon (meters = pixels * resizeFactor / PPM)
    public static final float WEAPON_OFFSET_METERS = 64.0f / PPM;

    // HeroBullet (meters = pixels * resizeFactor / PPM)
    public static final float HEROBULLET_VELOCITY_X = 0.0f;
    public static final float HEROBULLET_VELOCITY_Y = 6.0f;
    public static final float HEROBULLET_CIRCLESHAPE_RADIUS_METERS = 15.0f / PPM;
    public static final float HEROBULLET_WIDTH_METERS = 188.0f * 0.5f / PPM;
    public static final float HEROBULLET_HEIGHT_METERS = 175.0f * 0.5f / PPM;

    // EnemyBullet (meters = pixels * resizeFactor / PPM)
    public static final float ENEMYBULLET_CIRCLESHAPE_RADIUS_METERS = 10.0f / PPM;
    public static final float ENEMYBULLET_OFFSET_METERS = 40.0f / PPM;
    public static final float ENEMYBULLET_WIDTH_METERS = 106.0f * 0.5f / PPM;
    public static final float ENEMYBULLET_HEIGHT_METERS = 105.0f  * 0.5f / PPM;
    public static final float ENEMYBULLET_LINEAR_VELOCITY = 4.0f;

    // ExplosionA (meters = pixels * resizeFactor / PPM)
    public static final float EXPLOSIONA_WIDTH_METERS = 167.0f * 0.5f / PPM;
    public static final float EXPLOSIONA_HEIGHT_METERS = 167.0f * 0.5f / PPM;

    // ExplosionB (meters = pixels * resizeFactor / PPM)
    public static final float EXPLOSIONB_WIDTH_METERS = 98.0f * 0.8f / PPM;
    public static final float EXPLOSIONB_HEIGHT_METERS = 125.0f * 0.8f / PPM;

    // ExplosionC (meters = pixels * resizeFactor / PPM)
    public static final float EXPLOSIONC_WIDTH_METERS = 234.0f * 1.0f / PPM;
    public static final float EXPLOSIONC_HEIGHT_METERS = 189.0f * 1.0f / PPM;

    // ExplosionD (meters = pixels * resizeFactor / PPM)
    public static final float EXPLOSIOND_WIDTH_METERS = 205.0f * 1.0f / PPM;
    public static final float EXPLOSIOND_HEIGHT_METERS = 178.0f * 1.0f / PPM;

    // ExplosionE (meters = pixels * resizeFactor / PPM)
    public static final float EXPLOSIONE_WIDTH_METERS = 196.0f * 3.0f / PPM;
    public static final float EXPLOSIONE_HEIGHT_METERS = 178.0f * 3.0f / PPM;

}
