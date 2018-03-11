package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetPowerBox {
    private static final String TAG = AssetPowerBox.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 102.0f * 0.4f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 128.0f * 0.4f / PlayScreen.PPM;
    public static final int POWERBOX_MAX_TEXTURES = 14;

    private TextureRegion brickAStand;
    private TextureRegion brickBStand;
    private TextureRegion brickCStand;
    private TextureRegion brickDStand;
    private TextureRegion brickEStand;
    private TextureRegion brickFStand;
    private TextureRegion brickGStand;
    private TextureRegion brickHStand;
    private TextureRegion brickIStand;
    private TextureRegion brickJStand;
    private TextureRegion brickKStand;
    private TextureRegion brickLStand;
    private TextureRegion brickMStand;
    private TextureRegion brickNStand;

    private TextureRegion brickADamagedLittle;
    private TextureRegion brickBDamagedLittle;
    private TextureRegion brickCDamagedLittle;
    private TextureRegion brickDDamagedLittle;
    private TextureRegion brickEDamagedLittle;
    private TextureRegion brickFDamagedLittle;
    private TextureRegion brickGDamagedLittle;
    private TextureRegion brickHDamagedLittle;
    private TextureRegion brickIDamagedLittle;
    private TextureRegion brickJDamagedLittle;
    private TextureRegion brickKDamagedLittle;
    private TextureRegion brickLDamagedLittle;
    private TextureRegion brickMDamagedLittle;
    private TextureRegion brickNDamagedLittle;

    private TextureRegion brickADamagedMedium;
    private TextureRegion brickBDamagedMedium;
    private TextureRegion brickCDamagedMedium;
    private TextureRegion brickDDamagedMedium;
    private TextureRegion brickEDamagedMedium;
    private TextureRegion brickFDamagedMedium;
    private TextureRegion brickGDamagedMedium;
    private TextureRegion brickHDamagedMedium;
    private TextureRegion brickIDamagedMedium;
    private TextureRegion brickJDamagedMedium;
    private TextureRegion brickKDamagedMedium;
    private TextureRegion brickLDamagedMedium;
    private TextureRegion brickMDamagedMedium;
    private TextureRegion brickNDamagedMedium;

    private TextureRegion brickADamagedHard;
    private TextureRegion brickBDamagedHard;
    private TextureRegion brickCDamagedHard;
    private TextureRegion brickDDamagedHard;
    private TextureRegion brickEDamagedHard;
    private TextureRegion brickFDamagedHard;
    private TextureRegion brickGDamagedHard;
    private TextureRegion brickHDamagedHard;
    private TextureRegion brickIDamagedHard;
    private TextureRegion brickJDamagedHard;
    private TextureRegion brickKDamagedHard;
    private TextureRegion brickLDamagedHard;
    private TextureRegion brickMDamagedHard;
    private TextureRegion brickNDamagedHard;

    public AssetPowerBox(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        brickAStand = atlas.findRegion("brickA", 1);
        brickBStand = atlas.findRegion("brickB", 1);
        brickCStand = atlas.findRegion("brickC", 1);
        brickDStand = atlas.findRegion("brickD", 1);
        brickEStand = atlas.findRegion("brickE", 1);
        brickFStand = atlas.findRegion("brickF", 1);
        brickGStand = atlas.findRegion("brickG", 1);
        brickHStand = atlas.findRegion("brickH", 1);
        brickIStand = atlas.findRegion("brickI", 1);
        brickJStand = atlas.findRegion("brickJ", 1);
        brickKStand = atlas.findRegion("brickK", 1);
        brickLStand = atlas.findRegion("brickL", 1);
        brickMStand = atlas.findRegion("brickM", 1);
        brickNStand = atlas.findRegion("brickN", 1);

        brickADamagedLittle = atlas.findRegion("brickA", 2);
        brickBDamagedLittle = atlas.findRegion("brickB", 2);
        brickCDamagedLittle = atlas.findRegion("brickC", 2);
        brickDDamagedLittle = atlas.findRegion("brickD", 2);
        brickEDamagedLittle = atlas.findRegion("brickE", 2);
        brickFDamagedLittle = atlas.findRegion("brickF", 2);
        brickGDamagedLittle = atlas.findRegion("brickG", 2);
        brickHDamagedLittle = atlas.findRegion("brickH", 2);
        brickIDamagedLittle = atlas.findRegion("brickI", 2);
        brickJDamagedLittle = atlas.findRegion("brickJ", 2);
        brickKDamagedLittle = atlas.findRegion("brickK", 2);
        brickLDamagedLittle = atlas.findRegion("brickL", 2);
        brickMDamagedLittle = atlas.findRegion("brickM", 2);
        brickNDamagedLittle = atlas.findRegion("brickN", 2);

        brickADamagedMedium = atlas.findRegion("brickA", 3);
        brickBDamagedMedium = atlas.findRegion("brickB", 3);
        brickCDamagedMedium = atlas.findRegion("brickC", 3);
        brickDDamagedMedium = atlas.findRegion("brickD", 3);
        brickEDamagedMedium = atlas.findRegion("brickE", 3);
        brickFDamagedMedium = atlas.findRegion("brickF", 3);
        brickGDamagedMedium = atlas.findRegion("brickG", 3);
        brickHDamagedMedium = atlas.findRegion("brickH", 3);
        brickIDamagedMedium = atlas.findRegion("brickI", 3);
        brickJDamagedMedium = atlas.findRegion("brickJ", 3);
        brickKDamagedMedium = atlas.findRegion("brickK", 3);
        brickLDamagedMedium = atlas.findRegion("brickL", 3);
        brickMDamagedMedium = atlas.findRegion("brickM", 3);
        brickNDamagedMedium = atlas.findRegion("brickN", 3);

        brickADamagedHard = atlas.findRegion("brickA", 4);
        brickBDamagedHard = atlas.findRegion("brickB", 4);
        brickCDamagedHard = atlas.findRegion("brickC", 4);
        brickDDamagedHard = atlas.findRegion("brickD", 4);
        brickEDamagedHard = atlas.findRegion("brickE", 4);
        brickFDamagedHard = atlas.findRegion("brickF", 4);
        brickGDamagedHard = atlas.findRegion("brickG", 4);
        brickHDamagedHard = atlas.findRegion("brickH", 4);
        brickIDamagedHard = atlas.findRegion("brickI", 4);
        brickJDamagedHard = atlas.findRegion("brickJ", 4);
        brickKDamagedHard = atlas.findRegion("brickK", 4);
        brickLDamagedHard = atlas.findRegion("brickL", 4);
        brickMDamagedHard = atlas.findRegion("brickM", 4);
        brickNDamagedHard = atlas.findRegion("brickN", 4);
    }

    public TextureRegion getBrickAStand() {
        return brickAStand;
    }

    public TextureRegion getBrickBStand() {
        return brickBStand;
    }

    public TextureRegion getBrickCStand() {
        return brickCStand;
    }

    public TextureRegion getBrickDStand() {
        return brickDStand;
    }

    public TextureRegion getBrickEStand() {
        return brickEStand;
    }

    public TextureRegion getBrickFStand() {
        return brickFStand;
    }

    public TextureRegion getBrickGStand() {
        return brickGStand;
    }

    public TextureRegion getBrickHStand() {
        return brickHStand;
    }

    public TextureRegion getBrickIStand() {
        return brickIStand;
    }

    public TextureRegion getBrickJStand() {
        return brickJStand;
    }

    public TextureRegion getBrickKStand() {
        return brickKStand;
    }

    public TextureRegion getBrickLStand() {
        return brickLStand;
    }

    public TextureRegion getBrickMStand() {
        return brickMStand;
    }

    public TextureRegion getBrickNStand() {
        return brickNStand;
    }

    public TextureRegion getBrickADamagedLittle() {
        return brickADamagedLittle;
    }

    public TextureRegion getBrickBDamagedLittle() {
        return brickBDamagedLittle;
    }

    public TextureRegion getBrickCDamagedLittle() {
        return brickCDamagedLittle;
    }

    public TextureRegion getBrickDDamagedLittle() {
        return brickDDamagedLittle;
    }

    public TextureRegion getBrickEDamagedLittle() {
        return brickEDamagedLittle;
    }

    public TextureRegion getBrickFDamagedLittle() {
        return brickFDamagedLittle;
    }

    public TextureRegion getBrickGDamagedLittle() {
        return brickGDamagedLittle;
    }

    public TextureRegion getBrickHDamagedLittle() {
        return brickHDamagedLittle;
    }

    public TextureRegion getBrickIDamagedLittle() {
        return brickIDamagedLittle;
    }

    public TextureRegion getBrickJDamagedLittle() {
        return brickJDamagedLittle;
    }

    public TextureRegion getBrickKDamagedLittle() {
        return brickKDamagedLittle;
    }

    public TextureRegion getBrickLDamagedLittle() {
        return brickLDamagedLittle;
    }

    public TextureRegion getBrickMDamagedLittle() {
        return brickMDamagedLittle;
    }

    public TextureRegion getBrickNDamagedLittle() {
        return brickNDamagedLittle;
    }

    public TextureRegion getBrickADamagedMedium() {
        return brickADamagedMedium;
    }

    public TextureRegion getBrickBDamagedMedium() {
        return brickBDamagedMedium;
    }

    public TextureRegion getBrickCDamagedMedium() {
        return brickCDamagedMedium;
    }

    public TextureRegion getBrickDDamagedMedium() {
        return brickDDamagedMedium;
    }

    public TextureRegion getBrickEDamagedMedium() {
        return brickEDamagedMedium;
    }

    public TextureRegion getBrickFDamagedMedium() {
        return brickFDamagedMedium;
    }

    public TextureRegion getBrickGDamagedMedium() {
        return brickGDamagedMedium;
    }

    public TextureRegion getBrickHDamagedMedium() {
        return brickHDamagedMedium;
    }

    public TextureRegion getBrickIDamagedMedium() {
        return brickIDamagedMedium;
    }

    public TextureRegion getBrickJDamagedMedium() {
        return brickJDamagedMedium;
    }

    public TextureRegion getBrickKDamagedMedium() {
        return brickKDamagedMedium;
    }

    public TextureRegion getBrickLDamagedMedium() {
        return brickLDamagedMedium;
    }

    public TextureRegion getBrickMDamagedMedium() {
        return brickMDamagedMedium;
    }

    public TextureRegion getBrickNDamagedMedium() {
        return brickNDamagedMedium;
    }

    public TextureRegion getBrickADamagedHard() {
        return brickADamagedHard;
    }

    public TextureRegion getBrickBDamagedHard() {
        return brickBDamagedHard;
    }

    public TextureRegion getBrickCDamagedHard() {
        return brickCDamagedHard;
    }

    public TextureRegion getBrickDDamagedHard() {
        return brickDDamagedHard;
    }

    public TextureRegion getBrickEDamagedHard() {
        return brickEDamagedHard;
    }

    public TextureRegion getBrickFDamagedHard() {
        return brickFDamagedHard;
    }

    public TextureRegion getBrickGDamagedHard() {
        return brickGDamagedHard;
    }

    public TextureRegion getBrickHDamagedHard() {
        return brickHDamagedHard;
    }

    public TextureRegion getBrickIDamagedHard() {
        return brickIDamagedHard;
    }

    public TextureRegion getBrickJDamagedHard() {
        return brickJDamagedHard;
    }

    public TextureRegion getBrickKDamagedHard() {
        return brickKDamagedHard;
    }

    public TextureRegion getBrickLDamagedHard() {
        return brickLDamagedHard;
    }

    public TextureRegion getBrickMDamagedHard() {
        return brickMDamagedHard;
    }

    public TextureRegion getBrickNDamagedHard() {
        return brickNDamagedHard;
    }
}
