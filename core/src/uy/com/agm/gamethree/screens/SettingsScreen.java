package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.scene2d.AssetScene2d;
import uy.com.agm.gamethree.game.GameSettings;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.UIFactory;
import uy.com.agm.gamethree.tools.AudioManager;

/**
 * Created by AGM on 1/18/2018.
 */

public class SettingsScreen extends AbstractScreen {
    private static final String TAG = SettingsScreen.class.getName();

    // Constants
    private static final float SLIDER_MIN = 0.0f;
    private static final float SLIDER_MAX = 1.0f;
    private static final float SLIDER_STEP = 0.01f;
    private static final float SLIDER_WIDTH = 250.0f;

    private Label shootingLabel;
    private ImageButton music;
    private ImageButton sound;
    private ImageButton shooting;
    private Slider sliderMusic;
    private Slider sliderSound;
    private String manualShootingText;
    private String automaticShootingText;
    private GameSettings prefs;
    private I18NBundle i18NGameThreeBundle;

    public SettingsScreen() {
        super();
        prefs = GameSettings.getInstance();

        // I18n
        i18NGameThreeBundle = Assets.getInstance().getI18NGameThree().getI18NGameThreeBundle();
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

        // Slider style
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = new TextureRegionDrawable(assetScene2d.getSliderBackground());
        sliderStyle.knob = new TextureRegionDrawable(assetScene2d.getSliderKnob());

        // Button music
        Label settingsLabel = new Label(i18NGameThreeBundle.format("settings.title"), labelStyleBig);
        music = new ImageButton(new TextureRegionDrawable(assetScene2d.getMusic()),
                new TextureRegionDrawable(assetScene2d.getMusicPressed()), new TextureRegionDrawable(assetScene2d.getQuit())); // todo

        // Slide music
        sliderMusic = new Slider(SLIDER_MIN, SLIDER_MAX, SLIDER_STEP, false, sliderStyle);
        float value = prefs.getVolMusic();
        music.setChecked(value <= 0.0f ? true : false);
        sliderMusic.setValue(value);

        // Button sound
        sound = new ImageButton(new TextureRegionDrawable(assetScene2d.getSound()),
                new TextureRegionDrawable(assetScene2d.getSoundPressed()), new TextureRegionDrawable(assetScene2d.getCredits())); // todo

        // Slide sound
        sliderSound = new Slider(SLIDER_MIN, SLIDER_MAX, SLIDER_STEP, false, sliderStyle);
        value = prefs.getVolSound();
        sound.setChecked(value <= 0.0f ? true : false);
        sliderSound.setValue(value);

        // Button shooting
        shooting = new ImageButton(new TextureRegionDrawable(assetScene2d.getShooting()),
                new TextureRegionDrawable(assetScene2d.getShootingPressed()), new TextureRegionDrawable(assetScene2d.getResume())); // todo

        // Label shooting
        shootingLabel = new Label("SHOOTING", labelStyleNormal);
        shootingLabel.setAlignment(Align.center);
        automaticShootingText = i18NGameThreeBundle.format("settings.automaticShooting");
        manualShootingText = i18NGameThreeBundle.format("settings.manualShooting");
        shootingLabel.setText(prefs.isManualShooting() ? manualShootingText : automaticShootingText);
        shooting.setChecked(!prefs.isManualShooting());

        // Add values
        table.add(settingsLabel);
        table.row();
        table.add(music).height(music.getHeight()).padTop(AbstractScreen.PAD);
        table.row();
        table.add(sliderMusic).width(SLIDER_WIDTH);
        table.row();
        table.add(sound).height(sound.getHeight()).padTop(AbstractScreen.PAD);
        table.row();
        table.add(sliderSound).width(SLIDER_WIDTH);
        table.row();
        table.add(shooting).height(shooting.getHeight()).padTop(AbstractScreen.PAD);;
        table.row();
        table.add(shootingLabel);

        // Events
        music.addListener(
                new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());
                        toggleMusic();
                        save();
                    }

                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });

        sliderMusic.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                changeSliderMusic();
            }
        });

        sliderMusic.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                save();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        // Events
        sound.addListener(
                new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());
                        toggleSound();
                        save();
                    }

                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });

        sliderSound.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                changeSliderSound();
                playSampleSound();
            }
        });

        sliderSound.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                save();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        shooting.addListener(
                new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());
                        toggleShooting();
                        save();
                    }

                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });

        shootingLabel.addListener(
                new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());
                        shootingLabel.getStyle().fontColor = Color.WHITE; // Default
                        toggleShootingLabel();
                        save();
                    }

                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        shootingLabel.getStyle().fontColor = COLOR_LABEL_PRESSED;
                        return true;
                    }
                });

        // Adds table to stage
        addActor(table);
    }

    private void defineNavigationTable() {
        // UI assets
        AssetScene2d assetScene2d = Assets.getInstance().getScene2d();

        // Set table structure
        Table table = new Table();

        // Debug lines
        table.setDebug(PlayScreen.DEBUG_MODE);

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
        back.addListener(UIFactory.createListener(ScreenEnum.MAIN_MENU));

        // Adds table to stage
        addActor(table);
    }

    private void playSampleSound() {
        // Audio FX
        switch (MathUtils.random(1, 4)) {
            case 1:
                AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getPickUpPowerOne());
                break;
            case 2:
                AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getHeroShoot());
                break;
            case 3:
                AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getEnemyShoot());
                break;
            case 4:
                AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getHit());
                break;
        }
    }

    private void toggleMusic() {
        sliderMusic.setValue(music.isChecked() ? 0 : GameSettings.DEFAULT_VOLUME);
    }

    private void toggleSound() {
        sliderSound.setValue(sound.isChecked() ? 0 : GameSettings.DEFAULT_VOLUME);
    }

    private void changeSliderMusic() {
        float value = sliderMusic.getValue();//
        boolean musicOn = value <= 0.0f ? false : true;//
        prefs.setVolMusic(value);
        prefs.setMusic(musicOn);
        music.setChecked(!musicOn);
        AudioManager.getInstance().onSettingsUpdated();
    }

    private void changeSliderSound() {
        float value = sliderSound.getValue();
        boolean soundOn = value <= 0.0f ? false : true;
        prefs.setVolSound(value);
        prefs.setSound(soundOn);
        sound.setChecked(!soundOn);
        AudioManager.getInstance().onSettingsUpdated();
    }

    private void toggleShooting() {
        boolean automaticShooting = shooting.isChecked();
        prefs.setManualShooting(!automaticShooting);
        shootingLabel.setText(automaticShooting ? automaticShootingText : manualShootingText);
    }

    private void toggleShootingLabel() {
        prefs.setManualShooting(!prefs.isManualShooting());
        shootingLabel.setText(prefs.isManualShooting() ? manualShootingText : automaticShootingText);
        shooting.setChecked(!prefs.isManualShooting());
    }

    private void save() {
        prefs.save();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
