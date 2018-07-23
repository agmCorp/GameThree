package uy.com.agm.gamethree.screens.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

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
    private static final float MSG_WIDTH = 250.0f;
    private static final int COLUMNS = 2;
    private static final float COL_ONE_TABLE_HEIGHT = 30.0f;
    private static final float CELL_WIDTH = 40.0f;
    private static final float CELL_HEIGHT = 50.0f;

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

        // Cell defaults
        table.defaults().height(CELL_HEIGHT);

        // Design
        table.setBackground(new TextureRegionDrawable(Assets.getInstance().getScene2d().getHelpBackground()));

        // Debug lines
        table.setDebug(DebugConstants.DEBUG_LINES);

        // Top-Align table
        table.top().padTop(AbstractScreen.PAD * 3);

        // Make the table fill the entire stage
        table.setFillParent(true);

        // Define our images and labels based on labelStyle
        Label itemsLabel = new Label(i18NGameThreeBundle.format("helpThree.title"), labelStyleBig);

        AnimatedImage powerOne = new AnimatedImage(assetGame.getPowerOne().getPowerOneAnimation());
        TypingLabelWorkaround powerOneDsc = new TypingLabelWorkaround(i18NGameThreeBundle.format("helpThree.powerOneDsc"), labelStyleSmall);
        powerOneDsc.setAlignment(Align.left);
        powerOneDsc.setWrap(true);

        AnimatedImage powerTwo = new AnimatedImage(assetGame.getPowerTwo().getPowerTwoAnimation());
        TypingLabelWorkaround powerTwoDsc = new TypingLabelWorkaround(i18NGameThreeBundle.format("helpThree.powerTwoDsc"), labelStyleSmall);
        powerTwoDsc.setAlignment(Align.left);
        powerTwoDsc.setWrap(true);

        AnimatedImage powerThree = new AnimatedImage(assetGame.getPowerThree().getPowerThreeAnimation());
        TypingLabelWorkaround powerThreeDsc = new TypingLabelWorkaround(i18NGameThreeBundle.format("helpThree.powerThreeDsc"), labelStyleSmall);
        powerThreeDsc.setAlignment(Align.left);
        powerThreeDsc.setWrap(true);

        AnimatedImage powerFour = new AnimatedImage(assetGame.getPowerFour().getPowerFourAnimation());
        TypingLabelWorkaround powerFourDsc = new TypingLabelWorkaround(i18NGameThreeBundle.format("helpThree.powerFourDsc"), labelStyleSmall);
        powerFourDsc.setAlignment(Align.left);
        powerFourDsc.setWrap(true);

        AnimatedImage powerFive = new AnimatedImage(assetGame.getPowerFive().getPowerFiveAnimation());
        TypingLabelWorkaround powerFiveDsc = new TypingLabelWorkaround(i18NGameThreeBundle.format("helpThree.powerFiveDsc"), labelStyleSmall);
        powerFiveDsc.setAlignment(Align.left);
        powerFiveDsc.setWrap(true);

        AnimatedImage colTwo = new AnimatedImage(assetGame.getColTwo().getColTwoAnimation());
        TypingLabelWorkaround colTwoDsc = new TypingLabelWorkaround(i18NGameThreeBundle.format("helpThree.colTwoDsc"), labelStyleSmall);
        powerFiveDsc.setAlignment(Align.left);
        powerFiveDsc.setWrap(true);

        AnimatedImage colThreeHeart = new AnimatedImage(assetGame.getColThree().getColThreeAnimation());
        TypingLabelWorkaround colThreeHeartDsc = new TypingLabelWorkaround(i18NGameThreeBundle.format("helpThree.colThreeHeartDsc"), labelStyleSmall);
        colThreeHeartDsc.setAlignment(Align.left);
        colThreeHeartDsc.setWrap(true);

        AnimatedImage colThreeLives = new AnimatedImage(assetScene2d.getGoldenHeroHead().getGoldenHeroHeadAnimation());
        TypingLabelWorkaround colThreeLivesDsc = new TypingLabelWorkaround(i18NGameThreeBundle.format("helpThree.colThreeLivesDsc"), labelStyleSmall);
        colThreeLivesDsc.setAlignment(Align.left);
        colThreeLivesDsc.setWrap(true);

        AnimatedImage colSilverBullet = new AnimatedImage(assetGame.getSilverBullet().getColSilverBulletAnimation());
        TypingLabelWorkaround colSilverBulletDsc = new TypingLabelWorkaround(i18NGameThreeBundle.format("helpThree.colSilverBulletDsc"), labelStyleSmall);
        powerFiveDsc.setAlignment(Align.left);
        powerFiveDsc.setWrap(true);

        // Add values
        table.add(itemsLabel).colspan(COLUMNS);
        table.row();
        table.add(getColOneTable()).align(Align.left).padTop(AbstractScreen.PAD).colspan(COLUMNS);
        table.row();
        table.add(colThreeLives).align(Align.left).width(CELL_WIDTH);
        table.add(colThreeLivesDsc).width(MSG_WIDTH).fill();
        table.row();
        table.add(powerOne).align(Align.left).width(CELL_WIDTH);
        table.add(powerOneDsc).width(MSG_WIDTH).fill();
        table.row();
        table.add(powerTwo).align(Align.left).width(CELL_WIDTH);
        table.add(powerTwoDsc).width(MSG_WIDTH).fill();
        table.row();
        table.add(powerThree).align(Align.left).width(CELL_WIDTH);
        table.add(powerThreeDsc).width(MSG_WIDTH).fill();
        table.row();
        table.add(powerFour).align(Align.left).width(CELL_WIDTH);
        table.add(powerFourDsc).width(MSG_WIDTH).fill();
        table.row();
        table.add(powerFive).align(Align.left).width(CELL_WIDTH);
        table.add(powerFiveDsc).width(MSG_WIDTH).fill();
        table.row();
        table.add(colTwo).align(Align.left).width(CELL_WIDTH);
        table.add(colTwoDsc).width(MSG_WIDTH).fill();
        table.row();
        table.add(colSilverBullet).align(Align.left).width(CELL_WIDTH);
        table.add(colSilverBulletDsc).width(MSG_WIDTH).fill();
        table.row();
        table.add(colThreeHeart).align(Align.left).width(CELL_WIDTH);
        table.add(colThreeHeartDsc).width(MSG_WIDTH).fill();

        // Adds table to stage
        addActor(table);
    }

    private Table getColOneTable() {
        // Set table structure
        Table table = new Table();

        // Cell defaults
        table.defaults().height(COL_ONE_TABLE_HEIGHT);

        // Debug lines
        table.setDebug(DebugConstants.DEBUG_LINES);

        // Define images
        AnimatedImage gold;
        AnimatedImage silver;
        AnimatedImage bronze;
        for(int i = 0; i < AssetColOne.MAX_TEXTURES; i++) {
            gold = new AnimatedImage(assetGame.getColOne().getGoldAnimation(i));
            silver = new AnimatedImage(assetGame.getColOne().getSilverAnimation(i));
            bronze = new AnimatedImage(assetGame.getColOne().getBronzeAnimation(i));

            // Add values
            table.add(gold).width(CELL_WIDTH);
            table.add(silver).width(CELL_WIDTH);
            table.add(bronze).width(CELL_WIDTH);
        }

        // Define labels based on labelStyle
        Label colOneDscLabel = new Label(i18NGameThreeBundle.format("helpThree.colOneDsc"), labelStyleSmall);

        // Add values
        table.add(colOneDscLabel).padLeft(AbstractScreen.PAD);

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
        Label backMenuLabel = new Label(i18NGameThreeBundle.format("helpTwo.backMenu"), labelStyleSmall);

        // Add values
        table.add(back);
        table.row();
        table.add(backMenuLabel).height(AbstractScreen.PAD * 2).colspan(COLUMNS);

        // Events
        back.addListener(UIFactory.screenNavigationListener(ScreenEnum.HELP_TWO));
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
