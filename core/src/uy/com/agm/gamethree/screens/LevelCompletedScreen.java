package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Scaling;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.scene2d.AssetScene2d;
import uy.com.agm.gamethree.assets.scene2d.AssetStageCleared;
import uy.com.agm.gamethree.game.DebugConstants;
import uy.com.agm.gamethree.game.GameSettings;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.UIFactory;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.LevelFactory;
import uy.com.agm.gamethree.widget.AnimatedImage;
import uy.com.agm.gamethree.widget.TypingLabelWorkaround;

/**
 * Created by AGM on 12/23/2017.
 */

public class LevelCompletedScreen extends AbstractScreen {
    private static final String TAG = LevelCompletedScreen.class.getName();

    // Constants
    private static final int PENALTY_COST = 200;

    private int currentLevel;
    private int nextLevel;
    private int currentLives;
    private int currentScore;
    private int currentEndurance;
    private int currentPenalties;
    private int finalScore;
    private int currentStars;
    private TextureRegion trophy;
    private boolean showNewHighScoreLabel;
    private boolean showNextLevelLabel;

    public LevelCompletedScreen(Integer currentLevel, Integer currentLives, Integer currentScore, Integer currentEndurance) {
        super();
        this.currentLevel = currentLevel;
        this.nextLevel = currentLevel + 1;
        this.currentLives = currentLives;
        this.currentScore = currentScore;
        this.currentEndurance = currentEndurance;
        int maxEndurance = LevelFactory.getLevelEndurance(this.currentLevel);
        this.currentPenalties = this.currentEndurance >= maxEndurance ? 0 : maxEndurance - this.currentEndurance;
        this.finalScore = Math.abs(this.currentScore - this.currentPenalties * PENALTY_COST);
        this.currentStars = getStarsValue(this.currentEndurance, maxEndurance);

        GameSettings prefs = GameSettings.getInstance();
        prefs.setStars(this.currentLevel, this.currentStars);

        if (prefs.isNewGoldHighScore(this.finalScore)) {
            this.trophy = Assets.getInstance().getScene2d().getGoldTrophy();
            prefs.setGoldHighScore(this.finalScore);
        } else if (prefs.isNewSilverHighScore(this.finalScore)) {
            this.trophy = Assets.getInstance().getScene2d().getSilverTrophy();
            prefs.setSilverHighScore(this.finalScore);
        } else if (prefs.isNewBronzeHighScore(this.finalScore)) {
            this.trophy = Assets.getInstance().getScene2d().getBronzeTrophy();
            prefs.setBronzeHighScore(this.finalScore);
        } else {
            this.trophy = null;
        }
        this.showNewHighScoreLabel = this.trophy != null;
        this.showNextLevelLabel = this.nextLevel <= GameSettings.MAX_LEVEL;
        if (this.showNextLevelLabel) {
            prefs.addActiveLevel(this.nextLevel, this.currentLives, this.finalScore);

            // Resets levels from nextLevel + 1 to MAX_LEVEL
            for (int i = this.nextLevel + 1; i <= GameSettings.MAX_LEVEL; i++) {
                prefs.resetLevel(i);
            }
        }
        prefs.setShowGrandFinale(!showNextLevelLabel);

        // Saves preferences
        // We can't delete keys, only change values
        prefs.save();

        // Removes keys from memory
        if (!DebugConstants.DEBUG_LEVELS) {
            for (int i = this.nextLevel + 1; i <= GameSettings.MAX_LEVEL; i++) {
                prefs.removeLevel(i);
            }

            // If a level is removed then the grand finale is hidden
            prefs.setShowGrandFinale(!(this.nextLevel + 1 <= GameSettings.MAX_LEVEL));
        } else {
            // Enables grand finale
            prefs.setShowGrandFinale(true);
        }

        // Audio FX
        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getApplause());
    }

    @Override
    public void buildStage() {
        defineMainTable();
        defineNavigationTable();
    }

    private void defineMainTable() {
        // UI assets
        AssetScene2d assetScene2d = Assets.getInstance().getScene2d();

        // I18n
        I18NBundle i18NGameThreeBundle = Assets.getInstance().getI18NGameThree().getI18NGameThreeBundle();

        // Set table structure
        Table table = new Table();

        // Design
        table.setBackground(new TextureRegionDrawable(assetScene2d.getTable()));

        // Debug lines
        table.setDebug(DebugConstants.DEBUG_LINES);

        // Top-Align table
        table.top();

        // Make the table fill the entire stage
        table.setFillParent(true);

        // Personal fonts
        Label.LabelStyle labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.getInstance().getFonts().getDefaultBig();

        Label.LabelStyle labelStyleNormal = new Label.LabelStyle();
        labelStyleNormal.font = Assets.getInstance().getFonts().getDefaultNormal();

        // Animation
        AnimatedImage animatedImage = new AnimatedImage(assetScene2d.getStageCleared().getStageClearedAnimation());
        animatedImage.setAlign(Align.center);

        // Define our labels based on labelStyle
        Label currentScoreLabel = new Label(i18NGameThreeBundle.format("levelCompleted.currentScore", currentScore), labelStyleNormal);
        Label penaltiesLabel = new Label(i18NGameThreeBundle.format("levelCompleted.penalties", currentPenalties), labelStyleNormal);
        TypingLabelWorkaround finalScoreLabel = new TypingLabelWorkaround(i18NGameThreeBundle.format("levelCompleted.finalScore", finalScore), labelStyleNormal);
        Label newHighScoreLabel = new Label(i18NGameThreeBundle.format("levelCompleted.newHighScore"), labelStyleNormal);
        TypingLabelWorkaround nextLevelLabel = new TypingLabelWorkaround(i18NGameThreeBundle.format("levelCompleted.nextLevel"), labelStyleNormal);
        TypingLabelWorkaround grandFinaleLabel = new TypingLabelWorkaround(i18NGameThreeBundle.format("levelCompleted.grandFinale"), labelStyleNormal);

        // Add values
        table.add(animatedImage).size(AssetStageCleared.WIDTH_PIXELS, AssetStageCleared.HEIGHT_PIXELS).padTop(AbstractScreen.PAD);
        table.row();
        table.add(currentScoreLabel).padTop(AbstractScreen.PAD);
        table.row();
        table.add(penaltiesLabel).padTop(AbstractScreen.PAD);
        table.row();
        table.add(finalScoreLabel).padTop(AbstractScreen.PAD);
        table.row();
        table.add(getStarsTable(assetScene2d.getStar(), assetScene2d.getEmptyStar(), currentStars)).padTop(AbstractScreen.PAD);
        table.row();
        float padTop = AbstractScreen.PAD * 2;
        if (showNewHighScoreLabel) {
            padTop = AbstractScreen.PAD;
            table.add(getNewHighScoreTable(trophy, newHighScoreLabel)).padTop(AbstractScreen.PAD);
            table.row();
        }
        if (showNextLevelLabel) {
            table.row();
            table.add(nextLevelLabel).padTop(padTop);

            // Events
            nextLevelLabel.addListener(UIFactory.screenNavigationListener(ScreenEnum.PLAY_GAME, nextLevel, currentLives, finalScore));
        } else {
            table.row();
            table.add(grandFinaleLabel).padTop(padTop);

            // Events
            grandFinaleLabel.addListener(UIFactory.screenNavigationListener(ScreenEnum.GRAND_FINALE));
        }
        table.padTop(padTop);

        // Adds table to stage
        addActor(table);
    }

    private void defineNavigationTable() {
        // UI assets
        AssetScene2d assetScene2d = Assets.getInstance().getScene2d();

        // Set table structure
        Table table = new Table();

        // Debug lines
        table.setDebug(DebugConstants.DEBUG_LINES);

        // Bottom-Align table
        table.bottom();

        // Make the table fill the entire stage
        table.setFillParent(true);

        // Define images
        ImageButton back = new ImageButton(new TextureRegionDrawable(assetScene2d.getBack()),
                new TextureRegionDrawable(assetScene2d.getBackPressed()));

        // Add values
        table.add(back).padBottom(AbstractScreen.PAD * 2);

        // Events
        back.addListener(UIFactory.screenNavigationListener(ScreenEnum.MAIN_MENU));

        // Adds table to stage
        addActor(table);
    }

    private Table getNewHighScoreTable(TextureRegion trophy, Label highScoreLabel) {
        Image image = new Image();
        image.setDrawable(new TextureRegionDrawable(trophy));
        image.setScaling(Scaling.fit);

        Table table = new Table();
        table.setDebug(DebugConstants.DEBUG_LINES);
        table.add(image);
        table.add(highScoreLabel).padLeft(AbstractScreen.PAD / 2);

        return table;
    }

    private int getStarsValue(int currentEndurance, int maxEndurance) {
        int stars = 0;

        if (currentEndurance >= maxEndurance) {
            stars = 3;
        } else {
            if (currentEndurance <= MathUtils.ceil((float)(maxEndurance - 1) / 2)) {
                stars = 1;
            } else {
                stars = 2;
            }
        }
        return stars;
    }

    private Table getStarsTable(TextureRegion star, TextureRegion emptyStar, int stars) {
        Table table = new Table();
        table.setDebug(DebugConstants.DEBUG_LINES);
        Image image;
        for (int i = 1; i <= 3; i++) {
            image = new Image();
            image.setDrawable(new TextureRegionDrawable(i <= stars ? star : emptyStar));
            image.setScaling(Scaling.fit);
            table.add(image);
        }
        return table;
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
