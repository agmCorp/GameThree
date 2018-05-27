package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.scene2d.AssetStageCleared;
import uy.com.agm.gamethree.game.GameSettings;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.UIFactory;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.widget.AnimatedActor;

/**
 * Created by AGM on 12/23/2017.
 */

public class LevelCompletedScreen extends AbstractScreen {
    private static final String TAG = LevelCompletedScreen.class.getName();

    private int currentLevel;
    private int finalLives;
    private int finalScore;
    private int finalSkulls;
    private int nextLevel;

    public LevelCompletedScreen(Integer currentLevel, Integer finalLives, Integer finalScore, Integer finalSkulls) {
        super();
        this.currentLevel = currentLevel;
        this.finalLives = finalLives;
        this.finalScore = finalScore;
        this.finalSkulls = finalSkulls;
        this.nextLevel = currentLevel + 1;
        if (this.nextLevel <= GameSettings.MAX_AVAILABLE_LEVEL) {
            GameSettings.getInstance().addActiveLevel(nextLevel, finalLives, finalScore, finalSkulls);
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

        // Animation
        AnimatedActor animatedActor = new AnimatedActor();
        animatedActor.setAlign(Align.center);
        animatedActor.setAnimation(Assets.getInstance().getScene2d().getStageCleared().getStageClearedAnimation());

        // Define our labels based on labelStyle
        Label currentLevelLabel = new Label(i18NGameThreeBundle.format("levelCompleted.currentLevel", currentLevel), labelStyleBig);
        Label finalScoreLabel = new Label(i18NGameThreeBundle.format("levelCompleted.finalScore", finalScore), labelStyleNormal);
        Label nextLevelLabel = new Label(i18NGameThreeBundle.format("levelCompleted.nextLevel"), labelStyleNormal);
        Label newLevelsLabel = new Label(i18NGameThreeBundle.format("levelCompleted.newLevels"), labelStyleNormal);

        // Add values
        table.add(currentLevelLabel);
        table.row();
        table.add(animatedActor).size(AssetStageCleared.WIDTH_PIXELS, AssetStageCleared.HEIGHT_PIXELS).padTop(AbstractScreen.PAD);
        table.row();
        table.add(finalScoreLabel).padTop(AbstractScreen.PAD);
        table.row();
        if (nextLevel <= GameSettings.MAX_AVAILABLE_LEVEL) {
            table.row();
            table.add(nextLevelLabel).padTop(AbstractScreen.PAD * 2);
        } else {
            table.row();
            table.add(newLevelsLabel).padTop(AbstractScreen.PAD * 2);
        }

        // Set table structure
        Table navigation = new Table();

        // Debug lines
        navigation.setDebug(PlayScreen.DEBUG_MODE);

        // Bottom-Align table
        navigation.bottom();

        // Make the table fill the entire stage
        navigation.setFillParent(true);

        // Define images
        Image back = new Image(Assets.getInstance().getScene2d().getBack());

        // Add values
        navigation.add(back).padBottom(AbstractScreen.PAD * 2);

        // Events
        if (nextLevel <= GameSettings.MAX_AVAILABLE_LEVEL) {
            nextLevelLabel.addListener(UIFactory.createListener(ScreenEnum.GAME, nextLevel, finalLives, finalScore, finalSkulls));
        }
        back.addListener(UIFactory.createListener(ScreenEnum.MAIN_MENU));

        // Adds created tables to stage
        addActor(table);
        addActor(navigation);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
