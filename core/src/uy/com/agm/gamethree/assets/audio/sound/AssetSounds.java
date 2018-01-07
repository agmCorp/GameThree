package uy.com.agm.gamethree.assets.audio.sound;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

import uy.com.agm.gamethree.game.Constants;

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
    public final Sound pickUpPowerTwo;
    public final Sound powerDown;
    public final Sound powerTimer;
    public final Sound showUpPowerOne;
    public final Sound showUpPowerTwo;
    public final Sound finalLevelOnePowerUp;
    public final Sound finalLevelOnePowerDown;

    public AssetSounds(AssetManager am) {
        bump = am.get(Constants.FX_FILE_BUMP, Sound.class);
        crack = am.get(Constants.FX_FILE_CRACK, Sound.class);
        dead = am.get(Constants.FX_FILE_DEAD, Sound.class);
        enemyShoot = am.get(Constants.FX_FILE_ENEMY_SHOOT, Sound.class);
        heroShoot = am.get(Constants.FX_FILE_HERO_SHOOT, Sound.class);
        hit = am.get(Constants.FX_FILE_HIT, Sound.class);
        openPowerBox = am.get(Constants.FX_FILE_OPEN_POWERBOX, Sound.class);
        pickUpPowerOne = am.get(Constants.FX_FILE_PICK_UP_POWERONE, Sound.class);
        pickUpPowerTwo = am.get(Constants.FX_FILE_PICK_UP_POWERTWO, Sound.class);
        powerDown = am.get(Constants.FX_FILE_POWER_DOWN, Sound.class);
        powerTimer = am.get(Constants.FX_FILE_POWER_TIMER, Sound.class);
        showUpPowerOne = am.get(Constants.FX_FILE_SHOW_UP_POWERONE, Sound.class);
        showUpPowerTwo = am.get(Constants.FX_FILE_SHOW_UP_POWERTWO, Sound.class);
        finalLevelOnePowerUp = am.get(Constants.FX_FILE_FINAL_LEVEL_ONE_POWER_UP, Sound.class);
        finalLevelOnePowerDown = am.get(Constants.FX_FILE_FINAL_LEVEL_ONE_POWER_DOWN, Sound.class);
    }
}