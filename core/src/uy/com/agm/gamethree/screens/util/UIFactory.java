package uy.com.agm.gamethree.screens.util;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.tools.AudioManager;

/**
 * Created by AGM on 1/18/2018.
 */

public class UIFactory {
    private static final String TAG = UIFactory.class.getName();

    // Constants
    public static final int TIMER_LEVEL_ONE = 400;
    public static final int TIMER_LEVEL_TWO = 350;

    public static InputListener createListener(final ScreenEnum dstScreen, final Object... params) {
        return
                new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x,
                                             float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().stopSound();
                        AudioManager.getInstance().play(Assets.getInstance().getSounds().getClick());

                        // Display screen
                        ScreenManager.getInstance().showScreen(dstScreen, params);
                        return false;
                    }
                };
    }
}