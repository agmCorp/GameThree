package uy.com.agm.gamethree.screens.transitions;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by AGM on 10/02/2018.
 */

public interface ScreenTransition {
    public float getDuration();
    public abstract void render(SpriteBatch batch, Texture currScreen, Texture nextScreen, float alpha);
}
