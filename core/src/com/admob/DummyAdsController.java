package com.admob;

/**
 * Created by AGM on 6/17/2018.
 */

public class DummyAdsController implements IAdsController {
    @Override
    public void showInterstitialAd(Runnable then) {

    }

    @Override
    public boolean isWifiConnected() {
        return false;
    }
}
