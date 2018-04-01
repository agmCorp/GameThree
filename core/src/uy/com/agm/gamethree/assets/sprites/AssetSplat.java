package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetSplat {
    private static final String TAG = AssetSplat.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)|
    public static final float ENEMY_SPLAT_WIDTH_METERS = 100.0f * 0.6f / PlayScreen.PPM;
    public static final float ENEMY_SPLAT_HEIGHT_METERS = 100.0f * 0.6f / PlayScreen.PPM;
    public static final int ENEMY_SPLAT_MAX_TEXTURES = 16;
    public static final float FINAL_ENEMY_SPLAT_WIDTH_METERS = 190.0f * 1.0f / PlayScreen.PPM;
    public static final float FINAL_ENEMY_SPLAT_HEIGHT_METERS = 190.0f * 1.0f / PlayScreen.PPM;
    public static final int FINAL_ENEMY_SPLAT_MAX_TEXTURES = 4;
    private Array<TextureRegion> enemySplats;
    private Array<TextureRegion> finalEnemySplats;

    public AssetSplat(TextureAtlas atlas) {
        enemySplats = new Array<TextureRegion>();
        for(int i = 1; i <= ENEMY_SPLAT_MAX_TEXTURES; i++) {
            enemySplats.add(atlas.findRegion("enemySplat", i));
        }
        finalEnemySplats = new Array<TextureRegion>();
        for(int i = 1; i <= FINAL_ENEMY_SPLAT_MAX_TEXTURES; i++) {
            finalEnemySplats.add(atlas.findRegion("finalEnemySplat", i));
        }
    }

    public TextureRegion getRandomEnemySplat() {
        return enemySplats.get(MathUtils.random(0, ENEMY_SPLAT_MAX_TEXTURES - 1));
    }

    public TextureRegion getRandomFinalEnemySplat() {
        return finalEnemySplats.get(MathUtils.random(0, FINAL_ENEMY_SPLAT_MAX_TEXTURES - 1));
    }
}