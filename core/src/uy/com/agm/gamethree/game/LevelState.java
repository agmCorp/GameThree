package uy.com.agm.gamethree.game;

/**
 * Created by AGM on 5/27/2018.
 */

public class LevelState {
    private static final String TAG = LevelState.class.getName();

    private int level;
    private int lives;
    private int score;
    private int skulls;
    private boolean active;

    public LevelState() {
        this.level = 0;
        this.lives = 0;
        this.score = 0;
        this.skulls = 0;
        this.active = false;
    }

    public LevelState(int level, int lives, int score, int skulls, boolean active) {
        this.level = level;
        this.lives = lives;
        this.score = score;
        this.skulls = skulls;
        this.active = active;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getSkulls() {
        return skulls;
    }

    public void setSkulls(int skulls) {
        this.skulls = skulls;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
