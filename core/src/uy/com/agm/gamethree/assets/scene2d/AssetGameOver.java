package uy.com.agm.gamethree.assets.scene2d;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetGameOver {
    private static final String TAG = AssetGameOver.class.getName();

    // Constants (pixels * resizeFactor)
    public static final int WIDTH_PIXELS = MathUtils.round(459.0f * 0.9f);
    public static final int HEIGHT_PIXELS = MathUtils.round(86.0f * 0.9f);

    private TextureRegion gameOverStand;
    private Animation gameOverAnimation;

    public AssetGameOver(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        gameOverStand = atlas.findRegion("gameOver", 1);

        // Animation
        regions = atlas.findRegions("gameOver");
        gameOverAnimation = new Animation(2.0f / 35.0f, regions);
        regions.clear();
    }

    public TextureRegion getGameOverStand() {
        return gameOverStand;
    }

    public Animation getGameOverAnimation() {
        return gameOverAnimation;
    }
}