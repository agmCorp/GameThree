package uy.com.agm.gamethree.screens.util;

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
import uy.com.agm.gamethree.assets.sprites.AssetColOne;
import uy.com.agm.gamethree.game.DebugConstants;
import uy.com.agm.gamethree.screens.AbstractScreen;
import uy.com.agm.gamethree.widget.AnimatedImage;
import uy.com.agm.gamethree.widget.TypingLabelWorkaround;

/**
 * Created by AGM on 12/23/2017.
 */

public class HelpThreeScreen extends AbstractScreen {
    private static final String TAG = HelpThreeScreen.class.getName();

    // Constants
    private static final float MSG_WIDTH = 350.0f;
    private static final int COLUMNS = 2;
    private static final float COL_ONE_TABLE_CELL_HEIGHT = 30.0f;
    private static final float COL_ONE_TABLE_CELL_WIDTH = 80.0f;
    private static final float MAIN_TABLE_CELL_HEIGHT = 60.0f;

    private AssetScene2d assetScene2d;
    private Assets assetGame;
    private I18NBundle i18NGameThreeBundle;
    private Label.LabelStyle labelStyleBig;
    private Label.LabelStyle labelStyleSmall;

    public HelpThreeScreen() {
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
        table.top().padTop(AbstractScreen.PAD * 2);

        // Make the table fill the entire stage
        table.setFillParent(true);

        // Define our images and labels based on labelStyle
        Label itemsLabel = new Label(i18NGameThreeBundle.format("helpTwo.title"), labelStyleBig);

        AnimatedImage coin = new AnimatedImage(assetGame.getColOne().getCoinAnimation());
        TypingLabelWorkaround coinDsc = new TypingLabelWorkaround(i18NGameThreeBundle.format("helpTwo.coinDsc"), labelStyleSmall);
        coinDsc.setAlignment(Align.left);
        coinDsc.setWrap(true);

        Image trophy = new AnimatedImage();
        trophy.setDrawable(new TextureRegionDrawable(assetScene2d.getGoldTrophy()));
        trophy.setScaling(Scaling.fit);
        TypingLabelWorkaround trophyDsc = new TypingLabelWorkaround(i18NGameThreeBundle.format("helpTwo.trophyDsc"), labelStyleSmall);
        trophyDsc.setAlignment(Align.left);
        trophyDsc.setWrap(true);

        AnimatedImage hourglass = new AnimatedImage(assetScene2d.getHourglass().getHourglassAnimation());
        TypingLabelWorkaround hourglassDsc = new TypingLabelWorkaround(i18NGameThreeBundle.format("helpTwo.hourglassDsc"), labelStyleSmall);
        hourglassDsc.setAlignment(Align.left);
        hourglassDsc.setWrap(true);

        AnimatedImage heroHead = new AnimatedImage(assetScene2d.getHeroHead().getHeroHeadAnimation());
        TypingLabelWorkaround heroHeadDsc = new TypingLabelWorkaround(i18NGameThreeBundle.format("helpTwo.heroHeadDsc"), labelStyleSmall);
        heroHeadDsc.setAlignment(Align.left);
        heroHeadDsc.setWrap(true);

        Image heart = new Image();
        heart.setDrawable(new TextureRegionDrawable(assetScene2d.getEnergy().getEnergy75()));
        heart.setScaling(Scaling.fit);
        TypingLabelWorkaround heartDsc = new TypingLabelWorkaround(i18NGameThreeBundle.format("helpTwo.heartDsc"), labelStyleSmall);
        heartDsc.setAlignment(Align.left);
        heartDsc.setWrap(true);

        AnimatedImage shuriken = new AnimatedImage(assetGame.getSilverBullet().getSilverBulletAnimation());
        TypingLabelWorkaround shurikenDsc = new TypingLabelWorkaround(i18NGameThreeBundle.format("helpTwo.shurikenDsc"), labelStyleSmall);
        shurikenDsc.setAlignment(Align.left);
        shurikenDsc.setWrap(true);

        // Add values
        table.add(itemsLabel).colspan(COLUMNS);
        table.row();
        table.add(getColOneTable()).padTop(AbstractScreen.PAD).colspan(COLUMNS);
        table.row();
        table.add(coin).padTop(AbstractScreen.PAD).padLeft(AbstractScreen.PAD).height(MAIN_TABLE_CELL_HEIGHT);
        table.add(coinDsc).padTop(AbstractScreen.PAD).width(MSG_WIDTH).fill();
        table.row();
        table.add(trophy).padLeft(AbstractScreen.PAD).height(MAIN_TABLE_CELL_HEIGHT);
        table.add(trophyDsc).width(MSG_WIDTH).fill();
        table.row();
        table.add(hourglass).padLeft(AbstractScreen.PAD).height(MAIN_TABLE_CELL_HEIGHT);
        table.add(hourglassDsc).width(MSG_WIDTH).fill();
        table.row();
        table.add(heroHead).padLeft(AbstractScreen.PAD).height(MAIN_TABLE_CELL_HEIGHT);
        table.add(heroHeadDsc).width(MSG_WIDTH).fill();
        table.row();
        table.add(heart).padLeft(AbstractScreen.PAD).height(MAIN_TABLE_CELL_HEIGHT);
        table.add(heartDsc).width(MSG_WIDTH).fill();
        table.row();
        table.add(shuriken).padLeft(AbstractScreen.PAD).height(MAIN_TABLE_CELL_HEIGHT);
        table.add(shurikenDsc).width(MSG_WIDTH).fill();

        // Adds table to stage
        addActor(table);
    }

    private Table getColOneTable() {
        // Set table structure
        Table table = new Table();

        // Cell height
        table.row().height(COL_ONE_TABLE_CELL_HEIGHT);

        // Cell defaults
        table.defaults().width(COL_ONE_TABLE_CELL_WIDTH);

        // Debug lines
        table.setDebug(DebugConstants.DEBUG_LINES);

        // Define images
        AnimatedImage colOne;
        for(int i = 1; i < AssetColOne.MAX_TEXTURES; i++) {
            colOne = new AnimatedImage();

        }


        AnimatedImage coin = new AnimatedImage(assetGame.getColOne().getCoinAnimation());

        Image trophy = new AnimatedImage();
        trophy.setDrawable(new TextureRegionDrawable(assetScene2d.getGoldTrophy()));
        trophy.setScaling(Scaling.fit);

        AnimatedImage hourglass = new AnimatedImage(assetScene2d.getHourglass().getHourglassAnimation());

        AnimatedImage heroHead = new AnimatedImage(assetScene2d.getHeroHead().getHeroHeadAnimation());

        Image heart = new Image();
        heart.setDrawable(new TextureRegionDrawable(assetScene2d.getEnergy().getEnergy75()));
        heart.setScaling(Scaling.fit);

        AnimatedImage shuriken = new AnimatedImage(assetGame.getSilverBullet().getSilverBulletAnimation());

        // Add values
        table.add(coin);
        table.add(trophy);
        table.add(hourglass);
        table.add(heroHead);
        table.add(heart);
        table.add(shuriken);

        // Add a second row
        table.row();

        // Define label values based on labelStyle
        Label scoreValueLabel = new Label(i18NGameThreeBundle.format("helpTwo.fakeScore"), labelStyleSmall);
        scoreValueLabel.setAlignment(Align.center);
        Label highScoreValueLabel = new Label(i18NGameThreeBundle.format("helpTwo.fakeHighScore"), labelStyleSmall);
        highScoreValueLabel.setAlignment(Align.center);
        Label timeValueLabel = new Label(i18NGameThreeBundle.format("helpTwo.fakeTime"), labelStyleSmall);
        timeValueLabel.setAlignment(Align.center);
        Label livesValueLabel = new Label(i18NGameThreeBundle.format("helpTwo.fakeLives"), labelStyleSmall);
        livesValueLabel.setAlignment(Align.center);
        Label energyValueLabel = new Label(i18NGameThreeBundle.format("helpTwo.fakeEnergy"), labelStyleSmall);
        energyValueLabel.setAlignment(Align.center);
        Label silverBulletValueLabel = new Label(i18NGameThreeBundle.format("helpTwo.fakeSilverBullet"), labelStyleSmall);
        silverBulletValueLabel.setAlignment(Align.center);

        // Add values
        table.add(scoreValueLabel);
        table.add(highScoreValueLabel);
        table.add(timeValueLabel);
        table.add(livesValueLabel);
        table.add(energyValueLabel);
        table.add(silverBulletValueLabel);

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
        back.addListener(UIFactory.screenNavigationListener(ScreenEnum.HELP_ONE));
        backMenuLabel.addListener(UIFactory.screenNavigationListener(ScreenEnum.MAIN_MENU));

        // Adds table to stage
        addActor(table);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void render(float delta) {
        // Clear the screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        act();
        draw();
    }
}
