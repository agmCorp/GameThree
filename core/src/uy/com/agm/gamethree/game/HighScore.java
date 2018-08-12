package uy.com.agm.gamethree.game;

import java.util.Date;

/**
 * Created by AGM on 5/27/2018.
 */

public class HighScore {
    private static final String TAG = HighScore.class.getName();

    private int ranking;
    private int score;
    private long millis;
    transient private Date date; // Avoid serializing (see GameSettings.getData(...)).

    public HighScore() {
        this.ranking = 0;
        this.score = 0;
        this.millis = 0;
        this.date = null;
    }

    public HighScore(int ranking, int score, long millis) {
        this.ranking = ranking;
        this.score = score;
        this.millis = millis;
        this.date = new Date(millis);
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

    public Date getDate() {
        if (date == null) {
            date = new Date(millis);
        }
        return date;
    }
}
