package uy.com.agm.gamethree.tools;

import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Created by AGM on 12/14/2017.
 */

public class GameThreeActorDef {
    private static final String TAG = GameThreeActorDef.class.getName();

    private float x;
    private float y;
    float width;
    float height;
    private float angle;
    private Animation animation;
    private Class<?> type;

    public GameThreeActorDef(float x, float y, Class<?> type) {
        this.x = x;
        this.y = y;
        this.width = 0;
        this.height = 0;
        this.angle = 0;
        this.animation = null;
        this.type = type;
    }

    public GameThreeActorDef(float x, float y, float width, float height, Class<?> type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.angle = 0;
        this.animation = null;
        this.type = type;
    }

    public GameThreeActorDef(float x, float y, float width, float height, float angle, Class<?> type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.angle = angle;
        this.animation = null;
        this.type = type;
    }

    public GameThreeActorDef(float x, float y, float width, float height, float angle, Animation animation, Class<?> type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.angle = angle;
        this.animation = animation;
        this.type = type;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getAngle() {
        return angle;
    }

    public Animation getAnimation() {
        return animation;
    }

    public Class<?> getType() {
        return type;
    }
}
