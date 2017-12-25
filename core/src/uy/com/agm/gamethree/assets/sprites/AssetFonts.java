package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetFonts {
    private static final String TAG = AssetFonts.class.getName();

    public final BitmapFont defaultSmall;
    public final BitmapFont defaultNormal;
    public final BitmapFont defaultBig;

    public AssetFonts() {
            // create three fonts using Libgdx's built-in 15px bitmap font
            defaultSmall = new BitmapFont(Gdx.files.internal("fonts/Untitled-export.fnt"), false);
            defaultNormal = new BitmapFont(Gdx.files.internal("fonts/Untitled-export.fnt"), false);
            defaultBig = new BitmapFont(Gdx.files.internal("fonts/Untitled-export.fnt"), false);
            // set font sizes
            defaultSmall.getData().setScale(0.3f);
            defaultNormal.getData().setScale(0.5f);
            defaultBig.getData().setScale(1.5f);
            // enable linear texture filtering for smooth fonts
            defaultSmall.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            defaultNormal.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            defaultBig.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }
}

