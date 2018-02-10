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
    private Sound heroShootEmpty;
    private Sound heroShootSwish;
    private Sound hit;
    private Sound openPowerBox;
    private Sound clock;
    private Sound pickUpColOne;
    private Sound pickUpColSilverBullet;
    private Sound pickUpPowerOne;
    private Sound pickUpPowerTwo;
    private Sound pickUpPowerThree;
    private Sound pickUpPowerFour;
    private Sound powerDown;
    private Sound beepA;
    private Sound beepB;
    private Sound showUpColOne;
    private Sound showUpColSilverBullet;
    private Sound showUpPowerOne;
    private Sound showUpPowerTwo;
    private Sound showUpPowerThree;
    private Sound showUpPowerFour;
    private Sound timeIsUp;
    private Sound finalEnemyLevelOnePowerUp;
    private Sound finalEnemyLevelOnePowerDown;
    private Sound finalEnemyLevelOneExplosion;
    private Sound finalEnemyLevelOneHit;
    private Sound finalEnemyLevelOneIntro;
    private Sound levelCompleted;
    private Sound boing;
    private Sound click;
    private Sound aplause;
    private Sound squish;

    public AssetSounds(AssetManager am) {
        bump = am.get(Constants.FX_FILE_BUMP, Sound.class);
        crack = am.get(Constants.FX_FILE_CRACK, Sound.class);
        dead = am.get(Constants.FX_FILE_DEAD, Sound.class);
        enemyShoot = am.get(Constants.FX_FILE_ENEMY_SHOOT, Sound.class);
        heroShoot = am.get(Constants.FX_FILE_HERO_SHOOT, Sound.class);
        heroShootEmpty = am.get(Constants.FX_FILE_HERO_SHOOT_EMPTY, Sound.class);
        heroShootSwish = am.get(Constants.FX_FILE_HERO_SHOOT_SWISH, Sound.class);
        hit = am.get(Constants.FX_FILE_HIT, Sound.class);
        openPowerBox = am.get(Constants.FX_FILE_OPEN_POWERBOX, Sound.class);
        clock = am.get(Constants.FX_FILE_CLOCK, Sound.class);
        pickUpColOne = am.get(Constants.FX_FILE_PICK_UP_COLONE, Sound.class);
        pickUpColSilverBullet = am.get(Constants.FX_FILE_PICK_UP_COLSILVERBULLET, Sound.class);
        pickUpPowerOne = am.get(Constants.FX_FILE_PICK_UP_POWERONE, Sound.class);
        pickUpPowerTwo = am.get(Constants.FX_FILE_PICK_UP_POWERTWO, Sound.class);
        pickUpPowerThree = am.get(Constants.FX_FILE_PICK_UP_POWERTHREE, Sound.class);
        pickUpPowerFour = am.get(Constants.FX_FILE_PICK_UP_POWERFOUR, Sound.class);
        powerDown = am.get(Constants.FX_FILE_POWER_DOWN, Sound.class);
        beepA = am.get(Constants.FX_FILE_BEEP_A, Sound.class);
        beepB = am.get(Constants.FX_FILE_BEEP_B, Sound.class);
        showUpColOne = am.get(Constants.FX_FILE_SHOW_UP_COLONE, Sound.class);
        showUpColSilverBullet = am.get(Constants.FX_FILE_SHOW_UP_COLSILVERBULLET, Sound.class);
        showUpPowerOne = am.get(Constants.FX_FILE_SHOW_UP_POWERONE, Sound.class);
        showUpPowerTwo = am.get(Constants.FX_FILE_SHOW_UP_POWERTWO, Sound.class);
        showUpPowerThree = am.get(Constants.FX_FILE_SHOW_UP_POWERTHREE, Sound.class);
        showUpPowerFour = am.get(Constants.FX_FILE_SHOW_UP_POWERFOUR, Sound.class);
        timeIsUp = am.get(Constants.FX_FILE_TIME_IS_UP, Sound.class);
        finalEnemyLevelOnePowerUp = am.get(Constants.FX_FILE_FINAL_LEVEL_ONE_POWER_UP, Sound.class);
        finalEnemyLevelOnePowerDown = am.get(Constants.FX_FILE_FINAL_LEVEL_ONE_POWER_DOWN, Sound.class);
        finalEnemyLevelOneExplosion = am.get(Constants.FX_FILE_FINAL_LEVEL_ONE_EXPLOSION, Sound.class);
        finalEnemyLevelOneHit = am.get(Constants.FX_FILE_FINAL_LEVEL_ONE_HIT, Sound.class);
        finalEnemyLevelOneIntro = am.get(Constants.FX_FILE_FINAL_LEVEL_ONE_INTRO, Sound.class);
        levelCompleted = am.get(Constants.FX_FILE_LEVEL_COMPLETED, Sound.class);
        boing = am.get(Constants.FX_FILE_BOUNCE, Sound.class);
        click = am.get(Constants.FX_FILE_CLICK, Sound.class);
        aplause = am.get(Constants.FX_FILE_APLAUSE, Sound.class);
        squish = am.get(Constants.FX_FILE_SQUISH, Sound.class);
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

    public Sound getHeroShootEmpty() {
        return heroShootEmpty;
    }

    public Sound getHeroShootSwish() {
        return heroShootSwish;
    }

    public Sound getHit() {
        return hit;
    }

    public Sound getOpenPowerBox() {
        return openPowerBox;
    }

    public Sound getClock() {
        return clock;
    }

    public Sound getPickUpColOne() {
        return pickUpColOne;
    }

    public Sound getPickUpColSilverBullet() {
        return pickUpColSilverBullet;
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

    public Sound getBeepA() {
        return beepA;
    }

    public Sound getBeepB() {
        return beepB;
    }

    public Sound getShowUpColOne() {
        return showUpColOne;
    }

    public Sound getShowUpColSilverBullet() {
        return showUpColSilverBullet;
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

    public Sound getTimeIsUp() {
        return timeIsUp;
    }

    public Sound getFinalEnemyLevelOnePowerUp() {
        return finalEnemyLevelOnePowerUp;
    }

    public Sound getFinalEnemyLevelOnePowerDown() {
        return finalEnemyLevelOnePowerDown;
    }

    public Sound getFinalEnemyLevelOneExplosion() {
        return finalEnemyLevelOneExplosion;
    }

    public Sound getFinalEnemyLevelOneHit() {
        return finalEnemyLevelOneHit;
    }

    public Sound getFinalEnemyLevelOneIntro() {
        return finalEnemyLevelOneIntro;
    }

    public Sound getLevelCompleted() {
        return levelCompleted;
    }

    public Sound getBoing() {
        return  boing;
    }

    public Sound getClick() {
        return click;
    }

    public Sound getAplause() {
        return aplause;
    }

    public Sound getSquish() {
        return squish;
    }
}