package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetEnergyBall {
    public final TextureRegion energyBallStand;
    public final Animation energyBallAnimation;

    public AssetEnergyBall(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        energyBallStand = atlas.findRegion("energyBall", 1);

        // Animation
        regions = atlas.findRegions("energyBall");
        energyBallAnimation = new Animation(0.5f / 9.0f, regions);
        regions.clear();
    }
}
