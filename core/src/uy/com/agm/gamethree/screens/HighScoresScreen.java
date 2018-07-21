package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Scaling;

import java.text.DateFormat;
import java.util.Locale;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.scene2d.AssetScene2d;
import uy.com.agm.gamethree.game.DebugConstants;
import uy.com.agm.gamethree.game.GameSettings;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.UIFactory;
import uy.com.agm.gamethree.widget.TypingLabelWorkaround;

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
        table.top().padTop(AbstractScreen.PAD * 4);

        // Make the table fill the entire stage
        table.setFillParent(true);

        // Personal fonts
        Label.LabelStyle labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.getInstance().getFonts().getDefaultBig();

        Label.LabelStyle labelStyleSmall = new Label.LabelStyle();
        labelStyleSmall.font = Assets.getInstance().getFonts().getDefaultSmall();

        // Define our labels based on labelStyle
        int style = DateFormat.MEDIUM;
        DateFormat df = DateFormat.getDateInstance(style, Locale.getDefault());
        Label highScoresLabel = new Label(i18NGameThreeBundle.format("highScores.title"), labelStyleBig);
        TypingLabelWorkaround goldHighScoreLabel = new TypingLabelWorkaround(i18NGameThreeBundle.format("highScores.highScore", prefs.getGoldHighScore(), df.format(prefs.getGoldHighScoreDate())), labelStyleSmall);
        TypingLabelWorkaround silverHighScoreLabel = new TypingLabelWorkaround(i18NGameThreeBundle.format("highScores.highScore", prefs.getSilverHighScore(), df.format(prefs.getSilverHighScoreDate())), labelStyleSmall);
        TypingLabelWorkaround bronzeHighScoreLabel = new TypingLabelWorkaround(i18NGameThreeBundle.format("highScores.highScore", prefs.getBronzeHighScore(), df.format(prefs.getBronzeHighScoreDate())), labelStyleSmall);

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
        table.add(highScoresLabel).padBottom(AbstractScreen.PAD * 2).colspan(COLUMNS);
        table.row();
        table.add(goldTrophyImage).padTop(AbstractScreen.PAD);
        table.add(goldHighScoreLabel).padLeft(AbstractScreen.PAD / 2).padTop(AbstractScreen.PAD);
        table.row();
        table.add(silverTrophyImage).padTop(AbstractScreen.PAD);
        table.add(silverHighScoreLabel).padLeft(AbstractScreen.PAD / 2).padTop(AbstractScreen.PAD);
        table.row();
        table.add(bronzeTrophyImage).padTop(AbstractScreen.PAD);
        table.add(bronzeHighScoreLabel).padLeft(AbstractScreen.PAD / 2).padTop(AbstractScreen.PAD);

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
