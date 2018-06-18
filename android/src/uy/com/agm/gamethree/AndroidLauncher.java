package uy.com.agm.gamethree;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.admob.IAdsController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import uy.com.agm.gamethree.game.DebugConstants;
import uy.com.agm.gamethree.game.GameThree;

public class AndroidLauncher extends AndroidApplication implements IAdsController {
    private static final String TAG = AndroidApplication.class.getName();

    private static final String ADMOB_APP_ID = "ca-app-pub-3296591416050248~7409810295";
    private static final String INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3296591416050248/6643523530";
    private static final String INTERSTITIAL_TEST_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712";
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        // We want to conserve battery
        config.useAccelerometer = false;
        config.useCompass = false;

        // Keep the screen on and hide status bar
        config.useWakelock = true;
        config.hideStatusBar = true;

        initialize(new GameThree(this), config);

        setupAds();
    }

    public void setupAds() {
        MobileAds.initialize(this, ADMOB_APP_ID);
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(DebugConstants.TEST_ADS ? INTERSTITIAL_TEST_AD_UNIT_ID : INTERSTITIAL_AD_UNIT_ID);
        // Load the first interstitial
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public void showInterstitialAd(final Runnable then) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (then != null) {
                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            Gdx.app.postRunnable(then);
                            // Load the next interstitial
                            interstitialAd.loadAd(new AdRequest.Builder().build());
                        }
                    });
                }
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                } else {
                    Gdx.app.debug(TAG, "The interstitial wasn't loaded yet.");
                }
            }
        });
    }

    @Override
    public boolean isWifiConnected() {
        boolean isConnected = false;
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
            for (int networkType : networkTypes) {
                if (activeNetworkInfo != null && activeNetworkInfo.getType() == networkType) {
                    isConnected = true;
                    break;
                }
            }
        } catch (Exception e) {
            isConnected = false;
        }
        return isConnected;
    }
}
