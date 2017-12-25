package uy.com.agm.gamethree.assets.audio.sound;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetSounds {
    private static final String TAG = AssetSounds.class.getName();

    public final Sound bump;
    public final Sound crack;
    public final Sound dead;
    public final Sound enemyShoot;
    public final Sound heroShoot;
    public final Sound hit;
    public final Sound openPowerBox;
    public final Sound pickUpPowerOne;
    public final Sound powerDown;
    public final Sound powerTimer;
    public final Sound showUpPowerOne;


    public AssetSounds(AssetManager am) {
        bump = am.get("audio/sounds/bump.ogg", Sound.class);
        crack = am.get("audio/sounds/crack.ogg", Sound.class);
        dead = am.get("audio/sounds/dead.ogg", Sound.class);
        enemyShoot = am.get("audio/sounds/enemyShoot.ogg", Sound.class);
        heroShoot = am.get("audio/sounds/heroShoot.ogg", Sound.class);
        hit = am.get("audio/sounds/hit.ogg", Sound.class);
        openPowerBox = am.get("audio/sounds/openPowerBox.ogg", Sound.class);
        pickUpPowerOne = am.get("audio/sounds/pickUpPowerOne.ogg", Sound.class);
        powerDown = am.get("audio/sounds/powerDown.ogg", Sound.class);
        powerTimer = am.get("audio/sounds/powerTimer.ogg", Sound.class);
        showUpPowerOne = am.get("audio/sounds/showUpPowerOne.ogg", Sound.class);
    }
}