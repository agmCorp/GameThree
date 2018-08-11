package uy.com.agm.gamethree.game;

/**
 * Created by AGM on 5/27/2018.
 */

public class HighScore {
    private static final String TAG = HighScore.class.getName();

    private int ranking;
    private int score;
    private long millis;

    public HighScore(int ranking, int score, long millis) {
        this.ranking = ranking;
        this.score = score;
        this.millis = millis;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getMillis() {
        return millis;
    }

    public void setMillis(long millis) {
        this.millis = millis;
    }
}
