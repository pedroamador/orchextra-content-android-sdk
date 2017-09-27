package com.gigigo.orchextra.core.sdk.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.support.customtabs.CustomTabsIntent;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Display;
import android.view.WindowManager;
import com.gigigo.ggglib.device.AndroidSdkVersion;
import com.gigigo.orchextra.core.domain.entities.elementcache.FederatedAuthorization;
import com.gigigo.orchextra.core.sdk.ui.OcmWebViewActivity;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocmsdk.R;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class DeviceUtils {

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
  public static int calculateHeightDeviceInImmersiveMode(Context context) {
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

    DisplayMetrics metrics = new DisplayMetrics();
    Display display = wm.getDefaultDisplay();

    if (AndroidSdkVersion.hasJellyBean17()) {
      display.getRealMetrics(metrics);
      return metrics.heightPixels;
    } else {
      Point size = new Point();
      display.getSize(size);
      return size.y;
    }
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
  public static int calculateRealWidthDeviceInImmersiveMode(Context context) {
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

    DisplayMetrics metrics = new DisplayMetrics();
    Display display = wm.getDefaultDisplay();

    if (AndroidSdkVersion.hasJellyBean17()) {
      display.getRealMetrics(metrics);
      return metrics.widthPixels;
    } else {
      Point size = new Point();
      display.getSize(size);
      return size.x;
    }
  }

  private static final String URL_START_QUERY_DELIMITER = "?";
  private static final String URL_CONCAT_QUERY_DELIMITER = "&";
  private static final String URL_QUERY_VALUE_DELIMITER = "=";

  private static String getQueryDelimiter(String url) {
    try {
      return new URL(url).getQuery() != null ? URL_CONCAT_QUERY_DELIMITER
          : URL_START_QUERY_DELIMITER;
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static String addQueryParamsToUrl(List<Pair<String, String>> queryParams, String url) {
    if (getQueryDelimiter(url) != null) {
      url = url + getQueryDelimiter(url);

      Iterator<Pair<String, String>> iterator = queryParams.iterator();
      while (iterator.hasNext()) {
        Pair<String, String> pair = iterator.next();
        url =
            url + pair.first + URL_QUERY_VALUE_DELIMITER + pair.second + URL_CONCAT_QUERY_DELIMITER;
      }

      return url.substring(0, url.length() - 2);
    } else {
      return null;
    }
  }

  public static void openChromeTabs(Activity activity, String url) {
    if (activity != null && url != null) {
      try {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(activity.getResources().getColor(R.color.oc_background_detail_toolbar));
        //builder.setCloseButtonIcon(BitmapFactory.decodeResource(
        //    getResources(), android.R.drawable.ic_menu_b));
        CustomTabsIntent customTabsIntent = builder.build();

        //asv hack this never work if we use other builttools like 25, now we are using 24.0.3
        // customTabsIntent.launchUrl(activity, Uri.parse(url));
        //asv this is the same that launchUrl

        customTabsIntent.intent.setData(Uri.parse(url));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
          activity.startActivity(customTabsIntent.intent);
        }
      } catch (Exception e) {
        OcmWebViewActivity.open(activity, url);
      }
    }
  }

  public static void openChromeTabs(Activity activity, String url,
      FederatedAuthorization federatedAuthorization) {
    if (activity != null && url != null) {
      try {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(activity.getResources().getColor(R.color.oc_background_detail_toolbar));
        //builder.setCloseButtonIcon(BitmapFactory.decodeResource(
        //    getResources(), android.R.drawable.ic_menu_b));
        CustomTabsIntent customTabsIntent = builder.build();

        //asv hack this never work if we use other builttools like 25, now we are using 24.0.3
        // customTabsIntent.launchUrl(activity, Uri.parse(url));
        //asv this is the same that launchUrl

        if (federatedAuthorization != null) {
          if (federatedAuthorization != null
              && federatedAuthorization.isActive()
              && Ocm.getQueryStringGenerator() != null) {

            Ocm.getQueryStringGenerator().createQueryString(federatedAuthorization, queryString -> {
              if (queryString != null && !queryString.isEmpty()) {
                String urlWithQueryParams = addQueryParamsToUrl(queryString, url);
                System.out.println(
                    "Main ContentWebViewGridLayout federatedAuth url: " + urlWithQueryParams);
                if (urlWithQueryParams != null) {
                  customTabsIntent.intent.setData(Uri.parse(urlWithQueryParams));
                } else {
                  customTabsIntent.intent.setData(Uri.parse(url));
                }
              }
            });
          } else {
            customTabsIntent.intent.setData(Uri.parse(url));
          }
        } else {
          customTabsIntent.intent.setData(Uri.parse(url));
        }

        //customTabsIntent.intent.setData(Uri.parse(url));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
          activity.startActivity(customTabsIntent.intent);
        }
      } catch (Exception e) {
        OcmWebViewActivity.open(activity, url);
      }
    }
  }

  public static int calculateWidthDevice(Context context) {
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    return size.x;
  }

  public static int calculateHeightDevice(Context context) {
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    return size.y;
  }

  private static final int MIN_RAM_MEMORY = 256;
  private static final long ONE_MB_INTO_KB = 1048576L;
  public static boolean checkDeviceHasEnoughRamMemory() {
    try {
      final Runtime runtime = Runtime.getRuntime();
      final long usedMemInMB = (runtime.totalMemory() - runtime.freeMemory()) / ONE_MB_INTO_KB;
      final long maxHeapSizeInMB = runtime.maxMemory() / ONE_MB_INTO_KB;
      final long availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB;

      return availHeapSizeInMB > MIN_RAM_MEMORY;
    } catch (Exception e) {
      return true;
    }
  }
}
