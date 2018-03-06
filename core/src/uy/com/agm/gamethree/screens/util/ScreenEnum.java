package uy.com.agm.gamethree.screens.util;

import uy.com.agm.gamethree.screens.AbstractScreen;
import uy.com.agm.gamethree.screens.GameOverScreen;
import uy.com.agm.gamethree.screens.LevelCompletedScreen;
import uy.com.agm.gamethree.screens.MainMenuScreen;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.screens.SelectLevelScreen;
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