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

import uy.com.agm.gamethree.game.GameThree;

public class AndroidLauncher extends AndroidApplication implements IAdsController {
    private static final String TAG = AndroidApplication.class.getName();

    // Constants
    private static final String ADMOB_APP_ID = "ca-app-pub-3296591416050248~7409810295";
    private static final String INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3296591416050248/6643523530";
    private static final String TEST_DEVICE = "197A3D43D6743696E99BE0EE25126FF1";

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

        // Initialize Ads
        setupAds();
    }

    public void setupAds() {
        MobileAds.initialize(this, ADMOB_APP_ID);
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(INTERSTITIAL_AD_UNIT_ID);

        // Load the first interstitial
        AdRequest request = new AdRequest.Builder()
                .addTestDevice(TEST_DEVICE)
                .build();
        interstitialAd.loadAd(request);
    }

    @Override
    public void showInterstitialAd(final Runnable runCodeUIThreadOnAdClosed) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Show ad
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                } else {
                    Gdx.app.debug(TAG, "**** The interstitial wasn't loaded yet.");
                }

                if (runCodeUIThreadOnAdClosed != null) {
                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            Gdx.app.debug(TAG, "**** Ad finishes loading");
                        }

                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            Gdx.app.debug(TAG, "**** Ad request fails with errorCode: " + errorCode);
                        }

                        @Override
                        public void onAdOpened() {
                            Gdx.app.debug(TAG, "**** Ad is displayed");
                        }

                        @Override
                        public void onAdLeftApplication() {
                            Gdx.app.debug(TAG, "**** User has left the app");
                        }

                        @Override
                        public void onAdClosed() {
                            Gdx.app.debug(TAG, "**** Ad is closed");

                            // Run code on the UI Thread
                            Gdx.app.postRunnable(runCodeUIThreadOnAdClosed);

                            // Load the next interstitial
                            AdRequest request = new AdRequest.Builder()
                                    .addTestDevice(TEST_DEVICE)
                                    .build();
                            interstitialAd.loadAd(request);
                        }
                    });
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
