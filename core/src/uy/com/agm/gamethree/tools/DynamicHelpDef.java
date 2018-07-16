package uy.com.agm.gamethree.tools;

/**
 * Created by AGM on 2/13/2018.
 */

public class DynamicHelpDef {
    private static final String TAG = DynamicHelpDef.class.getName();

    // Constants
    public static final float DEFAULT_HELP_SECONDS = 3.0f;

    private boolean modal;
    private float seconds;

    public DynamicHelpDef() {
        this.modal = false;
        this.seconds = DEFAULT_HELP_SECONDS;
    }

    public DynamicHelpDef(float seconds) {
        this.modal = false;
        this.seconds = seconds;
    }

    public DynamicHelpDef(boolean modal) {
        this.modal = modal;
        this.seconds = modal ? 0 : DEFAULT_HELP_SECONDS;
    }

    public boolean isModal() {
        return modal;
    }

    public float getSeconds() {
        return seconds;
    }
}
