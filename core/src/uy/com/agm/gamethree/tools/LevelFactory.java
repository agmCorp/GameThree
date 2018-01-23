package uy.com.agm.gamethree.tools;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.tiled.TiledMap;

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
                finalEnemy = new FinalEnemyLevelOne(screen, screen.getGameCam().position.x,
                        screen.getGameViewPort().getWorldHeight() * Constants.WORLD_SCREENS -
                                Constants.FINALLEVELONE_HEIGHT_METERS + Constants.FINALLEVELONE_OFFSET_METERS);
                break;
            default:
                finalEnemy = null;
                break;
        }
        return finalEnemy;
    }

    public static int getLevelTimer(int level) {
        int[] levelTimers = {Constants.TIMER_LEVEL_ONE};
        return  levelTimers[level - 1];
    }

    public static Music getLevelMusic(int level) {
        Music music;

        switch (level) {
            case 1:
                music = Assets.instance.music.getSongLevelOne();
                break;
            default:
                music = null;
                break;
        }
        return music;
    }

    public static TiledMap getLevelMap(int level) {
        TiledMap map;

        switch (level) {
            case 1:
                map = Assets.instance.maps.getMapLevelOne();
                break;
            default:
                map = null;
                break;
        }
        return map;
    }
}
