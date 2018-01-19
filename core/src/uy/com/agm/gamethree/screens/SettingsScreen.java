package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.game.Constants;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.UIFactory;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.GamePreferences;

/**
 * Created by AGM on 1/18/2018.
 */

public class SettingsScreen extends AbstractScreen {
    private static final String TAG = SettingsScreen.class.getName();

    private Texture sliderBackgroundTex;
    private Texture sliderKnobTex;
    private Slider sliderMusic;
    private Slider sliderSound;
    private GamePreferences prefs;

    public SettingsScreen() {
        super();
        prefs = GamePreferences.instance;
        sliderBackgroundTex = new Texture(Gdx.files.internal(Constants.SLIDER_BACKGROUND));
        sliderKnobTex = new Texture(Gdx.files.internal(Constants.SLIDER_KNOB));
    }

    @Override
    public void buildStage() {
        // Personal fonts
        Label.LabelStyle labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.instance.fonts.defaultBig;

        Label.LabelStyle labelStyleNormal = new Label.LabelStyle();
        labelStyleNormal.font = Assets.instance.fonts.defaultNormal;

        // Define our labels based on labelStyle
        Label titleLabel = new Label("Preferences", labelStyleBig);
        Label musicLabel = new Label("Music", labelStyleNormal);
        Label soundLabel = new Label("sounds FX", labelStyleNormal);
        Label backLabel = new Label("Back to menu", labelStyleNormal);

        //Slider
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = new TextureRegionDrawable(new TextureRegion(sliderBackgroundTex));
        sliderStyle.knob = new TextureRegionDrawable(new TextureRegion(sliderKnobTex));
        sliderMusic = new Slider(Constants.SLIDER_MIN, Constants.SLIDER_MAX, Constants.SLIDER_STEP, false, sliderStyle);
        sliderSound = new Slider(Constants.SLIDER_MIN, Constants.SLIDER_MAX, Constants.SLIDER_STEP, false, sliderStyle);

        // Set table structure
        Table table = new Table();
        table.center();
        table.setFillParent(true);
        table.add(titleLabel).center();
        table.row();
        table.add(musicLabel).padTop(Constants.PAD_TOP).center();
        table.row();
        table.add(sliderMusic).padTop(Constants.PAD_TOP).center();
        table.row();
        table.add(soundLabel).padTop(Constants.PAD_TOP).center();
        table.row();
        table.add(sliderSound).padTop(Constants.PAD_TOP).center();
        table.row();
        table.add(backLabel).padTop(Constants.PAD_TOP).center();

        // Slider listener
        sliderMusic.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                saveSliderMusic();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            };
        });

        // Slider listener
        sliderSound.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                saveSliderSound();
                playSampleSound();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            };
        });

        // Events
        backLabel.addListener(UIFactory.createListener(ScreenEnum.MAIN_MENU));

        // Adds created table to stage
        addActor(table);

        // Load saved values
        loadSettings();
    }

    private void playSampleSound() {
        // Audio FX
        int sample = MathUtils.random(1, 4);
        switch (sample) {
            case 1:
                AudioManager.instance.play(Assets.instance.sounds.pickUpPowerOne);
                break;
            case 2:
                AudioManager.instance.play(Assets.instance.sounds.heroShoot, Constants.SHOOT_MAX_VOLUME);
                break;
            case 3:
                AudioManager.instance.play(Assets.instance.sounds.enemyShoot, Constants.SHOOT_MAX_VOLUME);
                break;
            case 4:
                AudioManager.instance.play(Assets.instance.sounds.hit);
                break;
        }
    }

    private void saveSliderMusic() {
        prefs.volMusic = sliderMusic.getValue();
        prefs.music = (sliderMusic.getValue() <= 0.0f)? false : true;
        prefs.save();
        AudioManager.instance.onSettingsUpdated();
    }

    private void saveSliderSound() {
        prefs.volSound = sliderSound.getValue();
        prefs.sound = (sliderSound.getValue() <= 0.0f)? false : true;
        prefs.save();
        AudioManager.instance.onSettingsUpdated();
    }

    private void loadSettings () {
        GamePreferences prefs = GamePreferences.instance;
        prefs.load();
        sliderSound.setValue(prefs.volSound);
        sliderMusic.setValue(prefs.volMusic);
    }

    @Override
    public void dispose() {
        super.dispose();

        sliderBackgroundTex.dispose();
        sliderKnobTex.dispose();
    }
}
