package uy.com.agm.gamethree.sprites.finals;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.game.Constants;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.items.ItemCreator;
import uy.com.agm.gamethree.sprites.weapons.Weapon;
import uy.com.agm.gamethree.tools.AudioManager;

/**
 * Created by AGM on 1/20/2018.
 */

public abstract class FinalEnemy extends Sprite {
    private static final String TAG = FinalEnemy.class.getName();

    protected World world;
    protected PlayScreen screen;
    protected Body b2body;
    protected Vector2 velocity;
    protected Vector2 tmp; // Temp GC friendly vector
    protected StateFinalEnemy currentStateFinalEnemy;
    private float introTimer;
    private boolean playingIntro;
    private float helpSilverBulletTimer;

    protected enum StateFinalEnemy {
        INACTIVE, WALKING, IDLE, SHOOTING, INJURED, DYING, EXPLODING, DEAD
    }

    protected enum PowerState {
        NORMAL, POWERFUL
    }

    public FinalEnemy(PlayScreen screen, float x, float y, float width, float height) {
        this.world = screen.getWorld();
        this.screen = screen;
        this.velocity = new Vector2();

        // Temp GC friendly vector
        tmp = new Vector2();

        /* Set this Sprite's bounds on the lower left vertex of a Rectangle.
        * This point will be used by defineFinalEnemy() calling getX(), getY() to center its b2body.
        * SetBounds always receives world coordinates.
        */
        setBounds(x, y, width, height);
        defineFinalEnemy();

        introTimer = 0;
        playingIntro = false;
        helpSilverBulletTimer = 0;

        // By default this FinalEnemy doesn't interact in our world
        b2body.setActive(false);
    }

    // This FinalEnemy can be removed from our game
    public boolean isDisposable() {
        return currentStateFinalEnemy == StateFinalEnemy.DEAD;
    }

    // This FinalEnemy doesn't have any b2body
    public boolean isDestroyed() {
        return currentStateFinalEnemy == StateFinalEnemy.DYING ||
                currentStateFinalEnemy == StateFinalEnemy.EXPLODING ||
                currentStateFinalEnemy == StateFinalEnemy.DEAD;
    }

    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
    }

    public void update(float dt) {
        if (currentStateFinalEnemy == StateFinalEnemy.INACTIVE) {
            // When our final enemy is on camera, it activates
            if (finalEnemyStarts()) {
                // Pause music for a few seconds
                AudioManager.getInstance().pauseMusic();
                introTimer = 0;
                playingIntro = true;

                // Audio FX
                AudioManager.getInstance().play(Assets.getInstance().getSounds().getFinalEnemyIntro());

                // HealthBar
                screen.getHud().showHealthBarInfo(getFinalEnemyName(), getFinalEnemyDamage());

                // Change Hero's weapon
                screen.getPlayer().applySilverBullet();

                // Initial state
                setInitialState();

                // update method
                updateLogic(dt);
            }
        } else {
            if (playingIntro) {
                introTimer += dt;
                if (introTimer > Constants.FINALENEMY_INTRO_TIME_SECONDS) {
                    if (!screen.getPlayer().isHeroDead() && !isDestroyed()) {
                        AudioManager.getInstance().resumeMusic();
                    }
                    playingIntro = false;
                }
            }

            // Release silver bullets if needed
            if (screen.getPlayer().getSilverBullets() <= 0) {
                helpSilverBulletTimer += dt;
                if (helpSilverBulletTimer > Constants.FINALENEMY_SILVERBULLET_HELP_TIME_SECONDS) {
                    releaseSilverBullet();
                    helpSilverBulletTimer = 0;
                }
            }

            updateLogic(dt);
        }
    }

    private void releaseSilverBullet() {
        MapObject object;
        int max = MathUtils.random(1, Constants.FINALENEMY_MAX_SILVERBULLET_HELP);

        for(int i = 0; i < max; i++) {
            object = new MapObject();
            object.getProperties().put("colSilverBullet", "");
            ItemCreator.getItemOnHit(object, screen.getCreator(), b2body.getPosition().x, b2body.getPosition().y + Constants.ITEM_OFFSET_METERS);
        }
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }

    protected abstract void defineFinalEnemy();
    protected abstract void updateLogic(float dt);
    public abstract void onHit(Weapon weapon);
    public abstract void onHitWall(boolean isBorder);
    protected abstract boolean finalEnemyStarts();
    protected abstract String getFinalEnemyName();
    protected abstract int getFinalEnemyDamage();
    protected abstract void setInitialState();
}
