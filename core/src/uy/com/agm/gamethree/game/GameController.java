package uy.com.agm.gamethree.game;

import com.badlogic.gdx.Gdx;
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
            Vector2 velocity = new Vector2(delta.x, delta.y);

            // Get the direction of the previous vector (normalization)
            velocity.nor();

            // Apply constant velocity on that direction
            velocity.x = velocity.x * Constants.HERO_LINEAR_VELOCITY;
            velocity.y = velocity.y * Constants.HERO_LINEAR_VELOCITY;
            game.playScreen.player.b2body.setLinearVelocity(velocity);
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
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
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
