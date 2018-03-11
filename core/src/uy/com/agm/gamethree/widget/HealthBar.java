package uy.com.agm.gamethree.widget;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by amorales on 1/2/2018.
 */

public class HealthBar extends ProgressBar {
    private static final String TAG = HealthBar.class.getName();

    // Constants
    private static final int WIDTH = 200;
    private static final int HEIGHT = 10;
    private static final float MIN = 0.0f;
    private static final float MAX = 100.0f;
    private static final float STEP = 0.01f;
    private static final float ANIMATION_DURATION = 0.25f;

    private int currentEnergy;
    private int fullEnergy;

    public HealthBar() {
        super(MIN, MAX, STEP, false, new ProgressBarStyle());

        getStyle().background = getColoredDrawable(WIDTH, HEIGHT, Color.RED);
        getStyle().knob = getColoredDrawable(0, HEIGHT, Color.GREEN);
        getStyle().knobBefore = getColoredDrawable(WIDTH, HEIGHT, Color.GREEN);

        setHeight(HEIGHT);
        setFull();
    }

    public void setInitialEnergy(int energy) {
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

    private void setFull() {
        setValue(MAX);
        setAnimateDuration(ANIMATION_DURATION);
    }

    public void decrease() {
        currentEnergy--;
        if (currentEnergy > 0) {
            setValue(getValue() - MAX / fullEnergy);
        } else {
            setValue(0);
        }
    }

    @Override
    public float getPrefWidth() {
        // WA: Impossible to set 'width' on an horizontal ProgressBar using size, resize or setWidth
        return (float) WIDTH;
    }
}
