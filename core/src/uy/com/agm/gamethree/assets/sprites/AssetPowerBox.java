package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
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
    public static final int MAX_TEXTURES = 14;

    private Array<TextureRegion> bricksStand;
    private Array<TextureRegion> bricksDamagedLittle;
    private Array<TextureRegion> bricksDamagedMedium;
    private Array<TextureRegion> bricksDamagedHard;

    public AssetPowerBox(TextureAtlas atlas) {
        bricksStand = new Array<TextureRegion>();
        bricksDamagedLittle = new Array<TextureRegion>();
        bricksDamagedMedium = new Array<TextureRegion>();
        bricksDamagedHard = new Array<TextureRegion>();

        bricksStand.add(atlas.findRegion("brickA", 1));
        bricksStand.add(atlas.findRegion("brickB", 1));
        bricksStand.add(atlas.findRegion("brickC", 1));
        bricksStand.add(atlas.findRegion("brickD", 1));
        bricksStand.add(atlas.findRegion("brickE", 1));
        bricksStand.add(atlas.findRegion("brickF", 1));
        bricksStand.add(atlas.findRegion("brickG", 1));
        bricksStand.add(atlas.findRegion("brickH", 1));
        bricksStand.add(atlas.findRegion("brickI", 1));
        bricksStand.add(atlas.findRegion("brickJ", 1));
        bricksStand.add(atlas.findRegion("brickK", 1));
        bricksStand.add(atlas.findRegion("brickL", 1));
        bricksStand.add(atlas.findRegion("brickM", 1));
        bricksStand.add(atlas.findRegion("brickN", 1));

        bricksDamagedLittle.add(atlas.findRegion("brickA", 2));
        bricksDamagedLittle.add(atlas.findRegion("brickB", 2));
        bricksDamagedLittle.add(atlas.findRegion("brickC", 2));
        bricksDamagedLittle.add(atlas.findRegion("brickD", 2));
        bricksDamagedLittle.add(atlas.findRegion("brickE", 2));
        bricksDamagedLittle.add(atlas.findRegion("brickF", 2));
        bricksDamagedLittle.add(atlas.findRegion("brickG", 2));
        bricksDamagedLittle.add(atlas.findRegion("brickH", 2));
        bricksDamagedLittle.add(atlas.findRegion("brickI", 2));
        bricksDamagedLittle.add(atlas.findRegion("brickJ", 2));
        bricksDamagedLittle.add(atlas.findRegion("brickK", 2));
        bricksDamagedLittle.add(atlas.findRegion("brickL", 2));
        bricksDamagedLittle.add(atlas.findRegion("brickM", 2));
        bricksDamagedLittle.add(atlas.findRegion("brickN", 2));

        bricksDamagedMedium.add(atlas.findRegion("brickA", 3));
        bricksDamagedMedium.add(atlas.findRegion("brickB", 3));
        bricksDamagedMedium.add(atlas.findRegion("brickC", 3));
        bricksDamagedMedium.add(atlas.findRegion("brickD", 3));
        bricksDamagedMedium.add(atlas.findRegion("brickE", 3));
        bricksDamagedMedium.add(atlas.findRegion("brickF", 3));
        bricksDamagedMedium.add(atlas.findRegion("brickG", 3));
        bricksDamagedMedium.add(atlas.findRegion("brickH", 3));
        bricksDamagedMedium.add(atlas.findRegion("brickI", 3));
        bricksDamagedMedium.add(atlas.findRegion("brickJ", 3));
        bricksDamagedMedium.add(atlas.findRegion("brickK", 3));
        bricksDamagedMedium.add(atlas.findRegion("brickL", 3));
        bricksDamagedMedium.add(atlas.findRegion("brickM", 3));
        bricksDamagedMedium.add(atlas.findRegion("brickN", 3));

        bricksDamagedHard.add(atlas.findRegion("brickA", 4));
        bricksDamagedHard.add(atlas.findRegion("brickB", 4));
        bricksDamagedHard.add(atlas.findRegion("brickC", 4));
        bricksDamagedHard.add(atlas.findRegion("brickD", 4));
        bricksDamagedHard.add(atlas.findRegion("brickE", 4));
        bricksDamagedHard.add(atlas.findRegion("brickF", 4));
        bricksDamagedHard.add(atlas.findRegion("brickG", 4));
        bricksDamagedHard.add(atlas.findRegion("brickH", 4));
        bricksDamagedHard.add(atlas.findRegion("brickI", 4));
        bricksDamagedHard.add(atlas.findRegion("brickJ", 4));
        bricksDamagedHard.add(atlas.findRegion("brickK", 4));
        bricksDamagedHard.add(atlas.findRegion("brickL", 4));
        bricksDamagedHard.add(atlas.findRegion("brickM", 4));
        bricksDamagedHard.add(atlas.findRegion("brickN", 4));
    }

    public int getRandomIndexBrick() {
        return MathUtils.random(1, AssetPowerBox.MAX_TEXTURES);
    }

    public TextureRegion getBrickStand(int index) {
        return bricksStand.get(index);
    }

    public TextureRegion getBrickDamagedLittle(int index) {
        return bricksDamagedLittle.get(index);
    }

    public TextureRegion getBrickDamagedMedium(int index) {
        return bricksDamagedMedium.get(index);
    }

    public TextureRegion getBrickDamagedHard(int index) {
        return bricksDamagedHard.get(index);
    }
}
