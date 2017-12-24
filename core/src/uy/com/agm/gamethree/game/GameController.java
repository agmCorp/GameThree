package uy.com.agm.gamethree.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import uy.com.agm.gamethree.sprites.player.Hero;
import uy.com.agm.gamethree.tools.Constants;

/**
 * Created by AGM on 12/6/2017.
 */

public class GameController implements GestureDetector.GestureListener, InputProcessor {
    private static final String TAG = GameController.class.getName();

    private Hero player;

    public GameController(Hero player) {
        this.player = player;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        // If Hero is dead, we don't handle any input
        if(!player.isHeroDead()) {
            player.openFire();
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
         */

            // In b2body y-axes sign is the opposite.
            deltaY = -deltaY;

            // DeltaX and deltaY are in pixels, therefore delta is in metres.
            Vector2 delta = new Vector2(deltaX / Constants.PPM, deltaY / Constants.PPM);

            // Deltas too small are discarded
            if (delta.len() > Constants.HERO_SENSIBILITY_METERS) {
            /*
            * origin.x = player.getB2body().getPosition().x
            * origin.y = player.getB2body().getPosition().y
             *
             * destination.x = origin.x + delta.x
             * destination.y = origin.y + delta.y
             *
             * To go from origin to destination we must subtract their position vectors: destination - origin.
             * Thus destination - origin is (delta.x, delta.y).
             */
                Vector2 newVelocity = new Vector2(delta.x, delta.y);

                // Get the direction of the previous vector (normalization)
                newVelocity.nor();

                // Apply constant velocity on that direction
                newVelocity.x = newVelocity.x * Constants.HERO_LINEAR_VELOCITY;
                newVelocity.y = newVelocity.y * Constants.HERO_LINEAR_VELOCITY;

                // To avoid shaking, we only consider the newVelocity if its direction is slightly different from the direction of the actual velocity.
                // In order to determine the difference in both directions (actual and new) we calculate their angle.
                if (Math.abs(player.getB2body().getLinearVelocity().angle() - newVelocity.angle()) > Constants.HERO_ANGLE_SENSIBILITY_DEGREES) {
                    // Apply the new velocity
                    player.getB2body().setLinearVelocity(newVelocity);
                }
            } else {
                // Stop
                player.getB2body().setLinearVelocity(0, 0);
            }
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
                player.getB2body().setLinearVelocity(player.getB2body().getLinearVelocity().x, Constants.HERO_LINEAR_VELOCITY);
            }
            if (keycode == Input.Keys.DOWN) {
                player.getB2body().setLinearVelocity(player.getB2body().getLinearVelocity().x, -Constants.HERO_LINEAR_VELOCITY);
            }
            if (keycode == Input.Keys.LEFT) {
                player.getB2body().setLinearVelocity(-Constants.HERO_LINEAR_VELOCITY, player.getB2body().getLinearVelocity().y);
            }
            if (keycode == Input.Keys.RIGHT) {
                player.getB2body().setLinearVelocity(Constants.HERO_LINEAR_VELOCITY, player.getB2body().getLinearVelocity().y);
            }
            if (keycode == Input.Keys.SPACE) {
                player.openFire();
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
        } else if (vy < 0.0f) {
            player.onMovingDown();
        } else {
            if (vx != 0.0f) {
                player.onMovingLeftRight();
            } else {
                player.onStanding(); // vx == 0 && vy == 0
            }
        }
    }
}
