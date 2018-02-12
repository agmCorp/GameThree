package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.I18NBundle;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.game.Constants;
import uy.com.agm.gamethree.game.GameSettings;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.UIFactory;

/**
 * Created by AGM on 12/23/2017.
 */

public class SelectLevelScreen extends AbstractScreen {
    private static final String TAG = SelectLevelScreen.class.getName();

    public SelectLevelScreen() {
        super();
    }

    @Override
    public void buildStage() {
        // I18n
        I18NBundle i18NGameThreeBundle = Assets.getInstance().getI18NGameThree().getI18NGameThreeBundle();

        // Set table structure
        Table table = new Table();

        // Debug lines
        table.setDebug(Constants.DEBUG_MODE);

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
        Label selectLevelLabel = new Label(i18NGameThreeBundle.format("selectLevel.title"), labelStyleBig);
        Label backLabel = new Label(i18NGameThreeBundle.format("selectLevel.backToMenu"), labelStyleNormal);

        // Add values
        table.add(selectLevelLabel).center();
        table.row();
        Label levelLabel;
        for (int level : GameSettings.getInstance().getAvailableLevels()) {
            table.row();
            levelLabel = new Label(i18NGameThreeBundle.format("selectLevel.playLevel", level), labelStyleNormal);
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
