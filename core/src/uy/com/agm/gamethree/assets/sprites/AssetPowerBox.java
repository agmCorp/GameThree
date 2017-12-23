package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetPowerBox {
    private static final String TAG = AssetPowerBox.class.getName();

    public final TextureRegion powerBoxStand;
    public final TextureRegion powerBoxDamagedLittle;
    public final TextureRegion powerBoxDamagedMedium;
    public final TextureRegion powerBoxDamagedHard;

    public AssetPowerBox(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        powerBoxStand = atlas.findRegion("powerBox", 1);
        powerBoxDamagedLittle = atlas.findRegion("powerBox", 2);
        powerBoxDamagedMedium = atlas.findRegion("powerBox", 3);
        powerBoxDamagedHard = atlas.findRegion("powerBox", 4);
    }
}
