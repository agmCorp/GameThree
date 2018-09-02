package uy.com.agm.gamethree.screens;

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
import uy.com.agm.gamethree.screens.util.ScreenTransitionEnum;
import uy.com.agm.gamethree.screens.util.UIFactory;
import uy.com.agm.gamethree.widget.TypingLabelWorkaround;

/**
 * Created by AGM on 12/23/2017.
 */

public class CreditsScreen extends AbstractScreen {
    private static final String TAG = CreditsScreen.class.getName();

    // Constants
    private static final float MSG_PAD_LEFT = 18.0f;
    private static final float MSG_WIDTH = 430.0f;

    public CreditsScreen() {
        super();
    }

    @Override
    public void buildStage() {
        defineMainTable();
        defineNavigationTable();
    }

    private void defineMainTable() {
        // I18n
        I18NBundle i18NGameThreeBundle = Assets.getInstance().getI18NGameThree().getI18NGameThreeBundle();

        // Set table structure
        Table table = new Table();

        // Design
        table.setBackground(new TextureRegionDrawable(Assets.getInstance().getScene2d().getTable()));

        // Debug lines
        table.setDebug(DebugConstants.DEBUG_LINES);

        // Top-Align table
        table.top().padTop(AbstractScreen.PAD * 3);

        // Make the table fill the entire stage
        table.setFillParent(true);

        // Personal fonts
        Label.LabelStyle labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.getInstance().getFonts().getDefaultBig();

        Label.LabelStyle labelStyleSmall = new Label.LabelStyle();
        labelStyleSmall.font = Assets.getInstance().getFonts().getDefaultSmall();

        // Define our labels based on labelStyle
        TypingLabelWorkaround creditsLabel = new TypingLabelWorkaround(i18NGameThreeBundle.format("credits.title"), labelStyleBig);
        TypingLabelWorkaround msgLabel = new TypingLabelWorkaround(i18NGameThreeBundle.format("credits.msg"), labelStyleSmall);
        msgLabel.setAlignment(Align.left);
        msgLabel.setWrap(true);

        // Add values
        table.add(creditsLabel);
        table.row();
        table.add(msgLabel).padLeft(MSG_PAD_LEFT).width(MSG_WIDTH).fill();

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
        back.addListener(UIFactory.screenNavigationListener(ScreenEnum.MAIN_MENU, ScreenTransitionEnum.SLIDE_DOWN));

        // Adds table to stage
        addActor(table);
    }
}
