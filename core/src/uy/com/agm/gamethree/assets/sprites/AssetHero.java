package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetHero {
    public final TextureRegion heroStand;
    public final Animation heroMovingUp;
    public final Animation heroMovingDown;

    public AssetHero(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        heroStand = atlas.findRegion("heroUp", 1);

        // Animation
        regions = atlas.findRegions("heroUp");
        heroMovingUp = new Animation(0.5f / 6.0f, regions);
        regions.clear();

        // Animation
        regions = atlas.findRegions("heroDown");
        heroMovingDown = new Animation(0.5f / 6.0f, regions);
    }
}
