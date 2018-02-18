package uy.com.agm.gamethree.tools.actordef;

/**
 * Created by AGM on 2/18/2018.
 */

public class ActorDefPower extends ActorDef {
    private static final String TAG = ActorDefPower.class.getName();

    private int timer;

    public ActorDefPower(float x, float y, Class<?> type) {
        super(x, y, type);
        timer = 0;
    }

    public ActorDefPower(float x, float y, int timer, Class<?> type) {
        super(x, y, type);
        this.timer = timer;
    }

    public int getTimer() {
        return timer;
    }
}
