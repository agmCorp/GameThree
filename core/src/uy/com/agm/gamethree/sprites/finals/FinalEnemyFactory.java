package uy.com.agm.gamethree.sprites.finals;

import uy.com.agm.gamethree.game.Constants;
import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 1/20/2018.
 */

public class FinalEnemyFactory {
    private static final String TAG = FinalEnemyFactory.class.getName();

    public static FinalEnemy getEnemy(PlayScreen screen, int level) {
        FinalEnemy finalEnemy;

        switch (level) {
            case 1:
                finalEnemy = new FinalEnemyLevelOne(screen, screen.gameCam.position.x,
                                screen.gameViewPort.getWorldHeight() * Constants.WORLD_SCREENS -
                                Constants.FINALLEVELONE_HEIGHT_METERS + Constants.FINALLEVELONE_OFFSET_METERS);
                break;
            default:
                finalEnemy = null;
                break;
        }
        return  finalEnemy;
    }
}
