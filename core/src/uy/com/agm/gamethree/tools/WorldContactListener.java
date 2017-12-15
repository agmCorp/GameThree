package uy.com.agm.gamethree.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import uy.com.agm.gamethree.game.GameThree;
import uy.com.agm.gamethree.sprites.enemies.Enemy;
import uy.com.agm.gamethree.sprites.enemies.EnemyOne;
import uy.com.agm.gamethree.sprites.tileObjects.InteractiveTileObject;

/**
 * Created by AGM on 12/8/2017.
 */

public class WorldContactListener implements ContactListener {
    private static final String TAG = WorldContactListener.class.getName();

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        Fixture fixC;

        int collisionDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (collisionDef) {
            case GameThree.HERO_BIT | GameThree.DEFAULT_BIT: // bordes
                fixC = fixA.getFilterData().categoryBits == GameThree.DEFAULT_BIT ? fixA : fixB;
                ((InteractiveTileObject) fixC.getUserData()).onHit();
                break;
            case GameThree.HERO_BIT | GameThree.OBSTACLE_BIT: // arboles
                fixC = fixA.getFilterData().categoryBits == GameThree.OBSTACLE_BIT ? fixA : fixB;
                ((InteractiveTileObject) fixC.getUserData()).onHit();
                break;
            case GameThree.HERO_BIT | GameThree.COINBOX_BIT:
                fixC = fixA.getFilterData().categoryBits == GameThree.COINBOX_BIT ? fixA : fixB;
                ((InteractiveTileObject) fixC.getUserData()).onHit();
                break;
            case GameThree.HERO_BIT | GameThree.ENEMY_BIT:
                fixC = fixA.getFilterData().categoryBits == GameThree.ENEMY_BIT ? fixA : fixB;
                ((Enemy) fixC.getUserData()).onHit();
                Gdx.app.debug(TAG, "Hero muere!");
                break;
            case GameThree.ENEMY_BIT | GameThree.DEFAULT_BIT:
                fixC = fixA.getFilterData().categoryBits == GameThree.ENEMY_BIT ? fixA : fixB;
                ((EnemyOne) fixC.getUserData()).reverseVelocity(true, false);
                break;
            case GameThree.ENEMY_BIT | GameThree.OBSTACLE_BIT:
                fixC = fixA.getFilterData().categoryBits == GameThree.ENEMY_BIT ? fixA : fixB;
                ((EnemyOne) fixC.getUserData()).reverseVelocity(true, false);
                break;
            case GameThree.ENEMY_BIT | GameThree.ENEMY_BIT:
                ((EnemyOne) fixA.getUserData()).reverseVelocity(true, false);
                ((EnemyOne) fixB.getUserData()).reverseVelocity(true, false);
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
