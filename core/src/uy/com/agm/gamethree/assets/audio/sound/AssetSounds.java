package uy.com.agm.gamethree.assets.audio.sound;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

import uy.com.agm.gamethree.assets.Assets;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetSounds {
    private static final String TAG = AssetSounds.class.getName();

    private Sound gameOver;
    private Sound bump;
    private Sound crack;
    private Sound crunch;
    private Sound dead;
    private Sound enemyShoot;
    private Sound heroShoot;
    private Sound rocketShoot;
    private Sound swordShoot;
    private Sound blastShoot;
    private Sound magicShoot;
    private Sound heroShootEmpty;
    private Sound heroShootSwish;
    private Sound hit;
    private Sound heroHit;
    private Sound openPowerBox;
    private Sound clock;
    private Sound pickUpColOne;
    private Sound pickUpColTwo;
    private Sound pickUpColThree;
    private Sound pickUpColSilverBullet;
    private Sound pickUpPowerOne;
    private Sound pickUpPowerTwo;
    private Sound pickUpPowerThree;
    private Sound pickUpPowerFour;
    private Sound pickUpPowerFive;
    private Sound abilityPowerDown;
    private Sound weaponPowerDown;
    private Sound beepA;
    private Sound beepB;
    private Sound beepC;
    private Sound showUpColOne;
    private Sound showUpColTwo;
    private Sound showUpColThree;
    private Sound showUpColSilverBullet;
    private Sound showUpPowerOne;
    private Sound showUpPowerTwo;
    private Sound showUpPowerThree;
    private Sound showUpPowerFour;
    private Sound showUpPowerFive;
    private Sound timeIsUp;
    private Sound bossIntro;
    private Sound bossPowerUp;
    private Sound bossPowerDown;
    private Sound bossExplosion;
    private Sound bossHit;
    private Sound levelCompleted;
    private Sound boing;
    private Sound click;
    private Sound applause;
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
    private Sound juicy;
    private Sound marching;
    private Sound flap;
    private Sound bite;
    private Sound roar;
    private Sound pebbles;
    private Sound blade;
    private Sound flutter;
    private Sound threat;

    public AssetSounds(AssetManager am) {
        gameOver = am.get(Assets.FX_FILE_GAME_OVER, Sound.class);
        bump = am.get(Assets.FX_FILE_BUMP, Sound.class);
        crack = am.get(Assets.FX_FILE_CRACK, Sound.class);
        crunch = am.get(Assets.FX_FILE_CRUNCH, Sound.class);
        dead = am.get(Assets.FX_FILE_DEAD, Sound.class);
        enemyShoot = am.get(Assets.FX_FILE_ENEMY_SHOOT, Sound.class);
        heroShoot = am.get(Assets.FX_FILE_HERO_SHOOT, Sound.class);
        rocketShoot = am.get(Assets.FX_FILE_ROCKET_SHOOT, Sound.class);
        swordShoot = am.get(Assets.FX_FILE_SWORD_SHOOT, Sound.class);
        blastShoot = am.get(Assets.FX_FILE_BLAST_SHOOT, Sound.class);
        magicShoot = am.get(Assets.FX_FILE_MAGIC_SHOOT, Sound.class);
        heroShootEmpty = am.get(Assets.FX_FILE_HERO_SHOOT_EMPTY, Sound.class);
        heroShootSwish = am.get(Assets.FX_FILE_HERO_SHOOT_SWISH, Sound.class);
        hit = am.get(Assets.FX_FILE_HIT, Sound.class);
        heroHit = am.get(Assets.FX_FILE_HERO_HIT, Sound.class);
        openPowerBox = am.get(Assets.FX_FILE_OPEN_POWER_BOX, Sound.class);
        clock = am.get(Assets.FX_FILE_CLOCK, Sound.class);
        pickUpColOne = am.get(Assets.FX_FILE_PICK_UP_COL_ONE, Sound.class);
        pickUpColTwo = am.get(Assets.FX_FILE_PICK_UP_COL_TWO, Sound.class);
        pickUpColThree = am.get(Assets.FX_FILE_PICK_UP_COL_THREE, Sound.class);
        pickUpColSilverBullet = am.get(Assets.FX_FILE_PICK_UP_COL_SILVER_BULLET, Sound.class);
        pickUpPowerOne = am.get(Assets.FX_FILE_PICK_UP_POWER_ONE, Sound.class);
        pickUpPowerTwo = am.get(Assets.FX_FILE_PICK_UP_POWER_TWO, Sound.class);
        pickUpPowerThree = am.get(Assets.FX_FILE_PICK_UP_POWER_THREE, Sound.class);
        pickUpPowerFour = am.get(Assets.FX_FILE_PICK_UP_POWER_FOUR, Sound.class);
        pickUpPowerFive = am.get(Assets.FX_FILE_PICK_UP_POWER_FIVE, Sound.class);
        abilityPowerDown = am.get(Assets.FX_FILE_ABILITY_POWER_DOWN, Sound.class);
        weaponPowerDown = am.get(Assets.FX_FILE_WEAPON_POWER_DOWN, Sound.class);
        beepA = am.get(Assets.FX_FILE_BEEP_A, Sound.class);
        beepB = am.get(Assets.FX_FILE_BEEP_B, Sound.class);
        beepC = am.get(Assets.FX_FILE_BEEP_C, Sound.class);
        showUpColOne = am.get(Assets.FX_FILE_SHOW_UP_COL_ONE, Sound.class);
        showUpColTwo = am.get(Assets.FX_FILE_SHOW_UP_COL_TWO, Sound.class);
        showUpColThree = am.get(Assets.FX_FILE_SHOW_UP_COL_THREE, Sound.class);
        showUpColSilverBullet = am.get(Assets.FX_FILE_SHOW_UP_COL_SILVER_BULLET, Sound.class);
        showUpPowerOne = am.get(Assets.FX_FILE_SHOW_UP_POWER_ONE, Sound.class);
        showUpPowerTwo = am.get(Assets.FX_FILE_SHOW_UP_POWER_TWO, Sound.class);
        showUpPowerThree = am.get(Assets.FX_FILE_SHOW_UP_POWER_THREE, Sound.class);
        showUpPowerFour = am.get(Assets.FX_FILE_SHOW_UP_POWER_FOUR, Sound.class);
        showUpPowerFive = am.get(Assets.FX_FILE_SHOW_UP_POWER_FIVE, Sound.class);
        timeIsUp = am.get(Assets.FX_FILE_TIME_IS_UP, Sound.class);
        bossPowerUp = am.get(Assets.FX_FILE_BOSS_POWER_UP, Sound.class);
        bossPowerDown = am.get(Assets.FX_FILE_BOSS_POWER_DOWN, Sound.class);
        bossExplosion = am.get(Assets.FX_FILE_BOSS_EXPLOSION, Sound.class);
        bossHit = am.get(Assets.FX_FILE_BOSS_HIT, Sound.class);
        bossIntro = am.get(Assets.FX_FILE_BOSS_INTRO, Sound.class);
        levelCompleted = am.get(Assets.FX_FILE_LEVEL_COMPLETED, Sound.class);
        boing = am.get(Assets.FX_FILE_BOUNCE, Sound.class);
        click = am.get(Assets.FX_FILE_CLICK, Sound.class);
        applause = am.get(Assets.FX_FILE_APPLAUSE, Sound.class);
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
        juicy = am.get(Assets.FX_FILE_JUICY, Sound.class);
        marching = am.get(Assets.FX_FILE_MARCHING, Sound.class);
        flap = am.get(Assets.FX_FILE_FLAP, Sound.class);
        bite = am.get(Assets.FX_FILE_BITE, Sound.class);
        roar = am.get(Assets.FX_FILE_ROAR, Sound.class);
        pebbles = am.get(Assets.FX_FILE_PEBBLES, Sound.class);
        blade = am.get(Assets.FX_FILE_BLADE, Sound.class);
        flutter = am.get(Assets.FX_FILE_FLUTTER, Sound.class);
        threat = am.get(Assets.FX_FILE_THREAT, Sound.class);
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

    public Sound getCrunch() {
        return crunch;
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

    public Sound getRocketShoot() {
        return rocketShoot;
    }

    public Sound getSwordShoot() {
        return swordShoot;
    }

    public Sound getBlastShoot() {
        return blastShoot;
    }

    public Sound getMagicShoot() {
        return magicShoot;
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

    public Sound getHeroHit() {
        return heroHit;
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

    public Sound getPickUpColTwo() {
        return pickUpColTwo;
    }

    public Sound getPickUpColThree() {
        return pickUpColThree;
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

    public Sound getPickUpPowerFive() {
        return pickUpPowerFive;
    }

    public Sound getAbilityPowerDown() {
        return abilityPowerDown;
    }

    public Sound getWeaponPowerDown() {
        return weaponPowerDown;
    }

    public Sound getBeepA() {
        return beepA;
    }

    public Sound getBeepB() {
        return beepB;
    }

    public Sound getBeepC() {
        return beepC;
    }

    public Sound getShowUpColOne() {
        return showUpColOne;
    }

    public Sound getShowUpColTwo() {
        return showUpColTwo;
    }

    public Sound getShowUpColThree() {
        return showUpColThree;
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

    public Sound getShowUpPowerFive() {
        return showUpPowerFive;
    }

    public Sound getTimeIsUp() {
        return timeIsUp;
    }

    public Sound getBossPowerUp() {
        return bossPowerUp;
    }

    public Sound getBossPowerDown() {
        return bossPowerDown;
    }

    public Sound getBossExplosion() {
        return bossExplosion;
    }

    public Sound getBossHit() {
        return bossHit;
    }

    public Sound getBossIntro() {
        return bossIntro;
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

    public Sound getApplause() {
        return applause;
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

    public Sound getJuicy() {
        return juicy;
    }

    public Sound getMarching() {
        return marching;
    }

    public Sound getFlap() {
        return flap;
    }

    public Sound getBite() {
        return bite;
    }

    public Sound getRoar() {
        return roar;
    }

    public Sound getPebbles() {
        return pebbles;
    }

    public Sound getBlade() {
        return blade;
    }

    public Sound getFlutter() {
        return flutter;
    }

    public Sound getThreat() {
        return threat;
    }
}