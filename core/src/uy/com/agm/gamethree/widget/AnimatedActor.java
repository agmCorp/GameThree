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

    private TextureRegionDrawable textureRegionDrawable;
    private float stateTime;
    Animation animation;

    public AnimatedActor() {
        super();
        textureRegionDrawable = new TextureRegionDrawable();
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
        stateTime = 0;
        TextureRegion region = (TextureRegion) animation.getKeyFrame(stateTime, false);
        textureRegionDrawable.setRegion(region);
        setDrawable(textureRegionDrawable);
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
