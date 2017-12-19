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
        Vector2 delta = new Vector2(deltaX / Constants.PPM, deltaY / Constants.PPM);

        if (delta.len() > Constants.LEN_HERO_SPEED / Constants.PPM) {
            game.playScreen.player.b2body.setLinearVelocity(deltaX * Constants.WEIGHTING_HERO_SPEED, -deltaY * Constants.WEIGHTING_HERO_SPEED);
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

    public boolean insideViewPort(Vector3 touch) {
        return game.playScreen.gameCam.frustum.pointInFrustum(touch);
    }
}
