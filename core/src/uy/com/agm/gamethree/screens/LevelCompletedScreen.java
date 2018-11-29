package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Scaling;

import uy.com.agm.gamethree.actors.player.Hero;
import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.scene2d.AssetScene2d;
import uy.com.agm.gamethree.assets.scene2d.AssetStageCleared;
import uy.com.agm.gamethree.game.DebugConstants;
import uy.com.agm.gamethree.game.GameSettings;
import uy.com.agm.gamethree.game.LevelState;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.ScreenManager;
import uy.com.agm.gamethree.screens.util.ScreenTransitionEnum;
import uy.com.agm.gamethree.screens.util.UIFactory;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.widget.AnimatedImage;
import uy.com.agm.gamethree.widget.TypingLabelWorkaround;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by AGM on 12/23/2017.
 */

public class LevelCompletedScreen extends AbstractScreen {
    private static final String TAG = LevelCompletedScreen.class.getName();

    // Constants
    private static final int PENALTY_COST = 200;
    private static final float HAND_SCALE = 0.4f;
    private static final float HAND_X = 340.0f;
    private static final float HAND_Y = 70.0f;
    private static final float HAND_FADE_DURATION = 2.0f;
    private static final float HAND_MOVE_DURATION = 0.5f;
    private static final float HAND_MOVE_BY = 15.0f;

    private int currentLevel;
    private int nextLevel;
    private int currentScore;
    private int currentPenalties;
    private int finalScore;
    private int finalStars;
    private int gameScore;
    private int ranking;
    private boolean showNewHighScoreLabel;
    private boolean showNextLevelLabel;

    public LevelCompletedScreen(Integer currentLevel, Integer currentScore, Integer currentPenalties) {
        super();
        GameSettings prefs = GameSettings.getInstance();

        this.currentLevel = currentLevel;
        this.nextLevel = currentLevel + 1;
        this.currentScore = currentScore;
        this.currentPenalties = currentPenalties;
        this.finalScore = Math.max(this.currentScore - this.currentPenalties * PENALTY_COST, this.currentScore / 2); // At most we take away half of your level score. We are good people :)
        this.finalStars = getFinalStars(this.currentPenalties);
        prefs.setLevelStateInfo(this.currentLevel, this.finalScore, this.finalStars);
        this.gameScore = getGameScore();
        this.ranking = prefs.updateRanking(this.gameScore);
        this.showNewHighScoreLabel = this.ranking >= 0;
        this.showNextLevelLabel = this.nextLevel <= GameSettings.MAX_LEVEL;
        if (this.showNextLevelLabel) {
            prefs.addNextLevel(this.nextLevel);
        }

        // Saves preferences
        prefs.save();

        // Leaderboards (always submit score)
        ScreenManager.getInstance().getGame().getPlayServices().submitScore(this.gameScore);

        // Audio FX
        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getApplause());
    }

    @Override
    public void buildStage() {
        defineMainTable();
        defineNavigationTable();
        setHand();
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
        Label penaltiesLabel = new Label(i18NGameThreeBundle.format("levelCompleted.penalties", currentPenalties), labelStyleNormal);
        Label levelScoreLabel = new Label(i18NGameThreeBundle.format("levelCompleted.levelScore", finalScore), labelStyleNormal);
        TypingLabelWorkaround gameScoreLabel = new TypingLabelWorkaround(i18NGameThreeBundle.format("levelCompleted.gameScore", gameScore), labelStyleNormal);
        Label newHighScoreLabel = new Label(i18NGameThreeBundle.format("levelCompleted.newHighScore"), labelStyleNormal);
        TypingLabelWorkaround nextLevelLabel = new TypingLabelWorkaround(i18NGameThreeBundle.format("levelCompleted.nextLevel"), labelStyleNormal);
        TypingLabelWorkaround grandFinaleLabel = new TypingLabelWorkaround(i18NGameThreeBundle.format("levelCompleted.grandFinale"), labelStyleNormal);

        // Add values
        table.add(animatedImage).size(AssetStageCleared.WIDTH_PIXELS, AssetStageCleared.HEIGHT_PIXELS).padTop(AbstractScreen.PAD);
        table.row();
        table.add(penaltiesLabel).padTop(AbstractScreen.PAD);
        table.row();
        table.add(levelScoreLabel).padTop(AbstractScreen.PAD);
        table.row();
        table.add(gameScoreLabel).padTop(AbstractScreen.PAD);
        table.row();
        table.add(getStarsTable(assetScene2d.getStar(), assetScene2d.getEmptyStar(), finalStars)).padTop(AbstractScreen.PAD);
        table.row();
        float padTop = AbstractScreen.PAD * 2;
        if (showNewHighScoreLabel) {
            padTop = AbstractScreen.PAD;
            table.add(getNewHighScoreTable(assetScene2d.getRankingImage(ranking), newHighScoreLabel)).padTop(AbstractScreen.PAD);
            table.row();
        }
        if (showNextLevelLabel) {
            table.row();
            table.add(nextLevelLabel).padTop(padTop);

            // Events
            nextLevelLabel.addListener(UIFactory.screenNavigationListener(ScreenEnum.PLAY_GAME, ScreenTransitionEnum.SLIDE_DOWN, nextLevel));
        } else {
            table.row();
            table.add(grandFinaleLabel).padTop(padTop);

            // Events
            grandFinaleLabel.addListener(UIFactory.screenNavigationListener(ScreenEnum.GRAND_FINALE, ScreenTransitionEnum.SLICE_UP_DOWN_80));
        }
        table.padTop(padTop);

        // WA: Sometimes the ranking image doesn't turn up. It happens only the first time this screen is shown and only on release version.
        // It seems that scene2d can't assign a size in some situations so the ranking image gets size zero.
        table.pack();

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
        back.addListener(UIFactory.screenNavigationListener(ScreenEnum.MAIN_MENU, ScreenTransitionEnum.SLIDE_DOWN));

        // Adds table to stage
        addActor(table);
    }

    private void setHand() {
        Image hand = new Image(Assets.getInstance().getScene2d().getHand());
        hand.setScale(HAND_SCALE);

        // WA: actor.localToStageCoordinates(new Vector2(0,0)) didn't work
        hand.setY(HAND_Y);
        hand.setX(HAND_X);

        // Actions
        SequenceAction sequenceOne = sequence(fadeIn(HAND_FADE_DURATION), fadeOut(HAND_FADE_DURATION));
        SequenceAction sequenceTwo = sequence(moveBy(HAND_MOVE_BY, -HAND_MOVE_BY, HAND_MOVE_DURATION, Interpolation.smooth),
                moveBy(-HAND_MOVE_BY, HAND_MOVE_BY, HAND_MOVE_DURATION, Interpolation.smooth));
        hand.addAction(parallel(forever(sequenceOne), forever(sequenceTwo)));

        addActor(hand);
    }

    private Table getNewHighScoreTable(TextureRegion highScoreImage, Label highScoreLabel) {
        Image image = new Image();
        image.setDrawable(new TextureRegionDrawable(highScoreImage));
        image.setScaling(Scaling.fit);

        Table table = new Table();
        table.setDebug(DebugConstants.DEBUG_LINES);
        table.add(image);
        table.add(highScoreLabel).padLeft(AbstractScreen.PAD / 2);

        return table;
    }

    private int getGameScore() {
        int gameScore = 0;
        for(LevelState levelState : GameSettings.getInstance().getLevels()) {
            gameScore += levelState.getFinalScore();
        }
        return gameScore;
    }

    private int getFinalStars(int currentPenalties) {
        int averageLevelPenalty = Hero.LIVES_START * Hero.ENDURANCE_START / 2;
        int stars = 0;

        if (currentPenalties == 0) {
            stars = 3;
        } else {
            if (currentPenalties < averageLevelPenalty) {
                stars = 2;
            } else {
                stars = 1;
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
}
