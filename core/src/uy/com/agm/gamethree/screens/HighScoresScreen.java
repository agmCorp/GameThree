package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Scaling;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.scene2d.AssetScene2d;
import uy.com.agm.gamethree.game.DebugConstants;
import uy.com.agm.gamethree.game.GameSettings;
import uy.com.agm.gamethree.game.LevelState;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.UIFactory;
import uy.com.agm.gamethree.tools.LevelFactory;

/**
 * Created by AGM on 12/23/2017.
 */

public class HighScoresScreen extends AbstractScreen {
    private static final String TAG = HighScoresScreen.class.getName();

    // Constants
    private static final int COLUMNS = 2;

    private GameSettings prefs;

    public HighScoresScreen() {
        super();
        prefs = GameSettings.getInstance();
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
        table.setBackground(new TextureRegionDrawable(Assets.getInstance().getScene2d().getTable()));

        // Debug lines
        table.setDebug(DebugConstants.DEBUG_LINES);

        // Top-Align table
        table.top().padTop(AbstractScreen.PAD * 2);

        // Make the table fill the entire stage
        table.setFillParent(true);

        // Personal fonts
        Label.LabelStyle labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.getInstance().getFonts().getDefaultBig();

        Label.LabelStyle labelStyleNormal = new Label.LabelStyle();
        labelStyleNormal.font = Assets.getInstance().getFonts().getDefaultNormal();

        // Define our labels based on labelStyle
        Label highScoresLabel = new Label(i18NGameThreeBundle.format("highScores.title"), labelStyleBig);
        Label goldHighScoreLabel = new Label(i18NGameThreeBundle.format("highScores.highScore", prefs.getGoldHighScore(), prefs.getGoldHighScoreMillis()), labelStyleNormal);
        Label silverHighScoreLabel = new Label(i18NGameThreeBundle.format("highScores.highScore", prefs.getSilverHighScore(), prefs.getSilverHighScoreMillis()), labelStyleNormal);
        Label bronzeHighScoreLabel = new Label(i18NGameThreeBundle.format("highScores.highScore", prefs.getBronzeHighScore(), prefs.getBronzeHighScoreMillis()), labelStyleNormal);

        // Gold trophy image
        Image goldTrophyImage = new Image();
        goldTrophyImage.setDrawable(new TextureRegionDrawable(assetScene2d.getGoldTrophy()));
        goldTrophyImage.setScaling(Scaling.fit);

        // Silver trophy image
        Image silverTrophyImage = new Image();
        silverTrophyImage.setDrawable(new TextureRegionDrawable(assetScene2d.getSilverTrophy()));
        silverTrophyImage.setScaling(Scaling.fit);

        // Bronze trophy image
        Image bronzeTrophyImage = new Image();
        bronzeTrophyImage.setDrawable(new TextureRegionDrawable(assetScene2d.getBronzeTrophy()));
        bronzeTrophyImage.setScaling(Scaling.fit);

        // Add values
        table.add(highScoresLabel).colspan(COLUMNS);
        table.row();
        table.add(goldTrophyImage).padLeft(AbstractScreen.PAD / 2).padTop(AbstractScreen.PAD);
        table.add(goldHighScoreLabel);
        table.row();
        table.add(silverTrophyImage).padLeft(AbstractScreen.PAD / 2).padTop(AbstractScreen.PAD);
        table.add(silverHighScoreLabel);
        table.row();
        table.add(bronzeTrophyImage).padLeft(AbstractScreen.PAD / 2).padTop(AbstractScreen.PAD);
        table.add(bronzeHighScoreLabel);

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
