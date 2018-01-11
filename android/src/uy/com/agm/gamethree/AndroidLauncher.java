package uy.com.agm.gamethree;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import uy.com.agm.gamethree.game.GameThree;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		// We want to conserve battery
		config.useAccelerometer = false;
		config.useCompass = false;
		initialize(new GameThree(), config);
	}
}
