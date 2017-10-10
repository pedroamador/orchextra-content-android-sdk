package com.gigigo.orchextra.ocm;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import com.gigigo.orchextra.core.controller.model.grid.ImageTransformReadArticle;
import com.gigigo.orchextra.core.domain.entities.OxCRM;
import com.gigigo.orchextra.ocm.callbacks.OcmCredentialCallback;
import com.gigigo.orchextra.ocm.callbacks.OnCustomSchemeReceiver;
import com.gigigo.orchextra.ocm.callbacks.OnRequiredLoginCallback;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import com.gigigo.orchextra.ocm.views.UiDetailBaseContentData;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;
import com.gigigo.orchextra.ocm.views.UiSearchBaseContentData;
import java.util.List;
import java.util.Map;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;

public final class Ocm {

  private static QueryStringGenerator queryStringGenerator;

  public static final String OCM_PREFERENCES = "OCMpreferencez";
  public static final String OCM_CHANGE_CREDENTIALS_DONE = "ChangeCredentialsDONE";

  public static void initialize(Application app, String apiKey, String apiSecret) {

    OcmBuilder ocmBuilder = new OcmBuilder(app);
    Class notificationActivityClass = ocmBuilder.getNotificationActivityClass();

    OCManager.initSdk(ocmBuilder.getApp());
    OCManager.setContentLanguage(ocmBuilder.getContentLanguage());
    OCManager.setDoRequiredLoginCallback(ocmBuilder.getOnRequiredLoginCallback());
    OCManager.setEventCallback(ocmBuilder.getOnEventCallback());

    OCManager.setShowReadArticles(ocmBuilder.getShowReadArticles());
    if (ocmBuilder.getShowReadArticles() && ocmBuilder.getTransformReadArticleMode()
        .equals(ImageTransformReadArticle.BITMAP_TRANSFORM)) {
      if (ocmBuilder.getCustomBitmapTransformReadArticle() == null) {
        OCManager.setBitmapTransformReadArticles(
            new GrayscaleTransformation(app.getApplicationContext()));
      } else {
        OCManager.setBitmapTransformReadArticles(ocmBuilder.getCustomBitmapTransformReadArticle());
      }
    }

    SharedPreferences prefs =
        ocmBuilder.getApp().getSharedPreferences(OCM_PREFERENCES, Context.MODE_PRIVATE);
    boolean IsCredentialsChanged = prefs.getBoolean(OCM_CHANGE_CREDENTIALS_DONE, false);

    if (!IsCredentialsChanged) {
      OCManager.initOrchextra(apiKey, apiSecret, notificationActivityClass,
          ocmBuilder.getOxSenderId());
    }
  }

  /**
   * Initialize the sdk. This method must be initialized in the onCreate method of the Application
   * class
   */
  public static void initialize(OcmBuilder ocmBuilder) {

    Application app = ocmBuilder.getApp();

    String oxKey = ocmBuilder.getOxKey();
    String oxSecret = ocmBuilder.getOxSecret();
    Class notificationActivityClass = ocmBuilder.getNotificationActivityClass();

    OCManager.initSdk(app);
    OCManager.setContentLanguage(ocmBuilder.getContentLanguage());
    OCManager.setDoRequiredLoginCallback(ocmBuilder.getOnRequiredLoginCallback());
    OCManager.setEventCallback(ocmBuilder.getOnEventCallback());

    OCManager.setShowReadArticles(ocmBuilder.getShowReadArticles());
    if (ocmBuilder.getShowReadArticles() && ocmBuilder.getTransformReadArticleMode()
        .equals(ImageTransformReadArticle.BITMAP_TRANSFORM)) {
      if (ocmBuilder.getCustomBitmapTransformReadArticle() == null) {
        OCManager.setBitmapTransformReadArticles(
            new GrayscaleTransformation(ocmBuilder.getApp().getApplicationContext()));
      } else {
        OCManager.setBitmapTransformReadArticles(ocmBuilder.getCustomBitmapTransformReadArticle());
      }
    }

    if (ocmBuilder.getShowReadArticles()) {
      OCManager.setMaxReadArticles(ocmBuilder.getMaxReadArticles());
    }

    OCManager.initOrchextra(oxKey, oxSecret, notificationActivityClass, ocmBuilder.getOxSenderId());
  }

  /**
   * Stylize the library Ui
   */
  public static void setStyleUi(OcmStyleUiBuilder ocmUiBuilder) {
    OCManager.setStyleUi(ocmUiBuilder);
  }

  /**
   * Set the language which the app content is showed
   */
  public static void setContentLanguage(String contentLanguage) {
    OCManager.setContentLanguage(contentLanguage);
  }

  /**
   * Get the app menus
   */
  public static void getMenus(boolean forceReload, OcmCallbacks.Menus menusCallback) {
    OCManager.getMenus(forceReload, new OCManagerCallbacks.Menus() {
      @Override public void onMenusLoaded(List<UiMenu> menus) {
        menusCallback.onMenusLoaded(menus);
      }

      @Override public void onMenusFails(Throwable e) {
        menusCallback.onMenusFails(e);
      }
    });
  }

  /**
   * Clear cached data
   *
   * @param clear callback
   */
  public static void clearData(boolean images, boolean data, final OCManagerCallbacks.Clear clear) {
    OCManager.clearData(images, data, new OCManagerCallbacks.Clear() {
      @Override public void onDataClearedSuccessfull() {
        clear.onDataClearedSuccessfull();
      }

      @Override public void onDataClearFails(Exception e) {
        clear.onDataClearFails(e);
      }
    });
  }

  /**
   * Return a fragment which you can add to your views.
   *
   * @param viewId It is the content url returned in the menus call.
   * @param filter To filter the content by a word
   * @param imagesToDownload Number of images that we can to download for caching
   * @param sectionCallbacks callback
   */
  public static void generateSectionView(String viewId, String filter, int imagesToDownload,
      OcmCallbacks.Section sectionCallbacks) {
    OCManager.generateSectionView(viewId, filter, imagesToDownload,
        new OCManagerCallbacks.Section() {
          @Override public void onSectionLoaded(UiGridBaseContentData uiGridBaseContentData) {
            sectionCallbacks.onSectionLoaded(uiGridBaseContentData);
          }

          @Override public void onSectionFails(Exception e) {
            sectionCallbacks.onSectionFails(e);
          }
        });
  }

  /**
   * Return a detail view which you have to add in your view. You have to specify the element url
   * to get the content.
   */
  public static UiDetailBaseContentData generateDetailView(String elementUrl) {
    return OCManager.generateDetailView(elementUrl);
  }

  /**
   * Return the search view which you have to add in your view.
   */
  public static UiSearchBaseContentData generateSearchView() {
    return OCManager.generateSearchView();
  }

  /**
   * The sdk does an action when deep link is provided and exists in dashboard
   */
  public static void processDeepLinks(String path) {
    OCManager.processDeepLinks(path);
  }

  /**
   * Set the local storage for webviews and login views
   */
  public static void setLocalStorage(Map<String, String> localStorage) {
    OCManager.setLocalStorage(localStorage);
  }

  /**
   * Provide when the user app is logged in.
   */
  public static void setUserIsAuthorizated(boolean isAuthorizated) {
    OCManager.setUserIsAuthorizated(isAuthorizated);
  }

  /**
   * Start or restart the sdk with a new credentials
   */
  public static void setOcmCredentialCallback(OcmCredentialCallback onCredentialCallback) {
    OCManager.setOcmCredentialCallback(onCredentialCallback);
  }

  /**
   * Set a business unit
   */
  public static void setBusinessUnit(String businessUnit) {
    OCManager.setOrchextraBusinessUnit(businessUnit);
  }

  /**
   * Set a custom app user
   */
  public static void bindUser(OxCRM crmUser) {
    OCManager.bindUser(crmUser);
  }

  public static void stop() {
    OCManager.stop();
  }

  public static void setOnCustomSchemeReceiver(OnCustomSchemeReceiver onCustomSchemeReceiver) {
    OCManager.setOnCustomSchemeReceiver(onCustomSchemeReceiver);
  }

  public static void closeDetailView() {
    OCManager.closeDetailView();
  }

  public static void setOnDoRequiredLoginCallback(
      OnRequiredLoginCallback onDoRequiredLoginCallback) {
    OCManager.setDoRequiredLoginCallback(onDoRequiredLoginCallback);
  }

  public static void setQueryStringGenerator(QueryStringGenerator queryStringGenerator) {
    Ocm.queryStringGenerator = queryStringGenerator;
  }

  public static QueryStringGenerator getQueryStringGenerator() {
    return Ocm.queryStringGenerator;
  }
}
