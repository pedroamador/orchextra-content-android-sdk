package com.gigigo.orchextra.core.sdk.model.detail.viewtypes;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.gigigo.ggglib.device.AndroidSdkVersion;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class WebViewContentData extends UiBaseContentData {

  private static final String EXTRA_URL = "EXTRA_URL";

  private WebView webView;
  private ProgressBar progress;
  private JsHandler jsInterface;
  private boolean localStorageUpdated;

  public static WebViewContentData newInstance(String url) {
    WebViewContentData webViewElements = new WebViewContentData();

    Bundle bundle = new Bundle();
    bundle.putString(EXTRA_URL, url);
    webViewElements.setArguments(bundle);

    return webViewElements;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.view_webview_elements_item, container, false);

    webView = (WebView) view.findViewById(R.id.ocm_webView);
    progress = (ProgressBar) view.findViewById(R.id.webview_progress);

    return view;
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    init();
  }

  private void init() {
    initWebView();
    loadUrl();
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN) private void initWebView() {
    jsInterface = new JsHandler(webView);
    webView.setClickable(true);

    webView.getSettings().setJavaScriptEnabled(true);
    webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    webView.getSettings().setDomStorageEnabled(true);
    webView.getSettings().setSupportZoom(false);
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
    });
    webView.setWebViewClient(new WebViewClient() {
      @Override public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        showProgressView(false);

        setCidLocalStorage();
      }
    });

    webView.setDownloadListener(new DownloadListener() {
      @Override
      public void onDownloadStart(String url, String userAgent, String contentDisposition,
          String mimetype, long contentLength) {
        getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
      }
    });
  }

  private void setCidLocalStorage() {
    if (!localStorageUpdated) {
      Map<String, String> cidLocalStorage = OCManager.getLocalStorage();
      if (cidLocalStorage != null) {
        for (Map.Entry<String, String> element : cidLocalStorage.entrySet()) {
          final String key = element.getKey();
          final String value = element.getValue();
          String script = "window.localStorage.setItem(\'%1s\',\'%2s\')";
          //String result = jsInterface.getJSValue(this, String.format(script, new Object[]{key, value}));
          jsInterface.javaFnCall(String.format(script, new Object[] { key, value }));
        }
      }

      localStorageUpdated = true;
      webView.reload();
    }
  }

  private void showProgressView(boolean visible) {
    progress.setVisibility(visible ? View.VISIBLE : View.GONE);
  }

  private void loadUrl() {
    showProgressView(true);
    String url = getArguments().getString(EXTRA_URL);
    if (!TextUtils.isEmpty(url)) {
      webView.loadUrl(url);
    }
  }

  private class JsHandler {
    String TAG = "JsHandler";
    WeakReference<WebView> webView;
    private CountDownLatch latch = null;
    private String returnValue;

    public JsHandler(WebView _webView) {
      webView = new WeakReference<>(_webView);
    }

    /**
     * This function handles call from Android-Java
     */
    public synchronized String javaFnSyncCall(String jsString) {
      this.latch = new CountDownLatch(1);
      String code = "javascript:window.JsHandler.setValue((function(){try{return "
          + jsString
          + "+\"\";}catch(js_eval_err){return \'\';}})());";
      if (webView.get() != null) webView.get().loadUrl(code);

      try {
        this.latch.await(1L, TimeUnit.SECONDS);
        return this.returnValue;
      } catch (InterruptedException var5) {
        Log.e("JsHandler", "Interrupted", var5);
        return null;
      }
    }

    public void javaFnCall(String jsString) {
      final String webUrl = "javascript:" + jsString;
      // Add this to avoid android.view.windowmanager$badtokenexception unable to add window

      new Runnable() {
        @Override public void run() {
          if (webView.get() != null) webView.get().loadUrl(webUrl);
        }
      }.run();
    }

    @JavascriptInterface public void setValue(String value) {
      this.returnValue = value;

      try {
        this.latch.countDown();
      } catch (Exception var3) {
        ;
      }
    }
  }
}
