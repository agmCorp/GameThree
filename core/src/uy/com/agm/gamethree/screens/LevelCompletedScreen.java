package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.game.Constants;
import uy.com.agm.gamethree.game.GameSettings;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.UIFactory;

/**
 * Created by AGM on 12/23/2017.
 */

public class LevelCompletedScreen extends AbstractScreen {
    private static final String TAG = LevelCompletedScreen.class.getName();
    private int currentLevel;
    private int nextLevel;

    public LevelCompletedScreen(Integer currentLevel) {
        super();
        this.currentLevel = currentLevel;
        this.nextLevel = currentLevel + 1;
        GameSettings.getInstance().addAvailableLevel(nextLevel);
        GameSettings.getInstance().save();
    }

    @Override
    public void buildStage() {
        // Personal fonts
        Label.LabelStyle labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.instance.fonts.defaultBig;

        Label.LabelStyle labelStyleNormal = new Label.LabelStyle();
        labelStyleNormal.font = Assets.instance.fonts.defaultNormal;

        // Define our labels based on labelStyle
        Label titleLabel1 = new Label("Level " + this.currentLevel, labelStyleBig);
        Label titleLabel2 = new Label("Completed!!", labelStyleBig);
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
        table.add(playAgainLabel).padTop(Constants.PAD_TOP).center();
        table.row();
        table.add(nextLevelLabel).padTop(Constants.PAD_TOP).center();
        table.row();
        table.add(backLabel).padTop(Constants.PAD_TOP).center();

        // Events
        playAgainLabel.addListener(UIFactory.createListener(ScreenEnum.GAME, this.currentLevel));
        nextLevelLabel.addListener(UIFactory.createListener(ScreenEnum.GAME, this.nextLevel));
        backLabel.addListener(UIFactory.createListener(ScreenEnum.MAIN_MENU));

        // Adds created table to stage
        addActor(table);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
