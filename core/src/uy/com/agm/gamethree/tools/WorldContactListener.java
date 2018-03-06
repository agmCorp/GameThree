package uy.com.agm.gamethree.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import uy.com.agm.gamethree.game.Constants;
import uy.com.agm.gamethree.sprites.boxes.PowerBox;
import uy.com.agm.gamethree.sprites.enemies.Enemy;
import uy.com.agm.gamethree.sprites.enemies.EnemyThree;
import uy.com.agm.gamethree.sprites.finals.FinalEnemy;
import uy.com.agm.gamethree.sprites.items.Item;
import uy.com.agm.gamethree.sprites.player.Hero;
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
            // Hero/HeroGhost/HeroTough - InteractiveTileObjects
            case Constants.HERO_BIT | Constants.BORDER_BIT:
            case Constants.HERO_BIT | Constants.OBSTACLE_BIT:
            case Constants.HERO_BIT | Constants.PATH_BIT:
            case Constants.HERO_GHOST_BIT | Constants.BORDER_BIT:
            case Constants.HERO_GHOST_BIT | Constants.OBSTACLE_BIT:
            case Constants.HERO_GHOST_BIT | Constants.PATH_BIT:
            case Constants.HERO_TOUGH_BIT | Constants.BORDER_BIT:
            case Constants.HERO_TOUGH_BIT | Constants.OBSTACLE_BIT:
            case Constants.HERO_TOUGH_BIT | Constants.PATH_BIT:
                fixC = (fixA.getFilterData().categoryBits == Constants.BORDER_BIT ||
                        fixA.getFilterData().categoryBits == Constants.OBSTACLE_BIT ||
                        fixA.getFilterData().categoryBits == Constants.PATH_BIT) ? fixA : fixB;
                ((InteractiveTileObject) fixC.getUserData()).onBump();
                break;

            // Hero/HeroGhost/HeroTough - PowerBox
            case Constants.HERO_BIT | Constants.POWERBOX_BIT:
            case Constants.HERO_GHOST_BIT | Constants.POWERBOX_BIT:
            case Constants.HERO_TOUGH_BIT | Constants.POWERBOX_BIT:
                fixC = fixA.getFilterData().categoryBits == Constants.POWERBOX_BIT ? fixA : fixB;
                ((PowerBox) fixC.getUserData()).onBump();
                break;

            // Hero/HeroGhost/HeroTough - Item
            case Constants.HERO_BIT | Constants.ITEM_BIT:
            case Constants.HERO_GHOST_BIT | Constants.ITEM_BIT:
            case Constants.HERO_TOUGH_BIT | Constants.ITEM_BIT:
                fixC = fixA.getFilterData().categoryBits == Constants.ITEM_BIT ? fixA : fixB;
                ((Item) fixC.getUserData()).onUse();
                break;

            // Hero - Enemies
            case Constants.HERO_BIT | Constants.ENEMY_BIT:
                fixC = fixA.getFilterData().categoryBits == Constants.HERO_BIT ? fixA : fixB;
                ((Hero) fixC.getUserData()).onDead();
                break;

            // HeroGhost - Enemies
            case Constants.HERO_GHOST_BIT | Constants.ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == Constants.ENEMY_BIT) {
                    Enemy enemy = ((Enemy) fixA.getUserData());
                    if (enemy instanceof EnemyThree) {
                        ((Hero) fixB.getUserData()).onDead();
                    }
                } else {
                    Enemy enemy = ((Enemy) fixB.getUserData());
                    if (enemy instanceof EnemyThree) {
                        ((Hero) fixA.getUserData()).onDead();
                    }
                }
                break;

            // HeroTough - Enemies
            case Constants.HERO_TOUGH_BIT | Constants.ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == Constants.ENEMY_BIT) {
                    Enemy enemy = ((Enemy) fixA.getUserData());
                    if (enemy instanceof EnemyThree) {
                        ((Hero) fixB.getUserData()).onDead();
                    } else {
                        enemy.onHit();
                    }
                } else {
                    Enemy enemy = ((Enemy) fixB.getUserData());
                    if (enemy instanceof EnemyThree) {
                        ((Hero) fixA.getUserData()).onDead();
                    } else {
                        enemy.onHit();
                    }
                }
                break;

            // Hero - Enemy's weapon
            case Constants.HERO_BIT | Constants.ENEMY_WEAPON_BIT:
                fixC = fixA.getFilterData().categoryBits == Constants.HERO_BIT ? fixA : fixB;
                ((Hero) fixC.getUserData()).onDead();
                break;

            // HeroTough - Enemy's weapon
            case Constants.HERO_TOUGH_BIT | Constants.ENEMY_WEAPON_BIT:
                fixC = fixA.getFilterData().categoryBits == Constants.ENEMY_WEAPON_BIT ? fixA : fixB;
                ((Weapon) fixC.getUserData()).onBounce();
                break;

            // Enemy - Borders
            case Constants.ENEMY_BIT | Constants.BORDER_BIT:
                fixC = fixA.getFilterData().categoryBits == Constants.ENEMY_BIT ? fixA : fixB;
                ((Enemy) fixC.getUserData()).onBump();
                break;

            // Enemy - InteractiveTileObjects
            case Constants.ENEMY_BIT | Constants.OBSTACLE_BIT:
            case Constants.ENEMY_BIT | Constants.PATH_BIT:
                fixC = fixA.getFilterData().categoryBits == Constants.ENEMY_BIT ? fixA : fixB;
                ((Enemy) fixC.getUserData()).onBumpWithFeint();
                break;

            // Enemy - PowerBox
            case Constants.ENEMY_BIT | Constants.POWERBOX_BIT:
                fixC = fixA.getFilterData().categoryBits == Constants.ENEMY_BIT ? fixA : fixB;
                ((Enemy) fixC.getUserData()).onBumpWithFeint();
                break;

            // Enemy - Enemy
            case Constants.ENEMY_BIT | Constants.ENEMY_BIT:
                ((Enemy) fixA.getUserData()).onBump();
                ((Enemy) fixB.getUserData()).onBump();
                break;

            // Item - InteractiveTileObjects
            case Constants.ITEM_BIT | Constants.BORDER_BIT:
            case Constants.ITEM_BIT | Constants.OBSTACLE_BIT:
            case Constants.ITEM_BIT | Constants.PATH_BIT:
                fixC = fixA.getFilterData().categoryBits == Constants.ITEM_BIT ? fixA : fixB;
                ((Item) fixC.getUserData()).onBump();
                break;

            // Item - Enemy
            case Constants.ITEM_BIT | Constants.ENEMY_BIT:
                fixC = fixA.getFilterData().categoryBits == Constants.ITEM_BIT ? fixA : fixB;
                ((Item) fixC.getUserData()).onBump();
                break;

            // Item - PowerBox
            case Constants.ITEM_BIT | Constants.POWERBOX_BIT:
                fixC = fixA.getFilterData().categoryBits == Constants.ITEM_BIT ? fixA : fixB;
                ((Item) fixC.getUserData()).onBump();
                break;

            // Item - Item
            case Constants.ITEM_BIT | Constants.ITEM_BIT:
                ((Item) fixA.getUserData()).onBump();
                ((Item) fixB.getUserData()).onBump();
                break;

            // Hero's weapon - InteractiveTileObjects & Items
            case Constants.HERO_WEAPON_BIT | Constants.BORDER_BIT:
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
            case Constants.ENEMY_WEAPON_BIT | Constants.BORDER_BIT:
                fixC = fixA.getFilterData().categoryBits == Constants.ENEMY_WEAPON_BIT ? fixA : fixB;
                ((Weapon) fixC.getUserData()).onTarget();
                break;

            // Final enemy - Borders
            case Constants.FINAL_ENEMY_BIT | Constants.BORDER_BIT:
                fixC = fixA.getFilterData().categoryBits == Constants.FINAL_ENEMY_BIT ? fixA : fixB;
                ((FinalEnemy) fixC.getUserData()).onHitWall(true);
                break;

            // Final enemy - Edges
            case Constants.FINAL_ENEMY_BIT | Constants.EDGE_BIT:
                fixC = fixA.getFilterData().categoryBits == Constants.FINAL_ENEMY_BIT ? fixA : fixB;
                ((FinalEnemy) fixC.getUserData()).onHitWall(false);
                break;

            // Final enemy - Hero's weapon
            case Constants.FINAL_ENEMY_BIT | Constants.HERO_WEAPON_BIT:
                if (fixA.getFilterData().categoryBits == Constants.HERO_WEAPON_BIT) {
                    ((FinalEnemy) fixB.getUserData()).onHit(((Weapon) fixA.getUserData()));
                } else {
                    ((FinalEnemy) fixA.getUserData()).onHit(((Weapon) fixB.getUserData()));
                }
                break;

            // Final enemy - Hero
            case Constants.FINAL_ENEMY_BIT | Constants.HERO_BIT:
                fixC = fixA.getFilterData().categoryBits == Constants.HERO_BIT ? fixA : fixB;
                ((Hero) fixC.getUserData()).onDead();
                break;

            // Shield - Enemy's weapon
            case Constants.SHIELD_BIT | Constants.ENEMY_WEAPON_BIT:
                fixC = fixA.getFilterData().categoryBits == Constants.ENEMY_BIT ? fixA : fixB;
                ((Weapon) fixC.getUserData()).onBounce();
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        Fixture fixC;

        int collisionDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        switch (collisionDef) {
            // HeroTough - Enemy's weapon
            // Avoid bouncing
            case Constants.HERO_TOUGH_BIT | Constants.ENEMY_WEAPON_BIT:
                fixC = fixA.getFilterData().categoryBits == Constants.HERO_TOUGH_BIT ? fixA : fixB;
                ((Hero) fixC.getUserData()).stop();
                break;

            // Shield - Enemy/Enemy's weapon
            // Avoid bouncing
            case Constants.SHIELD_BIT | Constants.ENEMY_BIT:
            case Constants.SHIELD_BIT | Constants.ENEMY_WEAPON_BIT:
                fixC = fixA.getFilterData().categoryBits == Constants.SHIELD_BIT ? fixA : fixB;
                ((Hero) fixC.getUserData()).stop();
                break;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
