package uy.com.agm.gamethree.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ObjectSet;

/**
 * Created by AGM on 1/18/2018.
 */

public class GameSettings {
    public static final String TAG = GameSettings.class.getName();
    public static final String SOUND = "sound";
    public static final String MUSIC = "music";
    public static final String VOLUME_SOUND = "volSound";
    public static final String VOLUME_MUSIC = "volMusic";
    public static final String AVAILABLE_LEVEL = "availableLevel_";

    public static final GameSettings instance = new GameSettings();
    public boolean sound;
    public boolean music;
    public float volSound;
    public float volMusic;
    public ObjectSet<Integer> availableLevels;

    private Preferences prefs;

    // Singleton: prevent instantiation from other classes
    private GameSettings() {
        prefs = Gdx.app.getPreferences(Constants.SETTINGS);
    }

    public void load () {
        sound = prefs.getBoolean(SOUND, true);
        music = prefs.getBoolean(MUSIC, true);
        volSound = MathUtils.clamp(prefs.getFloat(VOLUME_SOUND, Constants.DEFAULT_VOLUME), Constants.MIN_VOLUME, Constants.MAX_VOLUME);
        volMusic = MathUtils.clamp(prefs.getFloat(VOLUME_MUSIC, Constants.DEFAULT_VOLUME), Constants.MIN_VOLUME, Constants.MAX_VOLUME);

        availableLevels = new ObjectSet<Integer>();
        availableLevels.add(1); // Default level

        // Other levels
        int level = 2;
        boolean availableLevel;
        do {
            availableLevel = prefs.getBoolean(AVAILABLE_LEVEL + level, false);
            if (availableLevel) {
                availableLevels.add(level);
                level++;
            }
        } while (availableLevel);

        Gdx.app.debug(TAG, SOUND + " " + sound);
        Gdx.app.debug(TAG, MUSIC + " " + music);
        Gdx.app.debug(TAG, VOLUME_SOUND + " " + volSound);
        Gdx.app.debug(TAG, VOLUME_MUSIC + " " + volMusic);
        Gdx.app.debug(TAG, AVAILABLE_LEVEL + " " + availableLevels.toString(", "));
   }

    public void save () {
        prefs.putBoolean(SOUND, sound);
        prefs.putBoolean(MUSIC, music);
        prefs.putFloat(VOLUME_SOUND, volSound);
        prefs.putFloat(VOLUME_MUSIC, volMusic);
        for(Integer level : availableLevels) {
            prefs.putBoolean(AVAILABLE_LEVEL + level, true);
        }
        prefs.flush();
    }
}
