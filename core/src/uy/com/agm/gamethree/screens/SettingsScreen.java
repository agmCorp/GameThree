package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

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

    private Label.LabelStyle labelStyleBig;
    private Label.LabelStyle labelStyleNormal;
    Slider.SliderStyle sliderStyle;
    private Label shootingLabel;
    private Label backLabel;
    private Texture sliderBackgroundTex;
    private Texture sliderKnobTex;
    private Slider sliderMusic;
    private Slider sliderSound;
    private GameSettings prefs;

    public SettingsScreen() {
        super();
        prefs = GameSettings.getInstance();
        sliderBackgroundTex = new Texture(Gdx.files.internal(Constants.SLIDER_BACKGROUND));
        sliderKnobTex = new Texture(Gdx.files.internal(Constants.SLIDER_KNOB));
    }

    @Override
    public void buildStage() {
        // Personal fonts
        labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.getInstance().getFonts().getDefaultBig();

        labelStyleNormal = new Label.LabelStyle();
        labelStyleNormal.font = Assets.getInstance().getFonts().getDefaultNormal();

        // Define our labels based on labelStyle
        backLabel = new Label("Back to menu", labelStyleNormal);

        //Slider style
        sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = new TextureRegionDrawable(new TextureRegion(sliderBackgroundTex));
        sliderStyle.knob = new TextureRegionDrawable(new TextureRegion(sliderKnobTex));

        // Music
        sliderMusic = new Slider(Constants.SLIDER_MIN, Constants.SLIDER_MAX, Constants.SLIDER_STEP, false, sliderStyle);
        sliderMusic.setValue(prefs.getVolMusic());

        // Sound
        sliderSound = new Slider(Constants.SLIDER_MIN, Constants.SLIDER_MAX, Constants.SLIDER_STEP, false, sliderStyle);
        sliderSound.setValue(prefs.getVolSound());

        // Shooting
        shootingLabel = new Label("Manual shooting", labelStyleNormal);
        setTextLabelShooting();

        // Set table structure
        Table table = new Table();
        table.center();
        table.setFillParent(true);
        table.add(new Label("Settings", labelStyleBig)).center();
        table.row();
        table.add(new Label("Music", labelStyleNormal)).padTop(Constants.PAD_TOP).center();
        table.row();
        table.add(sliderMusic).width(Constants.SLIDER_WIDTH).padTop(Constants.PAD_TOP).center();
        table.row();
        table.add(new Label("Sound effects", labelStyleNormal)).padTop(Constants.PAD_TOP).center();
        table.row();
        table.add(sliderSound).width(Constants.SLIDER_WIDTH).padTop(Constants.PAD_TOP).center();
        table.row();
        table.add(shootingLabel).padTop(Constants.PAD_TOP).center();
        table.row();
        table.add(backLabel).padTop(Constants.PAD_TOP).center();

        // Debug lines
        table.setDebug(Constants.DEBUG_MODE);

        // Slider listener
        sliderMusic.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                saveSliderMusic();
            }
        });

        // Slider listener
        sliderSound.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                saveSliderSound();
                playSampleSound();
            }
        });

        // Events
        shootingLabel.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Audio FX
                AudioManager.getInstance().play(Assets.getInstance().getSounds().getClick());
                saveShooting();
                return true;
            }
        });

        // Events
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

    private void saveSliderMusic() {
        prefs.setVolMusic(sliderMusic.getValue());
        prefs.setMusic((sliderMusic.getValue() <= 0.0f)? false : true);
        prefs.save();
        AudioManager.getInstance().onSettingsUpdated();
    }

    private void saveSliderSound() {
        prefs.setVolSound(sliderSound.getValue());
        prefs.setSound((sliderSound.getValue() <= 0.0f)? false : true);
        prefs.save();
        AudioManager.getInstance().onSettingsUpdated();
    }

    private void saveShooting() {
        prefs.setManualShooting(!prefs.isManualShooting());
        setTextLabelShooting();
        prefs.save();
    }

    private void setTextLabelShooting() {
        if (prefs.isManualShooting()) {
            shootingLabel.setText("Manual shooting");
        } else {
            shootingLabel.setText("Automatic shooting");
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        sliderBackgroundTex.dispose();
        sliderKnobTex.dispose();
    }
}
