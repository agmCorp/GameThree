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
    float velocityX;
    float velocityY;
    private Animation animation;

    public ActorDefBullet(float x, float y, float width, float height, float circleShapeRadius, float angle, float velocityX, float velocityY, Animation animation, Class<?> type) {
        super(x, y, type);
        this.width = width;
        this.height = height;
        this.circleShapeRadius = circleShapeRadius;
        this.angle = angle;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
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

    public float getVelocityX() {
        return velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public Animation getAnimation() {
        return animation;
    }
}
