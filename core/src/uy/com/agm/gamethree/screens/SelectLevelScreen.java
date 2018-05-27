package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.game.GameSettings;
import uy.com.agm.gamethree.game.LevelState;
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

        // Define our labels based on labelStyle
        Label selectLevelLabel = new Label(i18NGameThreeBundle.format("selectLevel.title"), labelStyleBig);

        // Add values
        table.add(selectLevelLabel);
        table.row();
        Label levelLabel;
        for (LevelState levelState : GameSettings.getInstance().getLevels().values()) {
            table.row();
            levelLabel = new Label(i18NGameThreeBundle.format("selectLevel.playLevel", levelState.getLevel()), labelStyleNormal);
            table.add(levelLabel).padTop(AbstractScreen.PAD);

            // Events
            levelLabel.addListener(UIFactory.createListener(ScreenEnum.PLAY_GAME, levelState.getLevel(), levelState.getLives(), levelState.getScore(), levelState.getSkulls()));
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
