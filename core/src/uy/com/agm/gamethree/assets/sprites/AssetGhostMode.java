package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetGhostMode {
    public final TextureRegion ghostModeStand;
    public final Animation ghostModeAnimation;

    public AssetGhostMode(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        ghostModeStand = atlas.findRegion("ghostMode", 1);

        // Animation
        regions = atlas.findRegions("ghostMode");
        ghostModeAnimation = new Animation(1.0f / 20.0f, regions);
        regions.clear();
    }
}