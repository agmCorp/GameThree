package uy.com.agm.gamethree.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.finals.FinalEnemy;
import uy.com.agm.gamethree.sprites.player.Hero;
import uy.com.agm.gamethree.tools.Vector2Util;

/**
 * Created by AGM on 12/6/2017.
 */

public class GameController implements GestureDetector.GestureListener, InputProcessor {
    private static final String TAG = GameController.class.getName();

    // Constants
    public static final float ALPHA_LERP = 0.2f;

    private Vector2 candidateVelocity; // Temp GC friendly vector
    private Vector2 heroVelocity;
    private PlayScreen screen;
    private Hero player;
    private FinalEnemy finalEnemy;

    public GameController(PlayScreen screen) {
        candidateVelocity = new Vector2(0.0f, 0.0f);
        heroVelocity = new Vector2(0.0f, 0.0f);
        this.screen = screen;
        this.player = screen.getPlayer();
        this.finalEnemy = screen.getFinalEnemy();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        // If Hero is dead, we don't handle any input
        if (GameSettings.getInstance().isManualShooting() || player.isSilverBulletEnabled()) {
            if (!player.isHeroDead() && !finalEnemy.isDestroyed()) {
                player.openFire();
            }
        }
        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        // If Hero is dead, we don't handle any input
        if(!player.isHeroDead()) {
            /*
            * DeltaX is positive when I move my finger to the left, negative otherwise.
            * DeltaY is positive when I move my finger down, negative otherwise.
            * Both are in pixels, thus to get meters I must divide them by Constants.PPM.
            */

            // In b2body y-axes sign is the opposite.
            deltaY = -deltaY;

            // Go from origin to target at constant speed
            candidateVelocity.set(player.getB2body().getPosition().x, player.getB2body().getPosition().y);
            Vector2Util.goToTarget(candidateVelocity, player.getB2body().getPosition().x + deltaX / PlayScreen.PPM, player.getB2body().getPosition().y + deltaY / PlayScreen.PPM, Hero.LINEAR_VELOCITY);

            // Linear interpolation to avoid character shaking
            heroVelocity.lerp(candidateVelocity, ALPHA_LERP);

            // Apply the result
            player.getB2body().setLinearVelocity(heroVelocity);

            // Depending on the result, we change the animation if needed
            evaluateMovementDirection();
        }
        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        // If Hero is dead, we don't handle any input
        if(!player.isHeroDead()) {
            player.getB2body().setLinearVelocity(0, 0);
            evaluateMovementDirection();
        }
        return true;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
                         Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {
    }

    @Override
    public boolean keyDown(int keycode) {
        // If Hero is dead, we don't handle any input
        if(!player.isHeroDead()) {
            // Control our player using linear velocity
            if (keycode == Input.Keys.UP) {
                player.getB2body().setLinearVelocity(player.getB2body().getLinearVelocity().x, Hero.LINEAR_VELOCITY);
            }
            if (keycode == Input.Keys.DOWN) {
                player.getB2body().setLinearVelocity(player.getB2body().getLinearVelocity().x, -Hero.LINEAR_VELOCITY);
            }
            if (keycode == Input.Keys.LEFT) {
                player.getB2body().setLinearVelocity(-Hero.LINEAR_VELOCITY, player.getB2body().getLinearVelocity().y);
            }
            if (keycode == Input.Keys.RIGHT) {
                player.getB2body().setLinearVelocity(Hero.LINEAR_VELOCITY, player.getB2body().getLinearVelocity().y);
            }
            if (keycode == Input.Keys.SPACE) {
                if ((GameSettings.getInstance().isManualShooting() || player.isSilverBulletEnabled()) && !finalEnemy.isDestroyed()) {
                    player.openFire();
                }
            }
            evaluateMovementDirection();
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        // If Hero is dead, we don't handle any input
        if(!player.isHeroDead()) {
            // Control our player using linear velocity
            if (keycode == Input.Keys.UP || keycode == Input.Keys.DOWN) {
                player.getB2body().setLinearVelocity(player.getB2body().getLinearVelocity().x, 0);
            }
            if (keycode == Input.Keys.LEFT || keycode == Input.Keys.RIGHT) {
                player.getB2body().setLinearVelocity(0, player.getB2body().getLinearVelocity().y);
            }
            evaluateMovementDirection();
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private void evaluateMovementDirection() {
        float vy = player.getB2body().getLinearVelocity().y;
        float vx = player.getB2body().getLinearVelocity().x;

        // Test to Box2D for velocity on the y-axis.
        // If Hero is going positive in y-axis he is moving up.
        // If Hero is going negative in y-axis he is moving down.
        // Otherwise we check for velocity on the x-axis.
        if (vy > 0.0f) {
            player.onMovingUp();
        } else {
            if (vy < 0.0f) {
                player.onMovingDown();
            } else {
                if (vx != 0.0f) { // vy == 0
                    player.onMovingLeftRight();
                } else {
                    player.onStanding(); // vx == 0 && vy == 0
                }
            }
        }
    }
}
