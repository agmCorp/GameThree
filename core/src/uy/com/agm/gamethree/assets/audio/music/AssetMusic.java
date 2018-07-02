package uy.com.agm.gamethree.assets.audio.music;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

import uy.com.agm.gamethree.assets.Assets;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetMusic {
    private static final String TAG = AssetMusic.class.getName();

    private Music songMainMenu;
    private Music songFinalEnemyFight;
    private Music songLevelOne;
    private Music songLevelTwo;
    private Music songLevelThree;
    private Music songLevelFour;

    public AssetMusic(AssetManager am) {
        songMainMenu = am.get(Assets.MUSIC_FILE_MAIN_MENU, Music.class);
        songFinalEnemyFight = am.get(Assets.MUSIC_FILE_FINAL_ENEMY_FIGHT, Music.class);
        songLevelOne = am.get(Assets.MUSIC_FILE_LEVEL_ONE, Music.class);
        songLevelTwo = am.get(Assets.MUSIC_FILE_LEVEL_TWO, Music.class);
        songLevelThree = am.get(Assets.MUSIC_FILE_LEVEL_THREE, Music.class);
        songLevelFour = am.get(Assets.MUSIC_FILE_LEVEL_FOUR, Music.class);
    }

    public Music getSongMainMenu() {
        return songMainMenu;
    }

    public Music getSongFinalEnemyFight() {
        return songFinalEnemyFight;
    }

    public Music getSongLevelOne() {
        return songLevelOne;
    }

    public Music getSongLevelTwo() {
        return songLevelTwo;
    }

    public Music getSongLevelThree() {
        return songLevelThree;
    }

    public Music getSongLevelFour() {
        return songLevelFour;
    }
}