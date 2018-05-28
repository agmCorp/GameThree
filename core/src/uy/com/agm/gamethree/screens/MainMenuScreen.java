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
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.UIFactory;
import uy.com.agm.gamethree.tools.AudioManager;

/**
 * Created by AGM on 1/18/2018.
 */

public class MainMenuScreen extends AbstractScreen {
    private static final String TAG = MainMenuScreen.class.getName();

    public MainMenuScreen() {
        super();

        // Play menu music
        AudioManager.getInstance().playMusic(Assets.getInstance().getMusic().getSongMainMenu());
    }

    @Override
    public void buildStage() {
        AssetScene2d assetScene2d = Assets.getInstance().getScene2d();

        // I18n
        I18NBundle i18NGameThreeBundle = Assets.getInstance().getI18NGameThree().getI18NGameThreeBundle();

        // Set table structure
        Table table = new Table();

        // Design
        table.setBackground(new TextureRegionDrawable(assetScene2d.getTable()));

        // Debug lines
        table.setDebug(PlayScreen.DEBUG_MODE);

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
        ImageButton credits = new ImageButton(new TextureRegionDrawable(assetScene2d.getCredits()),
                new TextureRegionDrawable(assetScene2d.getCreditsPressed()));

        // Add values
        table.add(wipeThemOut).colspan(3).padTop(AbstractScreen.PAD * 2);
        table.row();
        table.add(play).colspan(3).height(play.getHeight());
        table.row().height(settings.getHeight()).padTop(AbstractScreen.PAD);
        table.add(settings).width(V_WIDTH / 3);
        table.add(help).width(V_WIDTH / 3);
        table.add(credits).width(V_WIDTH / 3);

        // Set table structure
        Table navigation = new Table();

        // Debug lines
        navigation.setDebug(PlayScreen.DEBUG_MODE);

        // Bottom-Align table
        navigation.bottom();

        // Make the table fill the entire stage
        navigation.setFillParent(true);

        // Personal fonts
        Label.LabelStyle labelStyleNormal = new Label.LabelStyle();
        labelStyleNormal.font = Assets.getInstance().getFonts().getDefaultNormal();

        // Define our labels based on labelStyle
        Label exitGameLabel = new Label(i18NGameThreeBundle.format("mainMenu.exitGame"), labelStyleNormal);

        // Add values
        navigation.add(exitGameLabel).padBottom(AbstractScreen.PAD * 2);

        // Events
        play.addListener(UIFactory.createListener(ScreenEnum.SELECT_LEVEL));
        settings.addListener(UIFactory.createListener(ScreenEnum.SETTINGS));
        exitGameLabel.addListener(
                new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());
                        Gdx.app.exit();
                        return false;
                    }
                });

        // Adds created tables to stage
        addActor(table);
        addActor(navigation);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}