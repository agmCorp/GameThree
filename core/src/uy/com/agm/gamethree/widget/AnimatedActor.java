package uy.com.agm.gamethree.widget;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by AGM on 5/13/2018.
 */

public class AnimatedActor extends Image
{
    private float stateTime = 0;
    Animation animation;

    public AnimatedActor(Animation animation) {
        super((TextureRegion) animation.getKeyFrame(0));
        this.animation = animation;
    }

    @Override
    public void act(float delta) {
        stateTime += delta;
        ((TextureRegionDrawable)getDrawable()).setRegion((TextureRegion) animation.getKeyFrame(stateTime, true));
        super.act(delta);
    }
}
