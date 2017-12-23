package uy.com.agm.gamethree.assets.audio.sound;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetSounds {
    private static final String TAG = AssetSounds.class.getName();

    public final Sound dead;
    public final Sound hit;
    public final Sound pickUpPowerOne;
    public final Sound openPowerBox;

    public AssetSounds(AssetManager am) {
        dead = am.get("audio/sounds/dead.ogg", Sound.class);
        hit = am.get("audio/sounds/hit.ogg", Sound.class);
        pickUpPowerOne = am.get("audio/sounds/pickUpPowerOne.ogg", Sound.class);
        openPowerBox = am.get("audio/sounds/openPowerBox.ogg", Sound.class);
    }
}