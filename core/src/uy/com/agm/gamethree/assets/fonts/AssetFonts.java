package uy.com.agm.gamethree.assets.fonts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Disposable;


/**
 * Created by AGM on 12/22/2017.
 */

public class AssetFonts implements Disposable {
    private static final String TAG = AssetFonts.class.getName();

    // Constants
    private static final float FONT_SMALL = 0.3f;
    private static final float FONT_NORMAL = 0.5f;
    private static final float FONT_BIG = 0.9f;
    private static final float FONT_GRAND_FINALE = 0.4f;
    private static final String FONT_FILE = "fonts/fonts.fnt";

    private BitmapFont defaultSmall;
    private BitmapFont defaultNormal;
    private BitmapFont defaultBig;
    private BitmapFont defaultGrandFinale;

    public AssetFonts() {
        // Create four fonts using a personal bitmap font
        defaultSmall = new BitmapFont(Gdx.files.internal(FONT_FILE), false);
        defaultNormal = new BitmapFont(Gdx.files.internal(FONT_FILE), false);
        defaultBig = new BitmapFont(Gdx.files.internal(FONT_FILE), false);
        defaultGrandFinale = new BitmapFont(Gdx.files.internal(FONT_FILE), false);

        // Set font sizes
        defaultSmall.getData().setScale(FONT_SMALL);
        defaultNormal.getData().setScale(FONT_NORMAL);
        defaultBig.getData().setScale(FONT_BIG);
        defaultGrandFinale.getData().setScale(FONT_GRAND_FINALE);

        // Enable linear texture filtering for smooth fonts
        defaultSmall.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        defaultNormal.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        defaultBig.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        defaultGrandFinale.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public BitmapFont getDefaultSmall() {
        return defaultSmall;
    }

    public BitmapFont getDefaultNormal() {
        return defaultNormal;
    }

    public BitmapFont getDefaultBig() {
        return defaultBig;
    }

    public BitmapFont getDefaultGrandFinale() {
        return defaultGrandFinale;
    }

    @Override
    public void dispose() {
        defaultSmall.dispose();
        defaultNormal.dispose();
        defaultBig.dispose();
        defaultGrandFinale.dispose();
    }
}

