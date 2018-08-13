package uy.com.agm.gamethree.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.TimeUtils;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.scene2d.AssetScene2d;

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
    private static final int FIRST = 21365;
    private static final long FIRST_MILLIS = 222168600000L; // 15/01/1977 Mi cumple \(^-^)/
    private static final int SECOND = 18491;
    private static final long SECOND_MILLIS = 327751800000L; // 21/05/1980 Pac-Man
    private static final int THIRD = 7273;
    private static final long THIRD_MILLIS = 1531794009758L; // 16/07/2018 When this class was coded
    private static final int FOURTH = 6500;
    private static final long FOURTH_MILLIS = 1532746800000L; // 28/07/2018 First release of this game
    private static final int FIFTH = 5800;
    private static final long FIFTH_MILLIS = 1533956400000L; // 11/08/2018 When this class was refactored

    // Singleton: unique instance
    private static GameSettings instance;

    private Preferences prefs;
    private Json json;
    private boolean sound;
    private boolean music;
    private float volSound;
    private float volMusic;
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
        manualShooting = prefs.getBoolean(MANUAL_SHOOTING, true);
        loadHighScores();
        loadLevels();
    }

    private void loadHighScores() {
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
        int scores[] = {FIRST, SECOND, THIRD, FOURTH, FIFTH};
        long millis[] = {FIRST_MILLIS, SECOND_MILLIS, THIRD_MILLIS, FOURTH_MILLIS, FIFTH_MILLIS};
        Array<HighScore> highScores = new Array<HighScore>();
        for (int i = 0; i < MAX_RANKING; i++) {
            highScores.add(new HighScore(i + 1, scores[i], millis[i]));
        }
        return highScores;
    }

    private Array<TextureRegion> getHighScoreImages() {
        // UI assets
        AssetScene2d assetScene2d = Assets.getInstance().getScene2d();

        Array<TextureRegion> highScoreImages = new Array<TextureRegion>();
        highScoreImages.add(assetScene2d.getGoldTrophy());
        highScoreImages.add(assetScene2d.getSilverTrophy());
        highScoreImages.add(assetScene2d.getBronzeTrophy());
        highScoreImages.add(assetScene2d.getBadge());

        return highScoreImages;
    }

    private void loadLevels() {
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
