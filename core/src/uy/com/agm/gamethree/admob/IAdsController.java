package uy.com.agm.gamethree.admob;

/**
 * Created by AGM on 6/17/2018.
 */

public interface IAdsController {
    public void showInterstitialAd(Runnable callbackOnAdClose);
    public boolean isWifiConnected();
}
