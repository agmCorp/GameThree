package uy.com.agm.gamethree.tools.actordef;

/**
 * Created by AGM on 12/14/2017.
 */

public class ActorDefMultiplicity extends ActorDef {
    private static final String TAG = ActorDefMultiplicity.class.getName();

    private int repeat;
    private boolean shoot;

    public ActorDefMultiplicity(float x, float y, int repeat, boolean shoot, Class<?> type) {
        super(x, y, type);
        this.repeat = repeat;
        this.shoot = shoot;
    }

    public int getRepeat() {
        return repeat;
    }

    public boolean getShoot() {
        return shoot;
    }
}
