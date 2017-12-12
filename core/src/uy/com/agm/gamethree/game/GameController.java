package uy.com.agm.gamethree.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

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
        final float FRICTION = 0.18f; // porcentaje de la velocidad
        final float MIN_DELTA = 100.0f / GameThree.PPM;
        float velocidad_camara = game.playScreen.gameCam.position.y;

        Vector2 delta = new Vector2(deltaX / GameThree.PPM, deltaY / GameThree.PPM);

        if (delta.len() > 5.0f / GameThree.PPM) {
            Gdx.app.debug(TAG, "norma: " + delta.len());
            game.playScreen.player.b2body.setLinearVelocity(deltaX * FRICTION, -deltaY * FRICTION);
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
