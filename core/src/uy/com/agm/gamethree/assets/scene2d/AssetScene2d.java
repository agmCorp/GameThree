package uy.com.agm.gamethree.assets.scene2d;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetScene2d {
    private static final String TAG = AssetScene2d.class.getName();

    private TextureRegion sliderBackground;
    private TextureRegion sliderKnob;
    private TextureRegion helpInitialManual;
    private TextureRegion helpInitialAutomatic;
    private TextureRegion helpEnemyOne;
    private TextureRegion helpEnemyTwo;
    private TextureRegion helpEnemyThree;
    private TextureRegion helpEnemyFour;
    private TextureRegion helpEnemyFive;
    private TextureRegion helpEnemySix;
    private TextureRegion helpEnemySeven;
    private TextureRegion helpEnemyEight;
    private TextureRegion helpEnemyNine;
    private TextureRegion helpEnemyTen;
    private TextureRegion helpEnemyEleven;
    private TextureRegion helpEnemyTwelve;
    private TextureRegion helpPowerOne;
    private TextureRegion helpPowerTwo;
    private TextureRegion helpPowerThree;
    private TextureRegion helpPowerFour;
    private TextureRegion helpPowerFive;
    private TextureRegion helpColOne;
    private TextureRegion helpColTwo;
    private TextureRegion helpColThree;
    private TextureRegion helpColSilverBulletLevelOne;
    private TextureRegion helpColSilverBulletLevelTwo;
    private TextureRegion helpColSilverBulletLevelThree;
    private TextureRegion helpFinalEnemyLevelOne;
    private TextureRegion helpFinalEnemyLevelTwo;
    private TextureRegion helpFinalEnemyLevelThree;
    private TextureRegion table;
    private TextureRegion red;
    private TextureRegion heroHead;
    private TextureRegion skullHead;
    private TextureRegion shuriken;
    private AssetVictory victory;
    private AssetStageCleared stageCleared;
    private AssetStageFailed stageFailed;
    private AssetGameOver gameOver;
    private AssetLetsGo letsGo;

    public AssetScene2d(TextureAtlas atlasUI) {
        sliderBackground = atlasUI.findRegion("sliderBackground");
        sliderKnob = atlasUI.findRegion("sliderKnob");

        helpInitialManual = atlasUI.findRegion("helpInitialManual");
        helpInitialAutomatic = atlasUI.findRegion("helpInitialAutomatic");
        helpEnemyOne = atlasUI.findRegion("helpEnemyOne");
        helpEnemyTwo = atlasUI.findRegion("helpEnemyTwo");
        helpEnemyThree = atlasUI.findRegion("helpEnemyThree");
        helpEnemyFour = atlasUI.findRegion("helpEnemyFour");
        helpEnemyFive = atlasUI.findRegion("helpEnemyFive");
        helpEnemySix = atlasUI.findRegion("helpEnemySix");
        helpEnemySeven = atlasUI.findRegion("helpEnemySeven");
        helpEnemyEight = atlasUI.findRegion("helpEnemyEight");
        helpEnemyNine = atlasUI.findRegion("helpEnemyNine");
        helpEnemyTen = atlasUI.findRegion("helpEnemyTen");
        helpEnemyEleven = atlasUI.findRegion("helpEnemyEleven");
        helpEnemyTwelve = atlasUI.findRegion("helpEnemyTwelve");
        helpPowerOne = atlasUI.findRegion("helpPowerOne");
        helpPowerTwo = atlasUI.findRegion("helpPowerTwo");
        helpPowerThree = atlasUI.findRegion("helpPowerThree");
        helpPowerFour = atlasUI.findRegion("helpPowerFour");
        helpPowerFive = atlasUI.findRegion("helpPowerFive");
        helpColOne = atlasUI.findRegion("helpColOne");
        helpColTwo = atlasUI.findRegion("helpColTwo");
        helpColThree = atlasUI.findRegion("helpColThree");
        helpColSilverBulletLevelOne = atlasUI.findRegion("helpColSilverBulletLevelOne");
        helpColSilverBulletLevelTwo = atlasUI.findRegion("helpColSilverBulletLevelTwo");
        helpColSilverBulletLevelThree = atlasUI.findRegion("helpColSilverBulletLevelThree");
        helpFinalEnemyLevelOne = atlasUI.findRegion("helpFinalEnemyLevelOne");
        helpFinalEnemyLevelTwo = atlasUI.findRegion("helpFinalEnemyLevelTwo");
        helpFinalEnemyLevelThree = atlasUI.findRegion("helpFinalEnemyLevelThree");
        table = atlasUI.findRegion("table");
        red = atlasUI.findRegion("red");
        heroHead = atlasUI.findRegion("heroHead");
        skullHead = atlasUI.findRegion("skullHead");
        shuriken = atlasUI.findRegion("shuriken");
        victory = new AssetVictory(atlasUI);
        stageCleared = new AssetStageCleared(atlasUI);
        stageFailed = new AssetStageFailed(atlasUI);
        gameOver = new AssetGameOver(atlasUI);
        letsGo = new AssetLetsGo(atlasUI);
    }

    public TextureRegion getSliderBackground() {
        return sliderBackground;
    }

    public TextureRegion getSliderKnob() {
        return sliderKnob;
    }

    public TextureRegion getHelpInitialManual() {
        return helpInitialManual;
    }

    public TextureRegion getHelpInitialAutomatic() {
        return helpInitialAutomatic;
    }

    public TextureRegion getHelpEnemyOne() {
        return helpEnemyOne;
    }

    public TextureRegion getHelpEnemyTwo() {
        return helpEnemyTwo;
    }

    public TextureRegion getHelpEnemyThree() {
        return helpEnemyThree;
    }

    public TextureRegion getHelpEnemyFour() {
        return helpEnemyFour;
    }

    public TextureRegion getHelpEnemyFive() {
        return helpEnemyFive;
    }

    public TextureRegion getHelpEnemySix() {
        return helpEnemySix;
    }

    public TextureRegion getHelpEnemySeven() {
        return helpEnemySeven;
    }

    public TextureRegion getHelpEnemyEight() {
        return helpEnemyEight;
    }

    public TextureRegion getHelpEnemyNine() {
        return helpEnemyNine;
    }

    public TextureRegion getHelpEnemyTen() {
        return helpEnemyTen;
    }

    public TextureRegion getHelpEnemyEleven() {
        return helpEnemyEleven;
    }

    public TextureRegion getHelpEnemyTwelve() {
        return helpEnemyTwelve;
    }

    public TextureRegion getHelpPowerOne() {
        return helpPowerOne;
    }

    public TextureRegion getHelpPowerTwo() {
        return helpPowerTwo;
    }

    public TextureRegion getHelpPowerThree() {
        return helpPowerThree;
    }

    public TextureRegion getHelpPowerFour() {
        return helpPowerFour;
    }

    public TextureRegion getHelpPowerFive() {
        return helpPowerFive;
    }

    public TextureRegion getHelpColOne() {
        return helpColOne;
    }

    public TextureRegion getHelpColTwo() {
        return helpColTwo;
    }

    public TextureRegion getHelpColThree() {
        return helpColThree;
    }

    public TextureRegion getHelpColSilverBulletLevelOne() {
        return helpColSilverBulletLevelOne;
    }

    public TextureRegion getHelpColSilverBulletLevelTwo() {
        return helpColSilverBulletLevelTwo;
    }

    public TextureRegion getHelpColSilverBulletLevelThree() {
        return helpColSilverBulletLevelThree;
    }

    public TextureRegion getHelpFinalEnemyLevelOne() {
        return helpFinalEnemyLevelOne;
    }

    public TextureRegion getHelpFinalEnemyLevelTwo() {
        return helpFinalEnemyLevelTwo;
    }

    public TextureRegion getHelpFinalEnemyLevelThree() {
        return helpFinalEnemyLevelThree;
    }

    public TextureRegion getTable() {
        return table;
    }

    public TextureRegion getRed() {
        return red;
    }

    public TextureRegion getHeroHead() {
        return heroHead;
    }

    public TextureRegion getSkullHead() {
        return skullHead;
    }

    public TextureRegion getShuriken() {
        return shuriken;
    }

    public AssetVictory getVictory() {
        return victory;
    }

    public AssetStageCleared getStageCleared() {
        return stageCleared;
    }

    public AssetStageFailed getStageFailed() {
        return stageFailed;
    }

    public AssetGameOver getGameOver() {
        return gameOver;
    }

    public AssetLetsGo getLetsGo() {
        return letsGo;
    }
}
