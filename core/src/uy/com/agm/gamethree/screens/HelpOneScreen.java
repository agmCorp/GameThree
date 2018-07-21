package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.scene2d.AssetScene2d;
import uy.com.agm.gamethree.game.DebugConstants;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.UIFactory;
import uy.com.agm.gamethree.tools.AudioManager;

/**
 * Created by AGM on 12/23/2017.
 */

public class HelpOneScreen extends AbstractScreen {
    private static final String TAG = HelpOneScreen.class.getName();

    // Constants
    private static final float MSG_WIDTH = 430.0f;

    TypingLabel msgLabel;
    I18NBundle i18NGameThreeBundle;
boolean puto = false;
    public HelpOneScreen() {
        super();
    }

    @Override
    public void buildStage() {
        // Play music
        AudioManager.getInstance().playMusic(Assets.getInstance().getMusic().getSongTale(), false);

        defineMainTable();
        defineNavigationTable();
    }

    private void defineMainTable() {
        // I18n
        i18NGameThreeBundle = Assets.getInstance().getI18NGameThree().getI18NGameThreeBundle();

        // Set table structure
        Table table = new Table();

        // Design
        table.setBackground(new TextureRegionDrawable(Assets.getInstance().getScene2d().getHelpOne()));

        // Debug lines
        table.setDebug(DebugConstants.DEBUG_LINES);

        // Top-Align table
        table.top().padTop(AbstractScreen.PAD * 2);

        // Make the table fill the entire stage
        table.setFillParent(true);

        Label.LabelStyle labelStyleSmall = new Label.LabelStyle();
        labelStyleSmall.font = Assets.getInstance().getFonts().getDefaultSmall();

        // Define our labels based on labelStyle
        msgLabel = new TypingLabel(i18NGameThreeBundle.format("helpOne.talePartOne"), labelStyleSmall);
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

        // Add values
        table.add(back).padBottom(AbstractScreen.PAD * 2);

        // Events
        back.addListener(UIFactory.screenNavigationListener(ScreenEnum.MAIN_MENU));

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

        if (msgLabel.hasEnded() && !puto) {
            puto = true;
            msgLabel.restart(i18NGameThreeBundle.format("helpOne.talePartTwo"));
        }
    }
}
