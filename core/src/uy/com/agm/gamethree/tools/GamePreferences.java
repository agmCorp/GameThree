package uy.com.agm.gamethree.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

import uy.com.agm.gamethree.game.Constants;

/**
 * Created by AGM on 1/18/2018.
 */

public class GamePreferences {
    public static final String TAG = GamePreferences.class.getName();

    public static final GamePreferences instance = new GamePreferences();
    public boolean sound;
    public boolean music;
    public float volSound;
    public float volMusic;

    private Preferences prefs;

    // singleton: prevent instantiation from other classes
    private GamePreferences () {
        prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
    }

    public void load () {
        sound = prefs.getBoolean("sound", true);
        music = prefs.getBoolean("music", true);
        volSound = MathUtils.clamp(prefs.getFloat("volSound", Constants.DEFAULT_VOLUME), Constants.MIN_VOLUME, Constants.MAX_VOLUME);
        volMusic = MathUtils.clamp(prefs.getFloat("volMusic", Constants.DEFAULT_VOLUME), Constants.MIN_VOLUME, Constants.MAX_VOLUME);
   }

    public void save () {
        prefs.putBoolean("sound", sound);
        prefs.putBoolean("music", music);
        prefs.putFloat("volSound", volSound);
        prefs.putFloat("volMusic", volMusic);
        prefs.flush();
    }
}
