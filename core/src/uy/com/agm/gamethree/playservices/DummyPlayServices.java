package uy.com.agm.gamethree.playservices;

/**
 * Created by AGM on 11/24/2018.
 */
public class DummyPlayServices implements IPlayServices {
    @Override
    public void signInSilently(Runnable callbackOnSilentlyConnected, Runnable callbackOnSilentlyDisconnected) {
        // Nothing to do here
    }

    @Override
    public void signIn(Runnable callbackOnConnected, Runnable callbackOnDisconnected) {
        // Nothing to do here
    }

    @Override
    public void signOut() {
        // Nothing to do here
    }

    @Override
    public void rateGame() {
        // Nothing to do here
    }

    @Override
    public void submitScore(int highScore) {
        // Nothing to do here
    }

    @Override
    public void showLeaderboards() {
        // Nothing to do here
    }

    @Override
    public boolean isSignedIn() {
        return false;
    }

    @Override
    public boolean isWifiConnected() {
        return false;
    }

    @Override
    public void showToast(String message) {
        // Nothing to do here
    }
}
