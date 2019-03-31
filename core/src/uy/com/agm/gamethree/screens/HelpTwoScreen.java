package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Scaling;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.scene2d.AssetScene2d;
import uy.com.agm.gamethree.game.DebugConstants;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.ScreenManager;
import uy.com.agm.gamethree.screens.util.ScreenTransitionEnum;
import uy.com.agm.gamethree.screens.util.UIFactory;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.widget.AnimatedImage;
import uy.com.agm.gamethree.widget.TypingLabelWorkaround;

/**
 * Created by AGM on 12/23/2017.
 */

public class HelpTwoScreen extends AbstractScreen {
    private static final String TAG = HelpTwoScreen.class.getName();

    // Constants
    private static final float MSG_WIDTH = 350.0f;
    private static final int COLUMNS = 2;
    private static final float FAKE_HUD_TABLE_CELL_HEIGHT = 30.0f;
    private static final float FAKE_HUD_TABLE_CELL_WIDTH = 80.0f;
    private static final float CELL_HEIGHT = 60.0f;
    private static final float CELL_WIDTH = 40.0f;
    private static final int ENDURANCE_INDEX = 10; // see i18NGameThreeBundle.helpTwo.fakeEndurance

    private AssetScene2d assetScene2d;
    private Assets assetGame;
    private I18NBundle i18NGameThreeBundle;
    private Label.LabelStyle labelStyleBig;
    private Label.LabelStyle labelStyleSmall;

    public HelpTwoScreen() {
        super();
        // UI assets
        assetScene2d = Assets.getInstance().getScene2d();

        // Game assets
        assetGame = Assets.getInstance();

        // I18n
        i18NGameThreeBundle = Assets.getInstance().getI18NGameThree().getI18NGameThreeBundle();

        // Personal fonts
        labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.getInstance().getFonts().getDefaultBig();
        labelStyleSmall = new Label.LabelStyle();
        labelStyleSmall.font = Assets.getInstance().getFonts().getDefaultSmall();

        // Play music
        AudioManager.getInstance().playMusic(Assets.getInstance().getMusic().getSongHelp(), true);
    }

    @Override
    public void buildStage() {
        defineMainTable();
        defineNavigationTable();
    }

    private void defineMainTable() {
        // Set table structure
        Table table = new Table();

        // Design
        table.setBackground(new TextureRegionDrawable(Assets.getInstance().getScene2d().getHelpBackground()));

        // Debug lines
        table.setDebug(DebugConstants.DEBUG_LINES);

        // Top-Align table
        table.top().padTop(AbstractScreen.PAD);

        // Make the table fill the entire stage
        table.setFillParent(true);

        // Define our images and labels based on labelStyle
        Label statusBarLabel = new Label(i18NGameThreeBundle.format("helpTwo.title"), labelStyleBig);

        AnimatedImage coin = new AnimatedImage(assetGame.getColOne().getCoinAnimation());
        TypingLabelWorkaround coinDsc = new TypingLabelWorkaround(i18NGameThreeBundle.format("helpTwo.coinDsc"), labelStyleSmall);
        coinDsc.setAlignment(Align.left);
        coinDsc.setWrap(true);

        AnimatedImage hourglass = new AnimatedImage(assetScene2d.getHourglass().getHourglassAnimation());
        TypingLabelWorkaround hourglassDsc = new TypingLabelWorkaround(i18NGameThreeBundle.format("helpTwo.hourglassDsc"), labelStyleSmall);
        hourglassDsc.setAlignment(Align.left);
        hourglassDsc.setWrap(true);

        AnimatedImage heroHead = new AnimatedImage(assetScene2d.getHeroHead().getHeroHeadAnimation());
        TypingLabelWorkaround heroHeadDsc = new TypingLabelWorkaround(i18NGameThreeBundle.format("helpTwo.heroHeadDsc"), labelStyleSmall);
        heroHeadDsc.setAlignment(Align.left);
        heroHeadDsc.setWrap(true);

        Image heart = new Image();
        heart.setDrawable(new TextureRegionDrawable(assetScene2d.getEndurance().getEnduranceStand(ENDURANCE_INDEX)));
        heart.setScaling(Scaling.fit);
        TypingLabelWorkaround heartDsc = new TypingLabelWorkaround(i18NGameThreeBundle.format("helpTwo.heartDsc"), labelStyleSmall);
        heartDsc.setAlignment(Align.left);
        heartDsc.setWrap(true);

        AnimatedImage shuriken = new AnimatedImage(assetGame.getSilverBullet().getSilverBulletAnimation());
        TypingLabelWorkaround shurikenDsc = new TypingLabelWorkaround(i18NGameThreeBundle.format("helpTwo.shurikenDsc"), labelStyleSmall);
        shurikenDsc.setAlignment(Align.left);
        shurikenDsc.setWrap(true);

        AnimatedImage skull = new AnimatedImage(assetScene2d.getPenalties().getPenaltiesAnimation());
        TypingLabelWorkaround skullDsc = new TypingLabelWorkaround(i18NGameThreeBundle.format("helpTwo.skullDsc"), labelStyleSmall);
        skullDsc.setAlignment(Align.left);
        skullDsc.setWrap(true);

        // Add values
        table.add(statusBarLabel).colspan(COLUMNS);
        table.row();
        table.add(getFakeHudTable()).colspan(COLUMNS);
        table.row().padTop(AbstractScreen.PAD);
        table.add(coin).padLeft(AbstractScreen.PAD).height(CELL_HEIGHT).width(CELL_WIDTH);
        table.add(coinDsc).width(MSG_WIDTH).fill();
        table.row().padTop(AbstractScreen.PAD / 2);
        table.add(hourglass).padLeft(AbstractScreen.PAD).height(CELL_HEIGHT).width(CELL_WIDTH);
        table.add(hourglassDsc).width(MSG_WIDTH).fill();
        table.row().padTop(AbstractScreen.PAD / 2);
        table.add(heroHead).padLeft(AbstractScreen.PAD).height(CELL_HEIGHT).width(CELL_WIDTH);
        table.add(heroHeadDsc).width(MSG_WIDTH).fill();
        table.row().padTop(AbstractScreen.PAD / 2);
        table.add(heart).padLeft(AbstractScreen.PAD).height(CELL_HEIGHT).width(CELL_WIDTH);
        table.add(heartDsc).width(MSG_WIDTH).fill();
        table.row().padTop(AbstractScreen.PAD / 2);
        table.add(shuriken).padLeft(AbstractScreen.PAD).height(CELL_HEIGHT).width(CELL_WIDTH);
        table.add(shurikenDsc).width(MSG_WIDTH).fill();
        table.row().padTop(AbstractScreen.PAD / 2);
        table.add(skull).padLeft(AbstractScreen.PAD).height(CELL_HEIGHT).width(CELL_WIDTH);
        table.add(skullDsc).width(MSG_WIDTH).fill();

        // Adds table to stage
        addActor(table);
    }

    private Table getFakeHudTable() {
        // Set table structure
        Table table = new Table();

        // Cell height
        table.row().height(FAKE_HUD_TABLE_CELL_HEIGHT);

        // Cell defaults
        table.defaults().width(FAKE_HUD_TABLE_CELL_WIDTH);

        // Debug lines
        table.setDebug(DebugConstants.DEBUG_LINES);

        // Define images
        AnimatedImage coin = new AnimatedImage(assetGame.getColOne().getCoinAnimation());

        AnimatedImage hourglass = new AnimatedImage(assetScene2d.getHourglass().getHourglassAnimation());

        AnimatedImage heroHead = new AnimatedImage(assetScene2d.getHeroHead().getHeroHeadAnimation());

        Image heart = new Image();
        heart.setDrawable(new TextureRegionDrawable(assetScene2d.getEndurance().getEnduranceStand(ENDURANCE_INDEX)));
        heart.setScaling(Scaling.fit);

        AnimatedImage shuriken = new AnimatedImage(assetGame.getSilverBullet().getSilverBulletAnimation());

        AnimatedImage skull = new AnimatedImage(assetScene2d.getPenalties().getPenaltiesAnimation());

        // Add values
        table.add(coin);
        table.add(hourglass);
        table.add(heroHead);
        table.add(heart);
        table.add(shuriken);
        table.add(skull);

        // Add a second row
        table.row();

        // Define label values based on labelStyle
        Label scoreValueLabel = new Label(i18NGameThreeBundle.format("helpTwo.fakeScore"), labelStyleSmall);
        scoreValueLabel.setAlignment(Align.center);
        Label timeValueLabel = new Label(i18NGameThreeBundle.format("helpTwo.fakeTime"), labelStyleSmall);
        timeValueLabel.setAlignment(Align.center);
        Label livesValueLabel = new Label(i18NGameThreeBundle.format("helpTwo.fakeLives"), labelStyleSmall);
        livesValueLabel.setAlignment(Align.center);
        Label enduranceValueLabel = new Label(i18NGameThreeBundle.format("helpTwo.fakeEndurance"), labelStyleSmall);
        enduranceValueLabel.setAlignment(Align.center);
        Label silverBulletValueLabel = new Label(i18NGameThreeBundle.format("helpTwo.fakeSilverBullet"), labelStyleSmall);
        silverBulletValueLabel.setAlignment(Align.center);
        Label penaltiesValueLabel = new Label(i18NGameThreeBundle.format("helpTwo.fakePenalties"), labelStyleSmall);
        penaltiesValueLabel.setAlignment(Align.center);

        // Add values
        table.add(scoreValueLabel);
        table.add(timeValueLabel);
        table.add(livesValueLabel);
        table.add(enduranceValueLabel);
        table.add(silverBulletValueLabel);
        table.add(penaltiesValueLabel);

        return table;
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

        // Define images and labels based on labelStyle
        ImageButton back = new ImageButton(new TextureRegionDrawable(assetScene2d.getBack()),
                new TextureRegionDrawable(assetScene2d.getBackPressed()));
        ImageButton forward = new ImageButton(new TextureRegionDrawable(assetScene2d.getForward()),
                new TextureRegionDrawable(assetScene2d.getForwardPressed()));
        Label backMenuLabel = new Label(i18NGameThreeBundle.format("helpTwo.backMenu"), labelStyleSmall);

        // Add values
        table.add(back);
        table.add(forward);
        table.row();
        table.add(backMenuLabel).height(AbstractScreen.PAD * 2).colspan(COLUMNS);

        // Events
        back.addListener(UIFactory.screenNavigationListener(ScreenEnum.HELP_ONE, ScreenTransitionEnum.SLIDE_RIGHT_EXP));
        forward.addListener(UIFactory.screenNavigationListener(ScreenEnum.HELP_THREE, ScreenTransitionEnum.SLIDE_LEFT_EXP));
        backMenuLabel.addListener(UIFactory.screenNavigationListener(ScreenEnum.MAIN_MENU, ScreenTransitionEnum.SLIDE_DOWN));

        // Adds table to stage
        addActor(table);
    }

    @Override
    protected void clearScreen() {
        // Clear the screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    protected void goBack() {
        playClick();
        ScreenManager.getInstance().showScreen(ScreenEnum.HELP_ONE, ScreenTransitionEnum.SLIDE_DOWN);
    }
}
