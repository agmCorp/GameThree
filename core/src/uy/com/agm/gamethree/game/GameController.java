package uy.com.agm.gamethree.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import uy.com.agm.gamethree.tools.Constants;

/**
 * Created by AGM on 12/6/2017.
 */

public class GameController implements GestureDetector.GestureListener, InputProcessor {
    private static final String TAG = GameController.class.getName();
    private GameThree game;

    public GameController(GameThree game) {
        this.game = game;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        game.playScreen.player.openFire();
        Gdx.app.debug(TAG, "fuego!!");
        return true;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
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
            * origin.x = game.playScreen.player.b2body.getPosition().x
            * origin.y = game.playScreen.player.b2body.getPosition().y
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
            if (Math.abs(game.playScreen.player.b2body.getLinearVelocity().angle() - newVelocity.angle()) > Constants.HERO_ANGLE_SENSIBILITY_DEGREES) {
                game.playScreen.player.b2body.setLinearVelocity(newVelocity);
            }
        } else {
            game.playScreen.player.b2body.setLinearVelocity(0, 0);
        }
        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        game.playScreen.player.b2body.setLinearVelocity(0, 0);
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
        // Control our player using linear velocity
        if (keycode == Input.Keys.UP){
            game.playScreen.player.b2body.setLinearVelocity(game.playScreen.player.b2body.getLinearVelocity().x, Constants.HERO_LINEAR_VELOCITY);
        }
        if (keycode == Input.Keys.DOWN) {
            game.playScreen.player.b2body.setLinearVelocity(game.playScreen.player.b2body.getLinearVelocity().x, -Constants.HERO_LINEAR_VELOCITY);
        }
        if (keycode == Input.Keys.LEFT) {
            game.playScreen.player.b2body.setLinearVelocity(-Constants.HERO_LINEAR_VELOCITY, game.playScreen.player.b2body.getLinearVelocity().y);
        }
        if (keycode == Input.Keys.RIGHT) {
            game.playScreen.player.b2body.setLinearVelocity(Constants.HERO_LINEAR_VELOCITY, game.playScreen.player.b2body.getLinearVelocity().y);
        }
        if (keycode == Input.Keys.SPACE) {
            game.playScreen.player.openFire();
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        // Control our player using linear velocity
        if (keycode == Input.Keys.UP || keycode == Input.Keys.DOWN){
            game.playScreen.player.b2body.setLinearVelocity(game.playScreen.player.b2body.getLinearVelocity().x, 0);
        }

        if (keycode == Input.Keys.LEFT || keycode == Input.Keys.RIGHT) {
            game.playScreen.player.b2body.setLinearVelocity(0, game.playScreen.player.b2body.getLinearVelocity().y);
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

    public void touchToWorld(Vector3 touch) {
        game.playScreen.gameCam.unproject(touch);
    }
}
