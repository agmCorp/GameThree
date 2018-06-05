package uy.com.agm.gamethree.game;

/**
 * Created by AGM on 5/27/2018.
 */

public class LevelState {
    private static final String TAG = LevelState.class.getName();

    private int level;
    private int initialLives;
    private int initialScore;
    private boolean active;

    public LevelState() {
        this.level = 0;
        this.initialLives = 0;
        this.initialScore = 0;
        this.active = false;
    }

    public LevelState(int level, int initialLives, int initialScore, boolean active) {
        this.level = level;
        this.initialLives = initialLives;
        this.initialScore = initialScore;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
