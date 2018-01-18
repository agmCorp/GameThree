package uy.com.agm.gamethree.screens.util;

import uy.com.agm.gamethree.screens.*;

/**
 * Created by AGM on 1/18/2018.
 */

public enum ScreenEnum {

    MAIN_MENU {
        public AbstractScreen getScreen(Object... params) {
            return new MainMenuScreen();
        }
    },

    PREFERENCES {
        public AbstractScreen getScreen(Object... params) {
            return new PreferencesScreen();
        }
    },

    LEVEL_SELECT {
        public AbstractScreen getScreen(Object... params) {
            return new LevelSelectScreen();
        }
    },

    GAME {
        public AbstractScreen getScreen(Object... params) {
            return new PlayScreen((Integer) params[0]);
        }
    },

    GAME_OVER {
        public AbstractScreen getScreen(Object... params) {
            return new GameOverScreen();
        }
    };

    public abstract AbstractScreen getScreen(Object... params);
}