package uy.com.agm.gamethree.widget;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

/**
 * Created by AGM on 5/13/2018.
 */

public class AnimatedActor extends Image {
    private static final String TAG = AnimatedActor.class.getName();

    private float stateTime;
    Animation animation;

    public AnimatedActor() {
        super();
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
        stateTime = 0;
        TextureRegion region = (TextureRegion) animation.getKeyFrame(stateTime, false);
        // WA: Using new each time is the only way I found to set the correct size
        // This could be a memory leak
        setDrawable(new TextureRegionDrawable(region));
        setScaling(Scaling.fit);
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
