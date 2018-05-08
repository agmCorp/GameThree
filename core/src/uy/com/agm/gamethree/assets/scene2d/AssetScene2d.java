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
    private TextureRegion helpPowerOne;
    private TextureRegion helpPowerTwo;
    private TextureRegion helpPowerThree;
    private TextureRegion helpPowerFour;
    private TextureRegion helpPowerFive;
    private TextureRegion helpColOne;
    private TextureRegion helpColSilverBulletLevelOne;
    private TextureRegion helpColSilverBulletLevelTwo;
    private TextureRegion helpColSilverBulletLevelThree;
    private TextureRegion helpFinalEnemyLevelOne;
    private TextureRegion helpFinalEnemyLevelTwo;
    private TextureRegion helpFinalEnemyLevelThree;
    private TextureRegion table;

    public AssetScene2d(TextureAtlas atlas) {
        sliderBackground = atlas.findRegion("sliderBackground");
        sliderKnob = atlas.findRegion("sliderKnob");

        helpInitialManual = atlas.findRegion("helpInitialManual");
        helpInitialAutomatic = atlas.findRegion("helpInitialAutomatic");
        helpEnemyOne = atlas.findRegion("helpEnemyOne");
        helpEnemyTwo = atlas.findRegion("helpEnemyTwo");
        helpEnemyThree = atlas.findRegion("helpEnemyThree");
        helpEnemyFour = atlas.findRegion("helpEnemyFour");
        helpEnemyFive = atlas.findRegion("helpEnemyFive");
        helpEnemySix = atlas.findRegion("helpEnemySix");
        helpEnemySeven = atlas.findRegion("helpEnemySeven");
        helpEnemyEight = atlas.findRegion("helpEnemyEight");
        helpEnemyNine = atlas.findRegion("helpEnemyNine");
        helpEnemyTen = atlas.findRegion("helpEnemyTen");
        helpEnemyEleven = atlas.findRegion("helpEnemyEleven");
        helpPowerOne = atlas.findRegion("helpPowerOne");
        helpPowerTwo = atlas.findRegion("helpPowerTwo");
        helpPowerThree = atlas.findRegion("helpPowerThree");
        helpPowerFour = atlas.findRegion("helpPowerFour");
        helpPowerFive = atlas.findRegion("helpPowerFive");
        helpColOne = atlas.findRegion("helpColOne");
        helpColSilverBulletLevelOne = atlas.findRegion("helpColSilverBulletLevelOne");
        helpColSilverBulletLevelTwo = atlas.findRegion("helpColSilverBulletLevelTwo");
        helpColSilverBulletLevelThree = atlas.findRegion("helpColSilverBulletLevelTwo"); // todo
        helpFinalEnemyLevelOne = atlas.findRegion("helpFinalEnemyLevelOne");
        helpFinalEnemyLevelTwo = atlas.findRegion("helpFinalEnemyLevelTwo");
        helpFinalEnemyLevelThree = atlas.findRegion("helpFinalEnemyLevelTwo"); // todo
        table = atlas.findRegion("table");
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
}
