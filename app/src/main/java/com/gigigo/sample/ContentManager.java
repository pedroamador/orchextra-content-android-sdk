package com.gigigo.sample;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import com.gigigo.orchextra.core.controller.model.grid.ImageTransformReadArticle;
import com.gigigo.orchextra.ocm.OCManagerCallbacks;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.OcmBuilder;
import com.gigigo.orchextra.ocm.OcmEvent;
import com.gigigo.orchextra.ocm.OcmStyleUiBuilder;
import com.gigigo.orchextra.ocm.callbacks.OcmCredentialCallback;
import com.gigigo.orchextra.ocm.callbacks.OnEventCallback;
import com.gigigo.vuforiaimplementation.ImageRecognitionVuforiaImpl;

public class ContentManager {

  private static final String DEFAULT_API_KEY = "33ecdcbe03d60cb530e6ae13a531a3c9cf3c150e";
  private static final String DEFAULT_API_SECRET = "be772ab61e2571230c596aa95237cc618023befb";
  private static final ContentManager instance = new ContentManager();
  private static final String COUNTRY = "it";
  private Handler handler;

  public static ContentManager getInstance() {
    return instance;
  }

  private ContentManager() {
    this.handler = new Handler(Looper.getMainLooper());
  }

  public void init(Application application) {

    OcmBuilder ocmBuilder =
        new OcmBuilder(application).setNotificationActivityClass(MainActivity.class)
            .setShowReadArticles(true)
            .setTransformReadArticleMode(ImageTransformReadArticle.BITMAP_TRANSFORM)
            .setMaxReadArticles(100)
            .setOrchextraCredentials(DEFAULT_API_KEY, DEFAULT_API_SECRET)
            .setContentLanguage("EN")
            .setVuforiaImpl(new ImageRecognitionVuforiaImpl())
            .setOnEventCallback(new OnEventCallback() {
              @Override public void doEvent(OcmEvent event, Object data) {
              }

              @Override public void doEvent(OcmEvent event) {
              }
            });

    Ocm.initialize(ocmBuilder);

    OcmStyleUiBuilder ocmStyleUiBuilder =
        new OcmStyleUiBuilder().setTitleToolbarEnabled(true).setEnabledStatusBar(true);

    Ocm.setStyleUi(ocmStyleUiBuilder);
    Ocm.setBusinessUnit(COUNTRY);
  }

  public void start(ContentManagerCallback<String> callback) {
    start(DEFAULT_API_KEY, DEFAULT_API_SECRET, callback);
  }

  public void start(String apiKey, String apiSecret,
      final ContentManagerCallback<String> callback) {

    Ocm.setBusinessUnit(COUNTRY);
    Ocm.startWithCredentials(apiKey, apiSecret, new OcmCredentialCallback() {
      @Override public void onCredentialReceiver(final String accessToken) {
        handler.post(new Runnable() {
          @Override public void run() {
            callback.onSuccess(accessToken);
          }
        });
      }

      @Override public void onCredentailError(final String code) {
        handler.post(new Runnable() {
          @Override public void run() {
            callback.onError(new Exception(code));
          }
        });
      }
    });
  }

  public void clear() {
    Ocm.clearData(true, true, new OCManagerCallbacks.Clear() {
      @Override public void onDataClearedSuccessfull() {

      }

      @Override public void onDataClearFails(Exception e) {

      }
    });
  }

  public void getContent() {

  }

  public interface ContentManagerCallback<T> {

    void onSuccess(T result);

    void onError(Exception exception);
  }
}
