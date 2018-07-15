package uy.com.agm.gamethree.game;

/**
 * Created by AGM on 5/27/2018.
 */

public class LevelState {
    private static final String TAG = LevelState.class.getName();

    private int level;
    private int initialLives;
    private int initialScore;
    private int finalStars;
    private boolean active;

    public LevelState() {
        this.level = 0;
        this.initialLives = 0;
        this.initialScore = 0;
        this.finalStars = 0;
        this.active = false;
    }

    public LevelState(int level, int initialLives, int initialScore, int finalStars, boolean active) {
        this.level = level;
        this.initialLives = initialLives;
        this.initialScore = initialScore;
        this.finalStars = finalStars;
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

    public int getFinalStars() {
        return finalStars;
    }

    public void setFinalStars(int finalStars) {
        this.finalStars = finalStars;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
