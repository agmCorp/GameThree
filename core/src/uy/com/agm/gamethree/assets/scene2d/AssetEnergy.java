package uy.com.agm.gamethree.assets.scene2d;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetEnergy {
    private static final String TAG = AssetEnergy.class.getName();

    // Constants (pixels * resizeFactor)
    public static final float WIDTH_PIXELS = 100.0f * 1.0f;
    public static final float HEIGHT_PIXELS = 100.0f * 1.0f;

    private TextureRegion energyStand;
    private TextureRegion energy100;
    private TextureRegion energy75;
    private TextureRegion energy50;
    private TextureRegion energy25;
    private TextureRegion energy0;

    public AssetEnergy(TextureAtlas atlas) {
        energyStand = atlas.findRegion("energy100");
        energy100 = atlas.findRegion("energy100");
        energy75 = atlas.findRegion("energy75");
        energy50 = atlas.findRegion("energy50");
        energy25 = atlas.findRegion("energy25");
        energy0 = atlas.findRegion("energy0");
    }

    public TextureRegion getEnergyStand() {
        return energyStand;
    }

    public TextureRegion getEnergy100() {
        return energy100;
    }

    public TextureRegion getEnergy75() {
        return energy75;
    }

    public TextureRegion getEnergy50() {
        return energy50;
    }

    public TextureRegion getEnergy25() {
        return energy25;
    }

    public TextureRegion getEnergy0() {
        return energy0;
    }
}
