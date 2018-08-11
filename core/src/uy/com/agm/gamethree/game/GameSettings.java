package uy.com.agm.gamethree.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Date;

/**
 * Created by AGM on 16/07/2018.
 */

public class GameSettings {
    private static final String TAG = GameSettings.class.getName();

    // Constants
    private static final String SETTINGS = "uy.com.agm.gameThree.settings";
    private static final String SOUND = "sound";
    private static final String MUSIC = "music";
    private static final String VOLUME_SOUND = "volSound";
    private static final String VOLUME_MUSIC = "volMusic";
    private static final String MANUAL_SHOOTING = "manualShooting";
    private static final String HIGH_SCORE = "highScore_";
    private static final String LEVEL_STATE = "levelState_";
    private static final String PROGRESS = "progress";
    private static final float MIN_VOLUME = 0.0f;
    private static final float MAX_VOLUME = 1.0f;
    public static final int MAX_LEVEL = 4;
    public static final int MAX_RANKING = 5;
    public static final float DEFAULT_VOLUME = 0.5f;

    // High scores
    private static final int GOLD_HIGH_SCORE = 21365;
    private static final long GOLD_HIGH_SCORE_MILLIS = 222168600000L; // 15/01/1977 Mi cumple \(^-^)/
    private static final int SILVER_HIGH_SCORE = 18491;
    private static final long SILVER_HIGH_SCORE_MILLIS = 327751800000L; // 21/05/1980 Pac-Man
    private static final int BRONZE_HIGH_SCORE = 7273;
    private static final long BRONZE_HIGH_SCORE_MILLIS = 1531794009758L; // 16/07/2018 When this class was coded
    private static final int FOURTH_HIGH_SCORE = 6500;
    private static final long FOURTH_HIGH_SCORE_MILLIS = 1532746800000L; // 28/07/2018 First release of this game
    private static final int FIVETH_HIGH_SCORE = 5800;
    private static final long FIVETH_HIGH_SCORE_MILLIS = 1533956400000L; // 11/08/2018 When this class was refactored

    // Singleton: unique instance
    private static GameSettings instance;

    private Preferences prefs;
    private Json json;
    private boolean sound;
    private boolean music;
    private float volSound;
    private float volMusic;
    private boolean manualShooting;
    private ArrayMap<Integer, HighScore> highScores; // Ordered map, starts from 0 to MAX_RANKING - 1
    private ArrayMap<Integer, LevelState> levels;   // Ordered map, starts from 0 to MAX_LEVEL - 1
    private int progress;

    // Singleton: prevent instantiation from other classes
    private GameSettings() {
        prefs = Gdx.app.getPreferences(SETTINGS);
        json = new Json();
        highScores = new ArrayMap<Integer, HighScore>();
        levels = new ArrayMap<Integer, LevelState>();
    }

    // Singleton: retrieve instance
    public static GameSettings getInstance() {
        if (instance == null) {
            instance = new GameSettings();
        }
        return instance;
    }

    private String getData(Object object) {
        return json.toJson(object);
    }

    private LevelState getLevelState(String data) {
        return json.fromJson(LevelState.class, data);
    }

    private HighScore getHighScore(String data) {
        return json.fromJson(HighScore.class, data);
    }

    public void load() {
        sound = prefs.getBoolean(SOUND, true);
        music = prefs.getBoolean(MUSIC, true);
        volSound = MathUtils.clamp(prefs.getFloat(VOLUME_SOUND, DEFAULT_VOLUME), MIN_VOLUME, MAX_VOLUME);
        volMusic = MathUtils.clamp(prefs.getFloat(VOLUME_MUSIC, DEFAULT_VOLUME), MIN_VOLUME, MAX_VOLUME);
        manualShooting = prefs.getBoolean(MANUAL_SHOOTING, true);
        loadHighScores();
        loadLevels();
    }

    private void loadHighScores() {
        highScores = getDefaultHighScores();
        String data;
        for (int i = 0; i < MAX_RANKING; i++) {
            data = prefs.getString(HIGH_SCORE + i, "");
            if (!data.isEmpty()) {
                highScores.put(i, getHighScore(data)); // If the key exists, it overwrites its value.
            }
        }
    }

    private ArrayMap<Integer, HighScore> getDefaultHighScores() {
        int scores[] = {GOLD_HIGH_SCORE, SILVER_HIGH_SCORE, BRONZE_HIGH_SCORE, FOURTH_HIGH_SCORE, FIVETH_HIGH_SCORE};
        long millis[] = {GOLD_HIGH_SCORE_MILLIS, SILVER_HIGH_SCORE_MILLIS,
                BRONZE_HIGH_SCORE_MILLIS, FOURTH_HIGH_SCORE_MILLIS, FIVETH_HIGH_SCORE_MILLIS};
        ArrayMap<Integer, HighScore> highScores = new ArrayMap<Integer, HighScore>();
        for(int i = 0; i < MAX_RANKING; i++) {
            highScores.put(i, new HighScore(i + 1, scores[i], millis[i]));
        }
        return highScores;
    }

    private void loadLevels() {
        String data;
        LevelState levelState;
        progress = prefs.getInteger(PROGRESS, DebugConstants.DEBUG_LEVELS ? MAX_LEVEL : 1);
        for (int i = 0; i < progress; i++) {
            data = prefs.getString(LEVEL_STATE + i, "");
            if (data.isEmpty()) {
                levelState = new LevelState(i + 1, 0, 0);
            } else {
                levelState = getLevelState(data);
            }
            levels.put(i, levelState); // If the key exists, it overwrites its value.
        }
    }

    public void save() {
        prefs.putBoolean(SOUND, sound);
        prefs.putBoolean(MUSIC, music);
        prefs.putFloat(VOLUME_SOUND, volSound);
        prefs.putFloat(VOLUME_MUSIC, volMusic);
        prefs.putBoolean(MANUAL_SHOOTING, manualShooting);
        for (int i = 0; i < MAX_RANKING; i++) {
            prefs.putString(HIGH_SCORE + i, getData(highScores.get(i)));
        }
        for (int i = 0; i < MAX_LEVEL; i++) {
            prefs.putString(LEVEL_STATE + i, getData(levels.get(i)));
        }
        prefs.putInteger(PROGRESS, progress);
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

    // ---------------------------------------------- TODO
    public int getGoldHighScore() {
        return goldHighScore;
    }

    public void setGoldHighScore(int goldHighScore) {
        this.goldHighScore = goldHighScore;
        this.goldHighScoreMillis = TimeUtils.millis();
        this.goldHighScoreDate.setTime(this.goldHighScoreMillis);
    }

    public Date getGoldHighScoreDate() {
        return goldHighScoreDate;
    }

    public int getSilverHighScore() {
        return silverHighScore;
    }

    public void setSilverHighScore(int silverHighScore) {
        this.silverHighScore = silverHighScore;
        this.silverHighScoreMillis = TimeUtils.millis();
        this.silverHighScoreDate.setTime(this.silverHighScoreMillis);
    }

    public Date getSilverHighScoreDate() {
        return silverHighScoreDate;
    }

    public int getBronzeHighScore() {
        return bronzeHighScore;
    }

    public void setBronzeHighScore(int bronzeHighScore) {
        this.bronzeHighScore = bronzeHighScore;
        this.bronzeHighScoreMillis = TimeUtils.millis();
        this.bronzeHighScoreDate.setTime(this.bronzeHighScoreMillis);
    }

    public Date getBronzeHighScoreDate() {
        return bronzeHighScoreDate;
    }

    public void setStars(int level, int stars) {
        levels.get(level).setFinalStars(stars);
    }

    public ArrayMap<Integer, LevelState> getLevels() {
        return levels;
    }

    public void addNewLevel(int level) {
        levels.put(level, new LevelState(level, 0, 0));
        if (level > progress) {
            progress = level;
        }
    }

    public boolean isGameComplete() {
        LevelState levelState = levels.get(MAX_LEVEL);
        return levelState != null ? levelState.getFinalStars() > 0 ||
                DebugConstants.DEBUG_LEVELS
                : false;
    }

    public boolean isNewGoldHighScore(int score) {
        return score > goldHighScore;
    }

    public boolean isNewSilverHighScore(int score) {
        return score > silverHighScore;
    }

    public boolean isNewBronzeHighScore(int score) {
        return score > bronzeHighScore;
    }
}
