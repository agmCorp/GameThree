package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

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
        GameSettings.getInstance().addAvailableLevel(nextLevel);
        GameSettings.getInstance().save();
    }

    @Override
    public void buildStage() {
        // Personal fonts
        Label.LabelStyle labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.getInstance().getFonts().getDefaultBig();

        Label.LabelStyle labelStyleNormal = new Label.LabelStyle();
        labelStyleNormal.font = Assets.getInstance().getFonts().getDefaultNormal();

        // Define our labels based on labelStyle
        Label titleLabel1 = new Label("Level " + this.currentLevel, labelStyleBig);
        Label titleLabel2 = new Label("Completed!!", labelStyleBig);
        Label scoreLabel = new Label("Score: " + finalScore, labelStyleNormal);
        Label playAgainLabel = new Label("Touch to play again", labelStyleNormal);
        Label nextLevelLabel = new Label("Next level", labelStyleNormal);
        Label backLabel = new Label("Back to menu", labelStyleNormal);

        // Set table structure
        Table table = new Table();
        table.center();
        table.setFillParent(true);
        table.add(titleLabel1).center();
        table.row();
        table.add(titleLabel2).padTop(Constants.PAD_TOP).center();
        table.row();
        table.add(scoreLabel).padTop(Constants.PAD_TOP).center();
        table.row();
        table.add(playAgainLabel).padTop(Constants.PAD_TOP).center();
        table.row();
        table.add(nextLevelLabel).padTop(Constants.PAD_TOP).center();
        table.row();
        table.add(backLabel).padTop(Constants.PAD_TOP).center();

        // Debug lines
        table.setDebug(Constants.DEBUG_MODE);

        // Events
        playAgainLabel.addListener(UIFactory.createListener(ScreenEnum.GAME, this.currentLevel));
        nextLevelLabel.addListener(UIFactory.createListener(ScreenEnum.GAME, this.nextLevel));
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
