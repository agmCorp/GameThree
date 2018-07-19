package uy.com.agm.gamethree.widget;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by AGM on 5/27/2018.
 */

public class AnimatedActor extends Actor {
    Animation animation;
    TextureRegion textureRegion;
    float stateTime;

    public AnimatedActor(Animation animation) {
        this.animation = animation;
        textureRegion = (TextureRegion) animation.getKeyFrame(0);
    }

    @Override
    public void act(float delta) {
        stateTime += delta;
        textureRegion = (TextureRegion) animation.getKeyFrame(stateTime);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, getX(), getY());
    }

    public void setAnimation(Animation animation) {
        stateTime = 0;
        this.animation = animation;
    }
}
