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

    private Vector2 candidateVelocity; // Temp GC friendly vector
    private Vector2 heroVelocity;
    private Hero player;

    public GameController(Hero player) {
        candidateVelocity = new Vector2(0.0f, 0.0f);
        heroVelocity = new Vector2(0.0f, 0.0f);
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
            * Both are in pixels, thus to get meters I must divide by Constants.PPM.
            */

            // In b2body y-axes sign is the opposite.
            deltaY = -deltaY;



            /*
            * origin.x = player.getB2body().getPosition().x
            * origin.y = player.getB2body().getPosition().y
            *
            * destination.x = origin.x + deltaX / Constants.PPM
            * destination.y = origin.y + deltaY / Constants.PPM
            *
            * To go from origin to destination we must subtract their position vectors: destination - origin.
            * Thus, destination - origin is (deltaX / Constants.PPM, deltaY / Constants.PPM).
            */
            candidateVelocity.x = deltaX / Constants.PPM;
            candidateVelocity.y = deltaY / Constants.PPM;

            // Get the direction of the previous vector (normalization)
            candidateVelocity.nor();

            // Apply constant velocity on that direction
            candidateVelocity.x = candidateVelocity.x * Constants.HERO_LINEAR_VELOCITY;
            candidateVelocity.y = candidateVelocity.y * Constants.HERO_LINEAR_VELOCITY;

            // Linear interpolation to avoid character shaking
            heroVelocity.lerp(candidateVelocity, Constants.HERO_ALPHA_LERP);

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
