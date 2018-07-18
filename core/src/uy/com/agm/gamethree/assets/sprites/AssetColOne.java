package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by amorales on 23/1/2018.
 */

public class AssetColOne {
    private static final String TAG = AssetColOne.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 85.0f * 0.5f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 85.0f * 0.5f / PlayScreen.PPM;
    public static final int MAX_TEXTURES = 2;

    private Array<TextureRegion> goldStand;
    private Array<TextureRegion> silverStand;
    private Array<TextureRegion> bronzeStand;
    private Array<Animation> goldAnimation;
    private Array<Animation> silverAnimation;
    private Array<Animation> bronzeAnimation;

    public AssetColOne(TextureAtlas atlas) {
        goldStand = new Array<TextureRegion>();
        silverStand = new Array<TextureRegion>();
        bronzeStand = new Array<TextureRegion>();
        Array<TextureAtlas.AtlasRegion> regions;
        goldAnimation = new Array<Animation>();
        silverAnimation = new Array<Animation>();
        bronzeAnimation = new Array<Animation>();

        goldStand.add(atlas.findRegion("coinGold", 1));
        goldStand.add(atlas.findRegion("diamond", 1));

        silverStand.add(atlas.findRegion("coinSilver", 1));
        silverStand.add(atlas.findRegion("jewelOrange", 1));

        bronzeStand.add(atlas.findRegion("coinBronze", 1));
        bronzeStand.add(atlas.findRegion("jewelFuchsia", 1));

        // Animation Gold
        regions = atlas.findRegions("coinGold");
        goldAnimation.add(new Animation(0.3f / 10.0f, regions, Animation.PlayMode.LOOP));
        regions.clear();
        regions = atlas.findRegions("diamond");
        goldAnimation.add(new Animation(0.5f / 6.0f, regions, Animation.PlayMode.LOOP));
        regions.clear();

        // Animation Silver
        regions = atlas.findRegions("coinSilver");
        silverAnimation.add(new Animation(0.3f / 10.0f, regions, Animation.PlayMode.LOOP));
        regions.clear();
        regions = atlas.findRegions("jewelOrange");
        silverAnimation.add(new Animation(0.3f / 8.0f, regions, Animation.PlayMode.LOOP));
        regions.clear();

        // Animation Bronze
        regions = atlas.findRegions("coinBronze");
        bronzeAnimation.add(new Animation(0.3f / 10.0f, regions, Animation.PlayMode.LOOP));
        regions.clear();
        regions = atlas.findRegions("jewelFuchsia");
        bronzeAnimation.add(new Animation(0.3f / 4.0f, regions, Animation.PlayMode.LOOP));
        regions.clear();
    }

    public int getRandomIndexGold() {
        return MathUtils.random(MAX_TEXTURES - 1);
    }

    public int getRandomIndexSilver() {
        return MathUtils.random(MAX_TEXTURES - 1);
    }

    public int getRandomIndexBronze() {
        return MathUtils.random(MAX_TEXTURES - 1);
    }

    public TextureRegion getGoldStand(int index) {
        return goldStand.get(index);
    }

    public TextureRegion getSilverStand(int index) {
        return silverStand.get(index);
    }

    public TextureRegion getBronzeStand(int index) {
        return bronzeStand.get(index);
    }

    public Animation getGoldAnimation(int index) {
        return goldAnimation.get(index);
    }

    public Animation getSilverAnimation(int index) {
        return silverAnimation.get(index);
    }

    public Animation getBronzeAnimation(int index) {
        return bronzeAnimation.get(index);
    }
}
