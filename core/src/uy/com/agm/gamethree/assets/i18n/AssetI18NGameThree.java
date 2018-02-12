package uy.com.agm.gamethree.assets.i18n;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.I18NBundle;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetI18NGameThree {
    private static final String TAG = AssetI18NGameThree.class.getName();

    private I18NBundle i18NGameThreeBundle;

    public AssetI18NGameThree(AssetManager am) {
        i18NGameThreeBundle = am.get("i18n/I18NGameThreeBundle", I18NBundle.class);
    }

    public I18NBundle getI18NGameThreeBundle() {
        return i18NGameThreeBundle;
    }
}

