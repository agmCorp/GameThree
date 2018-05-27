package uy.com.agm.gamethree.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import uy.com.agm.gamethree.actors.player.Hero;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.LevelFactory;

/**
 * Created by AGM on 1/18/2018.
 */

public class GameSettings {
    private static final String TAG = GameSettings.class.getName();

    // Constants
    private static final String SOUND = "sound";
    private static final String MUSIC = "music";
    private static final String VOLUME_SOUND = "volSound";
    private static final String VOLUME_MUSIC = "volMusic";
    private static final String MANUAL_SHOOTING = "manualShooting";
    private static final String AVAILABLE_LEVEL = "availableLevel_";
    private static final String SETTINGS = "uy.com.agm.gameThree.settings";
    private static final float DEFAULT_VOLUME = 0.5f;
    private static final float MIN_VOLUME = 0.0f;
    private static final float MAX_VOLUME = 1.0f;
    public static final int MAX_AVAILABLE_LEVEL = 4;

    // Singleton: unique instance
    private static GameSettings instance;

    private boolean sound;
    private boolean music;
    private float volSound;
    private float volMusic;
    private boolean manualShooting;
    private Array<GameState> availableLevels;
    private Preferences prefs;
    private Json json;

    // Singleton: prevent instantiation from other classes
    private GameSettings() {
        prefs = Gdx.app.getPreferences(SETTINGS);
        json = new Json();
    }

    // Singleton: retrieve instance
    public static GameSettings getInstance() {
        if (instance == null) {
            instance = new GameSettings();
        }
        return instance;
    }
/*
    private String getData(GameState gameState) {
        return getData(gameState.getLives(), gameState.getScore(), gameState.getSkulls(), gameState.isActive());
    }

    private String getData(int lives, int score, int skulls, boolean debug) {
        return lives + SEPARATOR + score + SEPARATOR + skulls + SEPARATOR + debug;
    }

    private int getLives(String data) {
        return Integer.parseInt(data.split(SEPARATOR)[0]);
    }

    private int getScore(String data) {
        return Integer.parseInt(data.split(SEPARATOR)[1]);
    }

    private int getSkulls(String data) {
        return Integer.parseInt(data.split(SEPARATOR)[2]);
    }

    private boolean getDebug(String data) {
        return Boolean.parseBoolean(data.split(SEPARATOR)[3]);
    }
*/
    public void load () {
        sound = prefs.getBoolean(SOUND, true);
        music = prefs.getBoolean(MUSIC, true);
        volSound = MathUtils.clamp(prefs.getFloat(VOLUME_SOUND, DEFAULT_VOLUME), MIN_VOLUME, MAX_VOLUME);
        volMusic = MathUtils.clamp(prefs.getFloat(VOLUME_MUSIC, DEFAULT_VOLUME), MIN_VOLUME, MAX_VOLUME);
        manualShooting = prefs.getBoolean(MANUAL_SHOOTING, true);

        int level = 1; // Default level
        GameState gameState = new GameState(level, Hero.LIVES_START, 0, LevelFactory.getLevelSkulls(level), true);
        availableLevels = new Array<GameState>();
        availableLevels.add(gameState);

        // Other levels
        level++;
        String data;
        boolean availableLevel;
        do {
            data = prefs.getString(AVAILABLE_LEVEL + level, "");
            if (data.isEmpty()) {
                if (level <= MAX_AVAILABLE_LEVEL) {
                    gameState = new GameState(level, Hero.LIVES_START, 0, LevelFactory.getLevelSkulls(level), false);
                }
            }

            if (availableLevel) {
                gameState = new GameState(level, getLives(data), getScore(data), getSkulls(data), getDebug(data));
                availableLevels.add(gameState);
                level++;
            }



        } while (availableLevel);
   }

    public void save () {
        prefs.putBoolean(SOUND, sound);
        prefs.putBoolean(MUSIC, music);
        prefs.putFloat(VOLUME_SOUND, volSound);
        prefs.putFloat(VOLUME_MUSIC, volMusic);
        prefs.putBoolean(MANUAL_SHOOTING, manualShooting);
        for(GameState gameState : availableLevels) {
            prefs.putString(AVAILABLE_LEVEL + gameState.getLevel(), getData(gameState));
        }
        prefs.flush();
    }

    public boolean isSound() {
        return sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }

    public boolean isMusic() {
        return music;
    }

    public void setMusic(boolean music) {
        this.music = music;
    }

    public float getVolSound() {
        return volSound;
    }

    public void setVolSound(float volSound) {
        this.volSound = volSound;
    }

    public float getVolMusic() {
        return volMusic;
    }

    public void setVolMusic(float volMusic) {
        this.volMusic = volMusic;
    }

    public boolean isManualShooting() {
        return manualShooting;
    }

    public void setManualShooting(boolean manualShooting) {
        this.manualShooting = manualShooting;
    }

    public Array<GameState> getAvailableLevels() {
        return availableLevels;
    }

    public void addAvailableLevel(int level, int lives, int score, int skulls) {
        GameState gameState = new GameState(level, lives, score, skulls, false);

        // todo debo verificar que no exista antes debo usar un hasmap o algo de eso O1 private ObjectMap<String, DynamicHelpDef> dynamicHelp;
        //if (dynamicHelp.containsKey(className)){
        //    // Keeps a sound filename and the last playing time in nanoseconds.
        //private ArrayMap<String, Long> trackSounds;
        // el put si existe actualiza
        availableLevels.add(gameState);
    }
}
