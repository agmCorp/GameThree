package uy.com.agm.gamethree.tools.actordef;


import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Created by AGM on 2/18/2018.
 */

public class ActorDefBullet extends ActorDef {
    private static final String TAG = ActorDefBullet.class.getName();

    private float width;
    private float height;
    private float circleShapeRadius;
    private float angle;
    private Animation animation;

    public ActorDefBullet(float x, float y, Class<?> type) {
        super(x, y, type);
        this.width = 0;
        this.height = 0;
        this.circleShapeRadius = 0;
        this.angle = 0;
        this.animation = null;
    }

    public ActorDefBullet(float x, float y, float angle, Class<?> type) {
        super(x, y, type);
        this.width = 0;
        this.height = 0;
        this.circleShapeRadius = 0;
        this.angle = angle;
        this.animation = null;
    }

    public ActorDefBullet(float x, float y, float width, float height, float circleShapeRadius, float angle, Animation animation, Class<?> type) {
        super(x, y, type);
        this.width = width;
        this.height = height;
        this.circleShapeRadius = circleShapeRadius;
        this.angle = angle;
        this.animation = animation;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getCircleShapeRadius() {
        return circleShapeRadius;
    }

    public float getAngle() {
        return angle;
    }

    public Animation getAnimation() {
        return animation;
    }
}
