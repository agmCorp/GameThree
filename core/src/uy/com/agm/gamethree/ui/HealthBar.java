package uy.com.agm.gamethree.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import uy.com.agm.gamethree.game.Constants;

/**
 * Created by amorales on 1/2/2018.
 */

public class HealthBar extends ProgressBar {
    private int currentEnergy;
    private int fullEnergy;

    public HealthBar() {
        super(Constants.HEALTHBAR_MIN, Constants.HEALTHBAR_MAX, Constants.HEALTHBAR_STEP, false, new ProgressBarStyle());

        getStyle().background = getColoredDrawable(Constants.HEALTHBAR_WIDTH, Constants.HEALTHBAR_HEIGHT, Color.RED);
        getStyle().knob = getColoredDrawable(0, Constants.HEALTHBAR_HEIGHT, Color.GREEN);
        getStyle().knobBefore = getColoredDrawable(Constants.HEALTHBAR_WIDTH, Constants.HEALTHBAR_HEIGHT, Color.GREEN);

        setHeight(Constants.HEALTHBAR_HEIGHT);
        setAnimateDuration(Constants.HEALTHBAR_ANIMATION_DURATION);
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

    // todo DISPOSE DE LAS TEXTURAS!!! y definir constantes

    public void decrease() {
        currentEnergy--;
        if (currentEnergy > 0) {
            setValue(getValue() - Constants.HEALTHBAR_MAX / fullEnergy);
        } else {
            setValue(0);
        }
    }

    @Override
    public float getPrefWidth() {
        // WA: Impossible to set 'width' on an horizontal ProgressBar using size, resize or setWidth
        return (float) Constants.HEALTHBAR_WIDTH;
    }

    public void setFull() {
        setValue(Constants.HEALTHBAR_MAX);
    }
}
