package uy.com.agm.gamethree.assets.scene2d;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetScene2d {
    private static final String TAG = AssetScene2d.class.getName();

    private TextureRegion sliderBackground;
    private TextureRegion sliderKnob;
    private TextureRegion drag;

    public AssetScene2d(TextureAtlas atlas) {
        sliderBackground = atlas.findRegion("sliderBackground");
        sliderKnob = atlas.findRegion("sliderKnob");
        drag = atlas.findRegion("drag");
    }

    public TextureRegion getSliderBackground() {
        return sliderBackground;
    }

    public TextureRegion getSliderKnob() {
        return sliderKnob;
    }

    public TextureRegion getDrag() {
        return drag;
    }
}
