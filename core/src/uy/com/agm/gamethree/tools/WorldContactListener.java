package uy.com.agm.gamethree.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import uy.com.agm.gamethree.actors.backgroundObjects.kinematicObjects.Bridge;
import uy.com.agm.gamethree.actors.backgroundObjects.staticObjects.PowerBox;
import uy.com.agm.gamethree.actors.enemies.Enemy;
import uy.com.agm.gamethree.actors.enemies.EnemyThree;
import uy.com.agm.gamethree.actors.finals.FinalEnemy;
import uy.com.agm.gamethree.actors.items.Item;
import uy.com.agm.gamethree.actors.player.Hero;
import uy.com.agm.gamethree.actors.backgroundObjects.staticObjects.StaticBackgroundObject;
import uy.com.agm.gamethree.actors.weapons.Weapon;

/**
 * Created by AGM on 12/8/2017.
 */

public class WorldContactListener implements ContactListener {
    private static final String TAG = WorldContactListener.class.getName();

    /* Box2D Collision Bits
    * I can have up to 16 different categories for collision. If I require even finer control over what
    * should collide with what, I can set a contact filter callback in the world so that when Box2D
    * needs to check if two fixtures should collide, instead of using the following rules (that is filters -categoryBits, maskBits and groupIndex-)
    * it will give me the two fixtures and let me decide. See world.setContactFilter(...) and ContactFilter interface (shouldCollide(...))
    * I could then have a enum every entity holds showing its type and based on that, sort out whether the collision
    * should be handled or not in shouldCollide(...) method.
    */
    public static final short NOTHING_BIT = 0;
    public static final short BORDER_BIT = 1;
    public static final short EDGE_BIT = 2;
    public static final short OBSTACLE_BIT = 4;
    public static final short PATH_BIT = 8;
    public static final short HERO_BIT = 16;
    public static final short HERO_GHOST_BIT = 32;
    public static final short HERO_TOUGH_BIT = 64;
    public static final short POWER_BOX_BIT = 128;
    public static final short ENEMY_BIT = 256;
    public static final short ITEM_BIT = 512;
    public static final short HERO_WEAPON_BIT = 1024;
    public static final short ENEMY_WEAPON_BIT = 2048;
    public static final short FINAL_ENEMY_BIT = 4096;
    public static final short SHIELD_BIT = 8192;

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        Fixture fixC;

        int collisionDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        switch (collisionDef) {
            // Hero/HeroGhost/HeroTough - Border/Obstacle/Path/Bridge
            case HERO_BIT | BORDER_BIT:
            case HERO_BIT | OBSTACLE_BIT:
            case HERO_BIT | PATH_BIT:
            case HERO_GHOST_BIT | BORDER_BIT:
            case HERO_GHOST_BIT | OBSTACLE_BIT:
            case HERO_GHOST_BIT | PATH_BIT:
            case HERO_TOUGH_BIT | BORDER_BIT:
            case HERO_TOUGH_BIT | OBSTACLE_BIT:
            case HERO_TOUGH_BIT | PATH_BIT:
                fixC = (fixA.getFilterData().categoryBits == BORDER_BIT ||
                        fixA.getFilterData().categoryBits == OBSTACLE_BIT ||
                        fixA.getFilterData().categoryBits == PATH_BIT) ? fixA : fixB;
                if (fixC.getUserData() instanceof Bridge) {
                    ((Bridge) fixC.getUserData()).onBump();
                } else {
                    ((StaticBackgroundObject) fixC.getUserData()).onBump();
                }
                break;

            // Hero/HeroGhost/HeroTough - PowerBox
            case HERO_BIT | POWER_BOX_BIT:
            case HERO_GHOST_BIT | POWER_BOX_BIT:
            case HERO_TOUGH_BIT | POWER_BOX_BIT:
                fixC = fixA.getFilterData().categoryBits == POWER_BOX_BIT ? fixA : fixB;
                ((PowerBox) fixC.getUserData()).onBump();
                break;

            // Hero/HeroGhost/HeroTough - Item
            case HERO_BIT | ITEM_BIT:
            case HERO_GHOST_BIT | ITEM_BIT:
            case HERO_TOUGH_BIT | ITEM_BIT:
                fixC = fixA.getFilterData().categoryBits == ITEM_BIT ? fixA : fixB;
                ((Item) fixC.getUserData()).onUse();
                break;

            // Hero - Enemies
            case HERO_BIT | ENEMY_BIT:
                fixC = fixA.getFilterData().categoryBits == HERO_BIT ? fixA : fixB;
                ((Hero) fixC.getUserData()).onDead();
                break;

            // HeroGhost - Enemies
            case HERO_GHOST_BIT | ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == ENEMY_BIT) {
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
            case HERO_TOUGH_BIT | ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == ENEMY_BIT) {
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
            case HERO_BIT | ENEMY_WEAPON_BIT:
                if (fixA.getFilterData().categoryBits == HERO_BIT) {
                    ((Hero) fixA.getUserData()).onDead();
                    ((Weapon) fixB.getUserData()).onTarget();
                } else {
                    ((Hero) fixB.getUserData()).onDead();
                    ((Weapon) fixA.getUserData()).onTarget();
                }
                break;

            // HeroTough - Enemy's weapon
            case HERO_TOUGH_BIT | ENEMY_WEAPON_BIT:
                fixC = fixA.getFilterData().categoryBits == ENEMY_WEAPON_BIT ? fixA : fixB;
                ((Weapon) fixC.getUserData()).onBounce();
                break;

            // Enemy - Border
            case ENEMY_BIT | BORDER_BIT:
                fixC = fixA.getFilterData().categoryBits == ENEMY_BIT ? fixA : fixB;
                ((Enemy) fixC.getUserData()).onBump();
                break;

            // Enemy - Obstacle/Path/Bridge
            case ENEMY_BIT | OBSTACLE_BIT:
            case ENEMY_BIT | PATH_BIT:
                fixC = fixA.getFilterData().categoryBits == ENEMY_BIT ? fixA : fixB;
                ((Enemy) fixC.getUserData()).onBumpWithFeint();
                break;

            // Enemy - PowerBox
            case ENEMY_BIT | POWER_BOX_BIT:
                fixC = fixA.getFilterData().categoryBits == ENEMY_BIT ? fixA : fixB;
                ((Enemy) fixC.getUserData()).onBumpWithFeint();
                break;

            // Enemy - Enemy
            case ENEMY_BIT | ENEMY_BIT:
                ((Enemy) fixA.getUserData()).onBump();
                ((Enemy) fixB.getUserData()).onBump();
                break;

            // Item - Border/Obstacle/Path/Bridge
            case ITEM_BIT | BORDER_BIT:
            case ITEM_BIT | OBSTACLE_BIT:
            case ITEM_BIT | PATH_BIT:
                fixC = fixA.getFilterData().categoryBits == ITEM_BIT ? fixA : fixB;
                ((Item) fixC.getUserData()).onBump();
                break;

            // Item - Enemy
            case ITEM_BIT | ENEMY_BIT:
                fixC = fixA.getFilterData().categoryBits == ITEM_BIT ? fixA : fixB;
                ((Item) fixC.getUserData()).onBump();
                break;

            // Item - PowerBox
            case ITEM_BIT | POWER_BOX_BIT:
                fixC = fixA.getFilterData().categoryBits == ITEM_BIT ? fixA : fixB;
                ((Item) fixC.getUserData()).onBump();
                break;

            // Item - Item
            case ITEM_BIT | ITEM_BIT:
                ((Item) fixA.getUserData()).onBump();
                ((Item) fixB.getUserData()).onBump();
                break;

            // Hero's weapon - Border/Obstacle/Item
            case HERO_WEAPON_BIT | BORDER_BIT:
            case HERO_WEAPON_BIT | OBSTACLE_BIT:
            case HERO_WEAPON_BIT | ITEM_BIT:
                fixC = fixA.getFilterData().categoryBits == HERO_WEAPON_BIT ? fixA : fixB;
                ((Weapon) fixC.getUserData()).onTarget();
                break;

            // Hero's weapon - PowerBox
            case HERO_WEAPON_BIT | POWER_BOX_BIT:
                if (fixA.getFilterData().categoryBits == HERO_WEAPON_BIT) {
                    ((Weapon) fixA.getUserData()).onTarget();
                    ((PowerBox) fixB.getUserData()).onHit();
                } else {
                    ((Weapon) fixB.getUserData()).onTarget();
                    ((PowerBox) fixA.getUserData()).onHit();
                }
                break;

            // Hero's weapon - Enemy
            case HERO_WEAPON_BIT | ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == HERO_WEAPON_BIT) {
                    ((Enemy) fixB.getUserData()).onHit((Weapon) fixA.getUserData());
                } else {
                    ((Enemy) fixA.getUserData()).onHit((Weapon) fixB.getUserData());
                }
                break;

            // Enemy's weapon - Border
            case ENEMY_WEAPON_BIT | BORDER_BIT:
                fixC = fixA.getFilterData().categoryBits == ENEMY_WEAPON_BIT ? fixA : fixB;
                ((Weapon) fixC.getUserData()).onTarget();
                break;

            // Final enemy - Border
            case FINAL_ENEMY_BIT | BORDER_BIT:
                fixC = fixA.getFilterData().categoryBits == FINAL_ENEMY_BIT ? fixA : fixB;
                ((FinalEnemy) fixC.getUserData()).onHitWall(true);
                break;

            // Final enemy - Edges
            case FINAL_ENEMY_BIT | EDGE_BIT:
                fixC = fixA.getFilterData().categoryBits == FINAL_ENEMY_BIT ? fixA : fixB;
                ((FinalEnemy) fixC.getUserData()).onHitWall(false);
                break;

            // Final enemy - Hero's weapon
            case FINAL_ENEMY_BIT | HERO_WEAPON_BIT:
                if (fixA.getFilterData().categoryBits == HERO_WEAPON_BIT) {
                    ((FinalEnemy) fixB.getUserData()).onHit(((Weapon) fixA.getUserData()));
                } else {
                    ((FinalEnemy) fixA.getUserData()).onHit(((Weapon) fixB.getUserData()));
                }
                break;

            // Final enemy - Hero
            case FINAL_ENEMY_BIT | HERO_BIT:
                fixC = fixA.getFilterData().categoryBits == HERO_BIT ? fixA : fixB;
                ((Hero) fixC.getUserData()).onDead();
                break;

            // Shield - Enemy's weapon
            case SHIELD_BIT | ENEMY_WEAPON_BIT:
                fixC = fixA.getFilterData().categoryBits == ENEMY_BIT ? fixA : fixB;
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
            // Sensor/Hero - Edge
            // Sensor and Hero use the same categoryBit but we are only interested in the sensor here
            case HERO_BIT | EDGE_BIT:
                fixC = fixA.getFilterData().categoryBits == HERO_BIT ? fixA : fixB;
                if (fixC.isSensor() && fixC.getUserData() != null) { // See Hero.setDefaultFixture()
                    ((Hero) fixC.getUserData()).checkSmashingCollision();
                }
                break;

            // Hero - Item
            // Avoid bouncing
            case HERO_BIT | ITEM_BIT:
                fixC = fixA.getFilterData().categoryBits == HERO_BIT ? fixA : fixB;
                ((Hero) fixC.getUserData()).stop();
                break;

            // HeroGhost - Item
            // Avoid bouncing
            case HERO_GHOST_BIT | ITEM_BIT:
                fixC = fixA.getFilterData().categoryBits == HERO_GHOST_BIT ? fixA : fixB;
                ((Hero) fixC.getUserData()).stop();
                break;

            // HeroTough - Item/Enemy's weapon
            // Avoid bouncing
            case HERO_TOUGH_BIT | ITEM_BIT:
            case HERO_TOUGH_BIT | ENEMY_WEAPON_BIT:
                fixC = fixA.getFilterData().categoryBits == HERO_TOUGH_BIT ? fixA : fixB;
                ((Hero) fixC.getUserData()).stop();
                break;

            // Shield - Item/Enemy/Enemy's weapon
            // Avoid bouncing
            case SHIELD_BIT | ITEM_BIT:
            case SHIELD_BIT | ENEMY_BIT:
            case SHIELD_BIT | ENEMY_WEAPON_BIT:
                fixC = fixA.getFilterData().categoryBits == SHIELD_BIT ? fixA : fixB;
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
