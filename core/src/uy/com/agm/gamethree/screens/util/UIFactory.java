package uy.com.agm.gamethree.screens.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.tools.AudioManager;

/**
 * Created by AGM on 1/18/2018.
 */

public class UIFactory {
    private static final String TAG = UIFactory.class.getName();

    public static ImageButton createButton(Texture texture) {
        return
                new ImageButton(
                        new TextureRegionDrawable(
                                new TextureRegion(texture) ) );
    }

    public static InputListener createListener(final ScreenEnum dstScreen, final Object... params) {
        return
                new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x,
                                             float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().play(Assets.getInstance().getSounds().getClick());

                        // Display screen
                        ScreenManager.getInstance().showScreen(dstScreen, params);
                        return false;
                    }
                };
    }
}