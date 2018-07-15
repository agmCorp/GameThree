package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
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
    private int currentGrace;
    private int currentPenalties;
    private int finalScore;
    private int currentStars;
    private boolean showNewHighScoreLabel;
    private boolean showNextLevelLabel;

    public LevelCompletedScreen(Integer currentLevel, Integer currentLives, Integer currentScore, Integer currentGrace) {
        super();
        this.currentLevel = currentLevel;
        this.nextLevel = currentLevel + 1;
        this.currentLives = currentLives;
        this.currentScore = currentScore;
        this.currentGrace = currentGrace;
        int maxGrace = LevelFactory.getLevelGrace(currentLevel);
        this.currentPenalties = maxGrace - currentGrace;
        this.finalScore = Math.abs(currentScore - currentPenalties * PENALTY_COST);
        this.currentStars = getStars(currentGrace, maxGrace);

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

    private int getStars(int currentGrace, int maxGrace) {
        int stars = 0;

        if (currentGrace == maxGrace) {
            stars = 3;
        } else {
            if (currentGrace <= MathUtils.ceil((float)(maxGrace - 1) / 2)) {
                stars = 1;
            } else {
                stars = 2;
            }
        }
        return stars;
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
        Label finalStarsLabel = new Label("ESTRELLAS " + currentStars, labelStyleNormal); // todo provisorio
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
        table.add(finalStarsLabel).padTop(AbstractScreen.PAD); // todo provisorio
        table.row();
        if (showNewHighScoreLabel) {
            table.add(newHighScoreLabel).padTop(AbstractScreen.PAD);
            table.row();
        }
        if (showNextLevelLabel) {
            table.row();
            table.add(nextLevelLabel).padTop(AbstractScreen.PAD * 2);

            // Events
            nextLevelLabel.addListener(UIFactory.screenNavigationListener(ScreenEnum.PLAY_GAME, nextLevel, currentLives, finalScore));
        } else {
            table.row();
            table.add(newLevelsLabel).padTop(AbstractScreen.PAD * 2);
        }

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

    @Override
    public void dispose() {
        super.dispose();
    }
}
