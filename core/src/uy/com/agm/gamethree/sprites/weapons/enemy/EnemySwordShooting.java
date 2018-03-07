package uy.com.agm.gamethree.sprites.weapons.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.game.Constants;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.weapons.IShootStrategy;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.Vector2Util;
import uy.com.agm.gamethree.tools.actordef.ActorDefBullet;

/**
 * Created by amorales on 5/3/2018.
 */

public class EnemySwordShooting implements IShootStrategy { // todo hacer esto
    private static final String TAG = EnemySwordShooting.class.getName();

    private PlayScreen screen;
    private float openFireTime;
    private float fireDelay;
    private Vector2 tmp; // Temp GC friendly vector

    public EnemySwordShooting(PlayScreen screen, float initialOpenFireTime, float fireDelay) {
        this.screen = screen;
        this.openFireTime = initialOpenFireTime;
        this.fireDelay = fireDelay;

        // Temp GC friendly vector
        tmp = new Vector2();
    }

    @Override
    public void updateShoot(float dt) {
        openFireTime += dt;
    }

    @Override
    public void shoot(float x, float y) {
        if (openFireTime > fireDelay) {
            shootImp(x, y);
            openFireTime = 0;
        }
    }

    private void shootImp(float x, float y) {
        // Move EnemyBullet from Enemy to Hero
        tmp.set(x, y);
        Vector2Util.goToTarget(tmp, screen.getPlayer().getB2body().getPosition().x, screen.getPlayer().getB2body().getPosition().y, Constants.ENEMYBULLET_LINEAR_VELOCITY);

//        float numberBullets = 4;
//        float directionDegrees = 180.0f / (numberBullets + 1);
//        float angle;
//        for (int i = 1; i <= numberBullets; i++) {
//            angle = directionDegrees * i;
//            angle = (angle >= 90.0f) ? angle - 90.0f : 270.0f + angle;
//        float angle = 90;
//        float velAngle = tmp.angle();
//        if (0 <= velAngle && velAngle <= 180.0f) {
//            angle = 270.0f;
//        }
//        angle = angle + velAngle;

        float angle; //90no 0no 180no
        float velAngle = tmp.angle();
        Gdx.app.debug(TAG, "ANGULO " + tmp.angle());


        if (0 <= velAngle && velAngle <= 180.0f) {
            angle = 270 + velAngle; // estoy anda bien
        } else {

        // if (180 <= velAngle && velAngle <= 360.0f) {
            angle = 0 - velAngle; // velAngle no, +90 manda bien pero imagen a 180, +180 no, angle 0 no ** velangle - 180no, -90no, -270no, -360no
            //270 - vel no, 180 - vel no, 90 - vel no, 360 - vel no, 0 - vel no
        }// mod 360 no, mod 180no, mod 90no, mod 270no

            screen.getCreator().createGameThreeActor(new ActorDefBullet(x, y,
                    Constants.POWERTHREE_BULLET_WIDTH_METERS,
                    Constants.POWERTHREE_BULLET_HEIGHT_METERS,
                    Constants.POWERTHREE_BULLET_CIRCLESHAPE_RADIUS_METERS,
                    angle,
                    tmp.x,
                    tmp.y,
                    Assets.getInstance().getBulletA().getBulletAAnimation(),
                    EnemyBullet.class));
            // Sound FX
            AudioManager.getInstance().play(Assets.getInstance().getSounds().getEnemyShoot());
        }
   // }
}
