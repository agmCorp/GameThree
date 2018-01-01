package uy.com.agm.gamethree.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import uy.com.agm.gamethree.sprites.enemies.Enemy;
import uy.com.agm.gamethree.sprites.finals.FinalEnemyLevelOne;
import uy.com.agm.gamethree.sprites.player.Hero;
import uy.com.agm.gamethree.sprites.powerup.Items.Item;
import uy.com.agm.gamethree.sprites.powerup.boxes.PowerBox;
import uy.com.agm.gamethree.sprites.tileobjects.InteractiveTileObject;
import uy.com.agm.gamethree.sprites.weapons.Weapon;

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
            // Hero - InteractiveTileObjects
            case Constants.HERO_BIT | Constants.BORDERS_BIT:
            case Constants.HERO_BIT | Constants.OBSTACLE_BIT:
                if (fixA.getFilterData().categoryBits == Constants.HERO_BIT) {
                    ((InteractiveTileObject) fixB.getUserData()).onHit();
                } else {
                    ((InteractiveTileObject) fixA.getUserData()).onHit();
                }
                break;

            // Hero - PowerBox
            case Constants.HERO_BIT | Constants.POWERBOX_BIT:
                if (fixA.getFilterData().categoryBits == Constants.HERO_BIT) {
                    ((PowerBox) fixB.getUserData()).onBump();
                } else {
                    ((PowerBox) fixA.getUserData()).onBump();
                }
                break;

            // Hero - Item
            case Constants.HERO_BIT | Constants.ITEM_BIT:
                if (fixA.getFilterData().categoryBits == Constants.ITEM_BIT) {
                    ((Item) fixA.getUserData()).use((Hero) fixB.getUserData());
                } else {
                    ((Item) fixB.getUserData()).use((Hero) fixA.getUserData());
                }
                break;

            // Hero - Enemies
            case Constants.HERO_BIT | Constants.ENEMY_BIT:
                fixC = fixA.getFilterData().categoryBits == Constants.HERO_BIT ? fixA : fixB;
                ((Hero) fixC.getUserData()).onDead();
                break;

            // Hero - Enemy's weapon
            case Constants.HERO_BIT | Constants.ENEMY_WEAPON_BIT:
                fixC = fixA.getFilterData().categoryBits == Constants.HERO_BIT ? fixA : fixB;
                ((Hero) fixC.getUserData()).onDead();
                break;

            // Enemy - InteractiveTileObjects
            case Constants.ENEMY_BIT | Constants.OBSTACLE_BIT:
            case Constants.ENEMY_BIT | Constants.BORDERS_BIT:
                fixC = fixA.getFilterData().categoryBits == Constants.ENEMY_BIT ? fixA : fixB;
                ((Enemy) fixC.getUserData()).reverseVelocity(true, false);
                break;

            // Enemy - PowerBox
            case Constants.ENEMY_BIT | Constants.POWERBOX_BIT:
                fixC = fixA.getFilterData().categoryBits == Constants.ENEMY_BIT ? fixA : fixB;
                ((Enemy) fixC.getUserData()).reverseVelocity(true, false);
                break;

            // Enemy - Enemy
            case Constants.ENEMY_BIT | Constants.ENEMY_BIT:
                ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                break;

            // Item - InteractiveTileObjects
            case Constants.ITEM_BIT | Constants.BORDERS_BIT:
            case Constants.ITEM_BIT | Constants.OBSTACLE_BIT:
                fixC = fixA.getFilterData().categoryBits == Constants.ITEM_BIT ? fixA : fixB;
                ((Item) fixC.getUserData()).reverseVelocity(true, false);
                break;

            // Item - Enemy
            case Constants.ITEM_BIT | Constants.ENEMY_BIT:
                fixC = fixA.getFilterData().categoryBits == Constants.ITEM_BIT ? fixA : fixB;
                ((Item) fixC.getUserData()).reverseVelocity(true, false);
                break;

            // Item - PowerBox
            case Constants.ITEM_BIT | Constants.POWERBOX_BIT:
                fixC = fixA.getFilterData().categoryBits == Constants.ITEM_BIT ? fixA : fixB;
                ((Item) fixC.getUserData()).reverseVelocity(true, false);
                break;

            // Item - Item
            case Constants.ITEM_BIT | Constants.ITEM_BIT:
                ((Item) fixA.getUserData()).reverseVelocity(true, false);
                ((Item) fixB.getUserData()).reverseVelocity(true, false);
                break;

            // Hero's weapon - InteractiveTileObjects & Items
            case Constants.HERO_WEAPON_BIT | Constants.BORDERS_BIT:
            case Constants.HERO_WEAPON_BIT | Constants.OBSTACLE_BIT:
            case Constants.HERO_WEAPON_BIT | Constants.ITEM_BIT:
                fixC = fixA.getFilterData().categoryBits == Constants.HERO_WEAPON_BIT ? fixA : fixB;
                ((Weapon) fixC.getUserData()).onTarget();
                break;

            // Hero's weapon - PowerBox
            case Constants.HERO_WEAPON_BIT | Constants.POWERBOX_BIT:
                if (fixA.getFilterData().categoryBits == Constants.HERO_WEAPON_BIT) {
                    ((Weapon) fixA.getUserData()).onTarget();
                    ((PowerBox) fixB.getUserData()).onHit();
                } else {
                    ((Weapon) fixB.getUserData()).onTarget();
                    ((PowerBox) fixA.getUserData()).onHit();
                }
                break;

            // Hero's weapon - Enemy
            case Constants.HERO_WEAPON_BIT | Constants.ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == Constants.HERO_WEAPON_BIT) {
                    ((Weapon) fixA.getUserData()).onTarget();
                    ((Enemy) fixB.getUserData()).onHit();
                } else {
                    ((Weapon) fixB.getUserData()).onTarget();
                    ((Enemy) fixA.getUserData()).onHit();
                }
                break;

            // Enemy's weapon - Borders
            case Constants.ENEMY_WEAPON_BIT | Constants.BORDERS_BIT:
                fixC = fixA.getFilterData().categoryBits == Constants.ENEMY_WEAPON_BIT ? fixA : fixB;
                ((Weapon) fixC.getUserData()).onTarget();
                break;

            // Final enemy - Borders
            case Constants.FINAL_ENEMY_LEVEL_ONE_BIT | Constants.BORDERS_BIT:
                fixC = fixA.getFilterData().categoryBits == Constants.FINAL_ENEMY_LEVEL_ONE_BIT ? fixA : fixB;
                ((FinalEnemyLevelOne) fixC.getUserData()).onHitWall();
                break;

            // Final enemy - Edges
            case Constants.FINAL_ENEMY_LEVEL_ONE_BIT | Constants.EDGES_BIT:
                fixC = fixA.getFilterData().categoryBits == Constants.FINAL_ENEMY_LEVEL_ONE_BIT ? fixA : fixB;
                ((FinalEnemyLevelOne) fixC.getUserData()).onHitWall();
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
