package uy.com.agm.gamethree.screens.util;

import com.badlogic.gdx.math.Interpolation;

import uy.com.agm.gamethree.game.GameThree;
import uy.com.agm.gamethree.screens.AbstractScreen;
import uy.com.agm.gamethree.screens.transitions.RotatingTransition;
import uy.com.agm.gamethree.screens.transitions.ScreenTransition;

/**
 * Created by AGM on 1/18/2018.
 */

public class ScreenManager {
    private static final String TAG = ScreenManager.class.getName();

    // Singleton: unique instance
    private static ScreenManager instance;

    // Reference to game
    private GameThree game;

    // Singleton: prevent instantiation from other classes
    private ScreenManager() {
    }

    // Singleton: retrieve instance
    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    // Initialization with the game class
    public void initialize(GameThree game) {
        this.game = game;
    }

    // Reference to game
    public GameThree getGame() {
        return game;
    }

    // Show in the game the screen which enum type is received
    public void showScreen(ScreenEnum screenEnum, Object... params) {
        // TODO
        ScreenTransition transition = RotatingTransition.init(2, Interpolation.pow2Out, 720, RotatingTransition.TransitionScaling.IN);

        // Show new screen
        AbstractScreen newScreen = screenEnum.getScreen(params);
        game.setScreen(newScreen, transition);
    }
}