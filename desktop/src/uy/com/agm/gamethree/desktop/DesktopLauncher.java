package uy.com.agm.gamethree.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import uy.com.agm.gamethree.game.GameThree;
import uy.com.agm.gamethree.screens.AbstractScreen;

public class DesktopLauncher {
	private static final String TAG = DesktopLauncher.class.getName();

	// Constants
	private static final String TITLE = "Wipe them out!";

    public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = TITLE;
		config.width = AbstractScreen.V_WIDTH;
		config.height = AbstractScreen.V_HEIGHT;
		new LwjglApplication(new GameThree(), config);
	}
}
