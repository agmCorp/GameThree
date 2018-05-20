package uy.com.agm.gamethree.widget;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by AGM on 5/13/2018.
 */

public class AnimatedActor extends Image {
    private static final String TAG = AnimatedActor.class.getName();

    private TextureRegionDrawable textureRegionDrawable;
    private float stateTime;
    Animation animation;

    public AnimatedActor() {
        textureRegionDrawable = new TextureRegionDrawable();
    }

    public void setAnimation(Animation animation) {
        textureRegionDrawable.setRegion((TextureRegion) animation.getKeyFrame(0));
        setDrawable(textureRegionDrawable);
        stateTime = 0;
        this.animation = animation;
    }

    @Override
    public void act(float delta) {
        if (animation != null) {
            stateTime += delta;
            ((TextureRegionDrawable) getDrawable()).setRegion((TextureRegion) animation.getKeyFrame(stateTime, false));
            super.act(delta);
        }
    }
}
