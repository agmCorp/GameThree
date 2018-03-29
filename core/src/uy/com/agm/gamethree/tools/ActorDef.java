package uy.com.agm.gamethree.tools;

/**
 * Created by AGM on 12/14/2017.
 */

public class ActorDef {
    private static final String TAG = ActorDef.class.getName();

    private Object userData;

    public ActorDef(Object userData) {
        this.userData = userData;
    }

    public Object getUserData() {
        return userData;
    }

    public void setUserData(Object userData) {
        this.userData = userData;
    }
}
