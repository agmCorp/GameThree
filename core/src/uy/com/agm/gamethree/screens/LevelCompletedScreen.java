package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.game.Constants;
import uy.com.agm.gamethree.game.GameSettings;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.UIFactory;
import uy.com.agm.gamethree.tools.AudioManager;

/**
 * Created by AGM on 12/23/2017.
 */

public class LevelCompletedScreen extends AbstractScreen {
    private static final String TAG = LevelCompletedScreen.class.getName();

    private int currentLevel;
    private int finalScore;
    private int nextLevel;

    public LevelCompletedScreen(Integer currentLevel, Integer finalScore) {
        super();
        this.currentLevel = currentLevel;
        this.finalScore = finalScore;
        this.nextLevel = currentLevel + 1;
        if (this.nextLevel <= Constants.MAX_AVAILABLE_LEVEL) {
            GameSettings.getInstance().addAvailableLevel(nextLevel);
            GameSettings.getInstance().save();
        }
    }

    @Override
    public void buildStage() {
        // I18n
        I18NBundle i18NGameThreeBundle = Assets.getInstance().getI18NGameThree().getI18NGameThreeBundle();

        // Set table structure
        Table table = new Table();

        // Design
        table.setBackground(new TextureRegionDrawable(Assets.getInstance().getScene2d().getTable()));

        // Debug lines
        table.setDebug(Constants.DEBUG_MODE);

        // Center-Align table
        table.center();

        // Make the table fill the entire stage
        table.setFillParent(true);

        // Personal fonts
        Label.LabelStyle labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.getInstance().getFonts().getDefaultBig();

        Label.LabelStyle labelStyleNormal = new Label.LabelStyle();
        labelStyleNormal.font = Assets.getInstance().getFonts().getDefaultNormal();

        // Define our labels based on labelStyle
        Label currentLevelLabel = new Label(i18NGameThreeBundle.format("levelCompleted.currentLevel", this.currentLevel), labelStyleBig);
        Label levelCompletedLabel = new Label(i18NGameThreeBundle.format("levelCompleted.completed"), labelStyleBig);
        Label finalScoreLabel = new Label(i18NGameThreeBundle.format("levelCompleted.finalScore", this.finalScore), labelStyleNormal);
        Label playAgainLabel = new Label(i18NGameThreeBundle.format("levelCompleted.playAgain"), labelStyleNormal);
        Label nextLevelLabel = new Label(i18NGameThreeBundle.format("levelCompleted.nextLevel"), labelStyleNormal);
        Label newLevelsLabel = new Label(i18NGameThreeBundle.format("levelCompleted.newLevels"), labelStyleNormal);
        Label backLabel = new Label(i18NGameThreeBundle.format("levelCompleted.backToMenu"), labelStyleNormal);

        // Add values
        table.add(currentLevelLabel).center();
        table.row();
        table.add(levelCompletedLabel).padTop(Constants.PAD_TOP).center();
        table.row();
        table.add(finalScoreLabel).padTop(Constants.PAD_TOP).center();
        table.row();
        table.add(playAgainLabel).padTop(Constants.PAD_TOP * 2).center();
        if (this.nextLevel <= Constants.MAX_AVAILABLE_LEVEL) {
            table.row();
            table.add(nextLevelLabel).padTop(Constants.PAD_TOP).center();
        } else {
            table.row();
            table.add(newLevelsLabel).padTop(Constants.PAD_TOP).center();
        }
        table.row();
        table.add(backLabel).padTop(Constants.PAD_TOP * 2).center();

        // Events
        playAgainLabel.addListener(UIFactory.createListener(ScreenEnum.GAME, this.currentLevel, 0));
        if (this.nextLevel <= Constants.MAX_AVAILABLE_LEVEL) {
            nextLevelLabel.addListener(UIFactory.createListener(ScreenEnum.GAME, this.nextLevel, this.finalScore));
        }
        backLabel.addListener(UIFactory.createListener(ScreenEnum.MAIN_MENU));

        // Audio FX
        AudioManager.getInstance().play(Assets.getInstance().getSounds().getAplause());

        // Adds created table to stage
        addActor(table);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
