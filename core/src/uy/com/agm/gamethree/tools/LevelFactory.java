package uy.com.agm.gamethree.tools;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.ObjectMap;

import uy.com.agm.gamethree.actors.bosses.Boss;
import uy.com.agm.gamethree.actors.bosses.BossFour;
import uy.com.agm.gamethree.actors.bosses.BossOne;
import uy.com.agm.gamethree.actors.bosses.BossThree;
import uy.com.agm.gamethree.actors.bosses.BossTwo;
import uy.com.agm.gamethree.actors.enemies.EnemyThree;
import uy.com.agm.gamethree.actors.items.collectibles.ColSilverBullet;
import uy.com.agm.gamethree.actors.items.powerups.PowerFive;
import uy.com.agm.gamethree.actors.items.powerups.PowerFour;
import uy.com.agm.gamethree.actors.items.powerups.PowerOne;
import uy.com.agm.gamethree.actors.items.powerups.PowerThree;
import uy.com.agm.gamethree.actors.items.powerups.PowerTwo;
import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.sprites.AssetBossFour;
import uy.com.agm.gamethree.assets.sprites.AssetBossOne;
import uy.com.agm.gamethree.assets.sprites.AssetBossThree;
import uy.com.agm.gamethree.assets.sprites.AssetBossTwo;
import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 1/20/2018.
 */

public class LevelFactory {
    private static final String TAG = LevelFactory.class.getName();

    // Constants
    private static final int TIMER_LEVEL_ONE = 350;  // It takes 220s to face the boss
    private static final int TIMER_LEVEL_TWO = 400;
    private static final int TIMER_LEVEL_THREE = 450;
    private static final int TIMER_LEVEL_FOUR = 450;
    private static final String NAME_LEVEL_ONE = "levelFactory.nameLevelOne";
    private static final String NAME_LEVEL_TWO = "levelFactory.nameLevelTwo";
    private static final String NAME_LEVEL_THREE = "levelFactory.nameLevelThree";
    private static final String NAME_LEVEL_FOUR = "levelFactory.nameLevelFour";

    public static Boss getBoss(PlayScreen screen, int level, float handicap) {
        Boss boss;

        switch (level) {
            case 1:
                boss = new BossOne(screen, handicap,
                        screen.getGameCam().position.x - AssetBossOne.WIDTH_METERS / 2,
                        screen.getGameViewPort().getWorldHeight() * PlayScreen.WORLD_SCREENS -
                                AssetBossOne.HEIGHT_METERS / 2 - BossOne.CIRCLE_SHAPE_RADIUS_METERS);
                break;
            case 2:
                boss = new BossTwo(screen, handicap,
                        screen.getGameCam().position.x - AssetBossTwo.WIDTH_METERS / 2,
                        screen.getGameViewPort().getWorldHeight() * PlayScreen.WORLD_SCREENS -
                                AssetBossTwo.HEIGHT_METERS / 2 - BossTwo.CIRCLE_SHAPE_RADIUS_METERS);
                break;
            case 3:
                boss = new BossThree(screen, handicap,
                        screen.getGameCam().position.x - AssetBossThree.WIDTH_METERS / 2,
                        screen.getGameViewPort().getWorldHeight() * PlayScreen.WORLD_SCREENS -
                                screen.getGameViewPort().getWorldHeight() / 2 -
                                AssetBossThree.HEIGHT_METERS / 2);
                break;
            case 4:
                boss = new BossFour(screen, handicap,
                        screen.getGameCam().position.x - AssetBossFour.WIDTH_METERS / 2,
                        screen.getGameViewPort().getWorldHeight() * PlayScreen.WORLD_SCREENS -
                                AssetBossFour.HEIGHT_METERS / 2 - BossFour.CIRCLE_SHAPE_RADIUS_METERS);
                break;
            default:
                boss = null;
                break;
        }
        return boss;
    }

    public static int getLevelTimer(int level) {
        int[] levelTimers = {TIMER_LEVEL_ONE, TIMER_LEVEL_TWO, TIMER_LEVEL_THREE, TIMER_LEVEL_FOUR};
        return  levelTimers[level - 1];
    }

    public static String getLevelName(int level) {
        String[] levelName = {NAME_LEVEL_ONE, NAME_LEVEL_TWO, NAME_LEVEL_THREE, NAME_LEVEL_FOUR};
        return  Assets.getInstance().getI18NGameThree().getI18NGameThreeBundle().format(levelName[level - 1]);
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
            case 3:
                music = Assets.getInstance().getMusic().getSongLevelThree();
                break;
            case 4:
                music = Assets.getInstance().getMusic().getSongLevelFour();
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
            case 3:
                map = Assets.getInstance().getMaps().getMapLevelThree();
                break;
            case 4:
                map = Assets.getInstance().getMaps().getMapLevelFour();
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
                dynamic.put(BossOne.class.getName(), new DynamicHelpDef(true));
                break;
            case 2:
                dynamic.put(PowerTwo.class.getName(), new DynamicHelpDef());
                dynamic.put(PowerFour.class.getName(), new DynamicHelpDef());
                dynamic.put(ColSilverBullet.class.getName(), new DynamicHelpDef());
                dynamic.put(BossTwo.class.getName(), new DynamicHelpDef(true));
                break;
            case 3:
                dynamic.put(PowerFive.class.getName(), new DynamicHelpDef());
                dynamic.put(ColSilverBullet.class.getName(), new DynamicHelpDef());
                dynamic.put(BossThree.class.getName(), new DynamicHelpDef(true));
                break;
            case 4:
                dynamic.put(EnemyThree.class.getName(), new DynamicHelpDef());
                dynamic.put(ColSilverBullet.class.getName(), new DynamicHelpDef());
                dynamic.put(BossFour.class.getName(), new DynamicHelpDef(true));
                break;
            default:
                break;
        }
        return dynamic;
    }

    public static TextureRegion getHelpColSilverBullet(int level) {
        TextureRegion helpColSilverBullet;
        switch (level) {
            case 1:
                helpColSilverBullet = Assets.getInstance().getScene2d().getHelpColSilverBulletLevelOne();
                break;
            case 2:
                helpColSilverBullet = Assets.getInstance().getScene2d().getHelpColSilverBulletLevelTwo();
                break;
            case 3:
                helpColSilverBullet = Assets.getInstance().getScene2d().getHelpColSilverBulletLevelThree();
                break;
            case 4:
                helpColSilverBullet = Assets.getInstance().getScene2d().getHelpColSilverBulletLevelFour();
                break;
            default:
                helpColSilverBullet = null;
                break;
        }
        return helpColSilverBullet;
    }
}
