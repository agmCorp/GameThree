package uy.com.agm.gamethree.tools;

import com.badlogic.gdx.audio.Music;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.game.Constants;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.finals.FinalEnemy;
import uy.com.agm.gamethree.sprites.finals.FinalEnemyLevelOne;

/**
 * Created by AGM on 1/20/2018.
 */

public class LevelFactory {
    private static final String TAG = LevelFactory.class.getName();

    public static FinalEnemy getFinalEnemy(PlayScreen screen, int level) {
        FinalEnemy finalEnemy;

        switch (level) {
            case 1:
                finalEnemy = new FinalEnemyLevelOne(screen, screen.gameCam.position.x,
                        screen.gameViewPort.getWorldHeight() * Constants.WORLD_SCREENS -
                                Constants.FINALLEVELONE_HEIGHT_METERS + Constants.FINALLEVELONE_OFFSET_METERS);
                break;
            default:
                finalEnemy = null;
                break;
        }
        return  finalEnemy;
    }

    public static int getLevelTimer(int level) {
        int[] levelTimers = {Constants.TIMER_LEVEL_ONE};
        return  levelTimers[level - 1];
    }

    public static Music getLevelMusic(int level) {
        Music music;

        switch (level) {
            case 1:
                music = Assets.instance.music.songLevelOne;
                break;
            default:
                music = null;
                break;
        }
        return  music;
    }
}
