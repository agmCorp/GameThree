package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.scene2d.AssetGameOver;
import uy.com.agm.gamethree.assets.scene2d.AssetScene2d;
import uy.com.agm.gamethree.game.DebugConstants;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.UIFactory;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.widget.AnimatedImage;

/**
 * Created by AGM on 12/23/2017.
 */

public class GameOverScreen extends AbstractScreen {
    private static final String TAG = GameOverScreen.class.getName();

    public GameOverScreen() {
        super();

        // Audio FX
        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getGameOver());
    }

    @Override
    public void buildStage() {
        defineMainTable();
        defineNavigationTable();
    }

    private void defineMainTable() {
        // UI assets
        AssetScene2d assetScene2d = Assets.getInstance().getScene2d();

        // Game assets
        Assets assetGame = Assets.getInstance();

        // Set table structure
        Table table = new Table();

        // Design
        table.setBackground(new TextureRegionDrawable(assetScene2d.getTable()));

        // Debug lines
        table.setDebug(DebugConstants.DEBUG_LINES);

        // Align table
        table.padBottom(AbstractScreen.PAD * 4);

        // Make the table fill the entire stage
        table.setFillParent(true);

        // Animation
        AnimatedImage dizzy = new AnimatedImage(assetGame.getDizzy().getDizzyAnimation());
        dizzy.setAlign(Align.center);

        // Animation
        AnimatedImage heroDeath = new AnimatedImage(assetGame.getHero().getHeroDeathAnimation());
        heroDeath.setAlign(Align.center);

        // Animation
        AnimatedImage animatedImage = new AnimatedImage(assetScene2d.getGameOver().getGameOverAnimation());
        animatedImage.setAlign(Align.center);

        // Add values
        table.add(dizzy);
        table.row();
        table.add(heroDeath);
        table.row();
        table.add(animatedImage).size(AssetGameOver.WIDTH_PIXELS, AssetGameOver.HEIGHT_PIXELS);

        // Adds table to stage
        addActor(table);
    }

    private void defineNavigationTable() {
        // UI assets
        AssetScene2d assetScene2d = Assets.getInstance().getScene2d();

        // Set table structure
        Table table = new Table();

        // Debug lines
        table.setDebug(DebugConstants.DEBUG_LINES);

        // Bottom-Align table
        table.bottom();

        // Make the table fill the entire stage
        table.setFillParent(true);

        // Define images
        ImageButton back = new ImageButton(new TextureRegionDrawable(assetScene2d.getBack()),
                new TextureRegionDrawable(assetScene2d.getBackPressed()));

        // Add values
        table.add(back).padBottom(AbstractScreen.PAD * 2);

        // Events
        back.addListener(UIFactory.screenNavigationListener(ScreenEnum.MAIN_MENU));

        // Adds table to stage
        addActor(table);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
