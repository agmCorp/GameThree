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

public class LevelSelectScreen extends AbstractScreen {
    private static final String TAG = LevelSelectScreen.class.getName();

    public LevelSelectScreen() {
        super();
        GameSettings.instance.load();
    }

    @Override
    public void buildStage() {
        // Personal fonts
        Label.LabelStyle labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.instance.fonts.defaultBig;

        Label.LabelStyle labelStyleNormal = new Label.LabelStyle();
        labelStyleNormal.font = Assets.instance.fonts.defaultNormal;

        // Define our labels based on labelStyle
        Label titleLabel = new Label("Select level", labelStyleBig);
        Label backLabel = new Label("Back to menu", labelStyleNormal);

        // Set table structure
        Table table = new Table();
        table.center();
        table.setFillParent(true);
        table.add(titleLabel).center();

        Label levelLabel;
        for (int level : GameSettings.instance.availableLevels) {
            // Define our labels based on labelStyle
            table.row();
            levelLabel = new Label("Play level " + level, labelStyleNormal);
            table.add(levelLabel).padTop(Constants.PAD_TOP).center();

            // Events
            levelLabel.addListener(UIFactory.createListener(ScreenEnum.GAME, level));
        }

        table.row();
        table.add(backLabel).padTop(Constants.PAD_TOP).center();

        // Events
        backLabel.addListener(UIFactory.createListener(ScreenEnum.MAIN_MENU));

        // Adds created table to stage
        addActor(table);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
