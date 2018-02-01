package uy.com.agm.gamethree.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by amorales on 1/2/2018.
 */

public class HealthBar extends ProgressBar {
    private int currentEnergy;
    private int fullEnergy;

    public HealthBar(int width, int height, int energy) {
        super(0.0f, 100.0f, 1.0f, false, new ProgressBarStyle());
        getStyle().background = getColoredDrawable(width, height, Color.RED);
        getStyle().knob = getColoredDrawable(0, height, Color.GREEN);
        getStyle().knobBefore = getColoredDrawable(width, height, Color.GREEN);
        setWidth(width);
        setHeight(height);
        setValue(100.0f);
        setAnimateDuration(0.25f);
        currentEnergy = energy;
        fullEnergy = energy;
    }

    // Creates an image of determined size filled with determined color.
    private Drawable getColoredDrawable(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();
        return drawable;
    }

    // DISPOSE DE LAS TEXTURAS!!!

    public void decrease() {
        currentEnergy--;
        if (currentEnergy > 0) {
            setValue(getValue() - MathUtils.round(100.0f / fullEnergy));
        } else {
            setValue(0);
        }
    }
}
