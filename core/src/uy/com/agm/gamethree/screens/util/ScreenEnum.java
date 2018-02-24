package uy.com.agm.gamethree.screens.util;

import uy.com.agm.gamethree.screens.*;
import uy.com.agm.gamethree.screens.SettingsScreen;

/**
 * Created by AGM on 1/18/2018.
 */

public enum ScreenEnum {

    MAIN_MENU {
        public AbstractScreen getScreen(Object... params) {
            return new MainMenuScreen();
        }
    },

    SETTINGS {
        public AbstractScreen getScreen(Object... params) {
            return new SettingsScreen();
        }
    },

    SELECT_LEVEL {
        public AbstractScreen getScreen(Object... params) {
            return new SelectLevelScreen();
        }
    },

    GAME {
        public AbstractScreen getScreen(Object... params) {
            return new PlayScreen((Integer) params[0], (Integer) params[1]);
        }
    },

    LEVEL_COMPLETED {
        public AbstractScreen getScreen(Object... params) {
            return new LevelCompletedScreen((Integer) params[0], (Integer) params[1]);
        }
    },

    GAME_OVER {
        public AbstractScreen getScreen(Object... params) {
            return new GameOverScreen();
        }
    };

    public abstract AbstractScreen getScreen(Object... params);
}