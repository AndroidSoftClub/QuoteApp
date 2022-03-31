package com.kessi.quotey.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.kessi.quotey.R;

public class AdManager {
    public static int adCounter = 1;
    public static int adDisplayCounter = 5;

    public static boolean isloadFbAd = true;


    public static void initAd(Context context) {
        MobileAds.initialize(context, initializationStatus -> { });
    }

    static AdView gadView;
    public static void loadBannerAd(Context context, LinearLayout adContainer) {
        gadView = new AdView(context);
        gadView.setAdUnitId(context.getString(R.string.admob_banner_id));
        adContainer.addView(gadView);
        loadBanner(context);
    }

    static void loadBanner(Context context) {
        AdRequest adRequest =
                new AdRequest.Builder().build();

        AdSize adSize = getAdSize((Activity) context);
        gadView.setAdSize(adSize);
        gadView.loadAd(adRequest);
    }

    static AdSize getAdSize(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
    }

    public static void adptiveBannerAd(Context context, LinearLayout adContainer) {
        AdView adView = new AdView(context);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.setAdSize(AdSize.LARGE_BANNER);
        adView.setAdUnitId(context.getString(R.string.admob_banner_id));
        adView.loadAd(adRequest);
        adContainer.addView(adView);
    }

    static InterstitialAd mInterstitialAd;

    public static void loadInterAd(Context context) {
        AdRequest adRequest = new AdRequest.Builder().build();
        com.google.android.gms.ads.interstitial.InterstitialAd.load(context,context.getString(R.string.admob_interstitial), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                mInterstitialAd = null;
            }
        });
    }

    public static void showInterAd(final Activity context, final Intent intent, final int requstCode) {
        if (adCounter == adDisplayCounter && mInterstitialAd != null) {
            adCounter = 1;
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when fullscreen content is dismissed.
//                    Log.d("TAG", "The ad was dismissed.");
                    loadInterAd(context);
                    startActivity(context, intent, requstCode);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
//                     Called when fullscreen content failed to show.
//                    Log.d("TAG", "The ad failed to show.");
                }


                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when fullscreen content is shown.
                    // Make sure to set your reference to null so you don't
                    // show it a second time.
                    mInterstitialAd = null;
//                    Log.d("TAG", "The ad was shown.");
                }
            });
            mInterstitialAd.show((Activity) context);
        } else {
            if (adCounter == adDisplayCounter){
                adCounter = 1;
            }
            startActivity(context, intent, requstCode);
        }
    }

    static void startActivity(Activity context, Intent intent, int requestCode) {
        if (intent != null) {
            context.startActivityForResult(intent, requestCode);
        }
    }

    static com.facebook.ads.AdView adView;

    public static void fbBannerAd(Context context, LinearLayout adContainer) {
        adView = new com.facebook.ads.AdView(context, context.getString(R.string.fbad_banner_id), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        adContainer.addView(adView);
        adView.loadAd();
    }

    static com.facebook.ads.AdView adaptiveView;

    public static void fbAdaptiveBannerAd(Context context, LinearLayout adContainer) {
        adaptiveView = new com.facebook.ads.AdView(context, context.getString(R.string.fbad_banner_id), com.facebook.ads.AdSize.BANNER_HEIGHT_90);
        adContainer.addView(adaptiveView);
        adaptiveView.loadAd();
    }

    static com.facebook.ads.InterstitialAd fbInterstitialAd;

    public static void loadFbInterAd(final Activity context) {
        fbInterstitialAd = new com.facebook.ads.InterstitialAd(context, context.getString(R.string.fbad_interstitial_id));

        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                loadFbInterAd(context);
                startActivity(context, fbIntent, fbRequstCode);
            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        fbInterstitialAd.loadAd(
                fbInterstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
    }

    static Intent fbIntent;
    static int fbRequstCode;
    public static void showFbInterAd(final Activity context, final Intent intent, final int requestCode) {
        fbIntent = intent;
        fbRequstCode = requestCode;
        if (adCounter == adDisplayCounter && fbInterstitialAd != null && fbInterstitialAd.isAdLoaded()) {
            adCounter = 1;
            fbInterstitialAd.show();
        } else {
            if (adCounter == adDisplayCounter){
                adCounter = 1;
            }
            startActivity(context, intent, requestCode);
        }
    }

    public static void destroyFbAd() {
        if (adView != null) {
            adView.destroy();
        }
        if (adaptiveView != null) {
            adaptiveView.destroy();
        }

        if (fbInterstitialAd != null) {
            fbInterstitialAd.destroy();
        }
    }


}
