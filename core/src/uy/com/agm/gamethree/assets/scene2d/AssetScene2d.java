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
    private TextureRegion helpPowerOne;
    private TextureRegion helpPowerTwo;
    private TextureRegion helpPowerThree;
    private TextureRegion helpPowerFour;
    private TextureRegion helpColOne;
    private TextureRegion helpColSilverBullet;
    private TextureRegion helpFinalEnemyLevelOne;

    public AssetScene2d(TextureAtlas atlas) {
        sliderBackground = atlas.findRegion("sliderBackground");
        sliderKnob = atlas.findRegion("sliderKnob");

        helpInitialManual = atlas.findRegion("helpInitialManual");
        helpInitialAutomatic = atlas.findRegion("helpInitialAutomatic");
        helpEnemyOne = atlas.findRegion("helpEnemyOne");
        helpEnemyTwo = atlas.findRegion("helpEnemyTwo");
        helpEnemyThree = atlas.findRegion("helpEnemyThree");
        helpEnemyFour = atlas.findRegion("helpEnemyFour");
        helpPowerOne = atlas.findRegion("helpPowerOne");
        helpPowerTwo = atlas.findRegion("helpPowerTwo");
        helpPowerThree = atlas.findRegion("helpPowerThree");
        helpPowerFour = atlas.findRegion("helpPowerFour");
        helpColOne = atlas.findRegion("helpColOne");
        helpColSilverBullet = atlas.findRegion("helpColSilverBullet");
        helpFinalEnemyLevelOne = atlas.findRegion("helpFinalEnemyLevelOne");
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

    public TextureRegion getHelpColOne() {
        return helpColOne;
    }

    public TextureRegion getHelpColSilverBullet() {
        return helpColSilverBullet;
    }

    public TextureRegion getHelpFinalEnemyLevelOne() {
        return helpFinalEnemyLevelOne;
    }
}
