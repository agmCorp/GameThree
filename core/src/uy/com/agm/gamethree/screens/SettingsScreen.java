package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.game.Constants;
import uy.com.agm.gamethree.game.GameSettings;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.UIFactory;
import uy.com.agm.gamethree.tools.AudioManager;

/**
 * Created by AGM on 1/18/2018.
 */

public class SettingsScreen extends AbstractScreen {
    private static final String TAG = SettingsScreen.class.getName();

    private Label shootingSettingLabel;
    private Slider sliderMusic;
    private Slider sliderSound;
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
        // Set table structure
        Table table = new Table();

        // Design
        table.setBackground(new TextureRegionDrawable(Assets.getInstance().getScene2d().getTable()));

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
        Label settingsLabel = new Label(i18NGameThreeBundle.format("settings.title"), labelStyleBig);
        Label musicLabel = new Label(i18NGameThreeBundle.format("settings.music"), labelStyleNormal);
        Label soundEffectsLabel = new Label(i18NGameThreeBundle.format("settings.soundEffects"), labelStyleNormal);
        Label shootingLabel = new Label(i18NGameThreeBundle.format("settings.shooting"), labelStyleNormal);
        shootingSettingLabel = new Label("SHOOTING", labelStyleNormal);
        setTextLabelShooting();
        Label backLabel = new Label(i18NGameThreeBundle.format("settings.backToMenu"), labelStyleNormal);

        //Slider style
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = new TextureRegionDrawable(Assets.getInstance().getScene2d().getSliderBackground());
        sliderStyle.knob = new TextureRegionDrawable(Assets.getInstance().getScene2d().getSliderKnob());

        // Music
        sliderMusic = new Slider(Constants.SLIDER_MIN, Constants.SLIDER_MAX, Constants.SLIDER_STEP, false, sliderStyle);
        sliderMusic.setValue(prefs.getVolMusic());

        // Sound
        sliderSound = new Slider(Constants.SLIDER_MIN, Constants.SLIDER_MAX, Constants.SLIDER_STEP, false, sliderStyle);
        sliderSound.setValue(prefs.getVolSound());

        // Add values
        table.add(settingsLabel);
        table.row();
        table.add(musicLabel).padTop(Constants.PAD_TOP);
        table.row();
        table.add(sliderMusic).width(Constants.SLIDER_WIDTH).padTop(Constants.PAD_TOP);
        table.row();
        table.add(soundEffectsLabel).padTop(Constants.PAD_TOP);
        table.row();
        table.add(sliderSound).width(Constants.SLIDER_WIDTH).padTop(Constants.PAD_TOP);
        table.row();
        table.add(shootingLabel).padTop(Constants.PAD_TOP);
        table.row();
        table.add(shootingSettingLabel).padTop(Constants.PAD_TOP);
        table.row();
        table.add(backLabel).padTop(Constants.PAD_TOP * 2);

        // Events
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

        shootingSettingLabel.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Audio FX
                AudioManager.getInstance().play(Assets.getInstance().getSounds().getClick());
                changeShooting();
                save();
                return true;
            }
        });

        backLabel.addListener(UIFactory.createListener(ScreenEnum.MAIN_MENU));

        // Adds created table to stage
        addActor(table);
    }

    private void playSampleSound() {
        // Audio FX
        switch (MathUtils.random(1, 4)) {
            case 1:
                AudioManager.getInstance().play(Assets.getInstance().getSounds().getPickUpPowerOne());
                break;
            case 2:
                AudioManager.getInstance().play(Assets.getInstance().getSounds().getHeroShoot(), Constants.SHOOT_MAX_VOLUME);
                break;
            case 3:
                AudioManager.getInstance().play(Assets.getInstance().getSounds().getEnemyShoot(), Constants.SHOOT_MAX_VOLUME);
                break;
            case 4:
                AudioManager.getInstance().play(Assets.getInstance().getSounds().getHit());
                break;
        }
    }

    private void changeSliderMusic() {
        prefs.setVolMusic(sliderMusic.getValue());
        prefs.setMusic((sliderMusic.getValue() <= 0.0f)? false : true);
        AudioManager.getInstance().onSettingsUpdated();
    }

    private void changeSliderSound() {
        prefs.setVolSound(sliderSound.getValue());
        prefs.setSound((sliderSound.getValue() <= 0.0f)? false : true);
        AudioManager.getInstance().onSettingsUpdated();
    }

    private void changeShooting() {
        prefs.setManualShooting(!prefs.isManualShooting());
        setTextLabelShooting();
    }

    private void setTextLabelShooting() {
        if (prefs.isManualShooting()) {
            shootingSettingLabel.setText(i18NGameThreeBundle.format("settings.manualShooting"));
        } else {
            shootingSettingLabel.setText(i18NGameThreeBundle.format("settings.automaticShooting"));
        }
    }

    private void save() {
        prefs.save();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
