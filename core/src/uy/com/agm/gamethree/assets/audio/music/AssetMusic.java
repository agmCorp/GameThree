package uy.com.agm.gamethree.assets.audio.music;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetMusic {
    public final Music songLevelOne;

    public AssetMusic(AssetManager am) {
        songLevelOne = am.get("audio/music/levelOne.ogg", Music.class);
    }
}