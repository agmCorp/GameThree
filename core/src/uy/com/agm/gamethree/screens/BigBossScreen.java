package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.game.DebugConstants;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.ScreenManager;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.widget.AnimatedActor;
import uy.com.agm.gamethree.widget.TypingLabelWorkaround;

/**
 * Created by AGM on 12/23/2017.
 */

public class BigBossScreen extends AbstractScreen {
    private static final String TAG = BigBossScreen.class.getName();

    // Constants
    private static final float MSG_WIDTH = 430.0f;
    private static final float BIG_BOSS_X = 37.0f;
    private static final float BIG_BOSS_Y = 100.0f;
    private static final float THREAT_TIME_SECONDS = 3.0f;
    private static final float HIT_TWO_ANIMATION_TIME_SECONDS = 3.0f;
    private static final float FX_HIT_TIME_SECONDS = 3.8f;
    private static final float HIT_INTERVAL_TIME_SECONDS = 1.0f;
    private static final float HIT_ONE_ANIMATION_TIME_SECONDS = 6.0f;
    private static final float IDLE_ANIMATION_TIME_SECONDS = 9.0f;
    private static final float FINALE_DURATION_SECONDS = 12.0f;

    private float timeBeat;
    private float hitTime;
    private Animation bigBossHitOneAnimation;
    private Animation bigBossHitTwoAnimation;
    private Animation bigBossIdleAnimation;
    private AnimatedActor bigBossActor;

    private boolean threat;
    private boolean hitTwoAnimation;
    private boolean hit;
    private boolean hitOneAnimation;
    private boolean idleAnimation;

    public BigBossScreen() {
        super();
        bigBossHitOneAnimation = Assets.getInstance().getScene2d().getBigBoss().getBigBossHitOneAnimation();
        bigBossHitTwoAnimation = Assets.getInstance().getScene2d().getBigBoss().getBigBossHitTwoAnimation();
        bigBossIdleAnimation = Assets.getInstance().getScene2d().getBigBoss().getBigBossIdleAnimation();
        timeBeat = 0;
        hitTime = 0;
        threat = false;
        hitTwoAnimation = false;
        hit = false;
        hitOneAnimation = false;
        idleAnimation =  false;

        // Play music
        AudioManager.getInstance().playMusic(Assets.getInstance().getMusic().getSongBigBoss(), false);
    }

    @Override
    public void buildStage() {
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
        TypingLabelWorkaround msgLabel = new TypingLabelWorkaround(i18NGameThreeBundle.format("bigBoss.threat"), labelStyleGrandFinale);
        msgLabel.setAlignment(Align.center);
        msgLabel.setWrap(true);

        // Add values
        table.add(msgLabel).width(MSG_WIDTH).fill();

        // Adds table to stage
        addActor(table);
    }

    private void defineBigBoss() {
        bigBossActor = new AnimatedActor(bigBossIdleAnimation);
        bigBossActor.setX(BIG_BOSS_X);
        bigBossActor.setY(BIG_BOSS_Y);
        addActor(bigBossActor);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        // Global timer
        timeBeat += delta;

        if (timeBeat >= THREAT_TIME_SECONDS && !threat) {
            threat = true;
            AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getThreat());
        } else if (timeBeat >= HIT_TWO_ANIMATION_TIME_SECONDS && !hitTwoAnimation) {
            hitTwoAnimation = true;
            bigBossActor.setAnimation(bigBossHitTwoAnimation);
        } else if (timeBeat >= FX_HIT_TIME_SECONDS && !hit) {
            hit = true;
            AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getPum());
        } else if (timeBeat >= HIT_ONE_ANIMATION_TIME_SECONDS && !hitOneAnimation) {
            hitOneAnimation = true;
            bigBossActor.setAnimation(bigBossHitOneAnimation);
        } else if (timeBeat >= IDLE_ANIMATION_TIME_SECONDS && !idleAnimation) {
            idleAnimation = true;
            bigBossActor.setAnimation(bigBossIdleAnimation);
        } else if (timeBeat >= FINALE_DURATION_SECONDS) {
            // Return to Menu screen
            ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
        }

        if (hit && timeBeat <= IDLE_ANIMATION_TIME_SECONDS) {
            hitTime += delta;
            if (hitTime >= HIT_INTERVAL_TIME_SECONDS) {
                hitTime = 0;
                AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getPum());
            }
        }
    }

    @Override
    protected void clearScreen() {
        // Clear the screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    protected void goBack() {
        // Nothing to do here
    }
}
