package uy.com.agm.gamethree.screens.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.screens.AbstractScreen;
import uy.com.agm.gamethree.tools.AudioManager;

/**
 * Created by AGM on 1/18/2018.
 */

public class UIFactory {
    private static final String TAG = UIFactory.class.getName();

    public static InputListener screenNavigationListener(final ScreenEnum dstScreen, final Object... params) {
        return
                new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        Actor actor = event.getTarget();
                        if (actor instanceof Label) {
                            actor.setColor(Color.WHITE); // Default
                        }

                        // Audio FX
                        AudioManager.getInstance().stopSound();
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());

                        // Display screen
                        ScreenManager.getInstance().showScreen(dstScreen, params);
                    }

                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        Actor actor = event.getTarget();
                        if (actor instanceof Label) {
                            actor.setColor(AbstractScreen.COLOR_LABEL_PRESSED);
                        }
                        return true;
                    }
                };
    }
}