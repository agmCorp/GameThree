package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by amorales on 23/1/2018.
 */

public class AssetColOne {
    private static final String TAG = AssetColOne.class.getName();

    private TextureRegion colOneStand;
    private Animation colOneAnimation;

    public AssetColOne(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        colOneStand = atlas.findRegion("colOne", 1);

        // Animation
        regions = atlas.findRegions("colOne");
        colOneAnimation = new Animation(0.3f / 4.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getColOneStand() {
        return colOneStand;
    }

    public Animation getColOneAnimation() {
        return colOneAnimation;
    }
}
