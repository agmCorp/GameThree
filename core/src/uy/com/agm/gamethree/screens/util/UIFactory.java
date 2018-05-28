package uy.com.agm.gamethree.screens.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.tools.AudioManager;

/**
 * Created by AGM on 1/18/2018.
 */

public class UIFactory {
    private static final String TAG = UIFactory.class.getName();

    public static InputListener createListener(final ScreenEnum dstScreen, final Object... params) {
        return

                new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().stopSound();
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());

                        // Display screen
                        ScreenManager.getInstance().showScreen(dstScreen, params);
                    }

                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                };
    }
}