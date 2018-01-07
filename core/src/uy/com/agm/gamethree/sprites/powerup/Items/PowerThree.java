package uy.com.agm.gamethree.sprites.powerup.Items;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 1/6/2018.
 */

public class PowerThree extends FirePower {
    public PowerThree(PlayScreen screen, float x, float y) {
        super(screen, x, y,  Assets.instance.powerOne.powerOneAnimation, Assets.instance.explosionA.explosionAAnimation, 0.2f, 5); // todo
    }
}
