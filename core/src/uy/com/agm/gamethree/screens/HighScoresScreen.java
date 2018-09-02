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
import uy.com.agm.gamethree.game.HighScore;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.ScreenTransitionEnum;
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

        // Date format
        int style = DateFormat.MEDIUM;
        DateFormat df = DateFormat.getDateInstance(style, Locale.getDefault());

        // Personal fonts
        Label.LabelStyle labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.getInstance().getFonts().getDefaultBig();

        Label.LabelStyle labelStyleSmall = new Label.LabelStyle();
        labelStyleSmall.font = Assets.getInstance().getFonts().getDefaultSmall();

        // Define labels based on labelStyle
        Label highScoresLabel = new Label(i18NGameThreeBundle.format("highScores.title"), labelStyleBig);

        // Add values
        table.add(highScoresLabel).colspan(COLUMNS);

        // Add scores
        TypingLabelWorkaround highScoreLabel;
        Image highScoreImage;
        for (HighScore highScore : prefs.getHighScores()) {
            highScoreImage = new Image();
            highScoreImage.setDrawable(new TextureRegionDrawable(Assets.getInstance().getScene2d().getRankingImage(highScore.getRanking())));
            highScoreImage.setScaling(Scaling.fit);
            highScoreLabel = new TypingLabelWorkaround(i18NGameThreeBundle.format("highScores.highScore",
                    highScore.getScore(), df.format(highScore.getDate())), labelStyleSmall);
            table.row();
            table.add(highScoreImage).padTop(AbstractScreen.PAD);
            table.add(highScoreLabel).padLeft(AbstractScreen.PAD / 2).padTop(AbstractScreen.PAD);
        }

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
}
