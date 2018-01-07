package uy.com.agm.gamethree.assets.audio.music;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

import uy.com.agm.gamethree.game.Constants;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetMusic {
    private static final String TAG = AssetMusic.class.getName();

    public final Music songLevelOne;

    public AssetMusic(AssetManager am) {
        songLevelOne = am.get(Constants.MUSIC_FILE_LEVEL_ONE, Music.class);
    }
}