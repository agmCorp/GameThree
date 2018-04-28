package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;

import uy.com.agm.gamethree.assets.Assets;
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
    private int lives;
    private int finalScore;
    private int nextLevel;

    public LevelCompletedScreen(Integer currentLevel, Integer lives, Integer finalScore) {
        super();
        this.currentLevel = currentLevel;
        this.lives = lives;
        this.finalScore = finalScore;
        this.nextLevel = currentLevel + 1;
        if (this.nextLevel <= GameSettings.MAX_AVAILABLE_LEVEL) {
            GameSettings.getInstance().addAvailableLevel(nextLevel);
            GameSettings.getInstance().save();
        }

        // Audio FX
        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getApplause());
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
        table.setDebug(PlayScreen.DEBUG_MODE);

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
        Label nextLevelLabel = new Label(i18NGameThreeBundle.format("levelCompleted.nextLevel"), labelStyleNormal);
        Label newLevelsLabel = new Label(i18NGameThreeBundle.format("levelCompleted.newLevels"), labelStyleNormal);
        Label backLabel = new Label(i18NGameThreeBundle.format("levelCompleted.backToMenu"), labelStyleNormal);

        // Add values
        table.add(currentLevelLabel).center();
        table.row();
        table.add(levelCompletedLabel).padTop(AbstractScreen.PAD_TOP).center();
        table.row();
        table.add(finalScoreLabel).padTop(AbstractScreen.PAD_TOP).center();
        table.row();
        if (this.nextLevel <= GameSettings.MAX_AVAILABLE_LEVEL) {
            table.row();
            table.add(nextLevelLabel).padTop(AbstractScreen.PAD_TOP * 2).center();
        } else {
            table.row();
            table.add(newLevelsLabel).padTop(AbstractScreen.PAD_TOP * 2).center();
        }
        table.row();
        table.add(backLabel).padTop(AbstractScreen.PAD_TOP * 2).center();

        // Events
        if (this.nextLevel <= GameSettings.MAX_AVAILABLE_LEVEL) {
            nextLevelLabel.addListener(UIFactory.createListener(ScreenEnum.GAME, this.nextLevel, this.lives, this.finalScore));
        }
        backLabel.addListener(UIFactory.createListener(ScreenEnum.MAIN_MENU));

        // Adds created table to stage
        addActor(table);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
