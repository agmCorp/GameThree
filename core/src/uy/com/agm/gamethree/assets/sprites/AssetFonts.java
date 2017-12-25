package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import uy.com.agm.gamethree.tools.Constants;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetFonts {
    private static final String TAG = AssetFonts.class.getName();

    public final BitmapFont defaultSmall;
    public final BitmapFont defaultNormal;
    public final BitmapFont defaultBig;

    public AssetFonts() {
            // Create three fonts using a personal bitmap font
            defaultSmall = new BitmapFont(Gdx.files.internal("fonts/fonts.fnt"), false);
            defaultNormal = new BitmapFont(Gdx.files.internal("fonts/fonts.fnt"), false);
            defaultBig = new BitmapFont(Gdx.files.internal("fonts/fonts.fnt"), false);
            // Set font sizes
            defaultSmall.getData().setScale(Constants.FONT_SMALL);
            defaultNormal.getData().setScale(Constants.FONT_NORMAL);
            defaultBig.getData().setScale(Constants.FONT_BIG);
            // Enable linear texture filtering for smooth fonts
            defaultSmall.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            defaultNormal.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            defaultBig.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }
}

