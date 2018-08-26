package uy.com.agm.gamethree.screens;

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
import uy.com.agm.gamethree.game.DebugConstants;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.UIFactory;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.widget.TypingLabelWorkaround;

/**
 * Created by AGM on 12/23/2017.
 */

public class HelpOneScreen extends AbstractScreen {
    private static final String TAG = HelpOneScreen.class.getName();

    // Constants
    private static final float MSG_WIDTH = 430.0f;

    private TypingLabelWorkaround msgLabel;
    private I18NBundle i18NGameThreeBundle;
    private boolean talePartTwo;

    public HelpOneScreen() {
        super();
        talePartTwo = false;

        // Play music
        AudioManager.getInstance().playMusic(Assets.getInstance().getMusic().getSongTale(), false);
    }

    @Override
    public void buildStage() {
        defineMainTable();
        defineNavigationTable();
    }

    private void defineMainTable() {
        // I18n
        i18NGameThreeBundle = Assets.getInstance().getI18NGameThree().getI18NGameThreeBundle();

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

        Label.LabelStyle labelStyleSmall = new Label.LabelStyle();
        labelStyleSmall.font = Assets.getInstance().getFonts().getDefaultSmall();

        // Define our labels based on labelStyle
        msgLabel = new TypingLabelWorkaround(i18NGameThreeBundle.format("helpOne.talePartOne"), labelStyleSmall);
        msgLabel.setAlignment(Align.left);
        msgLabel.setWrap(true);

        // Add values
        table.add(msgLabel).width(MSG_WIDTH).fill();

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
        ImageButton forward = new ImageButton(new TextureRegionDrawable(assetScene2d.getForward()),
                new TextureRegionDrawable(assetScene2d.getForwardPressed()));

        // Add values
        table.add(back).padBottom(AbstractScreen.PAD * 2);
        table.add(forward).padBottom(AbstractScreen.PAD * 2);

        // Events
        back.addListener(UIFactory.screenNavigationListener(ScreenEnum.MAIN_MENU));
        forward.addListener(UIFactory.screenNavigationListener(ScreenEnum.HELP_TWO));

        // Adds table to stage
        addActor(table);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (msgLabel.hasEnded() && !talePartTwo) {
            talePartTwo = true;
            msgLabel.restart(i18NGameThreeBundle.format("helpOne.talePartTwo"));
        }
    }

    @Override
    protected void clearScreen() {
        // Clear the screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
