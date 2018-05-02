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

    private Drawable knobBeforeWarningColor;
    private Drawable backgroundColor;
    private Drawable knobColor;
    private Drawable knobBeforeColor;

    private int warningBefore;
    private int currentEnergy;
    private int fullEnergy;

    public HealthBar() {
        super(MIN, MAX, STEP, false, new ProgressBarStyle());

        this.knobBeforeWarningColor = getColoredDrawable(WIDTH, HEIGHT, Color.YELLOW);
        this.backgroundColor = getColoredDrawable(WIDTH, HEIGHT, Color.RED);
        this.knobColor = getColoredDrawable(0, HEIGHT, Color.GREEN);
        this.knobBeforeColor = getColoredDrawable(WIDTH, HEIGHT, Color.GREEN);

        this.warningBefore = 0;
        this.currentEnergy = 0;
        this.fullEnergy = 0;

        setHeight(HEIGHT);
        setAnimateDuration(ANIMATION_DURATION);

        setFull();
    }

    private void setFull() {
        setDefaultColors();
        setValue(MAX);
    }

    private void setDefaultColors() {
        getStyle().background = backgroundColor;
        getStyle().knob = knobColor;
        getStyle().knobBefore = knobBeforeColor;
    }

    private Drawable getColoredDrawable(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();
        return drawable;
    }

    public void setInitialEnergy(int warningBefore, int energy) {
        this.warningBefore = Math.min(warningBefore, energy);
        this.currentEnergy = energy;
        this.fullEnergy = energy;
        setFull();
    }

    public void decrease() {
        currentEnergy--;
        if (currentEnergy > 0) {
            setValue(getValue() - MAX / fullEnergy);
            if (currentEnergy < warningBefore) {
                getStyle().knobBefore = knobBeforeWarningColor;
            }
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
