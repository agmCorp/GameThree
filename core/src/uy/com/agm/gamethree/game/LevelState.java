package uy.com.agm.gamethree.game;

/**
 * Created by AGM on 5/27/2018.
 */

public class LevelState {
    private static final String TAG = LevelState.class.getName();

    private int level;
    private int finalScore;
    private int finalStars;

    public LevelState(int level, int finalScore, int finalStars) {
        this.level = level;
        this.finalScore = finalScore;
        this.finalStars = finalStars;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }

    public int getFinalStars() {
        return finalStars;
    }

    public void setFinalStars(int finalStars) {
        this.finalStars = finalStars;
    }
}
