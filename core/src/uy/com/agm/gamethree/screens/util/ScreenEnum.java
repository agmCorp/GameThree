package uy.com.agm.gamethree.screens.util;

import uy.com.agm.gamethree.screens.AbstractScreen;
import uy.com.agm.gamethree.screens.BigBossScreen;
import uy.com.agm.gamethree.screens.CreditsScreen;
import uy.com.agm.gamethree.screens.GameOverScreen;
import uy.com.agm.gamethree.screens.HelpOneScreen;
import uy.com.agm.gamethree.screens.HelpThreeScreen;
import uy.com.agm.gamethree.screens.HelpTwoScreen;
import uy.com.agm.gamethree.screens.HighScoresScreen;
import uy.com.agm.gamethree.screens.LevelCompletedScreen;
import uy.com.agm.gamethree.screens.MainMenuScreen;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.screens.SelectLevelScreen;
import uy.com.agm.gamethree.screens.SettingsScreen;

/**
 * Created by AGM on 1/18/2018.
 */

public enum ScreenEnum {
    SPLASH {
        public AbstractScreen getScreen(Object... params) {
            return new SplashScreen();
        }
    },

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

    HIGH_SCORES {
        public AbstractScreen getScreen(Object... params) {
            return new HighScoresScreen();
        }
    },

    HELP_ONE {
        public AbstractScreen getScreen(Object... params) {
            return new HelpOneScreen();
        }
    },

    HELP_TWO {
        public AbstractScreen getScreen(Object... params) {
            return new HelpTwoScreen();
        }
    },

    HELP_THREE {
        public AbstractScreen getScreen(Object... params) {
            return new HelpThreeScreen();
        }
    },

    CREDITS {
        public AbstractScreen getScreen(Object... params) {
            return new CreditsScreen();
        }
    },

    SELECT_LEVEL {
        public AbstractScreen getScreen(Object... params) {
            return new SelectLevelScreen();
        }
    },

    PLAY_GAME {
        public AbstractScreen getScreen(Object... params) {
            return new PlayScreen((Integer) params[0]);
        }
    },

    LEVEL_COMPLETED {
        public AbstractScreen getScreen(Object... params) {
            return new LevelCompletedScreen((Integer) params[0], (Integer) params[1], (Integer) params[2]);
        }
    },

    GRAND_FINALE {
        public AbstractScreen getScreen(Object... params) {
            return new BigBossScreen();
        }
    },

    GAME_OVER {
        public AbstractScreen getScreen(Object... params) {
            return new GameOverScreen();
        }
    };

    public abstract AbstractScreen getScreen(Object... params);
}