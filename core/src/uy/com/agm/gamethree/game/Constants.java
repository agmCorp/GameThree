package uy.com.agm.gamethree.game;

/**
 * Created by amorales on 14/12/2017.
 */

public class Constants {
    // ---- Game -----

    public static final String TITLE = "Game Three";

    // GUI Width (pixels)
    // Visible game world is V_WIDTH / PPM meters wide
    public static final int V_WIDTH = 480;

    // GUI Height (pixels)
    // Visible game world is V_WIDTH / PPM meters tall
    public static final int V_HEIGHT = 800;

    // World step parameters
    public static final float WORLD_TIME_STEP = 1/300.0f;
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

    // Show/hide background image
    public static final boolean HIDE_BACKGROUND = false;

    public static final String SETTINGS = "uy.com.agm.gameThree.settings";

    // Settings
    public static final float DEFAULT_VOLUME = 0.5f;
    public static final float MIN_VOLUME = 0.0f;
    public static final float MAX_VOLUME = 1.0f;
    public static final int MAX_AVAILABLE_LEVEL = 2;

    // ---- Screens ----
    public static final float PAD_TOP = 40.0f;
    public static final float HUD_BUTTONS_PAD = 20.0f;
    public static final float HUD_CELL_HEIGHT = 20.0f;
    public static final float HUD_PAUSE_WIDTH = 100.0f;
    public static final float HUD_HEALTHBAR_PADBOTTOM = 30.0f;

    public static final String SLIDER_BACKGROUND = "scene2d/slider_background.png";
    public static final String SLIDER_KNOB = "scene2d/slider_knob.png";
    public static final float SLIDER_MIN = 0.0f;
    public static final float SLIDER_MAX = 1.0f;
    public static final float SLIDER_STEP = 0.1f;
    public static final int HEALTHBAR_WIDTH = 200;
    public static final int HEALTHBAR_HEIGHT = 10;
    public static final float HEALTHBAR_MIN = 0.0f;
    public static final float HEALTHBAR_MAX = 100.0f;
    public static final float HEALTHBAR_STEP = 0.01f;
    public static final float HEALTHBAR_ANIMATION_DURATION = 0.25f;

    // ---- Assets ----

    // Location of description file for texture atlas
    public static final String TEXTURE_ATLAS_OBJECTS = "dinamicObjects/dinamicObjects.atlas";

    // Location of description file for Bitmap fonts
    public static final String FONT_FILE = "fonts/fonts.fnt";

    // Location of the map file for each level
    public static final String MAP_FILE_LEVEL_ONE = "levelOne/levelOne.tmx";
    public static final String MAP_FILE_LEVEL_TWO = "levelTwo/levelTwo.tmx";

    // Font sizes
    public static final float FONT_SMALL = 0.6f;
    public static final float FONT_NORMAL = 0.9f;
    public static final float FONT_BIG = 1.6f;

    // Sound FXs
    public static final String FX_FILE_BUMP = "audio/sounds/bump.ogg";
    public static final String FX_FILE_CRACK = "audio/sounds/crack.ogg";
    public static final String FX_FILE_DEAD = "audio/sounds/dead.ogg";
    public static final String FX_FILE_ENEMY_SHOOT = "audio/sounds/enemyShoot.ogg";
    public static final String FX_FILE_HERO_SHOOT = "audio/sounds/heroShoot.ogg";
    public static final String FX_FILE_HIT = "audio/sounds/hit.ogg";
    public static final String FX_FILE_OPEN_POWERBOX = "audio/sounds/openPowerBox.ogg";
    public static final String FX_FILE_CLOCK = "audio/sounds/clock.ogg";
    public static final String FX_FILE_PICK_UP_COLONE = "audio/sounds/pickUpColOne.ogg";
    public static final String FX_FILE_PICK_UP_POWERONE = "audio/sounds/pickUpPowerOne.ogg";
    public static final String FX_FILE_PICK_UP_POWERTWO = "audio/sounds/pickUpPowerTwo.ogg";
    public static final String FX_FILE_PICK_UP_POWERTHREE = "audio/sounds/pickUpPowerThree.ogg";
    public static final String FX_FILE_PICK_UP_POWERFOUR = "audio/sounds/pickUpPowerFour.ogg";
    public static final String FX_FILE_POWER_DOWN = "audio/sounds/powerDown.ogg";
    public static final String FX_FILE_BEEP_A = "audio/sounds/beepA.ogg";
    public static final String FX_FILE_BEEP_B = "audio/sounds/beepB.ogg";
    public static final String FX_FILE_SHOW_UP_COLONE = "audio/sounds/showUpColOne.ogg";
    public static final String FX_FILE_SHOW_UP_POWERONE = "audio/sounds/showUpPowerOne.ogg";
    public static final String FX_FILE_SHOW_UP_POWERTWO = "audio/sounds/showUpPowerTwo.ogg";
    public static final String FX_FILE_SHOW_UP_POWERTHREE = "audio/sounds/showUpPowerThree.ogg";
    public static final String FX_FILE_SHOW_UP_POWERFOUR = "audio/sounds/showUpPowerFour.ogg";
    public static final String FX_FILE_TIME_IS_UP = "audio/sounds/timeIsUp.ogg";
    public static final String FX_FILE_FINAL_LEVEL_ONE_POWER_UP = "audio/sounds/finalEnemyLevelOnePowerUp.ogg";
    public static final String FX_FILE_FINAL_LEVEL_ONE_POWER_DOWN = "audio/sounds/finalEnemyLevelOnePowerDown.ogg";
    public static final String FX_FILE_FINAL_LEVEL_ONE_EXPLOSION = "audio/sounds/finalEnemyLevelOneExplosion.ogg";
    public static final String FX_FILE_FINAL_LEVEL_ONE_HIT = "audio/sounds/finalEnemyLevelOneHit.ogg";
    public static final String FX_FILE_FINAL_LEVEL_ONE_INTRO = "audio/sounds/fear.ogg";
    public static final String FX_FILE_LEVEL_COMPLETED = "audio/sounds/levelCompleted.ogg";
    public static final String FX_FILE_BOUNCE = "audio/sounds/boing.ogg";
    public static final String FX_FILE_CLICK = "audio/sounds/click.ogg";
    public static final String FX_FILE_APLAUSE = "audio/sounds/aplause.ogg";
    public static final String FX_FILE_SQUISH = "audio/sounds/squish.ogg";

    // Max volume
    public static final float SHOOT_MAX_VOLUME = 0.3f;
    public static final float HIT_MAX_VOLUME = 0.3f;

    // Music
    public static final String MUSIC_FILE_MAIN_MENU = "audio/music/mainMenu.ogg";
    public static final String MUSIC_FILE_LEVEL_ONE = "audio/music/levelOne.ogg";
    public static final String MUSIC_FILE_LEVEL_TWO = "audio/music/levelTwo.ogg";

    // ---- Level ----

    public static final int TIMER_LEVEL_ONE = 180;
    public static final int TIMER_LEVEL_TWO = 30;
    public static final float GAME_OVER_DELAY_SECONDS = 3.0f;
    public static final float PLAY_AGAIN_DELAY_SECONDS = 4.0f;
    public static final float LEVEL_COMPLETED_DELAY_SECONDS = 6.0f;
    public static final int LEVEL_TIMER_NOTIFICATION = 10;

    // ---- Dinamic objects -----

    // Box2D Collision Bits
    public static final short NOTHING_BIT = 0;
    public static final short BORDERS_BIT = 1;
    public static final short EDGES_BIT = 2;
    public static final short OBSTACLE_BIT = 4;
    public static final short HERO_BIT = 8;
    public static final short HERO_GHOST_BIT = 16;
    public static final short HERO_TOUGH_BIT = 32;
    public static final short POWERBOX_BIT = 64;
    public static final short ENEMY_BIT = 128;
    public static final short ITEM_BIT = 256;
    public static final short HERO_WEAPON_BIT = 512;
    public static final short ENEMY_WEAPON_BIT = 1024;
    public static final short FINAL_ENEMY_BIT = 2048;
    public static final short SHIELD_BIT = 4096;

    // Hero (meters = pixels * resizeFactor / PPM)
    public static final float HERO_LINEAR_VELOCITY = 5.2f;
    public static final float HERO_CIRCLESHAPE_RADIUS_METERS = 32.0f / PPM;
    public static final float HERO_WIDTH_METERS = 128.0f * 0.7f / PPM;
    public static final float HERO_HEIGHT_METERS = 128.0f * 0.7f / PPM;
    public static final float HERO_DEATH_LINEAR_VELOCITY = 5.0f;
    public static final float HERO_ALPHA_LERP = 0.2f;
    public static final float HERO_FIRE_DELAY_SECONDS = 0.3f;
    public static final int HERO_LIVES_START = 3;
    public static final float HERO_PLAY_AGAIN_WARM_UP_TIME = 2.0f;

    // Edge (meters = pixels * resizeFactor / PPM)
    public static final float EDGE_WIDTH_METERS = Constants.V_WIDTH / PPM;
    public static final float EDGE_HEIGHT_METERS = 1.0f * 1.0f / PPM;

    // PowerBox (meters = pixels * resizeFactor / PPM)
    public static final float POWERBOX_CIRCLESHAPE_RADIUS_METERS = 29.0f / PPM;
    public static final float POWERBOX_WIDTH_METERS = 140.0f * 0.3f / PPM;
    public static final float POWERBOX_HEIGHT_METERS = 175.0f * 0.3f / PPM;
    public static final int POWERBOX_SCORE = 10;
    public static final int POWERBOX_MAX_TEXTURES = 14;

    // Item (meters = pixels * resizeFactor / PPM)
    public static final float ITEM_OFFSET_METERS = 40.0f / PPM;

    // Notification (seconds) before power down
    public static final int POWER_TIMER_NOTIFICATION = 5;
    public static final float SPRITE_BLINKING_INTERVAL_SECONDS = 0.1f;

    // ColOne (meters = pixels * resizeFactor / PPM)
    public static final float COLONE_CIRCLESHAPE_RADIUS_METERS = 29.0f / PPM;
    public static final float COLONE_VELOCITY_X = 0.7f;
    public static final float COLONE_VELOCITY_Y = 0.0f;
    public static final float COLONE_WAITING_SECONDS = 1.0f;
    public static final float COLONE_FADING_SECONDS = 1.0f;
    public static final float COLONE_WIDTH_METERS = 77.0f * 0.5f / PPM;
    public static final float COLONE_HEIGHT_METERS = 77.0f * 0.5f / PPM;
    public static final int COLONE_SCORE = 25;

    // PowerOne (meters = pixels * resizeFactor / PPM)
    public static final float POWERONE_CIRCLESHAPE_RADIUS_METERS = 29.0f / PPM;
    public static final float POWERONE_VELOCITY_X = 0.7f;
    public static final float POWERONE_VELOCITY_Y = 0.0f;
    public static final float POWERONE_WAITING_SECONDS = 5.0f;
    public static final float POWERONE_FADING_SECONDS = 5.0f;
    public static final float POWERONE_WIDTH_METERS = 52.0f * 1.0f / PPM;
    public static final float POWERONE_HEIGHT_METERS = 52.0f * 1.0f / PPM;
    public static final int TIMER_POWERONE = 20;
    public static final int POWERONE_SCORE = 15;
    public static final float POWERONE_FX_WIDTH_METERS = 192.0f * 1.2f / PPM;
    public static final float POWERONE_FX_HEIGHT_METERS = 192.0f * 1.2f / PPM;

    // PowerTwo (meters = pixels * resizeFactor / PPM)
    public static final float POWERTWO_CIRCLESHAPE_RADIUS_METERS = 29.0f / PPM;
    public static final float POWERTWO_VELOCITY_X = 0.0f;
    public static final float POWERTWO_VELOCITY_Y = 0.7f;
    public static final float POWERTWO_WAITING_SECONDS = 5.0f;
    public static final float POWERTWO_FADING_SECONDS = 5.0f;
    public static final float POWERTWO_WIDTH_METERS = 150.0f * 0.4f / PPM;
    public static final float POWERTWO_HEIGHT_METERS = 158.0f * 0.4f / PPM;
    public static final int TIMER_POWERTWO = 20;
    public static final int POWERTWO_SCORE = 30;
    public static final float POWERTWO_FX_WIDTH_METERS = 192.0f * 1.2f / PPM;
    public static final float POWERTWO_FX_HEIGHT_METERS = 192.0f * 1.2f / PPM;

    // PowerThree (meters = pixels * resizeFactor / PPM)
    public static final float POWERTHREE_CIRCLESHAPE_RADIUS_METERS = 29.0f / PPM;
    public static final float POWERTHREE_VELOCITY_X = 1.0f;
    public static final float POWERTHREE_VELOCITY_Y = 1.0f;
    public static final float POWERTHREE_WAITING_SECONDS = 5.0f;
    public static final float POWERTHREE_FADING_SECONDS = 5.0f;
    public static final float POWERTHREE_WIDTH_METERS = 63.0f * 0.7f / PPM;
    public static final float POWERTHREE_HEIGHT_METERS = 64.0f * 0.7f / PPM;
    public static final int TIMER_POWERTHREE = 20;
    public static final int POWERTHREE_SCORE = 30;
    public static final float POWERTHREE_FIRE_DELAY_SECONDS = 0.3f;
    public static final int POWERTHREE_MAX_BULLETS = 6;
    public static final float POWERTHREE_BULLET_WIDTH_METERS = 39.0f * 1.0f / PPM;
    public static final float POWERTHREE_BULLET_HEIGHT_METERS = 102.0f * 1.0f / PPM;
    public static final float POWERTHREE_BULLET_CIRCLESHAPERADIUS_METERS = 30.0f / PPM;

    // PowerFour (meters = pixels * resizeFactor / PPM)
    public static final float POWERFOUR_CIRCLESHAPE_RADIUS_METERS = 29.0f / PPM;
    public static final float POWERFOUR_VELOCITY_X = 0.0f;
    public static final float POWERFOUR_VELOCITY_Y = 0.7f;
    public static final float POWERFOUR_WAITING_SECONDS = 5.0f;
    public static final float POWERFOUR_FADING_SECONDS = 5.0f;
    public static final float POWERFOUR_WIDTH_METERS = 192.0f * 0.5f / PPM;
    public static final float POWERFOUR_HEIGHT_METERS = 192.0f * 0.5f / PPM;
    public static final int TIMER_POWERFOUR = 20;
    public static final int POWERFOUR_SCORE = 30;
    public static final float POWERFOUR_FX_WIDTH_METERS = 192.0f * 1.2f / PPM;
    public static final float POWERFOUR_FX_HEIGHT_METERS = 192.0f * 1.2f / PPM;

    // Shield (meters = pixels * resizeFactor / PPM)
    public static final float SHIELD_HEIGHT_METERS = 10.0f * 1.0f / PPM;
    public static final float SHIELD_OFFSETX_METERS = 50.0f * 1.0f / PPM + Constants.HERO_CIRCLESHAPE_RADIUS_METERS;
    public static final float SHIELD_OFFSETY_METERS = 40.0f * 1.0f / PPM + Constants.HERO_CIRCLESHAPE_RADIUS_METERS;

    // EnemyOne (meters = pixels * resizeFactor / PPM)
    public static final float ENEMYONE_CIRCLESHAPE_RADIUS_METERS = 29.0f / PPM;
    public static final float ENEMYONE_VELOCITY_X = 1.0f;
    public static final float ENEMYONE_VELOCITY_Y = -1.0f;
    public static final float ENEMYONE_WIDTH_METERS = 120.0f * 0.6f / PPM;
    public static final float ENEMYONE_HEIGHT_METERS = 90.0f * 0.6f / PPM;
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

    // EnemyThree (meters = pixels * resizeFactor / PPM)
    public static final float ENEMYTHREE_CIRCLESHAPE_RADIUS_METERS = 29.0f / PPM;
    public static final float ENEMYTHREE_VELOCITY_X = 0.0f;
    public static final float ENEMYTHREE_VELOCITY_Y = 0.0f;
    public static final float ENEMYTHREE_WIDTH_METERS = 130.0f * 0.6f / PPM;
    public static final float ENEMYTHREE_HEIGHT_METERS = 130.0f * 0.6f / PPM;
    public static final float ENEMYTHREE_FIRE_DELAY_SECONDS = 2.0f;
    public static final int ENEMYTHREE_SCORE = 17;

    // EnemyFour (meters = pixels * resizeFactor / PPM)
    public static final float ENEMYFOUR_CIRCLESHAPE_RADIUS_METERS = 40.0f / PPM;
    public static final float ENEMYFOUR_LINEAR_VELOCITY = 3.0f;
    public static final float ENEMYFOUR_WIDTH_METERS = 125.0f * 0.8f / PPM;
    public static final float ENEMYFOUR_HEIGHT_METERS = 106.0f * 0.8f / PPM;
    public static final float ENEMYFOUR_FROZEN_WIDTH_METERS = 123.0f * 0.8f / PPM;
    public static final float ENEMYFOUR_FROZEN_HEIGHT_METERS = 99.0f * 0.8f / PPM;
    public static final float ENEMYFOUR_AMPLITUDE_METERS = 200.0f / PPM;
    public static final float ENEMYFOUR_WAVELENGTH_METERS = 100.0f / PPM;
    public static final float ENEMYFOUR_FIRE_DELAY_SECONDS = 1.0f;
    public static final float ENEMYFOUR_FROZEN_TIME_SECONDS = 4.0f;
    public static final int ENEMYFOUR_SCORE = 35;

    // FinalEnemyLevelOne (meters = pixels * resizeFactor / PPM)
    public static final float FINALLEVELONE_CIRCLESHAPE_RADIUS_METERS = 60.0f / PPM;
    public static final float FINALLEVELONE_LINEAR_VELOCITY = 5.0f;
    public static final float FINALLEVELONE_DENSITY = 1000.0f;
    public static final float FINALLEVELONE_WIDTH_METERS = 177.0f * 1.0f / PPM;
    public static final float FINALLEVELONE_HEIGHT_METERS = 169.0f * 1.0f / PPM;
    public static final float FINALLEVELONE_STATE_MAX_TIME_SECONDS = 10.0f;
    public static final float FINALLEVELONE_FIRE_DELAY_SECONDS = 0.7f;
    public static final float FINALLEVELONE_INTRO_TIME_SECONDS = 5.0f;
    public static final float FINALLEVELONE_POWER_WIDTH_METERS = 192.0f * 1.6f / PPM;
    public static final float FINALLEVELONE_POWER_HEIGHT_METERS = 192.0f * 1.6f / PPM;
    public static final int FINALLEVELONE_MAX_DAMAGE = 3;
    public static final int FINALLEVELONE_SCORE = 200;

    // HeroBullet (meters = pixels * resizeFactor / PPM)
    public static final float HEROBULLET_OFFSET_METERS = HERO_CIRCLESHAPE_RADIUS_METERS;
    public static final float HEROBULLET_VELOCITY_X = 0.0f;
    public static final float HEROBULLET_VELOCITY_Y = 6.0f;
    public static final float HEROBULLET_CIRCLESHAPE_RADIUS_METERS = 15.0f / PPM;
    public static final float HEROBULLET_WIDTH_METERS = 192.0f * 0.6f / PPM;
    public static final float HEROBULLET_HEIGHT_METERS = 192.0f * 0.6f / PPM;

    // EnemyBullet (meters = pixels * resizeFactor / PPM)
    public static final float ENEMYBULLET_CIRCLESHAPE_RADIUS_METERS = 10.0f / PPM;
    public static final float ENEMYBULLET_OFFSET_METERS = 40.0f / PPM;
    public static final float ENEMYBULLET_WIDTH_METERS = 192.0f * 0.3f / PPM;
    public static final float ENEMYBULLET_HEIGHT_METERS = 192.0f  * 0.3f / PPM;
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
