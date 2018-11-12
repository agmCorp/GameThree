package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.audio.sound.AssetSounds;
import uy.com.agm.gamethree.assets.scene2d.AssetScene2d;
import uy.com.agm.gamethree.game.DebugConstants;
import uy.com.agm.gamethree.game.GameSettings;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.ScreenTransitionEnum;
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

    private ImageButton music;
    private Slider sliderMusic;
    private ImageButton sound;
    private Slider sliderSound;
    private ImageButton easy;
    private ImageButton medium;
    private ImageButton hard;
    private Label difficultyLabel;
    private String easyDifficultyText;
    private String mediumDifficultyText;
    private String hardDifficultyText;
    private ImageButton shooting;
    private Label shootingLabel;
    private String manualShootingText;
    private String automaticShootingText;
    private GameSettings prefs;
    private I18NBundle i18NGameThreeBundle;
    Sound[] samples;

    public SettingsScreen() {
        super();
        prefs = GameSettings.getInstance();

        // I18n
        i18NGameThreeBundle = Assets.getInstance().getI18NGameThree().getI18NGameThreeBundle();

        AssetSounds assetSounds = Assets.getInstance().getSounds();
        Sound[] samples = {assetSounds.getPickUpPowerOne(), assetSounds.getPickUpPowerTwo(),
                assetSounds.getPickUpPowerThree(), assetSounds.getHeroShoot(),
                assetSounds.getEnemyShoot(), assetSounds.getHit(),
                assetSounds.getPum()};
        this.samples = samples;
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
        table.top().padTop(45); // todo

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
                new TextureRegionDrawable(assetScene2d.getMusicPressed()),
                new TextureRegionDrawable(assetScene2d.getMusicChecked()));

        // Slide music
        sliderMusic = new Slider(SLIDER_MIN, SLIDER_MAX, SLIDER_STEP, false, sliderStyle);
        float value = prefs.getVolMusic();
        music.setChecked(value <= 0.0f ? true : false);
        sliderMusic.setValue(value);

        // Button sound
        sound = new ImageButton(new TextureRegionDrawable(assetScene2d.getSound()),
                new TextureRegionDrawable(assetScene2d.getSoundPressed()),
                new TextureRegionDrawable(assetScene2d.getSoundChecked()));

        // Slide sound
        sliderSound = new Slider(SLIDER_MIN, SLIDER_MAX, SLIDER_STEP, false, sliderStyle);
        value = prefs.getVolSound();
        sound.setChecked(value <= 0.0f ? true : false);
        sliderSound.setValue(value);

        // Button easy
        easy = new ImageButton(new TextureRegionDrawable(assetScene2d.getEasy()),
                new TextureRegionDrawable(assetScene2d.getEasyPressed()));

        // Button medium
        medium = new ImageButton(new TextureRegionDrawable(assetScene2d.getMedium()),
                new TextureRegionDrawable(assetScene2d.getMediumPressed()));

        // Button hard
        hard = new ImageButton(new TextureRegionDrawable(assetScene2d.getHard()),
                new TextureRegionDrawable(assetScene2d.getHardPressed()));

        // Difficulty buttons visibility
        // todo aca es segun proppiedad
        Stack stack = new Stack();
        stack.add(easy);
        stack.add(medium);
        stack.add(hard);
        easy.setVisible(true);
        medium.setVisible(false);
        hard.setVisible(false);

        // Label difficulty
        // todo, aca es segun las propiedades!!!
        difficultyLabel = new Label("DIFFICULTY", labelStyleNormal);
        difficultyLabel.setAlignment(Align.center);
        easyDifficultyText = i18NGameThreeBundle.format("settings.easyDifficulty");
        mediumDifficultyText = i18NGameThreeBundle.format("settings.mediumDifficulty");
        hardDifficultyText = i18NGameThreeBundle.format("settings.hardDifficulty");
        difficultyLabel.setText(easyDifficultyText);

        // Button shooting
        shooting = new ImageButton(new TextureRegionDrawable(assetScene2d.getShooting()),
                new TextureRegionDrawable(assetScene2d.getShootingPressed()),
                new TextureRegionDrawable(assetScene2d.getShootingChecked()));

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
        table.add(stack).height(easy.getHeight()).padTop(AbstractScreen.PAD);
        table.row();
        table.add(difficultyLabel);
        table.row();
        table.add(shooting).height(shooting.getHeight()).padTop(AbstractScreen.PAD);
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

        easy.addListener(
                new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());

                        // todo debo setear la propiedad
                        easy.setVisible(false);
                        medium.setVisible(true);
                        hard.setVisible(false);
                        difficultyLabel.setText(mediumDifficultyText);
                        save();
                    }

                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });
        medium.addListener(
                new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());

                        // todo debo setear la propiedad
                        easy.setVisible(false);
                        medium.setVisible(false);
                        hard.setVisible(true);
                        difficultyLabel.setText(hardDifficultyText);
                        save();
                    }

                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });
        hard.addListener(
                new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());

                        // todo debo setear la propiedad
                        easy.setVisible(true);
                        medium.setVisible(false);
                        hard.setVisible(false);
                        difficultyLabel.setText(easyDifficultyText);
                        save();
                    }

                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });

        difficultyLabel.addListener(
                new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());
                        difficultyLabel.setColor(DEFAULT_COLOR);
                        // todo, voy por acá, no defini estoy aun pero seria leer la propiedad y en base a eso discriminar texto y botones
                        // creo que podría hacer 3 metodos seteasy, sethard, setmedim que lean la propety (seteada antes) y cambien texto y botones.
                        toggleShootingLabel();
                        save();
                    }

                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        shootingLabel.setColor(COLOR_LABEL_PRESSED);
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
                        shootingLabel.setColor(DEFAULT_COLOR);
                        toggleShootingLabel();
                        save();
                    }

                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        shootingLabel.setColor(COLOR_LABEL_PRESSED);
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
        back.addListener(UIFactory.screenNavigationListener(ScreenEnum.MAIN_MENU, ScreenTransitionEnum.SLIDE_RIGHT_EXP));

        // Adds table to stage
        addActor(table);
    }

    private void playSampleSound() {
        if (prefs.isSound()) {
            AudioManager.getInstance().playSound(samples[MathUtils.random(0, samples.length - 1)]);
        }
    }

    private void toggleMusic() {
        sliderMusic.setValue(music.isChecked() ? 0 : GameSettings.DEFAULT_VOLUME);
    }

    private void toggleSound() {
        sliderSound.setValue(sound.isChecked() ? 0 : GameSettings.DEFAULT_VOLUME);
    }

    private void changeSliderMusic() {
        float value = sliderMusic.getValue();
        boolean musicOn = value <= 0.0f ? false : true;
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
}
