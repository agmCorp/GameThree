package uy.com.agm.gamethree.tools;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.ObjectMap;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.game.Constants;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.enemies.EnemyThree;
import uy.com.agm.gamethree.sprites.finals.FinalEnemy;
import uy.com.agm.gamethree.sprites.finals.FinalEnemyLevelOne;
import uy.com.agm.gamethree.sprites.items.collectibles.ColSilverBullet;
import uy.com.agm.gamethree.sprites.items.powerups.PowerOne;
import uy.com.agm.gamethree.sprites.items.powerups.PowerThree;

/**
 * Created by AGM on 1/20/2018.
 */

public class LevelFactory {
    private static final String TAG = LevelFactory.class.getName();

    public static FinalEnemy getFinalEnemy(PlayScreen screen, int level) {
        FinalEnemy finalEnemy;

        switch (level) {
            case 1:
            case 2:
                finalEnemy = new FinalEnemyLevelOne(screen, screen.getGameCam().position.x,
                        screen.getGameViewPort().getWorldHeight() * Constants.WORLD_SCREENS -
                                Constants.FINALLEVELONE_HEIGHT_METERS + Constants.FINALLEVELONE_HEIGHT_METERS / 2 - Constants.FINALLEVELONE_CIRCLESHAPE_RADIUS_METERS);
                break;
            default:
                finalEnemy = null;
                break;
        }
        return finalEnemy;
    }

    public static int getLevelTimer(int level) {
        int[] levelTimers = {Constants.TIMER_LEVEL_ONE, Constants.TIMER_LEVEL_TWO};
        return  levelTimers[level - 1];
    }

    public static Music getLevelMusic(int level) {
        Music music;

        switch (level) {
            case 1:
                music = Assets.getInstance().getMusic().getSongLevelOne();
                break;
            case 2:
                music = Assets.getInstance().getMusic().getSongLevelTwo();
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
                map = Assets.getInstance().getMaps().getMapLevelOne();
                break;
            case 2:
                map = Assets.getInstance().getMaps().getMapLevelTwo();
                break;
            default:
                map = null;
                break;
        }
        return map;
    }

    public static ObjectMap<String, DynamicHelpDef> getDynamicHelp(int level) {
        ObjectMap<String, DynamicHelpDef> dynamic = new ObjectMap<String, DynamicHelpDef>();

        switch (level) {
            case 1:
                dynamic.put(PowerOne.class.getName(), new DynamicHelpDef());
                dynamic.put(PowerThree.class.getName(), new DynamicHelpDef());
                dynamic.put(ColSilverBullet.class.getName(), new DynamicHelpDef());
                dynamic.put(EnemyThree.class.getName(), new DynamicHelpDef(true));
                dynamic.put(FinalEnemyLevelOne.class.getName(), new DynamicHelpDef(true));
                break;
            case 2:
//                dynamic.put(EnemyOne.class.getName(), new DynamicHelpDef());
//                dynamic.put(EnemyTwo.class.getName(), new DynamicHelpDef(true));
//                dynamic.put(EnemyThree.class.getName(), new DynamicHelpDef());
//                dynamic.put(EnemyFour.class.getName(), new DynamicHelpDef());
                break;
            default:
                break;
        }
        return dynamic;
    }
}
