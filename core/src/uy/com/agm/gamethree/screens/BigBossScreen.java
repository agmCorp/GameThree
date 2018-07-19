package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.game.DebugConstants;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.ScreenManager;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.widget.LoadingBar;

/**
 * Created by AGM on 12/23/2017.
 */

public class BigBossScreen extends AbstractScreen {
    private static final String TAG = BigBossScreen.class.getName();

    // Constants
    private static final float MSG_PAD_LEFT = 18.0f;
    private static final float MSG_WIDTH = 430.0f;
    private static final float INITIAL_DELAY_SECONDS = 3.0f;
    private static final float FINALE_DURATION_SECONDS = 12.0f;

    private float initialDelayTime;
    private float finaleDelayTime;
    private boolean threat;
    private Animation bigBossHitOneAnimation;
    private Animation bigBossHitTwoAnimation;
    private Animation bigBossIdleAnimation;
    private LoadingBar bigBossActor;

    // basura // TODO: 7/18/2018
    boolean isA = true;
    boolean isB = true;
    boolean isC = true;
    boolean isD = true;
    boolean isF = false;
    float golpes = 0;

    public BigBossScreen() {
        super();
        bigBossHitOneAnimation = Assets.getInstance().getScene2d().getBigBoss().getBigBossHitOneAnimation();
        bigBossHitTwoAnimation = Assets.getInstance().getScene2d().getBigBoss().getBigBossHitTwoAnimation();
        bigBossIdleAnimation = Assets.getInstance().getScene2d().getBigBoss().getBigBossIdleAnimation();
        initialDelayTime = 0;
        finaleDelayTime = 0;
        threat = true;
    }

    @Override
    public void buildStage() {
        // Stop music
        AudioManager.getInstance().stopMusic();

        defineMainTable();
        defineBigBoss();
    }

    private void defineMainTable() {
        // I18n
        I18NBundle i18NGameThreeBundle = Assets.getInstance().getI18NGameThree().getI18NGameThreeBundle();

        // Set table structure
        Table table = new Table();

        // Design
        table.setBackground(new TextureRegionDrawable(Assets.getInstance().getScene2d().getBigBossBackground()));

        // Debug lines
        table.setDebug(DebugConstants.DEBUG_LINES);

        // Top-Align table
        table.top().padTop(AbstractScreen.PAD * 3);

        // Make the table fill the entire stage
        table.setFillParent(true);

        // Personal fonts
        Label.LabelStyle labelStyleGrandFinale = new Label.LabelStyle();
        labelStyleGrandFinale.font = Assets.getInstance().getFonts().getDefaultGrandFinale();

        // Define our labels based on labelStyle
        TypingLabel msgLabel = new TypingLabel(i18NGameThreeBundle.format("bigBoss.threat"), labelStyleGrandFinale);
        msgLabel.setAlignment(Align.center);
        msgLabel.setWrap(true);

        // Add values
        table.add(msgLabel).padLeft(MSG_PAD_LEFT).width(MSG_WIDTH).fill();

        // Adds table to stage
        addActor(table);
    }

    private void defineBigBoss() { // todo
        // Add the loading bar animation
        bigBossActor = new LoadingBar(bigBossIdleAnimation);

        // Place the loading bar at the same spot as the frame
        bigBossActor.setX(37);
        bigBossActor.setY(100);

        addActor(bigBossActor);
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
        if (initialDelayTime >= INITIAL_DELAY_SECONDS && threat) {
            threat = false;

            // Play voice
            AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getThreat());
        } else {
            initialDelayTime += delta;
        }

        finaleDelayTime += delta;
        if (finaleDelayTime >= FINALE_DURATION_SECONDS) {
            // Display screen
            ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
        } else {
            if (finaleDelayTime >= 3 && isA) {
                isA = false;
                bigBossActor.setAnimation(bigBossHitTwoAnimation);
                Gdx.app.debug(TAG, "CAMBIO ANIMACION A HIT 2");
            } else {
                if (finaleDelayTime >= 6 && isB) {
                    isB = false;
                    bigBossActor.setAnimation(bigBossHitOneAnimation);
                    Gdx.app.debug(TAG, "CAMBIO ANIMACION A IDLE");
                } else {
                    if (finaleDelayTime >= 9 && isC) {
                        isC = false;
                        bigBossActor.setAnimation(bigBossIdleAnimation);
                        Gdx.app.debug(TAG, "CAMBIO ANIMACION A IDLE");
                    }
                }
            }
        }

        // goles
        if (finaleDelayTime > 3.8 && isD) {
            isD = false;
            // Play voice
            AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getPum());
            isF = true;
        }

        if (isF && finaleDelayTime <= 9) { // el mismo nueve de arriba
            golpes += delta;
            if (golpes >= 1) {
                golpes = 0;
                // Play voice
                AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getPum());
            }
        }
    }
}
