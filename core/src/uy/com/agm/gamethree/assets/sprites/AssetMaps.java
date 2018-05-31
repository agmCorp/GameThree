package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.game.DebugConstants;

/**
 * Created by AGM on 1/20/2018.
 */

public class AssetMaps {
    private static final String TAG = AssetMaps.class.getName();

    private TiledMap mapLevelOne;
    private TiledMap mapLevelTwo;
    private TiledMap mapLevelThree;
    private TiledMap mapLevelFour;

    public AssetMaps(AssetManager am) {
        mapLevelOne = getMap(am, Assets.MAP_FILE_LEVEL_ONE);
        mapLevelTwo = getMap(am, Assets.MAP_FILE_LEVEL_TWO);
        mapLevelThree = getMap(am, Assets.MAP_FILE_LEVEL_THREE);
        mapLevelFour = getMap(am, Assets.MAP_FILE_LEVEL_FOUR);
    }

    private TiledMap getMap(AssetManager am, String mapFile) {
        TiledMap map = am.get(mapFile);
        if (DebugConstants.HIDE_BACKGROUND) {
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

    public TiledMap getMapLevelThree() {
        return mapLevelThree;
    }

    public TiledMap getMapLevelFour() {
        return mapLevelFour;
    }
}
