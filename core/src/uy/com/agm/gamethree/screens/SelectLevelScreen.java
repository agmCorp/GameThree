package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Scaling;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.scene2d.AssetScene2d;
import uy.com.agm.gamethree.game.DebugConstants;
import uy.com.agm.gamethree.game.GameSettings;
import uy.com.agm.gamethree.game.LevelState;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.ScreenTransitionEnum;
import uy.com.agm.gamethree.screens.util.UIFactory;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.LevelFactory;
import uy.com.agm.gamethree.widget.TypingLabelWorkaround;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by AGM on 12/23/2017.
 */

public class SelectLevelScreen extends AbstractScreen {
    private static final String TAG = SelectLevelScreen.class.getName();

    // Constants
    private static final float SCROLL_PANE_MAX_HEIGHT = 650.0f;
    private static final float CONTAINER_WIDTH = 400.0f;
    private static final float STAR_WIDTH = 30.0f;
    private static final float STAR_HEIGHT = 100.0f;
    private static final float STARS_TABLE_WIDTH = 300.0f;
    private static final int NINE_PATCH_PIXELS = 2;
    private static final float HAND_SCALE = 0.4f;
    private static final float HAND_X = 340.0f;
    private static final float HAND_Y = 350.0f;
    private static final float HAND_OFFSET = 50.0f;
    private static final float HAND_GAME_COMPLETED = 70.0f;
    private static final float HAND_FADE_DURATION = 2.0f;
    private static final float HAND_MOVE_DURATION = 0.5f;
    private static final float HAND_MOVE_BY = 15.0f;

    public SelectLevelScreen() {
        super();

        // Play menu music (if we arrive at this screen from DimScreen)
        AudioManager.getInstance().playMusic(Assets.getInstance().getMusic().getSongMainMenu(), true);
    }

    @Override
    public void buildStage() {
        defineMainTable();
        defineNavigationTable();
        setHand();
    }

    private void setHand() {
        if (!DebugConstants.DEBUG_LEVELS) {
            Image hand = new Image(Assets.getInstance().getScene2d().getHand());
            hand.setScale(HAND_SCALE);

            // WA: actor.localToStageCoordinates(new Vector2(0,0)) didn't work
            GameSettings prefs = GameSettings.getInstance();
            hand.setY(prefs.isGameComplete() ? HAND_GAME_COMPLETED : HAND_Y - prefs.getLevels().size * HAND_OFFSET);
            hand.setX(HAND_X);

            // Actions
            SequenceAction sequenceOne = sequence(fadeIn(HAND_FADE_DURATION), fadeOut(HAND_FADE_DURATION));
            SequenceAction sequenceTwo = sequence(moveBy(HAND_MOVE_BY, -HAND_MOVE_BY, HAND_MOVE_DURATION, Interpolation.smooth),
                    moveBy(-HAND_MOVE_BY, HAND_MOVE_BY, HAND_MOVE_DURATION, Interpolation.smooth));
            hand.addAction(parallel(forever(sequenceOne), forever(sequenceTwo)));

            addActor(hand);
        }
    }

    private void defineMainTable() {
        // I18n
        I18NBundle i18NGameThreeBundle = Assets.getInstance().getI18NGameThree().getI18NGameThreeBundle();

        // Set table structure
        Table container = new Table();

        // Design
        container.setBackground(new TextureRegionDrawable(Assets.getInstance().getScene2d().getTable()));

        // Debug lines
        container.setDebug(DebugConstants.DEBUG_LINES);

        // Center-Align table
        container.center().padBottom(AbstractScreen.PAD * 2);

        // Make the table fill the entire stage
        container.setFillParent(true);

        // Personal fonts
        Label.LabelStyle labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.getInstance().getFonts().getDefaultBig();

        // Define our labels based on labelStyle
        Label selectLevelLabel = new Label(i18NGameThreeBundle.format("selectLevel.title"), labelStyleBig);

        // Define a ScrollPane with scrollbars
        ScrollPane.ScrollPaneStyle scrollStyle = new ScrollPane.ScrollPaneStyle();
        NinePatch vScrollKnobNine = new NinePatch(Assets.getInstance().getScene2d().getvScrollbarKnob9(),
                NINE_PATCH_PIXELS, NINE_PATCH_PIXELS, NINE_PATCH_PIXELS, NINE_PATCH_PIXELS);
        NinePatch vScrollNine = new NinePatch(Assets.getInstance().getScene2d().getvScrollbar9(),
                NINE_PATCH_PIXELS, NINE_PATCH_PIXELS, NINE_PATCH_PIXELS, NINE_PATCH_PIXELS);
        NinePatch hScrollKnobNine = new NinePatch(Assets.getInstance().getScene2d().gethScrollbarKnob9(),
                NINE_PATCH_PIXELS, NINE_PATCH_PIXELS, NINE_PATCH_PIXELS, NINE_PATCH_PIXELS);
        NinePatch hScrollNine = new NinePatch(Assets.getInstance().getScene2d().gethScrollbar9(),
                NINE_PATCH_PIXELS, NINE_PATCH_PIXELS, NINE_PATCH_PIXELS, NINE_PATCH_PIXELS);

        scrollStyle.vScrollKnob = new NinePatchDrawable(vScrollKnobNine);
        scrollStyle.vScroll = new NinePatchDrawable(vScrollNine);
        scrollStyle.hScrollKnob = new NinePatchDrawable(hScrollKnobNine);
        scrollStyle.hScroll = new NinePatchDrawable(hScrollNine);

        ScrollPane scrollPane = new ScrollPane(defineLevelsTable(), scrollStyle);

        // Add values
        container.add(selectLevelLabel);
        container.row();
        container.add(scrollPane).top().maxHeight(SCROLL_PANE_MAX_HEIGHT).width(CONTAINER_WIDTH);

        // Adds table to stage
        addActor(container);
    }

    private Table defineLevelsTable() {
        // UI assets
        AssetScene2d assetScene2d = Assets.getInstance().getScene2d();

        // I18n
        I18NBundle i18NGameThreeBundle = Assets.getInstance().getI18NGameThree().getI18NGameThreeBundle();

        // Set table structure
        Table table = new Table();

        // Debug lines
        table.setDebug(DebugConstants.DEBUG_LINES);

        // Personal fonts
        Label.LabelStyle labelStyleNormal = new Label.LabelStyle();
        labelStyleNormal.font = Assets.getInstance().getFonts().getDefaultNormal();

        // Add values
        int level;
        Label levelLabel;
        Table levelTable;
        TextureRegion star = assetScene2d.getStar();
        TextureRegion emptyStar = assetScene2d.getEmptyStar();
        GameSettings prefs = GameSettings.getInstance();
        for (LevelState levelState : prefs.getLevels()) {
            table.row();
            level = levelState.getLevel();
            levelLabel = new Label(LevelFactory.getLevelName(level), labelStyleNormal);
            levelTable = getStarsTable(levelLabel, star, emptyStar, levelState.getFinalStars());
            table.add(levelTable);

            // Events
            levelTable.addListener(UIFactory.screenNavigationListener(ScreenEnum.PLAY_GAME, ScreenTransitionEnum.SLIDE_LEFT, level));
        }

        if (prefs.isGameComplete()) {
            table.row();
            TypingLabelWorkaround grandFinaleLabel = new TypingLabelWorkaround(i18NGameThreeBundle.format("selectLevel.grandFinale"), labelStyleNormal);
            table.add(grandFinaleLabel).padTop(AbstractScreen.PAD);

            // Events
            grandFinaleLabel.addListener(UIFactory.screenNavigationListener(ScreenEnum.GRAND_FINALE, ScreenTransitionEnum.SLICE_UP_DOWN_80));
        }

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

    private Table getStarsTable(Label levelLabel, TextureRegion star, TextureRegion emptyStar, int stars) {
        Stack stack = new Stack();
        Table starsTable = new Table();
        starsTable.center();
        levelLabel.setAlignment(Align.center);
        Image image;
        if (stars > 0) {
            for (int i = 1; i <= 3; i++) {
                image = new Image();
                image.setDrawable(new TextureRegionDrawable(i <= stars ? star : emptyStar));
                image.setAlign(Align.bottom);
                image.setScaling(Scaling.fit);
                starsTable.add(image).size(STAR_WIDTH, STAR_HEIGHT);
            }
            stack.add(starsTable);
        }
        stack.add(levelLabel);

        Table table = new Table();
        starsTable.setDebug(DebugConstants.DEBUG_LINES);
        table.add(stack).size(STARS_TABLE_WIDTH, STAR_HEIGHT);

        return table;
    }
}
