package uy.com.agm.gamethree.assets.audio.sound;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

import uy.com.agm.gamethree.game.Constants;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetSounds {
    private static final String TAG = AssetSounds.class.getName();

    private Sound bump;
    private Sound crack;
    private Sound dead;
    private Sound enemyShoot;
    private Sound heroShoot;
    private Sound hit;
    private Sound openPowerBox;
    private Sound levelTimer;
    private Sound pickUpColOne;
    private Sound pickUpPowerOne;
    private Sound pickUpPowerTwo;
    private Sound pickUpPowerThree;
    private Sound pickUpPowerFour;
    private Sound powerDown;
    private Sound powerTimer;
    private Sound showUpColOne;
    private Sound showUpPowerOne;
    private Sound showUpPowerTwo;
    private Sound showUpPowerThree;
    private Sound showUpPowerFour;
    private Sound finalLevelOnePowerUp;
    private Sound finalLevelOnePowerDown;

    public AssetSounds(AssetManager am) {
        bump = am.get(Constants.FX_FILE_BUMP, Sound.class);
        crack = am.get(Constants.FX_FILE_CRACK, Sound.class);
        dead = am.get(Constants.FX_FILE_DEAD, Sound.class);
        enemyShoot = am.get(Constants.FX_FILE_ENEMY_SHOOT, Sound.class);
        heroShoot = am.get(Constants.FX_FILE_HERO_SHOOT, Sound.class);
        hit = am.get(Constants.FX_FILE_HIT, Sound.class);
        openPowerBox = am.get(Constants.FX_FILE_OPEN_POWERBOX, Sound.class);
        levelTimer = am.get(Constants.FX_FILE_LEVEL_TIMER, Sound.class);
        pickUpColOne = am.get(Constants.FX_FILE_PICK_UP_COLONE, Sound.class);
        pickUpPowerOne = am.get(Constants.FX_FILE_PICK_UP_POWERONE, Sound.class);
        pickUpPowerTwo = am.get(Constants.FX_FILE_PICK_UP_POWERTWO, Sound.class);
        pickUpPowerThree = am.get(Constants.FX_FILE_PICK_UP_POWERTHREE, Sound.class);
        pickUpPowerFour = am.get(Constants.FX_FILE_PICK_UP_POWERFOUR, Sound.class);
        powerDown = am.get(Constants.FX_FILE_POWER_DOWN, Sound.class);
        powerTimer = am.get(Constants.FX_FILE_POWER_TIMER, Sound.class);
        showUpColOne = am.get(Constants.FX_FILE_SHOW_UP_COLONE, Sound.class);
        showUpPowerOne = am.get(Constants.FX_FILE_SHOW_UP_POWERONE, Sound.class);
        showUpPowerTwo = am.get(Constants.FX_FILE_SHOW_UP_POWERTWO, Sound.class);
        showUpPowerThree = am.get(Constants.FX_FILE_SHOW_UP_POWERTHREE, Sound.class);
        showUpPowerFour = am.get(Constants.FX_FILE_SHOW_UP_POWERFOUR, Sound.class);
        finalLevelOnePowerUp = am.get(Constants.FX_FILE_FINAL_LEVEL_ONE_POWER_UP, Sound.class);
        finalLevelOnePowerDown = am.get(Constants.FX_FILE_FINAL_LEVEL_ONE_POWER_DOWN, Sound.class);
    }

    public Sound getBump() {
        return bump;
    }

    public Sound getCrack() {
        return crack;
    }

    public Sound getDead() {
        return dead;
    }

    public Sound getEnemyShoot() {
        return enemyShoot;
    }

    public Sound getHeroShoot() {
        return heroShoot;
    }

    public Sound getHit() {
        return hit;
    }

    public Sound getOpenPowerBox() {
        return openPowerBox;
    }

    public Sound getLevelTimer() {
        return levelTimer;
    }

    public Sound getPickUpColOne() {
        return pickUpColOne;
    }

    public Sound getPickUpPowerOne() {
        return pickUpPowerOne;
    }

    public Sound getPickUpPowerTwo() {
        return pickUpPowerTwo;
    }

    public Sound getPickUpPowerThree() {
        return pickUpPowerThree;
    }

    public Sound getPickUpPowerFour() {
        return pickUpPowerFour;
    }

    public Sound getPowerDown() {
        return powerDown;
    }

    public Sound getPowerTimer() {
        return powerTimer;
    }

    public Sound getShowUpColOne() {
        return showUpColOne;
    }

    public Sound getShowUpPowerOne() {
        return showUpPowerOne;
    }

    public Sound getShowUpPowerTwo() {
        return showUpPowerTwo;
    }

    public Sound getShowUpPowerThree() {
        return showUpPowerThree;
    }

    public Sound getShowUpPowerFour() {
        return showUpPowerFour;
    }

    public Sound getFinalLevelOnePowerUp() {
        return finalLevelOnePowerUp;
    }

    public Sound getFinalLevelOnePowerDown() {
        return finalLevelOnePowerDown;
    }
}