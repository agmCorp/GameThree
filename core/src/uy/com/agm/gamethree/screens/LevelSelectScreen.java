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

    private Label.LabelStyle labelStyleBig;
    private Label.LabelStyle labelStyleNormal;

    public LevelSelectScreen() {
        super();
    }

    @Override
    public void buildStage() {
        // Personal fonts
        labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.getInstance().getFonts().getDefaultBig();

        labelStyleNormal = new Label.LabelStyle();
        labelStyleNormal.font = Assets.getInstance().getFonts().getDefaultNormal();

        // Define our labels based on labelStyle
        Label backLabel = new Label("Back to menu", labelStyleNormal);

        // Set table structure
        Table table = new Table();
        table.center();
        table.setFillParent(true);
        table.add(new Label("Select level", labelStyleBig)).center();

        Label levelLabel;
        for (int level : GameSettings.getInstance().getAvailableLevels()) {
            table.row();
            levelLabel = new Label("Play level " + level, labelStyleNormal);
            table.add(levelLabel).padTop(Constants.PAD_TOP).center();

            // Events
            levelLabel.addListener(UIFactory.createListener(ScreenEnum.GAME, level));
        }

        table.row();
        table.add(backLabel).padTop(Constants.PAD_TOP).center();

        // Debug lines
        table.setDebug(Constants.DEBUG_MODE);

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
