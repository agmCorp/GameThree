package uy.com.agm.gamethree.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ObjectSet;

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

    // Singleton: unique instance
    private static GameSettings instance;

    private boolean sound;
    private boolean music;
    private float volSound;
    private float volMusic;
    private boolean manualShooting;
    private ObjectSet<Integer> availableLevels;
    private Preferences prefs;

    // Singleton: prevent instantiation from other classes
    private GameSettings() {
        prefs = Gdx.app.getPreferences(Constants.SETTINGS);
    }

    // Singleton: retrieve instance
    public static GameSettings getInstance() {
        if (instance == null) {
            instance = new GameSettings();
        }
        return instance;
    }

    public void load () {
        sound = prefs.getBoolean(SOUND, true);
        music = prefs.getBoolean(MUSIC, true);
        volSound = MathUtils.clamp(prefs.getFloat(VOLUME_SOUND, Constants.DEFAULT_VOLUME), Constants.MIN_VOLUME, Constants.MAX_VOLUME);
        volMusic = MathUtils.clamp(prefs.getFloat(VOLUME_MUSIC, Constants.DEFAULT_VOLUME), Constants.MIN_VOLUME, Constants.MAX_VOLUME);
        manualShooting = prefs.getBoolean(MANUAL_SHOOTING, true);

        availableLevels = new ObjectSet<Integer>();
        availableLevels.add(1); // Default level

        // Other levels
        int level = 2;
        boolean availableLevel;
        do {
            availableLevel = prefs.getBoolean(AVAILABLE_LEVEL + level, false) && level <= Constants.MAX_AVAILABLE_LEVEL;
            if (availableLevel) {
                availableLevels.add(level);
                level++;
            }
        } while (availableLevel);

        Gdx.app.debug(TAG, SOUND + " " + sound);
        Gdx.app.debug(TAG, MUSIC + " " + music);
        Gdx.app.debug(TAG, VOLUME_SOUND + " " + volSound);
        Gdx.app.debug(TAG, VOLUME_MUSIC + " " + volMusic);
        Gdx.app.debug(TAG, MANUAL_SHOOTING + " " + manualShooting);
        Gdx.app.debug(TAG, AVAILABLE_LEVEL + " " + availableLevels.toString(", "));
   }

    public void save () {
        prefs.putBoolean(SOUND, sound);
        prefs.putBoolean(MUSIC, music);
        prefs.putFloat(VOLUME_SOUND, volSound);
        prefs.putFloat(VOLUME_MUSIC, volMusic);
        prefs.putBoolean(MANUAL_SHOOTING, manualShooting);
        for(Integer level : availableLevels) {
            prefs.putBoolean(AVAILABLE_LEVEL + level, true);
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

    public ObjectSet<Integer> getAvailableLevels() {
        return availableLevels;
    }

    public void addAvailableLevel(int level) {
        availableLevels.add(level);
    }
}
