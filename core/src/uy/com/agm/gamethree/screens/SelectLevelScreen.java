package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.scene2d.AssetScene2d;
import uy.com.agm.gamethree.game.DebugConstants;
import uy.com.agm.gamethree.game.GameSettings;
import uy.com.agm.gamethree.game.LevelState;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.UIFactory;

/**
 * Created by AGM on 12/23/2017.
 */

public class SelectLevelScreen extends AbstractScreen {
    private static final String TAG = SelectLevelScreen.class.getName();

    // Constants
    public static final float SCROLL_PANE_MAX_HEIGHT = 300.0f;

    public SelectLevelScreen() {
        super();
    }

    @Override
    public void buildStage() {
        defineMainTable();
        defineNavigationTable();
    }

    private void defineMainTable() {
        // I18n
        I18NBundle i18NGameThreeBundle = Assets.getInstance().getI18NGameThree().getI18NGameThreeBundle();

        // Set table structure
        Table container = new Table();

        // Design
        container.setBackground(new TextureRegionDrawable(Assets.getInstance().getScene2d().getTable()));

        // Debug lines
        container.setDebug(DebugConstants.DEBUG_LINES);

        // Center-Align table
        container.center().padBottom(AbstractScreen.PAD * 2);

        // Make the table fill the entire stage
        container.setFillParent(true);

        // Personal fonts
        Label.LabelStyle labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.getInstance().getFonts().getDefaultBig();

        // Define our labels based on labelStyle
        Label selectLevelLabel = new Label(i18NGameThreeBundle.format("selectLevel.title"), labelStyleBig);

        // Add values
        container.add(selectLevelLabel);
        container.row();
        ScrollPane scrollPane = new ScrollPane(defineLevelsTable());
        container.add(scrollPane).top().width(AbstractScreen.V_WIDTH).maxHeight(SCROLL_PANE_MAX_HEIGHT);

        // Adds table to stage
        addActor(container);
    }

    private Table defineLevelsTable() {
        // I18n
        I18NBundle i18NGameThreeBundle = Assets.getInstance().getI18NGameThree().getI18NGameThreeBundle();

        // Set table structure
        Table table = new Table();

        // Debug lines
        table.setDebug(DebugConstants.DEBUG_LINES);

        // Personal fonts
        Label.LabelStyle labelStyleNormal = new Label.LabelStyle();
        labelStyleNormal.font = Assets.getInstance().getFonts().getDefaultNormal();

        // Add values
        int level;
        Label levelLabel;
        for (LevelState levelState : GameSettings.getInstance().getLevels().values()) {
            table.row();
            level = levelState.getLevel();
            levelLabel = new Label(i18NGameThreeBundle.format("selectLevel.playLevel", level, levelState.getFinalStars()), labelStyleNormal); // todo ACA VAN IMAGENES
            table.add(levelLabel).padTop(AbstractScreen.PAD);

            // Events
            levelLabel.addListener(UIFactory.screenNavigationListener(ScreenEnum.PLAY_GAME, level,
                    levelState.getInitialLives(),
                    levelState.getInitialScore()));
        }

        return table;
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
