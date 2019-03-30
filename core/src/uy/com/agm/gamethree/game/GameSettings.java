package uy.com.agm.gamethree.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by AGM on 16/07/2018.
 */

public class GameSettings {
    private static final String TAG = GameSettings.class.getName();

    // Constants
    private static final String SETTINGS = "wipeThemOutSettings";
    private static final String SOUND = "sound";
    private static final String MUSIC = "music";
    private static final String VOLUME_SOUND = "volSound";
    private static final String VOLUME_MUSIC = "volMusic";
    private static final String DIFFICULTY = "difficulty";
    private static final String MANUAL_SHOOTING = "manualShooting";
    private static final String HIGH_SCORE = "highScore_";
    private static final String LEVEL_STATE = "levelState_";
    private static final String PROGRESS = "progress";
    private static final float MIN_VOLUME = 0.0f;
    private static final float MAX_VOLUME = 1.0f;
    private static final long TODAY = TimeUtils.millis();
    public static final int MAX_LEVEL = 4;
    public static final int MAX_RANKING = 5;
    public static final float DEFAULT_VOLUME = 0.5f;

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }

    // Singleton: unique instance
    private static GameSettings instance;

    private Preferences prefs;
    private Json json;
    private boolean sound;
    private boolean music;
    private float volSound;
    private float volMusic;
    private Difficulty difficulty;
    private boolean manualShooting;
    private Array<HighScore> highScores;
    private Array<LevelState> levels;
    private int progress;

    // Singleton: prevent instantiation from other classes
    private GameSettings() {
        prefs = Gdx.app.getPreferences(SETTINGS);
        json = new Json();
        highScores = new Array<HighScore>();
        levels = new Array<LevelState>();
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
        difficulty = Difficulty.valueOf(prefs.getString(DIFFICULTY, Difficulty.MEDIUM.toString()));;
        manualShooting = prefs.getBoolean(MANUAL_SHOOTING, false);
        loadHighScores();
        loadLevels();
    }

    private void loadHighScores() {
        // Gdx.app.exit() schedules an exit from the application (see MainMenuScreen).
        // On android, this will cause a call to pause() and dispose() some time in the future,
        // it will not immediately finish the application. Therefore, sometimes this singleton remains
        // on memory when the app runs a second time.
        // To avoid duplication of all its elements, we must empty the array.
        highScores.clear();

        highScores = getDefaultHighScores();
        String data;
        for (int i = 0; i < MAX_RANKING; i++) {
            data = prefs.getString(HIGH_SCORE + i, "");
            if (!data.isEmpty()) {
                highScores.set(i, getHighScore(data));
            }
        }
    }

    private Array<HighScore> getDefaultHighScores() {
        int scores[] = {0, 0, 0, 0, 0};
        long millis[] = {TODAY, TODAY, TODAY, TODAY, TODAY};
        Array<HighScore> highScores = new Array<HighScore>();
        for (int i = 0; i < MAX_RANKING; i++) {
            highScores.add(new HighScore(i + 1, scores[i], millis[i]));
        }
        return highScores;
    }

    private void loadLevels() {
        // Gdx.app.exit() schedules an exit from the application (see MainMenuScreen).
        // On android, this will cause a call to pause() and dispose() some time in the future,
	    // it will not immediately finish the application. Therefore, sometimes this singleton remains
        // on memory when the app runs a second time.
        // To avoid duplication of all its elements, we must empty the array.
        levels.clear();

        String data;
        LevelState levelState;
        progress = prefs.getInteger(PROGRESS, 1);
        int n = DebugConstants.DEBUG_LEVELS ? MAX_LEVEL : progress;
        for (int i = 0; i < n; i++) {
            data = prefs.getString(LEVEL_STATE + i, "");
            if (data.isEmpty()) {
                levelState = new LevelState(i + 1, 0, 0);
            } else {
                levelState = getLevelState(data);
            }
            levels.add(levelState);
        }
    }

    public void save() {
        prefs.putBoolean(SOUND, sound);
        prefs.putBoolean(MUSIC, music);
        prefs.putFloat(VOLUME_SOUND, volSound);
        prefs.putFloat(VOLUME_MUSIC, volMusic);
        prefs.putString(DIFFICULTY, difficulty.toString());
        prefs.putBoolean(MANUAL_SHOOTING, manualShooting);
        for (int i = 0, n = highScores.size; i < n; i++) {
            prefs.putString(HIGH_SCORE + i, getData(highScores.get(i)));
        }
        for (int i = 0, n = levels.size; i < n; i++) {
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

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isManualShooting() {
        return manualShooting;
    }

    public void setManualShooting(boolean manualShooting) {
        this.manualShooting = manualShooting;
    }

    public int updateRanking(int score) {
        int ranking = -1;
        HighScore highScore;
        for (int i = 0; i < MAX_RANKING; i++) {
            if (highScores.get(i).getScore() < score) {
                for (int j = MAX_RANKING - 1; j > i; j--) {
                    highScore = highScores.get(j - 1);
                    highScore.setRanking(j + 1);
                    highScores.set(j, highScore);
                }
                highScores.set(i, new HighScore(i + 1, score, TimeUtils.millis()));
                ranking = i + 1;
                break;
            }
        }
        return ranking;
    }

    public void setLevelStateInfo(int level, int finalScore, int finalStars) {
        LevelState levelState;
        if (level <= levels.size) {
            levelState = levels.get(level - 1);
            levelState.setFinalScore(finalScore);
            levelState.setFinalStars(finalStars);
        }
    }

    public Array<HighScore> getHighScores() {
        return highScores;
    }

    public int getHighScore() {
        return highScores.get(0).getScore();
    }

    public Array<LevelState> getLevels() {
        return levels;
    }

    public void addNextLevel(int level) {
        if (!DebugConstants.DEBUG_LEVELS) {
            if (level > progress) {
                progress = level;
                levels.add(new LevelState(level, 0, 0));
            }
        }
    }

    public boolean isGameComplete() {
        LevelState levelState = levels.size == MAX_LEVEL ? levels.get(MAX_LEVEL - 1) : null;
        return levelState != null ? levelState.getFinalStars() > 0 ||
                DebugConstants.DEBUG_LEVELS
                : false;
    }
}
