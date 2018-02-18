package uy.com.agm.gamethree.tools;

import uy.com.agm.gamethree.game.Constants;

/**
 * Created by AGM on 2/13/2018.
 */

public class DynamicHelpDef {
    private boolean modal;
    private float seconds;

    public DynamicHelpDef() {
        this.modal = false;
        this.seconds = Constants.HUD_DEFAULT_HELP_SECONDS;
    }

    public DynamicHelpDef(float seconds) {
        this.modal = false;
        this.seconds = seconds;
    }

    public DynamicHelpDef(boolean modal) {
        this.modal = modal;
        this.seconds = 0;
    }

    public boolean isModal() {
        return modal;
    }

    public float getSeconds() {
        return seconds;
    }
}
