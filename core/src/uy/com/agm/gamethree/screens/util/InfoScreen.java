package uy.com.agm.gamethree.screens.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Scaling;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.scene2d.AssetLetsGo;
import uy.com.agm.gamethree.assets.scene2d.AssetScene2d;
import uy.com.agm.gamethree.assets.scene2d.AssetStageFailed;
import uy.com.agm.gamethree.assets.scene2d.AssetVictory;
import uy.com.agm.gamethree.game.DebugConstants;
import uy.com.agm.gamethree.game.GameSettings;
import uy.com.agm.gamethree.screens.AbstractScreen;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.DynamicHelpDef;
import uy.com.agm.gamethree.tools.LevelFactory;
import uy.com.agm.gamethree.widget.AnimatedImage;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by AGM on 1/18/2018.
 *
 */

public class InfoScreen extends AbstractScreen {
    private static final String TAG = InfoScreen.class.getName();

    /*
     * Displays a message, image or animation in the center of the screen in a mutually exclusive manner.
     * Display types:
     *  - Normal: Always visible until hideInfo() is called
     *  - Temporary: Just for a few seconds
     *  - Modal: Force user interaction
     */

    // Constants
    private static final float BUTTONS_PAD = 65.0f;
    private static final float RED_FLASH_TIME = 0.1f;
    private static final float LIGHT_RED_FLASH_ALPHA = 0.2f;
    private static final float LETS_GO_TIME = 1.3f;
    private static final float ANIMATION_DURATION = 1.0f;

    // InfoScreen state
    private enum InfoScreenState {
        PAUSED, RUNNING
    }
    private InfoScreenState infoScreenState;

    private PlayScreen screen;
    private int level;

    // Track help screens depending on the object's class name
    private ObjectMap<String, DynamicHelpDef> dynamicHelp;

    private enum MessageSize {
        BIG, NORMAL, SMALL
    }

    private I18NBundle i18NGameThreeBundle;
    private Label.LabelStyle labelStyleBig;
    private Label.LabelStyle labelStyleNormal;
    private Label.LabelStyle labelStyleSmall;

    // Animations
    private Animation stageFailedAnimation;
    private Animation victoryAnimation;
    private Animation letsGoAnimation;

    private Table centerTable;
    private Label messageLabel;
    private AnimatedImage animatedImage;
    private Image image;
    private float overlayTime;
    private float overlaySeconds;
    private boolean overlayTemporaryAnimation;
    private boolean overlayTemporaryImage;
    private boolean overlayTemporaryMessage;

    private Table buttonsTable;
    private ImageButton gotIt;

    private Stack stack;
    private Cell stackCell;

    private TextureRegion redFlash;
    private TextureRegion lightRedFlash;

    public InfoScreen(PlayScreen screen, Integer level) {
        super();

        // Define tracking variables
        this.screen = screen;
        this.level = level;
        overlayTime = 0;
        overlaySeconds = 0;
        overlayTemporaryAnimation = false;
        overlayTemporaryImage = false;
        overlayTemporaryMessage = false;

        // I18n
        i18NGameThreeBundle = Assets.getInstance().getI18NGameThree().getI18NGameThreeBundle();

        // Track help screens
        dynamicHelp = LevelFactory.getDynamicHelp(level);

        // Personal fonts
        labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.getInstance().getFonts().getDefaultBig();

        labelStyleNormal = new Label.LabelStyle();
        labelStyleNormal.font = Assets.getInstance().getFonts().getDefaultNormal();

        labelStyleSmall = new Label.LabelStyle();
        labelStyleSmall.font = Assets.getInstance().getFonts().getDefaultSmall();

        // Animations
        AssetScene2d assetScene2d = Assets.getInstance().getScene2d();
        stageFailedAnimation = assetScene2d.getStageFailed().getStageFailedAnimation();
        victoryAnimation = assetScene2d.getVictory().getVictoryAnimation();
        letsGoAnimation = assetScene2d.getLetsGo().getStageLetsGoAnimation();

        // Red flash Texture
        Pixmap pixmap = new Pixmap(V_WIDTH, V_HEIGHT, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fill();
        redFlash = new TextureRegion(new Texture(pixmap));
        pixmap.dispose();

        // Light red flash Texture
        pixmap = new Pixmap(V_WIDTH, V_HEIGHT, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 0, 0, LIGHT_RED_FLASH_ALPHA);
        pixmap.fill();
        lightRedFlash = new TextureRegion(new Texture(pixmap));
        pixmap.dispose();

        // InfoScreen running
        infoScreenState = InfoScreenState.RUNNING;
    }

    private void defineCenterTable() {
        // Define a new table used to display a message
        centerTable = new Table();

        // Debug lines
        centerTable.setDebug(DebugConstants.DEBUG_LINES);

        // Center-Align table
        centerTable.center();

        // Make the table fill the entire stage
        centerTable.setFillParent(true);

        // Define a label based on labelStyle
        messageLabel = new Label("MESSAGE", labelStyleSmall);
        messageLabel.setAlignment(Align.center);

        // Define animatedImage and image
        animatedImage = new AnimatedImage();
        animatedImage.setAlign(Align.center);
        image = new Image();
        image.setAlign(Align.center);

        // Add values
        stack = new Stack();
        stack.add(animatedImage);
        stack.add(image);
        stack.add(messageLabel);
        stackCell = centerTable.add(stack);

        // Add our table to the stage
        addActor(centerTable);

        // Initially hidden
        messageLabel.setVisible(false);
        animatedImage.setVisible(false);
        image.setVisible(false);
        centerTable.setVisible(false);
    }

    private void defineButtonsTable() {
        // UI assets
        AssetScene2d assetScene2d = Assets.getInstance().getScene2d();

        // Define a new table used to display buttons
        buttonsTable = new Table();

        // Debug lines
        buttonsTable.setDebug(DebugConstants.DEBUG_LINES);

        // Bottom-Align table
        buttonsTable.bottom().padBottom(BUTTONS_PAD);

        // Make the container fill the entire stage
        buttonsTable.setFillParent(true);

        // Define button
        gotIt = new ImageButton(new TextureRegionDrawable(assetScene2d.getGotIt()),
                new TextureRegionDrawable(assetScene2d.getGotItPressed()));

        // Add values
        buttonsTable.add(gotIt);

        gotIt.addListener(
                new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());
                        gotIt();
                    }

                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });

        // Add the table to the stage
        addActor(buttonsTable);

        // Initially hidden
        gotIt.setVisible(false);
    }

    private void gotIt() {
        hideInfo();
        gotIt.setVisible(false);
        screen.getDimScreen().showButtons();
        screen.setPlayScreenStateRunning();
    }

    @Override
    public void buildStage() {
        defineCenterTable();
        defineButtonsTable();
    }

    // ----------- Message functions

    public void showMessage(String message, MessageSize messageSize) {
        messageLabel.setText(message);
        switch (messageSize) {
            case BIG:
                messageLabel.setStyle(labelStyleBig);
                break;
            case NORMAL:
                messageLabel.setStyle(labelStyleNormal);
                break;
            case SMALL:
                messageLabel.setStyle(labelStyleSmall);
                break;
            default:
                break;
        }
        messageLabel.setVisible(true);
        animatedImage.setVisible(false);
        image.setVisible(false);
        centerTable.setVisible(true);
    }

    public void showMessage(String message, MessageSize messageSize, float seconds) {
        overlayTime = 0;
        overlaySeconds = seconds;
        overlayTemporaryMessage = true;
        overlayTemporaryImage = false;
        overlayTemporaryAnimation = false;
        showMessage(message, messageSize);
    }

    public void showModalMessage(String message, MessageSize messageSize) {
        gotIt.setVisible(true);
        showMessage(message, messageSize);
        screen.getDimScreen().hideButtons();
        screen.setPlayScreenStatePaused(false);
    }

    public boolean isMessageVisible() {
        return messageLabel.isVisible();
    }

    // ----------- Image functions

    public void  showImage(TextureRegion textureRegion) {
        showImage(textureRegion, textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
    }

    public void showImage(TextureRegion textureRegion, float width, float height) {
        setImage(textureRegion, width, height);

        image.setVisible(true);
        messageLabel.setVisible(false);
        animatedImage.setVisible(false);
        centerTable.setVisible(true);
    }

    private void setImage(TextureRegion textureRegion, float width, float height) {
        stackCell.size(width, height);
        centerTable.pack();
        // WA: Using new each time is the only way I found to set the correct size
        // This could be a memory leak
        image.setDrawable(new TextureRegionDrawable(textureRegion));
        image.setScaling(Scaling.fit);
    }

    public void showImage(TextureRegion textureRegion, float seconds) {
        showImage(textureRegion, textureRegion.getRegionWidth(), textureRegion.getRegionHeight(), seconds);
    }

    public void showImage(TextureRegion textureRegion, float width, float height, float seconds) {
        overlayTime = 0;
        overlaySeconds = seconds;
        overlayTemporaryImage = true;
        overlayTemporaryAnimation = false;
        overlayTemporaryMessage = false;
        showImage(textureRegion, width, height);
    }

    public void showModalImage(TextureRegion textureRegion) {
        showModalImage(textureRegion, textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
    }

    public void showModalImage(TextureRegion textureRegion, float width, float height) {
        gotIt.setVisible(true);
        showImage(textureRegion, width, height);
        screen.getDimScreen().hideButtons();
        screen.setPlayScreenStatePaused(false);
    }

    public boolean isImageVisible() {
        return image.isVisible();
    }

    // ----------- Animation functions

    public void showAnimation(Animation animation) {
        TextureRegion textureRegion = (TextureRegion) animation.getKeyFrame(0, false);
        showAnimation(animation, textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
    }

    public void showAnimation(Animation animation, float width, float height) {
        stackCell.size(width, height);
        centerTable.pack();
        animatedImage.setAnimation(animation);

        animatedImage.setVisible(true);
        image.setVisible(false);
        messageLabel.setVisible(false);
        centerTable.setVisible(true);
    }

    public void showAnimation(Animation animation, float seconds) {
        TextureRegion textureRegion = (TextureRegion) animation.getKeyFrame(0, false);
        showAnimation(animation, textureRegion.getRegionWidth(), textureRegion.getRegionHeight(), seconds);
    }

    public void showAnimation(Animation animation,  float width, float height, float seconds) {
        overlayTime = 0;
        overlaySeconds = seconds;
        overlayTemporaryAnimation = true;
        overlayTemporaryImage = false;
        overlayTemporaryMessage = false;
        showAnimation(animation, width, height);
    }

    public void showModalAnimation(Animation animation) {
        TextureRegion textureRegion = (TextureRegion) animation.getKeyFrame(0, false);
        showModalAnimation(animation, textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
    }

    public void showModalAnimation(Animation animation, float width, float height) {
        gotIt.setVisible(true);
        showAnimation(animation, width, height);
        screen.getDimScreen().hideButtons();
        screen.setPlayScreenStatePaused(false);
    }

    public boolean isAnimationVisible() {
        return animatedImage.isVisible();
    }

    // ----------- General functions

    public void hideInfo() {
        clearTransition();
        messageLabel.setVisible(false);
        image.setVisible(false);
        animatedImage.setVisible(false);
        centerTable.setVisible(false);
    }

    public void hideModalInfo() {
        hideInfo();
        gotIt.setVisible(false);
        screen.getDimScreen().showButtons();
        screen.setPlayScreenStateRunning();
    }

    public boolean isModalVisible() {
        return gotIt.isVisible();
    }

    public void disableModal() {
        gotIt.setTouchable(Touchable.disabled);
    }

    public void enableModal() {
        gotIt.setTouchable(Touchable.enabled);
    }

    // ----------- Specialized functions

    public void justBeginning() {
        screen.getDimScreen().hideButtons();
        screen.setPlayScreenStatePaused(false);
    }

    public void startPlaying() {
        screen.getDimScreen().showButtons();
        screen.setPlayScreenStateRunning();
    }

    public void showGameControllersHelp() {
        setNewTransition();
        if (GameSettings.getInstance().isManualShooting()) {
            showModalImage(Assets.getInstance().getScene2d().getHelpInitialManual());
        } else {
            showModalImage(Assets.getInstance().getScene2d().getHelpInitialAutomatic());
        }
    }

    public void showHurryUpMessage() {
        showMessage(i18NGameThreeBundle.format("infoScreen.hurryUp"), MessageSize.BIG);
    }

    public void showTimeIsUpMessage() {
        setNewTransition();
        showMessage(i18NGameThreeBundle.format("infoScreen.timeIsUp"), MessageSize.BIG);
    }

    public void showStageFailedAnimation() {
        showAnimation(stageFailedAnimation, AssetStageFailed.WIDTH_PIXELS, AssetStageFailed.HEIGHT_PIXELS);
    }

    public void showVictoryAnimation() {
        showAnimation(victoryAnimation, AssetVictory.WIDTH_PIXELS, AssetVictory.HEIGHT_PIXELS);
    }

    public void showFightMessage() {
        setNewTransition();
        showMessage(i18NGameThreeBundle.format("infoScreen.fight"), MessageSize.BIG);
    }

    public void showChallengeBeginHelp(TextureRegion helpImage) {
        setNewTransition();
        showModalImage(helpImage);
    }

    public void showRedFlash() {
        showImage(redFlash, RED_FLASH_TIME);
    }

    public void showModalRedFlashHelp() {
        if (!isModalVisible()) {
            setNewTransition();
            showLightRedFlash(i18NGameThreeBundle.format("infoScreen.redFlashHelp"));
        }
    }

    public void showModalLoseLifeWarning() {
        if (!isModalVisible()) {
            setNewTransition();
            showLightRedFlash(i18NGameThreeBundle.format("infoScreen.loseLifeWarning"));
        }
    }

    private void showLightRedFlash(String text) {
        gotIt.setVisible(true);
        setImage(lightRedFlash, lightRedFlash.getRegionWidth(), lightRedFlash.getRegionHeight());
        image.setVisible(true);
        messageLabel.setText(text);
        messageLabel.setStyle(labelStyleNormal);
        messageLabel.setVisible(true);
        animatedImage.setVisible(false);
        centerTable.setVisible(true);
        screen.getDimScreen().hideButtons();
        screen.setPlayScreenStatePaused(false);
    }

    public boolean isRedFlashVisible() {
        return isImageVisible() && ((TextureRegionDrawable) image.getDrawable()).getRegion() == redFlash;
    }

    public void showLetsGo() {
        showAnimation(letsGoAnimation, AssetLetsGo.WIDTH_PIXELS, AssetLetsGo.HEIGHT_PIXELS, LETS_GO_TIME);
    }

    // Show help screens depending on the object's class name
    public void showDynamicHelp(String className, TextureRegion helpImage) {
        if (dynamicHelp.containsKey(className)){
            DynamicHelpDef dynamicHelpDef = dynamicHelp.get(className);
            setNewTransition();
            if (dynamicHelpDef.isModal()) {
                showModalImage(helpImage);
            } else {
                showImage(helpImage, dynamicHelpDef.getSeconds());
            }
            dynamicHelp.remove(className);
        }
    }

    private void clearTransition() {
        getRoot().clearActions();
    }

    private void setNewTransition() {
        clearTransition();
        getRoot().setY(AbstractScreen.V_HEIGHT);
        getRoot().setTouchable(Touchable.disabled);
        getRoot().addAction(sequence(moveBy(0, -AbstractScreen.V_HEIGHT, ANIMATION_DURATION, Interpolation.bounceOut), run(new Runnable() {
            public void run () {
                getRoot().setTouchable(Touchable.enabled);
            }
        })));
    }

    // ----------- InfoScreen logic functions

    public void update(float dt) {
        overlayTemporaryAnimation(dt);
        overlayTemporaryImage(dt);
        overlayTemporaryMessage(dt);
    }

    private void overlayTemporaryAnimation(float dt) {
        if (overlayTemporaryAnimation) {
            overlayTime += dt;
            if (overlayTime >= overlaySeconds) {
                overlayTemporaryAnimation = false;
                overlayTime = 0;
                hideInfo();
            }
        }
    }

    private void overlayTemporaryImage(float dt) {
        if (overlayTemporaryImage) {
            overlayTime += dt;
            if (overlayTime >= overlaySeconds) {
                overlayTemporaryImage = false;
                overlayTime = 0;
                hideInfo();
            }
        }
    }

    private void overlayTemporaryMessage(float dt) {
        if (overlayTemporaryMessage) {
            overlayTime += dt;
            if (overlayTime >= overlaySeconds) {
                overlayTemporaryMessage = false;
                overlayTime = 0;
                hideInfo();
            }
        }
    }

    public void setInfoScreenStatePaused() {
        this.infoScreenState = InfoScreenState.PAUSED;
    }

    public boolean isInfoScreenStateRunning() {
        return infoScreenState == InfoScreenState.RUNNING;
    }

    public void setInfoScreenStateRunning(){
        this.infoScreenState = InfoScreenState.RUNNING;
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void render(float delta) {
        // Calling to Stage methods
        if (isInfoScreenStateRunning()) {
            super.act(delta);  // Don't animate if InfoScreen is on pause
        }
        super.draw();
    }
}
