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
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

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
    private int currentEnergy;
    private int currentPenalties;
    private int finalScore;
    private int currentStars;
    private boolean showNewHighScoreLabel;
    private boolean showNextLevelLabel;

    public LevelCompletedScreen(Integer currentLevel, Integer currentLives, Integer currentScore, Integer currentEnergy) {
        super();
        this.currentLevel = currentLevel;
        this.nextLevel = currentLevel + 1;
        this.currentLives = currentLives;
        this.currentScore = currentScore;
        this.currentEnergy = currentEnergy;
        int maxEnergy = LevelFactory.getLevelEnergy(currentLevel);
        this.currentPenalties = maxEnergy - currentEnergy;
        this.finalScore = Math.abs(currentScore - currentPenalties * PENALTY_COST);
        this.currentStars = getStarsValue(currentEnergy, maxEnergy);

        GameSettings prefs = GameSettings.getInstance();
        prefs.setStars(currentLevel, currentStars);
        showNewHighScoreLabel = finalScore > prefs.getHighScore();
        showNextLevelLabel = this.nextLevel <= GameSettings.MAX_LEVEL;
        if (showNewHighScoreLabel) {
            prefs.setHighScore(finalScore);
        }
        if (showNextLevelLabel) {
            prefs.addActiveLevel(nextLevel, currentLives, finalScore);
        }
        prefs.save();

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
        AnimatedImage animatedImage = new AnimatedImage();
        animatedImage.setAlign(Align.center);
        animatedImage.setAnimation(assetScene2d.getStageCleared().getStageClearedAnimation());

        // Define our labels based on labelStyle
        Label currentScoreLabel = new Label(i18NGameThreeBundle.format("levelCompleted.currentScore", currentScore), labelStyleNormal);
        Label penaltiesLabel = new Label(i18NGameThreeBundle.format("levelCompleted.penalties", currentPenalties), labelStyleNormal);
        Label finalScoreLabel = new Label(i18NGameThreeBundle.format("levelCompleted.finalScore", finalScore), labelStyleNormal);
        Label newHighScoreLabel = new Label(i18NGameThreeBundle.format("levelCompleted.newHighScore"), labelStyleNormal);
        TypingLabel nextLevelLabel = new TypingLabel(i18NGameThreeBundle.format("levelCompleted.nextLevel"), labelStyleNormal);
        TypingLabel newLevelsLabel = new TypingLabel(i18NGameThreeBundle.format("levelCompleted.newLevels"), labelStyleNormal);

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
            table.add(getNewHighScoreTable(assetScene2d.getGoldTrophy(), newHighScoreLabel)).padTop(AbstractScreen.PAD);
            table.row();
        }
        if (showNextLevelLabel) {
            table.row();
            table.add(nextLevelLabel).padTop(padTop);

            // Events
            nextLevelLabel.addListener(UIFactory.screenNavigationListener(ScreenEnum.PLAY_GAME, nextLevel, currentLives, finalScore));
        } else {
            table.row();
            table.add(newLevelsLabel).padTop(padTop);
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

    private Table getNewHighScoreTable(TextureRegion goldTrophy, Label highScoreLabel) {
        Image image = new Image();
        image.setDrawable(new TextureRegionDrawable(goldTrophy));
        image.setScaling(Scaling.fit);

        Table table = new Table();
        table.setDebug(DebugConstants.DEBUG_LINES);
        table.add(image);
        table.add(highScoreLabel).padLeft(AbstractScreen.PAD / 2);

        return table;
    }

    private int getStarsValue(int currentEnergy, int maxEnergy) {
        int stars = 0;

        if (currentEnergy == maxEnergy) {
            stars = 3;
        } else {
            if (currentEnergy <= MathUtils.ceil((float)(maxEnergy - 1) / 2)) {
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
