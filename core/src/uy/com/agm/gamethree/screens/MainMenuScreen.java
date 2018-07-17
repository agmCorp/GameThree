package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.scene2d.AssetScene2d;
import uy.com.agm.gamethree.game.DebugConstants;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.UIFactory;
import uy.com.agm.gamethree.tools.AudioManager;

/**
 * Created by AGM on 1/18/2018.
 */

public class MainMenuScreen extends AbstractScreen {
    private static final String TAG = MainMenuScreen.class.getName();

    // Constants
    private static final int COLUMNS = 4;
    private static final float BUTTON_WIDTH = 400 / COLUMNS;

    public MainMenuScreen() {
        super();

        // Play menu music
        AudioManager.getInstance().playMusic(Assets.getInstance().getMusic().getSongMainMenu());
    }

    @Override
    public void buildStage() {
        defineMainTable();
        defineNavigationTable();
    }

    private void defineMainTable() {
        // UI assets
        AssetScene2d assetScene2d = Assets.getInstance().getScene2d();

        // Set table structure
        Table table = new Table();

        // Design
        table.setBackground(new TextureRegionDrawable(assetScene2d.getTable()));

        // Debug lines
        table.setDebug(DebugConstants.DEBUG_LINES);

        // Top-Align table
        table.top();

        // Make the table fill the entire stage
        table.setFillParent(true);

        // Define buttons and images
        Image wipeThemOut = new Image(assetScene2d.getWipeThemOut());
        ImageButton play = new ImageButton(new TextureRegionDrawable(assetScene2d.getPlay()),
                new TextureRegionDrawable(assetScene2d.getPlayPressed()));
        ImageButton settings = new ImageButton(new TextureRegionDrawable(assetScene2d.getSettings()),
                new TextureRegionDrawable(assetScene2d.getSettingsPressed()));
        ImageButton highScores = new ImageButton(new TextureRegionDrawable(assetScene2d.getHighScores()),
                new TextureRegionDrawable(assetScene2d.getHighScoresPressed()));
        ImageButton help = new ImageButton(new TextureRegionDrawable(assetScene2d.getHelp()),
                new TextureRegionDrawable(assetScene2d.getHelpPressed()));
        ImageButton credits = new ImageButton(new TextureRegionDrawable(assetScene2d.getCredits()),
                new TextureRegionDrawable(assetScene2d.getCreditsPressed()));

        // Add values
        table.add(wipeThemOut).colspan(COLUMNS).padTop(AbstractScreen.PAD * 2);
        table.row();
        table.add(play).colspan(COLUMNS).height(play.getHeight());
        table.row().height(settings.getHeight()).padTop(AbstractScreen.PAD);
        table.add(settings).width(BUTTON_WIDTH);
        table.add(highScores).width(BUTTON_WIDTH);
        table.add(help).width(BUTTON_WIDTH);
        table.add(credits).width(BUTTON_WIDTH);

        // Events
        play.addListener(UIFactory.screenNavigationListener(ScreenEnum.SELECT_LEVEL));
        settings.addListener(UIFactory.screenNavigationListener(ScreenEnum.SETTINGS));
        highScores.addListener(UIFactory.screenNavigationListener(ScreenEnum.HIGH_SCORES));
        credits.addListener(UIFactory.screenNavigationListener(ScreenEnum.CREDITS));

        // Adds table to stage
        addActor(table);
    }

    private void defineNavigationTable() {
        // I18n
        I18NBundle i18NGameThreeBundle = Assets.getInstance().getI18NGameThree().getI18NGameThreeBundle();

        // Set table structure
        Table table = new Table();

        // Debug lines
        table.setDebug(DebugConstants.DEBUG_LINES);

        // Bottom-Align table
        table.bottom();

        // Make the table fill the entire stage
        table.setFillParent(true);

        // Personal fonts
        Label.LabelStyle labelStyleNormal = new Label.LabelStyle();
        labelStyleNormal.font = Assets.getInstance().getFonts().getDefaultNormal();

        // Define our labels based on labelStyle
        final Label exitGameLabel = new Label(i18NGameThreeBundle.format("mainMenu.exitGame"), labelStyleNormal);

        // Add values
        table.add(exitGameLabel).padBottom(AbstractScreen.PAD * 2);

        exitGameLabel.addListener(
                new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());
                        exitGameLabel.setColor(Color.WHITE); // Default
                        Gdx.app.exit();
                    }

                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        exitGameLabel.setColor(COLOR_LABEL_PRESSED);
                        return true;
                    }
                });

        // Adds table to stage
        addActor(table);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}