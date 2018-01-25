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
    private TiledMap mapLevelTwo;

    public AssetMaps(AssetManager am) {
        mapLevelOne = getMap(am, Constants.MAP_FILE_LEVEL_ONE);
        mapLevelTwo = getMap(am, Constants.MAP_FILE_LEVEL_TWO);
    }

    private TiledMap getMap(AssetManager am, String mapFile) {
        TiledMap map = am.get(mapFile);
        if (Constants.HIDE_BACKGROUND) {
            MapLayer floor = map.getLayers().get("floor");
            map.getLayers().remove(floor);
        }
        return map;
    }

    public TiledMap getMapLevelOne() {
        return mapLevelOne;
    }

    public TiledMap getMapLevelTwo() {
        return mapLevelTwo;
    }
}
