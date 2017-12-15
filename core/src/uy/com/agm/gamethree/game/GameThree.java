package uy.com.agm.gamethree.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;

import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.Assets;
import uy.com.agm.gamethree.tools.AudioManager;

public class GameThree extends Game {
	private static final String TAG = GameThree.class.getName();
	public static final int V_WIDTH = 480;
	public static final int V_HEIGHT = 800;
	public static final float PPM = 100;

	public static final short DEFAULT_BIT = 1; // bordes
	public static final short HERO_BIT = 2;
	public static final short OBSTACLE_BIT = 4; // arboles
	public static final short COINBOX_BIT = 8;
	public static final short COINBOXOPENED_BIT = 16; // lo quiero sacar
	public static final short ENEMY_BIT = 32;



	public SpriteBatch batch;
	public PlayScreen playScreen;

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		// Load assets
		Assets.instance.init(new AssetManager());

		// Load preferences for audio settings and start playing music
		//GamePreferences.instance.load();
		AudioManager.instance.play(Assets.instance.music.songLevel1);

		batch = new SpriteBatch();
		playScreen = new PlayScreen(this);
		setScreen(playScreen);
		Gdx.input.setInputProcessor(getInputProcessor(new GameController(this)));
	}

	private InputProcessor getInputProcessor(GameController gc) {
		/*
		 * GameController es un InputAdapter porque extiende dicha clase y
		 * también es un GestureListener porque implementa dicha interfaz. En
		 * GameController entonces puedo reconocer gestos (como fling) y puedo
		 * reconocer eventos como touchUp que no existe dentro de la interfaz
		 * GestureListener sino que existe dentro de un InputAdapter. Como los
		 * métodos de InputAdapter son demasiados, decidí extender de dicha
		 * clase (para implementear dentro de GameController solo el método que
		 * me interesa) e implemento la interfaz GestureListener porque después
		 * de todo son pocos métodos extra que debo declarar que no use.
		 * Para trabajar con ambos InputProcessors a la vez, debo usar un
		 * InputMultiplexer. Los eventos fling y touchUp, por ejemplo, se
		 * ejecutan a la vez siempre. Primero registré GestureDetector para que
		 * fling se ejecute antes que touchUp y como están relacionados, al
		 * retornar true en el evento fling se cancela el touchUp. Si retorno
		 * false se ejecutan ambos.
		 */
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(new GestureDetector(gc));
		multiplexer.addProcessor(gc);
		return multiplexer;
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
	}
}
