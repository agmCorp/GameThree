package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;

import uy.com.agm.gamethree.game.Constants;

/**
 * Created by AGM on 1/20/2018.
 */

public class AssetMaps {
    private static final String TAG = AssetMaps.class.getName();

    private TiledMap mapLevelOne;

    public AssetMaps(AssetManager am) {
        mapLevelOne = am.get(Constants.MAP_FILE_LEVEL_ONE);
        if (Constants.HIDE_BACKGROUND) {
            MapLayer floor = mapLevelOne.getLayers().get("floor");
            mapLevelOne.getLayers().remove(floor);
        }
    }

    public TiledMap getMapLevelOne() {
        return mapLevelOne;
    }
}
