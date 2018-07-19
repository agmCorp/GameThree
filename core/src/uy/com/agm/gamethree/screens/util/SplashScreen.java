package uy.com.agm.gamethree.screens.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.screens.AbstractScreen;
import uy.com.agm.gamethree.widget.AnimatedActor;

/**
 * Created by AGM on 12/23/2017.
 */

public class SplashScreen extends AbstractScreen {
    private static final String TAG = SplashScreen.class.getName();

    // Constants
    private static final String TEXTURE_ATLAS_SPLASH_SCREEN = "atlas/loading/loading.atlas";
    private static final float LOGO_OFFSET_Y = 100.0f;
    private static final float START_X = 35.0f;
    private static final float END_X = 430.0f;
    private static final float PIVOT = 450.0f;
    private static final float LOADING_BACKGROUND_HEIGHT = 55.0f;
    private static final float MIN_SPLASH_TIME = 3.0f;

    private AssetManager assetManager;
    private float splashTime;

    private Image logo;
    private Image loadingFrame;
    private Image loadingBarHidden;
    private Image screenBg;
    private Image loadingBg;

    private float startX, endX;
    private float percent;

    private Actor loadingBar;

    public SplashScreen() {
        super();
        this.assetManager = new AssetManager();
        splashTime = 0;
    }

    @Override
    public void buildStage() {
        // Tell the manager to load assets for the loading screen
        assetManager.load(TEXTURE_ATLAS_SPLASH_SCREEN, TextureAtlas.class);

        // Wait until they are finished loading
        assetManager.finishLoading();

        // Get our texture atlas from the manager
        TextureAtlas atlas = assetManager.get(TEXTURE_ATLAS_SPLASH_SCREEN, TextureAtlas.class);

        // Grab the regions from the atlas and create some images
        logo = new Image(atlas.findRegion("logo"));
        loadingFrame = new Image(atlas.findRegion("loadingFrame"));
        loadingBarHidden = new Image(atlas.findRegion("loadingBarHidden"));
        screenBg = new Image(atlas.findRegion("screenBg"));
        loadingBg = new Image(atlas.findRegion("loadingFrameBg"));

        // Add the loading bar animation
        Animation anim = new Animation(0.05f, atlas.findRegions("loadingBarAnim"), Animation.PlayMode.LOOP_REVERSED);
        loadingBar = new AnimatedActor(anim);

        // Or if you only need a static bar, you can use loadingBar1 or loadingBar2
        // loadingBar = new Image(atlas.findRegion("loadingBar2"));

        // Make the background fill the screen
        screenBg.setSize(V_WIDTH, V_HEIGHT);

        // Place the logo in the middle of the screen and LOGO_OFFSET_Y px up
        logo.setX((V_WIDTH - logo.getWidth()) / 2);
        logo.setY((V_HEIGHT - logo.getHeight()) / 2 + LOGO_OFFSET_Y);

        // Place the loading frame in the middle of the screen
        loadingFrame.setX((getWidth() - loadingFrame.getWidth()) / 2);
        loadingFrame.setY((getHeight() - loadingFrame.getHeight()) / 2);

        // Place the loading bar at the same spot as the frame
        loadingBar.setX(loadingFrame.getX());
        loadingBar.setY(loadingFrame.getY());

        // The start position and how far to move the hidden loading bar
        startX = START_X;
        endX = END_X;
        percent = 0;

        // Place the image that will hide the bar on top of the bar
        loadingBarHidden.setX(startX);
        loadingBarHidden.setY(loadingBar.getY());

        // The rest of the hidden bar
        loadingBg.setSize(PIVOT, LOADING_BACKGROUND_HEIGHT);
        loadingBg.setX(loadingBarHidden.getX() + loadingBarHidden.getWidth());
        loadingBg.setY(loadingBarHidden.getY());

        // Add all the actors to the stage
        addActor(screenBg);
        addActor(loadingBar);
        addActor(loadingBg);
        addActor(loadingBarHidden);
        addActor(loadingFrame);
        addActor(logo);

        // Load the rest of assets asynchronously
        Assets.getInstance().init(assetManager);
    }

    @Override
    public void dispose() {
        super.dispose();

        // Dispose the loading assets as we no longer need them
        assetManager.unload(TEXTURE_ATLAS_SPLASH_SCREEN);
    }

    @Override
    public void render(float delta) {
        // Clear the screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        splashTime += delta;
        if (assetManager.update() && splashTime >= MIN_SPLASH_TIME) { // Load some, will return true if done loading
            Assets.getInstance().finishLoading();
            ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
        } else {
            // Interpolate the percentage to make it more smooth
            percent = Interpolation.linear.apply(percent, assetManager.getProgress(), 0.1f);

            // Update positions (and size) to match the percentage
            loadingBarHidden.setX(startX + endX * percent);
            loadingBg.setX(loadingBarHidden.getX() + loadingBarHidden.getWidth());
            loadingBg.setWidth(PIVOT - PIVOT * percent);
            loadingBg.invalidate();
        }

        // Show the loading screen
        act();
        draw();
    }
}
