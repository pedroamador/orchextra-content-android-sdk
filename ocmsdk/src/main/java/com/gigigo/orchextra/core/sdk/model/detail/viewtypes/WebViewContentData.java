package com.gigigo.orchextra.core.sdk.model.detail.viewtypes;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.MimeTypeMap;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.gigigo.ggglib.device.AndroidSdkVersion;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheRender;
import com.gigigo.orchextra.core.domain.entities.elementcache.FederatedAuthorization;
import com.gigigo.orchextra.core.sdk.model.grid.dto.ClipToPadding;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.federatedAuth.FAUtils;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;
import com.gigigo.orchextra.ocmsdk.R;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;

public class WebViewContentData extends UiGridBaseContentData {

  public static final int REQUEST_SELECT_FILE = 101;
  public static final int FILECHOOSER_RESULTCODE = 102;
  public static final float PADDING_CONTAINER = 400f;
  private static final String EXTRA_URL = "EXTRA_URL";
  private static final String EXTRA_FEDERATED_AUTH = "EXTRA_FEDERATED_AUTH";
  private View mView;
  private WebView webView;
  private View progress;
  private int addictionalPadding;
  private View webviewClipToPaddingContainer;
  private ValueCallback<Uri[]> uploadMessageUri;
  private ValueCallback uploadMessage;

  public static WebViewContentData newInstance(ElementCacheRender render) {
    WebViewContentData webViewElements = new WebViewContentData();

    Bundle bundle = new Bundle();
    bundle.putString(EXTRA_URL, render.getUrl());
    bundle.putSerializable(EXTRA_FEDERATED_AUTH, render.getFederatedAuth());
    webViewElements.setArguments(bundle);

    return webViewElements;
  }

  public static WebViewContentData newInstance(String url, FederatedAuthorization fa) {
    WebViewContentData webViewElements = new WebViewContentData();

    Bundle bundle = new Bundle();
    bundle.putString(EXTRA_URL, url);
    bundle.putSerializable(EXTRA_FEDERATED_AUTH, fa);
    webViewElements.setArguments(bundle);

    return webViewElements;
  }

  public static WebViewContentData newInstance(String url) {
    WebViewContentData webViewElements = new WebViewContentData();

    Bundle bundle = new Bundle();
    bundle.putString(EXTRA_URL, url);
    webViewElements.setArguments(bundle);

    return webViewElements;
  }

  public String getMimeType(String url) {
    String extension = MimeTypeMap.getFileExtensionFromUrl(url);
    if (extension != null) {
      return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    // this is to handle call from main thread
    StrictMode.ThreadPolicy prviousThreadPolicy = StrictMode.getThreadPolicy();

    // temporary allow network access main thread
    // in order to get mime type from content-type

    StrictMode.ThreadPolicy permitAllPolicy =
        new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(permitAllPolicy);

    try {
      URLConnection connection = new URL(url).openConnection();
      connection.setConnectTimeout(150);
      connection.setReadTimeout(150);
      return connection.getContentType();
    } catch (Exception ignored) {
    } finally {
      // restore main thread's default network access policy
      StrictMode.setThreadPolicy(prviousThreadPolicy);
    }

    // Our B plan: guessing from from url
    try {
      return URLConnection.guessContentTypeFromName(url);
    } catch (Exception e) {
      return null;
    }
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mView = inflater.inflate(R.layout.view_webview_detail_item, container, false);

    webView = (WebView) mView.findViewById(R.id.ocm_webView);
    progress = mView.findViewById(R.id.webview_progress);

    //webviewClipToPaddingContainer = mView.findViewById(R._id.webviewClipToPaddingContainer);
    // webView.setmScrollView((ScrollView) webviewClipToPaddingContainer);

    showProgressView(false); //avoid double loading

    return mView;
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    init();
  }

  @Override public void onDestroy() {
    if (mView != null) {
      unbindDrawables(mView);
      System.gc();
      Glide.get(this.getContext()).clearMemory();
      webView = null;
      progress = null;

      ((ViewGroup) mView).removeAllViews();
      Glide.get(this.getContext()).clearMemory();

      mView = null;
      System.gc();
    }

    super.onDestroy();
  }

  private void unbindDrawables(View view) {
    System.gc();
    Runtime.getRuntime().gc();
    if (view.getBackground() != null) {
      view.getBackground().setCallback(null);
    }
    if (view instanceof ViewGroup) {
      for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
        unbindDrawables(((ViewGroup) view).getChildAt(i));
      }
      ((ViewGroup) view).removeAllViews();
    }
  }

  private void init() {
    if (webView != null) {
      initWebView();
      loadUrl();
    }
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN) private void initWebView() {
    JsHandler jsInterface = new JsHandler(webView);
    webView.setClickable(true);

    webView.getSettings().setJavaScriptEnabled(true);
    webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    webView.getSettings().setDomStorageEnabled(true);
    webView.getSettings().setSupportZoom(true);
    webView.getSettings().setAppCacheEnabled(false);
    webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
    webView.getSettings().setDatabaseEnabled(true);
    String databasePath = this.getContext().getDir("databases", Context.MODE_PRIVATE).getPath();
    webView.getSettings().setDatabasePath(databasePath);

    if (AndroidSdkVersion.hasJellyBean16()) {
      webView.getSettings().setAllowFileAccessFromFileURLs(true);
      webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
    }

    webView.addJavascriptInterface(jsInterface, "JsHandler");

    webView.getSettings().setGeolocationDatabasePath(getContext().getFilesDir().getPath());
    webView.setWebChromeClient(new WebChromeClient() {
      public void onGeolocationPermissionsShowPrompt(String origin,
          GeolocationPermissions.Callback callback) {
        callback.invoke(origin, true, false);
      }
      // For Android 3.0+
      public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
        uploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
      }

      //For Android 4.1+ only
      protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)
      {
        uploadMessage = uploadMsg;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
      }

      protected void openFileChooser(ValueCallback<Uri> uploadMsg)
      {
        uploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
      }

      @Override
      public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
          FileChooserParams fileChooserParams) {

        if (uploadMessageUri != null) {
          uploadMessageUri.onReceiveValue(null);
          uploadMessageUri = null;
        }

        uploadMessageUri = filePathCallback;
        Intent intent = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          intent = fileChooserParams.createIntent();
        }
        try {
          startActivityForResult(intent, REQUEST_SELECT_FILE);
        } catch (ActivityNotFoundException e) {
          uploadMessageUri = null;
          Toast.makeText(webView.getContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
          return false;
        }
        return true;
      }
    });

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      WebView.setWebContentsDebuggingEnabled(true);
    }

    webView.setWebViewClient(new WebViewClient() {
      @Override public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        //asv el padding se puede hacer así xo no es correcto para todas las subpantallas que navegan la promo
        //"javascript:(function(){ document.body.style.paddingTop = '55px'})();"
        //webView.loadUrl("javascript:(function(){ document.body.style.marginBottom = '155px'})();");
        //    webView.loadUrl("javascript:document.body.style.marginBottom=\"555px\"; void 0");
        showProgressView(false);
      }

      @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {

        String mimeType = getMimeType(url);
        return launchPdfReader(Uri.parse(url), mimeType) || super.shouldOverrideUrlLoading(view,
            url);
      }

      @TargetApi(Build.VERSION_CODES.LOLLIPOP) @Override
      public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        Uri url = request.getUrl();

        String mimeType = getMimeType(url.toString());

        return launchPdfReader(url, mimeType) || super.shouldOverrideUrlLoading(view, request);
      }
    });

    webView.setDownloadListener(
        (url, userAgent, contentDisposition, mimetype, contentLength) -> getContext().startActivity(
            new Intent(Intent.ACTION_VIEW, Uri.parse(url))));

    //for avoid overlay keyboard over inputbox html webview
    webView.getSettings().setLoadWithOverviewMode(true);
    webView.getSettings().setUseWideViewPort(true);
    webView.getSettings().setBuiltInZoomControls(true);
    webView.getSettings().setDisplayZoomControls(true);

    //asv this is for play sound in some promos(guille)
    webView.getSettings().setMediaPlaybackRequiresUserGesture(false);

    this.getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    this.getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
  }

  @JavascriptInterface public void resize(final float height) {
    this.getActivity().runOnUiThread(new Runnable() {
      @Override public void run() {
        Toast.makeText(WebViewContentData.this.getActivity(), "" + height, Toast.LENGTH_SHORT)
            .show();

        webView.setLayoutParams(
            new RelativeLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels,
                (int) (height * getResources().getDisplayMetrics().density)));
      }
    });
  }

  private boolean launchPdfReader(Uri url, String mimeType) {
    if (mimeType != null && mimeType.equals("application/pdf")) {
      Intent browserIntent = new Intent(Intent.ACTION_VIEW, url);
      startActivity(browserIntent);

      return true;
    }
    return false;
  }

  //private void setCidLocalStorage() {
  //
  //  System.out.println("Main webview setCidLocalStorage");
  //  if (!localStorageUpdated && webView != null) {
  //    Map<String, String> cidLocalStorage = OCManager.getLocalStorage();
  //    if (cidLocalStorage != null) {
  //      System.out.println("Main  webview setCidLocalStorage cidLocalStorages");
  //
  //      for (Map.Entry<String, String> element : cidLocalStorage.entrySet()) {
  //        final String key = element.getKey();
  //        final String value = element.getValue();
  //        String script = "window.localStorage.setItem(\'%1s\',\'%2s\')";
  //        //String result = jsInterface.getJSValue(this, String.format(script, new Object[]{key, value}));
  //        jsInterface.javaFnCall(String.format(script, new Object[] { key, value }));
  //
  //        System.out.println(
  //            "Main webview setCidLocalStorage call js key:" + key + "value:" + value);
  //      }
  //    }
  //
  //    localStorageUpdated = true;
  //    webView.reload();
  //  }
  //}

  private void showProgressView(boolean visible) {
    if (progress != null) {
      progress.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
  }

  private void loadUrl() {

    String url = getArguments().getString(EXTRA_URL);
    // "https://register.coca-cola.com/pl/multikino?utm_source=ocm"; // getArguments().getString(EXTRA_URL);
    //  "https://register.coca-cola.com/pl/dagrasso?utm_source=ocm";// "https://www.cocacola.de/de/home/";//

    if (url != null && !url.isEmpty() && webView != null) {
      FederatedAuthorization federatedAuthorization =
          (FederatedAuthorization) getArguments().getSerializable(EXTRA_FEDERATED_AUTH);

      if (federatedAuthorization != null
          && federatedAuthorization.isActive()
          && Ocm.getQueryStringGenerator() != null) {
        Ocm.getQueryStringGenerator().createQueryString(federatedAuthorization, queryString -> {

          if (webView != null) {
            if (queryString != null && !queryString.isEmpty()) {
              String urlWithQueryParams = FAUtils.addQueryParamsToUrl(queryString, url);
              //no es necesario  OCManager.saveFedexAuth(url);
              Log.d(WebViewContentData.class.getSimpleName(),
                  "federatedAuth url: " + urlWithQueryParams);
              if (urlWithQueryParams != null) {
                showProgressView(true);
                webView.loadUrl(urlWithQueryParams);
              }
            } else {
              showProgressView(true);
              webView.loadUrl(url);
            }
          }
        });
      } else {
        showProgressView(true);
        webView.loadUrl(url);
      }
    }
  }

  @Override public void setFilter(String filter) {

  }

  @Override public void onResume() {
    super.onResume();
    setClipToPaddingBottomSize(ClipToPadding.PADDING_NONE, addictionalPadding);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == REQUEST_SELECT_FILE) {
      if (uploadMessageUri == null) return;
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        uploadMessageUri.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
      }
      uploadMessageUri = null;
    } else if (requestCode == FILECHOOSER_RESULTCODE) {
      if (null == uploadMessage)
        return;
      // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
      // Use RESULT_OK only if you're implementing WebView inside an Activity
      Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
      uploadMessage.onReceiveValue(result);
      uploadMessage = null;
    }
  }

  @Override
  public void setClipToPaddingBottomSize(ClipToPadding clipToPadding, int addictionalPadding) {
    this.addictionalPadding = addictionalPadding;

    if (webviewClipToPaddingContainer != null) {
      webviewClipToPaddingContainer.setPadding(0, 0, 0, addictionalPadding);
    }
  }

  @Override public void scrollToTop() {
    if (webView != null) {
      webView.scrollTo(0, 0);
    }
    System.out.println("SCROLL scrollToTop");
  }

  @Override public void setEmptyView(View emptyView) {

  }

  @Override public void setErrorView(View errorLayoutView) {

  }

  @Override public void setProgressView(View progressView) {
    if (progressView != null) {
      progress = progressView;
    }
  }

  @Override public void reloadSection(boolean hasToShowNewContentButton) {
    if (webView != null) {
      webView.reload();
    }
  }

  private class JsHandler {
    WebView webView;
    private CountDownLatch latch = null;
    private String returnValue;

    public JsHandler(WebView _webView) {
      this.webView = _webView;
    }

    /**
     * This function handles call from Android-Java
     */
    public synchronized String javaFnSyncCall(String jsString) {
      this.latch = new CountDownLatch(1);
      String code = "javascript:window.JsHandler.setValue((function(){try{return "
          + jsString
          + "+\"\";}catch(js_eval_err){return \'\';}})());";
      if (webView != null) webView.loadUrl(code);

      try {
        this.latch.await(1L, TimeUnit.SECONDS);
        return this.returnValue;
      } catch (InterruptedException e) {
        Log.e("JsHandler", "Interrupted", e);
        Thread.currentThread().interrupt();
        return null;
      }
    }

    public void javaFnCall(String jsString) {
      final String webUrl = "javascript:" + jsString;
      // Add this to avoid android.view.windowmanager$badtokenexception unable to add window

      if (webView != null) webView.loadUrl(webUrl);
    }

    @JavascriptInterface public void setValue(String value) {
      this.returnValue = value;

      try {
        this.latch.countDown();
      } catch (Exception ignored) {
      }
    }
  }
}
