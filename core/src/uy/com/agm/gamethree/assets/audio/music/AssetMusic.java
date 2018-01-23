package uy.com.agm.gamethree.assets.audio.music;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

import uy.com.agm.gamethree.game.Constants;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetMusic {
    private static final String TAG = AssetMusic.class.getName();

    private Music songMainMenu;
    private Music songLevelOne;

    public AssetMusic(AssetManager am) {
        songMainMenu = am.get(Constants.MUSIC_FILE_MAIN_MENU, Music.class);
        songLevelOne = am.get(Constants.MUSIC_FILE_LEVEL_ONE, Music.class);
    }

    public Music getSongMainMenu() {
        return songMainMenu;
    }

    public Music getSongLevelOne() {
        return songLevelOne;
    }
}