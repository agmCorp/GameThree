package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.Gdx;
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
import uy.com.agm.gamethree.game.GameSettings;
import uy.com.agm.gamethree.playservices.IPlayServices;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.ScreenManager;
import uy.com.agm.gamethree.screens.util.ScreenTransitionEnum;
import uy.com.agm.gamethree.screens.util.UIFactory;
import uy.com.agm.gamethree.tools.AudioManager;

/**
 * Created by AGM on 1/18/2018.
 */

public class MainMenuScreen extends AbstractScreen {
    private static final String TAG = MainMenuScreen.class.getName();

    // Constants
    private static final int COLUMNS = 3;
    private static final float BUTTON_WIDTH = 300 / COLUMNS;

    private I18NBundle i18NGameThreeBundle;
    private GameSettings prefs;
    protected IPlayServices playServices;

    public MainMenuScreen() {
        super();

        i18NGameThreeBundle = Assets.getInstance().getI18NGameThree().getI18NGameThreeBundle();
        prefs = GameSettings.getInstance();

        // Google play game services
        playServices = ScreenManager.getInstance().getGame().getPlayServices();

        // Play menu music
        AudioManager.getInstance().playMusic(Assets.getInstance().getMusic().getSongMainMenu(), true);
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
        ImageButton help = new ImageButton(new TextureRegionDrawable(assetScene2d.getHelp()),
                new TextureRegionDrawable(assetScene2d.getHelpPressed()));
        final ImageButton rateGame = new ImageButton(new TextureRegionDrawable(assetScene2d.getRateGame()),
                new TextureRegionDrawable(assetScene2d.getRateGamePressed()));
        ImageButton showLeaderboards = new ImageButton(new TextureRegionDrawable(assetScene2d.getShowLeaderboards()),
                new TextureRegionDrawable(assetScene2d.getShowLeaderboardsPressed()));
        ImageButton highScores = new ImageButton(new TextureRegionDrawable(assetScene2d.getHighScores()),
                new TextureRegionDrawable(assetScene2d.getHighScoresPressed()));
        ImageButton credits = new ImageButton(new TextureRegionDrawable(assetScene2d.getCredits()),
                new TextureRegionDrawable(assetScene2d.getCreditsPressed()));

        // Add values
        table.add(wipeThemOut).colspan(COLUMNS).padTop(AbstractScreen.PAD * 5 / 3);
        table.row();
        table.add(play).colspan(COLUMNS).height(play.getHeight());
        table.row().height(settings.getHeight()).padTop(AbstractScreen.PAD / 2);
        table.add(settings).width(BUTTON_WIDTH);
        table.add(help).width(BUTTON_WIDTH);
        table.add(rateGame).width(BUTTON_WIDTH);
        table.row().height(settings.getHeight()).padTop(AbstractScreen.PAD / 2);
        table.add(showLeaderboards).width(BUTTON_WIDTH);
        table.add(highScores).width(BUTTON_WIDTH);
        table.add(credits).width(BUTTON_WIDTH);

        // Events
        play.addListener(UIFactory.screenNavigationListener(ScreenEnum.SELECT_LEVEL, ScreenTransitionEnum.SLIDE_LEFT_EXP));
        settings.addListener(UIFactory.screenNavigationListener(ScreenEnum.SETTINGS, ScreenTransitionEnum.SLIDE_LEFT_EXP));
        //help.addListener(UIFactory.screenNavigationListener(ScreenEnum.HELP_ONE, ScreenTransitionEnum.SLIDE_LEFT_EXP));
        help.addListener(UIFactory.screenNavigationListener(ScreenEnum.GRAND_FINALE, ScreenTransitionEnum.SLIDE_LEFT_EXP));
        rateGame.addListener(
                new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());
                        rateGame();
                    }

                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });
        showLeaderboards.addListener(
                new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());
                        showLeaderboards();
                    }

                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });
        highScores.addListener(UIFactory.screenNavigationListener(ScreenEnum.HIGH_SCORES, ScreenTransitionEnum.SLIDE_LEFT_EXP));
        credits.addListener(UIFactory.screenNavigationListener(ScreenEnum.CREDITS, ScreenTransitionEnum.SLIDE_LEFT_EXP));

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

        // Define labels based on labelStyle
        final Label exitGameLabel = new Label(i18NGameThreeBundle.format("mainMenu.exitGame"), labelStyleNormal);

        // Add values
        table.add(exitGameLabel).padBottom(AbstractScreen.PAD * 4 / 3);

        exitGameLabel.addListener(
                new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());
                        exitGameLabel.setColor(DEFAULT_COLOR);
                        signOut();
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
    public void goBack() {
        playClick();
        Gdx.app.exit();
    }

    private void showLeaderboards() {
        if (playServices.isWifiConnected()) {
            if (playServices.isSignedIn()) {
                showLeaderboardsImp();
            } else {
                playServices.signIn(new Runnable() {
                    @Override
                    public void run() {
                        showLeaderboards();
                    }
                }, null);
            }
        } else {
            playServices.showToast(i18NGameThreeBundle.format("mainMenu.wifiError"));
        }
    }

    private void showLeaderboardsImp() {
        playServices.submitScore(prefs.getHighScore());
        playServices.showLeaderboards();
    }

    private void rateGame() {
        playServices.rateGame();
    }

    private void signOut() {
        playServices.signOut();
    }
}