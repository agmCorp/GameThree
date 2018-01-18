package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.game.GameThree;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.UIFactory;

/**
 * Created by AGM on 12/23/2017.
 */

public class GameOverScreen extends AbstractScreen {
    private static final String TAG = GameOverScreen.class.getName();

    private GameThree game;

    public GameOverScreen() {
        super();
    }

    @Override
    public void buildStage() {
        // Personal fonts
        Label.LabelStyle labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.instance.fonts.defaultBig;

        Label.LabelStyle labelStyleSmall = new Label.LabelStyle();
        labelStyleSmall.font = Assets.instance.fonts.defaultSmall;

        // Define our labels based on labelStyle
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label gameOverLabel = new Label("GAME OVER", labelStyleBig);
        Label playAgainLabel = new Label("Back to menu", labelStyleSmall);
        table.add(gameOverLabel).center();
        table.row();
        table.add(playAgainLabel).padTop(10.0f).center();
        addActor(table);

        table.addListener(UIFactory.createListener(ScreenEnum.MAIN_MENU));
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
