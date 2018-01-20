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
        sound = prefs.getBoolean("sound", true);
        music = prefs.getBoolean("music", true);
        volSound = MathUtils.clamp(prefs.getFloat("volSound", Constants.DEFAULT_VOLUME), Constants.MIN_VOLUME, Constants.MAX_VOLUME);
        volMusic = MathUtils.clamp(prefs.getFloat("volMusic", Constants.DEFAULT_VOLUME), Constants.MIN_VOLUME, Constants.MAX_VOLUME);

        availableLevels = new ObjectSet<Integer>();
        availableLevels.add(1); // Default level

        // Other levels
        int level = 2;
        boolean availableLevel;
        do {
            availableLevel = prefs.getBoolean("availableLevel_" + level, false);
            if (availableLevel) {
                availableLevels.add(level);
                level++;
            }
        } while (availableLevel);

        Gdx.app.debug(TAG, "sound " + sound);
        Gdx.app.debug(TAG, "music " + music);
        Gdx.app.debug(TAG, "volSound " + volSound);
        Gdx.app.debug(TAG, "volMusic " + volMusic);
        Gdx.app.debug(TAG, "availableLevels " + availableLevels.toString("|"));
   }

    public void save () {
        prefs.putBoolean("sound", sound);
        prefs.putBoolean("music", music);
        prefs.putFloat("volSound", volSound);
        prefs.putFloat("volMusic", volMusic);
        for(Integer level : availableLevels) {
            prefs.putBoolean("availableLevel_" + level, true);
        }
        prefs.flush();
    }
}
