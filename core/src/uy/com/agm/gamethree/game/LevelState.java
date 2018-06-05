package uy.com.agm.gamethree.game;

import uy.com.agm.gamethree.tools.LevelFactory;

/**
 * Created by AGM on 5/27/2018.
 */

public class LevelState {
    private static final String TAG = LevelState.class.getName();

    private int level;
    private int initialLives;
    private int initialScore;
    private int initialSkulls;
    private int defaultLevelSkulls;
    private boolean active;

    public LevelState() {
        this.level = 0;
        this.initialLives = 0;
        this.initialScore = 0;
        this.initialSkulls = 0;
        this.defaultLevelSkulls = 0;
        this.active = false;
    }

    public LevelState(int level, int initialLives, int initialScore, int initialSkulls, boolean active) {
        this.level = level;
        this.initialLives = initialLives;
        this.initialScore = initialScore;
        this.initialSkulls = initialSkulls;
        this.defaultLevelSkulls = LevelFactory.getLevelSkulls(level);
        this.active = active;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getInitialLives() {
        return initialLives;
    }

    public void setInitialLives(int initialLives) {
        this.initialLives = initialLives;
    }

    public int getInitialScore() {
        return initialScore;
    }

    public void setInitialScore(int initialScore) {
        this.initialScore = initialScore;
    }

    public int getInitialSkulls() {
        return initialSkulls;
    }

    public void setInitialSkulls(int initialSkulls) {
        this.initialSkulls = initialSkulls;
    }

    public int getDefaultLevelSkulls() {
        return defaultLevelSkulls;
    }

    public void setDefaultLevelSkulls(int defaultLevelSkulls) {
        this.defaultLevelSkulls = defaultLevelSkulls;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
