package com.gigigo.sample;

import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import com.gigigo.orchextra.core.controller.model.grid.ImageTransformReadArticle;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.OcmBuilder;
import com.gigigo.orchextra.ocm.OcmEvent;
import com.gigigo.orchextra.ocm.OcmStyleUiBuilder;
import com.gigigo.orchextra.ocm.callbacks.OnEventCallback;
import com.gigigo.orchextra.ocm.callbacks.OnRequiredLoginCallback;
import com.gigigo.vuforiaimplementation.ImageRecognitionVuforiaImpl;
import com.squareup.leakcanary.LeakCanary;

public class App extends MultiDexApplication {

  // DEMO
  public static final String API_KEY = "33ecdcbe03d60cb530e6ae13a531a3c9cf3c150e";
  public static final String API_SECRET = "be772ab61e2571230c596aa95237cc618023befb";

  private String apiKey = API_KEY;
  private String apiSecret = API_SECRET;

  private OnRequiredLoginCallback onDoRequiredLoginCallback = new OnRequiredLoginCallback() {
    @Override public void doRequiredLogin() {

    }
  };
  private OnEventCallback onEventCallback = new OnEventCallback() {
    @Override public void doEvent(OcmEvent event, Object data) {
    }

    @Override public void doEvent(OcmEvent event) {
    }
  };

  @Override public void onCreate() {
    enableStrictMode();
    super.onCreate();
    if (LeakCanary.isInAnalyzerProcess(this)) {
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      return;
    }
    LeakCanary.install(this);
    MultiDex.install(this);

    OcmBuilder ocmBuilder = new OcmBuilder(this).setNotificationActivityClass(MainActivity.class)
        .setShowReadArticles(true)
        .setTransformReadArticleMode(ImageTransformReadArticle.BITMAP_TRANSFORM)
        .setMaxReadArticles(100)
        .setOrchextraCredentials(API_KEY, API_SECRET)
        .setContentLanguage("EN")
        .setVuforiaImpl(new ImageRecognitionVuforiaImpl())
        .setOnEventCallback(onEventCallback);

    Ocm.initialize(ocmBuilder);

    OcmStyleUiBuilder ocmStyleUiBuilder =
        new OcmStyleUiBuilder().setTitleToolbarEnabled(true).setEnabledStatusBar(true);

    Ocm.setStyleUi(ocmStyleUiBuilder);

    Ocm.setBusinessUnit("ro");
  }

  private void enableStrictMode() {
    StrictMode.setThreadPolicy(
        new StrictMode.ThreadPolicy.Builder().detectAll()   // or .detectAll() for all detectable problems
            .penaltyLog().build());
    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().build());
  }

  @NonNull public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(@NonNull String apiKey) {
    this.apiKey = apiKey;
  }

  @NonNull public String getApiSecret() {
    return apiSecret;
  }

  public void setApiSecret(@NonNull String apiSecret) {
    this.apiSecret = apiSecret;
  }
}
