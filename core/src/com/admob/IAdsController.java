package com.admob;

/**
 * Created by AGM on 6/17/2018.
 */

public interface IAdsController {
    public void showInterstitialAd(Runnable runOnAdClose);
    public boolean isWifiConnected();
}
