package uy.com.agm.gamethree.assets.audio.sound;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ArrayMap;

import uy.com.agm.gamethree.assets.Assets;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetSounds {
    private static final String TAG = AssetSounds.class.getName();

    // Keeps a sound filename and the moment it started in nanoseconds.
    private ArrayMap<String, Long> trackSounds;

    private Sound gameOver;
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
    private Sound finalEnemyIntro;
    private Sound finalEnemyPowerUp;
    private Sound finalEnemyPowerDown;
    private Sound finalEnemyExplosion;
    private Sound finalEnemyHit;
    private Sound levelCompleted;
    private Sound boing;
    private Sound click;
    private Sound aplause;
    private Sound squish;
    private Sound frozen;
    private Sound beam;
    private Sound pum;
    private Sound chirp;
    private Sound squeak;
    private Sound spring;
    private Sound growl;
    private Sound warble;
    private Sound dragon;
    private Sound buzz;
    private Sound whistle;

    public AssetSounds(AssetManager am) {
        trackSounds = new ArrayMap<String, Long>();
        gameOver = am.get(Assets.FX_FILE_GAME_OVER, Sound.class);
        bump = am.get(Assets.FX_FILE_BUMP, Sound.class);
        crack = am.get(Assets.FX_FILE_CRACK, Sound.class);
        dead = am.get(Assets.FX_FILE_DEAD, Sound.class);
        enemyShoot = am.get(Assets.FX_FILE_ENEMY_SHOOT, Sound.class);
        heroShoot = am.get(Assets.FX_FILE_HERO_SHOOT, Sound.class);
        heroShootEmpty = am.get(Assets.FX_FILE_HERO_SHOOT_EMPTY, Sound.class);
        heroShootSwish = am.get(Assets.FX_FILE_HERO_SHOOT_SWISH, Sound.class);
        hit = am.get(Assets.FX_FILE_HIT, Sound.class);
        openPowerBox = am.get(Assets.FX_FILE_OPEN_POWER_BOX, Sound.class);
        clock = am.get(Assets.FX_FILE_CLOCK, Sound.class);
        pickUpColOne = am.get(Assets.FX_FILE_PICK_UP_COL_ONE, Sound.class);
        pickUpColSilverBullet = am.get(Assets.FX_FILE_PICK_UP_COL_SILVER_BULLET, Sound.class);
        pickUpPowerOne = am.get(Assets.FX_FILE_PICK_UP_POWER_ONE, Sound.class);
        pickUpPowerTwo = am.get(Assets.FX_FILE_PICK_UP_POWER_TWO, Sound.class);
        pickUpPowerThree = am.get(Assets.FX_FILE_PICK_UP_POWER_THREE, Sound.class);
        pickUpPowerFour = am.get(Assets.FX_FILE_PICK_UP_POWER_FOUR, Sound.class);
        powerDown = am.get(Assets.FX_FILE_POWER_DOWN, Sound.class);
        beepA = am.get(Assets.FX_FILE_BEEP_A, Sound.class);
        beepB = am.get(Assets.FX_FILE_BEEP_B, Sound.class);
        showUpColOne = am.get(Assets.FX_FILE_SHOW_UP_COL_ONE, Sound.class);
        showUpColSilverBullet = am.get(Assets.FX_FILE_SHOW_UP_COL_SILVER_BULLET, Sound.class);
        showUpPowerOne = am.get(Assets.FX_FILE_SHOW_UP_POWER_ONE, Sound.class);
        showUpPowerTwo = am.get(Assets.FX_FILE_SHOW_UP_POWER_TWO, Sound.class);
        showUpPowerThree = am.get(Assets.FX_FILE_SHOW_UP_POWER_THREE, Sound.class);
        showUpPowerFour = am.get(Assets.FX_FILE_SHOW_UP_POWER_FOUR, Sound.class);
        timeIsUp = am.get(Assets.FX_FILE_TIME_IS_UP, Sound.class);
        finalEnemyPowerUp = am.get(Assets.FX_FILE_FINAL_ENEMY_POWER_UP, Sound.class);
        finalEnemyPowerDown = am.get(Assets.FX_FILE_FINAL_ENEMY_POWER_DOWN, Sound.class);
        finalEnemyExplosion = am.get(Assets.FX_FILE_FINAL_ENEMY_EXPLOSION, Sound.class);
        finalEnemyHit = am.get(Assets.FX_FILE_FINAL_ENEMY_HIT, Sound.class);
        finalEnemyIntro = am.get(Assets.FX_FILE_FINAL_ENEMY_INTRO, Sound.class);
        levelCompleted = am.get(Assets.FX_FILE_LEVEL_COMPLETED, Sound.class);
        boing = am.get(Assets.FX_FILE_BOUNCE, Sound.class);
        click = am.get(Assets.FX_FILE_CLICK, Sound.class);
        aplause = am.get(Assets.FX_FILE_APPLAUSE, Sound.class);
        squish = am.get(Assets.FX_FILE_SQUISH, Sound.class);
        frozen = am.get(Assets.FX_FILE_FROZEN, Sound.class);
        beam = am.get(Assets.FX_FILE_BEAM, Sound.class);
        pum = am.get(Assets.FX_FILE_PUM, Sound.class);
        chirp = am.get(Assets.FX_FILE_CHIRP, Sound.class);
        squeak = am.get(Assets.FX_FILE_SQUEAK, Sound.class);
        spring = am.get(Assets.FX_FILE_SPRING, Sound.class);
        growl = am.get(Assets.FX_FILE_GROWL, Sound.class);
        warble = am.get(Assets.FX_FILE_WARBLE, Sound.class);
        dragon = am.get(Assets.FX_FILE_DRAGON, Sound.class);
        buzz = am.get(Assets.FX_FILE_BUZZ, Sound.class);
        whistle = am.get(Assets.FX_FILE_WHISTLE, Sound.class);
    }

    public void setStartTime(String soundFilename, Long nanoTime) {
        trackSounds.put(soundFilename, nanoTime);
    }

    public Long getStartTime(String soundFilename) {
        return trackSounds.get(soundFilename);
    }

    public Sound getGameOver() {
        return gameOver;
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

    public Sound getFinalEnemyPowerUp() {
        return finalEnemyPowerUp;
    }

    public Sound getFinalEnemyPowerDown() {
        return finalEnemyPowerDown;
    }

    public Sound getFinalEnemyExplosion() {
        return finalEnemyExplosion;
    }

    public Sound getFinalEnemyHit() {
        return finalEnemyHit;
    }

    public Sound getFinalEnemyIntro() {
        return finalEnemyIntro;
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

    public Sound getFrozen() {
        return frozen;
    }

    public Sound getBeam() {
        return beam;
    }

    public Sound getPum() {
        return pum;
    }

    public Sound getChirp() {
        return chirp;
    }

    public Sound getSqueak() {
        return squeak;
    }

    public Sound getSpring() {
        return spring;
    }

    public Sound getGrowl() {
        return growl;
    }

    public Sound getWarble() {
        return warble;
    }

    public Sound getDragon() {
        return dragon;
    }

    public Sound getBuzz() {
        return buzz;
    }

    public Sound getWhistle() {
        return whistle;
    }
}